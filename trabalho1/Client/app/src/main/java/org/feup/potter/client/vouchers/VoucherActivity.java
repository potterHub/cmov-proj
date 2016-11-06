package org.feup.potter.client.vouchers;


import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.feup.potter.client.LunchAppData;
import org.feup.potter.client.R;
import org.feup.potter.client.db.DataBaseHelper;
import org.feup.potter.client.db.VouchersInList;
import org.feup.potter.client.serverConnection.GetVoucher;
import org.feup.potter.client.serverConnection.HttpResponse;

import java.util.ArrayList;

public class VoucherActivity extends ListActivity implements HttpResponse {
    // list that holds the data
    protected ArrayList<VouchersInList> vouchers;
    // adapter to sync the data list with the ListView
    protected ArrayAdapter<VouchersInList> listAdapter;

    protected GetVoucher connApi;

    protected LunchAppData data;

    protected DataBaseHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchers);

        this.data = (LunchAppData) getApplicationContext();

        this.DB = new DataBaseHelper(this);

        this.initiateListAndListAdapter();

        this.connectToServer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DB.close();
    }

    protected void initiateListAndListAdapter() {
        this.vouchers = new ArrayList<VouchersInList>();
        // instantiate the adapter
        this.listAdapter = new ListRowAdapter();
        //attach the adapter to the list
        this.setListAdapter(this.listAdapter);
    }

    protected void connectToServer() {
        this.connApi = new GetVoucher(this, this.data.user.getTokan()); // this must implement HttpResponse interface
        Thread thr = new Thread(this.connApi);
        thr.start();
    }

    // handle response from server (don't here is exacly the same as oerder tab activity)
    @Override
    public void handleResponse(int code, String response) {
        if (code == 200) {
            Log.d("response",response);
            /*try {

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
            }*/
        } else {
            Log.d("response",response);
            // maybe toast with error user friendly
        }
    }
    // Cursor adapter (to implement the list row view)
    public class ListRowAdapter extends ArrayAdapter<VouchersInList> {
        // receives the cursor model
        public ListRowAdapter() {
            super(VoucherActivity.this, R.layout.row_list_menu, vouchers);
        }

        @Override // to make our custom adapter build each row according with our list layout
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = VoucherActivity.this.getLayoutInflater();
                row = inflater.inflate(R.layout.row_list_menu, parent, false); // get out the custom layout
            }

            final VouchersInList item = vouchers.get(position);

            /*
            // set the custom row view values
            ((TextView) row.findViewById(R.id.title)).setText(item.getName());        // sets the restaurant name by the cursor from the selected line
            ((TextView) row.findViewById(R.id.price)).setText(item.getPrice() + " " + getResources().getString(R.string.money));   // sets the restaurant address by the cursor from the selected line

            // set the symbol image by the restaurant type
            ImageView img = (ImageView) row.findViewById(R.id.img);
            img.setImageBitmap(item.getImage());
            */

            // return the changed row
            return (row);
        }
    }
}
