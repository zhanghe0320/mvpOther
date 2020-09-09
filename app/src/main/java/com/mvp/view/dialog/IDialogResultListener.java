package com.mvp.view.dialog;

/**
 *对话框回调
 */
public  interface IDialogResultListener<T> {
    void onDataResult(T result);
}