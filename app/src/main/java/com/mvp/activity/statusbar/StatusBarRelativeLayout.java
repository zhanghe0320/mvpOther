package com.mvp.activity.statusbar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.hjq.toast.ToastUtils;
import com.mvp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class StatusBarRelativeLayout extends RelativeLayout implements View.OnClickListener, StatusBarContract.IStatusBarRelativeLayout {
    @BindView(R.id.status_left)
    TextView mStatusLeft;
    @BindView(R.id.status_center)
    TextView mStatusCenter;
    @BindView(R.id.status_right)
    TextView mStatusRight;


    StatusBarContract.IStatusBarPresenter StatusationBarPresenter;

    public StatusBarRelativeLayout(Context context) {
        super(context);
    }

    public StatusBarRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);

    }

    public StatusBarRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StatusBarRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void initData(AttributeSet attrs) {

    }


    private void initView(Context context, AttributeSet attrs) {
        inflate(context, R.layout.relativelayout_status, this);
        ButterKnife.bind(this);
        mStatusLeft.setOnClickListener(this::onClick);
        mStatusRight.setOnClickListener(this::onClick);
        mStatusCenter.setOnClickListener(this::onClick);
        mStatusCenter.setText("主要标题栏");
        if (((Activity) context).getComponentName().getClassName().toLowerCase().contains("mainactivity")) {
            mStatusLeft.setText("返回");
        } else {
            mStatusLeft.setText("退出");
        }

        mStatusRight.setText("设置");

        StatusationBarPresenter = new StatusationBarPresenter(this);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.status_left:
                StatusationBarPresenter.statusLeft((Activity) getContext());
                break;
            case R.id.status_right:
                StatusationBarPresenter.statusRight((Activity) getContext());
                break;
            case R.id.status_center:
                StatusationBarPresenter.statusCenter((Activity) getContext());
                break;
            default:
                break;
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void statusLeft(Activity activity) {
        if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
            return;
        }

        //如果实在主页 退出app之前进行提示  其他界面直接退出
        if (activity.getComponentName().getClassName().toLowerCase().contains("mainactivity")) {
            new AlertDialog.Builder(activity)
                    .setTitle("确认退出吗？")
                    .setIcon(R.mipmap.ic_launcher_round)
                    .setMessage("即将退出app")
                    .setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // MvpApplication.removeActivity(activity);
                                    //ActivityUtils.finishActivity(activity);

                                    activity.finish();
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ToastUtils.show("querasjd");
                        }
                    }).create()
                    .show();

        } else {
            //ActivityUtils.finishActivity(activity);
            // BaseApplication.removeActivity(activity);
            activity.finish();
            // activity.finish();
        }

    }

    @Override
    public void statusCenter(Activity activity) {
        ToastUtils.show("标题点击事件");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void statusRight(Activity activity) {

        if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
            return;
        }
        ToastUtils.show("主页面点击事件");
    }
}