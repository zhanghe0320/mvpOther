package com.mvp.base.db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class Product implements Parcelable {
    private int mId;//不修改
    private int mProductId;//产品ID
    private String mProductName;//产品名称  南京  中华
    private String mProductDaysum;//产品日出货量    来自平台
    private String mProductTotal;//产品总出货量     来自平台
    private String mImgUrl;//图片的地址
    private Long mCreatedTime;//时间戳 数据修改的时候插入时间轴
    private String mEquipmenthost;//唯一标识 			010100000001
    private String mEquipmentbase;//产品机器码分机号码  货架  020200000001


    private String mPrematchImgurl;//预配
    private String mPrematchProductname;//预配

    private String mProductMess;//产品介绍信息
    private String mShelfState;//产品介绍信息
    private String mLackProduct;//绑定 010100000001           根据equipmentbase删除货架
    private String mTextSpeak;//绑定 010100000001           根据equipmentbase删除货架

    public Product() {

    }

    @Override
    public String toString() {
        return "Product{" +
                "mId=" + mId +
                ", mProductId=" + mProductId +
                ", mProductName='" + mProductName + '\'' +
                ", mProductDaysum='" + mProductDaysum + '\'' +
                ", mProductTotal='" + mProductTotal + '\'' +
                ", mImgUrl='" + mImgUrl + '\'' +
                ", mCreatedTime=" + mCreatedTime +
                ", mEquipmenthost='" + mEquipmenthost + '\'' +
                ", mEquipmentbase='" + mEquipmentbase + '\'' +
                ", mPrematchImgurl='" + mPrematchImgurl + '\'' +
                ", mPrematchProductname='" + mPrematchProductname + '\'' +
                ", mProductMess='" + mProductMess + '\'' +
                ", mShelfState='" + mShelfState + '\'' +
                ", mLackProduct='" + mLackProduct + '\'' +
                ", mTextSpeak='" + mTextSpeak + '\'' +
                '}';
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmProductId() {
        return mProductId;
    }

    public void setmProductId(int mProductId) {
        this.mProductId = mProductId;
    }

    public String getmProductName() {
        return mProductName;
    }

    public void setmProductName(String mProductName) {
        this.mProductName = mProductName;
    }

    public String getmProductDaysum() {
        return mProductDaysum;
    }

    public void setmProductDaysum(String mProductDaysum) {
        this.mProductDaysum = mProductDaysum;
    }

    public String getmProductTotal() {
        return mProductTotal;
    }

    public void setmProductTotal(String mProductTotal) {
        this.mProductTotal = mProductTotal;
    }

    public String getmImgUrl() {
        return mImgUrl;
    }

    public void setmImgUrl(String mImgUrl) {
        this.mImgUrl = mImgUrl;
    }

    public Long getmCreatedTime() {
        return mCreatedTime;
    }

    public void setmCreatedTime(Long mCreatedTime) {
        this.mCreatedTime = mCreatedTime;
    }

    public String getmEquipmenthost() {
        return mEquipmenthost;
    }

    public void setmEquipmenthost(String mEquipmenthost) {
        this.mEquipmenthost = mEquipmenthost;
    }

    public String getmEquipmentbase() {
        return mEquipmentbase;
    }

    public void setmEquipmentbase(String mEquipmentbase) {
        this.mEquipmentbase = mEquipmentbase;
    }

    public String getmPrematchImgurl() {
        return mPrematchImgurl;
    }

    public void setmPrematchImgurl(String mPrematchImgurl) {
        this.mPrematchImgurl = mPrematchImgurl;
    }

    public String getmPrematchProductname() {
        return mPrematchProductname;
    }

    public void setmPrematchProductname(String mPrematchProductname) {
        this.mPrematchProductname = mPrematchProductname;
    }

    public String getmProductMess() {
        return mProductMess;
    }

    public void setmProductMess(String mProductMess) {
        this.mProductMess = mProductMess;
    }

    public String getmShelfState() {
        return mShelfState;
    }

    public void setmShelfState(String mShelfState) {
        this.mShelfState = mShelfState;
    }

    public String getmLackProduct() {
        return mLackProduct;
    }

    public void setmLackProduct(String mLackProduct) {
        this.mLackProduct = mLackProduct;
    }

    public String getmTextSpeak() {
        return mTextSpeak;
    }

    public void setmTextSpeak(String mTextSpeak) {
        this.mTextSpeak = mTextSpeak;
    }

    public static Creator<Product> getCREATOR() {
        return CREATOR;
    }

    protected Product(Parcel in) {
        mId = in.readInt();
        mProductId = in.readInt();
        mProductName = in.readString();
        mProductDaysum = in.readString();
        mProductTotal = in.readString();
        mImgUrl = in.readString();
        if (in.readByte() == 0) {
            mCreatedTime = null;
        } else {
            mCreatedTime = in.readLong();
        }
        mEquipmenthost = in.readString();
        mEquipmentbase = in.readString();
        mPrematchImgurl = in.readString();
        mPrematchProductname = in.readString();
        mProductMess = in.readString();
        mShelfState = in.readString();
        mLackProduct = in.readString();
        mTextSpeak = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mProductId);
        dest.writeString(mProductName);
        dest.writeString(mProductDaysum);
        dest.writeString(mProductTotal);
        dest.writeString(mImgUrl);
        if (mCreatedTime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mCreatedTime);
        }
        dest.writeString(mEquipmenthost);
        dest.writeString(mEquipmentbase);
        dest.writeString(mPrematchImgurl);
        dest.writeString(mPrematchProductname);
        dest.writeString(mProductMess);
        dest.writeString(mShelfState);
        dest.writeString(mLackProduct);
        dest.writeString(mTextSpeak);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
