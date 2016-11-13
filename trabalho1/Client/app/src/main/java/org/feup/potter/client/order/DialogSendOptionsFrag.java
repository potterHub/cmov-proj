package org.feup.potter.client.order;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;


public class DialogSendOptionsFrag extends DialogFragment {
    public static DialogSendOptionsFrag newInstance() {
        return new DialogSendOptionsFrag();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DialogSendOptions(getActivity());
    }
}
