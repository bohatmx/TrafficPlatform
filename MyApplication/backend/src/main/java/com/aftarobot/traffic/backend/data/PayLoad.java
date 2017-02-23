package com.aftarobot.traffic.backend.data;

import com.googlecode.objectify.annotation.Entity;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 11/9/16.
 */

@Entity
public class PayLoad implements Serializable {
    String to;
    Data data;

    public PayLoad() {
    }

    public PayLoad(String to, Data data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
