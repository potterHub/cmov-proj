package org.feup.potter.client.vouchers;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.feup.potter.client.R;
import org.feup.potter.client.db.VouchersInList;

import java.util.ArrayList;

public class VoucherActivity extends BaseVoucherList{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchers);

        this.initiateListAndListAdapter();

        this.connectToServer();
    }

    protected void initiateListAndListAdapter() {
        this.vouchers = new ArrayList<VouchersInList>();
        // instantiate the adapter
        this.listAdapter = new ListRowAdapter();
        //attach the adapter to the list
        this.setListAdapter(this.listAdapter);
    }

    // Cursor adapter (to implement the list row view)
    public class ListRowAdapter extends ArrayAdapter<VouchersInList> {
        // receives the cursor model
        public ListRowAdapter() {
            super(VoucherActivity.this, R.layout.row_list_vauchers, vouchers);
        }
        @Override // to make our custom adapter build each row according with our list layout
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = VoucherActivity.this.getLayoutInflater();
                row = inflater.inflate(R.layout.row_list_vauchers, parent, false); // get out the custom layout
            }
            final VouchersInList voucher = vouchers.get(position);

            ((TextView) row.findViewById(R.id.text_description_voucher)).setText(voucher.getDescriptionOfVoucher());   // sets the restaurant address by the cursor from the selected line
            // set the custom row view values
            ((TextView) row.findViewById(R.id.code_voucher)).setText(voucher.getCodeVoucher().toString());        // sets the restaurant name by the cursor from the selected line

            return (row);
        }
    }
}
