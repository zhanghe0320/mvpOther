package com.mvp.activity.main;

import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.de.rocket.bean.ActivityParamBean;
import com.de.rocket.bean.StatusBarBean;
import com.mvp.R;
import com.mvp.activity.MainActivity;
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

/**
 *
 */
public class SpannableActivity extends BaseActivity {



    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //显示的字符串
        String str = "  这是设置TextView部分文字背景颜色和前景颜色的demo!可以点击连接的地址：我的博客";
        int bstart = str.indexOf("背景");
        int bend = bstart + "背景".length();
        int fstart = str.indexOf("前景");
        int fend = fstart + "前景".length();
        int baidustart = str.indexOf("我的博客");
        int baiduend = baidustart + "我的博客".length();
        //创建SpannableString对象，要传入字符串对象
        SpannableString style = new SpannableString(str);
        //设置多个Span对象
        style.setSpan(new BackgroundColorSpan(Color.RED), bstart, bend,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.RED), fstart, fend,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置超链接Span对象的自定义设置
        style.setSpan(new ClickableSpan() {

            // 在onClick方法中，指向
            @Override
            public void onClick(View widget) {
                Log.e("TAG", "你点击到我了");
                // 页面的跳转
                Intent intent = new Intent(SpannableActivity.this, WebActivity.class);
                intent.putExtra("url",
                        "http://blog.csdn.net/wenzhi20102321?viewmode=list");
                startActivity(intent);
            }

        }, baidustart, baiduend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //找到TextView控件
        TextView tvColor = (TextView) findViewById(R.id.tv);
        // 在点击连接时，有执行的动作，都必须设置MovementMethod对象
        tvColor.setMovementMethod(LinkMovementMethod.getInstance());
        // 给TextView添加图片
        Drawable d = getResources().getDrawable(R.drawable.ic_launcher);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        style.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//显示在字符串开始的地方
        //给TextView控件设置SpannableString对象
        tvColor.setText(style);
    }

    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_spannable;
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
