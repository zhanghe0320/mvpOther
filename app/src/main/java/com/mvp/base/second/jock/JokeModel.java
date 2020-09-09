package com.mvp.base.second.jock;

import com.mvp.base.second.MVPListener;

/**
 *
 */
public class JokeModel implements JokeContract.JokeModel {

    public static final String REQUEST_SERVER_URL = "http://api.jisuapi.com/xiaohua/text?";

    public static final String APPKEY = "&appkey=9814b57c706d0a23";

    //http://api.jisuapi.com/xiaohua/text?pagenum=10&pagesize=3&appkey=9814b57c706d0a23
    @Override
    public void requestJoke(String pNum, String pSize, final MVPListener pMVPListener) {

//        VolleyRequest.newInstance().newGsonRequest(REQUEST_SERVER_URL + "pagenum=" + pNum + "&" + "pagesize=" + pSize + "&sort=addtime" + APPKEY,
//
//                Joke.class, new Response.Listener<Joke>() {
//                    @Override
//                    public void onResponse(Joke pJoke) {
//                        if (pJoke != null) {
//                            pMVPListener.onSuccess(pJoke);
//                        } else {
//                            pMVPListener.onError();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        pMVPListener.onError();
//                    }
//                });
    }
}
