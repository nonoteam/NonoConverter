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

import com.mithridat.nonoconverter.R;
import com.mithridat.nonoconverter.ui.result.ResultActivity;

import java.util.concurrent.TimeUnit;


public class EditImageActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog progressDialog;
    AsyncTaskTransition atTransition = null;

    OnCancelListener pdCancelListener = new OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            if (atTransition != null)
                atTransition.cancel(true);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        findViewById(R.id.btn_crop).setOnClickListener(this);
        findViewById(R.id.btn_columns).setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(
            getResources().getString(R.string.pd_process_image)
        );

        atTransition = (AsyncTaskTransition) getLastCustomNonConfigurationInstance();
        if (atTransition != null && !atTransition.isCancelled() &&
            atTransition.getStatus() == AsyncTask.Status.RUNNING) {

            atTransition.link(this);
            progressDialog.show();
            progressDialog.setOnCancelListener(pdCancelListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_edit_image, menu);
        setTitle(R.string.edit_image_activity_name);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miConvert:
                progressDialog.show();
                progressDialog.setOnCancelListener(pdCancelListener);
                atTransition = new AsyncTaskTransition();
                atTransition.link(this);
                atTransition.execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_crop:
                break;
            case R.id.btn_columns:
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

    private void createDialogColumns() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        builder.setView(inflater.inflate(R.layout.dialog_columns, null))
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO: save columns and rows
                    }
                });

        builder.create().show();
    }

    public Object onRetainCustomNonConfigurationInstance() {
        if (atTransition != null)
            atTransition.unLink();
        return atTransition;
    }

    static class AsyncTaskTransition extends AsyncTask<Void, Void, Void> {

        private EditImageActivity activity;

        void link(EditImageActivity act) {
            activity = act;
        }

        void unLink() {
            activity = null;
        }

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
            activity.progressDialog.dismiss();
            Intent intent = new Intent(activity, ResultActivity.class);
            activity.startActivity(intent);
            activity.overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_left
            );
        }
    }

}
