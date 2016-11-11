package org.feup.potter.client.pastTranfer;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.feup.potter.client.LunchAppData;
import org.feup.potter.client.R;
import org.feup.potter.client.db.DataBaseHelper;
import org.feup.potter.client.db.PastTransactionsInList;
import org.feup.potter.client.serverConnection.GetPastTransactions;
import org.feup.potter.client.serverConnection.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PastTransferActivity extends ListActivity implements HttpResponse {
    // list that holds the data
    protected ArrayList<PastTransactionsInList> transactions;
    // adapter to sync the data list with the ListView
    protected ArrayAdapter<PastTransactionsInList> listAdapter;

    protected GetPastTransactions connApi;

    protected LunchAppData data;

    protected DataBaseHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_trasactions);

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
        this.transactions = new ArrayList<PastTransactionsInList>();
        // instantiate the adapter
        this.listAdapter = new ListRowAdapter();
        //attach the adapter to the list
        this.setListAdapter(this.listAdapter);
    }

    protected void connectToServer() {
        this.connApi = new GetPastTransactions(this, this.data.user.getTokan()); // this must implement HttpResponse interface
        Thread thr = new Thread(this.connApi);
        thr.start();
    }

    // handle response from server (don't here is exactly the same as order tab activity)
    @Override
    public void handleResponse(int code, String response) {
        if (code == 200) {
            try {
                Log.d("PastTransferActivity", "response OK : " + response);

                JSONArray jsonArray = new JSONArray(response);
                insertInListView(jsonArray);
            } catch (JSONException e) {
                Log.d("PastTransferActivity", "Json format error");
                populateListWithLocalDb();
            }
        } else {
            Log.d("PastTransferActivity", "Response code: " + code);
            populateListWithLocalDb();
        }
    }

    private void populateListWithLocalDb() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               /* Cursor c = DB.getAllItems();
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
                    listAdapter.add(DB.getVoucherInList(c));*/
            }
        });
    }

    private void insertInListView(final JSONArray jsonArray) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              try {
                                  for (int i = 0; i < jsonArray.length(); i++) {
                                      JSONObject saleObj = jsonArray.getJSONObject(i);
                                      System.out.println(saleObj.toString());

                                      String idSale = saleObj.getString("idSale");
                                      String dateSale = saleObj.getString("myDateTime");

                                      PastTransactionsInList data = new PastTransactionsInList(idSale, dateSale);

                                      JSONArray vouchers = saleObj.getJSONArray("vouchers");
                                      for (int x = 0; x < vouchers.length(); x++) {
                                          JSONObject voucherObj = vouchers.getJSONObject(x);
                                          JSONObject voucherTemplate = voucherObj.getJSONObject("voucherTemplate");
                                          String descriptionofVoucher = voucherTemplate.getString("description");
                                          String codeVoucher = voucherObj.getString("code");

                                          data.addVouchers(descriptionofVoucher, codeVoucher);
                                      }
                                      JSONArray items = saleObj.getJSONArray("items");
                                      for (int x = 0; x < items.length(); x++) {
                                          JSONObject itemObj = items.getJSONObject(x);
                                          String idItem = itemObj.getString("idItem");
                                          String quantity = itemObj.getString("quantity");

                                          data.addItems(idItem, quantity, "price");
                                      }
                                      listAdapter.add(data);
                                  }
                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                          }
                      }

        );
    }

    // Cursor adapter (to implement the list row view)
    public class ListRowAdapter extends ArrayAdapter<PastTransactionsInList> {
        // receives the cursor model
        public ListRowAdapter() {
            super(PastTransferActivity.this, R.layout.row_list_transactions, transactions);
        }

        @Override // to make our custom adapter build each row according with our list layout
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = PastTransferActivity.this.getLayoutInflater();
                row = inflater.inflate(R.layout.row_list_transactions, parent, false); // get out the custom layout
            }
            final PastTransactionsInList tranfer = transactions.get(position);

            ((TextView) row.findViewById(R.id.transfer_name)).setText(tranfer.getIdSale());   // sets the restaurant address by the cursor from the selected line
            // set the custom row view values
            //((TextView) row.findViewById(R.id.code_voucher)).setText(voucher.getCodeVoucher().toString());        // sets the restaurant name by the cursor from the selected line

            return (row);
        }
    }
}
