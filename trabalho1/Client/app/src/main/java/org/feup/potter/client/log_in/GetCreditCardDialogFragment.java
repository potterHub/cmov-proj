package org.feup.potter.client.log_in;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import org.feup.potter.client.DataStructures.CreditCard;

public class GetCreditCardDialogFragment extends DialogFragment {

    // static constructor
    public static GetCreditCardDialogFragment newInstance(CreditCard card) {
        GetCreditCardDialogFragment frag = new GetCreditCardDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("creditCard", card);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CreditCard card = (CreditCard) getArguments().getSerializable("creditCard");
        GetCreditCardDialog dialog = new GetCreditCardDialog(getActivity(),card);
        return dialog;
    }
}