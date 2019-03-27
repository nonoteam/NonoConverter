package com.mithridat.nonoconverter.ui.editimage;

import android.app.AlertDialog;
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

import android.view.LayoutInflater;
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
     * ImageView in the activity
     */
    ImageView _ivImage;

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
     * Tag for fragment columns
     */
    static final String _fragmentColumnsTag = "_fragmentColumns";

    /**
     * Tag for fragment main
     */
    static final String _fragmentMainTag = "_fragmentMain";

    int _rows = 45;
    int _columns = 35;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit);
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

        if (_bmpCurrentImage == null) {
            String path =
                    getIntent().getStringExtra(ActivitiesConstants.EX_IMAGE_PATH);
            setImageFromPath(path);
        }

        if (!checkColumns())
        {
            _fragmentColumns = new FragmentColumns();
        }
        else
        {
            _fragmentColumns =
                    (FragmentColumns) getSupportFragmentManager()
                            .findFragmentByTag(_fragmentColumnsTag);
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            _fragmentMain = new FragmentMain();
            _fragmentTransaction = getSupportFragmentManager().beginTransaction();
            _fragmentTransaction.add(R.id.fragment_layout_edit, _fragmentMain, _fragmentMainTag);
            _fragmentTransaction.addToBackStack(null);
            _fragmentTransaction.commit();
        }
    }

    /**
     * Check if columns fragment exists
     * @return true if existing
     */
    public boolean checkColumns()
    {
        FragmentColumns myFragment1 = (FragmentColumns) getSupportFragmentManager()
                .findFragmentByTag(_fragmentColumnsTag);
        if (myFragment1 != null) {
            return true;
        }
        return false;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            // Handle error
        } else {
            _baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setTitle(R.string.title_edit_image_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_convert:
                showPd();
                _atConvert = new AsyncTaskConvertImage();
                _atConvert.link(this);
                _atConvert.execute();
                return true;
            case R.id.menu_done:

                getSupportFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_crop:

                break;
            case R.id.button_columns:
                EditImageActivity editImageActivity = (EditImageActivity)this;
                editImageActivity.changeFragment(2);
                checkColumns();
                break;
            default:
                break;
        }
    }

    void setSizes(int rows, int columns) {
        _rows = rows;
        _columns = columns;
    }

    /**
     * Change current fragment to the _count_columns'th fragment
     * @param count index of the fragment
     */
    public void changeFragment(int count)
    {
        switch (count)
        {
            case 1:
                _fragmentTransaction.replace(R.id.fragment_layout_edit, _fragmentMain);
                break;
            case 2:
                _fragmentTransaction =  getSupportFragmentManager().beginTransaction();
                _fragmentTransaction
                        .replace(R.id.fragment_layout_edit,
                                _fragmentColumns,
                                _fragmentColumnsTag);
                _fragmentTransaction.addToBackStack(null);
                _fragmentTransaction.commit();
            case 3:
                break;
        }


    }

    @Override
    public void onBackPressed() {
        int count_fragments = getSupportFragmentManager().getBackStackEntryCount();

        if (count_fragments == 1) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ActivitiesConstants.RC_PICK_IMAGE_EDIT_IMAGE
                && resultCode == RESULT_CANCELED) {
            super.onBackPressed();
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
        if (requestCode == ActivitiesConstants.RC_PICK_IMAGE_EDIT_IMAGE
                && resultCode == RESULT_OK
                && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);

            if (images.size() > 0) {
                String path = images.get(0).getPath();
                setImageFromPath(path);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
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
    private BaseLoaderCallback _baseLoaderCallback = new BaseLoaderCallback(this) {
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
     * Create dialog for choosing number of columns or rows
     */
    private void createDialogColumns() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        builder.setView(inflater.inflate(R.layout.dialog_columns, null))
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(R.string.action_change,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO: save columns and rows
                    }
                })
                .create()
                .show();
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
