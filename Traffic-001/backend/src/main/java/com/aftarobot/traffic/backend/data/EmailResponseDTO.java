package com.aftarobot.traffic.backend.data;

/**
 * Created by aubreymalabie on 11/11/16.
 */

public class EmailResponseDTO {

    int statusCode;
    String message;

    public EmailResponseDTO() {
    }

    public EmailResponseDTO(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
