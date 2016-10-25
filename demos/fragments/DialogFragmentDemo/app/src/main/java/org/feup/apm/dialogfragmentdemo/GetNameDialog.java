package org.feup.apm.dialogfragmentdemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;

public class GetNameDialog extends Dialog implements OnClickListener{
  public interface GetNameDialogListener {
    public void onDoneClick(GetNameDialog dlg);
  }
  
  EditText edt;
  String initialHint;
  GetNameDialogListener listener;

  public GetNameDialog(Context context, String init) {
	  super(context);
	  setTitle(R.string.dialog_title);
    setOwnerActivity((Activity)context);
    initialHint = init;
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog);
    getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	  edt = (EditText)findViewById(R.id.yourname);
	  edt.setHint(initialHint);
	  Button okbut = (Button)findViewById(R.id.getnameokbutton);
	  listener = (GetNameDialogListener) getOwnerActivity();
	  okbut.setOnClickListener(this);
  }

  /* for the dialog button */
  @Override
  public void onClick(View v) {
    listener.onDoneClick(this);
    dismiss();
  }
}
