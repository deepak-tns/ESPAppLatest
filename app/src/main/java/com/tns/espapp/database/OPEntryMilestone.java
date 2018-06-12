package com.tns.espapp.database;

/**
 * Created by TNS on 05-Feb-18.
 */

public class OPEntryMilestone {

        private   int id ;
        private String formno;
        private String date;
        private String lat;
        private String longi;
        private String location;
        private String nearestLocation;
        private  int milestone_flag;


        public  OPEntryMilestone(){

        }

        public OPEntryMilestone(String formno, String date, String lat, String longi,String location,String nearestLocation, int milestone_flag) {
            this.formno = formno;
            this.date = date;
            this.lat = lat;
            this.longi = longi;
            this.milestone_flag = milestone_flag;
            this. location= location;
            this.nearestLocation = nearestLocation;

        }

/*    public LatLongData(String formno, String date, String lat, String longi, int milestone_flag, String totaldis) {

        this.formno = formno;
        this.date = date;
        this.lat = lat;
        this.longi = longi;
        this.milestone_flag = milestone_flag;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMilestone_flag() {
            return milestone_flag;
        }

        public void setMilestone_flag(int milestone_flag) {
            this.milestone_flag = milestone_flag;
        }

    public String getNearestLocation() {
        return nearestLocation;
    }

    public void setNearestLocation(String nearestLocation) {
        this.nearestLocation = nearestLocation;
    }
}
