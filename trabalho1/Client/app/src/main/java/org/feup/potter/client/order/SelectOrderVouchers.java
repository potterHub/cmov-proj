package org.feup.potter.client.order;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.feup.potter.client.R;
import org.feup.potter.client.db.VouchersInList;
import org.feup.potter.client.vouchers.BaseVoucherList;

import java.util.ArrayList;


public class SelectOrderVouchers extends BaseVoucherList {

    private Drawable originalBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchers);

        originalBackground = this.findViewById(android.R.id.content).getBackground();

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

    // create the option toast menu (main.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.order_option, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    //  method from the interface on listActivity that handles the user clicks in the items from the ListView the list with the restaurants
    public void onListItemClick(ListView list, View view, int position, long id) {
        VouchersInList newVoucher = vouchers.get(position);
        int index = this.data.orderVoucherList.indexOf(newVoucher);

        if (index < 0) {
            if (this.data.orderVoucherList.size() >= 3) {
                Toast.makeText(SelectOrderVouchers.this, "Max number of vouchers reached.", Toast.LENGTH_SHORT).show();
            } else {
                if (newVoucher.getVoucherType() == VouchersInList.VOUCHER_TYPE.GLOBAL_DISCOUNT && this.data.alreadyHasGlobalDiscont) {
                    Toast.makeText(SelectOrderVouchers.this, "Only one global discount is allowed per order.", Toast.LENGTH_SHORT).show();
                } else {
                    this.data.orderVoucherList.add(newVoucher);
                    if (newVoucher.getVoucherType() == VouchersInList.VOUCHER_TYPE.GLOBAL_DISCOUNT)
                        this.data.alreadyHasGlobalDiscont = true;
                }
            }
        } else {
            this.data.orderVoucherList.remove(index);
            if (newVoucher.getVoucherType() == VouchersInList.VOUCHER_TYPE.GLOBAL_DISCOUNT)
                this.data.alreadyHasGlobalDiscont = false;
        }
        this.listAdapter.notifyDataSetChanged();
    }

    // when the menu is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back_icon:
                finish();
                return (true);
            case R.id.next_icon:
                Toast.makeText(SelectOrderVouchers.this, "Selected.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SelectOrderVouchers.this, EnterPinActivity.class));
                return (true);
            default:
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    // Cursor adapter (to implement the list row view)
    public class ListRowAdapter extends ArrayAdapter<VouchersInList> {
        // receives the cursor model
        public ListRowAdapter() {
            super(SelectOrderVouchers.this, R.layout.row_order_list_vauchers, vouchers);
        }

        @Override // to make our custom adapter build each row according with our list layout
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = SelectOrderVouchers.this.getLayoutInflater();
                row = inflater.inflate(R.layout.row_order_list_vauchers, parent, false); // get out the custom layout
            }
            final VouchersInList voucher = vouchers.get(position);

            CheckBox checkBox = (CheckBox) row.findViewById(R.id.checkbox_voucher);
            checkBox.setFocusable(false);
            if (SelectOrderVouchers.this.data.orderVoucherList.contains(voucher)) {
                checkBox.setChecked(true);
                row.setBackgroundColor(0xFF9BE89D);
            }else{
                checkBox.setChecked(false);
                row.setBackground(originalBackground);
            }

            ((TextView) row.findViewById(R.id.text_description_voucher)).setText(voucher.getDescriptionOfVoucher());   // sets the restaurant address by the cursor from the selected line
            // set the custom row view values
            ((TextView) row.findViewById(R.id.code_voucher)).setText(voucher.getCodeVoucher().toString());        // sets the restaurant name by the cursor from the selected line

            return (row);
        }
    }
}
