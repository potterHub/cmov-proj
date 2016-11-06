package org.feup.potter.client.menus;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.feup.potter.client.LunchAppData;
import org.feup.potter.client.Util.Util;
import org.feup.potter.client.db.DataBaseHelper;
import org.feup.potter.client.db.ItemInList;
import org.feup.potter.client.serverConnection.GetItem;
import org.feup.potter.client.serverConnection.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class BaseItemMenuList extends ListActivity implements HttpResponse {
    // list that holds the data
    protected ArrayList<ItemInList> menus;
    // adapter to sync the data list with the ListView
    protected ArrayAdapter<ItemInList> listAdapter;

    protected GetItem connApi;

    protected LunchAppData data;

    protected DataBaseHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.data = (LunchAppData) getApplicationContext();

        this.DB = new DataBaseHelper(this);
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
    //  method from the interface on listActivity that handles the user clicks in the items from the ListView the list with the restaurants
    public void onListItemClick(ListView list, View view, int position, long id) {
        DialogMenuDetailsFrag dialog = DialogMenuDetailsFrag.newInstance(menus.get(position));// enviamos o id do objecto na lista
        dialog.show(getFragmentManager(), "item_details");
    }

    // handle response from server (don't here is exacly the same as oerder tab activity)
    @Override
    public void handleResponse(int code, String response) {
        if (code == 200) {
            try {
                Log.d("BaseItemMenuList","response OK : " + response);
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
                Log.d("BaseItemMenuList","Json format error");
                populateListWithLocalDb();
            }
        } else {
            Log.d("BaseItemMenuList","Response code: " + code);
            populateListWithLocalDb();
        }
    }

    private void saveHash(final String hash) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseItemMenuList.this.data.hash = hash;
                Util.saveData(BaseItemMenuList.this.data.hash, BaseItemMenuList.this.data.itemHashPath, BaseItemMenuList.this);
            }
        });
    }

    private void populateListWithLocalDb() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Cursor c = DB.getAllItems();
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
                    listAdapter.add(DB.getItemInList(c));
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

                    long id = BaseItemMenuList.this.DB.insertItem(data);
                    if(id < 0){
                        // needs to be updated
                        BaseItemMenuList.this.DB.updateItem(data);
                    }

                    listAdapter.add(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
