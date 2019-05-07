package com.mithridat.nonoconverter.ui.result;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import static com.mithridat.nonoconverter.ui.result.ImageSaver.saveImage;
import static com.mithridat.nonoconverter.ui.result.StringKeys.NONOGRAM;
import static com.mithridat.nonoconverter.ui.result.StringKeys.THUMBNAIL;

/**
 * Class for handling result activity
 */
public class ResultActivity extends AppCompatActivity implements OnClickListener {

    /**
     * Home-return fragment.
     */
    FragmentHomeReturnDialog _fragmentHomeReturnDialog;

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
        findViewById(R.id.button_save_thumb).setOnClickListener(this);
        findViewById(R.id.button_save_nng).setOnClickListener(this);
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
            case R.id.button_save_thumb:
                path =
                        saveImage(this,
                                _nonogram.getField().getBitmap(),
                                THUMBNAIL);
                if (path == null) {
                    Toast.makeText(this,
                            R.string.msg_save_image_fail,
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(this,
                            getString(R.string.msg_save_image_done) + path,
                            Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.button_save_nng:
                path =
                        saveImage(this,
                                _nonogram.getBitmap(),
                                NONOGRAM);
                if (path == null) {
                    Toast.makeText(this,
                            R.string.msg_save_image_fail,
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(this,
                            getString(R.string.msg_save_image_done) + path,
                            Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                break;
        }
    }

}
