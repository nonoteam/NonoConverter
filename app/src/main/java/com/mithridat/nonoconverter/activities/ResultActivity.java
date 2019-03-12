package com.mithridat.nonoconverter.activities;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private void createDialogDone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dlg_done_title);
        builder.setIcon(R.drawable.ic_done);
        builder.setPositiveButton(R.string.dlg_done_ok, new OnClickListener() {
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
}
