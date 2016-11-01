package org.feup.potter.client.log_in;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class GetCreditCardDialogFragment extends DialogFragment {

    // static constructor
    public static GetCreditCardDialogFragment newInstance() {
        GetCreditCardDialogFragment frag = new GetCreditCardDialogFragment();
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        GetCreditCardDialog dialog = new GetCreditCardDialog(getActivity());
        return dialog;
    }
}