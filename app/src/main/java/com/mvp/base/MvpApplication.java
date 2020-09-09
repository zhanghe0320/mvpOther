package com.mvp.base;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDexApplication;


import com.blankj.utilcode.util.LogUtils;
import com.de.rocket.Rocket;
import com.hjq.toast.ToastUtils;
import com.mvp.base.db.ProductDBHelper;
import com.mvp.base.db.Singleton;
import com.mvp.commonutils.NetWorkUtils;
import com.mvp.commonutils.netstatus.NetWorkMonitorManager;
import com.mvp.oserver.NetConnectionObserver;
import com.mvp.oserver.NetConnectionSubject;
import com.mvp.util.ActivityHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

/**
 * 本类给出一个Application的Demo，当然开发者也可以直接extends RoApplication，
 * 最终只需要调用必须的Rocket.init(application)
 */
public class MvpApplication extends MultiDexApplication /*implements NetConnectionSubject*/ {

    private int currentNetType = -1;

    private List<NetConnectionObserver> observers = new ArrayList<>();


    private static Context mContext;

    public static MvpApplication getInstance() {
        // 双重判空机制 保证线程安全
        return sInstance.getInstance();
    }

    private static Singleton<MvpApplication> sInstance = new Singleton<MvpApplication>() {
        @Override
        protected MvpApplication newInstance() {
            return new MvpApplication();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        //架构必须要初始化操作
        Rocket.init(this);
        //RoLogUtil.stopALLLog();
        //ToastUtils.init(this);
        ToastUtils.init(this);
        mContext = getApplicationContext();//全局context
        //重写生命周期
        registerActivityLifecycleCallbacks(new ActivityHelper());

        //对单位的自定义配置, 请在 App 启动时完成
        configUnits();
        LogUtils.i(ActivityApplication.getInstance().getApplicationByReflect());
        //currentNetType = NetWorkUtils.getConnectionType(this);
        //注册网络状态监听
        NetWorkMonitorManager.getInstance().init(this);

    }


    public static Context getmContext() {
        return mContext;
    }

    @Override
    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.registerActivityLifecycleCallbacks(callback);
    }

    /**
     * 注意!!! 布局时的实时预览在开发阶段是一个很重要的环节, 很多情况下 Android Studio 提供的默认预览设备并不能完全展示我们的设计图
     * 所以我们就需要自己创建模拟设备, 以下链接是给大家的福利, 按照链接中的操作可以让预览效果和设计图完全一致!
     *
     * @see <a href="https://github.com/JessYanCoding/AndroidAutoSize/blob/master/README-zh.md#preview">dp、pt、in、mm 这四种单位的模拟设备创建方法</a>
     * <p>
     * v0.9.0 以后, AndroidAutoSize 强势升级, 将这个方案做到极致, 现在支持5种单位 (dp、sp、pt、in、mm)
     * {@link UnitsManager} 可以让使用者随意配置自己想使用的单位类型
     * 其中 dp、sp 这两个是比较常见的单位, 作为 AndroidAutoSize 的主单位, 默认被 AndroidAutoSize 支持
     * pt、in、mm 这三个是比较少见的单位, 只可以选择其中的一个, 作为 AndroidAutoSize 的副单位, 与 dp、sp 一起被 AndroidAutoSize 支持
     * 副单位是用于规避修改 {@link DisplayMetrics#density} 所造成的对于其他使用 dp 布局的系统控件或三方库控件的不良影响
     * 您选择什么单位, 就在 layout 文件中用什么单位布局
     * <p>
     * 两个主单位和一个副单位, 可以随时使用 {@link UnitsManager} 的方法关闭和重新开启对它们的支持
     * 如果您想完全规避修改 {@link DisplayMetrics#density} 所造成的对于其他使用 dp 布局的系统控件或三方库控件的不良影响
     * 那请调用 {@link UnitsManager#setSupportDP}、{@link UnitsManager#setSupportSP} 都设置为 {@code false}
     * 停止对两个主单位的支持 (如果开启 sp, 对其他三方库控件影响不大, 也可以不关闭对 sp 的支持)
     * 并调用 {@link UnitsManager#setSupportSubunits} 从三个冷门单位中选择一个作为副单位
     * 三个单位的效果都是一样的, 按自己的喜好选择, 比如我就喜欢 mm, 翻译为中文是妹妹的意思
     * 然后在 layout 文件中只使用这个副单位进行布局, 这样就可以完全规避修改 {@link DisplayMetrics#density} 所造成的不良影响
     * 因为 dp、sp 这两个单位在其他系统控件或三方库控件中都非常常见, 但三个冷门单位却非常少见
     */
    private void configUnits() {
        //AndroidAutoSize 默认开启对 dp 的支持, 调用 UnitsManager.setSupportDP(false); 可以关闭对 dp 的支持
        //主单位 dp 和 副单位可以同时开启的原因是, 对于旧项目中已经使用了 dp 进行布局的页面的兼容
        //让开发者的旧项目可以渐进式的从 dp 切换到副单位, 即新页面用副单位进行布局, 然后抽时间逐渐的将旧页面的布局单位从 dp 改为副单位
        //最后将 dp 全部改为副单位后, 再使用 UnitsManager.setSupportDP(false); 将 dp 的支持关闭, 彻底隔离修改 density 所造成的不良影响
        //如果项目完全使用副单位, 则可以直接以像素为单位填写 AndroidManifest 中需要填写的设计图尺寸, 不需再把像素转化为 dp
        AutoSizeConfig.getInstance().getUnitsManager()
                .setSupportDP(false)

                //当使用者想将旧项目从主单位过渡到副单位, 或从副单位过渡到主单位时
                //因为在使用主单位时, 建议在 AndroidManifest 中填写设计图的 dp 尺寸, 比如 360 * 640
                //而副单位有一个特性是可以直接在 AndroidManifest 中填写设计图的 px 尺寸, 比如 1080 * 1920
                //但在 AndroidManifest 中却只能填写一套设计图尺寸, 并且已经填写了主单位的设计图尺寸
                //所以当项目中同时存在副单位和主单位, 并且副单位的设计图尺寸与主单位的设计图尺寸不同时, 可以通过 UnitsManager#setDesignSize() 方法配置
                //如果副单位的设计图尺寸与主单位的设计图尺寸相同, 则不需要调用 UnitsManager#setDesignSize(), 框架会自动使用 AndroidManifest 中填写的设计图尺寸
//                .setDesignSize(2160, 3840)

                //AndroidAutoSize 默认开启对 sp 的支持, 调用 UnitsManager.setSupportSP(false); 可以关闭对 sp 的支持
                //如果关闭对 sp 的支持, 在布局时就应该使用副单位填写字体的尺寸
                //如果开启 sp, 对其他三方库控件影响不大, 也可以不关闭对 sp 的支持, 这里我就继续开启 sp, 请自行斟酌自己的项目是否需要关闭对 sp 的支持
//                .setSupportSP(false)

                //AndroidAutoSize 默认不支持副单位, 调用 UnitsManager#setSupportSubunits() 可选择一个自己心仪的副单位, 并开启对副单位的支持
                //只能在 pt、in、mm 这三个冷门单位中选择一个作为副单位, 三个单位的适配效果其实都是一样的, 您觉的哪个单位看起顺眼就用哪个
                //您选择什么单位就在 layout 文件中用什么单位进行布局, 我选择用 mm 为单位进行布局, 因为 mm 翻译为中文是妹妹的意思
                //如果大家生活中没有妹妹, 那我们就让项目中最不缺的就是妹妹!
                .setSupportSubunits(Subunits.MM);
    }



    /**
     * 当前网络状态
     *
     * @return
     */
    public int getCurrentNetType() {
        return currentNetType;
    }

    /**
     * 档期网络链接状态
     *
     * @return
     */
    public boolean isNetConnection() {
        return currentNetType == NetWorkUtils.NET_NO_CONNECTION ? false : true;
    }

    /**
     * 注册观察者
     *
     * @param observer
     */
//    @Override
//    public void addNetObserver(NetConnectionObserver observer) {
//        if (!observers.contains(observer)) {
//            observers.add(observer);
//        }
//    }

    /**
     * 移除观察者
     *
     * @param observer
     */
//    @Override
//    public void removeNetObserver(NetConnectionObserver observer) {
//        if (observers != null && observers.contains(observer)) {
//            observers.remove(observer);
//        }
//    }

    /**
     * 状态更新通知
     *
     * @param type
     */
//    @Override
//    public void notifyNetObserver(int type) {
//
//        /**
//         * 避免多次发送相同的网络状态
//         */
//        if (currentNetType == type) {
//            return;
//        } else {
//            currentNetType = type;
//            if (observers != null && observers.size() > 0) {
//                for (NetConnectionObserver observer : observers) {
//                    observer.updateNetStatus(type);
//                }
//            }
//        }
//    }

}
