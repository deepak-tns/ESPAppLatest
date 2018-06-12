package com.tns.espapp.database;

/**
 * Created by TNS on 31-Jan-18.
 */

public class AttendenceRemoteData {

    private int id;
    private String empId;
    private String currentDate;
    private String inTime;
    private String outTime;
    private String intimeImage;
    private String outtimeImage;
    private String inlat;
    private String inlongi;
    private String outlat;
    private String outlongi;
    private String intimeRemark;
    private String outtimeRemark;
    private String intimetimeNear;
    private String outtimeNear;
    private int flag;


    public AttendenceRemoteData() {
    }

    public AttendenceRemoteData(String empId, String currentDate, String inTime, String outTime, String intimeImage, String outtimeImage, String inlat, String inlongi,String outlat, String outlongi,String intimeRemark,String outtimeRemark,String intimenear,String outtimenear, int flag) {
        this.empId = empId;
        this.currentDate = currentDate;
        this.inTime = inTime;
        this.outTime = outTime;
        this.intimeImage = intimeImage;
        this.outtimeImage = outtimeImage;
        this.inlat = inlat;
        this.inlongi = inlongi;
        this.outlat = outlat;
        this.outlongi = outlongi;
        this.intimeRemark = intimeRemark;
        this.outtimeRemark = outtimeRemark;
        this.intimetimeNear = intimenear;
        this.outtimeNear = outtimenear;
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getIntimeImage() {
        return intimeImage;
    }

    public void setIntimeImage(String intimeImage) {
        this.intimeImage = intimeImage;
    }

    public String getOuttimeImage() {
        return outtimeImage;
    }

    public void setOuttimeImage(String outtimeImage) {
        this.outtimeImage = outtimeImage;
    }

    public String getInlat() {
        return inlat;
    }

    public void setInlat(String inlat) {
        this.inlat = inlat;
    }

    public String getInlongi() {
        return inlongi;
    }

    public void setInlongi(String inlongi) {
        this.inlongi = inlongi;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getOutlat() {
        return outlat;
    }

    public void setOutlat(String outlat) {
        this.outlat = outlat;
    }

    public String getOutlongi() {
        return outlongi;
    }

    public void setOutlongi(String outlongi) {
        this.outlongi = outlongi;
    }

    public String getIntimeRemark() {
        return intimeRemark;
    }

    public void setIntimeRemark(String intimeRemark) {
        this.intimeRemark = intimeRemark;
    }

    public String getOuttimeRemark() {
        return outtimeRemark;
    }

    public void setOuttimeRemark(String outtimeRemark) {
        this.outtimeRemark = outtimeRemark;
    }

    public String getIntimetimeNear() {
        return intimetimeNear;
    }

    public void setIntimetimeNear(String intimetimeNear) {
        this.intimetimeNear = intimetimeNear;
    }

    public String getOuttimeNear() {
        return outtimeNear;
    }

    public void setOuttimeNear(String outtimeNear) {
        this.outtimeNear = outtimeNear;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
