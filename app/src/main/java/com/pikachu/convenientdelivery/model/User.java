package com.pikachu.convenientdelivery.model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 83624 on 2017/7/16.
 */

public class User extends BmobUser {
    private String nick;
    private BmobFile avatar;


    public User(){}
    public User(String nick, BmobFile avatar) {
        this.nick = nick;
        this.avatar = avatar;
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

    @Override
    public String toString() {
        return "User{" +
                "nick='" + nick + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
