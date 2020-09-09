package com.wgd.gdcp.gdcplibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Log;

import com.wgd.gdcp.gdcplibrary.thread.ThreadManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/*
* change the picture width and height
* Single picture compression
*
* Please apply for permission dynamically
 * android.permission.WRITE_EXTERNAL_STORAGE
 * android.permission.READ_EXTERNAL_STORAGE
*
* 备注：20190314
* 通过处理批量压缩发现：
* 这个逻辑的处理方式由于每创建一个线程，都会在线程中产生新的Bitmap，
* 所以产生OOM的几率比较大
* 故这里应该禁止通过循环调用此方式来大量处理图片
*
* */
public class GDCompressC {

    private Context mContext;
    private GDCompressImageListener mGDCompressImageListener;



    //为了将Bitmap释放使用
    private List<Bitmap> garbage = new ArrayList<>();

    public GDCompressC(Context context, GDConfig mGDConfig, GDCompressImageListener mGDCompressImageListener){
        this.mContext = context ;
        this.mGDCompressImageListener = mGDCompressImageListener ;
        start(mGDConfig);
    }

    public GDCompressC(Context context, GDCompressImageListener mGDCompressImageListener){
        this.mContext = context ;
        this.mGDCompressImageListener = mGDCompressImageListener ;
    }


    public void start(GDConfig gdConfig){
        if (null==gdConfig)gdConfig = new GDConfig();
        if (!GDTools.ImageTesting(gdConfig.getmPath())){
            InformCallError(1, "Incorrect picture format!");
            return;
        }
        if (null==gdConfig.getSavePath() || TextUtils.equals("", gdConfig.getSavePath())){
            gdConfig.setSavePath(gdConfig.getmPath());
        }
        final GDConfig mGDConfig = gdConfig;
        ThreadManager.getIO().execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmapMin = null;
                if (mGDConfig.isChangeWH()) {
                    if (mGDConfig.getWidth() <= 0 || mGDConfig.getHeight() <= 0) {
                        try {
                            bitmapMin = new GDCompressUtil().SysCompressMin(mGDConfig.getmPath());
//                            bitmapMin= new GDCompressUtil().SysCompressMin(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        garbage.add(bitmapMin);
                        if (null== bitmapMin){
                            InformCallError(0, "Image compression failure!");
                        }else {
                            if (new GDCompressUtil().compressLibJpeg(bitmapMin, mGDConfig.getSavePath())) {
                                InformCallSuccess(mGDConfig.getSavePath());
                            } else {
                                InformCallError(0, "Image compression failure!");
                            }
                        }

                    } else {

                        try {
                            bitmapMin = new GDCompressUtil().SysCompressMySamp(mGDConfig.getmPath(), mGDConfig.getWidth(), mGDConfig.getHeight());
//                            bitmapMin= new GDCompressUtil().SysCompressMin(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        garbage.add(bitmapMin);
                        if (null== bitmapMin){
                            InformCallError(0, "Image compression failure!");
                        }else {
                            if (new GDCompressUtil().compressLibJpeg(bitmapMin, mGDConfig.getSavePath())) {
                                InformCallSuccess(mGDConfig.getSavePath());
                            } else {
                                InformCallError(0, "Image compression failure!");
                            }
                        }

                    }
                } else {
//                    Bitmap bitmap = null;
                    try {
                        bitmapMin = GDBitmapUtil.getBitmap(mGDConfig.getmPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                        bitmapMin = BitmapFactory.decodeFile(mGDConfig.getmPath());
                    }

                    garbage.add(bitmapMin);
                    if (null== bitmapMin){
                        InformCallError(0, "Image compression failure!");
                    }else
                    if (new GDCompressUtil().compressLibJpeg(bitmapMin, mGDConfig.getSavePath())) {
                        InformCallSuccess(mGDConfig.getSavePath());
                    } else {
                        InformCallError(0, "Image compression failure!");
                    }
                }
            }
        });
    }

    private void InformCallSuccess(final String path){
        try {
            GDBitmapUtil.saveBitmapDegree(path);
        }catch (Exception e){e.printStackTrace();}
        try {
            if (null!=garbage && garbage.size()>0){
                for (int i = 0; i < garbage.size(); i++) {
                    Bitmap bitmap = garbage.get(i);
                    if(bitmap != null && !bitmap.isRecycled()){
                        bitmap.recycle();
                        bitmap = null;
                    }
                }
                garbage.clear();
                System.gc();
            }
        }catch (Exception e){e.printStackTrace();}
        try {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null!= mGDCompressImageListener) mGDCompressImageListener.OnSuccess(path);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            if (null!= mGDCompressImageListener) mGDCompressImageListener.OnSuccess(path);
        }
    }
    private void InformCallError(final int code, final String errorMsg){
        try {
            if (null!=garbage && garbage.size()>0){
                for (int i = 0; i < garbage.size(); i++) {
                    Bitmap bitmap = garbage.get(i);
                    if(bitmap != null && !bitmap.isRecycled()){
                        bitmap.recycle();
                        bitmap = null;
                    }
                }
                garbage.clear();
                System.gc();
            }
        }catch (Exception e){e.printStackTrace();}
        try {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null!= mGDCompressImageListener) mGDCompressImageListener.OnError(code, errorMsg);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            if (null!= mGDCompressImageListener) mGDCompressImageListener.OnError(code, errorMsg);
        }
    }



}
