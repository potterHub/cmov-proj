package org.feup.potter.terminal;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.feup.potter.terminal.serverConnection.DoOrder;

public class ShowOrder extends Activity {
    protected DoOrder connApi;

    protected LunchAppData data;

    //protected DataBaseHelper DB;

    private TextView text_id;
    private TextView text_total_p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sale);

        this.data = (LunchAppData) getApplicationContext();

        // this.DB = new DataBaseHelper(this);

        //this.connectToServer();
        Bundle extras = getIntent().getExtras();
        String totalP = extras.getString("totalP");
        String idSale = extras.getString("id");

        this.text_id = (TextView) findViewById(R.id.text_id_sale);
        this.text_id.setText(idSale);

        this.text_total_p = (TextView) findViewById(R.id.text_total_price);
        this.text_total_p.setText(totalP);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // DB.close();
    }

    public void done(View view){
        finish();
    }

}
