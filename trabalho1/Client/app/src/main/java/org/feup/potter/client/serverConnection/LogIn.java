package org.feup.potter.client.serverConnection;

import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LogIn extends ServerConnectionAPI implements Runnable {

    private String username;
    private String password;

    protected HttpResponsePassword androidActivity;

    // serverURL + path
    public LogIn(HttpResponsePassword myclass, String username, String password) {
        super();
        this.androidActivity = myclass;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL("http://" + address + ":" + port + "/" + LOG_IN_PATH);

            //writeText("GET " + url.toExternalForm());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setUseCaches(false);

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("username", username);
            jsonObj.put("password", password);

            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(jsonObj.toString());
            outputStream.flush();
            outputStream.close();


            int responseCode = urlConnection.getResponseCode();
            String response = readStream(urlConnection.getInputStream());
            if (responseCode == 200) {
                Log.d("LogIn", "Server Response OK");
            } else {
                Log.d("LogIn", "Server Response ERROR");
            }
            androidActivity.handleResponse(responseCode, response, password);
        } catch (Exception e) {
            androidActivity.handleResponse(0, "Error connecting to server.", "");
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }
}
