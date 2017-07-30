package com.pikachu.convenientdelivery.model;

import android.databinding.ObservableField;
import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 83624 on 2017/7/16.
 */

public class User extends BmobUser implements Parcelable {

    private final ObservableField<String> nick = new ObservableField<>();
    private final ObservableField<String> phone = new ObservableField<>();
    private final ObservableField<BmobFile> avatar = new ObservableField<>();
    private final ObservableField<String> city = new ObservableField<>();
    private final ObservableField<String> address = new ObservableField<>();
    private final ObservableField<String> intro = new ObservableField<>();

    private final ObservableField<Boolean> isIdCardVerify = new ObservableField<>(false);
    private final ObservableField<Boolean> isAlipayVerify = new ObservableField<>(false);
    private final ObservableField<Boolean> isSesameVerify = new ObservableField<>(false);

    public User(){}

    public User(String nick, String phone, BmobFile avatar, String city, String address, String intro, boolean isIdCardVerify, boolean isAlipayVerify, boolean isSesameVerify) {
        this.nick.set(nick);
        this.phone.set(phone);
        this.avatar.set(avatar);
        this.city.set(city);
        this.address.set(address);
        this.intro.set(intro);
        this.isIdCardVerify.set(isIdCardVerify);
        this.isAlipayVerify.set(isAlipayVerify);
        this.isSesameVerify.set(isSesameVerify);
    }

    public String getNick() {
        return nick.get();
    }

    public void setNick(String nick) {
        this.nick.set(nick);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
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
        return avatar.get();
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar.set(avatar);
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getIntro() {
        return intro.get();
    }

    public void setIntro(String intro) {
        this.intro.set(intro);
    }

    public Boolean getIdCardVerify() {
        return isIdCardVerify.get();
    }

    public void setIdCardVerify(Boolean idCardVerify) {
        this.isIdCardVerify.set(idCardVerify);
    }

    public Boolean getAlipayVerify() {
        return isAlipayVerify.get();
    }

    public void setAlipayVerify(Boolean alipayVerify) {
        this.isAlipayVerify.set(alipayVerify);
    }

    public Boolean getSesameVerify() {
        return isSesameVerify.get();
    }

    public void setSesameVerify(Boolean sesameVerify) {
        this.isSesameVerify.set(sesameVerify);
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
        dest.writeByte((byte) (getIdCardVerify() ? 1 : 0));
        dest.writeByte((byte) (getAlipayVerify() ? 1 : 0));
        dest.writeByte((byte) (getSesameVerify() ? 1 : 0));
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            User user = new User();
            user.setNick(source.readString());
            user.setPhone(source.readString());
            user.setAvatar((BmobFile) source.readSerializable());
            user.setCity(source.readString());
            user.setAddress(source.readString());
            user.setIntro(source.readString());
            user.setIdCardVerify(source.readByte() != 0);
            user.setAlipayVerify(source.readByte() != 0);
            user.setSesameVerify(source.readByte() != 0);
            return user;
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
