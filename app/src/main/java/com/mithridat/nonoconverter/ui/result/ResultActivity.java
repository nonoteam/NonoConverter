package com.mithridat.nonoconverter.ui.result;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.mithridat.nonoconverter.R;
import com.mithridat.nonoconverter.backend.nonogram.Nonogram;
import com.mithridat.nonoconverter.ui.ActivitiesConstants;
import com.mithridat.nonoconverter.ui.result.nonogramDrawable.IDrawable;
import com.mithridat.nonoconverter.ui.result.nonogramDrawable.NonogramDrawableExtended;

/**
 * Class for handling result activity
 */
public class ResultActivity extends AppCompatActivity implements OnClickListener {

    /**
     * Home-return fragment dialog.
     */
    DialogFragment _fragmentHomeReturnDialog;

    /**
     * Save fragment dialog.
     */
    DialogFragment _fragmentSaveDialog;

    /**
     * Share fragment dialog.
     */
    DialogFragment _fragmentShareDialog;

    /**
     * Nonogram from backend
     */
    Nonogram _nonogram;

    /**
     * Nonogram as drawable for ResultImageView
     */
    IDrawable _nonogramDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        findViewById(R.id.button_save).setOnClickListener(this);
        findViewById(R.id.button_share).setOnClickListener(this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_result));
        ResultImageView resultImageView = findViewById(R.id.result_image_view);
        _nonogram =
                getIntent()
                        .getParcelableExtra(ActivitiesConstants.EX_NONO_FIELD);
        _nonogramDrawable = new NonogramDrawableExtended(_nonogram);
        if (resultImageView != null) {
            resultImageView.setDrawable(_nonogramDrawable);
        }
        _fragmentHomeReturnDialog = new FragmentHomeReturnDialog();
        _fragmentSaveDialog = new FragmentSaveDialog();
        _fragmentShareDialog = new FragmentShareDialog();
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
        String path = null;
        switch (v.getId()) {
            case R.id.button_save:
                if (checkFragmentAbsence(StringKeys.DIALOG_SAVE_TAG)) {
                    _fragmentSaveDialog.show(getSupportFragmentManager(),
                            StringKeys.DIALOG_SAVE_TAG);
                }
                break;
            case R.id.button_share:
                if (checkFragmentAbsence(StringKeys.DIALOG_SHARE_TAG)) {
                    _fragmentShareDialog.show(getSupportFragmentManager(),
                            StringKeys.DIALOG_SHARE_TAG);
                }
                break;
            default:
                break;
        }
    }

    /**
     * Function for checking absence of the fragment by tag.
     * @param tag - tag of the fragment
     * @return false if fragment exists and true otherwise
     */
    private boolean checkFragmentAbsence(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag) == null;
    }

}
