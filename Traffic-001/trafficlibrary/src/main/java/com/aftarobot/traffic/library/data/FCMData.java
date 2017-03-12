package com.aftarobot.traffic.library.data;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 3/12/17.
 */

public class FCMData implements Serializable{
    private String fromUser, message, title,
            announcementID, userID, json;
    private long date, expiryDate;
    private int messageType;
    /**
     * FCM message types
     */
    public static final int
            ANNOUNCEMENT = 101,
            EMERGENCY = 102,
            ACCIDENT = 103,
            INSTRUCTION = 104, WELCOME = 105;

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnnouncementID() {
        return announcementID;
    }

    public void setAnnouncementID(String announcementID) {
        this.announcementID = announcementID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
