package com.pikachu.convenientdelivery.model;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by 83624 on 2017/7/20.
 */

public class Dynamic extends BmobObject {
    private User user;
    private String content;
    private ArrayList<String> likeId;
    private List<String> imageUrls;

    public Dynamic() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getLikeId() {
        return likeId;
    }

    public void setLikeId(ArrayList<String> likeId) {
        this.likeId = likeId;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public String toString() {
        return "Dynamic{" +
                "user=" + user +
                ", content='" + content + '\'' +
                ", likeId=" + likeId +
                ", imageUrls=" + imageUrls +
                '}';
    }
}
