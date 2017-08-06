package com.pikachu.convenientdelivery.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * LogisticsInfo
 */

public class LogisticsInfo implements Parcelable {

    private String time;
    private String info;

    public LogisticsInfo(String time, String info) {
        this.time = time;
        this.info = info;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(info);
    }

    public static Creator<LogisticsInfo> CREATOR = new Creator<LogisticsInfo>() {
        @Override
        public LogisticsInfo createFromParcel(Parcel source) {
            return new LogisticsInfo(source.readString(), source.readString());
        }

        @Override
        public LogisticsInfo[] newArray(int size) {
            return new LogisticsInfo[size];
        }
    };
}
