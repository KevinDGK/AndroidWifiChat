package com.dgk.wifichat.model.bean;

import com.dgk.wifichat.app.GlobalConfig;
import com.dgk.wifichat.model.bean.BaseDataPackage;

/**
 * Created by Kevin on 2016/8/9.
 * 心跳数据包
 *   标准数据包头head + 心跳数据包的body部分

 * 继承了标准数据包头：
 * 标准数据包头 100byte
 * <p>          个人              群组
 * ID         id(手机号)         id(随机字符串)       18210186283     11位字符串                  20字节    0~19
 * Name         名称              群名称              aaaaaaaaaa      20个英文字符或者6个汉字      20字节    20~39
 * IpAddress    个人ip            群主的ip            172.168.0.1     1个int类型                  4字节     40~43
 * Time         心跳包的时间       心跳包的时间                         毫秒值，long类型            8字节     44~51
 * Extend       扩展字段           扩展字段                                                      48字节    52~99
 *
 * 自己的数据包的body：
 * Action       上下线:0/1         上下线：2/3                                                   1字节     0
 *
 */
public class HeartDataPackage extends BaseDataPackage {

    private byte[] body = new byte[GlobalConfig.heartBodyLength];

    public HeartDataPackage() {
        super();
    }

    public HeartDataPackage(byte[] head ,byte[] body) {
        super(head);
        this.body = body;
    }
    
    public void setBody(byte[] data) {
        body = new byte[GlobalConfig.heartBodyLength];
        for (int i = 0; i < Math.min(data.length,GlobalConfig.heartBodyLength); i++) {
            body[i] = data[i];
        }
    }

    public void setAction(byte action) {
        body[0] = action;
    }

    public byte getAction() {
        return body[0];
    }

    public byte[] getBody() {
        return body;
    }

    public byte[] getAllData() {
        byte[] data = new byte[GlobalConfig.heartDataLength];
        for (int i = 0; i < GlobalConfig.heartDataLength; i++) {
            if (i<GlobalConfig.baseHeadLength) {
                data[i] = head[i];
            } else {
                data[i] = body[i - GlobalConfig.baseHeadLength];
            }
        }
        return data;
    }
}
