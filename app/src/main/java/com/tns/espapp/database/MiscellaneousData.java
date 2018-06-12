package com.tns.espapp.database;

/**
 * Created by TNS on 12-Oct-17.
 */

public class MiscellaneousData {

  private String  Days;
    private String Date;
    private String  Message;
    private String FilePath;

  public String getDays() {
    return Days;
  }

  public void setDays(String days) {
    Days = days;
  }

  public String getDate() {
    return Date;
  }

  public void setDate(String date) {
    Date = date;
  }

  public String getMessage() {
    return Message;
  }

  public void setMessage(String message) {
    Message = message;
  }

  public String getFilePath() {
    return FilePath;
  }

  public void setFilePath(String filePath) {
    FilePath = filePath;
  }
}
