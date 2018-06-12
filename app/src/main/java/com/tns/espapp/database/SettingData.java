package com.tns.espapp.database;

/**
 * Created by TNS on 17-Jun-17.
 */

public class SettingData {

    private   int sett_id ;

    private int sett_Gpsenabled;
    private int sett_Gpsinterval;
    private int sett_Gpsspeed;
    private String sett_status;


    public SettingData() {
    }

    public SettingData(int sett_Gpsenabled, int sett_Gpsinterval, int sett_Gpsspeed, String sett_status) {
        this.sett_Gpsenabled = sett_Gpsenabled;
        this.sett_Gpsinterval = sett_Gpsinterval;
        this.sett_Gpsspeed = sett_Gpsspeed;
        this.sett_status = sett_status;
    }

    public int getSett_id() {
        return sett_id;
    }

    public void setSett_id(int sett_id) {
        this.sett_id = sett_id;
    }

    public int getSett_Gpsenabled() {
        return sett_Gpsenabled;
    }

    public void setSett_Gpsenabled(int sett_Gpsenabled) {
        this.sett_Gpsenabled = sett_Gpsenabled;
    }

    public int getSett_Gpsinterval() {
        return sett_Gpsinterval;
    }

    public void setSett_Gpsinterval(int sett_Gpsinterval) {
        this.sett_Gpsinterval = sett_Gpsinterval;
    }

    public int getSett_Gpsspeed() {
        return sett_Gpsspeed;
    }

    public void setSett_Gpsspeed(int sett_Gpsspeed) {
        this.sett_Gpsspeed = sett_Gpsspeed;
    }

    public String getSett_status() {
        return sett_status;
    }

    public void setSett_status(String sett_status) {
        this.sett_status = sett_status;
    }
}
