package com.mvp.activity.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.de.rocket.bean.ActivityParamBean;
import com.de.rocket.bean.StatusBarBean;
import com.mvp.R;
import com.mvp.activity.navigation.NavigationBar;
import com.mvp.activity.navigation.NavigationBarBean;
import com.mvp.activity.navigation.NavigationBarRelativeLayout;
import com.mvp.api.ApiRetrofit;
import com.mvp.base.BaseActivity;
import com.mvp.base.BaseContent;
import com.mvp.base.mvp.Base2Model;
import com.mvp.base.mvp.BaseModel;
import com.mvp.bean.Bean1;
import com.mvp.bean.Bean2;
import com.mvp.bean.Bean3;
import com.mvp.bean.MainBean;
import com.mvp.bean.MainBean2;
import com.mvp.bean.TextBean;
import com.mvp.commonutils.L;
import com.mvp.commonutils.RetrofitUtil;
import com.mvp.commonutils.netstatus.NetWorkMonitor;
import com.mvp.commonutils.netstatus.NetWorkState;
import com.mvp.serialPort.SerialPortUtil;
import com.mvp.util.DiskLruCacheUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxActivity extends BaseActivity<RxPresenter> implements RxView, View.OnClickListener, NavigationBar.NavigationPresenter {
   //缓存处理
    private DiskLruCacheUtils mDiskLruCacheUtils;//创建对象
    private static final String DISK_CACHE_SUBDIR = "temp"; //设置图片缓存的文件
    private static final int DISK_CACHE_SIZE= 100*1024*1024; // 设置SD卡缓存的大小
    @BindView(R.id.rx_recyclerview)
    RecyclerView recyclerView;
    NavigationBarRelativeLayout relativelayout;
    List list= new ArrayList<>();
    @Override
    protected RxPresenter createPresenter() {
        return new RxPresenter(this,this,getSupportFragmentManager());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置成全屏模式
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        //设置屏幕为横屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        setContentView(R.layout.activity_rx);
        // setContentView( getLayoutId());
//        if( Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION//隐藏nav栏
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN//隐藏状态栏
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//            );
//        }
        ButterKnife.bind(this);
        initData();




        mDiskLruCacheUtils = DiskLruCacheUtils.getInstance();
        mDiskLruCacheUtils.open(this,DISK_CACHE_SUBDIR,DISK_CACHE_SIZE);//打开缓存
        DiskLruCacheUtils.getInstance().loadBitmap("sss",null);//加载图片
        mPresenter.initBtn();
        byte [] bytes = "asd".getBytes();
        SerialPortUtil.getInstance().onCreate();
        SerialPortUtil.getInstance().SendMessage(bytes);
        SerialPortUtil.getInstance().closeSerialPort();

//        Intent intent =new Intent(RxActivity.this,DialogActivity.class);
//        startActivity(intent);


       NavigationBarBean navigationBarBean = new NavigationBarBean(1,"所有");
        list.add(navigationBarBean);
        navigationBarBean = new NavigationBarBean(1,"新闻");
        list.add(navigationBarBean);
        navigationBarBean = new NavigationBarBean(1,"网页");
        list.add(navigationBarBean);
        navigationBarBean = new NavigationBarBean(1,"文字");
        list.add(navigationBarBean);
        navigationBarBean = new NavigationBarBean(1,"我的");
        list.add(navigationBarBean);

        NavigationBarRelativeLayout.setPresenter(this,list);

    }
    //或者 仅监听某一种网络变化
    @NetWorkMonitor(/*monitorFilter = {NetWorkState.NET_NO_CONNECTION,//无连接
            NetWorkState.NET_TYPE_WIFI,//WIFI
            NetWorkState.NET_TYPE_2G,//2G
            NetWorkState.NET_TYPE_3G,//3G
            NetWorkState.NET_TYPE_4G,//4G
            NetWorkState.NET_TYPE_5G,//5G
            NetWorkState.NET_TYPE_UNKNOWN//未知
    }*/)//接收网络状态变化
    public void onNetWorkStateChange(NetWorkState netWorkState) {
        LogUtils.i("onNetWorkStateChange:" + netWorkState.name());
    }
    /**
     * Activity全局配置信息
     *
     * @return ActivityParamBean
     */
    @Override
    public ActivityParamBean initProperty() {
        ActivityParamBean activityParamBean = new ActivityParamBean();
        activityParamBean.setLayoutId(R.layout.activity_rx);//Activity布局
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
     * 页面初始化
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


    @BindView(R.id.tv_text)
    TextView mTvText;
    @BindView(R.id.btn_net)
    Button btn_net;
    @BindView(R.id.btn_img)
    Button btn_img;
    @BindView(R.id.btn_upload)
    Button btn_upload;
    @BindView(R.id.btn_mvc)
    Button btn_mvc;
    @BindView(R.id.btn_test1)
    Button btn_test1;
    @BindView(R.id.btn_test2)
    Button btn_test2;
    @BindView(R.id.btn_test3)
    Button btn_test3;
    @BindView(R.id.btn_down)
    Button btn_down;
    @BindView(R.id.myother)
    Button myother;
    @BindView(R.id.listview)
    ListView listview;


    //监听事件
    @SuppressLint("ResourceAsColor")
    @OnClick({R.id.myother, R.id.btn_net, R.id.btn_img, R.id.btn_upload, R.id.btn_mvc, R.id.btn_test1, R.id.btn_test2, R.id.btn_test3, R.id.btn_down})
//多个控件可以一起发在里面进行监听
    public void onViewClicked(View v) {
        switch (v.getId()) {
            //默认请求
            case R.id.btn_net:
                mPresenter.getTextApi();

                btn_net.setSelected(true);
                btn_img.setSelected(false);
                btn_upload.setSelected(false);
                btn_mvc.setSelected(false);
                btn_test1.setSelected(false);
                btn_test2.setSelected(false);
                btn_test3.setSelected(false);
                btn_down.setSelected(false);
                myother.setSelected(false);


                break;
            //上传图片
            case R.id.btn_img:
//                upLoadImage();
                getDataApi2();
                btn_net.setSelected(false);
                btn_img.setSelected(true);
                btn_upload.setSelected(false);
                btn_mvc.setSelected(false);
                btn_test1.setSelected(false);
                btn_test2.setSelected(false);
                btn_test3.setSelected(false);
                btn_down.setSelected(false);
                myother.setSelected(false);

                break;
            //上传文件进度测试
            case R.id.btn_upload:
                mPresenter.upLoadVideoApi(BaseContent.baseFileName + "ceshi.mp4");
                btn_net.setSelected(false);
                btn_img.setSelected(false);
                btn_upload.setSelected(true);
                btn_mvc.setSelected(false);
                btn_test1.setSelected(false);
                btn_test2.setSelected(false);
                btn_test3.setSelected(false);
                btn_down.setSelected(false);
                myother.setSelected(false);

                break;
            //mvc测试
            case R.id.btn_mvc:
                //startActivity(MvcActivity.class);
                mPresenter.getCheShiApi();
                btn_net.setSelected(false);
                btn_img.setSelected(false);
                btn_upload.setSelected(false);
                btn_mvc.setSelected(true);
                btn_test1.setSelected(false);
                btn_test2.setSelected(false);
                btn_test3.setSelected(false);
                btn_down.setSelected(false);
                myother.setSelected(false);

                break;
            //多基类测试
            case R.id.btn_test1:
                mPresenter.getTableListApi();
                btn_net.setSelected(false);
                btn_img.setSelected(false);
                btn_upload.setSelected(false);
                btn_mvc.setSelected(false);
                btn_test1.setSelected(true);
                btn_test2.setSelected(false);
                btn_test3.setSelected(false);
                btn_down.setSelected(false);
                myother.setSelected(false);

                break;
            //多基类测试
            case R.id.btn_test2:
                mPresenter.getRestrictionsApi();
                btn_net.setSelected(false);
                btn_img.setSelected(false);
                btn_upload.setSelected(false);
                btn_mvc.setSelected(false);
                btn_test1.setSelected(false);
                btn_test2.setSelected(true);
                btn_test3.setSelected(false);
                btn_down.setSelected(false);
                myother.setSelected(false);

                break;
            //多基类测试
            case R.id.btn_test3:
                //  mPresenter.getCheShiApi();
                mPresenter.getCheShiApi2();
                btn_net.setSelected(false);
                btn_img.setSelected(false);
                btn_upload.setSelected(false);
                btn_mvc.setSelected(false);
                btn_test1.setSelected(false);
                btn_test2.setSelected(false);
                btn_test3.setSelected(true);
                btn_down.setSelected(false);
                myother.setSelected(false);

//                Dao dao = new Dao();
//                dao.setName("ada");
//                DbManager.getDaoSession(this).insertOrReplace(dao);
//                DbManager.getDaoSession(this).refresh(dao);
//                DbManager.getDaoSession(this).update(dao);
                // DbManager.getDaoSession(this).delete(dao);
                // DbManager.getDaoSession(this).deleteAll(dao.getClass());

                //DbManager.getDaoSession(this).getDaoDao().insert(dao);
                break;
            //文件上传测试
            case R.id.btn_down:
                btn_net.setSelected(false);
                btn_img.setSelected(false);
                btn_upload.setSelected(false);
                btn_mvc.setSelected(false);
                btn_test1.setSelected(false);
                btn_test2.setSelected(false);
                btn_test3.setSelected(false);
                btn_down.setSelected(true);
                myother.setSelected(false);
                startActivity(new Intent(RxActivity.this, SpannableActivity.class));
                //startActivity(FileActivity.class);
                getDataApi();
                break;
            case R.id.myother://其他查询
                mPresenter.myother();
                btn_net.setSelected(false);
                btn_img.setSelected(false);
                btn_upload.setSelected(false);
                btn_mvc.setSelected(false);
                btn_test1.setSelected(false);
                btn_test2.setSelected(false);
                btn_test3.setSelected(false);
                btn_down.setSelected(false);
                myother.setSelected(true);

                break;


        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }


    // 页面自己的view 处理

    /**
     * 主页加载成功
     *
     * @param o
     */
    @Override
    public void onMainSuccess(BaseModel<List<MainBean>> o) {
//        Log.e(o.getErrmsg(), "");
//        Log.e(o.getErrcode() + "", "");
//        mainBeans.addAll(o.getData());
//        Log.e("sm", mainBeans.toString() + "");
//        mTvText.setText(o.getData().toString());
        L.e("onMainSuccess=" + o.getResult());

    }

    /**
     * 文字内容加载成功过
     *
     * @param o
     */
    @Override
    public void onTextSuccess(BaseModel<TextBean> o) {
        mTvText.setText(o.getResult().getData().toString());
        L.e("onTextSuccess=" + o.getResult());

    }


    /**
     * 图片加载成功
     *
     * @param o
     */
    @Override
    public void onUpLoadImgSuccess(BaseModel<Object> o) {
        /**
         * 俩个参数  一个是图片集合路径   一个是和后台约定的Key，如果后台不需要，随便写都行
         */
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            strings.add("tupian.lujing");
        }
        mPresenter.upLoadImgApi(
                "title",
                "content",
                RetrofitUtil.filesToMultipartBodyParts(RetrofitUtil.initImages(strings), "tupian.key"));
        L.e("文件视频路径==" + o.getResult());
    }

    /**
     * 表格加载成功
     *
     * @param o
     */
    @Override
    public void onTableListSuccess(BaseModel<Object> o) {
        L.e("测试多基类1=" + o.getResult());
    }

    /**
     * @param o
     */
    @Override
    public void onRestrictionsSuccess(BaseModel<Object> o) {
        L.e("测试多基类2=" + o.getResult());
    }

    /**
     * 测试
     *
     * @param o
     */
    @Override
    public void onCheShiSuccess(BaseModel<Object> o) {
        L.e("测试多基类3=" + o.getResult());
    }

    /**
     * 其他
     *
     * @param o
     */
    @Override
    public void onMyOtherSuccess(MainBean2 o) {
        mTvText.setText(o.toString());
        L.e("onMyOtherSuccess=" + o.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initBtn(ArrayList list) {

        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager =  new GridLayoutManager(this, 1);

        RxAdapter rxAdapter = new RxAdapter(recyclerView, this, list,mPresenter,mDiskLruCacheUtils);
        recyclerView.setLayoutManager(layoutManager);
       // recyclerView.getScrollState();
//        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//            }
//        });

        recyclerView.setAdapter(rxAdapter);
    }




    @SuppressLint("CheckResult")
    public void getDataApi2() {
        //http://www.nj-lsj.net/Taste/mechineShelfList.do
        //  equipmentCode   201904190003000
        HashMap<String, String> params = new HashMap<>();
        params.put("equipmentCode", "201904190003000");
        // params.put("key", "2c1cb93f8c7430a754bc3ad62e0fac06");
        ApiRetrofit.getBaseUrlInstance("https://www.nj-lsj.net/Taste/").getApiService()
                .getText(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<BaseModel<TextBean>, BaseModel<TextBean>>() {
                    @Override
                    public BaseModel<TextBean> apply(BaseModel<TextBean> objectBaseModel) throws Exception {
                        return objectBaseModel;
                    }
                }).subscribe(new Consumer<BaseModel<TextBean>>() {
            @Override
            public void accept(BaseModel<TextBean> o) throws Exception {
                mTvText.setText(o.getResult().getData().toString());
                LogUtils.i(o.getResult().getData().toString());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });


        // params.put("key", "2c1cb93f8c7430a754bc3ad62e0fac06");
        ApiRetrofit.getBaseUrlInstance("https://www.nj-lsj.net/Taste/").getApiService()
                .getMain3Demo(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Base2Model<List<Bean1>, Bean2, List<Bean3>>, Object>() {
                    @Override
                    public Object apply(Base2Model<List<Bean1>, Bean2, List<Bean3>> listBean2ListBase2Model) throws Exception {
                        return null;
                    }
                }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                mTvText.setText(o.toString());
                LogUtils.i(o.toString());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    @SuppressLint("CheckResult")
    public void getDataApi() {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", "junshi");
        params.put("key", "2c1cb93f8c7430a754bc3ad62e0fac06");
        ApiRetrofit.getInstance().getApiService()
                .getText(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<BaseModel<TextBean>, BaseModel<TextBean>>() {
                    @Override
                    public BaseModel<TextBean> apply(BaseModel<TextBean> objectBaseModel) throws Exception {
                        return objectBaseModel;
                    }
                }).
                subscribe(new Consumer<BaseModel<TextBean>>() {
                    @Override
                    public void accept(BaseModel<TextBean> o) throws Exception {
                        mTvText.setText(o.getResult().getData().toString());
                        LogUtils.i(o.getResult().getData().toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }


    @Override
    protected void onPause() {
        super.onPause();
        mDiskLruCacheUtils.flush(); //刷新缓存
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDiskLruCacheUtils = DiskLruCacheUtils.getInstance();
        mDiskLruCacheUtils.open(this,DISK_CACHE_SUBDIR,DISK_CACHE_SIZE);//打开缓存
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDiskLruCacheUtils.close(); //关闭缓存
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void oneOnclick() {
        ToastUtils.showShort("点击一");
    }

    @Override
    public void twoOnclick() {
        ToastUtils.showShort("点击二");
    }

    @Override
    public void threeOnclick() {
        ToastUtils.showShort("点击三");
    }

    @Override
    public void fourOnclick() {
        ToastUtils.showShort("点击四");
    }

    @Override
    public void fiveOnclick() {
        ToastUtils.showShort("点击五");
    }
}
