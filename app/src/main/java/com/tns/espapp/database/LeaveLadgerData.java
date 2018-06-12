package com.tns.espapp.database;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TNS on 18-balanceCL-17.
 */

public class LeaveLadgerData {
    @SerializedName("Month")
    private String Month;
    @SerializedName("Opening EL")
    private String openingEL;
    @SerializedName("Opening CL")
    private String openingCL;
    @SerializedName("Credit EL")
    private String creditEL;
    @SerializedName("Credit CL")
    private String creditCL;
    @SerializedName("Taken EL")
    private String takenEL;
    @SerializedName("Taken CL")
    private String takenCL;
    @SerializedName("Balance EL")
    private String balanceEL;
    @SerializedName("Balance CL")
    private String balanceCL;
    @SerializedName("Permited By HR")
    private String permittedbyHR;
    @SerializedName("Total Credit")
    private String totalcredit;
    @SerializedName("Total Taken")
    private String totaltaken;
    @SerializedName("Deducted By HR")
    private String deductedByHR;
    @SerializedName("Total Balance")
    private String totalBalance;

    public LeaveLadgerData(String month, String openingEL, String openingCL, String creditEL, String creditCL, String takenEL, String takenCL, String balanceEL, String balanceCL, String permittedbyHR, String totalcredit, String totaltaken) {
        this.Month = month;
        this.openingEL = openingEL;
        this.openingCL = openingCL;
        this.creditEL = creditEL;
        this.creditCL = creditCL;
        this.takenEL = takenEL;
        this.takenCL = takenCL;
        this.balanceEL = balanceEL;
        this.balanceCL = balanceCL;
        this.permittedbyHR = permittedbyHR;
        this.totalcredit = totalcredit;
        this.totaltaken = totaltaken;
    }

    public LeaveLadgerData(String month, String openingEL, String openingCL, String creditEL, String creditCL, String takenEL, String takenCL, String balanceEL, String balanceCL, String permittedbyHR, String totalcredit, String totaltaken, String deductedByHR, String totalBalance) {
        this.Month = month;
        this.openingEL = openingEL;
        this.openingCL = openingCL;
        this.creditEL = creditEL;
        this.creditCL = creditCL;
        this.takenEL = takenEL;
        this.takenCL = takenCL;
        this.balanceEL = balanceEL;
        this.balanceCL = balanceCL;
        this.permittedbyHR = permittedbyHR;
        this.totalcredit = totalcredit;
        this.totaltaken = totaltaken;
        this.deductedByHR = deductedByHR;
        this.totalBalance = totalBalance;
    }

    public LeaveLadgerData() {
    }

    public String getmonth() {
        return Month;
    }

    public void setmonth(String month) {
        this.Month = month;
    }

    public String getopeningEL() {
        return openingEL;
    }

    public void setopeningEL(String openingEL) {
        this.openingEL = openingEL;
    }

    public String getopeningCL() {
        return openingCL;
    }

    public void setopeningCL(String openingCL) {
        this.openingCL = openingCL;
    }

    public String getcreditEL() {
        return creditEL;
    }

    public void setcreditEL(String creditEL) {
        this.creditEL = creditEL;
    }

    public String getcreditCL() {
        return creditCL;
    }

    public void setcreditCL(String creditCL) {
        this.creditCL = creditCL;
    }

    public String gettakenEL() {
        return takenEL;
    }

    public void settakenEL(String takenEL) {
        this.takenEL = takenEL;
    }

    public String gettakenCL() {
        return takenCL;
    }

    public void settakenCL(String takenCL) {
        this.takenCL = takenCL;
    }

    public String getbalanceEL() {
        return balanceEL;
    }

    public void setbalanceEL(String balanceEL) {
        this.balanceEL = balanceEL;
    }

    public String getbalanceCL() {
        return balanceCL;
    }

    public void setbalanceCL(String balanceCL) {
        this.balanceCL = balanceCL;
    }

    public String getpermittedbyHR() {
        return permittedbyHR;
    }

    public void setpermittedbyHR(String permittedbyHR) {
        this.permittedbyHR = permittedbyHR;
    }

    public String gettotalcredit() {
        return totalcredit;
    }

    public void settotalcredit(String totalcredit) {
        this.totalcredit = totalcredit;
    }

    public String gettotaltaken() {
        return totaltaken;
    }

    public void settotaltaken(String totaltaken) {
        this.totaltaken = totaltaken;
    }

    public String getDeductedByHR() {
        return deductedByHR;
    }

    public void setDeductedByHR(String deductedByHR) {
        this.deductedByHR = deductedByHR;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }
}
