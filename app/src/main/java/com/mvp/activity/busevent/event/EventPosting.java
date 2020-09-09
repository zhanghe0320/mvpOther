package com.mvp.activity.busevent.event;

/**
 * 事件消息
 */
public class EventPosting {
    private String messgae;

    public EventPosting(String messgae) {
        this.messgae = messgae;
    }

    public String getMessgae() {
        return messgae;
    }
}
