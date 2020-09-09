package com.mvp.activity.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * wifi 设置
 */
public class WifiUtils {
	private Context mContext;
	// WifiManager对象
	public WifiManager mWifiManager;

	public WifiUtils(Context mContext) {
		this.mContext = mContext;
		mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
	}
	public boolean isWifiOPened(){
		return  mWifiManager.isWifiEnabled();
	}
	public void EnableWifi(){
		mWifiManager.setWifiEnabled(true);
	}
	/**
	 * 判断手机是否连接在Wifi上
	 */
	public boolean isConnectWifi() {
		// 获取ConnectivityManager对象
		ConnectivityManager conMgr = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获取NetworkInfo对象
		NetworkInfo info = conMgr.getActiveNetworkInfo();
		// 获取连接的方式为wifi
		NetworkInfo.State wifi = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();

		if (info != null && info.isAvailable() && wifi == NetworkInfo.State.CONNECTED)

		{
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 获取当前手机所连接的wifi信息
	 */
	public WifiInfo getCurrentWifiInfo() {
		return mWifiManager.getConnectionInfo();
	}

	/**
	 * 添加一个网络并连接 传入参数：WIFI发生配置类WifiConfiguration
	 */
	public boolean addNetwork(WifiConfiguration wcg) {
		int wcgID = mWifiManager.addNetwork(wcg);
		return mWifiManager.enableNetwork(wcgID, true);
	}

	/**
	 * 搜索附近的热点信息，并返回所有热点为信息的SSID集合数据
	 */
	public List<String> getScanWifiResult() {
		// 扫描的热点数据
		List<ScanResult> resultList;
		// 开始扫描热点
		mWifiManager.startScan();
		resultList = mWifiManager.getScanResults();
		ArrayList<String> ssids = new ArrayList<String>();
		if (resultList != null) {
			for (ScanResult scan : resultList) {
				ssids.add(scan.SSID);// 遍历数据，取得ssid数据集
			}
		}
		return ssids;
	}

	/**
	 * 连接wifi 参数：wifi的ssid及wifi的密码
	 */
	public boolean connectWifiTest(final String ssid, final String pwd) {
		boolean isSuccess = false;
		boolean flag = false;
		mWifiManager.disconnect();
		boolean addSucess = addNetwork(CreateWifiInfo(ssid, pwd, 3));
		if (addSucess) {
			while (!flag && !isSuccess) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				String currSSID = getCurrentWifiInfo().getSSID();
				if (currSSID != null)
					currSSID = currSSID.replace("\"", "");
				int currIp = getCurrentWifiInfo().getIpAddress();
				if (currSSID != null && currSSID.equals(ssid) && currIp != 0) {
					isSuccess = true;
				} else {
					flag = true;
				}
			}
		}
		return isSuccess;

	}

	/**
	 * 创建WifiConfiguration对象 分为三种情况：1没有密码;2用wep加密;3用wpa加密
	 *
	 * @param SSID
	 * @param Password
	 * @param Type
	 * @return
	 */
	public WifiConfiguration CreateWifiInfo(String SSID, String Password,
											int Type) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";

		WifiConfiguration tempConfig = this.IsExsits(SSID);
		if (tempConfig != null) {
			mWifiManager.removeNetwork(tempConfig.networkId);
		}

		if (Type == 1) // WIFICIPHER_NOPASS
		{
			config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == 2) // WIFICIPHER_WEP
		{
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + Password + "\"";
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == 3) // WIFICIPHER_WPA
		{
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}
		return config;
	}

	private WifiConfiguration IsExsits(String SSID) {
		List<WifiConfiguration> existingConfigs = mWifiManager
				.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
				return existingConfig;
			}
		}
		return null;
	}

	private static final String TAG = "WifiUtils";

	//1.第一种
	public void removeWifi(){//移除所有WIFI 信息，清除WIFI密码
		List<WifiConfiguration> conlist = mWifiManager.getConfiguredNetworks();//获取保存的配置信息
			if(conlist.size() == 0 ){//如果扫描的WIFI长度为空  就不做移除

			}else{//长度不为空，进行wifi的移除处理
				for(int i =0; i< conlist.size(); i++){
				//	Log.i(TAG,"i = " + String.valueOf(i) + "SSID = " + conlist.get(i).SSID + " netId = " + String.valueOf(conlist.get(i).networkId));
					//忘记所有wifi密码
					//mWifiManager.forget(conlist.get(i).networkId, null);
					mWifiManager.removeNetwork(conlist.get(i).networkId);
					mWifiManager.saveConfiguration();
				//	Log.i(TAG, "removeWifi: "+conlist.get(i).BSSID);
					//忘记当前wifi密码
				/*	if(i == 0){
						mWifiManager.removeNetwork(conlist.get(i).networkId);
						mWifiManager.saveConfiguration();

					}*/
			}
		}
	}
	/**
	 * 忘记某一个wifi密码
	 *
	 * @param wifiManager
	 * @param targetSsid
	 */
	public static void removeWifiBySsid(WifiManager wifiManager, String targetSsid) {
		//Log.d(TAG, "try to removeWifiBySsid, targetSsid=" + targetSsid);
		List<WifiConfiguration> wifiConfigs = wifiManager.getConfiguredNetworks();

		for (WifiConfiguration wifiConfig : wifiConfigs) {
			String ssid = wifiConfig.SSID;
		//	Log.d(TAG, "removeWifiBySsid ssid=" + ssid);
			if (ssid.equals(targetSsid)) {
			//	Log.d(TAG, "removeWifiBySsid success, SSID = " + wifiConfig.SSID + " netId = " + String.valueOf(wifiConfig.networkId));
				wifiManager.removeNetwork(wifiConfig.networkId);
				wifiManager.saveConfiguration();
			}
		}
	}
	//2.第二种
	public void removeWifi_2(){
		List<WifiConfiguration> wifiConfigs = mWifiManager.getConfiguredNetworks();
		//忘记所有wifi密码
		for(WifiConfiguration wifiConfig : wifiConfigs){
			//Log.e(TAG,"SSID = " + wifiConfig.SSID + " netId = " + String.valueOf(wifiConfig.networkId));
			mWifiManager.removeNetwork(wifiConfig.networkId);
			mWifiManager.saveConfiguration();

		}
	}

	//3.第三种
	public void removeWifi_3(){
		 List<WifiConfiguration> listeners = mWifiManager.getConfiguredNetworks();//new ArrayList<WifiConfiguration>();
		Iterator<WifiConfiguration> iterator = listeners.iterator();
		//忘记所有wifi密码
		while(iterator.hasNext()){
			WifiConfiguration wifilist = iterator.next();
			//Log.e(TAG,"SSID = " + wifilist.SSID + " netId = " + String.valueOf(wifilist.networkId));
			mWifiManager.removeNetwork(wifilist.networkId);
			mWifiManager.saveConfiguration();
		}
	}



}