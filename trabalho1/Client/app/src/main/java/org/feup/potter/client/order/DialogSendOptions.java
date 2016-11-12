package org.feup.potter.client.order;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.feup.potter.client.R;


public class DialogSendOptions extends Dialog implements View.OnClickListener {
    public interface DialogSendOptionsListener {
        void onDoneClick(SEND_METHOD sendMethod);
    }

    public enum SEND_METHOD {
        QR, NFC, NOTHING
    }

    private DialogSendOptionsListener listener;

    public DialogSendOptions(Context context) {
        super(context);
        setOwnerActivity((Activity) context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_send_method);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button but1 = (Button) findViewById(R.id.qr_code_order);
        but1.setOnClickListener(this);

        Button but2 = (Button) findViewById(R.id.nfc_order);
        but2.setOnClickListener(this);

        this.listener = (DialogSendOptionsListener) getOwnerActivity();
    }

    @Override
    public void onClick(View v) {
        SEND_METHOD method = SEND_METHOD.NOTHING;
        switch (v.getId()) {
            case R.id.qr_code_order:
                method = SEND_METHOD.QR;
                break;
            case R.id.nfc_order:
                method = SEND_METHOD.NFC;
                break;
        }
        listener.onDoneClick(method);
        dismiss();
    }
}
