package com.mithridat.nonoconverter.ui.editimage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.mithridat.nonoconverter.R;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/**
 * Class for convert dialog
 */
public class FragmentConvertDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_dialog_convert)
                .setIcon(R.drawable.ic_error)
                .setMessage(R.string.msg_convert)
                .setPositiveButton(R.string.action_ok, listenerOk)
                .create();
    }

    private DialogInterface.OnClickListener listenerOk =
            new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    };
}
