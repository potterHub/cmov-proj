package org.feup.potter.client.order;


import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.feup.potter.client.LunchAppData;
import org.feup.potter.client.R;
import org.feup.potter.client.db.ItemInList;
import org.feup.potter.client.menus.DialogMenuDetailsFrag;

import java.util.Timer;
import java.util.TimerTask;

public class OrderConfirmAndMake extends ListActivity {
    // adapter to sync the data list with the ListView
    protected ArrayAdapter<ItemInList> listAdapter;

    private LunchAppData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);

        this.data = (LunchAppData) getApplicationContext();

        // initiate list adapter
        initiateListAndListAdapter();
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
        if (item.getItemId() == R.id.menu_icon) {
            finish();
            return (true);
        }

        return (super.onOptionsItemSelected(item));
    }

    protected void initiateListAndListAdapter() {
        this.listAdapter = new OrderConfirmAndMake.ListRowAdapter();
        //attach the adapter to the list
        this.setListAdapter(this.listAdapter);
    }

    @Override
    //  method from the interface on listActivity that handles the user clicks in the items from the ListView the list with the restaurants
    public void onListItemClick(ListView list, View view, int position, long id) {
        DialogMenuDetailsFrag dialog = DialogMenuDetailsFrag.newInstance(data.orderItemList.get(position));// enviamos o id do objecto na lista
        dialog.show(getFragmentManager(), "item_details");
    }

    // Cursor adapter (to implement the list row view)
    public class ListRowAdapter extends ArrayAdapter<ItemInList> {
        // receives the cursor model
        public ListRowAdapter() {
            super(OrderConfirmAndMake.this, R.layout.row_list_menu_in_order, OrderConfirmAndMake.this.data.orderItemList);
        }

        @Override // to make our custom adapter build each row according with our list layout
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = OrderConfirmAndMake.this.getLayoutInflater();
                row = inflater.inflate(R.layout.row_list_menu_in_order, parent, false); // get out the custom layout
            }

            final ItemInList data = OrderConfirmAndMake.this.data.orderItemList.get(position);

            // set the custom row view values
            ((TextView) row.findViewById(R.id.title)).setText(data.getName() + ": ");
            ((TextView) row.findViewById(R.id.quantity)).setText(data.getQuantity() + " Unit(s)");
            ((TextView) row.findViewById(R.id.price)).setText(data.getPrice() + " " + getResources().getString(R.string.money));   // sets the restaurant address by the cursor from the selected line

            // set the symbol image by the restaurant type
            ImageView img = (ImageView) row.findViewById(R.id.img);
            img.setImageBitmap(data.getImage());

            final Button button = (Button) row.findViewById(R.id.button_delete_in_order);
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // change to string.xml and stuff
                    Toast.makeText(OrderConfirmAndMake.this, data.getName() + " deleted", Toast.LENGTH_SHORT).show();

                    //**************** try to change to hashTable or map ***************
                    for (ItemInList item : OrderConfirmAndMake.this.data.orderItemList) {
                        if (item.getIdItem().equals(data.getIdItem())) {
                            item.decQ();
                            if (item.getQuantity() < 1)
                                OrderConfirmAndMake.this.data.orderItemList.remove(data);
                            break;
                        }
                    }

                    listAdapter.notifyDataSetChanged();

                    // blocks the button for half second
                    button.setEnabled(false);
                    Timer buttonTimer = new Timer();
                    buttonTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    button.setEnabled(true);
                                }
                            });
                        }
                    }, 500);
                }
            });
            button.setFocusable(false);

            // return the changed row
            return (row);
        }
    }

}
