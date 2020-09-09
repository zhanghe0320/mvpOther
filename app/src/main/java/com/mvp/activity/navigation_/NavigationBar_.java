package com.mvp.activity.navigation_;

import java.util.List;

/**
 * 导航接口
 */
public interface NavigationBar_ {
    /**
     */
    interface INavigationBarModel {
        List getAll();
    }
    
    /**
     */
    interface INavigationBarPresenter {
        void setText(List list);
    }
    
    /**
     * 导航栏
     */
    interface INavigationBarRelativeLayout {
        void setText(List list);
    }

}
