package com.mithridat.nonoconverter.ui.editimage;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mithridat.nonoconverter.R;
import com.mithridat.nonoconverter.ui.imagepicker.ImageUpload;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.fragment.app.Fragment;

public class FragmentCrop extends Fragment implements View.OnClickListener {
    private CropImageView myView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_image_crop, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done:
                EditImageActivity editImageActivity =
                        (EditImageActivity)getActivity();
                editImageActivity._bmpCurrentImage = myView.getCroppedImage();
                editImageActivity._needSaveCropped = true;
                editImageActivity.getSupportFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View _viewMainFragment = inflater.inflate(R.layout.fragment_crop,
                null);

        _viewMainFragment.findViewById(R.id.button_rotate)
                .setOnClickListener(this);
        _viewMainFragment.findViewById(R.id.button_flip)
                .setOnClickListener(this);

        myView  = (CropImageView)_viewMainFragment
                .findViewById(R.id.cropImageView);
        EditImageActivity editImageActivity = (EditImageActivity)getActivity();
        myView.setImageBitmap(ImageUpload.getBitmapFromPath(
                editImageActivity._pathImage));
        if (editImageActivity._rectCrop != null) {
            myView.setCropRect(editImageActivity._rectCrop);
        }

        return _viewMainFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_rotate:
                myView.rotateImage(90);
                break;
            case R.id.button_flip:
                myView.flipImageHorizontally();
                break;
            default:
                break;
        }
    }

    Rect getCropRect() {
        if (myView != null) return myView.getCropRect();
        return null;
    }

}
