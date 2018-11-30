package com.bluebuddy.models;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by admin on 6/24/2017.
 */

public class PushNotification {
    private long multicast_id;
    private int success;
    private int failure;
    private long canonical_ids;
    private ArrayList<Pair> results;

    public long getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(long multicast_id) {
        this.multicast_id = multicast_id;
    }

    public int isSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int isFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public long getCanonical_ids() {
        return canonical_ids;
    }

    public void setCanonical_ids(long canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    public ArrayList<Pair> getResults() {
        return results;
    }

    public void setResults(ArrayList<Pair> results) {
        this.results = results;
    }
}
