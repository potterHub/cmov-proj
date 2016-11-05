package org.feup.potter.client.serverConnection;


// interface to implement (receives the response form server)
public interface HttpResponsePassword {
    // response code and response
    void handleResponse(int code , String response, String password);
    // jsonString = StringBuffer.toString();
    // jsonArray = new JSONArray(jsonString);
}
