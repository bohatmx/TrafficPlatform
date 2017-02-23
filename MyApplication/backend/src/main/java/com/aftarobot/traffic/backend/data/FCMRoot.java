package com.aftarobot.traffic.backend.data;

import java.util.ArrayList;

/**
 * Created by aubreymalabie on 11/9/16.
 */

public class FCMRoot {
    private long multicast_id;

    public long getMulticastId() {
        return this.multicast_id;
    }

    public void setMulticastId(long multicast_id) {
        this.multicast_id = multicast_id;
    }

    private int success;

    public int getSuccess() {
        return this.success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    private int failure;

    public int getFailure() {
        return this.failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    private int canonical_ids;

    public int getCanonicalIds() {
        return this.canonical_ids;
    }

    public void setCanonicalIds(int canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    private ArrayList<Result> results;

    public ArrayList<Result> getResults() {
        return this.results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }
}
