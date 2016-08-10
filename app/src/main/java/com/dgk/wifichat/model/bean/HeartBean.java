package com.dgk.wifichat.model.bean;

/**
 * Created by Kevin on 2016/8/10.
 */
public class HeartBean {

    private String id;
    private String name;
    private int ipAddress;
    private String extend;
    private byte Action;

    public HeartBean(String id, String name, int ipAddress, String extend, byte action) {
        this.id = id;
        this.name = name;
        this.ipAddress = ipAddress;
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

    @Override
    public String toString() {
        return "HeartBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ipAddress=" + ipAddress +
                ", extend='" + extend + '\'' +
                ", Action=" + Action +
                '}';
    }
}
