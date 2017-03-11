package com.aftarobot.traffic.backend.data;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 11/11/16.
 */

public class EmailRecordDTO implements Serializable{
    private Integer emailID;
    private String trafficDepartmentID;
    private String trafficDepartmentName;
    private String sentTo;
    private String sentFrom;
    private String text;
    private Long dateSent;
    private String subject;

    public Integer getEmailID() {
        return emailID;
    }

    public void setEmailID(Integer emailID) {
        this.emailID = emailID;
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

    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }

    public String getSentFrom() {
        return sentFrom;
    }

    public void setSentFrom(String sentFrom) {
        this.sentFrom = sentFrom;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getDateSent() {
        return dateSent;
    }

    public void setDateSent(Long dateSent) {
        this.dateSent = dateSent;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

