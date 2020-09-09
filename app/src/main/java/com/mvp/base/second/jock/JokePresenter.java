package com.mvp.base.second.jock;

import com.mvp.base.second.MVPListener;

/**
 *
 */
public class JokePresenter extends JokeContract.JokePresenter {

    @Override
    public void requestJoke(String pNum, String pSize) {

        final JokeContract.JokeView mView = getView();

        if (mView == null) {
            return;
        }

        mView.showLoading();

        mModel.requestJoke(pNum, pSize, new MVPListener<Joke>() {

            @Override
            public void onSuccess(Joke pJoke) {
                mView.hideLoading();
                mView.setJoke(pJoke);
            }

            @Override
            public void onError() {
                mView.hideLoading();
                mView.showError();
            }
        });
    }
}
