package com.mvp.base.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 管理工具
 * 封装增删改查的处理
 */
public abstract class BaseDBHelper extends SQLiteOpenHelper {


    BaseDBHelper(Context context, String databaseName, int version) {
        super(context, databaseName, null, version);
    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

       // DatabasesUtils.deleteAllTables(db);
        onCreate(db);
    }


    /**
     * 删除数据
     * @param tableName
     * @param selection
     * @param selectionArgs
     * @return
     */
    public synchronized int delete(String tableName, String selection, String[] selectionArgs) {

        if (null == tableName) {
            throw new IllegalArgumentException("Unrecognized tableName:" + tableName);
        }
        return getWritableDatabase().delete(tableName, selection, selectionArgs);
    }

    /**
     * 插入数据
     * @param tableName
     * @param values
     * @return
     */
    public synchronized long insert(String tableName, ContentValues values) {

        if ((null == values) || (0 == values.size())) {
            return -1;
        }
        if (null == tableName) {
            throw new IllegalArgumentException("Unrecognized tableName:" + tableName);
        }

        SQLiteDatabase db = getWritableDatabase();
        long row = db.insert(tableName, null, values);
        if (row > 0) {
            return row;
        }
        return -1;
    }

    /**
     * 修改数据
     * @param tableName
     * @param values
     * @return
     */
    public synchronized long replace(String tableName, ContentValues values) {

        if ((null == values) || (0 == values.size())) {
            return -1;
        }
        if (null == tableName) {
            throw new IllegalArgumentException("Unrecognized tableName:" + tableName);
        }

        SQLiteDatabase db = getWritableDatabase();
        long row = db.replace(tableName, null, values);
        if (row > 0) {
            return row;
        }
        return -1;
    }

    /**
     * 查询数据
     * @param tableName
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    public synchronized Cursor query(String tableName, String[] projection, String selection, String[] selectionArgs,
                                     String sortOrder) {
        if (null == tableName) {
            throw new IllegalArgumentException("Unrecognized tableName:" + tableName);
        }
        return getWritableDatabase().query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public synchronized Cursor query(String tableName, String[] projection, String selection, String[] selectionArgs,
                                     String groupBy, String having, String orderBy, String limit) {

        if (null == tableName) {
            throw new IllegalArgumentException("Unrecognized tableName:" + tableName);
        }
        return getWritableDatabase().query(tableName, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * 更新数据
     * @param tableName
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    public synchronized int update(String tableName, ContentValues values, String selection, String[] selectionArgs) {

        if (null == tableName) {
            throw new IllegalArgumentException("Unrecognized tableName:" + tableName);
        }
        return getWritableDatabase().update(tableName, values, selection, selectionArgs);
    }

    /**
     *
     * @param sql
     * @param selectionArgs
     * @return
     */
    public synchronized Cursor rawQuery(String sql, String[] selectionArgs) {
        return getWritableDatabase().rawQuery(sql, selectionArgs);
    }

    /**
     * 执行sql
     * @param sql
     */
    public synchronized void execSQL(String sql) {
        getWritableDatabase().execSQL(sql);
    }

}
