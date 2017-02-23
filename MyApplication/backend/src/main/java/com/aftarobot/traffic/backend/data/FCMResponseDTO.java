package com.aftarobot.traffic.backend.data;

/**
 * Created by aubreymalabie on 11/9/16.
 */

public class FCMResponseDTO {
    String message;
    int statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
