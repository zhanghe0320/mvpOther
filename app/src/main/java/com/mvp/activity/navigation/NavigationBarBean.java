package com.mvp.activity.navigation;

/**
 * 导航
 */
public class NavigationBarBean {
    private String mNavigationBarBean;
    private int mPosition;
    
    public NavigationBarBean(int position, String list) {
        this.mNavigationBarBean = list;
        this.mPosition = position;
    }

    public String getmNavigationBarBean() {
        return mNavigationBarBean;
    }

    public void setmNavigationBarBean(String mNavigationBarBean) {
        this.mNavigationBarBean = mNavigationBarBean;
    }

    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }
}
