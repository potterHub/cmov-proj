package org.feup.potter.client.log_in;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


import org.feup.potter.client.DataStructures.CreditCard;
import org.feup.potter.client.R;

public class GetCreditCardDialog extends Dialog implements View.OnClickListener {

    // this interface is for the communications between the activity that manages this fragment
    public interface CredidCardInterface {
        public void whenCreditCardAdded(CreditCard card);
    }

    // interface object
    private CredidCardInterface interfaceWithMain;

    // card type spinner
    private Spinner mSpinnerCardType;

    // edit texts
    private EditText edit_number;
    private Spinner spinner_month;
    private Spinner spinner_year;

    private Context context;


    public GetCreditCardDialog(Context context) {
        super(context);
        this.context = context;

        setTitle(R.string.label_credit_card_title);
        setOwnerActivity((Activity) context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_credit_card);

        // handle the view
        // create spinner
        ArrayList<CreditCard> list = new ArrayList<>();
        list.add(new CreditCard(CreditCard.CardType.MASTER, R.drawable.master_card_icon));
        list.add(new CreditCard(CreditCard.CardType.VISA, R.drawable.visa_icon));

        this.mSpinnerCardType = (Spinner) findViewById(R.id.spinner_card_type);

        SpinnerAdapter adapter = new SpinnerAdapter((Activity) this.context,
                R.layout.row_spinner_credit_card_type, R.id.text, list);

        mSpinnerCardType.setAdapter(adapter);

        // text editors
        this.edit_number = (EditText) findViewById(R.id.edit_card_number);

        this.spinner_month = (Spinner) findViewById(R.id.spinner_month);
        Integer[] array_m = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        ArrayAdapter<Integer> spinnerMonthAdapter = new ArrayAdapter<Integer>
                (context, R.layout.simple_spinner_item, array_m);
        this.spinner_month.setAdapter(spinnerMonthAdapter);

        this.spinner_year = (Spinner) findViewById(R.id.spinner_year);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        Integer[] array_y = new Integer[51];
        for(int i = 0 ; i < array_y.length ; i++)
            array_y[i] = year + i;

        ArrayAdapter<Integer> spinnerYearAdapter = new ArrayAdapter<Integer>
                (context, R.layout.simple_spinner_item, array_y);
        this.spinner_year.setAdapter(spinnerYearAdapter);

        // button done
        Button doneBut = (Button) findViewById(R.id.button_done);
        interfaceWithMain = (CredidCardInterface) getOwnerActivity();
        doneBut.setOnClickListener(this);
    }

    // spinner layout
    public class SpinnerAdapter extends ArrayAdapter<CreditCard> {
        int groupid;
        Activity context;
        ArrayList<CreditCard> list;
        LayoutInflater inflater;

        public SpinnerAdapter(Activity context, int groupid, int id, ArrayList<CreditCard>
                list) {
            super(context, id, list);
            this.list = list;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.groupid = groupid;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = inflater.inflate(groupid, parent, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.icon);

            imageView.setImageResource(list.get(position).getImageId());

            TextView textView = (TextView) itemView.findViewById(R.id.text);
            textView.setText(list.get(position).getText());
            return itemView;
        }

        public View getDropDownView(int position, View convertView, ViewGroup
                parent) {
            return getView(position, convertView, parent);
        }
    }

    /* for the dialog button */
    @Override
    public void onClick(View v) {
        CreditCard c = null;
        try {
            c = (CreditCard) this.mSpinnerCardType.getSelectedItem();
            c.setCardNumber(this.edit_number.getText().toString());
            try {
                // change to data editText

                //c.setMonthExpiration(this.edit_month.getText().toString());
                //c.setYearExpiration(this.edit_year.getText().toString());
            } catch (NumberFormatException e) {

            }
        } catch (ClassCastException e) {
            Log.d("error", "casting credit card");
        }
        interfaceWithMain.whenCreditCardAdded(c);
        dismiss();
    }
}
