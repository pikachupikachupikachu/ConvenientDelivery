package com.pikachu.convenientdelivery.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.pikachu.convenientdelivery.BR;

/**
 * RecipientInfo
 */

public class RecipientInfo extends BaseObservable implements Parcelable {

    private String name;
    private String phone;
    private String address;

    public String getName() {
        return name;
    }

    @Bindable
    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getPhone() {
        return phone;
    }

    @Bindable
    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    public String getAddress() {
        return address;
    }

    @Bindable
    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(address);
    }

    public static final Parcelable.Creator<RecipientInfo> CREATOR = new Parcelable.Creator<RecipientInfo>() {

        @Override
        public RecipientInfo createFromParcel(Parcel source) {
            RecipientInfo recipientInfo = new RecipientInfo();
            recipientInfo.name = source.readString();
            recipientInfo.phone = source.readString();
            recipientInfo.address = source.readString();
            return recipientInfo;
        }

        @Override
        public RecipientInfo[] newArray(int size) {
            return new RecipientInfo[size];
        }
    };
}
