package com.mithridat.nonoconverter.ui.result;

import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Toast;

import com.mithridat.nonoconverter.R;
import com.mithridat.nonoconverter.backend.nonogram.Nonogram;

import static com.mithridat.nonoconverter.ui.result.ImageSaver.saveImage;
import static com.mithridat.nonoconverter.ui.result.StringKeys.NONOGRAM;
import static com.mithridat.nonoconverter.ui.result.StringKeys.THUMBNAIL;
import static com.mithridat.nonoconverter.ui.ActivitiesConstants.EX_NONO_FIELD;

/**
 * Class for save dialog fragment.
 */
public class FragmentSaveDialog extends DialogFragment implements OnClickListener {

    /**
     * Nonogram from result activity.
     */
    private Nonogram _nonogram = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_save, null);
        v.findViewById(R.id.button_save_nng).setOnClickListener(this);
        v.findViewById(R.id.button_save_thumb).setOnClickListener(this);
        Activity activity = getActivity();
        if (activity != null) {
            _nonogram = activity.getIntent().getParcelableExtra(EX_NONO_FIELD);
        }
        return v;
    }

    @Override
    public void onClick(View v) {
        if (_nonogram == null) return;

        String path;
        Context context = getContext();
        switch (v.getId()) {
            case R.id.button_save_thumb:
                path =
                        saveImage(context,
                                _nonogram.getField().getBitmap(),
                                THUMBNAIL);
                if (path == null) {
                    Toast.makeText(context,
                            R.string.msg_save_image_fail,
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(context,
                            getString(R.string.msg_save_image_done) + path,
                            Toast.LENGTH_SHORT)
                            .show();
                }
                getDialog().cancel();
                break;

            case R.id.button_save_nng:
                path =
                        saveImage(context,
                                _nonogram.getBitmap(),
                                NONOGRAM);
                if (path == null) {
                    Toast.makeText(context,
                            R.string.msg_save_image_fail,
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(context,
                            getString(R.string.msg_save_image_done) + path,
                            Toast.LENGTH_SHORT)
                            .show();
                }
                getDialog().cancel();
                break;
            default:
                break;
        }
    }
}
