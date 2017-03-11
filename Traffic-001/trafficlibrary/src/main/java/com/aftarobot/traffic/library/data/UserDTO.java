package com.aftarobot.traffic.library.data;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by aubreymalabie on 2/23/17.
 */

public class UserDTO implements Serializable, Comparable<UserDTO> {
    String departmentID, departmentName, userID, uid,
            firstName, middleName, lastName, stringDateRegistered,
            email, cellphone, userDescription, password, fullName;
    private Long dateRegistered, lastUpdated;
    private int userType;
    private HashMap<String, DeviceDTO> devices;
    private HashMap<String, PhotoDTO> photos;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
    public UserDTO() {
        dateRegistered = new Date().getTime();
        stringDateRegistered = sdf.format(new Date());
    }

    public static final int
            TRAFFIC_OFFICER = 1,
            ADMINISTRATOR = 2,
            MANAGEMENT = 3,
            POLITICAL_OFFICIAL = 4;
    public static final String
            DESC_TRAFFIC_OFFICER = "Traffic Officer",
            DESC_ADMINISTRATOR = "Administrator",
            DESC_MANAGEMENT = "Management",
            DESC_POLITICAL_OFFICIAL = "Political Official";

    public String getStringDateRegistered() {
        return stringDateRegistered;
    }

    public void setStringDateRegistered(String stringDateRegistered) {
        this.stringDateRegistered = stringDateRegistered;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        if (fullName != null) {
            return fullName;
        }
        if (middleName != null) {
            return firstName.concat(" ").concat(middleName).concat(" ").concat(lastName);
        }
        return firstName.concat(" ").concat(lastName);
    }
    public void setFullName(String fullName) {
          this.fullName = fullName;
    }
    public HashMap<String, DeviceDTO> getDevices() {
        return devices;
    }

    public void setDevices(HashMap<String, DeviceDTO> devices) {
        this.devices = devices;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
        switch (userType) {
            case TRAFFIC_OFFICER:
                userDescription = DESC_TRAFFIC_OFFICER;
                break;
            case ADMINISTRATOR:
                userDescription = DESC_ADMINISTRATOR;
                break;
            case MANAGEMENT:
                userDescription = DESC_MANAGEMENT;
                break;
            case POLITICAL_OFFICIAL:
                userDescription = DESC_POLITICAL_OFFICIAL;
                break;
        }
    }

    public HashMap<String, PhotoDTO> getPhotos() {
        return photos;
    }

    public void setPhotos(HashMap<String, PhotoDTO> photos) {
        this.photos = photos;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public Long getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Long dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public int compareTo(@NonNull UserDTO u) {
        String n1 = this.lastName.concat(this.firstName);
        String n2 = u.lastName.concat(u.firstName);
        return n1.compareTo(n2);
    }
}
