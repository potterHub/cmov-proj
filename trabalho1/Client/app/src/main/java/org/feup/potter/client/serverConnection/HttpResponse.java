package org.feup.potter.client.serverConnection;

import org.json.JSONException;

// interface to implement (receives the response form server)
public interface HttpResponse {
    // response code and response
    void handleResponse(int code , String response);

    // jsonString = StringBuffer.toString();
    // jsonArray = new JSONArray(jsonString);
}
