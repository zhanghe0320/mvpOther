package com.mvp.db.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

/**
 */
@Entity
public class SystemValues {

    @Index(unique = true)
    @NotNull  //唯一
    private String mValueNeme ;

    @NotNull
    private String mValueMess ;


    private Long mTimeCreate;

    @Id // 自增长(autoincrement = true)   需要自增长的时候
    private Long id ;

    @Generated(hash = 407258205)
    public SystemValues(@NotNull String mValueNeme, @NotNull String mValueMess,
            Long mTimeCreate, Long id) {
        this.mValueNeme = mValueNeme;
        this.mValueMess = mValueMess;
        this.mTimeCreate = mTimeCreate;
        this.id = id;
    }

    @Generated(hash = 2047301065)
    public SystemValues() {
    }

    public String getMValueNeme() {
        return this.mValueNeme;
    }

    public void setMValueNeme(String mValueNeme) {
        this.mValueNeme = mValueNeme;
    }

    public String getMValueMess() {
        return this.mValueMess;
    }

    public void setMValueMess(String mValueMess) {
        this.mValueMess = mValueMess;
    }

    public Long getMTimeCreate() {
        return this.mTimeCreate;
    }

    public void setMTimeCreate(Long mTimeCreate) {
        this.mTimeCreate = mTimeCreate;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
