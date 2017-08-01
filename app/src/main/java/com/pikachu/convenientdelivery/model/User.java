package com.pikachu.convenientdelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 83624 on 2017/7/16.
 */

public class User extends BmobUser implements Parcelable {

    private String nick;
    private String phone;
    private BmobFile avatar;
    private String city;
    private String address;
    private String intro;
    private List<RecipientInfo> recipientInfoList;
    private Boolean isCourier = false;
    private Boolean isIdCardVerify = false;
    private Boolean isAlipayVerify = false;
    private Boolean isSesameVerify = false;

    public User() {
        recipientInfoList = new ArrayList<>();
    }

    public User(Parcel in) {
        recipientInfoList = new ArrayList<>();
        setNick(in.readString());
        setPhone(in.readString());
        setAvatar((BmobFile) in.readSerializable());
        setCity(in.readString());
        setAddress(in.readString());
        setIntro(in.readString());
        setCourier(in.readByte() != 0);
        setIdCardVerify(in.readByte() != 0);
        setAlipayVerify(in.readByte() != 0);
        setSesameVerify(in.readByte() != 0);
        in.readList(getRecipientInfoList(), RecipientInfo.class.getClassLoader());
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getMobilePhoneNumber() {
        return getPhone();
    }

    @Override
    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        setPhone(mobilePhoneNumber);
    }

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public List<RecipientInfo> getRecipientInfoList() {
        return recipientInfoList;
    }

    public void setRecipientInfoList(List<RecipientInfo> recipientInfoList) {
        this.recipientInfoList = recipientInfoList;
    }

    public void setRecipientInfo(int position, RecipientInfo recipientInfo) {
        recipientInfoList.set(position, recipientInfo);
    }

    public void addRecipientInfo(RecipientInfo recipientInfo) {
        recipientInfoList.add(recipientInfo);
    }

    public void removeRecipientInfo(int position) {
        recipientInfoList.remove(position);
    }

    public Boolean getCourier() {
        return isCourier;
    }

    public void setCourier(Boolean courier) {
        isCourier = courier;
    }

    public Boolean getIdCardVerify() {
        return isIdCardVerify;
    }

    public void setIdCardVerify(Boolean idCardVerify) {
        isIdCardVerify = idCardVerify;
    }

    public Boolean getAlipayVerify() {
        return isAlipayVerify;
    }

    public void setAlipayVerify(Boolean alipayVerify) {
        isAlipayVerify = alipayVerify;
    }

    public Boolean getSesameVerify() {
        return isSesameVerify;
    }

    public void setSesameVerify(Boolean sesameVerify) {
        isSesameVerify = sesameVerify;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getNick());
        dest.writeString(getPhone());
        dest.writeSerializable(getAvatar());
        dest.writeString(getCity());
        dest.writeString(getAddress());
        dest.writeString(getIntro());
        dest.writeByte((byte) (getCourier() ? 1 : 0));
        dest.writeByte((byte) (getIdCardVerify() ? 1 : 0));
        dest.writeByte((byte) (getAlipayVerify() ? 1 : 0));
        dest.writeByte((byte) (getSesameVerify() ? 1 : 0));
        dest.writeList(getRecipientInfoList());
    }

    public static Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
