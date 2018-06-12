package com.tns.espapp.database;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Codeslay-03 on 10/10/2017.
 */

public class AttendenceDateTimeModel implements Parcelable {
    private int HoliDay;
    private int Sunday;
    private String Date;
    private String Status;
    private String InTime;
    private String OutTime;
    private int MinuteConsume;


    protected AttendenceDateTimeModel(Parcel in) {
        HoliDay = in.readInt();
        Sunday = in.readInt();
        Date = in.readString();
        Status = in.readString();
        InTime = in.readString();
        OutTime = in.readString();
        MinuteConsume= in.readInt();

    }
    public AttendenceDateTimeModel() {

    }

    public static final Creator<AttendenceDateTimeModel> CREATOR = new Creator<AttendenceDateTimeModel>() {
        @Override
        public AttendenceDateTimeModel createFromParcel(Parcel in) {
            return new AttendenceDateTimeModel(in);
        }

        @Override
        public AttendenceDateTimeModel[] newArray(int size) {
            return new AttendenceDateTimeModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(HoliDay);
        dest.writeInt(Sunday);
        dest.writeString(Date);
        dest.writeString(Status);
        dest.writeString(InTime);
        dest.writeString(OutTime);
        dest.writeInt(MinuteConsume);



    }

    public int getHoliDay() {
        return HoliDay;
    }

    public void setHoliDay(int holiDay) {
        HoliDay = holiDay;
    }

    public int getSunday() {
        return Sunday;
    }

    public void setSunday(int sunday) {
        Sunday = sunday;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getInTime() {
        return InTime;
    }

    public void setInTime(String inTime) {
        InTime = inTime;
    }

    public String getOutTime() {
        return OutTime;
    }

    public void setOutTime(String outTime) {
        OutTime = outTime;
    }

    public int getMinuteConsume() {
        return MinuteConsume;
    }

    public void setMinuteConsume(int minuteConsume) {
        MinuteConsume = minuteConsume;
    }
}
