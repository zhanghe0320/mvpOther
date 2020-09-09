package com.mvp.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.de.rocket.ue.activity.RoActivity;
import com.hjq.toast.ToastUtils;
import com.mvp.base.mvp.BaseModel;
import com.mvp.base.mvp.BasePresenter;
import com.mvp.commonutils.KeyBoardUtils;
import com.mvp.commonutils.netstatus.NetWorkMonitorManager;
import com.mvp.commonutils.netstatus.NetWorkState;
import com.mvp.oserver.NetConnectionObserver;
import com.mvp.view.LoadingDialog;
import com.mvp.view.ProgressDialog;
import com.mvp.base.mvp.BaseView;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.lang.ref.WeakReference;


import io.reactivex.disposables.CompositeDisposable;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * activity 基类
 */
public abstract class BaseActivity<P extends BasePresenter> extends RoActivity implements BaseView, CustomAdapt /*, NetConnectionObserver*/ {
    protected final String TAG = this.getClass().getSimpleName();//获取类名信息
    public Context mContext;
    protected P mPresenter;

    protected abstract P createPresenter();

    private LoadingDialog loadingDialog;
    private ProgressDialog progressDialog;
    //弱引用持有
    private WeakReference<LifecycleProvider> lifecycle;
    //管理RxJava，主要针对RxJava异步操作造成的内存泄漏
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //  setContentView(getLayoutId());
        mPresenter = createPresenter();
        //setStatusBar();

        //  this.initData();
        /**注册观察者网络状态**/
        //使用另一个方法
        //MvpApplication.getInstance().addNetObserver(this);
    }

    /**
     * 网络状态改变
     * @param type
     */
//    @Override
//    public void updateNetStatus(int type) {
//        //当监听网络状态发生变化 这里会及时的收到回馈
//        //updateNetStatu(type);
//    }
    //public abstract void updateNetStatu(int type);

    /**
     * 注入RxLifecycle生命周期
     *
     * @param lifecycle
     */
    public void injectLifecycleProvider(LifecycleProvider lifecycle) {
        this.lifecycle = new WeakReference<>(lifecycle);
    }

    public LifecycleProvider getLifecycleProvider() {
        return lifecycle.get();
    }

    /**
     * 获取布局ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 数据初始化操作
     */
    protected abstract void initData();

    /**
     * 此处设置沉浸式地方
     */
    protected void setStatusBar() {
        //StatusBarUtil.setTranslucentForDrawerLayout(this,null);
        // StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        // StatusBarUtil.setTransparentForWindow(this);

        getSupportActionBar().hide(); //隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏幕显示
    }


    @Override
    public void showError(String msg) {
        ToastUtils.show(msg);
    }

    /**
     * 返回所有状态  除去指定的值  可设置所有（根据需求）
     *
     * @param model
     */
    @Override
    public void onErrorCode(BaseModel model) {
        if (model.getError_code() == 10000000) {
            //处理些后续逻辑   如果某个页面不想实现  子类重写这个方法  将super去掉  自定义方法
//            App.put();
//            startActivity(LoginActivity.class);
        }
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dissMissDialog();
    }

    public void showLoadingDialog() {
        showLoadingDialog("加载中...");
    }

    /**
     * 加载  黑框...
     */
    public void showLoadingDialog(String msg) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.setMessage(msg);
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    /**
     * 消失  黑框...
     */
    public void dissMissDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onRestart() {//重新启动
        super.onRestart();
    }

    @Override
    protected void onPause() {//暂停
        super.onPause();
    }

    @Override
    protected void onResume() {//刷新
        super.onResume();
    }

    @Override
    protected void onStart() {//启动
        super.onStart();
        //注册网络监听
        NetWorkMonitorManager.getInstance().register(this);
    }

    @Override
    protected void onStop() {//停止
        super.onStop();
    }

    public static void actionStart(Context context, String date, String data) {
        Intent intent = new Intent(context, BaseActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onDestroy() {//销毁界面
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }

        /**
         * 销毁网络观察者
         */
        //移除网络监听
        NetWorkMonitorManager.getInstance().unregister(this);
        //  MvpApplication.getInstance().removeNetObserver(this);
    }

    //不加注解默认监听所有的状态，方法名随意，只需要参数是一个NetWorkState即可
    //@NetWorkMonitor(monitorFilter = {NetWorkState.GPRS})//只接受网络状态变为GPRS类型的消息


    @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        // progressDialog.getProgressBar().performAnimation();
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            //progressDialog.getProgressBar().releaseAnimation();
            progressDialog.dismiss();
        }
    }

    @Override
    public void onProgress(int progress) {
        if (progressDialog != null) {
            progressDialog.updateProgress(progress);
        }
    }

    /**
     * [页面跳转]
     *
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }


    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 以下是关于软键盘的处理
     */

    /**
     * 清除editText的焦点
     *
     * @param v   焦点所在View
     * @param ids 输入框
     */
    public void clearViewFocus(View v, int... ids) {
        if (null != v && null != ids && ids.length > 0) {
            for (int id : ids) {
                if (v.getId() == id) {
                    v.clearFocus();
                    break;
                }
            }
        }
    }

    /**
     * 隐藏键盘
     *
     * @param v   焦点所在View
     * @param ids 输入框
     * @return true代表焦点在edit上
     */
    public boolean isFocusEditText(View v, int... ids) {
        if (v instanceof EditText) {
            EditText et = (EditText) v;
            for (int id : ids) {
                if (et.getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    //是否触摸在指定view上面,对某个控件过滤
    public boolean isTouchView(View[] views, MotionEvent ev) {
        if (views == null || views.length == 0) {
            return false;
        }
        int[] location = new int[2];
        for (View view : views) {
            view.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            if (ev.getX() > x && ev.getX() < (x + view.getWidth())
                    && ev.getY() > y && ev.getY() < (y + view.getHeight())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isTouchView(filterViewByIds(), ev)) {
                return super.dispatchTouchEvent(ev);
            }
            if (hideSoftByEditViewIds() == null || hideSoftByEditViewIds().length == 0) {
                return super.dispatchTouchEvent(ev);
            }
            View v = getCurrentFocus();
            if (isFocusEditText(v, hideSoftByEditViewIds())) {
                KeyBoardUtils.hideInputForce(this);
                clearViewFocus(v, hideSoftByEditViewIds());
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 传入EditText的Id
     * 没有传入的EditText不做处理
     *
     * @return id 数组
     */
    public int[] hideSoftByEditViewIds() {
        return null;
    }

    /**
     * 传入要过滤的View
     * 过滤之后点击将不会有隐藏软键盘的操作
     *
     * @return id 数组
     */
    public View[] filterViewByIds() {
        return null;
    }






    /*实现案例===============================================================================================*/
    /*

    @Override
    public int[] hideSoftByEditViewIds() {
        int[] ids = {R.id.et_company_name, R.id.et_address};
        return ids;
    }

    @Override
    public View[] filterViewByIds() {
        View[] views = {mEtCompanyName, mEtAddress};
        return views;
    }

    */

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LogUtils.i("修改项目横竖屏设置ORIENTATION_LANDSCAPE");
            AutoSizeConfig.getInstance().setBaseOnWidth(true);
        } else {
            LogUtils.i("修改项目横竖屏设置");
            AutoSizeConfig.getInstance().setBaseOnWidth(false);
        }
    }

    // 这是屏幕的自适应适配信息

    /**
     * 是否按照宽度进行等比例适配 (为了保证在高宽比不同的屏幕上也能正常适配, 所以只能在宽度和高度之中选择一个作为基准进行适配)
     *
     * @return {@code true} 为按照宽度进行适配, {@code false} 为按照高度进行适配
     */
    @Override
    public boolean isBaseOnWidth() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LogUtils.i("修改项目横竖屏设置ORIENTATION_LANDSCAPE");
            return true;
        } else {
            LogUtils.i("修改项目横竖屏设置");
            AutoSizeConfig.getInstance().setBaseOnWidth(true);
            return false;
        }

    }

    /**
     * 这里使用 iPhone 的设计图, iPhone 的设计图尺寸为 750px * 1334px,
     * 高换算成 dp 为 667 (1334px / 2 = 667dp)
     * <p>
     * 返回设计图上的设计尺寸, 单位 dp
     * {@link #getSizeInDp} 须配合 {@link #isBaseOnWidth()} 使用, 规则如下:
     * 如果 {@link #isBaseOnWidth()} 返回 {@code true}, {@link #getSizeInDp} 则应该返回设计图的总宽度
     * 如果 {@link #isBaseOnWidth()} 返回 {@code false}, {@link #getSizeInDp} 则应该返回设计图的总高度
     * 如果您不需要自定义设计图上的设计尺寸, 想继续使用在 AndroidManifest 中填写的设计图尺寸, {@link #getSizeInDp} 则返回 {@code 0}
     *
     * @return 设计图上的设计尺寸, 单位 dp
     */
    @Override
    public float getSizeInDp() {
        return 560;
    }
}
