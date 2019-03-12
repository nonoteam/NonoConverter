package com.mithridat.nonoconverter.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.mithridat.nonoconverter.R;


public class EditImageActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        findViewById(R.id.btn_crop).setOnClickListener(this);
        findViewById(R.id.btn_columns).setOnClickListener(this);
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

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
        Window windowDialog = alertDialog.getWindow();
        if (windowDialog != null)
            windowDialog.setLayout(800,650);
    }

}