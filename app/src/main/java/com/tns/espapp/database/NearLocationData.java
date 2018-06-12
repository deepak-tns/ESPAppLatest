package com.tns.espapp.database;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by TNS on 28-Mar-18.
 */

public class NearLocationData implements Comparable<NearLocationData> {
    private double lat;
    private double log;
    private String name;
    private String address;

    public NearLocationData(double lat, double log, String name, String address) {
        this.lat = lat;
        this.log = log;
        this.name = name;
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public int compareTo(@NonNull NearLocationData nearLocationData) {
          return new Double(lat).compareTo( nearLocationData.getLat());
    }

    @Override
    public String toString() {
        return "[ lat =" + lat + ", lng=" + log + ", name=" + name + ", address=" + address + "]";
    }
}
