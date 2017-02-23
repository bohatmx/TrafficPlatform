package com.aftarobot.traffic.platform.library.data;

import java.util.HashMap;

/**
 * Created by aubreymalabie on 2/23/17.
 */

public class UserDTO {
    String trafficDepartmentID, trafficDepartmentName, userID, uid,
            firstName, middleName, lastName, email, cellphone;
    private Long dateRegistered, lastUpdated;
    private int userType;

    private HashMap<String, PhotoDTO> photos;

    public static final int
            TRAFFIC_OFFICER = 1,
            ADMINISTRATOR = 2,
            MANAGEMENT = 3,
            POLITICAL_OFFICIAL = 4;

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
    }

    public HashMap<String, PhotoDTO> getPhotos() {
        return photos;
    }

    public void setPhotos(HashMap<String, PhotoDTO> photos) {
        this.photos = photos;
    }

    public String getTrafficDepartmentID() {
        return trafficDepartmentID;
    }

    public void setTrafficDepartmentID(String trafficDepartmentID) {
        this.trafficDepartmentID = trafficDepartmentID;
    }

    public String getTrafficDepartmentName() {
        return trafficDepartmentName;
    }

    public void setTrafficDepartmentName(String trafficDepartmentName) {
        this.trafficDepartmentName = trafficDepartmentName;
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
}
