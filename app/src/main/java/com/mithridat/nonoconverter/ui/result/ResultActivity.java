package com.mithridat.nonoconverter.ui.result;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mithridat.nonoconverter.R;
import com.mithridat.nonoconverter.backend.nonogram.Nonogram;
import com.mithridat.nonoconverter.ui.ActivitiesConstants;

/**
 * Class for handling result activity
 */
public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Done dialog fragment.
     */
    FragmentDoneDialog _fragmentDoneDialog;

    /**
     * Home-return fragment.
     */
    FragmentHomeReturnDialog _fragmentHomeReturnDialog;

    /**
     * Tag for fragment done dialog.
     */
    private static final String DIALOG_DONE_TAG = "fragmentDoneDialog";

    /**
     * Tag for fragment home-return dialog.
     */
    private static final String DIALOG_HOME_RETURN_TAG = "fragmentHomeReturnDialog";

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
        _fragmentDoneDialog = new FragmentDoneDialog();
        _fragmentHomeReturnDialog = new FragmentHomeReturnDialog();
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
                _fragmentHomeReturnDialog.show(getSupportFragmentManager(),
                        DIALOG_HOME_RETURN_TAG);
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
                _fragmentDoneDialog.show(getSupportFragmentManager(),
                        DIALOG_DONE_TAG);
                break;
            default:
                break;
        }
    }
}
