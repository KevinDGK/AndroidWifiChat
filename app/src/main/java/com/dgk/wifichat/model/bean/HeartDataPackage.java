package com.dgk.wifichat.model.bean;

import com.dgk.wifichat.app.GlobalConfig;
import com.dgk.wifichat.model.bean.BaseDataPackage;

/**
 * Created by Kevin on 2016/8/9.
 * 心跳数据包
 *   标准数据包头head + 心跳数据包的body部分
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
