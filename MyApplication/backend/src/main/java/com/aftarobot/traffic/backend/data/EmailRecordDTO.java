package com.aftarobot.traffic.backend.data;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 11/11/16.
 */

public class EmailRecordDTO implements Serializable{
    private Integer emailID;
    private String associationID;
    private String associationName;
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

    public String getAssociationID() {
        return associationID;
    }

    public void setAssociationID(String associationID) {
        this.associationID = associationID;
    }

    public String getAssociationName() {
        return associationName;
    }

    public void setAssociationName(String associationName) {
        this.associationName = associationName;
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

