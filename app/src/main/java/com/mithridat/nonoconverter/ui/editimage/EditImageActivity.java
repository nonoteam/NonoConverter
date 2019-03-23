package com.mithridat.nonoconverter.ui.editimage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.mithridat.nonoconverter.R;
import com.mithridat.nonoconverter.ui.result.ResultActivity;

import java.util.concurrent.TimeUnit;

/**
 * Class for the editing image activity.
 */
public class EditImageActivity extends AppCompatActivity implements OnClickListener {
    ProgressDialog _pdLoading;
    AsyncTaskConvertImage _atConvert = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        findViewById(R.id.button_crop).setOnClickListener(this);
        findViewById(R.id.button_columns).setOnClickListener(this);

        _pdLoading = new ProgressDialog(this);
        _pdLoading.setMessage(
                getResources().getString(R.string.msg_process_image));

        _atConvert =
                (AsyncTaskConvertImage) getLastCustomNonConfigurationInstance();
        if (_atConvert != null && !_atConvert.isCancelled()
                && _atConvert.getStatus() == AsyncTask.Status.RUNNING) {
            _atConvert.link(this);
            _pdLoading.show();
            _pdLoading.setOnCancelListener(_diCancelListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_edit_image, menu);
        setTitle(R.string.title_edit_image_activity);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miConvert:
                _pdLoading.show();
                _pdLoading.setOnCancelListener(_diCancelListener);
                _atConvert = new AsyncTaskConvertImage();
                _atConvert.link(this);
                _atConvert.execute();
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
                createDialogColumns();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_right);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        super.onRetainCustomNonConfigurationInstance();
        if (_atConvert != null) _atConvert.unLink();
        return _atConvert;
    }

    /**
     * Provides cancellation of asynch task execution when the dialog is cancelled
     */
    OnCancelListener _diCancelListener = new OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            if (_atConvert != null) _atConvert.cancel(true);
        }
    };

    /**
     * Create dialog for choosing number of columns or rows
     */
    private void createDialogColumns() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        builder.setView(inflater.inflate(R.layout.dialog_columns, null))
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Change",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO: save columns and rows
                    }
                })
                .create()
                .show();
    }

    static class AsyncTaskConvertImage extends AsyncTask<Void, Void, Void> {
        private EditImageActivity _activity;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (int i = 0; i < 5; ++i) {
                    if (isCancelled()) return null;
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (isCancelled()) return;
            _activity._pdLoading.dismiss();
            Intent intent = new Intent(_activity, ResultActivity.class);
            _activity.startActivity(intent);
            _activity.overridePendingTransition(R.anim.slide_in_left,
                    R.anim.slide_out_left);
        }

        /**
         * Get link to EditImageActivity
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
