package org.feup.potter.client.menus;

import android.app.ListActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.feup.potter.client.serverConnection.GetItem;
import org.feup.potter.client.serverConnection.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class BaseItemMenuList extends ListActivity implements HttpResponse {
    // list that holds the data
    protected ArrayList<String[]> menus;
    // adapter to sync the data list with the ListView
    protected ArrayAdapter<String[]> listAdapter;

    protected GetItem connApi;

    protected void connectToServer() {
        this.connApi = new GetItem(this); // this must implement HttpResponse interface
        Thread thr = new Thread(this.connApi);
        thr.start();
    }

    @Override
    public abstract void onListItemClick(ListView list, View view, int position, long id);

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
                    //{"idItem":1,"itemType":{"idItemType":1,"description":"Beverage"},"price":1.35,"description":"Pepsi"}
                    // ITEM_ID = 1; NAME = 2; PRICE = 3; DESCRIPTION = 4; IMG = 5; TYPE = 6;

                    String[] data = new String[6];
                    data[0] = obj.getString("idItem");
                    data[1] = obj.getString("idItem");
                    data[2] = obj.getString("price");
                    data[3] = obj.getString("description");
                    data[4] = "";
                    data[5] = obj.getJSONObject("itemType").getString("description");

                    listAdapter.add(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
