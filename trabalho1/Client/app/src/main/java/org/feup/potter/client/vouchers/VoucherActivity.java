package org.feup.potter.client.vouchers;


import android.app.ListActivity;
import android.database.Cursor;
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
import org.feup.potter.client.db.VouchersInList;
import org.feup.potter.client.serverConnection.GetVoucher;
import org.feup.potter.client.serverConnection.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            try {
                Log.d("VoucherActivity", "response OK : " + response);

                JSONArray jsonArray = new JSONArray(response);
                insertInListView(jsonArray);
            } catch (JSONException e) {
                Log.d("VoucherActivity", "Json format error");
                populateListWithLocalDb();
            }
        } else {
            Log.d("VoucherActivity", "Response code: " + code);
            populateListWithLocalDb();
        }
    }

    private void populateListWithLocalDb() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Cursor c = DB.getAllItems();
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
                    listAdapter.add(DB.getVoucherInList(c));
            }
        });
    }

    private void insertInListView(final JSONArray jsonArray) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              try {
                                  // {"idVoucher":3,"voucherTemplate":{"idVoucherTemplate":1,"voucherTemplateType":{"idVoucherTemplateType":1,"description":"Global discount"},"description":"5% Discount","value":0.05},"code":"111111111111111111111111111111111111111111111","gotVoucher":"0001-01-01T00:00:00Z"}
                                  // {"idVoucher":4,"voucherTemplate":{"idVoucherTemplate":2,"voucherTemplateType":{"idVoucherTemplateType":5,"description":"Free Item"},"items":[41],"description":"Free popcorn","value":1},"code":"999999999999999999999999999999999999999999999","gotVoucher":"0001-01-01T00:00:00Z"}
                                  // {"idVoucher":5,"voucherTemplate":{"idVoucherTemplate":3,"voucherTemplateType":{"idVoucherTemplateType":6,"description":"Free Item type"},"itemType":{"idItemType":8,"description":"Coffee"},"description":"Free coffee","value":1},"code":"555555555555555555555555555555555555555555555","gotVoucher":"0001-01-01T00:00:00Z"}

                                  for (int i = 0; i < jsonArray.length(); i++) {
                                      JSONObject voucherObj = jsonArray.getJSONObject(i);
                                      System.out.println(voucherObj.toString());

                                      String idVoucher = voucherObj.getString("idVoucher");
                                      String codeVoucher = voucherObj.getString("code");
                                      String dateVoucher = voucherObj.getString("gotVoucher");

                                      JSONObject voucherTemplate = voucherObj.getJSONObject("voucherTemplate");
                                      String descriptionofVoucher = voucherTemplate.getString("description");
                                      String valueOfdiscontOrNumberOfItems = voucherTemplate.getString("value");

                                      JSONObject voucherTemplateType = voucherTemplate.getJSONObject("voucherTemplateType");

                                      VouchersInList data =
                                              new VouchersInList(idVoucher,
                                                      codeVoucher,
                                                      dateVoucher,
                                                      descriptionofVoucher,
                                                      valueOfdiscontOrNumberOfItems);

                                      String voucherType = voucherTemplateType.getString("description");
                                      ArrayList<String> itemIdList = new ArrayList<String>();
                                      if (voucherType.equals("Free Item")) {
                                          JSONArray strArr = voucherTemplate.getJSONArray("items");
                                          for (int j = 0; j < strArr.length(); j++)
                                              itemIdList.add(strArr.getString(j));
                                          data.setVoucherType(VouchersInList.VOUCHER_TYPE.FREE_ITEM);
                                          data.setItemIdList(itemIdList);
                                      } else if (voucherType.equals("Free Item type")) {
                                          JSONObject ItemIdFreeType = voucherTemplate.getJSONObject("itemType");
                                          String typeItem = ItemIdFreeType.getString("description");
                                          data.setVoucherType(VouchersInList.VOUCHER_TYPE.FREE_ITEM_TYPE);
                                          data.setTypeItem(typeItem);
                                      } else {// "Global discount"
                                          data.setVoucherType(VouchersInList.VOUCHER_TYPE.GLOBAL_DISCOUNT);
                                      }

                                      long id = VoucherActivity.this.DB.insertVoucher(data);
                                      if (id < 0) {
                                          // needs to be updated
                                          VoucherActivity.this.DB.updateVoucher(data);
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
