package com.mvp.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import com.bumptech.glide.util.LruCache;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *缓存图片加载工具
 */
public class DiskLruCacheUtils {
    private static DiskLruCacheUtils diskLruCacheUtils;

    private DiskLruCache diskLruCache; //LRU 磁盘缓存
    private LruCache<String, Bitmap> lruCache; //LRU 内存缓存

    private Context context;

    public DiskLruCacheUtils() {

    }

    public static DiskLruCacheUtils getInstance() {
        if (diskLruCacheUtils == null) {
            diskLruCacheUtils = new DiskLruCacheUtils();
        }
        return diskLruCacheUtils;
    }


    public void open(Context context, String disk_cache_subdir, int disk_cache_size) {
        try {
            this.context = context;

            // 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
            // LruCache通过构造函数传入缓存值，以KB为单位。
            int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            // 使用最大可用内存值的1/8作为缓存的大小。
            int cacheSize = maxMemory / 8;
            lruCache = new LruCache<>(cacheSize);

            /**
             * open()方法接受四个参数:
             * 第一个参数: 指定缓存地址
             * 第二个参数: 指定当前引用程序的版本号
             * 第三个参数: 指定同一个key可以对应多少个缓存文件,基本都是传1
             * 第四个参数: 指定最多可以缓存的字节数. 通常是10MB
             */

            diskLruCache = DiskLruCache.open(getCacheDir(disk_cache_subdir), getAppVersion(), 1, disk_cache_size);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取磁盘缓存
     * @param url
     * @return
     */
    public InputStream getDiskCache(String url) {
        String key = hashkeyForDisk(url);
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if (snapshot != null) {
                return snapshot.getInputStream(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 下载图片并缓存到内存和磁盘中
     * @param url
     * @param callBack
     */
    public void putCache(final String url, final CallBack callBack){
        new AsyncTask<String,Void,Bitmap>(){

            @Override
            protected Bitmap doInBackground(String... params) {
                String key = hashkeyForDisk(params[0]);
//                System.out.println("Key = "+key);
                DiskLruCache.Editor editor = null;
                Bitmap bitmap = null;

                URL url = null;
                try {
                    url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(30*1000);
                    conn.setConnectTimeout(30*1000);
                    ByteArrayOutputStream baos = null;

                    if (conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                        baos = new ByteArrayOutputStream();
                        byte[] bytes = new byte[1024];
                        int len = -1;
                        while ((len = bis.read(bytes)) != -1) {
                            baos.write(bytes, 0, len);
                        }

                        bis.close();
                        baos.close();
                        conn.disconnect();
                    }
                    if (baos !=null){
                        bitmap = decodeSampleadBitmapFromStream(baos.toByteArray(),300,300);
//                        bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(),0,baos.toByteArray().length);
                        addBitmapToCache(params[0],bitmap); // 添加到内存缓存
                        editor = diskLruCache.edit(key); // 加入磁盘缓存
//                        System.out.println(url.getFile());
                        //位图压缩后输出(参数1: 压缩格式, 参数2: 质量(100 表示不压缩,30 表示压缩70%),参数3: 输出流)
                        bitmap.compress(Bitmap.CompressFormat.JPEG,30,editor.newOutputStream(0));
                        editor.commit();//提交
                    }


                } catch (Exception e) {
                    try {
                        editor.abort();//放弃写入
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }


                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                callBack.response(bitmap);
            }
        }.execute(url);

    }

    /**
     * 关闭磁盘缓存
     */
    public void close(){
        if (diskLruCache!=null&& !diskLruCache.isClosed()){
            try {
                diskLruCache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 刷新磁盘缓存
     */
    public void flush(){
        if (diskLruCache!=null){
            try {
                diskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 回调接口
     * @param <T>
     */
    public interface  CallBack<T>{
        public void response(T entity);
    }


    /**
     * 位图重新采样
     *
     * @param reqWidth  自定义的宽高
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampleadBitmapFromStream(byte[] bytes, int reqWidth, int reqHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析边界,不加载到内存中
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inSampleSize = calculatInSampleSize(options, reqWidth, reqHeight);//设置采样比为计算出的采样比例
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);//重新解析图片
    }


    //添加缓存的对象
    public  void addBitmapToCache(String url,Bitmap bitmap){
        String key = hashkeyForDisk(url);
        if (getBitmapFromMenCache(key)==null){
            lruCache.put(key,bitmap);
        }
    }

    //从缓存中获取对象
    public  Bitmap getBitmapFromMenCache(String url){
        String key = hashkeyForDisk(url);
        return  lruCache.get(key);
    }


    /**
     * 计算位图的采样比例大小
     *
     * @param options
     * @param reqWidth  需要的宽高
     * @param reqHeight
     * @return
     */
    private static int calculatInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //获取位图的原宽高
        final int w = options.outWidth;
        final int h = options.outHeight;
        int inSampleSize = 1;
        //如果原图的宽高比需要的图片宽高大
        if (w > reqWidth || h > reqHeight) {
            if (w > h) {
                inSampleSize = Math.round((float) h / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) w / (float) reqWidth);
            }
        }
        return inSampleSize;
    }


    /**
     * MD5加密计算
     *
     * @param key
     * @return
     */
    private String hashkeyForDisk(String key) {
        String cachekey;

        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cachekey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cachekey = String.valueOf(key.hashCode());
        }

        return cachekey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xff & bytes[i]);
            if (hex.length() == 1) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }


    /**
     * 获取缓存的地址
     *
     * @param name
     * @return
     */
    private File getCacheDir(String name) {
        String cachePath = Environment.getExternalStorageState()
                == Environment.MEDIA_MOUNTED || !Environment.isExternalStorageRemovable() ?
                context.getExternalCacheDir().getPath() : context.getCacheDir().getPath();

        return new File(cachePath + File.separator + name);
    }

    /**
     * 获取App的版本号
     *
     * @return
     */
    private int getAppVersion() {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }





    /**
     * 加载图片的方法
     * @param url
     * @param imageView
     */
    public void loadBitmap(String url, final ImageView imageView) {
        if(imageView == null || url == null){
            return;
        }
        if (imageView.getTag().equals(url)) {
            //从内存缓存中取图片
            Bitmap bitmap = diskLruCacheUtils.getBitmapFromMenCache(url);
            if (bitmap == null) {
                //如果内存中为空 从磁盘缓存中取
                InputStream in = diskLruCacheUtils.getDiskCache(url);

                if (in == null) {
                    //如果缓存中都为空,就通过网络加载,并加入缓存
                    diskLruCacheUtils.putCache(url, new DiskLruCacheUtils.CallBack<Bitmap>() {
                        @Override
                        public void response(Bitmap entity) {
                            //                            System.out.println("网络中下载...");
                            imageView.setImageBitmap(entity);
                        }
                    });
                } else {
                    System.out.println("磁盘中取出...");
                    bitmap = BitmapFactory.decodeStream(in);
                    diskLruCacheUtils.addBitmapToCache(url, bitmap);
                    imageView.setImageBitmap(bitmap);
                }
            } else {
                //                System.out.println("内存中取出...");
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
