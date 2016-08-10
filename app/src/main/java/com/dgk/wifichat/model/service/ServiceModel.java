package com.dgk.wifichat.model.service;

import android.content.Context;
import android.content.Intent;

import com.dgk.wifichat.app.GlobalConfig;
import com.dgk.wifichat.app.MyApplication;
import com.dgk.wifichat.model.service.heart.HeartService;

/**
 * Created by Kevin on 2016/8/9.
 * 服务管理模型
 *  - 用于管理各个服务：
 *      心跳服务
 *      群聊服务
 *      单聊服务
 *      。。。
 */
public class ServiceModel {

    private static ServiceModel serviceModel;
    private static Context ctx;
    private Intent intent_heartService;

    private ServiceModel() {}

    public static ServiceModel getInstance() {
        if (serviceModel == null) {
            synchronized (ServiceModel.class) {
                if (serviceModel == null) {
                    serviceModel = new ServiceModel();
                    ctx = MyApplication.getContext();
                }
            }
        }
        return serviceModel;
    }

    /**
     * 开启心跳服务
     */
    public void startHeartService() {

        //设置本机上线
        GlobalConfig.PERSON_CURRENT_STATE = GlobalConfig.ACTION_PERSON_ONLINE;

        intent_heartService = new Intent(ctx, HeartService.class);
        ctx.startService(intent_heartService);
    }

    /**
     * 停止心跳服务
     */
    public void stopHeartService() {

        ctx.stopService(intent_heartService);
    }

}
