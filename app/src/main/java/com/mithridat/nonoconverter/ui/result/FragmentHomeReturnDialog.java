package com.mithridat.nonoconverter.ui.result;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;

import com.mithridat.nonoconverter.R;
import com.mithridat.nonoconverter.ui.start.StartActivity;

/**
 * Class for home-return dialog fragment.
 */
public class FragmentHomeReturnDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final FragmentActivity activity = getActivity();
        if (activity == null) {
            return super.onCreateDialog(savedInstanceState);
        }

        OnClickListener listenerReturn = new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                Intent intent = new Intent(activity, StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_right);
            }
        };

        OnClickListener listenerCancel = new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        };

        return new AlertDialog.Builder(activity)
                .setTitle(R.string.title_dialog_home_return)
                .setIcon(R.drawable.ic_info)
                .setMessage(R.string.msg_home_return)
                .setPositiveButton(R.string.action_return, listenerReturn)
                .setNegativeButton(R.string.action_cancel, listenerCancel)
                .create();
    }
}
