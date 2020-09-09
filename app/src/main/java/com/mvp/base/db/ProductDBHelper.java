package com.mvp.base.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mvp.base.MvpApplication;

/**
 * 数据库处理
 * 对各个数据库的处理
 */
public class ProductDBHelper extends BaseDBHelper {
    private static final int DATABASE_VERSION = 14;
    private static final String DATABASE_NAME = "product.db";


    private static Singleton<ProductDBHelper> sInstance = new Singleton<ProductDBHelper>() {
        @Override
        protected ProductDBHelper newInstance() {
            return new ProductDBHelper(MvpApplication.getmContext(), DATABASE_NAME, DATABASE_VERSION);
        }
    };

    ProductDBHelper(Context context, String databaseName, int version) {
        super(context, databaseName, version);
    }

    /**
     * 获取单列
     * @return
     */
    public static ProductDBHelper getInstance() {
        return sInstance.getInstance();
    }

    /**
     * 释放资源
     */
    public static void releaseInstance() {
         sInstance.releaseInstance();
    }

    /**
     * 创建数据库
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        ProductTable.cursoreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 数据库版本更新，不用加break
        switch (oldVersion) {
            case 1:
                upgradeFrom1To2(db);
            case 2:
                upgradeFrom1To2(db);
            case 3:
                upgradeFrom1To2(db);
        }
    }

    /**
     * from 5.1 to 5.2, add News Topic
     */
    private void upgradeFrom1To2(SQLiteDatabase db) {

    }


}
