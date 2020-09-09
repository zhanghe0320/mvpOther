package com.mvp.activity.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.de.rocket.bean.ActivityParamBean;
import com.de.rocket.bean.StatusBarBean;
import com.mvp.R;
import com.mvp.base.BaseActivity;
import com.mvp.base.mvp.BasePresenter;

/**
 *
 */
public class WebActivity extends BaseActivity {

    // 定义布局内的控件
    WebView webView;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        // 实例化控件
        webView = (WebView) findViewById(R.id.main_wv);
        toWeb();
    }

    /**
     * 跳转到网络页面
     */
    public void toWeb() {

        // 设置页面显示的URL地址
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);


        // 一般要设置WebViewClient，才能让网页在本页面布局内显示，否则会跳转到浏览器软件打开页面
        webView.setWebViewClient(new WebViewClient() {
            // shouldOverrideUrlLoading方法return true
            // 表示我加载后这个Intent就消费了，不再跳转到其他页面。
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("TAG", "你点击到我了"+url);
                //重定向，不设置的话，很多网址是不能链接到的
                view.loadUrl(url);
                return true;
            }
        });
        // 取消滚动条
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // 添加网页的文本，支持java的JavaScript显示
        // webView.loadDataWithBaseURL(null, string, null, "utf-8", null);
        // 获取WebView的设置对象，能设置是否支持缩放或是否在页面显示图片等等。
        WebSettings settings = webView.getSettings();

        // 设置是否支持缩放
        settings.setSupportZoom(false);// 有时无效！
        // 设置是否阻止显示图片
        // settings.setBlockNetworkImage(true);
        // 设置先从本地获取数据，没有的话再请求网络数据
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 设置字体的大小
        settings.setTextZoom(20);
        // 支持javaScript
        settings.setJavaScriptEnabled(true);
    }

    /**
     * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            // 返回键退回
            webView.goBack();
            return true;// 直接返回
        }
        finish();// 关闭页面
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 回退页面
     */
    public void toback(View view) {
        if (webView.canGoBack()) {
            // 退回
            webView.goBack();
        } else {
            Toast.makeText(this, "没有能回退的页面了", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 前一个页面
     */
    public void topre(View view) {
        if (webView.canGoForward()) {
            // 前进
            webView.goForward();
        } else {
            Toast.makeText(this, "没有能前进的页面了", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
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
        activityParamBean.setLayoutId(R.layout.activity_spannable);//Activity布局
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
