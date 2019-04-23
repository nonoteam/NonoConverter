package com.mithridat.nonoconverter.ui.result;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.mithridat.nonoconverter.R;
import com.mithridat.nonoconverter.backend.nonogram.Nonogram;
import com.mithridat.nonoconverter.ui.ActivitiesConstants;

import static com.mithridat.nonoconverter.ui.result.SaveImage.saveImage;
import static com.mithridat.nonoconverter.ui.result.StringKeys.DIALOG_AFTER_SAVE_TAG;
import static com.mithridat.nonoconverter.ui.result.StringKeys.THUMBNAIL;

/**
 * Class for handling result activity
 */
public class ResultActivity extends AppCompatActivity implements OnClickListener {

    /**
     * Done and Fail dialog fragments.
     */
    FragmentAfterSaveDialog _fragmentDoneDialog, _fragmentFailDialog;

    /**
     * Home-return fragment.
     */
    FragmentHomeReturnDialog _fragmentHomeReturnDialog;

    /**
     * Nonogram from backend
     */
    Nonogram _nonogram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        findViewById(R.id.button_save_thumb).setOnClickListener(this);
        findViewById(R.id.button_save_nng).setOnClickListener(this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_result));
        NonogramView nonorgamView = findViewById(R.id.nonogram_view);
        _nonogram =
                getIntent()
                        .getParcelableExtra(ActivitiesConstants.EX_NONO_FIELD);
        if (nonorgamView != null) nonorgamView.setNonogram(_nonogram);
        _fragmentDoneDialog = FragmentAfterSaveDialog.newInstance(true);
        _fragmentFailDialog = FragmentAfterSaveDialog.newInstance(false);
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
                        StringKeys.DIALOG_HOME_RETURN_TAG);
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
                boolean result =
                        saveImage(_nonogram.getField().getBitmap(), THUMBNAIL);
                showAfterSaveDialog(result);
                break;
            case R.id.button_save_nng:
                showAfterSaveDialog(false);
                break;
            default:
                break;
        }
    }

    /**
     * Method for showing dialog after saving thumbnail/nonogram
     *
     * @param success - true, if image was saved
     *                  false, otherwise
     */
    private void showAfterSaveDialog(boolean success) {
        FragmentAfterSaveDialog dialog =
                success ? _fragmentDoneDialog : _fragmentFailDialog;
        dialog.show(getSupportFragmentManager(), DIALOG_AFTER_SAVE_TAG);
    }
}
