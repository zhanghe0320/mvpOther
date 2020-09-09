package com.mvp.util.cache;

import android.os.Environment;
import android.util.Log;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.StringUtils;
import com.mvp.base.file.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 *缓存工具类
 */
public class ConfigCacheUtil {
    private static final String TAG=ConfigCacheUtil.class.getName();

    /** 1秒超时时间 */
    public static final int CONFIG_CACHE_SHORT_TIMEOUT=1000 * 60 * 5; // 5 分钟

    /** 5分钟超时时间 */
    public static final int CONFIG_CACHE_MEDIUM_TIMEOUT=1000 * 3600 * 2; // 2小时

    /** 中长缓存时间 */
    public static final int CONFIG_CACHE_ML_TIMEOUT=1000 * 60 * 60 * 24 * 1; // 1天

    /** 最大缓存时间 */
    public static final int CONFIG_CACHE_MAX_TIMEOUT=1000 * 60 * 60 * 24 * 7; // 7天

    /**
     * CONFIG_CACHE_MODEL_LONG : 长时间(7天)缓存模式 <br>
     * CONFIG_CACHE_MODEL_ML : 中长时间(12小时)缓存模式<br>
     * CONFIG_CACHE_MODEL_MEDIUM: 中等时间(2小时)缓存模式 <br>
     * CONFIG_CACHE_MODEL_SHORT : 短时间(5分钟)缓存模式
     */
    public enum ConfigCacheModel {
        CONFIG_CACHE_MODEL_SHORT, CONFIG_CACHE_MODEL_MEDIUM, CONFIG_CACHE_MODEL_ML, CONFIG_CACHE_MODEL_LONG;
    }

    /**
     * 获取缓存
     * @param url 访问网络的URL
     * @return 缓存数据
     */
    public static String getUrlCache(String url, ConfigCacheModel model) {
        if(url == null) {
            return null;
        }

        String result=null;
        String path="Constants.ENVIROMENT_DIR_CACHE" +" StringUtils.replaceUrlWithPlus(EncryptUtils.encryptToMD5(url))";
        File file=new File(path);
        if(file.exists() && file.isFile()) {
            long expiredTime=System.currentTimeMillis() - file.lastModified();
            Log.d(TAG, file.getAbsolutePath() + " expiredTime:" + expiredTime / 60000 + "min");
            // 1。如果系统时间是不正确的
            // 2。当网络是无效的,你只能读缓存
            if(true/*NetworkUtils.getNetworkType(GameStoreApplication.getInstance().getContext()) != NetworkState.NETWORN_NONE*/) {
                if(expiredTime < 0) {
                    return null;
                }
                if(model == ConfigCacheModel.CONFIG_CACHE_MODEL_SHORT) {
                    if(expiredTime > CONFIG_CACHE_SHORT_TIMEOUT) {
                        return null;
                    }
                } else if(model == ConfigCacheModel.CONFIG_CACHE_MODEL_MEDIUM) {
                    if(expiredTime > CONFIG_CACHE_MEDIUM_TIMEOUT) {
                        return null;
                    }
                } else if(model == ConfigCacheModel.CONFIG_CACHE_MODEL_ML) {
                    if(expiredTime > CONFIG_CACHE_ML_TIMEOUT) {
                        return null;
                    }
                } else if(model == ConfigCacheModel.CONFIG_CACHE_MODEL_LONG) {
                    if(expiredTime > CONFIG_CACHE_MEDIUM_TIMEOUT) {
                        return null;
                    }
                } else {
                    if(expiredTime > CONFIG_CACHE_MAX_TIMEOUT) {
                        return null;
                    }
                }
            }

            try {
                result=FileUtil.readTextFile(file);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 设置缓存
     * @param data
     * @param url
     */
    public static void setUrlCache(String data, String url) {
        if("Constants.ENVIROMENT_DIR_CACHE" == null) {
            return;
        }
        File dir=new File("Constants.ENVIROMENT_DIR_CACHE");
        if(!dir.exists() && Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            dir.mkdirs();
        }
        File file=new File("Constants.ENVIROMENT_DIR_CACHE" + "StringUtils.replaceUrlWithPlus(EncryptUtils.encryptToMD5(url))");
        try {
            // 创建缓存数据到磁盘，就是创建文件
            FileUtil.writeTextFile(file, data);
        } catch(IOException e) {
            Log.d(TAG, "write " + file.getAbsolutePath() + " data failed!");
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除历史缓存文件
     * @param cacheFile
     */
    public static void clearCache(File cacheFile) {
        if(cacheFile == null) {
            if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                try {
                    File cacheDir=new File(Environment.getExternalStorageDirectory().getPath() + "/hulutan/cache/");
                    if(cacheDir.exists()) {
                        clearCache(cacheDir);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        } else if(cacheFile.isFile()) {
            cacheFile.delete();
        } else if(cacheFile.isDirectory()) {
            File[] childFiles=cacheFile.listFiles();
            for(int i=0; i < childFiles.length; i++) {
                clearCache(childFiles[i]);
            }
        }
    }
}
