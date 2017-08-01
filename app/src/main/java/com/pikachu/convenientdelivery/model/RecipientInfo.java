package com.pikachu.convenientdelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * RecipientInfo
 */

public class RecipientInfo implements Parcelable {

    private String name;
    private String phone;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getName());
        dest.writeString(getPhone());
        dest.writeString(getAddress());
    }

    public static final Parcelable.Creator<RecipientInfo> CREATOR = new Parcelable.Creator<RecipientInfo>() {
        @Override
        public RecipientInfo createFromParcel(Parcel source) {
            RecipientInfo recipientInfo = new RecipientInfo();
            recipientInfo.setName(source.readString());
            recipientInfo.setPhone(source.readString());
            recipientInfo.setAddress(source.readString());
            return recipientInfo;
        }

        @Override
        public RecipientInfo[] newArray(int size) {
            return new RecipientInfo[size];
        }
    };
}
