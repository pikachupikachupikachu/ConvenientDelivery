package com.pikachu.convenientdelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.location.AMapLocation;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * PathRecord
 */

public class PathRecord extends BmobObject implements Parcelable {
    private AMapLocation mStartPoint;
    private AMapLocation mEndPoint;
    private List<AMapLocation> mPathLinePoints = new ArrayList<>();
    private String mDistance;
    private String mDuration;
    private String mAveragespeed;
    private String mDate;
    private int mId = 0;

    public PathRecord() {}

    public PathRecord(Parcel in) {
        setStartpoint((AMapLocation) in.readParcelable(AMapLocation.class.getClassLoader()));
        setEndpoint((AMapLocation) in.readParcelable(AMapLocation.class.getClassLoader()));
        in.readList(mPathLinePoints, AMapLocation.class.getClassLoader());
        setDistance(in.readString());
        setDuration(in.readString());
        setAveragespeed(in.readString());
        setDate(in.readString());
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public AMapLocation getStartpoint() {
        return mStartPoint;
    }

    public void setStartpoint(AMapLocation startpoint) {
        this.mStartPoint = startpoint;
    }

    public AMapLocation getEndpoint() {
        return mEndPoint;
    }

    public void setEndpoint(AMapLocation endpoint) {
        this.mEndPoint = endpoint;
    }

    public List<AMapLocation> getPathline() {
        return mPathLinePoints;
    }

    public void setPathline(List<AMapLocation> pathline) {
        this.mPathLinePoints = pathline;
    }

    public String getDistance() {
        return mDistance;
    }

    public void setDistance(String distance) {
        this.mDistance = distance;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        this.mDuration = duration;
    }

    public String getAveragespeed() {
        return mAveragespeed;
    }

    public void setAveragespeed(String averagespeed) {
        this.mAveragespeed = averagespeed;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public void addpoint(AMapLocation point) {
        mPathLinePoints.add(point);
    }

    @Override
    public String toString() {
        StringBuilder record = new StringBuilder();
        record.append("recordSize:" + getPathline().size() + ", ");
        record.append("distance:" + getDistance() + "m, ");
        record.append("duration:" + getDuration() + "s");
        return record.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(getStartpoint(), flags);
        dest.writeParcelable(getEndpoint(), flags);
        dest.writeList(getPathline());
        dest.writeString(getDistance());
        dest.writeString(getDuration());
        dest.writeString(getAveragespeed());
        dest.writeString(getDate());
    }

    public static Creator<PathRecord> CREATOR = new Creator<PathRecord>() {

        @Override
        public PathRecord createFromParcel(Parcel source) {
            return new PathRecord(source);
        }

        @Override
        public PathRecord[] newArray(int size) {
            return new PathRecord[size];
        }
    };
}

