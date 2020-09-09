package com.mvp.activity.navigation;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础数据
 */
public class NavigationBarModel implements NavigationBar.INavigationBarModel {
    @Override
    public List getAll() {
        List list = new ArrayList();

        int postion = 1;
        NavigationBarBean navigationBarBean = new NavigationBarBean(postion,"所有");
        list.add(navigationBarBean);
        navigationBarBean = new NavigationBarBean(postion,"新闻");
        list.add(navigationBarBean);
        navigationBarBean = new NavigationBarBean(postion,"网页");
        list.add(navigationBarBean);
        navigationBarBean = new NavigationBarBean(postion,"文字");
        list.add(navigationBarBean);
        navigationBarBean = new NavigationBarBean(postion,"我的");
        list.add(navigationBarBean);

        return list;
    }
}
