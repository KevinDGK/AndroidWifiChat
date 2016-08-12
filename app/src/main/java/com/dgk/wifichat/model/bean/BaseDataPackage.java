package com.dgk.wifichat.model.bean;

import com.dgk.wifichat.app.GlobalConfig;
import com.dgk.wifichat.utils.LogUtil;

/**
 * Created by Kevin on 2016/8/9.
 * 标准数据包头 100byte
 * <p>          个人              群组
 * ID         id(手机号)         id(随机字符串)       18210186283      11位字符串                 20字节    0~19
 * Name         名称              群名称              aaaaaaaaaa      20个英文字符或者6个汉字      20字节    20~39
 * IpAddress    个人ip            群主的ip            172.168.0.1     1个int类型                  4字节     40~43
 * Time         心跳包的时间       心跳包的时间                         毫秒值，long类型            8字节     44~51
 * Extend       扩展字段           扩展字段                                                      48字节    52~99
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

    public void setTime(long time) {
//        LogUtil.i(tag,"Extend 字节数组长度：" + data.length);
//        LogUtil.i(tag, "time:  " + time);
        for (int i=0;i<8;i++) {
            head[i + 44] = (byte)(time>>>(56-(i*8)));
        }
    }

    public void setExtend(String extend) {
        byte[] data = extend.getBytes(GlobalConfig.ENCODING_STYLE);
//        LogUtil.i(tag,"Extend 字节数组长度：" + data.length);
        for (int i = 0; i < Math.min(data.length,48); i++) {
            head[i + 52] = data[i];
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

    public long getTime() {
//        LogUtil.i(tag,"Time 字节数组长度：" + data.length);
        long time = 0;
        long temp;
        for (int i=0;i<8;i++) {
            time <<= 8;
            temp = head[i + 44] & 0xff;
            time |= temp;
        }
        return time;
    }

    public String getExtend() {
        byte[] extend = new byte[48];
        for (int i = 52; i <= 99; i++) {
            extend[i-52] = head[i];
        }
        return new String(extend,GlobalConfig.ENCODING_STYLE).trim();
    }
}
