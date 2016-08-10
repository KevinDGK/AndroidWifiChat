package com.dgk.wifichat.model.bean;

import com.dgk.wifichat.app.GlobalConfig;
import com.dgk.wifichat.utils.LogUtil;

/**
 * Created by Kevin on 2016/8/9.
 * 标准数据包头 100byte
 * <p>
 * ID           18210186283     11位字符串                  20字节    0~19
 * Name         aaaaaaaaaa      20个英文字符或者6个汉字      20字节    20~39
 * IpAddress    172.168.0.1     1个int类型                  4字节     40~43
 * Extend       扩展字段                                    56字节    44~99
 */
public class BaseDataPackage {

    private static String tag = "【BaseDataPackage】";

    public byte[] head = new byte[GlobalConfig.baseHeadLength];

    public BaseDataPackage() {
    }


    public BaseDataPackage(byte[] head) {
        this.head = head;
    }

    public void setID(String id) {
        byte[] data = id.getBytes(GlobalConfig.ENCODING_STYLE);
//        LogUtil.i(tag,"ID 字节数组长度：" + data.length);
        for (int i = 0; i < Math.min(data.length,20); i++) {
            head[i] = data[i];
        }
    }

    public void setName(String name) {
        byte[] data = name.getBytes(GlobalConfig.ENCODING_STYLE);
//        LogUtil.i(tag,"Name 字节数组长度：" + data.length);
        for (int i = 0; i < Math.min(data.length,20); i++) {
            head[i + 20] = data[i];
        }
    }

    public void setIpAddress(int ipAddress) {
//        LogUtil.i(tag,"ipAddress 字节数组长度：" + 4);
        head[40] = (byte) ((ipAddress & 0xFF000000) >> 24);
        head[41] = (byte) ((ipAddress & 0xFF0000) >> 16);
        head[42] = (byte) ((ipAddress & 0xFF00) >> 8);
        head[43] = (byte) ((ipAddress & 0xFF));
    }

    public void setExtend(byte[] data) {
//        LogUtil.i(tag,"Extend 字节数组长度：" + data.length);
        for (int i = 0; i < Math.min(data.length,56); i++) {
            head[i + 44] = data[i];
        }
    }

    public byte[] getHead() {
        return head;
    }

    public String getID() {
        byte[] id = new byte[20];
        for (int i = 0; i < 20; i++) {
            id[i] = head[i];
        }
        return new String(id,GlobalConfig.ENCODING_STYLE).trim();
    }

    public String getName() {
        byte[] name = new byte[20];
        for (int i = 20; i <= 39; i++) {
            name[i-20] = head[i];
        }
        return new String(name,GlobalConfig.ENCODING_STYLE).trim();
    }

    public int getIpAddress() {
        int ipAddress = 0;
        for (int i = 40; i <= 43; i++) {
            ipAddress = (ipAddress<<8) + head[i];
        }
        return ipAddress;
    }

    public String getExtend() {
        byte[] extend = new byte[57];
        for (int i = 44; i <= 99; i++) {
            extend[i-44] = head[i];
        }
        return new String(extend,GlobalConfig.ENCODING_STYLE).trim();
    }
}
