package org.feup.potter.client.order;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.feup.potter.client.LunchAppData;
import org.feup.potter.client.R;
import org.feup.potter.client.order.qr_nfc.GenerateQRCodeActivity;
import org.feup.potter.client.order.qr_nfc.NFCtranmitor;

public class EnterPinActivity extends Activity implements View.OnClickListener, DialogSendOptions.DialogSendOptionsListener {
    private TextView text_pin;
    private EditText edit_pin;
    private String userpin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);

        LunchAppData data = (LunchAppData) getApplicationContext();
        this.userpin = data.user.getPin();

        text_pin = (TextView) findViewById(R.id.text_inform_user_pin);
        edit_pin = (EditText) findViewById(R.id.enter_pin_edit_view);

        Button but2 = (Button) findViewById(R.id.ok_button);
        but2.setOnClickListener(this);
    }


    // create the option toast menu (main.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.order_option, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    // when the menu is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.next_icon:
                creatDialogSend();
                return (true);
            case R.id.back_icon:
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }

    /* for the credit card button button */
    public void onClick(View v) {
        creatDialogSend();
    }

    private void creatDialogSend() {
        String pin = edit_pin.getText().toString();
        if (pin.equals(this.userpin)) {
            DialogSendOptionsFrag dialog = DialogSendOptionsFrag.newInstance();
            dialog.show(getFragmentManager(), "set_method");
        }else{
            this.text_pin.setText("Wrong pin...");
            this.text_pin.setTextColor(Color.parseColor("#7c0909"));
        }
    }

    @Override
    public void onDoneClick(DialogSendOptions.SEND_METHOD sendMethod) {
        switch (sendMethod){
            case NFC:
                Intent intent = new Intent(getApplicationContext(), NFCtranmitor.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case QR:
                Intent intente_2 = new Intent(getApplicationContext(), GenerateQRCodeActivity.class);
                intente_2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intente_2);
                break;
            case NOTHING:
                break;
        }
    }
}
