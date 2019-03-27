package com.mithridat.nonoconverter.ui.help;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.mithridat.nonoconverter.R;
import com.mithridat.nonoconverter.ui.ActivitiesConstants;

public class HelpActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        findViewById(R.id.button_help_load_image).setOnClickListener(this);
        findViewById(R.id.button_help_edit_image).setOnClickListener(this);
        findViewById(R.id.button_help_result).setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_help);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setTitle(R.string.title_help);
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
            case R.id.button_help_load_image:
                startSectionActivity(R.string.title_help_section_load_image);
                break;
            case R.id.button_help_edit_image:
                startSectionActivity(R.string.title_help_section_edit_image);
                break;
            case R.id.button_help_result:
                startSectionActivity(R.string.title_help_section_result);
                break;
            default:
                break;
        }
    }

    private void startSectionActivity(int id) {
        Intent intent = new Intent(this, HelpSectionActivity.class);
        intent.putExtra(ActivitiesConstants.EX_HELP_SECTION_ID, id);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_left);
    }
}
