package com.pikachu.convenientdelivery.model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 83624 on 2017/7/16.
 */

public class User extends BmobUser {
    private String nick;
    private BmobFile avatar;
    private String city;
    private String address;
    private String intro;

    private Boolean isIdCardVerify = false;
    private Boolean isAliPayVerify = false;
    private Boolean isSesameVerify = false;



    public User(){}


    public User(String nick, BmobFile avatar, String city, String address, String intro, Boolean isIdCardVerify, Boolean isAliPayVerify, Boolean isSesameVerify) {
        this.nick = nick;
        this.avatar = avatar;
        this.city = city;
        this.address = address;
        this.intro = intro;
        this.isIdCardVerify = isIdCardVerify;
        this.isAliPayVerify = isAliPayVerify;
        this.isSesameVerify = isSesameVerify;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
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

    public Boolean getIdCardVerify() {
        return isIdCardVerify;
    }

    public void setIdCardVerify(Boolean idCardVerify) {
        isIdCardVerify = idCardVerify;
    }

    public Boolean getAliPayVerify() {
        return isAliPayVerify;
    }

    public void setAliPayVerify(Boolean aliPayVerify) {
        isAliPayVerify = aliPayVerify;
    }

    public Boolean getSesameVerify() {
        return isSesameVerify;
    }

    public void setSesameVerify(Boolean sesameVerify) {
        isSesameVerify = sesameVerify;
    }

    @Override
    public String toString() {
        return "User{" +
                "nick='" + nick + '\'' +
                ", avatar=" + avatar +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", intro='" + intro + '\'' +
                ", isIdCardVerify=" + isIdCardVerify +
                ", isAliPayVerify=" + isAliPayVerify +
                ", isSesameVerify=" + isSesameVerify +
                '}';
    }
}
