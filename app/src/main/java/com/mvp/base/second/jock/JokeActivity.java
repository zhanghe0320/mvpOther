package com.mvp.base.second.jock;

import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mvp.R;
import com.mvp.base.second.BaseActivity;

import java.util.ArrayList;

/**
 *
 */
public class JokeActivity extends BaseActivity<JokePresenter, JokeModel> implements JokeContract.JokeView {

    public static final String PAGE_NUM = "1";

    public static final String PAGE_SIZE = "20";

    private ListView mListView;

    private ProgressBar mLoadingBar;

    private ArrayList<JokeInfo> mJokeInfoArrayList = new ArrayList<>();

    private JokeAdapter mJokeAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_dialog;
    }

    @Override
    public void initView() {
//        mLoadingBar= (ProgressBar) findViewById(R.id.pressbar);
//        mListView = (ListView) findViewById(R.id.main_page_joke_lv);
//        mJokeAdapter = new JokeAdapter(this, mJokeInfoArrayList);
//        mListView.setAdapter(mJokeAdapter);
//        mPresenter.requestJoke(PAGE_NUM, PAGE_SIZE);
    }

    @Override
    public void setJoke(Joke pJoke) {
//        if (pJoke != null) {
//            Joke.Result result = pJoke.getResult();
//            if (result != null) {
//                ArrayList<JokeInfo> jokeInfoArrayList = result.getJokeInfoArrayList();
//                mJokeInfoArrayList.addAll(jokeInfoArrayList);
//                mJokeAdapter.notifyDataSetChanged();
//            }
//        }
    }

    @Override
    public void showLoading() {
        mLoadingBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mLoadingBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError() {
        TextView errorView = new TextView(this);
        errorView.setTextSize(20);
        errorView.setText("请求失败了");
        mListView.setEmptyView(errorView);
    }
}
