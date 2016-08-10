package com.dgk.wifichat.model.event;

/**
 * Created by Kevin on 2016/8/10.
 */
public class HeartWorkingEvent {

    private boolean flag;

    public HeartWorkingEvent(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
