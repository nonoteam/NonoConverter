package com.mithridat.nonoconverter.ui.editimage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.mithridat.nonoconverter.R;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

public class FragmentConvertDialog extends DialogFragment{

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity == null) return null;
        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(fragmentActivity)
                .setTitle(R.string.title_dialog_convert)
                .setIcon(R.drawable.ic_info)
                .setMessage(R.string.msg_convert_ok)
                .setPositiveButton(R.string.action_ok, listenerOk);
        return builder.create();
    }

    private DialogInterface.OnClickListener listenerOk = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    };

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
