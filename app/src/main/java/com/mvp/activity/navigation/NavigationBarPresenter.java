package com.mvp.activity.navigation;

import java.util.List;

/**
 * 底部导航
 */
public class NavigationBarPresenter  implements NavigationBar.INavigationBarPresenter {
    private NavigationBar.INavigationBarRelativeLayout mNavigationBarRelativeLayout;
    private NavigationBar.INavigationBarModel iNavigationBarModel;

    public NavigationBarPresenter(NavigationBar.INavigationBarRelativeLayout iNavigationBarRelativeLayout) {
        super();
        this.mNavigationBarRelativeLayout = iNavigationBarRelativeLayout;
        iNavigationBarModel = new NavigationBarModel();
    }

    @Override
    public void setText(List list) {
        if (list == null || list.size() <= 0) {
            list = iNavigationBarModel.getAll();
        }
//
//        for (Iterator<String> iterator = list.getmNavigationBarBean().iterator(); iterator.hasNext(); ) {
//            String str = (String) iterator.next();
//            // System.out.println(str);
//        }

        mNavigationBarRelativeLayout.setText(list);
    }

}
