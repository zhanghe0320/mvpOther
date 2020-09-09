package com.mvp.activity.wifi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.de.rocket.bean.ActivityParamBean;
import com.de.rocket.ue.activity.RoActivity;
import com.mvp.base.MvpApplication;
import com.mvp.R;
import com.mvp.activity.wifi.adapter.WifiListAdapter;
import com.mvp.activity.wifi.app.AppContants;
import com.mvp.activity.wifi.dialog.WifiLinkDialog;
import com.mvp.activity.wifi.utils.CollectionUtils;
import com.mvp.activity.wifi.bean.WifiBean;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WifiActivity extends RoActivity {

    private static final String TAG = "WifiActivity2";
    //权限请求码
    private static final int PERMISSION_REQUEST_CODE = 0;
    //两个危险权限需要动态申请
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private boolean mHasPermission;

    ProgressBar pbWifiLoading;

    List<WifiBean> realWifiList = new ArrayList<>();

    private WifiListAdapter adapter;

    private RecyclerView recyWifiList;

    private WifiActivity.WifiBroadcastReceiver wifiReceiver;

    private int connectType = 0;//1：连接成功？ 2 正在连接（如果wifi热点列表发生变需要该字段）
    private Button wifi_back_systembase;

    public static WifiManager mWiFiManager;
    private ImageView wifi_img;
    private ImageView wifi_img2;
    /**
     * 开启WiFi
     *
     *
     */
    public void openWiFi() {
        // 判断是否已经打开WiFi
        if (!mWiFiManager.isWifiEnabled()) {
            // 打开Wifi连接
            mWiFiManager.setWifiEnabled(true);
            Message message = Message.obtain();
            message.what = 2;
            handler.sendMessage(message);
            //   Log.i(TAG, "openWiFi: 开启");
            recyWifiList = (RecyclerView) this.findViewById(R.id.recy_list_wifi);
            adapter = new WifiListAdapter(MvpApplication.getInstance(),realWifiList);
            recyWifiList.setLayoutManager(new LinearLayoutManager(this));
            recyWifiList.setAdapter(adapter);


         /*   if(WifiSupport.isOpenWifi(WifiActivity2.this) && mHasPermission){
                sortScaResult();//转换参数
            }else{
                // Toast.makeText(WifiActivity2.this,"WIFI处于关闭状态或权限获取失败22222",Toast.LENGTH_SHORT).show();
            }*/

            adapter.setOnItemClickListener(new WifiListAdapter.onItemClickListener() {
                @Override
                public void onItemClick(View view, int postion, Object o) {
                    WifiBean wifiBean = realWifiList.get(postion);
                    if(wifiBean.getState().equals(AppContants.WIFI_STATE_UNCONNECT) || wifiBean.getState().equals(AppContants.WIFI_STATE_CONNECT)){
                        String capabilities = realWifiList.get(postion).getCapabilities();
                        if(WifiSupport.getWifiCipher(capabilities) == WifiSupport.WifiCipherType.WIFICIPHER_NOPASS){//无需密码
                            WifiConfiguration tempConfig  = WifiSupport.isExsits(wifiBean.getWifiName(), WifiActivity.this);
                            if(tempConfig == null){
                                WifiConfiguration exsits = WifiSupport.createWifiConfig(wifiBean.getWifiName(), null, WifiSupport.WifiCipherType.WIFICIPHER_NOPASS);
                                WifiSupport.addNetWork(exsits, WifiActivity.this);
                            }else{
                                WifiSupport.addNetWork(tempConfig, WifiActivity.this);
                            }
                        }else{   //需要密码，弹出输入密码dialog
                            noConfigurationWifi(postion);
                        }
                    }
                }
            });
        }
    }

    /**
     * 关闭WiFi
     *
     *
     */
    public void closeWiFi() {
        if (mWiFiManager.isWifiEnabled()) {
            // 关闭Wifi连接
            mWiFiManager.setWifiEnabled(false);
            // Log.i(TAG, "closeWiFi: 关闭");
            Message message = Message.obtain();
            message.what = 1;
            handler.sendMessage(message);

            recyWifiList = (RecyclerView) this.findViewById(R.id.recy_list_wifi);
            realWifiList.clear();
            adapter = new WifiListAdapter(MvpApplication.getInstance(),realWifiList);
            recyWifiList.setLayoutManager(new LinearLayoutManager(this));
            recyWifiList.setAdapter(adapter);
        }
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){


            switch (msg.what){//延时更新UI
                case 1://wifi关闭
                    wifi_img.setImageResource(0);
                   // wifi_img.setImageResource(R.drawable.wifi_off);
                    break;
                case 2://wifi开启
                    wifi_img.setImageResource(0);
                   // wifi_img.setImageResource(R.drawable.wifi_on);
                    break;
                default:
                    break;
            }



        }
    };
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

//        Intent intentOne = new Intent(OneSmockApplication.getInstance(), Backservice.class);
//        stopService(intentOne);
        if(wifiReceiver != null){
            this.unregisterReceiver(wifiReceiver);
        }

        //注册广播
        wifiReceiver = new WifiActivity.WifiBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//监听wifi是开关变化的状态
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//监听wifi连接状态广播,是否连接了一个有效路由
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//监听wifi列表变化（开启一个热点或者关闭一个热点）
        this.registerReceiver(wifiReceiver, filter);

        mHasPermission = checkPermission();
        if (!mHasPermission && WifiSupport.isOpenWifi(WifiActivity.this)) {  //未获取权限，申请权限
            requestPermission();


        }else if(mHasPermission && WifiSupport.isOpenWifi(WifiActivity.this)){  //已经获取权限
            initRecycler();
/*            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                        }

                    }).start();
                }
            }, 4*1000);*/

        }else{
            // Toast.makeText(WifiActivity22.this,"WIFI处于关闭状态",Toast.LENGTH_SHORT).show();

        }



        // 获取WiFi管理者对象
        mWiFiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
      /*  if(mWiFiManager.getWifiState()){

        }*/
        wifi_img = findViewById(R.id.wifi_img);
        mWiFiManager.getWifiState();
        //  Log.i(TAG, "onCreate: "+"------------"+mWiFiManager.getWifiState());
        openWiFi();
        if (mWiFiManager.getWifiState()==3) {//实时刷新UI信息状态
            Message message = Message.obtain();
            message.what = 2;
            handler.sendMessage(message);
        }else{
            Message message = Message.obtain();
            message.what = 1;
            handler.sendMessage(message);



        }


        pbWifiLoading = (ProgressBar) this.findViewById(R.id.pb_wifi_loading);
        // wifi_img2 =findViewById(R.id.wifi_img2);
        hidingProgressBar();





    }

    /**
     * Activity全局配置信息
     *
     * @return ActivityParamBean
     */
    @Override
    public ActivityParamBean initProperty() {
        return null;
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


    //返回主页
    public void back_to_main(View view) {


    }


    //返回主页
    public void back_to_main2(View view) {

    }


    //返回主页
    public void wifi_on_off(View view) {
        if (!mWiFiManager.isWifiEnabled()){

            openWiFi();
        }else{
            closeWiFi();
        }
    }



    private void initRecycler() {
        recyWifiList = (RecyclerView) this.findViewById(R.id.recy_list_wifi);
        adapter = new WifiListAdapter(MvpApplication.getInstance(),realWifiList);
        recyWifiList.setLayoutManager(new LinearLayoutManager(this));
        recyWifiList.setAdapter(adapter);

        if(WifiSupport.isOpenWifi(WifiActivity.this) && mHasPermission){
            sortScaResult();
        }else{
            // Toast.makeText(WifiActivity22.this,"WIFI处于关闭状态或权限获取失败22222",Toast.LENGTH_SHORT).show();
        }

        adapter.setOnItemClickListener(new WifiListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, Object o) {
                WifiBean wifiBean = realWifiList.get(postion);
                if(wifiBean.getState().equals(AppContants.WIFI_STATE_UNCONNECT) || wifiBean.getState().equals(AppContants.WIFI_STATE_CONNECT)){
                    String capabilities = realWifiList.get(postion).getCapabilities();
                    if(WifiSupport.getWifiCipher(capabilities) == WifiSupport.WifiCipherType.WIFICIPHER_NOPASS){//无需密码
                        WifiConfiguration tempConfig  = WifiSupport.isExsits(wifiBean.getWifiName(), WifiActivity.this);
                        if(tempConfig == null){
                            WifiConfiguration exsits = WifiSupport.createWifiConfig(wifiBean.getWifiName(), null, WifiSupport.WifiCipherType.WIFICIPHER_NOPASS);
                            WifiSupport.addNetWork(exsits, WifiActivity.this);
                        }else{
                            WifiSupport.addNetWork(tempConfig, WifiActivity.this);
                        }
                    }else{   //需要密码，弹出输入密码dialog
                        noConfigurationWifi(postion);
                    }
                }
            }
        });
    }


    private void noConfigurationWifi(int position) {//之前没配置过该网络， 弹出输入密码界面
        WifiLinkDialog linkDialog = new WifiLinkDialog(this, R.style.FullScreenDialog,realWifiList.get(position).getWifiName(), realWifiList.get(position).getCapabilities());


        // new AlertDialog.Builder(manufactorSetActivity.this,R.style.FullScreenDialog).create();
        if(!linkDialog.isShowing()){
            linkDialog.show();
//            Intent intentOne = new Intent(OneSmockApplication.getInstance(), Backservice.class);
//            stopService(intentOne);
//            ServiceController.SocketInit();
//            startService(intentOne);
//            isAddFriend = true;
        }
    }

    /**
     * 调用onCreate(), 目的是刷新数据,  从另一activity界面返回到该activity界面时, 此方法自动调用
     */
    @Override
    protected void onResume() {
        super.onResume();

        //注册广播
        wifiReceiver = new WifiActivity.WifiBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//监听wifi是开关变化的状态
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//监听wifi连接状态广播,是否连接了一个有效路由
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//监听wifi列表变化（开启一个热点或者关闭一个热点）
        this.registerReceiver(wifiReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //  backMain.cancel();
        //  Log.i(TAG, "onDestroy: 注销WiFi监听");

        this.unregisterReceiver(wifiReceiver);
    }


    //监听wifi状态
    public class WifiBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())){
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (state){
                    /**
                     * WIFI_STATE_DISABLED    WLAN已经关闭
                     * WIFI_STATE_DISABLING   WLAN正在关闭
                     * WIFI_STATE_ENABLED     WLAN已经打开
                     * WIFI_STATE_ENABLING    WLAN正在打开
                     * WIFI_STATE_UNKNOWN     未知
                     */
                    case WifiManager.WIFI_STATE_DISABLED:{
                        //     Log.d(TAG,"已经关闭");


//                        Intent intentOne = new Intent(OneSmockApplication.getInstance(), Backservice.class);
//                        stopService(intentOne);
                        // Toast.makeText(WifiActivity22.this,"WIFI处于关闭状态",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case WifiManager.WIFI_STATE_DISABLING:{
                        //     Log.d(TAG,"正在关闭");
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLED:{
                        //      Log.d(TAG,"已经打开");
                        sortScaResult();
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLING:{
                        //     Log.d(TAG,"正在打开");
                        break;
                    }
                    case WifiManager.WIFI_STATE_UNKNOWN:{
                        //   Log.d(TAG,"未知状态");
                        break;
                    }
                }
            }else if(WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())){
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                Log.d(TAG, "--NetworkInfo--" + info.toString());
                if(NetworkInfo.State.DISCONNECTED == info.getState()){//wifi没连接上
                    //    Log.d(TAG,"wifi没连接上");
                    hidingProgressBar();
                    for(int i = 0;i < realWifiList.size();i++){//没连接上将 所有的连接状态都置为“未连接”
                        realWifiList.get(i).setState(AppContants.WIFI_STATE_UNCONNECT);
                    }
                    adapter.notifyDataSetChanged();
                }else if(NetworkInfo.State.CONNECTED == info.getState()){//wifi连接上了
                    //   Log.d(TAG,"wifi连接上了");
                    hidingProgressBar();
                    WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(WifiActivity.this);

                    //连接成功 跳转界面 传递ip地址
                    //Toast.makeText(WifiActivity22.this,"wifi连接上了",Toast.LENGTH_SHORT).show();
                    // xmppConnect.xmppLogin(xmppConnect.getuserName());
                    connectType = 1;
                    wifiListSet(connectedWifiInfo.getSSID(),connectType);
                }else if(NetworkInfo.State.CONNECTING == info.getState()){//正在连接
                    //   Log.d(TAG,"wifi正在连接");
                    showProgressBar();
                    WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(WifiActivity.this);
                    connectType = 2;
                    wifiListSet(connectedWifiInfo.getSSID(),connectType );
                }
            }else if(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())){
                //    Log.d(TAG,"网络列表变化了");
                wifiListChange();
            }
        }
    }

    /**
     * //网络状态发生改变 调用此方法！
     */
    public void wifiListChange(){
        sortScaResult();
        WifiInfo connectedWifiInfo = WifiSupport.getConnectedWifiInfo(this);
        if(connectedWifiInfo != null){
            wifiListSet(connectedWifiInfo.getSSID(),connectType);
        }
    }

    /**
     * 将"已连接"或者"正在连接"的wifi热点放置在第一个位置
     * @param wifiName
     * @param type
     */
    public void wifiListSet(String wifiName , int type){
        int index = -1;
        WifiBean wifiInfo = new WifiBean();
        if(CollectionUtils.isNullOrEmpty(realWifiList)){
            return;
        }
        for(int i = 0;i < realWifiList.size();i++){
            realWifiList.get(i).setState(AppContants.WIFI_STATE_UNCONNECT);
        }
        Collections.sort(realWifiList);//根据信号强度排序
        for(int i = 0;i < realWifiList.size();i++){
            WifiBean wifiBean = realWifiList.get(i);
            if(index == -1 && ("\"" + wifiBean.getWifiName() + "\"").equals(wifiName)){
                index = i;
                wifiInfo.setLevel(wifiBean.getLevel());
                wifiInfo.setWifiName(wifiBean.getWifiName());
                wifiInfo.setCapabilities(wifiBean.getCapabilities());
                if(type == 1){
                    wifiInfo.setState(AppContants.WIFI_STATE_CONNECT);
                }else{
                    wifiInfo.setState(AppContants.WIFI_STATE_ON_CONNECTING);
                }
            }
        }
        if(index != -1){
            realWifiList.remove(index);
            realWifiList.add(0, wifiInfo);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 检查是否已经授予权限
     * @return
     */
    private boolean checkPermission() {
        for (String permission : NEEDED_PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 申请权限
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                NEEDED_PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    /**
     * 获取wifi列表然后将bean转成自己定义的WifiBean
     */
    public void sortScaResult(){
        List<ScanResult> scanResults = WifiSupport.noSameName(WifiSupport.getWifiScanResult(this));
        //   Log.i(TAG, "sortScaResult: "+scanResults.size());

        realWifiList.clear();
        if(!CollectionUtils.isNullOrEmpty(scanResults)){
            for(int i = 0;i < scanResults.size();i++){


                WifiBean wifiBean = new WifiBean();
                wifiBean.setWifiName(scanResults.get(i).SSID);
                wifiBean.setState(AppContants.WIFI_STATE_UNCONNECT);   //只要获取都假设设置成未连接，真正的状态都通过广播来确定
                wifiBean.setCapabilities(scanResults.get(i).capabilities);
                wifiBean.setLevel(WifiSupport.getLevel(scanResults.get(i).level)+"");
                realWifiList.add(wifiBean);

                //排序
                Collections.sort(realWifiList);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasAllPermission = true;
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i : grantResults) {
                if (i != PackageManager.PERMISSION_GRANTED) {
                    hasAllPermission = false;   //判断用户是否同意获取权限
                    break;
                }
            }

            //如果同意权限
            if (hasAllPermission) {
                mHasPermission = true;
                if(WifiSupport.isOpenWifi(WifiActivity.this) && mHasPermission){  //如果wifi开关是开 并且 已经获取权限
                    initRecycler();
                }else{
                    Toast.makeText(WifiActivity.this,"WIFI处于关闭状态或权限获取失败1111",Toast.LENGTH_SHORT).show();
                }

            } else {  //用户不同意权限
                mHasPermission = false;
                Toast.makeText(WifiActivity.this,"获取权限失败",Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void showProgressBar() {
        pbWifiLoading.setVisibility(View.VISIBLE);
    }

    public void hidingProgressBar() {
        pbWifiLoading.setVisibility(View.GONE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
   /*     if (wifiReceiver != null) {

            OneSmockApplication.getInstance().unregisterReceiver(wifiReceiver);
            Log.i(TAG, "onDestroy: 注销WiFi监听");
        }*/
    }
}
