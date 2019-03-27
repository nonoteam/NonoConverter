package com.mithridat.nonoconverter.ui.editimage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;

import com.mithridat.nonoconverter.R;


/**
 * Class for the main fragment
 */
public class FragmentMain extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View _viewMainFragment = inflater.inflate(R.layout.fragment_main, null);

        _viewMainFragment
                .findViewById(R.id.button_crop)
                .setOnClickListener((View.OnClickListener) getActivity());
        _viewMainFragment
                .findViewById(R.id.button_columns)
                .setOnClickListener((View.OnClickListener) getActivity());

        EditImageActivity editImageActivity = (EditImageActivity)getActivity();
        ImageView ivMain = (ImageView)_viewMainFragment.findViewById(R.id.image_view_main);
        ivMain.setImageBitmap(editImageActivity._bmpCurrentImage);

        return _viewMainFragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_image_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
