package com.mvp.util.diff;

/**
 * 会话列表item数据集
 */

public class ChatItemBean {
    private String avatarPath;
    private String nickname;
    private String time;
    private String msg;
    private String userId;

    public ChatItemBean() {

    }

    public ChatItemBean(String msg, String avatarPath, String nickname, String time) {
        this.msg = msg;
        this.avatarPath = avatarPath;
        this.nickname = nickname;
        this.time = time;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}