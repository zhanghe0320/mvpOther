package com.mvp.util;

import android.webkit.JavascriptInterface;

/**
 *
 */
public class JsBaseInterface {

    public static final String BASE_JS_NAME = "JsBaseInterface";

    /**
     * 返回消息到web界面
     * @return
     */
    @JavascriptInterface
    public String getMessFromPhne() {

        return "";
    }

}
