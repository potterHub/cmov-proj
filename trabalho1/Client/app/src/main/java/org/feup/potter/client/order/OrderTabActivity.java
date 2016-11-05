package org.feup.potter.client.order;

import android.app.TabActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.feup.potter.client.LunchAppData;
import org.feup.potter.client.R;
import org.feup.potter.client.Util.Util;
import org.feup.potter.client.db.DataBaseHelper;
import org.feup.potter.client.db.ItemInList;
import org.feup.potter.client.serverConnection.GetItem;
import org.feup.potter.client.serverConnection.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderTabActivity extends TabActivity implements AdapterView.OnItemClickListener, HttpResponse {
    // list that holds the data
    protected ArrayList<ItemInList> menus;
    protected ArrayList<ItemInList> orders;
    // adapter to sync the data list with the ListView
    protected ArrayAdapter<ItemInList> listMenusAdapter;
    protected ArrayAdapter<ItemInList> listOrderAdapater;

    protected GetItem connApi;

    protected LunchAppData data;

    protected DataBaseHelper DB;

    protected ListView listViewMenus;
    protected ListView listViewOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        this.data = (LunchAppData) getApplicationContext();

        this.DB = new DataBaseHelper(this);

        // menus list
        this.listViewMenus = (ListView) findViewById(R.id.menus_list_view);
        this.menus = new ArrayList<ItemInList>();
        // instantiate the adapter
        this.listMenusAdapter = new ListMenuRowAdapter();
        //attach the adapter to the list
        this.listViewMenus.setAdapter(this.listMenusAdapter);
        listViewMenus.setOnItemClickListener(this);
        //listViewMenus.setEmptyView((TextView) findViewById(R.id.em));

        // order list
        this.listViewOrder = (ListView) findViewById(R.id.order_list_view);
        this.orders = new ArrayList<ItemInList>();
        // instantiate the adapter
        this.listOrderAdapater = new ListOrderRowAdapter();
        this.listOrderAdapater.add(new ItemInList("","","","","",""));
        //attach the adapter to the list
        this.listViewOrder.setAdapter(this.listOrderAdapater);
        listViewOrder.setOnItemClickListener(this);
        //listViewMenus.setEmptyView((TextView) findViewById(R.id.empty_order));

        // define tabHost view one (list show part)
        TabHost.TabSpec spec = getTabHost().newTabSpec("tab1");
        spec.setContent(R.id.menus_list_view); // setting the listing tab
        spec.setIndicator("Menus", getResources().getDrawable(R.drawable.menus_icon));
        // add the list tab settings
        getTabHost().addTab(spec);


        // setting tabHost view 2 (editing part)
        spec = getTabHost().newTabSpec("tag2");
        spec.setContent(R.id.order_list_view);// setting the edit table
        spec.setIndicator("Order", getResources().getDrawable(R.drawable.order_icon));
        // add the list tab settings
        getTabHost().addTab(spec);

        connectToServer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DB.close();
    }

    protected void connectToServer() {
        this.connApi = new GetItem(this, this.data.hash); // this must implement HttpResponse interface
        Thread thr = new Thread(this.connApi);
        thr.start();
    }

    @Override
    // method from the interface AdapterView.OnItemClickListener handles the user clicks in the items from the ListView
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    // list of menus adapater
    public class ListMenuRowAdapter extends ArrayAdapter<ItemInList> {
        // receives the cursor model
        public ListMenuRowAdapter() {
            super(OrderTabActivity.this, R.layout.row_list_menu_to_order, menus);
        }

        @Override // to make our custom adapter build each row according with our list layout
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = OrderTabActivity.this.getLayoutInflater();
                row = inflater.inflate(R.layout.row_list_menu_to_order, parent, false); // get out the custom layout
            }
            final ItemInList data = menus.get(position);

            // set the custom row view values
            ((TextView) row.findViewById(R.id.title)).setText(data.getName());        // sets the restaurant name by the cursor from the selected line
            ((TextView) row.findViewById(R.id.price)).setText(data.getPrice() + " " + getResources().getString(R.string.money));   // sets the restaurant address by the cursor from the selected line

            // set the symbol image by the restaurant type
            ImageView img = (ImageView) row.findViewById(R.id.img);
            img.setImageBitmap(data.getImage());

            Button button = (Button) row.findViewById(R.id.button_add_to_order);
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(OrderTabActivity.this, data.getName() + " added", Toast.LENGTH_LONG).show();

                    // set quantity of the item
                    // send it to order adapater
                }
            });

            // return the changed row
            return (row);
        }
    }

    // Cursor adapter (to implement the list row view)
    public class ListOrderRowAdapter extends ArrayAdapter<ItemInList> {
        // receives the cursor model
        public ListOrderRowAdapter() {
            super(OrderTabActivity.this, R.layout.row_list_order, menus);
        }

        @Override // to make our custom adapter build each row according with our list layout
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = OrderTabActivity.this.getLayoutInflater();
                row = inflater.inflate(R.layout.row_list_menu_to_order, parent, false); // get out the custom layout
            }

            final ItemInList data = orders.get(position);
            // set the custom row view values
            ((TextView) row.findViewById(R.id.title)).setText(data.getName());
            ((TextView) row.findViewById(R.id.price)).setText(data.getPrice() + " " + getResources().getString(R.string.money));
            ((TextView) row.findViewById(R.id.quantity)).setText(getResources().getString(R.string.quantity) + " " + data.getQuantity());

            // set the symbol image by the restaurant type
            ImageView img = (ImageView) row.findViewById(R.id.img);
            img.setImageBitmap(data.getImage());
            // return the changed row
            return (row);
        }
    }

    // handle response from server (don't here is exacly the same as oerder tab activity)
    @Override
    public void handleResponse(int code, String response) {
        if (code == 200) {
            try {
                Log.d("response", response);
                JSONObject obj = new JSONObject(response);

                String hash = obj.getString("hash");

                if (hash.equals(data.hash)) {
                    // the local db is up to dated
                    Log.d("getItem", "table updated");
                    populateListWithLocalDb();
                } else {


                    JSONArray jsonArray = obj.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        final JSONObject itemObj = jsonArray.getJSONObject(i);
                        insertInListView(itemObj);
                    }

                    saveHash(hash);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // maybe toast with error user friendly
        }
    }

    private void saveHash(final String hash) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OrderTabActivity.this.data.hash = hash;
                Util.saveData(OrderTabActivity.this.data.hash, OrderTabActivity.this.data.itemHashPath, OrderTabActivity.this);
            }
        });
    }

    private void populateListWithLocalDb() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Cursor c = DB.getAllItems();
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
                    listMenusAdapter.add(DB.getItemInList(c));
            }
        });
    }

    private void insertInListView(final JSONObject obj) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    //{"idItem":1,"itemType":{"idItemType":1,"description":"Beverage"},"price":1.35,"name":"Pepsi","description":"Pepsi","image":
                    // ITEM_ID = 1; NAME = 2; PRICE = 3; DESCRIPTION = 4; IMG = 5; TYPE = 6;
                    ItemInList data =
                            new ItemInList(obj.getString("idItem"),
                                    obj.getString("name"),
                                    obj.getString("price"),
                                    obj.getString("description"),
                                    obj.getString("image"),
                                    obj.getJSONObject("itemType").getString("description"));

                    long id = OrderTabActivity.this.DB.insertItem(data);
                    if (id < 0) {
                        // needs to be updated
                        OrderTabActivity.this.DB.updateItem(data);
                    }

                    listMenusAdapter.add(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
