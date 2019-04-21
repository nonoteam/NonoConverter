package com.mithridat.nonoconverter.ui.result;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.mithridat.nonoconverter.R;

/**
 * Class for done dialog fragment.
 */
public class FragmentDoneDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity == null) {
            return super.onCreateDialog(savedInstanceState);
        }

        OnClickListener okListener = new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        };

        return new AlertDialog.Builder(fragmentActivity)
                .setTitle(R.string.title_dialog_done)
                .setIcon(R.drawable.ic_done)
                .setPositiveButton(R.string.action_ok, okListener)
                .create();
    }

}
