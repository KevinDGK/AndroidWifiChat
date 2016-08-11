package com.dgk.wifichat.model.bean;

/**
 * Created by Kevin on 2016/8/10.
 * 心跳的Bean
 *  用于保存在线的个人/群组的心跳包中的数据。
 */
public class HeartBean {

    private String id;
    private String name;
    private int ipAddress;
    private long time;
    private String extend;
    private byte Action;

    public HeartBean(String id, String name, int ipAddress, long time, String extend, byte action) {
        this.id = id;
        this.name = name;
        this.ipAddress = ipAddress;
        this.time = time;
        this.extend = extend;
        Action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(int ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public byte getAction() {
        return Action;
    }

    public void setAction(byte action) {
        Action = action;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "HeartBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ipAddress=" + ipAddress +
                ", time=" + time +
                ", extend='" + extend + '\'' +
                ", Action=" + Action +
                '}';
    }
}
