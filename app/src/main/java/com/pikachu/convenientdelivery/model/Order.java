package com.pikachu.convenientdelivery.model;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Order
 */

public class Order extends BmobObject implements Parcelable {

    private String createTime;
    private String finishedTime;
    private String deadline;
    private String goodsName;
    private String goodsDetail;
    private BmobFile goodsImage;
    private Boolean isRewardDefault = true;
    private Double reward;
    private User shipper;
    private User recipient;
    private RecipientInfo recipientInfo;
    private User courier;
    private Boolean isGoodsSpecific;
    private Boolean isAddressSpecific;
    private String purchasingAddress;
    private Double goodsPrice;
    private Double deliveryFee;
    private Integer status;

    public static  int WAITING = 0;        // 待接单
    public static  int PURCHASING = 1;     // 购买中
    public static  int NONDELIVERY = 2;    // 购买完成，未交给快递员
    public static  int SHIPPING = 3;       // 快递中
    public static  int UNCONFIRMED = 4;    // 待收货人确认
    public static  int UNCOMMENT = 5;      // 未评价
    public static  int FINISHED = 6;       // 完成
    public static  int ERROR = 7;          // 订单异常

    public Order() {
        createTime = getCreatedAt();
        recipient = BmobUser.getCurrentUser(User.class);
        isRewardDefault = true;
        isGoodsSpecific = true;
        isAddressSpecific = true;
        goodsPrice = 0.0;
        deliveryFee = 0.0;
        status = WAITING;
    }

    public Order(Parcel in) {
        createTime = in.readString();
        finishedTime = in.readString();
        deadline = in.readString();
        recipient = BmobUser.getCurrentUser(User.class);
        isRewardDefault = true;
        isGoodsSpecific = true;
        isAddressSpecific = true;
        goodsPrice = 0.0;
        deliveryFee = 0.0;
        status = WAITING;
        setObjectId(in.readString());
        setGoodsName(in.readString());
        setGoodsDetail(in.readString());
        setGoodsImage((BmobFile) in.readSerializable());
        setRewardDefault(in.readByte() != 0);
        setReward(in.readDouble());
        setShipper((User) in.readParcelable(User.class.getClassLoader()));
        setRecipient((User) in.readParcelable(User.class.getClassLoader()));
        setRecipientInfo((RecipientInfo) in.readParcelable(RecipientInfo.class.getClassLoader()));
        setCourier((User) in.readParcelable(User.class.getClassLoader()));
        setGoodsSpecific(in.readByte() != 0);
        setAddressSpecific(in.readByte() != 0);
        setPurchasingAddress(in.readString());
        setGoodsPrice(in.readDouble());
        setDeliveryFee(in.readDouble());
        setStatus(in.readInt());
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

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

    public BmobFile getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(BmobFile goodsImage) {
        this.goodsImage = goodsImage;
    }

    public Boolean getRewardDefault() {
        return isRewardDefault;
    }

    public void setRewardDefault(Boolean rewardDefault) {
        isRewardDefault = rewardDefault;
    }

    public Double getReward() {
        return reward;
    }

    public void setReward(Double reward) {
        this.reward = reward;
    }

    public User getShipper() {
        return shipper;
    }

    public void setShipper(User shipper) {
        this.shipper = shipper;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public RecipientInfo getRecipientInfo() {
        return recipientInfo;
    }

    public void setRecipientInfo(RecipientInfo recipientInfo) {
        this.recipientInfo = recipientInfo;
    }

    public User getCourier() {
        return courier;
    }

    public void setCourier(User courier) {
        this.courier = courier;
    }

    public Boolean getGoodsSpecific() {
        return isGoodsSpecific;
    }

    public void setGoodsSpecific(Boolean goodsSpecific) {
        isGoodsSpecific = goodsSpecific;
    }

    public Boolean getAddressSpecific() {
        return isAddressSpecific;
    }

    public void setAddressSpecific(Boolean addressSpecific) {
        isAddressSpecific = addressSpecific;
    }

    public String getPurchasingAddress() {
        return purchasingAddress;
    }

    public void setPurchasingAddress(String purchasingAddress) {
        this.purchasingAddress = purchasingAddress;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getCreateTime());
        dest.writeString(getFinishedTime());
        dest.writeString(getDeadline());
        dest.writeString(getObjectId());
        dest.writeString(getGoodsName());
        dest.writeString(getGoodsDetail());
        dest.writeSerializable(getGoodsImage());
        dest.writeByte((byte) (isRewardDefault ? 1 : 0));
        dest.writeDouble(getReward());
        dest.writeParcelable(getShipper(), flags);
        dest.writeParcelable(getRecipient(), flags);
        dest.writeParcelable(getRecipientInfo(), flags);
        dest.writeParcelable(getCourier(), flags);
        dest.writeByte((byte) (isGoodsSpecific ? 1 : 0));
        dest.writeByte((byte) (isAddressSpecific ? 1 : 0));
        dest.writeString(getPurchasingAddress());
        dest.writeDouble(getGoodsPrice());
        dest.writeDouble(getDeliveryFee());
        dest.writeInt(getStatus());
    }

    public static  Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {

        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
