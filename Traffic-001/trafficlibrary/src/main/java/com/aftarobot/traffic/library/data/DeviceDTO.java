package com.aftarobot.traffic.library.data;

import android.os.Build;

/**
 * Created by aubreymalabie on 3/9/17.
 */

public class DeviceDTO {
    private String model, manufacturer, androidVersion, serialNumber;
    private String trafficDepartmentID, userID, email;
    private String token;
    private boolean active;

    public DeviceDTO() {
        setInfo();
    }

    public void setInfo() {
        try {
            setModel(Build.MODEL);
            setManufacturer(Build.MANUFACTURER);
            setSerialNumber(Build.SERIAL);
            setAndroidVersion(Build.VERSION.RELEASE);
        } catch (Exception e) {
            //ignore
        }
    }
    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTrafficDepartmentID() {
        return trafficDepartmentID;
    }

    public void setTrafficDepartmentID(String trafficDepartmentID) {
        this.trafficDepartmentID = trafficDepartmentID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
