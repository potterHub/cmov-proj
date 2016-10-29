package org.feup.potter.client.menus;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import org.feup.potter.client.db.DataBaseHelper;

public class DialogMenuDetailsFrag extends DialogFragment {
    public static DialogMenuDetailsFrag newInstance(long idItem) {
        DialogMenuDetailsFrag frag = new DialogMenuDetailsFrag();
        Bundle args = new Bundle();
        args.putSerializable("idItem", idItem + "");
        frag.setArguments(args);
        return frag;
    }

    private DataBaseHelper dataBase;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String idItem = getArguments().getString("idItem");

        DialogMenuDetails dialog = new DialogMenuDetails(getActivity(), idItem);
        return dialog;
    }
}
