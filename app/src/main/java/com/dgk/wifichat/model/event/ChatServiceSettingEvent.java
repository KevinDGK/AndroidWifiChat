package com.dgk.wifichat.model.event;

/**
 * Created by Kevin on 2016/8/10.
 * 聊天服务设置事件
 */
public class ChatServiceSettingEvent {

    private int flag;

    public ChatServiceSettingEvent(int flag) {
        this.flag = flag;
    }

    public int isFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
