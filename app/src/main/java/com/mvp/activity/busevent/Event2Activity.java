package com.mvp.activity.busevent;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


import com.mvp.R;
import com.mvp.activity.busevent.event.EventBackgroud;
import com.mvp.activity.busevent.event.EventAsync;
import com.mvp.activity.busevent.event.EventMain;
import com.mvp.activity.busevent.event.EventPosting;

import org.greenrobot.eventbus.EventBus;

/**
 * 事件消息总线
 */
public class Event2Activity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event2);
        // ButterKnife.bind(this);
        findViewById(R.id.btn_post).setOnClickListener(this);
        findViewById(R.id.btn_post2).setOnClickListener(this);
        findViewById(R.id.btn_post3).setOnClickListener(this);
        findViewById(R.id.btn_post4).setOnClickListener(this);

    }
//
//    @OnClick(R.id.btn_post)
//    public void onPostA() {
//    }
//
//    @OnClick(R.id.btn_post2)
//    public void onPostB() {
//
//    }
//
//    @OnClick(R.id.btn_post3)
//    public void onPostC() {
//
//    }
//
//    @OnClick(R.id.btn_post4)
//    public void onPostD() {
//
//
//    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        //事件触发 发送消息
        switch (v.getId()) {
            case R.id.btn_post:
//                StudyBean studyBean = new StudyBean("EventMain","baidu");
                EventBus.getDefault().post(new EventMain("EventMainstudyBean"));

                break;
            case R.id.btn_post2:
                EventBus.getDefault().post(new EventPosting("EventPosting"));

                break;
            case R.id.btn_post3:
                EventBus.getDefault().post(new EventBackgroud("EventBackgroud"));
                break;
            case R.id.btn_post4:
                EventBus.getDefault().post(new EventAsync("EventAsync"));
                break;

        }
    }
}
