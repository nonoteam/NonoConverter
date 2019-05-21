package com.mithridat.nonoconverter.ui.result;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.fragment.app.DialogFragment;

import com.mithridat.nonoconverter.R;
import com.mithridat.nonoconverter.backend.nonogram.Nonogram;

import static com.mithridat.nonoconverter.Utils.getSnackbar;
import static com.mithridat.nonoconverter.Utils.openImage;
import static com.mithridat.nonoconverter.Utils.saveImage;
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
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
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

        switch (v.getId()) {
            case R.id.button_save_thumb:
                saveImageAction(getActivity(),
                        _nonogram.getField().getBitmap(),
                        THUMBNAIL);
                dismiss();
                break;
            case R.id.button_save_nng:
                saveImageAction(getActivity(),
                        _nonogram.getBitmap(),
                        NONOGRAM);
                dismiss();
                break;
            default:
                break;
        }
    }

    private void saveImageAction(
            final Activity activity,
            final Bitmap bitmap,
            final String title) {
        final String path = saveImage(activity, bitmap, title);
        if (path == null) {
            getSnackbar(activity,
                    getString(R.string.msg_save_image_fail),
                    R.id.coordinator).setAction(R.string.action_retry,
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveImageAction(activity, bitmap, title);
                        }
                    })
                    .show();
        } else {
            getSnackbar(activity,
                    getString(R.string.msg_save_image_done,
                            path),
                    R.id.coordinator).setAction(R.string.action_open,
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openImage(activity, path);
                        }
                    })
                    .show();
        }
    }

}
