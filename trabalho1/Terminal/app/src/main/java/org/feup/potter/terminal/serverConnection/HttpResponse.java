package org.feup.potter.terminal.serverConnection;

public interface HttpResponse {
    void handleResponse(int code, String response);
}
