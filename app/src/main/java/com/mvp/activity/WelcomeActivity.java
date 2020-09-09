package com.mvp.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.de.rocket.bean.ActivityParamBean;
import com.de.rocket.bean.PermissionBean;
import com.de.rocket.bean.StatusBarBean;
import com.de.rocket.helper.ToastHelper;
import com.de.rocket.listener.PermissionListener;
import com.mvp.R;
import com.mvp.activity.wifi.WifiUtils;
import com.mvp.base.BaseActivity;
import com.mvp.base.db.ProductTable;
import com.mvp.base.mvp.BasePresenter;
import com.mvp.db.greendao.ProductMess;
import com.mvp.db.greendao.utils.DbManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;


public class WelcomeActivity extends BaseActivity/* implements OnDownloadListener */ implements PermissionListener {

    private String[] permission = {
            "android.permission.MOUNT_FORMAT_FILESYSTEMS"
    };

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    //    @BindView(R.id.start_down)
    //    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setActionBar(toolbar);
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        // LogUtils.i("初始化完成");

        ToastHelper.toastCustom("sad", 2000);
        WifiUtils mUtils = new WifiUtils(this);
        if (!mUtils.isWifiOPened()) {
            mUtils.EnableWifi();
        }
        //延时7秒后进入主页
        Observable.timer(3 * 1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    }
                });

        // PermissionHelper.getInstance().requestPermission(this);

        //自定义数据库实现
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductTable.ProductColumns.equipmentbase, "host");
        contentValues.put(ProductTable.ProductColumns.equipmenthost, "count");
        contentValues.put(ProductTable.ProductColumns.prematchimgurl, "time");
        contentValues.put(ProductTable.ProductColumns.prematchProductname, "path");
        ProductTable.insertItem(contentValues);

        //数据插入或者覆盖替换  greendao  数据库
        DbManager.getDaoSession(this).getProductMessDao().insertOrReplace(new ProductMess(1, "String mProductName",
                " String mProductDaysum", "String mProductTotal", "String mImgUrl",
                System.currentTimeMillis(), "String mEquipmenthost", " String mEquipmentbase",
                "String mPrematchImgurl", "String mPrematchProductname",
                "String mProductMess", "String mShelfState", "String mLackProduct",
                " String mTextSpeak"));

    }

    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return 0;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {

    }


    @Override
    public void setActionBar(@Nullable android.widget.Toolbar toolbar) {
        super.setActionBar(toolbar);
    }

//    @Override
//    public void setSupportActionBar(@Nullable Toolbar toolbar) {
//        super.setSupportActionBar(toolbar);
//    }


    /**
     * Activity全局配置信息
     *
     * @return ActivityParamBean
     */
    @Override
    public ActivityParamBean initProperty() {
        ActivityParamBean activityParamBean = new ActivityParamBean();
        activityParamBean.setLayoutId(R.layout.activity_welcome);//Activity布局
        // activityParamBean.setFragmentContainId(R.id.fl_fragment_contaner);//Fragment容器
        // activityParamBean.setSaveInstanceState(true);//页面重载是否要恢复之前的页面
        activityParamBean.setToastCustom(true);//用自定义的吐司风格
        // activityParamBean.setRoFragments(roFragments);//需要注册Fragment列表
        activityParamBean.setShowViewBall(false);//是否显示悬浮球
        // activityParamBean.setRecordBean(new RecordBean(true,true,true,7));
        //  activityParamBean.setEnableCrashWindow(true);//是否隐藏框架自定义的崩溃的窗口
        activityParamBean.setStatusBar(new StatusBarBean(true, Color.argb(0, 0, 0, 0)));//状态栏
        LogUtils.i("初始化完成");
        return activityParamBean;
    }

    /**
     * 页面初始化完成
     */
    @Override
    public void initView() {
        //初始化view
        // LogUtils.i("初始化完成");
    }

    /**
     * 业务逻辑初始化完成
     *
     * @param object
     */
    @Override
    public void onWork(Object object) {
        //初始化事件
        //提示框等等
        //LogUtils.i("初始化完成");


    }

    /**
     * 回调
     *
     * @param requestCode     请求码
     * @param allAccept       是否全部允许
     * @param permissionBeans 用户点击详情列表
     */
    @Override
    public void onResult(int requestCode, boolean allAccept, List<PermissionBean> permissionBeans) {

    }

    /**
     * Called when pointer capture is enabled or disabled for the current window.
     *
     * @param hasCapture True if the window has pointer capture.
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    /**
     * 开始下载
     */
//    @Override
//    public void start() {
//
//    }

    /**
     * 下载中
     *
     * @param max      总进度
     * @param progress 当前进度
     */
//    @Override
//    public void downloading(int max, int progress) {
//
//    }

    /**
     * 下载完成
     *
     * @param apk 下载好的apk
     */
//    @Override
//    public void done(File apk) {
//
//    }

    /**
     * 取消下载
     */
//    @Override
//    public void cancel() {
//
//    }

    /**
     * 下载出错
     *
     * @param e 错误信息
     */
//    @Override
//    public void error(Exception e) {
//
//    }

//    @Event(R.id.start_down)
//    private void animDownToUp(TextView view) {
//        DownloadManager manager = DownloadManager.getInstance(this);
//        manager.setApkName("appupdate.apk")
//                .setApkUrl("https://raw.githubusercontent.com/azhon/AppUpdate/master/apk/appupdate.apk")
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .download();
//    }
}
