package com.mvp.activity.navigation;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;


import com.mvp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 导航栏封装，传入不同数据 修改不同的导航栏数据。
 * 增加修改代码，可以提高通用性，如传入参数,不同，传入不同presenter
 */
public class NavigationBarRelativeLayout extends RelativeLayout
        implements View.OnClickListener, NavigationBar.INavigationBarRelativeLayout/*, CustomAdapt */{
    private static final String TAG = "NavigationBarRelativeLa";
    @BindView(R.id.navigation_one)
    TextView mNavigationOne;
    @BindView(R.id.navigation_two)
    TextView mNavigationTwo;
    @BindView(R.id.navigation_three)
    TextView mNavigationThree;
    @BindView(R.id.navigation_four)
    TextView mNavigationFour;
    @BindView(R.id.navigation_five)
    TextView mNavigationFive;

    private static NavigationBar.INavigationBarPresenter iNavigationBarPresenter;
    // private IMainPresenter iMainPresenter;
    private static NavigationBar.NavigationPresenter mNavigationPresenter;
    private static List mList = null;

    public static void setPresenter(NavigationBar.NavigationPresenter navigationPresenter, List list) {
        mNavigationPresenter = navigationPresenter;
        mList = list;
        iNavigationBarPresenter.setText(mList);
    }

    public NavigationBarRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public NavigationBarRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NavigationBarRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NavigationBarRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initData(AttributeSet attrs) {

    }

    private void init(Context Context ) {
        inflate(Context, R.layout.relativelayout_navigationbar, this);
        ButterKnife.bind(this);

        mNavigationOne.setOnClickListener(this::onClick);
        mNavigationTwo.setOnClickListener(this::onClick);
        mNavigationThree.setOnClickListener(this::onClick);
        mNavigationFour.setOnClickListener(this::onClick);
        mNavigationFive.setOnClickListener(this::onClick);

        iNavigationBarPresenter = new NavigationBarPresenter(this);

        iNavigationBarPresenter.setText(mList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navigation_one:
                mNavigationPresenter.oneOnclick();
                break;
            case R.id.navigation_two:
                mNavigationPresenter.twoOnclick();
                break;
            case R.id.navigation_three:
                mNavigationPresenter.threeOnclick();
                break;
            case R.id.navigation_four:
                mNavigationPresenter.fourOnclick();
                break;
            case R.id.navigation_five:
                mNavigationPresenter.fiveOnclick();
                break;
            default:
                break;
        }
    }

    @Override
    public void setText(List list) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {// 数据处理，按钮名称进行重置

//                 for (Iterator<String> iterator = list.getmNavigationBarBean().iterator(); iterator.hasNext(); ) {
//                 String str = (String) iterator.next();
//
//                 }
                if (list == null || list.size() < 5) {

                    return;
                }

                for (int i = 0; i < list.size(); i++) {
                    NavigationBarBean navigationBarBean = (NavigationBarBean) list.get(i);
                    switch (i) {
                        case 4:
                            if (navigationBarBean.getmPosition() != 1) {
                                mNavigationFive.setVisibility(GONE);
                            }
                            mNavigationFive.setText(navigationBarBean.getmNavigationBarBean());
                            break;
                        case 3:
                            if (navigationBarBean.getmPosition() != 1) {
                                mNavigationFour.setVisibility(GONE);
                                findViewById(R.id.navigation_v4).setVisibility(GONE);
                            }
                            mNavigationFour.setText(navigationBarBean.getmNavigationBarBean());
                            break;
                        case 2:
                            if (navigationBarBean.getmPosition() != 1) {
                                mNavigationThree.setVisibility(GONE);
                                findViewById(R.id.navigation_v3).setVisibility(GONE);
                            }
                            mNavigationThree.setText(navigationBarBean.getmNavigationBarBean());

                            break;
                        case 1:
                            if (navigationBarBean.getmPosition() != 1) {
                                mNavigationTwo.setVisibility(GONE);
                                findViewById(R.id.navigation_v2).setVisibility(GONE);
                            }
                            mNavigationTwo.setText(navigationBarBean.getmNavigationBarBean());

                            break;
                        case 0:
                            if (navigationBarBean.getmPosition() != 1) {
                                mNavigationOne.setVisibility(GONE);
                                findViewById(R.id.navigation_v1).setVisibility(GONE);
                            }
                            mNavigationOne.setText(navigationBarBean.getmNavigationBarBean());
                            break;

                    }
                }

            }
        });
    }

    /**
     * 是否按照宽度进行等比例适配 (为了保证在高宽比不同的屏幕上也能正常适配, 所以只能在宽度和高度之中选一个作为基准进行适配)
     *
     * @return {@code true} 为按照宽度适配, {@code false} 为按照高度适配
     */
//    @Override
//    public boolean isBaseOnWidth() {
//        return true;
//    }

    /**
     * 返回设计图上的设计尺寸, 单位 dp
     * {@link #getSizeInDp} 须配合 {@link #isBaseOnWidth()} 使用, 规则如下:
     * 如果 {@link #isBaseOnWidth()} 返回 {@code true}, {@link #getSizeInDp} 则应该返回设计图的总宽度
     * 如果 {@link #isBaseOnWidth()} 返回 {@code false}, {@link #getSizeInDp} 则应该返回设计图的总高度
     * 如果您不需要自定义设计图上的设计尺寸, 想继续使用在 AndroidManifest 中填写的设计图尺寸, {@link #getSizeInDp} 则返回 {@code 0}
     *
     * @return 设计图上的设计尺寸, 单位 dp
     */
//    @Override
//    public float getSizeInDp() {
//        return 360;
//    }


}
