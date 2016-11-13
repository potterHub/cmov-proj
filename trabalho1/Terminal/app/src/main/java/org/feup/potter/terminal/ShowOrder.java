package org.feup.potter.terminal;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ShowOrder extends Activity {
    protected LunchAppData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sale);

        this.data = (LunchAppData) getApplicationContext();

        Bundle extras = getIntent().getExtras();
        String idSale = extras.getString("id");
        String totalP = extras.getString("totalP");
        String discount = extras.getString("discount");

        TextView text_id = (TextView) findViewById(R.id.text_id_sale);
        text_id.setText(idSale);

        TextView text_total_p = (TextView) findViewById(R.id.text_total_price);
        text_total_p.setText(totalP + " " + getResources().getString(R.string.money));

        TextView text_discount = (TextView) findViewById(R.id.text_discount);
        text_discount.setText("-" + discount + " " + getResources().getString(R.string.money));
    }
    public void done(View view){
        finish();
    }

}
