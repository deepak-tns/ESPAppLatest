package com.tns.espapp.database;

/**
 * Created by TNS on 04-Apr-18.
 */

public class AttendanceSettingData {
    private int id;
    private String lat;
    private String log;
    private String radialdistance;
    private String type;

    public AttendanceSettingData() {
    }

    public AttendanceSettingData(String lat, String log, String radialdistance, String type) {
        this.lat = lat;
        this.log = log;
        this.radialdistance = radialdistance;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getRadialdistance() {
        return radialdistance;
    }

    public void setRadialdistance(String radialdistance) {
        this.radialdistance = radialdistance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
