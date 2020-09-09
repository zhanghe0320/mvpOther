package com.mvp.commonutils.WebTool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 WebTool.java: 网页信息获取，可在主线程中调用
 * 1、byte[] GetBytes(final String url)
 * 2、String GetString(String dataUrl)
 * 3、JSONObject GetJSONObject(String jsonUrl)
 * 4、BitmapGetBitmap(String imgUrl)
 * 5、Drawable GetDrawable(String imgUrl)
 * 6、GetRedirectUrl(final Context context, final String url, final CallBackUrl call) —— 获取重定向url最终的地址信息，支持所有网址
 * 7、GetUrlHref(String url) —— 获取url加载后页面中的href信息
 * 8、OpenRedirectUrl(final Context context, String Url)	 —— 直接打开重定向后的地址
 */
public class WebTool {

    // 缓存Drawable图像
    private static HashMap<String, Drawable> DrawableDic = new HashMap<String, Drawable>();

    /** 从网络上下载图片,转为Drawable */
    public static Drawable GetDrawable(String imgUrl)
    {
        Drawable drawable = null;

        if (DrawableDic.containsKey(imgUrl))
            drawable = DrawableDic.get(imgUrl);	// 从缓存读取图像
        else
        {
            Bitmap bmp = GetBitmap(imgUrl);						// 从服务器端下载图像
            if (bmp != null) drawable = Bitmap2Drawable(bmp);			// 转化为Drawable
            if (drawable != null) DrawableDic.put(imgUrl, drawable);		// 记录图像
        }

        return drawable;
    }

    /** 从网络上下载图片资源 */
    public static Bitmap GetBitmap(String imgUrl)
    {
        Bitmap bmp = null;
        try
        {
            byte[] data = GetBytes(imgUrl);								// 下载数据
            bmp = BitmapFactory.decodeByteArray(data, 0, data.length);	// 载入Bitmap
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bmp;
    }

    /** Bitmap转化为Drawable */
    public static Drawable Bitmap2Drawable(Bitmap bitmap)
    {
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    /** Drawable转化为Bitmap */
    public static Bitmap Drawable2Bitmap(Drawable drawable)
    {
        BitmapDrawable bitDrawable = (BitmapDrawable) drawable;
        return bitDrawable.getBitmap();
    }

    /** 获取url最终跳转的herf信息 */
    public static String GetUrlHref(String url)
    {
        // if (url.contains("t.seoniao.com")) // http://t.seoniao.com/to.php?t=ONOERIC8Z1
        {
            String data = GetString(url);	// 获取网址信息
            // <script>if(/baiduboxapp/i.test(navigator.userAgent)){window.location.href="bdbox://utils?action=sendIntent&minver=7.4&params=%7B%22intent%22%3A%22weixin://dl/business/?ticket=tc27384c6af2fb995e18de2cbc27a0da3%23Intent%3Bend%22%7D";}else{window.location.href="weixin://dl/business/?ticket=tc27384c6af2fb995e18de2cbc27a0da3";}
            // </script>

            ArrayList<String> herfList = new ArrayList<String>();

            int start = 0;
            String hrefKey = "window.location.href=\"";
            while (data.contains(hrefKey))
            {
                int index = data.indexOf(hrefKey, start);
                if (index != -1)
                {
                    int end = data.indexOf("\"", index + hrefKey.length());
                    if (end != -1)
                    {
                        start = end + 1;

                        String herf = data.substring(index + hrefKey.length(), end).trim();
                        if (!herfList.contains(herf))
                        {
                            herfList.add(herf);
                            if (herf.startsWith("weixin://"))
                            {
                                url = herf;
                                break;
                            }
                        }
                    }
                }
            }
        }

        Log.d("WebTool", "GetWeixinUrl -> " + url);
        return url;
    }

    /** 获取指定网址的数据 */
    public static String GetString(String dataUrl)
    {
        String Str = "";
        try
        {
            byte[] data = GetBytes(dataUrl);	// 下载数据
            Str = new String(data);				// 转化为字符串
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return Str;
    }

    /** 获取指定网址的数据为JSON */
    public static JSONObject GetJSONObject(String jsonUrl)
    {
        String webData = WebTool.GetString(jsonUrl);
        JSONObject webJson = null;
        try
        {
            webJson = new JSONObject(webData);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return webJson;
    }

    // ------------------------
    // 直接打开链接url的重定向地址
    // ------------------------

    /** 打开链接url的重定向地址 */
    public static void OpenRedirectUrl(final Context context, String Url)
    {
        OpenRedirectUrl(context, Url, true);
    }

    /** 打开链接Url */
    private static void OpenRedirectUrl(final Context context, String Url, boolean reditect)
    {
        try
        {
            if (reditect)
            {
                WebTool.GetRedirectUrl(context, Url, new CallBackUrl()
                {
                    @Override
                    public void F(String fUrl)
                    {
                        OpenRedirectUrl(context, fUrl, false);
                    }
                });

                return;
            }

            Uri uri = Uri.parse(Url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        }
        catch (Exception ex)
        {
            Log.d("WebTool", "链接地址配置有误：" + Url);
        }
    }

    // ------------------------
    // 获取重定向后的url
    // ------------------------

    public interface CallBackUrl
    {
        // 回调处理逻辑
        void F(String url);
    }

    static final class UrlData
    {
        public List<String> StartList = new ArrayList<String>();
        public List<String> FinishList = new ArrayList<String>();
        public String CallUrl = "";
        public long currrentTimeMillion = System.currentTimeMillis();
        public boolean isClicked = false;

        public String url = "";

        public UrlData(String url)
        {
            this.url = url;
        }
    }

    // static List<String> StartList = new ArrayList<String>();
    // static List<String> FinishList = new ArrayList<String>();
    // static String CallUrl = "";
    // static long currrentTimeMillion = System.currentTimeMillis();
    // static boolean isClicked = false;

    static HashMap<String, UrlData> urlDic = new HashMap<String, UrlData>();

    /** 获取指定网址的数据 */
    public static void GetRedirectUrl(final Context context, final String url, final CallBackUrl call)
    {
        String LowerUrl = url.trim().toLowerCase();
        if (!LowerUrl.startsWith("http://") && !LowerUrl.startsWith("https://")) 	// 非网页地址，则直接返回
        {
            call.F(url);
            return;
        }

        // 在非主线程中执行网络请求，获取数据
        ThreadTool.RunInMainThread(new ThreadTool.ThreadPram()
        {
            @Override
            public void Function()
            {
                if (!urlDic.containsKey(url))
                {
                    urlDic.put(url, new UrlData(url));
                }
                final UrlData data = urlDic.get(url);

                if (data.isClicked && System.currentTimeMillis() - data.currrentTimeMillion > 3 * 1000) data.isClicked = false;
                if (!data.isClicked)
                {
                    data.StartList.clear();
                    data.FinishList.clear();
                }

                final WebView webView = new WebView(context);
                webView.loadUrl(url);
                webView.getSettings().setJavaScriptEnabled(true);

                webView.setWebViewClient(new WebViewClient()
                {
                    // 页面加载开始
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon)
                    {
                        Log.d("WebTool", "Start-> " + url);

                        if (!data.StartList.contains(url))
                        {
                            data.StartList.add(url);		// 记录重定向的url信息
                            waitCallBack(url);	//
                        }

                        // super.onPageStarted(view, url, favicon);
                    }

                    // 页面加载完成
                    @Override
                    public void onPageFinished(WebView view, String url)
                    {
                        Log.d("WebTool", "Finish -> " + url);
                        if (data.StartList.contains(url))
                        {
                            if (!data.FinishList.contains(url))
                            {
                                data.FinishList.add(url);
                                waitCallBack(url);
                            }
                        }
                    }

                    public void waitCallBack(String paramUrl)
                    {
                        data.CallUrl = paramUrl;
                        data.currrentTimeMillion = System.currentTimeMillis();	// 重置当前时间值，延时等待后续重定向

                        ThreadTool.RunInMainThread(new ThreadTool.ThreadPram()
                        {
                            @Override
                            public void Function()
                            {
                                if (!data.isClicked)
                                {
                                    if (call != null && System.currentTimeMillis() >= data.currrentTimeMillion + 700)
                                    {
                                        data.isClicked = true;

                                        call.F(data.CallUrl);
                                        Log.d("WebTool", "CallUrl -> " + data.CallUrl);
                                    }
                                }
                            }
                        }, 700);
                    }
                });
            }
        });
    }

    // ------------------------
    // 网络数据载入
    // ------------------------

    static HashMap<Long, byte[]> GetBytesDic = new HashMap<Long, byte[]>();

    /** 获取指定网址的数据，函数可在任意线程中执行，包括主线程 */
    public static byte[] GetBytes(final String url)
    {
        final long KEY = System.currentTimeMillis();
        if (!GetBytesDic.containsKey(KEY)) GetBytesDic.put(KEY, null);

        // 在非主线程中执行网络请求，获取数据
        ThreadTool.RunInCachedThread(new ThreadTool.ThreadPram()
        {
            @Override
            public void Function()
            {
                byte[] data = GetBytes_process(url);
                GetBytesDic.put(KEY, data);
            }
        });

        // 等待异步线程中的网络请求逻辑执行完成
        while (GetBytesDic.get(KEY) == null) 	// 未获取到数据则
        {
            if (System.currentTimeMillis() > KEY + 1000 * 3) break;	// 超出3秒则终止
            Sleep(50); // 延时等待异步线程逻辑执行完成
        }

        byte[] data = GetBytesDic.get(KEY);
        GetBytesDic.remove(KEY);

        return data;
    }

    /** 获取指定网址的数据 */
    public static byte[] GetBytes_process(String url)
    {
        byte[] data = new byte[0];
        try
        {
            URL webUrl = new URL(url);
            URLConnection con = webUrl.openConnection();	// 打开连接
            InputStream in = con.getInputStream();			// 获取InputStream

            data = InputStreamToByte(in);					// 读取输入流数据
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return data;
    }

    /** InputStream -> Byte */
    public static final byte[] InputStreamToByte(InputStream in)
    {
        byte[] bytes = {};

        try
        {
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int count = 0;
            while ((count = in.read(data, 0, 1024)) > 0)
            {
                byteOutStream.write(data, 0, count);
            }

            bytes = byteOutStream.toByteArray();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return bytes;
    }

    /** 当前线程延时毫秒 */
    private static void Sleep(long timeMillion)
    {
        try
        {
            Thread.sleep(timeMillion);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

}
