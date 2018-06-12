package com.tns.espapp.database;

/**
 * Created by TNS on 01-Feb-18.
 */

public class OPEntryData {

    private int id;
    private int keyid;
    private String projectNo;
    private String mode;
    private String startkm;
    private String endkm;
    private String startkmImage;
    private String endkmImage;
    private String currentdate;
    private String customerName;
    private String optype;
    private String reasonforop;
    private String manuallocation;
    private String currentLocation;
    private String remark;
    private int flag ;

    public OPEntryData() {
    }

    public OPEntryData(int keyid,String projectNo, String mode, String startkm, String endkm, String startkmImage, String endkmImage, String currentdate, String customerName, String optype, String reasonforop, String manuallocation, String currentLocation, String remark, int flag) {
       this.keyid= keyid;
        this.projectNo = projectNo;
        this.mode = mode;
        this.startkm = startkm;
        this.endkm = endkm;
        this.startkmImage = startkmImage;
        this.endkmImage = endkmImage;
        this.currentdate = currentdate;
        this.customerName = customerName;
        this.optype = optype;
        this.reasonforop = reasonforop;
        this.manuallocation = manuallocation;
        this.currentLocation = currentLocation;
        this.remark = remark;
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKeyid() {
        return keyid;
    }

    public void setKeyid(int keyid) {
        this.keyid = keyid;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getStartkm() {
        return startkm;
    }

    public void setStartkm(String startkm) {
        this.startkm = startkm;
    }

    public String getEndkm() {
        return endkm;
    }

    public void setEndkm(String endkm) {
        this.endkm = endkm;
    }

    public String getStartkmImage() {
        return startkmImage;
    }

    public void setStartkmImage(String startkmImage) {
        this.startkmImage = startkmImage;
    }

    public String getEndkmImage() {
        return endkmImage;
    }

    public void setEndkmImage(String endkmImage) {
        this.endkmImage = endkmImage;
    }

    public String getCurrentdate() {
        return currentdate;
    }

    public void setCurrentdate(String currentdate) {
        this.currentdate = currentdate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOptype() {
        return optype;
    }

    public void setOptype(String optype) {
        this.optype = optype;
    }

    public String getReasonforop() {
        return reasonforop;
    }

    public void setReasonforop(String reasonforop) {
        this.reasonforop = reasonforop;
    }

    public String getManuallocation() {
        return manuallocation;
    }

    public void setManuallocation(String manuallocation) {
        this.manuallocation = manuallocation;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
