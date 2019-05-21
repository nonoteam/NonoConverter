package com.mithridat.nonoconverter.ui.result;

import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mithridat.nonoconverter.R;

/**
 * Class for share dialog fragment.
 */
public class FragmentShareDialog extends DialogFragment implements OnClickListener {

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_share, null);
        v.findViewById(R.id.button_share_nng).setOnClickListener(this);
        v.findViewById(R.id.button_share_thumb).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_share_thumb:
            case R.id.button_share_nng:
                Toast.makeText(getContext(),
                        R.string.msg_func_unavailable,
                        Toast.LENGTH_SHORT)
                        .show();
                dismiss();
                break;
            default:
                break;
        }
    }
}

