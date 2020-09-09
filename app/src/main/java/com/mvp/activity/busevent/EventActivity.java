package com.mvp.activity.busevent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mvp.R;
import com.mvp.activity.busevent.event.EventAsync;
import com.mvp.activity.busevent.event.EventBackgroud;
import com.mvp.activity.busevent.event.EventMain;
import com.mvp.activity.busevent.event.EventPosting;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 事件消息总线
 */
public class EventActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FirstActivity";

    //@BindView(R.id.btn_open)
    Button mOpenBtn;

    // @BindView(R.id.tv_showinfo)
    TextView mInfoTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // ButterKnife.bind(this);
        mInfoTxt = findViewById(R.id.tv_showinfo);
        mOpenBtn = findViewById(R.id.btn_open);
        mOpenBtn.setOnClickListener(this);
        //注册
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }


    /**
     * 事件响应方法
     * 接受处理事件消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMain(EventMain event) {
        String msg = event.getMessgae();
        Log.i(TAG, "onEventMain: " + event.getMessgae());
        mInfoTxt.setText(msg);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventPosting(EventPosting event) {
        String msg = event.getMessgae();
        Log.i(TAG, "onEventPosting: " + event.getMessgae());
        mInfoTxt.setText(msg);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackgroud(EventBackgroud event) {
        String msg = event.getMessgae();
        Log.i(TAG, "onEventBackgroud: " + event.getMessgae());
        mInfoTxt.setText(msg);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(EventAsync event) {
        String msg = event.getMessgae();
        Log.i(TAG, "onEventAsync: " + event.getMessgae());
        mInfoTxt.setText(msg);
    }


    //接收事件并处理事件消息
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventPosting(EventMain event) {
        String msg = event.getMessgae();
        Log.i(TAG, "onEventPosting: " + event.getMessgae());
        mInfoTxt.setText(msg);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackgroud(EventMain event) {
        String msg = event.getMessgae();
        Log.i(TAG, "onEventBackgroud: " + event.getMessgae());
        mInfoTxt.setText(msg);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(EventMain event) {
        String msg = event.getMessgae();
        Log.i(TAG, "onEventAsync: " + event.getMessgae());
        mInfoTxt.setText(msg);
    }


    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onTestEvent(EventMain event) {
        String msg = event.getMessgae();
        //StudyBean studyBean = event.getStudyBean();
        // Log.i(TAG, "onTestEvent: " + studyBean.toString());
        Log.i(TAG, "onTestEvent: " + event.getMessgae());
        mInfoTxt.setText(msg);
    }


    //绑定点击事件
//    @OnClick(R.id.btn_open)
//    public void openSecondActivity(View view) {
//
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册事件
        EventBus.getDefault().unregister(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: ");
        switch (v.getId()) {
            case R.id.btn_open:
                Log.i(TAG, "onClick: ");
                Intent intent = new Intent(EventActivity.this, Event2Activity.class);
                startActivity(intent);

                break;

            case R.id.tv_showinfo:
                Log.i(TAG, "onClick: ");
//                intent = new Intent(EventActivity.this, Event2Activity.class);
//                startActivity(intent);

                break;
        }
    }
}
