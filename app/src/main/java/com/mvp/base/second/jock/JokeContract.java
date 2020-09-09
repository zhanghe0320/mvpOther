package com.mvp.base.second.jock;

import com.mvp.base.second.BaseModel;
import com.mvp.base.second.BasePresenter;
import com.mvp.base.second.BaseView;
import com.mvp.base.second.MVPListener;
import com.mvp.base.second.jock.Joke;

/**
 *
 */
public interface JokeContract {

    interface JokeView extends BaseView {
        void setJoke(Joke pJoke);
    }

    interface JokeModel extends BaseModel {
        void requestJoke(String pNum, String pSize, MVPListener pMVPListener);
    }

    abstract class JokePresenter extends BasePresenter<JokeModel, JokeView> {

        public abstract  void requestJoke(String pNum, String pSize);
    }
}