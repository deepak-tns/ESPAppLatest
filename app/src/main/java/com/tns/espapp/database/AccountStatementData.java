package com.tns.espapp.database;

/**
 * Created by TNS on 09-Oct-17.
 */

public class AccountStatementData {

    private String    Date;
    private String     Type;
    private String    Particulars;
    private String    Details;
    private String    Debit;
    private String  Credit;
    private String   Balance;

    public AccountStatementData() {
    }

    public AccountStatementData(String date) {
        Date = date;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getParticulars() {
        return Particulars;
    }

    public void setParticulars(String particulars) {
        Particulars = particulars;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getDebit() {
        return Debit;
    }

    public void setDebit(String debit) {
        Debit = debit;
    }

    public String getCredit() {
        return Credit;
    }

    public void setCredit(String credit) {
        Credit = credit;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }
}
