package com.mithridat.nonoconverter.ui.result;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mithridat.nonoconverter.R;
import com.mithridat.nonoconverter.backend.nonogram.Nonogram;

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
                shareImageAction(getContext(),
                        _nonogram.getField().getBitmap(),
                        THUMBNAIL);
                dismiss();
                break;
            case R.id.button_share_nng:
                shareImageAction(getContext(),
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
     * @param context - the Context
     * @param bitmap - image for sharing
     * @param title - title of the image
     */
    private static void shareImageAction(
            final Context context,
            final Bitmap bitmap,
            final String title) {
        final boolean result = shareImage(context, bitmap, title);
        if (!result) {
            Toast.makeText(context,
                    R.string.msg_share_image_fail,
                    Toast.LENGTH_LONG)
                    .show();
        }
    }
}

