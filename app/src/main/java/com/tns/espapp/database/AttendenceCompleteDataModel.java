package com.tns.espapp.database;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Codeslay-03 on 10/10/2017.
 */

public class AttendenceCompleteDataModel implements Parcelable {

    private List<AttendenceDateTimeModel> attendenceDateTimeModelArrayList;
    private int remainingMinuts;
    private int totalTakenMinuts;

    public AttendenceCompleteDataModel() {

    }


    protected AttendenceCompleteDataModel(Parcel in) {
        attendenceDateTimeModelArrayList = in.createTypedArrayList(AttendenceDateTimeModel.CREATOR);
        remainingMinuts = in.readInt();
        totalTakenMinuts = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(attendenceDateTimeModelArrayList);
        dest.writeInt(remainingMinuts);
        dest.writeInt(totalTakenMinuts);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AttendenceCompleteDataModel> CREATOR = new Creator<AttendenceCompleteDataModel>() {
        @Override
        public AttendenceCompleteDataModel createFromParcel(Parcel in) {
            return new AttendenceCompleteDataModel(in);
        }

        @Override
        public AttendenceCompleteDataModel[] newArray(int size) {
            return new AttendenceCompleteDataModel[size];
        }
    };

    public List<AttendenceDateTimeModel> getAttendenceDateTimeModelArrayList() {
        return attendenceDateTimeModelArrayList;
    }

    public void setAttendenceDateTimeModelArrayList(List<AttendenceDateTimeModel> attendenceDateTimeModelArrayList) {
        this.attendenceDateTimeModelArrayList = attendenceDateTimeModelArrayList;
    }

    public int getRemainingMinuts() {
        return remainingMinuts;
    }

    public void setRemainingMinuts(int remainingMinuts) {
        this.remainingMinuts = remainingMinuts;
    }

    public int getTotalTakenMinuts() {
        return totalTakenMinuts;
    }

    public void setTotalTakenMinuts(int totalTakenMinuts) {
        this.totalTakenMinuts = totalTakenMinuts;
    }
}
