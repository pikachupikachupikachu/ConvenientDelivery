package com.pikachu.convenientdelivery.db;



import cn.bmob.newim.bean.BmobIMConversationType;

/**
 * Conversation
 */

public  class Conversation {

    private String tv_recent_name;
    private String tv_recent_msg;
    private String tv_recent_time;
    private String tv_recent_unread;
    private int imageId;

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getImageId() {

        return imageId;
    }

    public Conversation(String tv_recent_name, String tv_recent_msg, String tv_recent_time, String tv_recent_unread, int imageId) {
        this.tv_recent_name = tv_recent_name;
        this.tv_recent_msg = tv_recent_msg;
        this.tv_recent_time = tv_recent_time;
        this.tv_recent_unread = tv_recent_unread;
        this.imageId = imageId;

    }

    public Conversation(String tv_recent_name, String tv_recent_msg, String tv_recent_time, String tv_recent_unread) {
        this.tv_recent_name = tv_recent_name;
        this.tv_recent_msg = tv_recent_msg;
        this.tv_recent_time = tv_recent_time;
        this.tv_recent_unread = tv_recent_unread;
    }

    public String getTv_recent_name() {
        return tv_recent_name;
    }

    public String getTv_recent_msg() {
        return tv_recent_msg;
    }

    public String getTv_recent_time() {
        return tv_recent_time;
    }

    public String getTv_recent_unread() {
        return tv_recent_unread;
    }

    public void setTv_recent_name(String tv_recent_name) {
        this.tv_recent_name = tv_recent_name;
    }

    public void setTv_recent_msg(String tv_recent_msg) {
        this.tv_recent_msg = tv_recent_msg;
    }

    public void setTv_recent_time(String tv_recent_time) {
        this.tv_recent_time = tv_recent_time;
    }

    public void setTv_recent_unread(String tv_recent_unread) {
        this.tv_recent_unread = tv_recent_unread;
    }
}
