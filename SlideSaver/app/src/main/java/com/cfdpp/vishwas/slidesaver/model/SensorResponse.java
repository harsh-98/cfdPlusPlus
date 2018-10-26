package com.cfdpp.vishwas.slidesaver.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vishwas on 26/10/18.
 */

public class SensorResponse {
    @SerializedName(value = "lat")
    private float _lat;

    @SerializedName(value = "long")
    private float _long;

    @SerializedName(value = "riskFactor")
    private float riskFactor;

    @SerializedName(value = "lastUpdated")
    private long lastUpdated;

    @SerializedName(value = "deviceID")
    private String deviceID;

    public float get_lat() {
        return _lat;
    }

    public void set_lat(float _lat) {
        this._lat = _lat;
    }

    public float get_long() {
        return _long;
    }

    public void set_long(float _long) {
        this._long = _long;
    }

    public float getRiskFactor() {
        return riskFactor;
    }

    public void setRiskFactor(float riskFactor) {
        this.riskFactor = riskFactor;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
