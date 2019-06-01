package com.mithridat.nonoconverter.ui.editimage;

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

/**
 * Class for the fragment for cropping of the image
 */
public class FragmentCrop extends Fragment implements View.OnClickListener {

    /**
     * Tag for saving state of the CropImageView
     */
    private static final String CROP_VIEW = "CROP_VIEW";

    /**
     * The instance of the CropImageView
     */
    private CropImageView _civCrop;

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
                editImageActivity._bmpCurrentImage = _civCrop.getCroppedImage();
                editImageActivity._needSaveCropped = true;
                editImageActivity._isCropped = true;
                editImageActivity._cropState = _civCrop.onSaveInstanceState();
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

        _civCrop =
                (CropImageView) viewCropFragment
                        .findViewById(R.id.crop_image_view);
        EditImageActivity editImageActivity = (EditImageActivity)getActivity();
        if (savedInstanceState != null) {
            _civCrop.onRestoreInstanceState(
                    savedInstanceState.getParcelable(CROP_VIEW));
        } else if (editImageActivity._cropState != null) {
            _civCrop.onRestoreInstanceState(editImageActivity._cropState);
        }
        _civCrop.setImageBitmap(ImageUpload.getBitmapFromPath(
                editImageActivity._pathImage));

        return viewCropFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_rotate:
                _civCrop.rotateImage(90);
                break;
            case R.id.button_flip:
                _civCrop.flipImageHorizontally();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CROP_VIEW, _civCrop.onSaveInstanceState());
    }

}

