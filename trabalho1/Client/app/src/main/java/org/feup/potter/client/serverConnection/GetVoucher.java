package org.feup.potter.client.serverConnection;


import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

public class GetVoucher extends ServerConnectionAPI implements Runnable {
    protected HttpResponse androidActivity;
    protected String tokan;

    public GetVoucher(HttpResponse myclass, String tokan) {
        super();
        this.androidActivity = myclass;
        this.tokan = tokan;
    }

    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL("http://" + address + ":" + port + "/" + GET_VOUCHER_PATH);

            //writeText("GET " + url.toExternalForm());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Authorization", "Bearer " + this.tokan);
            urlConnection.setUseCaches(false);

            int responseCode = urlConnection.getResponseCode();

            String response = "";
            Log.d("GetItem","Server request: " + tokan);
            if (responseCode == 200) {
                response = readStream(urlConnection.getInputStream());
                Log.d("GetItem","Server Response OK");
            } else {
                response = readStream(urlConnection.getErrorStream());
                Log.d("GetItem","Server Response ERROR");
            }
            androidActivity.handleResponse(responseCode, response);
        } catch (Exception e) {
            androidActivity.handleResponse(0, e.toString());
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }
}
