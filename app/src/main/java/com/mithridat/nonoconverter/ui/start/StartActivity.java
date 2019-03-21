package com.mithridat.nonoconverter.ui.start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.mithridat.nonoconverter.R;
import com.mithridat.nonoconverter.ui.editimage.EditImageActivity;

/**
 * Class for the launcher activity. This is the first thing users see of our app.
 */
public class StartActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        findViewById(R.id.button_load_image).setOnClickListener(this);
        findViewById(R.id.button_about).setOnClickListener(this);
        findViewById(R.id.button_help).setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tbToolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_load_image:
                Intent intent = new Intent(this, EditImageActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
                break;
            case R.id.button_about:
                Toast.makeText(this,
                        "Open 'about' screen",
                        Toast.LENGTH_LONG)
                        .show();
                break;
            case R.id.button_help:
                Toast.makeText(this,
                        "Open 'help' screen",
                        Toast.LENGTH_LONG)
                        .show();
                break;
            default:
                break;
        }
    }

}
