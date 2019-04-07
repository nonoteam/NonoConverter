package com.mithridat.nonoconverter.ui.editimage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.mithridat.nonoconverter.backend.Field;
import com.mithridat.nonoconverter.backend.ImageConverter;
import com.mithridat.nonoconverter.ui.ActivitiesConstants;
import com.mithridat.nonoconverter.ui.imagepicker.ImageUpload;
import com.mithridat.nonoconverter.ui.result.ResultActivity;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;

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
     * Main fragment of the EditImageActivity
     */
    FragmentMain _fragmentMain;

    /**
     * Columns fragment of the EditImageActivity
     */
    FragmentColumns _fragmentColumns;

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
    String _path = null;

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
            _path = savedInstanceState.getString(BMP_CURRENT_IMAGE_TAG);
            setImageFromPath(_path);
        } else if (_bmpCurrentImage == null) {
            _path =
                    getIntent()
                            .getStringExtra(ActivitiesConstants.EX_IMAGE_PATH);
            setImageFromPath(_path);
        }
        if (!checkColumns()) {
            _fragmentColumns = new FragmentColumns();
        } else {
            _fragmentColumns =
                    (FragmentColumns) getSupportFragmentManager()
                            .findFragmentByTag(FRAGMENT_COLUMNS_TAG);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(COUNT_COLUMNS_TAG, _columns);
        outState.putInt(COUNT_ROWS_TAG, _rows);
        outState.putByte(IS_SELECTED_COLUMNS_TAG,
                _isSelectedColumns ? (byte) 1 : (byte) 0);
        outState.putString(BMP_CURRENT_IMAGE_TAG, _path);
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
                //for Ilya
                break;
            case R.id.button_columns:
                changeFragment(2);
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
                _path = images.get(0).getPath();
                setImageFromPath(_path);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Check if columns fragment exists
     *
     * @return true if existing
     */
    public boolean checkColumns() {
        FragmentColumns myFragment1 =
                (FragmentColumns) getSupportFragmentManager()
                .findFragmentByTag(FRAGMENT_COLUMNS_TAG);

        return myFragment1 != null;
    }

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
     * Change current fragment to the _countColumns'th fragment
     *
     * @param count - index of the fragment
     */
    public void changeFragment(int count) {
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
            case FRAGMENT_CROP:
                break;
        }
    }

    /**
     * Provides cancellation of async task execution
     * when the dialog is cancelled
     */
    OnCancelListener _diCancelListener = new OnCancelListener() {
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
    static class AsyncTaskConvertImage extends AsyncTask<Void, Void, Field> {

        /**
         * Reference to activity
         */
        private EditImageActivity _activity;

        @Override
        protected Field doInBackground(Void... params) {
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
        protected void onPostExecute(Field result) {
            if (isCancelled()) return;
            _activity._pdLoading.dismiss();
            Intent intent = new Intent(_activity, ResultActivity.class);
            intent.putExtra(ActivitiesConstants.EX_NONO_FIELD, result);
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
