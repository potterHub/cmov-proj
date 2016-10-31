package org.feup.potter.client.menus;


import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import org.feup.potter.client.R;
import org.feup.potter.client.db.DataBaseHelper;
import org.feup.potter.client.db.ItemTable;
import org.feup.potter.client.serverConnection.GetItem;
import org.feup.potter.client.serverConnection.HttpResponse;
import org.feup.potter.client.serverConnection.ServerConnectionAPI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenusActivity extends ListActivity implements HttpResponse {
    // list that holds the data
    protected ArrayList<String[]> menus;
    // adapter to sync the data list with the ListView
    protected ArrayAdapter<String[]> listAdapter;


    protected GetItem connApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);

        // initiate list adapter
        initiateListAndListAdapter(true);

        // connection api
        connectToServer();
    }

    protected void initiateListAndListAdapter(boolean onlyList) {
        this.menus = new ArrayList<String[]>();
        // instantiate the adapter
        this.listAdapter = new ListRowAdapter(onlyList);
        //attach the adapter to the list
        this.setListAdapter(this.listAdapter);
    }

    protected void connectToServer() {
        this.connApi = new GetItem(this); // this must implement HttpResponse interface
        Thread thr = new Thread(this.connApi);
        thr.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    //  method from the interface on listActivity that handles the user clicks in the items from the ListView the list with the restaurants
    public void onListItemClick(ListView list, View view, int position, long id) {
        DialogMenuDetailsFrag dialog = DialogMenuDetailsFrag.newInstance(menus.get(position));// enviamos o id do objecto na lista
        dialog.show(getFragmentManager(), "item_details");
    }

    // handle response from server
    @Override
    public void handleResponse(int code, String response) {
        if (code == 200) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    final JSONObject obj = jsonArray.getJSONObject(i);

                    // convert string image to bitmap
                    // byte[] encodeByte = Base64.decode(obj.getString("img"), Base64.DEFAULT);
                    // Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                    //Log.d("", obj.getInt("IdItem") + " | " + obj.getDouble("Price") + " | " + obj.getString("Description"));
                    // {"IdItem":1,"name":"Pepsi","Price":1.35,"Description":"Pepsi","Img":"...","Type": }
                    insertInListView(obj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // maybe toast with error user friendly
        }
    }

    private void insertInListView(final JSONObject obj) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String[] data = new String[6];
                    data[0] = obj.getString("IdItem");
                    data[1] = obj.getString("IdItem");
                    data[2] = obj.getString("Price");
                    data[3] = obj.getString("Description");
                    data[4] = "";
                    data[5] = "";

                    listAdapter.add(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Cursor adapter (to implement the list row view)
    public class ListRowAdapter extends ArrayAdapter<String[]> {
        private boolean onlyList;

        // receives the cursor model
        public ListRowAdapter(boolean onlyList) {
            super(MenusActivity.this, onlyList ? R.layout.row_list_menu : R.layout.row_list_menu_to_order, menus);
            this.onlyList = onlyList;
        }

        @Override // to make our custom adapter build each row according with our list layout
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = MenusActivity.this.getLayoutInflater();
                row = inflater.inflate(onlyList ? R.layout.row_list_menu : R.layout.row_list_menu_to_order, parent, false); // get out the custom layout
            }

            final String[] data = menus.get(position);

            // set the custom row view values
            ((TextView) row.findViewById(R.id.title)).setText(data[1]);        // sets the restaurant name by the cursor from the selected line
            ((TextView) row.findViewById(R.id.price)).setText(data[2] + " " + getResources().getString(R.string.money));   // sets the restaurant address by the cursor from the selected line

            // set the symbol image by the restaurant type
            // ImageView img = (ImageView) row.findViewById(R.id.img);
            // img.setImageBitmap(dataHelper.getImg(cursor));

            if (!onlyList) {
                Button button = (Button) row.findViewById(R.id.button_add_to_order);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // change to string.xml and stuff
                        Toast.makeText(MenusActivity.this, data[1] + " added", Toast.LENGTH_LONG).show();
                        // need to add to the application data or sql
                    }
                });
            }

            // return the changed row
            return (row);
        }
    }
}
