package com.tns.espapp.database;

/**
 * Created by TNS on 05-Oct-17.
 */

public class BillInfoData {

        private  String Month;
        private String  Cycle;
    private String curAmount;
    private String curPassingAmount;

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getCycle() {
        return Cycle;
    }

    public void setCycle(String cycle) {
        Cycle = cycle;
    }

    public String getCurAmount() {
        return curAmount;
    }

    public void setCurAmount(String curAmount) {
        this.curAmount = curAmount;
    }

    public String getCurPassingAmount() {
        return curPassingAmount;
    }

    public void setCurPassingAmount(String curPassingAmount) {
        this.curPassingAmount = curPassingAmount;
    }
}
