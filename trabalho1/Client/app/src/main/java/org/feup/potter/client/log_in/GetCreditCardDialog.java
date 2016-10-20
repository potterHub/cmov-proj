package org.feup.potter.client.log_in;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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


import org.feup.potter.client.DataStructures.CreditCard;
import org.feup.potter.client.R;

public class GetCreditCardDialog extends Dialog implements View.OnClickListener {

    // this interface is for the communications between the activity that manages this fragment
    public interface CredidCardInterface {
        public void whenCreditCardAdded(GetCreditCardDialog Dialog);
    }

    // interface object
    private CredidCardInterface interfaceWithMain;

    // card type spinner
    private Spinner mSpinnerCardType;

    // edit texts
    private EditText edit_number;
    private EditText edit_month;
    private EditText edit_year;

    private Context context;


    public GetCreditCardDialog(Context context){
            super(context);
            this.context = context;

            setTitle(R.string.label_credit_card_title);
            setOwnerActivity((Activity)context);
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
                R.layout.spinner_credit_card_type_row, R.id.text, list);

        mSpinnerCardType.setAdapter(adapter);

        // text editors
        this.edit_number = (EditText) findViewById(R.id.edit_card_number);
        this.edit_month  = (EditText) findViewById(R.id.edit_month);
        this.edit_year = (EditText) findViewById(R.id.edit_year);

        // button done
        Button doneBut = (Button)findViewById(R.id.button_done);
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
        interfaceWithMain.whenCreditCardAdded(this);
        dismiss();
    }
}
