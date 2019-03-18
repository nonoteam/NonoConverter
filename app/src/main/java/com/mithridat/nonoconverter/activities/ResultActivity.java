package com.mithridat.nonoconverter.activities;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mithridat.nonoconverter.R;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        findViewById(R.id.btnSaveThumb).setOnClickListener(this);
        findViewById(R.id.btnSaveNon).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_result, menu);
        setTitle(R.string.result_activity_name);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miHome:
                createDialogHomeReturn();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createDialogDone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dlg_done_title);
        builder.setIcon(R.drawable.ic_done);
        builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void createDialogHomeReturn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dlg_home_return_title);
        builder.setIcon(R.drawable.ic_info);
        builder.setMessage(R.string.dlg_home_return_message);
        builder.setPositiveButton(R.string.dlg_home_return_pos,
                new OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        onBackPressed();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel,
                new OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSaveThumb:
            case R.id.btnSaveNon:
                createDialogDone();
                break;
            default:
                break;
        }
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
