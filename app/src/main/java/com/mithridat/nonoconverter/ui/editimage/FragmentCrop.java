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

    private CropImageView _myView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        EditImageActivity editImageActivity =
                (EditImageActivity)getActivity();
        editImageActivity._rectCrop = null;
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
                editImageActivity._bmpCurrentImage = _myView.getCroppedImage();
                editImageActivity._needSaveCropped = true;
                editImageActivity._isCropped = true;
                editImageActivity.getSupportFragmentManager().popBackStack();
                editImageActivity.resetConvertParams();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View viewCropFragment = inflater.inflate(R.layout.fragment_crop, null);

        viewCropFragment.findViewById(R.id.button_rotate)
                .setOnClickListener(this);
        viewCropFragment.findViewById(R.id.button_flip)
                .setOnClickListener(this);

        _myView  = (CropImageView)viewCropFragment
                .findViewById(R.id.crop_image_view);
        EditImageActivity editImageActivity = (EditImageActivity)getActivity();
        _myView.setImageBitmap(ImageUpload.getBitmapFromPath(
                editImageActivity._pathImage));
        if (editImageActivity._rectCrop != null) {
            _myView.setCropRect(editImageActivity._rectCrop);
        }

        return viewCropFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_rotate:
                _myView.rotateImage(90);
                break;
            case R.id.button_flip:
                _myView.flipImageHorizontally();
                break;
            default:
                break;
        }
    }

    /**
     * Get crop area
     *
     * @return current crop area
     */
    Rect getCropRect() {
        if (_myView != null) return _myView.getCropRect();
        return null;
    }
}

