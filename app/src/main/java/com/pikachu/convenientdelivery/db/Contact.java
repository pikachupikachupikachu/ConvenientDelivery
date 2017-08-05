package com.pikachu.convenientdelivery.db;

/**
 * Created by JinBo on 2017/7/31.
 */

public class Contact {
    private String tv_recent_name;
    private int imageId;

    public Contact(String tv_recent_name, int imageId) {
        this.tv_recent_name = tv_recent_name;
        this.imageId = imageId;
    }

    public String getTv_recent_name() {
        return tv_recent_name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setTv_recent_name(String tv_recent_name) {
        this.tv_recent_name = tv_recent_name;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
