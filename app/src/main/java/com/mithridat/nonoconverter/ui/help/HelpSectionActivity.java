package com.mithridat.nonoconverter.ui.help;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;

import com.mithridat.nonoconverter.R;
import com.mithridat.nonoconverter.ui.ActivitiesConstants;

public class HelpSectionActivity extends AppCompatActivity {

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_section);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_help_section);
        setSupportActionBar(toolbar);

        ScrollView svSection =
                (ScrollView) findViewById(R.id.scroll_view_help_section);
        id =
                getIntent().getIntExtra(ActivitiesConstants.EX_HELP_SECTION_ID,
                        R.string.title_help_section_load_image);
        switch (id) {
            case R.string.title_help_section_result:
                getLayoutInflater().inflate(R.layout.partial_help_result,
                        svSection);
                break;
            case R.string.title_help_section_edit_image:
                getLayoutInflater().inflate(R.layout.partial_help_edit_image,
                        svSection);
                break;
            case R.string.title_help_section_load_image:
            default:
                getLayoutInflater().inflate(R.layout.partial_help_load_image,
                        svSection);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setTitle(id);
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
}
