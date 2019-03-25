package com.mithridat.nonoconverter.ui.start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.mithridat.nonoconverter.R;
import com.mithridat.nonoconverter.ui.ActivitiesConstants;
import com.mithridat.nonoconverter.ui.about.AboutActivity;
import com.mithridat.nonoconverter.ui.editimage.EditImageActivity;
import com.mithridat.nonoconverter.ui.imagepicker.ImageUpload;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.ArrayList;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_start);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.button_load_image:
                ImageUpload.startImagePicker(this,
                        ActivitiesConstants.RC_PICK_IMAGE_START);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
                break;
            case R.id.button_about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ActivitiesConstants.RC_PICK_IMAGE_START
                && resultCode == RESULT_OK
                && data != null) {
            ArrayList<Image> images =
                    data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);

            if (images.size() > 0)
            {
                Intent intent = new Intent(this, EditImageActivity.class);
                intent.putExtra(ActivitiesConstants.EX_IMAGE_PATH,
                        images.get(0).getPath());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
