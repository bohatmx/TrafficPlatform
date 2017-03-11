package com.aftarobot.traffic.backend.data;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 11/9/16.
 */


@Entity
public class FCMessageDTO implements Serializable{

    @Id Long id;
    @Index String trafficDepartmentID;
    @Index Long date;
    List<String> userIDs = new ArrayList<>();
    String trafficDepartmentName;
    Data data;
    int userType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrafficDepartmentID() {
        return trafficDepartmentID;
    }

    public void setTrafficDepartmentID(String trafficDepartmentID) {
        this.trafficDepartmentID = trafficDepartmentID;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public List<String> getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(List<String> userIDs) {
        this.userIDs = userIDs;
    }

    public String getTrafficDepartmentName() {
        return trafficDepartmentName;
    }

    public void setTrafficDepartmentName(String trafficDepartmentName) {
        this.trafficDepartmentName = trafficDepartmentName;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
