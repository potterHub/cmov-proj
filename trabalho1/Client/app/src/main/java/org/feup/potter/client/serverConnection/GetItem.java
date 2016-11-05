package org.feup.potter.client.serverConnection;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

public class GetItem extends ServerConnectionAPI implements Runnable {

    // serverURL + path
    protected HttpResponse androidActivity;
    protected String hash;
    public GetItem(HttpResponse myclass,String hash) {
        super();
        this.androidActivity = myclass;
        if(hash == null || hash.isEmpty())
            this.hash = "";
        else{
            this.hash = this.HASH_GET_FIELD + hash;
        }
    }

    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            Log.d("GetItem","Server request: " +this.hash);
            url = new URL("http://" + address + ":" + port + "/" + GET_ITEMS_PATH + this.hash);

            //writeText("GET " + url.toExternalForm());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setUseCaches(false);

            int responseCode = urlConnection.getResponseCode();
            String response = readStream(urlConnection.getInputStream());
            if (responseCode == 200) {
                Log.d("GetItem","Server Response OK");
            } else {
                Log.d("GetItem","Server Response ERROR");
            }
            androidActivity.handleResponse(responseCode, response);
        } catch (Exception e) {
            androidActivity.handleResponse(0, "");
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }
}