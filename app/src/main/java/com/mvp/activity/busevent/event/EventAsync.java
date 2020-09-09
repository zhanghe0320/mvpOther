package com.mvp.activity.busevent.event;

/**
 * 事件传递的消息类
 */
public class EventAsync {
    private String messgae;

    public EventAsync(String messgae) {
        this.messgae = messgae;
    }

    public String getMessgae() {
        return messgae;
    }
}
