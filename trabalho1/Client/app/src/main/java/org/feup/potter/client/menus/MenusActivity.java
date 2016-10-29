package org.feup.potter.client.menus;


import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;

import org.feup.potter.client.R;
import org.feup.potter.client.db.DataBaseHelper;
import org.feup.potter.client.db.ItemTable;
import org.feup.potter.client.serverConnection.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenusActivity extends ListActivity implements HttpResponse {
    //
    private DataBaseHelper dataBase;
    private ItemTable dataHelper;

    // list
    private Cursor model;
    private DataBaseCursorRowAdapter dataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);

        // creates the data base
        this.dataBase = new DataBaseHelper(this);

        this.dataHelper = dataBase.getItemsTable();
        // gets the items table cursor model
        this.model = dataHelper.getAll();
        // defines the cursor as a managing cursor
        startManagingCursor(model);
        // sets initiates the table data base adapter
        dataBaseAdapter = new DataBaseCursorRowAdapter(this.model);

        //attach the adapter to the list
        this.setListAdapter(this.dataBaseAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.dataBase.close();
    }


    @Override
    //  method from the interface on listActivity that handles the user clicks in the items from the ListView the list with the restaurants
    public void onListItemClick(ListView list, View view, int position, long id) {
        DialogMenuDetailsFrag dialog = DialogMenuDetailsFrag.newInstance(id);// enviamos o id do objecto na lista
        dialog.show(getFragmentManager(), "item_details");
    }

    // handle response from server
    @Override
    public void handleResponse(int code, String response) {
        if (code == 200) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = null;
                    try {
                        obj = jsonArray.getJSONObject(i);

                        // convert string image to bitmap
                        byte[] encodeByte = Base64.decode(obj.getString("img"), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                        dataHelper.insertItem(obj.getInt("itemId"), obj.getString("name"), obj.getDouble("price"), obj.getString("description"), bitmap, obj.getString("type"));
                    } catch (JSONException e) {
                        // change to toast
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // maybe toast with error user friendly
        }
    }

    // Cursor adapter (to implement the list row view)
    public class DataBaseCursorRowAdapter extends CursorAdapter {

        // receives the cursor model
        public DataBaseCursorRowAdapter(Cursor c) {
            super(MenusActivity.this, c);
        }

        @Override // it receives the cursor for the selected line
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = MenusActivity.this.getLayoutInflater();
            // inflate -> Inflate a new view hierarchy from the specified XML node. (create the custom row view)
            View row = inflater.inflate(R.layout.row_list_menu, parent, false); // get out the custom layout

            // set the custom row view values
            ((TextView) row.findViewById(R.id.title)).setText(dataHelper.getName(cursor));        // sets the restaurant name by the cursor from the selected line
            ((TextView) row.findViewById(R.id.price)).setText(dataHelper.getPrice(cursor) + " " + getResources().getString(R.string.money));   // sets the restaurant address by the cursor from the selected line

            // set the symbol image by the restaurant type
            ImageView img = (ImageView) row.findViewById(R.id.img);
            img.setImageBitmap(dataHelper.getImg(cursor));

            // return the changed row
            return (row);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
        }
    }
}
