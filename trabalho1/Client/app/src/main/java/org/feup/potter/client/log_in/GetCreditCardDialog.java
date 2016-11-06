package org.feup.potter.client.log_in;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.feup.potter.client.DataStructures.CreditCard;
import org.feup.potter.client.R;

import java.util.ArrayList;
import java.util.Calendar;

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

    private CreditCard card;


    public GetCreditCardDialog(Context context, CreditCard card) {
        super(context);
        this.context = context;

        setTitle(R.string.label_credit_card_title);
        setOwnerActivity((Activity) context);

        Log.d("log", this.card == null ? "null" : "not null");
        this.card = card;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_credit_card);

        // handle the view
        // spinner
        ArrayList<CreditCard> list = new ArrayList<>();
        list.add(new CreditCard(CreditCard.CardType.MASTER, R.drawable.master_card_icon, card));
        list.add(new CreditCard(CreditCard.CardType.VISA, R.drawable.visa_icon, card));

        this.mSpinnerCardType = (Spinner) findViewById(R.id.spinner_card_type);

        SpinnerAdapter adapter = new SpinnerAdapter((Activity) this.context,
                R.layout.row_spinner_credit_card_type, R.id.text, list);

        mSpinnerCardType.setAdapter(adapter);

        // number
        this.edit_number = (EditText) findViewById(R.id.edit_card_number);

        // month
        this.spinner_month = (Spinner) findViewById(R.id.spinner_month);
        Integer[] array_m = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        ArrayAdapter<Integer> spinnerMonthAdapter = new ArrayAdapter<Integer>
                (context, R.layout.simple_spinner_item, array_m);
        this.spinner_month.setAdapter(spinnerMonthAdapter);

        // year
        this.spinner_year = (Spinner) findViewById(R.id.spinner_year);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        Integer[] array_y = new Integer[51];
        for (int i = 0; i < array_y.length; i++)
            array_y[i] = year + i;

        ArrayAdapter<Integer> spinnerYearAdapter = new ArrayAdapter<Integer>
                (context, R.layout.simple_spinner_item, array_y);
        this.spinner_year.setAdapter(spinnerYearAdapter);


        // set data
        if (this.card != null) {
            int spinnerPosition = adapter.getPosition(card);
            mSpinnerCardType.setSelection(spinnerPosition);

            edit_number.setText(card.getCardNumber());

            int sP1 = spinnerMonthAdapter.getPosition(card.getMonthExpiration());
            spinner_month.setSelection(sP1);

            int sP2 = spinnerYearAdapter.getPosition(card.getYearExpiration());
            spinner_year.setSelection(sP2);
        }

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
            if (c != null) {
                c.setCardNumber(this.edit_number.getText().toString());
                c.setMonthExpiration((Integer) this.spinner_month.getSelectedItem());
                c.setYearExpiration((Integer) this.spinner_year.getSelectedItem());
            }
        } catch (ClassCastException e) {
            Log.d("error", "casting credit card");
        }
        interfaceWithMain.whenCreditCardAdded(c);
        dismiss();
    }
}
