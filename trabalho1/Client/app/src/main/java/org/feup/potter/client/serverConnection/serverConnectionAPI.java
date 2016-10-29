package org.feup.potter.client.serverConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerConnectionAPI {

    private final String address = "localhost";// path to server root
    private final int port = 8080;

    private final String getItemsPath = "";
    private final String register = "";

    // how to call it
    /*
        GetItems getItems = new GetItems(this); // this must implement HttpResponse interface
        Thread thr = new Thread(GetItems);
        thr.start();


     */

    private class GetItems implements Runnable {
        private HttpResponse androidActivity;

        // serverURL + path
        GetItems(HttpResponse myclass) {
            this.androidActivity = myclass;
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


    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                // nao se para converter para json preciso do \n (eu acho que precisa line + "\n")
                response.append(line);
            }
        } catch (IOException e) {
            return e.getMessage();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    return e.getMessage();
                }
            }
        }
        return response.toString();
    }
}
