package com.mithridat.nonoconverter.ui.editimage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.mithridat.nonoconverter.R;
import com.mithridat.nonoconverter.backend.ImageConverter;
import com.mithridat.nonoconverter.backend.nonogram.Nonogram;
import com.mithridat.nonoconverter.ui.ActivitiesConstants;
import com.mithridat.nonoconverter.ui.imagepicker.ImageUpload;
import com.mithridat.nonoconverter.ui.result.ResultActivity;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.provider.MediaStore.Images.Media.getBitmap;

/**
 * Class for the editing image activity.
 */
public class EditImageActivity extends AppCompatActivity implements OnClickListener {

    /**
     * Tag for fragment columns
     */
    private static final String FRAGMENT_COLUMNS_TAG = "fragmentColumns";

    /**
     * Tag for fragment main
     */
    private static final String FRAGMENT_MAIN_TAG = "fragmentMain";

    /**
     * Tag for fragment crop
     */
    private static final String FRAGMENT_CROP_TAG = "fragmentCrop";

    /**
     * Tag for fragment columns
     */
    private static final String COUNT_COLUMNS_TAG = "countColumns";

    /**
     * Tag for fragment main
     */
    private static final String COUNT_ROWS_TAG = "countRows";

    /**
     * Tag for fragment columns
     */
    private static final String IS_SELECTED_COLUMNS_TAG = "isSelectedColumns";

    /**
     * Tag for fragment main
     */
    private static final String BMP_CURRENT_IMAGE_TAG = "bmpCurrentImage";

    /**
     * Tag for fragment convert dialog
     */
    private static final String DIALOG_CONVERT_TAG = "fragmentConvertDialog";

    /**
     * Tag for fragment convert dialog
     */
    private static final String CROP_LOADED_IMAGE  = "cropLoadedImage";

    /**
     * Tag for crop area rect
     */
    private static final String RECT = "RECT";

    /**
     * Id of the main fragment
     */
    private static final int FRAGMENT_MAIN = 1;

    /**
     * Id of the columns fragment
     */
    private static final int FRAGMENT_COLUMNS = 2;

    /**
     * Id of the crop fragment
     */
    private static final int FRAGMENT_CROP = 3;

    /**
     * Progress dialog for showing processing of the converting
     */
    ProgressDialog _pdLoading;

    /**
     * Async task where image will be converted
     */
    AsyncTaskConvertImage _atConvert = null;

    /**
     * Current image in ImageView
     */
    Bitmap _bmpCurrentImage = null;

    /**
     * Current uri
     */
    Uri _uriCurrentImage = null;

    /**
     * Main fragment of the EditImageActivity
     */
    FragmentMain _fragmentMain;

    /**
     * Columns fragment of the EditImageActivity
     */
    FragmentColumns _fragmentColumns;

    /**
     * Crop fragment of the EditImageActivity
     */
    FragmentCrop _fragmentCrop;

    /**
     * Fragment manager of the EditImageActivity
     */
    FragmentTransaction _fragmentTransaction;

    /**
     * Flag for starting convert
     */
    boolean _isSelectedColumns = false;

    /**
     * Rows count
     */
    int _rows = 0;

    /**
     * Columns count
     */
    int _columns = 0;

    /**
     * Convert dialog fragment
     */
    FragmentConvertDialog _fragmentConvertDialog;

    /**
     * Path to current image
     */
    String _pathImage = null;

    /**
     * Rect of cropping area
     */
    Rect _rectCrop = null;

    /**
     * Flag for checking if we need to rewrite bitmap to file
     */
    boolean _needSaveCropped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        Toolbar toolbar = findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);

        _pdLoading = new ProgressDialog(this);
        _pdLoading.setMessage(
                getResources().getString(R.string.msg_process_image));

        _atConvert =
                (AsyncTaskConvertImage) getLastCustomNonConfigurationInstance();
        if (_atConvert != null && !_atConvert.isCancelled()
                && _atConvert.getStatus() == AsyncTask.Status.RUNNING) {
            _atConvert.link(this);
            showPd();
        }

        if (savedInstanceState != null) {
            _columns =
                    savedInstanceState
                            .getInt(COUNT_COLUMNS_TAG, 0);
            _rows =
                    savedInstanceState
                            .getInt(COUNT_ROWS_TAG, 0);
            _isSelectedColumns = savedInstanceState
                    .getByte(IS_SELECTED_COLUMNS_TAG, (byte) 0) != 0;

            _uriCurrentImage = (Uri)savedInstanceState
                    .getParcelable(CROP_LOADED_IMAGE);
            try {
                _bmpCurrentImage
                        = getBitmap(this.getContentResolver(),
                                _uriCurrentImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            _rectCrop = (Rect) savedInstanceState.getParcelable(RECT);
            _pathImage = savedInstanceState.getString(BMP_CURRENT_IMAGE_TAG);
        } else if (_bmpCurrentImage == null) {
            _pathImage =
                    getIntent()
                            .getStringExtra(ActivitiesConstants.EX_IMAGE_PATH);
            setImageFromPath(_pathImage);
        }
        if (!checkColumns()) {
            _fragmentColumns = new FragmentColumns();
        } else {
            _fragmentColumns =
                    (FragmentColumns) getSupportFragmentManager()
                            .findFragmentByTag(FRAGMENT_COLUMNS_TAG);
        }
        if (!checkCrop()) {
            _fragmentCrop = new FragmentCrop();
        } else {
            _fragmentCrop =
                    (FragmentCrop) getSupportFragmentManager()
                            .findFragmentByTag(FRAGMENT_CROP_TAG);
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            _fragmentMain = new FragmentMain();
            _fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            _fragmentTransaction.add(R.id.fragment_layout_edit,
                    _fragmentMain, FRAGMENT_MAIN_TAG);
            _fragmentTransaction.addToBackStack(null);
            _fragmentTransaction.commit();
        }
        _fragmentConvertDialog = new FragmentConvertDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            // Handle error
        } else {
            _baseLoaderCallback
                    .onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        _fragmentMain =
                (FragmentMain) getSupportFragmentManager()
                        .findFragmentByTag(FRAGMENT_MAIN_TAG);
        if (_fragmentMain != null && _fragmentMain.getView()!= null) {
            ((ImageView)_fragmentMain.getView()
                    .findViewById(R.id.image_view_main))
                    .setImageBitmap(_bmpCurrentImage);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setTitle(R.string.title_edit_image_activity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(COUNT_COLUMNS_TAG, _columns);
        outState.putInt(COUNT_ROWS_TAG, _rows);
        outState.putByte(IS_SELECTED_COLUMNS_TAG,
                _isSelectedColumns ? (byte) 1 : (byte) 0);

        outState.putString(BMP_CURRENT_IMAGE_TAG, _pathImage);
        _uriCurrentImage = writeTempStateStoreBitmap(this,
                _bmpCurrentImage, _uriCurrentImage);
        outState.putParcelable(CROP_LOADED_IMAGE, _uriCurrentImage);
        _rectCrop = _fragmentCrop.getCropRect();
        if (_rectCrop != null) {
            outState.putParcelable(RECT, _fragmentCrop.getCropRect());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_convert:
                if (!_isSelectedColumns) {
                    _fragmentConvertDialog.show(getSupportFragmentManager(),
                            DIALOG_CONVERT_TAG);
                } else {
                    showPd();
                    _atConvert = new AsyncTaskConvertImage();
                    _atConvert.link(this);
                    _atConvert.execute();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_crop:
                changeFragment(FRAGMENT_CROP);
                break;
            case R.id.button_columns:
                changeFragment(FRAGMENT_COLUMNS);
                checkColumns();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        int countFragments =
                getSupportFragmentManager().getBackStackEntryCount();

        if (countFragments == 1) {
            _rows = 0;
            _columns = 0;
            _isSelectedColumns = false;
            ImageUpload.startImagePicker(this,
                    ActivitiesConstants.RC_PICK_IMAGE_EDIT_IMAGE);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_right);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        super.onRetainCustomNonConfigurationInstance();
        if (_atConvert != null) _atConvert.unLink();
        return _atConvert;
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {
        if (requestCode == ActivitiesConstants.RC_PICK_IMAGE_EDIT_IMAGE
                && resultCode == RESULT_CANCELED) {
            super.onBackPressed();
            finish();
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_right);
        }
        if (requestCode == ActivitiesConstants.RC_PICK_IMAGE_EDIT_IMAGE
                && resultCode == RESULT_OK
                && data != null) {
            ArrayList<Image> images =
                    data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);

            if (images.size() > 0) {
                _pathImage = images.get(0).getPath();
                setImageFromPath(_pathImage);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Provides cancellation of async task execution
     * when the dialog is cancelled
     */
    private OnCancelListener _diCancelListener = new OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            if (_atConvert != null) _atConvert.cancel(true);
        }
    };

    /**
     * Callback for OpenCVLoader
     */
    private BaseLoaderCallback _baseLoaderCallback =
            new BaseLoaderCallback(this) {
                @Override
                public void onManagerConnected(int status) {
                    switch (status) {
                        default:
                            super.onManagerConnected(status);
                            break;
                    }
                }
            };

    /**
     * Set rows and columns count
     *
     * @param rows - rows count
     * @param columns - columns count
     */
    void setSizes(int rows, int columns) {
        _rows = rows;
        _columns = columns;
    }

    /**
     * Reset number of columns and rows in nonogram
     */
    void resetConvertParams() {
        _rows = 0;
        _columns = 0;
        _isSelectedColumns = false;
    }

    /**
     * Close the given closeable object (Stream) in a safe way:
     * check if it is null and catch.
     *
     * @param closeable the closable object to close
     */
    private static void closeSafe(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {}
        }
    }

    /**
     * Write given bitmap to a temp file. If file already exists no-op as we
     * already saved the file in this session. Uses JPEG 100% compression.
     *
     * @param uri the uri to write the bitmap to, if null
     * @return the uri where the image was saved in, either the given uri or
     * new pointing to temp file.
     */
    private Uri writeTempStateStoreBitmap(Context context, Bitmap bitmap, Uri uri) {
        try {
            if(uri == null) {
                uri =
                        Uri.fromFile(
                                File.createTempFile("ic_state_store_temp",
                                        ".jpg",
                                        context.getCacheDir()));
            }
            if(_needSaveCropped) {
                writeBitmapToUri(context,
                        bitmap,
                        uri,
                        Bitmap.CompressFormat.JPEG,
                        100);
                _needSaveCropped = false;
            }
            return uri;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Write the given bitmap to the given uri using the given compression.
     */
     private void writeBitmapToUri(
            Context context,
            Bitmap bitmap,
            Uri uri,
            Bitmap.CompressFormat compressFormat,
            int compressQuality)
            throws FileNotFoundException {
        OutputStream outputStream = null;
        try {
            outputStream = context.getContentResolver().openOutputStream(uri);
            bitmap.compress(compressFormat, compressQuality, outputStream);
        } finally {
            closeSafe(outputStream);
        }
    }

    /**
     * Check if columns fragment exists
     *
     * @return true if existing
     */
     private boolean checkColumns() {
        FragmentColumns myFragment1 =
                (FragmentColumns) getSupportFragmentManager()
                .findFragmentByTag(FRAGMENT_COLUMNS_TAG);

        return myFragment1 != null;
    }

    /**
     * Check if crop fragment exists
     *
     * @return true if existing
     */
    private boolean checkCrop()
    {
        FragmentCrop myFragment =
                (FragmentCrop) getSupportFragmentManager()
                        .findFragmentByTag(FRAGMENT_CROP_TAG);

        return myFragment != null;
    }

    /**
     * Change current fragment to the _countColumns'th fragment
     *
     * @param count - index of the fragment
     */
    private void changeFragment(int count) {
        switch (count) {
            case FRAGMENT_MAIN:
                _fragmentTransaction.replace(R.id.fragment_layout_edit,
                        _fragmentMain);
                break;
            case FRAGMENT_COLUMNS:
                _fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                _fragmentTransaction
                        .replace(R.id.fragment_layout_edit,
                                _fragmentColumns, FRAGMENT_COLUMNS_TAG);
                _fragmentTransaction.addToBackStack(null);
                _fragmentTransaction.commit();
                break;
            case FRAGMENT_CROP:
                _fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                _fragmentTransaction
                        .replace(R.id.fragment_layout_edit,
                                _fragmentCrop, FRAGMENT_CROP_TAG);
                _fragmentTransaction.addToBackStack(null);
                _fragmentTransaction.commit();
                break;
        }
    }

    /**
     * Show progress dialog
     */
    private void showPd() {
        _pdLoading.show();
        _pdLoading.setOnCancelListener(_diCancelListener);
    }

    /**
     * Method for upload image from path
     *
     * @param path - path of image
     */
    private void setImageFromPath(String path) {
        _bmpCurrentImage = ImageUpload.getBitmapFromPath(path);
    }

    /**
     * Inner class for async converting image in background.
     */
    static class AsyncTaskConvertImage extends AsyncTask<Void, Void, Nonogram> {

        /**
         * Reference to activity
         */
        private EditImageActivity _activity;

        @Override
        protected Nonogram doInBackground(Void... params) {
            try {
                return ImageConverter.convertImage(_activity._bmpCurrentImage,
                        _activity._rows,
                        _activity._columns,
                        this);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Nonogram result) {
            if (isCancelled()) return;
            _activity._pdLoading.dismiss();
            Intent intent = new Intent(_activity, ResultActivity.class);
            intent.putExtra(ActivitiesConstants.EX_NONO_FIELD,
                    result.getField());
            _activity.startActivity(intent);
            _activity.overridePendingTransition(R.anim.slide_in_left,
                    R.anim.slide_out_left);
        }

        /**
         * Get link to EditImageActivity
         *
         * @param act - activity which link we get
         */
        void link(EditImageActivity act) {
            _activity = act;
        }

        /**
         * Reset link
         */
        void unLink() {
            _activity = null;
        }
    }
}
