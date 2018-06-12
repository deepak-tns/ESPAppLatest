package com.tns.espapp.database;

/**
 * Created by TNS on 1/3/2017.
 */

public class LatLongData {
    private   int id ;
    private String formno;
    private String date;
    private String lat;
    private String longi;
    private  int latlong_flag;
    private String totaldis;
    private String current_time_str;
    private String speed;
    private int countFlag;
    private String bearing_degree;
    private String bearing_name;


    public  LatLongData(){

    }

    public LatLongData(String formno, String date, String lat, String longi, int latlong_flag, String totaldis, String current_time_str, String speed,int countFlag,String bearing_degree,String bearing_name) {
        this.formno = formno;
        this.date = date;
        this.lat = lat;
        this.longi = longi;
        this.latlong_flag = latlong_flag;
        this.totaldis = totaldis;
        this.current_time_str = current_time_str;
        this.speed = speed;
        this.countFlag = countFlag;
        this.bearing_degree = bearing_degree;
        this.bearing_name = bearing_name;

    }

/*    public LatLongData(String formno, String date, String lat, String longi, int latlong_flag, String totaldis) {

        this.formno = formno;
        this.date = date;
        this.lat = lat;
        this.longi = longi;
        this.latlong_flag = latlong_flag;
        this.totaldis = totaldis;


    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFormno() {
        return formno;
    }

    public void setFormno(String formno) {
        this.formno = formno;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public int getLatlong_flag() {
        return latlong_flag;
    }

    public void setLatlong_flag(int latlong_flag) {
        this.latlong_flag = latlong_flag;
    }


    public String getTotaldis() {
        return totaldis;
    }

    public void setTotaldis(String totaldis) {
        this.totaldis = totaldis;
    }

    public String getCurrent_time_str() {
        return current_time_str;
    }

    public void setCurrent_time_str(String current_time_str) {
        this.current_time_str = current_time_str;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public int getCountFlag() {
        return countFlag;
    }

    public void setCountFlag(int countFlag) {
        this.countFlag = countFlag;
    }


    public String getBearing_degree() {
        return bearing_degree;
    }

    public void setBearing_degree(String bearing_degree) {
        this.bearing_degree = bearing_degree;
    }

    public String getBearing_name() {
        return bearing_name;
    }

    public void setBearing_name(String bearing_name) {
        this.bearing_name = bearing_name;
    }
}
