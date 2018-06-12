package com.tns.espapp.BusinessIntelligence;

/**
 * Created by TNS on 11-Apr-18.
 */

public class AttendanceReportPiechart {
   // private String tdays;
    private String Absent;
    private String AbsentAB;
    private String AbsentLWP;
    private String Loaded;
    private String Idle;
    private String Unloaded;
    private String Transfer;


    public AttendanceReportPiechart(String absent, String absentAB, String absentLWP, String loaded, String idle, String unloaded, String transfer) {
        Absent = absent;
        AbsentAB = absentAB;
        AbsentLWP = absentLWP;
        Loaded = loaded;
        Idle = idle;
        Unloaded = unloaded;
        Transfer = transfer;
    }

    public String getAbsent() {
        return Absent;
    }

    public void setAbsent(String absent) {
        Absent = absent;
    }

    public String getAbsentAB() {
        return AbsentAB;
    }

    public void setAbsentAB(String absentAB) {
        AbsentAB = absentAB;
    }

    public String getAbsentLWP() {
        return AbsentLWP;
    }

    public void setAbsentLWP(String absentLWP) {
        AbsentLWP = absentLWP;
    }

    public String getLoaded() {
        return Loaded;
    }

    public void setLoaded(String loaded) {
        Loaded = loaded;
    }

    public String getIdle() {
        return Idle;
    }

    public void setIdle(String idle) {
        Idle = idle;
    }

    public String getUnloaded() {
        return Unloaded;
    }

    public void setUnloaded(String unloaded) {
        Unloaded = unloaded;
    }

    public String getTransfer() {
        return Transfer;
    }

    public void setTransfer(String transfer) {
        Transfer = transfer;
    }
}
