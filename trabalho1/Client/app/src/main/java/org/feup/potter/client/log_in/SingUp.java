package org.feup.potter.client.log_in;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;

import org.feup.potter.client.DataStructures.CreditCard;
import org.feup.potter.client.R;



public class SingUp extends Activity implements GetCreditCardDialog.CredidCardInterface,View.OnClickListener {

    // layout edit texts
    private EditText text_name;
    private EditText text_user_name;
    private EditText text_password;
    private CreditCard card;

    private CheckBox checkCreditCard;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        this.text_name = (EditText) findViewById(R.id.edit_name);
        this.text_user_name= (EditText) findViewById(R.id.edit_username);
        this.text_password = (EditText) findViewById(R.id.edit_password);
        this.card = null;

        this.checkCreditCard = (CheckBox) findViewById(R.id.check_credit_card);

        Button but = (Button)findViewById(R.id.button_field_card);
        but.setOnClickListener(this);
    }

    /* for the credit card button button */
    public void onClick(View v) {
        GetCreditCardDialogFragment dialog = GetCreditCardDialogFragment.newInstance();

        dialog.show(getFragmentManager(), "getcard");
    }

    public void sign_up(View view){
        boolean isValid = false;

        String name = this.text_name.getText().toString();
        String username = this.text_user_name.getText().toString();
        String password = this.text_password.getText().toString();

        if(this.checkCreditCard.isChecked()){
            Log.d("","Valid credit card...");
        }


        if(isValid){
            // valid credit card validate data and continue
        }else{
            // error message to user
        }
    }


    /* for the dialog button */
    @Override
    public void whenCreditCardAdded(CreditCard card) {
        if(card.isValid()){
            this.card = card;
            this.checkCreditCard.setChecked(true);
        }else{
            this.card = null;
            this.checkCreditCard.setChecked(false);
        }
    }
}
