package com.aftarobot.traffic.backend.data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 11/9/16.
 */

@Entity
public class FCMUserDTO implements Serializable{


    @Id String fcmID;
    @Index String userID;
    @Index String serialNumber;
    @Index String associationID;

    String name, token;
    Long date;
    String deviceModel, androidVersion, manufacturer;

    public String getFcmID() {
        return fcmID;
    }

    public void setFcmID(String fcmID) {
        if (fcmID != null && fcmID.length() > 0) {
            this.fcmID = fcmID;
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (userID != null) {
            sb.append(userID);
        }
        if (serialNumber != null) {
            sb.append("@").append(serialNumber);
        }
        this.fcmID = sb.toString();
    }

    public String getAssociationID() {
        return associationID;
    }

    public void setAssociationID(String associationID) {
        this.associationID = associationID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
