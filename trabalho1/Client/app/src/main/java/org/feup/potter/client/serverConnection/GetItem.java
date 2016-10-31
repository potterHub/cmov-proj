package org.feup.potter.client.serverConnection;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

public class GetItem extends ServerConnectionAPI implements Runnable {

    // serverURL + path
    public GetItem(HttpResponse myclass) {
        super(myclass);
    }

    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL("http://" + address + ":" + port + "/" + getItemsPath);

            //writeText("GET " + url.toExternalForm());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setUseCaches(false);

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                String response = readStream(urlConnection.getInputStream());

                Log.d("GetItem","Server Response OK");

                androidActivity.handleResponse(responseCode, response);
            } else
                androidActivity.handleResponse(responseCode, "");
        } catch (Exception e) {
            androidActivity.handleResponse(0, e.toString());
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }
}