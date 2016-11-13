package org.feup.potter.client.vouchers;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.feup.potter.client.LunchAppData;
import org.feup.potter.client.db.DataBaseHelper;
import org.feup.potter.client.db.VouchersInList;
import org.feup.potter.client.serverConnection.GetVoucher;
import org.feup.potter.client.serverConnection.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class BaseVoucherList extends ListActivity implements HttpResponse {
    // list that holds the data
    protected ArrayList<VouchersInList> vouchers;
    // adapter to sync the data list with the ListView
    protected ArrayAdapter<VouchersInList> listAdapter;

    protected GetVoucher connApi;

    protected LunchAppData data;

    protected DataBaseHelper DB;

    protected ProgressDialog progDiag;

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
        startProgressBar();

        this.connApi = new GetVoucher(this, this.data.user.getTokan()); // this must implement HttpResponse interface
        Thread thr = new Thread(this.connApi);
        thr.start();
    }

    public void startProgressBar() {
        progDiag = new ProgressDialog(BaseVoucherList.this);
        progDiag.setTitle("Loading vouchers");
        progDiag.setMessage("Please wait...");
        progDiag.show();
    }

    private void stopProgressBar() {
        if (progDiag != null)
            progDiag.dismiss();
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
                Cursor c = DB.getAllVouchers();
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
                    listAdapter.add(DB.getVoucherInList(c));

                stopProgressBar();
            }
        });
    }

    private void insertInListView(final JSONArray jsonArray) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              try {
                                  for (int i = 0; i < jsonArray.length(); i++) {
                                      JSONObject voucherObj = jsonArray.getJSONObject(i);

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

                                      long id = BaseVoucherList.this.DB.insertVoucher(data);
                                      if (id < 0) {
                                          // needs to be updated
                                          BaseVoucherList.this.DB.updateVoucher(data);
                                      }
                                      listAdapter.add(data);
                                  }
                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                              stopProgressBar();
                          }
                      }

        );
    }
}
