package com.pikachu.convenientdelivery.model;

import android.databinding.ObservableField;
import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Order
 */

public class Order extends BmobObject implements Parcelable {

    private final ObservableField<String> goodsName = new ObservableField<>();
    private final ObservableField<String> goodsDetail = new ObservableField<>();
    private final ObservableField<BmobFile> goodsImage = new ObservableField<>();
    private final ObservableField<Boolean> isRewardDefault = new ObservableField<>(true);
    private final ObservableField<Double> reward = new ObservableField<>();
    private final ObservableField<User> shipper = new ObservableField<>();
    private final ObservableField<User> recipient = new ObservableField<>();
    private final ObservableField<User> courier = new ObservableField<>();
    private final ObservableField<Boolean> isGoodsSpecific = new ObservableField<>();
    private final ObservableField<Boolean> isAddressSpecific = new ObservableField<>();
    private final ObservableField<String> purchasingAddress = new ObservableField<>();
    private final ObservableField<Double> goodsPrice = new ObservableField<>();
    private final ObservableField<Double> deliveryFee = new ObservableField<>();
    private final ObservableField<Integer> status = new ObservableField<>();

    public static final int WAITING = 0;        // 待接单
    public static final int PURCHASING = 1;     // 购买中
    public static final int NONDELIVERY = 2;    // 购买完成，未交给快递员
    public static final int SHIPPING = 3;       // 快递中
    public static final int UNCONFIRMED = 4;    // 待收货人确认
    public static final int UNCOMMENT = 5;      // 未评价
    public static final int FINISHED = 6;       // 完成

    public Order() {
        isRewardDefault.set(true);
        isGoodsSpecific.set(true);
        isAddressSpecific.set(true);
        status.set(WAITING);
    }

    public String getGoodsName() {
        return goodsName.get();
    }

    public void setGoodsName(String goodsName) {
        this.goodsName.set(goodsName);
    }

    public String getGoodsDetail() {
        return goodsDetail.get();
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail.set(goodsDetail);
    }

    public BmobFile getGoodsImage() {
        return goodsImage.get();
    }

    public void setGoodsImage(BmobFile goodsImage) {
        this.goodsImage.set(goodsImage);
    }

    public boolean isRewardDefault() {
        return isRewardDefault.get();
    }

    public void setRewardDefault(boolean rewardDefault) {
        isRewardDefault.set(rewardDefault);
    }

    public Double getReward() {
        return reward.get();
    }

    public void setReward(Double reward) {
        this.reward.set(reward);
    }

    public User getShipper() {
        return shipper.get();
    }

    public void setShipper(User shipper) {
        this.shipper.set(shipper);
    }

    public User getRecipient() {
        return recipient.get();
    }

    public void setRecipient(User recipient) {
        this.recipient.set(recipient);
    }

    public User getCourier() {
        return courier.get();
    }

    public void setCourier(User courier) {
        this.courier.set(courier);
    }

    public boolean isGoodsSpecific() {
        return isGoodsSpecific.get();
    }

    public void setGoodsSpecific(boolean goodsSpecific) {
        isGoodsSpecific.set(goodsSpecific);
    }

    public boolean isAddressSpecific() {
        return isAddressSpecific.get();
    }

    public void setAddressSpecific(boolean addressSpecific) {
        isAddressSpecific.set(addressSpecific);
    }

    public String getPurchasingAddress() {
        return purchasingAddress.get();
    }

    public void setPurchasingAddress(String purchasingAddress) {
        this.purchasingAddress.set(purchasingAddress);
    }

    public Double getGoodsPrice() {
        return goodsPrice.get();
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice.set(goodsPrice);
    }

    public Double getDeliveryFee() {
        return deliveryFee.get();
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee.set(deliveryFee);
    }

    public int getStatus() {
        return status.get();
    }

    public void setStatus(int status) {
        this.status.set(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getObjectId());
        dest.writeString(getGoodsName());
        dest.writeString(getGoodsDetail());
        dest.writeSerializable(getGoodsImage());
        dest.writeByte((byte) (isRewardDefault() ? 1 : 0));
        dest.writeDouble(getReward());
        dest.writeParcelable(getShipper(), flags);
        dest.writeParcelable(getRecipient(), flags);
        dest.writeParcelable(getCourier(), flags);
        dest.writeByte((byte) (isGoodsSpecific() ? 1 : 0));
        dest.writeByte((byte) (isAddressSpecific() ? 1 : 0));
        dest.writeString(getPurchasingAddress());
        dest.writeDouble(getGoodsPrice());
        dest.writeDouble(getDeliveryFee());
        dest.writeInt(getStatus());
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {

        @Override
        public Order createFromParcel(Parcel source) {
            Order order = new Order();
            order.setObjectId(source.readString());
            order.setGoodsName(source.readString());
            order.setGoodsDetail(source.readString());
            order.setGoodsImage((BmobFile) source.readSerializable());
            order.setRewardDefault(source.readByte() != 0);
            order.setReward(source.readDouble());
            order.setShipper((User) source.readParcelable(User.class.getClassLoader()));
            order.setRecipient((User) source.readParcelable(User.class.getClassLoader()));
            order.setCourier((User) source.readParcelable(User.class.getClassLoader()));
            order.setGoodsSpecific(source.readByte() != 0);
            order.setAddressSpecific(source.readByte() != 0);
            order.setPurchasingAddress(source.readString());
            order.setGoodsPrice(source.readDouble());
            order.setDeliveryFee(source.readDouble());
            order.setStatus(source.readInt());
            return order;
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
