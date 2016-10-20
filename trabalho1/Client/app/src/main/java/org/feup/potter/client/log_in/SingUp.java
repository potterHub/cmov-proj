package org.feup.potter.client.log_in;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.feup.potter.client.R;



public class SingUp extends Activity implements GetCreditCardDialog.CredidCardInterface,View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        Button but = (Button)findViewById(R.id.showdialog);
        but.setOnClickListener(this);
    }


    /* for the activity button */
    public void onClick(View v) {
        GetCreditCardDialogFragment dialog = GetCreditCardDialogFragment.newInstance();

        dialog.show(getFragmentManager(), "getcard");
    }


    /* for the dialog button */
    @Override
    public void whenCreditCardAdded(GetCreditCardDialog Dialog) {
        Log.d("credit card","done");
    }
}
