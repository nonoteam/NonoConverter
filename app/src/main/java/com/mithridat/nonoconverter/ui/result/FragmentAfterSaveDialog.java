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

import static com.mithridat.nonoconverter.ui.result.StringKeys.DIALOG_TITLE;
import static com.mithridat.nonoconverter.ui.result.StringKeys.DIALOG_ICON;

/**
 * Class for done dialog fragment.
 */
public class FragmentAfterSaveDialog extends DialogFragment {

    static FragmentAfterSaveDialog newInstance(boolean success) {
        FragmentAfterSaveDialog frag = new FragmentAfterSaveDialog();
        Bundle args = new Bundle();
        args.putInt(DIALOG_TITLE,
                success ? R.string.title_dialog_done
                        : R.string.title_dialog_fail);
        args.putInt(DIALOG_ICON,
                success ? R.drawable.ic_done
                        : R.drawable.ic_error);
        frag.setArguments(args);
        return frag;
    }

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

        Bundle args = getArguments();
        int title = R.string.title_dialog_done, icon = R.drawable.ic_done;
        if (args != null) {
            title = args.getInt(DIALOG_TITLE);
            icon = args.getInt(DIALOG_ICON);
        }

        return new AlertDialog.Builder(fragmentActivity)
                .setTitle(title)
                .setIcon(icon)
                .setPositiveButton(R.string.action_ok, okListener)
                .create();
    }

}
