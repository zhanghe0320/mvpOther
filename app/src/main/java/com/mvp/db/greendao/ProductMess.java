package com.mvp.db.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 */
@Entity
public class ProductMess {

    @Id // 自增长(autoincrement = true)   需要自增长的时候
    private Long id ;

    private int mProductId;//产品ID
    private String mProductName;//产品名称  南京  中华
    private String mProductDaysum;//产品日出货量    来自平台
    private String mProductTotal;//产品总出货量     来自平台
    private String mImgUrl;//图片的地址
    //private Long mTimeCreated;//时间戳 数据修改的时候插入时间轴
    private String mEquipmenthost;//唯一标识 			010100000001
    private String mEquipmentbase;//产品机器码分机号码  货架  020200000001


    private String mPrematchImgurl;//预配
    private String mPrematchProductname;//预配

    private String mProductMess;//产品介绍信息
    private String mShelfState;//产品介绍信息
    private String mLackProduct;//绑定 010100000001           根据equipmentbase删除货架
    private String mTextSpeak;//绑定 010100000001           根据equipmentbase删除货架

    @Generated(hash = 212782357)
    public ProductMess(Long id, int mProductId, String mProductName, String mProductDaysum,
            String mProductTotal, String mImgUrl, String mEquipmenthost,
            String mEquipmentbase, String mPrematchImgurl, String mPrematchProductname,
            String mProductMess, String mShelfState, String mLackProduct, String mTextSpeak) {
        this.id = id;
        this.mProductId = mProductId;
        this.mProductName = mProductName;
        this.mProductDaysum = mProductDaysum;
        this.mProductTotal = mProductTotal;
        this.mImgUrl = mImgUrl;
        this.mEquipmenthost = mEquipmenthost;
        this.mEquipmentbase = mEquipmentbase;
        this.mPrematchImgurl = mPrematchImgurl;
        this.mPrematchProductname = mPrematchProductname;
        this.mProductMess = mProductMess;
        this.mShelfState = mShelfState;
        this.mLackProduct = mLackProduct;
        this.mTextSpeak = mTextSpeak;
    }
    @Generated(hash = 1869063519)
    public ProductMess() {
    }


    public ProductMess(int mProductId, String mProductName,
                       String mProductDaysum, String mProductTotal, String mImgUrl,
                       Long mTimeCreated, String mEquipmenthost, String mEquipmentbase,
                       String mPrematchImgurl, String mPrematchProductname,
                       String mProductMess, String mShelfState, String mLackProduct,
                       String mTextSpeak) {
        this.mProductId = mProductId;
        this.mProductName = mProductName;
        this.mProductDaysum = mProductDaysum;
        this.mProductTotal = mProductTotal;
        this.mImgUrl = mImgUrl;
        //this.mTimeCreated = mTimeCreated;
        this.mEquipmenthost = mEquipmenthost;
        this.mEquipmentbase = mEquipmentbase;
        this.mPrematchImgurl = mPrematchImgurl;
        this.mPrematchProductname = mPrematchProductname;
        this.mProductMess = mProductMess;
        this.mShelfState = mShelfState;
        this.mLackProduct = mLackProduct;
        this.mTextSpeak = mTextSpeak;
    }



    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getMProductId() {
        return this.mProductId;
    }
    public void setMProductId(int mProductId) {
        this.mProductId = mProductId;
    }
    public String getMProductName() {
        return this.mProductName;
    }
    public void setMProductName(String mProductName) {
        this.mProductName = mProductName;
    }
    public String getMProductDaysum() {
        return this.mProductDaysum;
    }
    public void setMProductDaysum(String mProductDaysum) {
        this.mProductDaysum = mProductDaysum;
    }
    public String getMProductTotal() {
        return this.mProductTotal;
    }
    public void setMProductTotal(String mProductTotal) {
        this.mProductTotal = mProductTotal;
    }
    public String getMImgUrl() {
        return this.mImgUrl;
    }
    public void setMImgUrl(String mImgUrl) {
        this.mImgUrl = mImgUrl;
    }
    public String getMEquipmenthost() {
        return this.mEquipmenthost;
    }
    public void setMEquipmenthost(String mEquipmenthost) {
        this.mEquipmenthost = mEquipmenthost;
    }
    public String getMEquipmentbase() {
        return this.mEquipmentbase;
    }
    public void setMEquipmentbase(String mEquipmentbase) {
        this.mEquipmentbase = mEquipmentbase;
    }
    public String getMPrematchImgurl() {
        return this.mPrematchImgurl;
    }
    public void setMPrematchImgurl(String mPrematchImgurl) {
        this.mPrematchImgurl = mPrematchImgurl;
    }
    public String getMPrematchProductname() {
        return this.mPrematchProductname;
    }
    public void setMPrematchProductname(String mPrematchProductname) {
        this.mPrematchProductname = mPrematchProductname;
    }
    public String getMProductMess() {
        return this.mProductMess;
    }
    public void setMProductMess(String mProductMess) {
        this.mProductMess = mProductMess;
    }
    public String getMShelfState() {
        return this.mShelfState;
    }
    public void setMShelfState(String mShelfState) {
        this.mShelfState = mShelfState;
    }
    public String getMLackProduct() {
        return this.mLackProduct;
    }
    public void setMLackProduct(String mLackProduct) {
        this.mLackProduct = mLackProduct;
    }
    public String getMTextSpeak() {
        return this.mTextSpeak;
    }
    public void setMTextSpeak(String mTextSpeak) {
        this.mTextSpeak = mTextSpeak;
    }


    @Override
    public String toString() {
        return "ProductMess{" +
                "id=" + id +
                ", mProductId=" + mProductId +
                ", mProductName='" + mProductName + '\'' +
                ", mProductDaysum='" + mProductDaysum + '\'' +
                ", mProductTotal='" + mProductTotal + '\'' +
                ", mImgUrl='" + mImgUrl + '\'' +
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
}
