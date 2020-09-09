package com.mvp.oserver;

/**
 *网络状态改变时通知观察者
 */
public interface NetConnectionObserver {
    /**
     * 通知观察者更改状态
     *
     * @param type
     */
    public void updateNetStatus(int type);
}
