package org.feup.apm.dialogfragmentdemo;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class GetNameDialogFragment extends DialogFragment {
  public static GetNameDialogFragment newInstance(String init) {
    GetNameDialogFragment frag = new GetNameDialogFragment();
    Bundle args = new Bundle();
    args.putString("title", init);
    frag.setArguments(args);
    return frag;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    String title;
    
    title = getArguments().getString("title");
    GetNameDialog dialog = new GetNameDialog(getActivity(), title);
    return dialog;
  }
}
