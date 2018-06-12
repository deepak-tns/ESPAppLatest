package com.tns.espapp.database;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TNS on 20-Sep-17.
 */

public class LeaveTransactionData {

 // "Approved Days":1,"Approved":"Yes","Action":""},{"Emp Name\/Id":"Deepak Sachan (16865) ","Dept":"TS","Reporting Head":"Randhir Kumar (11732)","Leasve Form":"06-Sep-2017","Leave To":"06-Sep-2017","Apply Days":1,"Sec From":"06-Sep-2017","Sec To":"06-Sep-2017","Approved Days":1,"Approved":"Yes","Action":""},{"Emp Name\/Id":"Deepak Sachan (16865) ","Dept":"TS","Reporting Head":"Randhir Kumar (11732)","Leasve Form":"29-Sep-2017","Leave To":"29-Sep-2017","Apply Days":1,"Sec From":"29-Sep-2017","Sec To":"29-Sep-2017","Approved Days":1,"Approved":"Yes","Action":""}]

    @SerializedName("Emp Name/Id")
    private String empname;

    @SerializedName("Dept")
    private String dept;
    @SerializedName("Reporting Head")
    private String repotingHead;
    @SerializedName("Leasve Form")
    private String leaveForm;
    @SerializedName("Leave To")
    private String leaveTo;
    @SerializedName("Apply Days")
    private String applyDays;
    @SerializedName("Sec From")
    private String secfrom;
    @SerializedName("Sec To")
    private String secto;
    @SerializedName("Approved Days")
    private String approvedDays;
    @SerializedName("Approved")
    private String approved;
    @SerializedName("Action")
    private String action;

    private int flag;


    private String header;

    public LeaveTransactionData() {
    }

    public LeaveTransactionData(String header) {

        this.header = header;
    }

    public LeaveTransactionData(String empname, String dept, String repotingHead, String leaveForm, String leaveTo, String applyDays, String secfrom, String secto, String approvedDays, String approved, String action, int flag) {
        this.empname = empname;
        this.dept = dept;
        this.repotingHead = repotingHead;
        this.leaveForm = leaveForm;
        this.leaveTo = leaveTo;
        this.applyDays = applyDays;
        this.secfrom = secfrom;
        this.secto = secto;
        this.approvedDays = approvedDays;
        this.approved = approved;
        this.action = action;
        this.flag = flag;
    }

    public String getEmpname() {
        return empname;
    }

    public String getDept() {
        return dept;
    }

    public String getRepotingHead() {
        return repotingHead;
    }

    public String getLeaveForm() {
        return leaveForm;
    }

    public String getLeaveTo() {
        return leaveTo;
    }

    public String getApplyDays() {
        return applyDays;
    }

    public String getSecfrom() {
        return secfrom;
    }

    public String getSecto() {
        return secto;
    }

    public String getApprovedDays() {
        return approvedDays;
    }

    public String getApproved() {
        return approved;
    }

    public String getAction() {
        return action;
    }

    public int getFlag() {
        return flag;
    }
}
