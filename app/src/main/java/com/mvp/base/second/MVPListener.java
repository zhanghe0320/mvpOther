package com.mvp.base.second;

/**
 *
 */
public interface MVPListener<E> {

    /**
     * 成功的时候回调

     */
    void  onSuccess(E pJoke);

    /**
     * 失败的时候回调
     */
    void  onError();
}
