package com.pikachu.convenientdelivery.model;

import cn.bmob.v3.BmobObject;

/**
 * Order
 */

public class Order extends BmobObject {

    private String goodsName;
    private String goodsDetail;
    private String goodsImagePath;
    private boolean isRewardDefault = true;
    private Double reward;
    private String shipperName;
    private String shipperPhone;
    private String recipientName;
    private String recipientPhone;
    private boolean isGoodsSpecific;
    private boolean isAddressSpecific;
    private String purchasingAddress;
    private String shippingAddress;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public String getGoodsImagePath() {
        return goodsImagePath;
    }

    public void setGoodsImagePath(String goodsImagePath) {
        this.goodsImagePath = goodsImagePath;
    }

    public boolean isRewardDefault() {
        return isRewardDefault;
    }

    public void setRewardDefault(boolean rewardDefault) {
        isRewardDefault = rewardDefault;
    }

    public Double getReward() {
        return reward;
    }

    public void setReward(Double reward) {
        this.reward = reward;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getShipperPhone() {
        return shipperPhone;
    }

    public void setShipperPhone(String shipperPhone) {
        this.shipperPhone = shipperPhone;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public boolean isGoodsSpecific() {
        return isGoodsSpecific;
    }

    public void setGoodsSpecific(boolean goodsSpecific) {
        isGoodsSpecific = goodsSpecific;
    }

    public boolean isAddressSpecific() {
        return isAddressSpecific;
    }

    public void setAddressSpecific(boolean addressSpecific) {
        isAddressSpecific = addressSpecific;
    }

    public String getPurchasingAddress() {
        return purchasingAddress;
    }

    public void setPurchasingAddress(String purchasingAddress) {
        this.purchasingAddress = purchasingAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
