package com.mvp.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.de.rocket.bean.ActivityParamBean;
import com.de.rocket.bean.StatusBarBean;
import com.mvp.R;
import com.mvp.activity.main.DialogActivity;
import com.mvp.activity.main.RxActivity;
import com.mvp.base.BaseActivity;
import com.mvp.base.db.ProductTable;
import com.mvp.base.mvp.BasePresenter;
import com.mvp.commonutils.SystemSharePreferenceMgr;
import com.mvp.db.greendao.ProductMess;
import com.mvp.db.greendao.ProductMessDao;
import com.mvp.db.greendao.utils.DbManager;
import com.mvp.util.JsBaseInterface;
import com.mvp.view.dialog.CommonDialogFragment;
import com.mvp.view.dialog.DialogFragmentHelper;
import com.mvp.view.dialog.IDialogResultListener;

import java.util.List;

public class MainActivity extends BaseActivity {


    TextView main_rx;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemSharePreferenceMgr.getInstance().putString(SystemSharePreferenceMgr.system_password, "密码存储需要加密");
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductTable.ProductColumns.equipmentbase, "host");
        contentValues.put(ProductTable.ProductColumns.equipmenthost, "count");
        contentValues.put(ProductTable.ProductColumns.prematchimgurl, "time");
        contentValues.put(ProductTable.ProductColumns.prematchProductname, "path");
        ProductTable.insertItem(contentValues);

        ProductTable.delete(ProductTable.ProductColumns._ID + " > ? ", new String[]{"5"});

        // DbManager.getDaoSession(this).getProductMessDao().queryRaw()
        List<ProductMess> productMessList = DbManager.getDaoSession(this).getProductMessDao().queryBuilder().where(ProductMessDao.Properties.MProductId.eq(1)).build().list();
        //List<User> list = userDao.queryBuilder().where(UserDao.Properties.Id.between(2, 13)).limit(5).build().list();

        for (ProductMess productMess : productMessList) {

            LogUtils.i(productMess.toString());
        }

        startActivity(new Intent(MainActivity.this, RxActivity.class));
//        DbManager.getDaoSession(this).getProductMessDao().queryBuilder().where(
//
//        )
        //List<JBXX> jbxxes = jbxxDao.queryBuilder().where
        // (jbxxDao.queryBuilder().and(JBXXDao.Properties.统一编号.like(map.get(data.getCity()) + "%"),
        // jbxxDao.queryBuilder().or(JBXXDao.Properties.统一编号.like("%" + keywords + "%"),JBXXDao.Properties.位置.like("%" + keywords + "%"),
        //   JBXXDao.Properties.名称.like("%" + keywords + "%")))).list();
        //删除数据
        DbManager.getDaoSession(this).getProductMessDao().queryBuilder().where(ProductMessDao.Properties.MProductId.eq("1")).buildDelete().executeDeleteWithoutDetachingEntities();

        main_rx = findViewById(R.id.main_rx);
        main_rx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取对话框需要挪到view 当中处理
                DialogFragmentHelper.showConfirmDialog(getSupportFragmentManager(), "是否选择 Android？", new IDialogResultListener<Integer>() {
                    @Override
                    public void onDataResult(Integer result) {
                        ToastUtils.showShort(result);
                        switch (result) {
                            case DialogInterface.BUTTON_POSITIVE://DIALOG_POSITIVE
                                //确认
                                startActivity(new Intent(MainActivity.this, RxActivity.class));

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //取消
                                startActivity(new Intent(MainActivity.this, DialogActivity.class));
                                break;
                            case DialogInterface.BUTTON_NEUTRAL:
                                //中立
                                break;
                        }

                    }
                }, true, new CommonDialogFragment.OnDialogCancelListener() {
                    @Override
                    public void onCancel() {
                        ToastUtils.showShort("You Click Cancel");
                    }
                });
                //RxActivity.actionStart(MainActivity.this,"","");

            }
        });

        WebView webView = new WebView(this);
        JsBaseInterface jsBaseInterface = new JsBaseInterface();
        //添加JS 支持
        webView.addJavascriptInterface(jsBaseInterface, JsBaseInterface.BASE_JS_NAME);
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

    /**
     * Activity全局配置信息
     *
     * @return ActivityParamBean
     */
    @Override
    public ActivityParamBean initProperty() {
        ActivityParamBean activityParamBean = new ActivityParamBean();
        activityParamBean.setLayoutId(R.layout.activity_main);//Activity布局
        // activityParamBean.setFragmentContainId(R.id.fl_fragment_contaner);//Fragment容器
        // activityParamBean.setSaveInstanceState(true);//页面重载是否要恢复之前的页面
        activityParamBean.setToastCustom(true);//用自定义的吐司风格
        // activityParamBean.setRoFragments(roFragments);//需要注册Fragment列表
        activityParamBean.setShowViewBall(false);//是否显示悬浮球
        // activityParamBean.setRecordBean(new RecordBean(true,true,true,7));
        //  activityParamBean.setEnableCrashWindow(true);//是否隐藏框架自定义的崩溃的窗口
        activityParamBean.setStatusBar(new StatusBarBean(true, Color.argb(0, 0, 0, 0)));//状态栏

        return activityParamBean;
    }

    /**
     * 页面初始化完成
     */
    @Override
    public void initView() {

    }

    /**
     * 业务逻辑初始化完成
     *
     * @param object
     */
    @Override
    public void onWork(Object object) {

    }
}
