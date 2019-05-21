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
import static com.mithridat.nonoconverter.Utils.shareImage;
import static com.mithridat.nonoconverter.ui.ActivitiesConstants.EX_NONO_FIELD;
import static com.mithridat.nonoconverter.ui.result.StringKeys.NONOGRAM;
import static com.mithridat.nonoconverter.ui.result.StringKeys.THUMBNAIL;

/**
 * Class for share dialog fragment.
 */
public class FragmentShareDialog extends DialogFragment implements OnClickListener {

    /**
     * Nonogram from result activity.
     */
    private Nonogram _nonogram = null;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_share, null);
        v.findViewById(R.id.button_share_nng).setOnClickListener(this);
        v.findViewById(R.id.button_share_thumb).setOnClickListener(this);
        Activity activity = getActivity();
        if (activity != null) {
            _nonogram = activity.getIntent().getParcelableExtra(EX_NONO_FIELD);
        }
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_share_thumb:
                shareImageAction(getActivity(),
                        _nonogram.getField().getBitmap(),
                        THUMBNAIL);
                dismiss();
                break;
            case R.id.button_share_nng:
                shareImageAction(getActivity(),
                        _nonogram.getBitmap(),
                        NONOGRAM);
                dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * Method for sharing image
     *
     * @param activity - the Activity
     * @param bitmap - image for sharing
     * @param title - title of the image
     */
    private void shareImageAction(
            final Activity activity,
            final Bitmap bitmap,
            final String title) {
        final boolean result = shareImage(activity, bitmap, title);
        if (!result) {
            getSnackbar(activity,
                    getString(R.string.msg_share_image_fail),
                    R.id.coordinator).setAction(R.string.action_retry,
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shareImageAction(activity, bitmap, title);
                        }
                    })
                    .show();
        }
    }

}

