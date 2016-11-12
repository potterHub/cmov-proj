package org.feup.potter.terminal.serverConnection;

import android.util.Log;

import org.feup.potter.terminal.db.Order;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DoOrder extends ServerConnectionAPI implements Runnable {
    private Order order;

    protected HttpResponse androidActivity;

    public DoOrder(HttpResponse myclass, Order order) {
        super();
        this.androidActivity = myclass;
        this.order = order;
    }

    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL("http://" + address + ":" + port + "/" + DO_ORDER_PATH);

            // writeText("POST " + url.toExternalForm());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authentication", "Bearer " + order.getUserTokan());
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setUseCaches(false);

            //'{"idcustomer": 1, "items": [{"idItem": 1, "quantity": 2}], "items": [{"idItem": 1, "quantity": 100}], "vouchers": [{"idVoucher": 1, "code": "111111111111111111111111111111111111111111111"}]}' http://127.0.0.1:8080/terminal/order

            JSONObject jsonObj = new JSONObject();

            JSONArray jsonItems = new JSONArray();
            for(String [] item : order.getItems()) {
                JSONObject jsonItem = new JSONObject();
                jsonItem.put("idItem",Integer.parseInt(item[order.ID_ITEM]));
                jsonItem.put("quantity",Integer.parseInt(item[order.NUM_ITEM]));
                jsonItems.put(jsonItem);
            }

            JSONArray jsonVouchers = new JSONArray();
            for(String [] vouch : order.getVouchers()) {
                JSONObject jsonVouch = new JSONObject();
                jsonVouch.put("code",vouch[order.CODE_VOUCH]);
                jsonVouch.put("idVoucher",Integer.parseInt(vouch[order.ID_VOUCH]));
                jsonVouchers.put(jsonVouch);
            }

            jsonObj.put("idcustomer", Integer.parseInt(order.getUserId()));
            jsonObj.put("items", jsonItems);
            jsonObj.put("vouchers", jsonVouchers);


            Log.d("DoOrder Json", jsonObj.toString());
            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(jsonObj.toString());
            outputStream.flush();
            outputStream.close();


            int responseCode = urlConnection.getResponseCode();

            String response = "";
            if (responseCode == 200) {
                response = readStream(urlConnection.getInputStream());
                Log.d("DoOrder", "Server Response OK");
            } else {
                response = readStream(urlConnection.getErrorStream());
                Log.d("DoOrder", "Server Response ERROR");
            }
            androidActivity.handleResponse(responseCode, response);
        } catch (Exception e) {
            androidActivity.handleResponse(0, e.toString());
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }
}
