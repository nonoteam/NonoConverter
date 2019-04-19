package com.mithridat.nonoconverter.ui.result;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mithridat.nonoconverter.R;
import com.mithridat.nonoconverter.backend.nonogram.Nonogram;
import com.mithridat.nonoconverter.ui.ActivitiesConstants;
import com.mithridat.nonoconverter.ui.start.StartActivity;

/**
 * Class for handling result activity
 */
public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        findViewById(R.id.button_save_thumb).setOnClickListener(this);
        findViewById(R.id.button_save_nng).setOnClickListener(this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_result));
        NonogramView nonorgamView = findViewById(R.id.nonogram_view);
        Nonogram nonogram =
                getIntent()
                        .getParcelableExtra(ActivitiesConstants.EX_NONO_FIELD);
        if (nonorgamView != null) nonorgamView.setNonogram(nonogram);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.result, menu);
        setTitle(R.string.title_result_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                createDialogHomeReturn();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save_thumb:
            case R.id.button_save_nng:
                createDialogDone();
                break;
            default:
                break;
        }
    }

    /**
     * Create dialog with "Done" message
     */
    private void createDialogDone() {
        OnClickListener listener = new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_dialog_done)
                .setIcon(R.drawable.ic_done)
                .setPositiveButton(R.string.action_ok, listener)
                .create()
                .show();
    }

    /**
     * Create home-return dialog
     */
    private void createDialogHomeReturn() {
        OnClickListener listenerReturn = new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Intent intent = new Intent(ResultActivity.this,
                        StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_right);
            }
        };

        OnClickListener listenerCancel = new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_dialog_home_return)
                .setIcon(R.drawable.ic_info)
                .setMessage(R.string.msg_home_return)
                .setPositiveButton(R.string.action_return, listenerReturn)
                .setNegativeButton(R.string.action_cancel, listenerCancel)
                .create()
                .show();
    }
}
