package com.mvp.base.db;

/**
 * 相关泛型术语
 * 　　1）参数化的类型：List<String>
 * 　　2）实际类型参数：String
 * 　　3）泛型：List<E>
 * 　　4）形式类型参数：E
 * 　　5）无限制通配符类型：List<?>
 * 　　6）原生态类型：List
 * 　　7）递归类型限制：<T extends Comparable<T>>
 * 　　8）有限制的通配符类型：List<? extends Number>
 * 　　9）泛型方法：static <E> List<E> union()
 * 　　10）类型令牌：String.class
 * 常用的形式类型参数
 * 　　1）T 代表一般的任何类。
 * 　　2）E 代表 Element 的意思，或者 Exception 异常的意思。
 * 　　3）K 代表 Key 的意思。
 * 　　4）V 代表 Value 的意思，通常与 K 一起配合使用。
 * 　　5）S 代表 Subtype 的意思
 * 泛型单例
 */

public abstract class Singleton<T> {

    private volatile T mInstance;

    protected abstract T newInstance();

    public final T getInstance() {
        if (mInstance == null) {
            synchronized (this) {
                if (mInstance == null) {
                    mInstance = newInstance();
                }
            }
        }
        return mInstance;
    }

    public final void releaseInstance() {
        if (mInstance == null) {
            return;
        }
        synchronized (this) {
            mInstance = null;
        }
    }
}
