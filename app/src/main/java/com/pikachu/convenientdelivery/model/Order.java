package com.pikachu.convenientdelivery.model;

import android.graphics.Bitmap;
import android.media.Image;

import cn.bmob.v3.BmobObject;

/**
 * Created by JinBo on 2017/7/20.
 */

public class Order extends BmobObject {
    private String requirement;
    private String message;
    private String goods;
    private String locale;
    private String address;
    private String pay;
    private String ImagePath;

    //true 代表具体金额 false代表按商品支付
    private boolean priceway;

    public String getRequirement() {
        return requirement;
    }
    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }
    public  String getMessage(){
        return  message;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public  String getGoods(){
        return  goods;
    }
    public void setGoods(String goods){
        this.goods = goods;
    }
    public  String getLocale(){
        return  locale;
    }
    public void setLocale(String locale){
        this.locale = locale;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPay(){return  pay;}
    public  void  setPay(String pay){
        this.pay = pay;
    }
    public String getImagePath(){return ImagePath;}
    public void setImagePath(String  ImagePath){ this.ImagePath = ImagePath;}
    public boolean getPriceway(){return priceway;}
    public void setPriceway(boolean priceway){this.priceway = priceway;}


}
