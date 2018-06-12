package com.tns.espapp.database;

/**
 * Created by TNS on 08-Jun-18.
 */

public class GPSCheckStatusData {
    private int id;
    private String time;
    private String status;

    public GPSCheckStatusData() {
    }

    public GPSCheckStatusData(String time, String status) {

        this.time = time;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
