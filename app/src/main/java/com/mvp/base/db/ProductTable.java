package com.mvp.base.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * 数据库处理
 * 对各个数据库的处理
 */
public class ProductTable {
    public static final int MAX_SIZE_PREFETCH_LIST = 10;

    private static final String TABLE_NAME = "product";

    public static class ProductColumns implements BaseColumns {
        /**
         * sort default
         */
        public static final String DEFAULT_SORT_ORDER = "_id DESC";
        public static final String product_name = "productname";
        public static final String product_daysum = "productdaysum";
        public static final String product_total = "producttotal";
        public static final String product_id = "productid";
        public static final String product_createdTime = "productcreatedtime";
        public static final String product_imgUrl = "productimgurl";
        public static final String equipmentbase = "equipmentbase";
        public static final String equipmenthost = "equipmenthost";
        public static final String prematchimgurl = "prematchImgurl";
        public static final String prematchProductname = "prematchproductname";
        public static final String productMess = "productmess";
        public static final String lackProduct = "lackProduct";
        public static final String shelfState = "shelfState";
    }

    public static void cursoreate(SQLiteDatabase sqlitedatabase) {
        if (sqlitedatabase == null) {
            return;
        }
        sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + ProductColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ProductColumns.product_name + " TEXT,"
                + ProductColumns.product_daysum + " TEXT,"
                + ProductColumns.product_total + " TEXT,"
                + ProductColumns.product_id + " INTEGER ,"
                + ProductColumns.product_createdTime + " Long ,"
                + ProductColumns.product_imgUrl + " TEXT,"
                + ProductColumns.equipmentbase + " TEXT,"
                + ProductColumns.equipmenthost + " TEXT,"
                + ProductColumns.prematchimgurl + " TEXT,"
                + ProductColumns.prematchProductname + " TEXT,"
                + ProductColumns.productMess + " TEXT,"
                + ProductColumns.lackProduct + " TEXT,"
                + ProductColumns.shelfState + " TEXT"
                + ");");
    }


    public static void insertItem(ContentValues values) {

        ProductDBHelper.getInstance().insert(TABLE_NAME, values);
    }

    public static String queryBestReplaceID(int path) {

        Cursor cursor = null;
        try {
            cursor = ProductDBHelper.getInstance().query(TABLE_NAME, new String[]{ProductColumns._ID},
                    ProductColumns.shelfState + " = ?", new String[]{Integer.toString(path)},
                    ProductColumns.shelfState + " ASC, " + ProductColumns.shelfState + " ASC");
            if (cursor != null && cursor.getCount() < 10) {
                return null;
            } else if (cursor != null && cursor.moveToFirst() && cursor.getCount() >= 20) {
                return Long.toString(cursor.getLong(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return null;
    }

    public static void updateItem(ContentValues values, String selection,
                                  String[] selectionArgs) {

        ProductDBHelper.getInstance().update(TABLE_NAME, values, selection, selectionArgs);
    }

    public static void delete(String select, String[] arg) {

        ProductDBHelper.getInstance().delete(TABLE_NAME, select, arg);
    }

    public static String[] queryGetSameExists(String host, int path) {

        Cursor cursor = null;
        try {
            cursor = ProductDBHelper.getInstance().query(TABLE_NAME, new String[]{
                    ProductColumns._ID,
                    ProductColumns.shelfState}, ProductColumns.shelfState + " = ? AND "
                    + ProductColumns.shelfState + " = ? ", new String[]{host, Integer.toString(path)}, null);
            if (cursor != null && cursor.moveToFirst()) {
                return new String[]{Long.toString(cursor.getLong(0)), Integer.toString(cursor.getInt(1))};
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return null;
    }

    public static boolean queryIsSameExists(String host, int path) {

        Cursor cursor = null;
        try {
            cursor = ProductDBHelper.getInstance().query(TABLE_NAME, null, ProductColumns.shelfState + " = ? AND "
                    + ProductColumns.shelfState + " = ? ", new String[]{host, Integer.toString(path)}, null);
            if (cursor != null && cursor.moveToFirst()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return false;
    }

    public static ArrayList<String> queryList(int path) {

        Cursor cursor = null;
        ArrayList<String> prefetchList = new ArrayList<String>();
        try {
            cursor = ProductDBHelper.getInstance().query(TABLE_NAME,
                    new String[]{ProductColumns.shelfState}, ProductColumns.shelfState + " = ?",
                    new String[]{Integer.toString(path)}, ProductColumns.shelfState + " DESC, "
                            + ProductColumns.shelfState + " DESC");
            if (cursor != null && cursor.moveToFirst()) {
                int num = 0;
                do {
                    String hostname = cursor.getString(0);
                    if (!TextUtils.isEmpty(hostname)) {
                        prefetchList.add(hostname);
                    }
                    num++;
                } while (cursor != null && cursor.moveToNext() && num < MAX_SIZE_PREFETCH_LIST);
            }
        } catch (Throwable e) {
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return prefetchList;
    }

}
