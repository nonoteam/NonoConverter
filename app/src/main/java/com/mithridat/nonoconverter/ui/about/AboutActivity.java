package com.mithridat.nonoconverter.ui.about;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mithridat.nonoconverter.BuildConfig;
import com.mithridat.nonoconverter.R;

/**
 * Class for the about activity. This is a screen that gives the user
 * information about the application
 */
public class AboutActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);

        TextView tvVersion = (TextView) findViewById(R.id.text_view_version);
        tvVersion.setText(getString(R.string.msg_version,
                BuildConfig.VERSION_NAME));

        findViewById(R.id.button_send_email).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setTitle(R.string.title_about_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
            case R.id.button_send_email:
                String uriText =
                        "mailto:"
                                + getString(R.string.msg_email)
                                + "?subject="
                                + Uri.encode(getString(R.string.app_name))
                                + "&body="
                                + Uri.encode(getBodyForEmail(this));
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setData(Uri.parse(uriText));
                startActivity(Intent.createChooser(i,
                        getString(R.string.msg_send_email)));
                break;
            default:
                break;
        }
    }

    /**
     * Method for getting body for email
     *
     * @param ctx - the instance of the Context
     * @return body as string
     */
    private static String getBodyForEmail(Context ctx) {
        return ctx.getString(R.string.msg_toemail,
                BuildConfig.VERSION_NAME,
                Build.VERSION.RELEASE,
                Build.VERSION.SDK_INT,
                Build.MANUFACTURER,
                Build.DEVICE,
                Build.MODEL,
                Build.PRODUCT);
    }
    
}

