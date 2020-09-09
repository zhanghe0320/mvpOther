package com.mvp.activity.main;

import com.mvp.base.mvp.BaseModel;
import com.mvp.base.mvp.BaseView;
import com.mvp.bean.MainBean;
import com.mvp.bean.MainBean2;
import com.mvp.bean.TextBean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public interface RxView extends BaseView {
    /**
     * 主页加载成功
     * @param o
     */
    void onMainSuccess(BaseModel<List<MainBean>> o);

    /**
     * 文字内容加载成功过
     * @param o
     */
    void onTextSuccess(BaseModel<TextBean> o);

    /**
     * 图片加载成功
     * @param o
     */
    void onUpLoadImgSuccess(BaseModel<Object> o);

    /**
     * 表格加载成功
     * @param o
     */
    void onTableListSuccess(BaseModel<Object> o);

    /**
     *
     * @param o
     */
    void onRestrictionsSuccess(BaseModel<Object> o);

    /**
     * 测试
     * @param o
     */
    void onCheShiSuccess(BaseModel<Object> o);

    /**
     * 其他
     * @param mainBean2
     */
    void onMyOtherSuccess(MainBean2 mainBean2);


    void initBtn(ArrayList list);

    void getDataApi();

    void getDataApi2();

}
