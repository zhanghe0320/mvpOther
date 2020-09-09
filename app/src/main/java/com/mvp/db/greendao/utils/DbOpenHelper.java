package com.mvp.db.greendao.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mvp.db.greendao.DaoMaster;
import com.mvp.db.greendao.ProductMessDao;

import org.greenrobot.greendao.database.Database;


//import com.dongjiu.utils.dao.StudentDao;

public class DbOpenHelper extends DaoMaster.OpenHelper {

    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * 数据库升级
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        //操作数据库的更新 有几个表升级都可以传入到下面
//        Log.i("version", oldVersion + "---先前和更新之后的版本---" + newVersion);
//        if (oldVersion < newVersion) {
//            Log.i("version", oldVersion + "---先前和更新之后的版本---" + newVersion);
//            MigrationHelper.getInstance().migrate(db, ProductMessDao.class);
//            MigrationHelper.getInstance().migrate(db, SystemValuesDao.class);
//        //更改过的实体类(新增的不用加) 更新UserDao文件 可以添加多个 XXDao.class 文件
//        // MigrationHelper.getInstance().migrate(db, UserDao.class,XXDao.class);
//        }

        switch (oldVersion) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                MigrationHelper.getInstance().migrate(db, ProductMessDao.class);// 6版本后对Bean1Dao 数据进行升级
            case 7:
            case 8:
            case 9:
            case 10:
                MigrationHelper.getInstance().migrate(db, ProductMessDao.class);// 10版本后对BeanNewDao数据进行升级
            case 11:
                MigrationHelper.getInstance().migrate(db, ProductMessDao.class);// 11版本后对Bean3Dao数据进行升级
            case 12:
                MigrationHelper.getInstance().migrate(db, ProductMessDao.class);
            case 13:
                MigrationHelper.getInstance().migrate(db, ProductMessDao.class);
                MigrationHelper.getInstance().migrate(db, ProductMessDao.class);
            case 14:
                MigrationHelper.getInstance().migrate(db, ProductMessDao.class);
                MigrationHelper.getInstance().migrate(db, ProductMessDao.class);
            default:
                break;
        }
          //MigrationHelper.getInstance().migrate(db,StudentDao.class);
    }

}
