package org.feup.apm.dialogfragmentdemo;

import org.feup.apm.dialogfragmentdemo.GetNameDialog.GetNameDialogListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements GetNameDialogListener, OnClickListener{
  private TextView tv;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    Button but = (Button)findViewById(R.id.button);
    but.setOnClickListener(this);

    tv = (TextView)findViewById(R.id.name);
  }

  /* for the activity button */
  public void onClick(View v) {
    GetNameDialogFragment dialog = GetNameDialogFragment.newInstance(getString(R.string.initial_name));
    dialog.show(getFragmentManager(), "getname");
  }

  /* for the dialog button */
  public void onDoneClick(GetNameDialog dlg) {
    tv.setText("Hello, " + dlg.edt.getText().toString() + "!");
  }
}
