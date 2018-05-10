package com.anyun.cloud.api;

import com.google.gson.annotations.SerializedName;

import java.sql.Time;

/**
 * Created by sxt on 16-6-13.
 */
public class Timestamp<T> {

    @SerializedName("timestamp")
    private  T  timestamp;


    public Timestamp(T  timestamp){
        this.timestamp=timestamp;
    }

    public T getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(T timestamp) {
        this.timestamp = timestamp;
    }

}
