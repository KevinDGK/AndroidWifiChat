package com.dgk.wifichat.model;

import android.content.Context;
import android.content.Intent;

import com.dgk.wifichat.app.GlobalConfig;
import com.dgk.wifichat.app.MyApplication;
import com.dgk.wifichat.model.event.ChatServiceSettingEvent;
import com.dgk.wifichat.model.service.ChatService;
import com.dgk.wifichat.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;

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

    private static String tag = "【ServiceModel】";

    private static ServiceModel serviceModel;
    private static Context ctx;
    private Intent intent_ChatService;

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
     * 创建服务
     *  - 创建发送和接收心跳的线程
     *  - 创建发送和接收群聊消息的线程
     *  - 创建发送和接收单聊消息的线程
     */
    public void onCreateChatService() {

        LogUtil.i(tag,"onCreateChatService");
        // 创建聊天服务
        intent_ChatService = new Intent(ctx, ChatService.class);
        ctx.startService(intent_ChatService);
    }

    /**
     * 开启服务工作：上线
     *  开启服务中的线程，即让线程全部working，开始工作。
     */
    public void onStartChatService() {

        LogUtil.i(tag,"onStartChatService");
        GlobalConfig.PERSON_CURRENT_STATE = GlobalConfig.ACTION_PERSON_ONLINE;
        EventBus.getDefault().post(new ChatServiceSettingEvent(GlobalConfig.ACTION_PERSON_ONLINE));
    }

    /**
     * 停止服务工作：下线
     *  - 停止服务中的线程，即让线程全部停止working，不工作，但是线程此时仍然正在运行执行run方法
     */
    public void onStopHeartService() {

        LogUtil.i(tag,"onStopHeartService");
        GlobalConfig.PERSON_CURRENT_STATE = GlobalConfig.ACTION_PERSON_OFFLINE;
        EventBus.getDefault().post(new ChatServiceSettingEvent(GlobalConfig.ACTION_PERSON_OFFLINE));
    }

    /**
     * 销毁服务：程序退出
     *  - 销毁服务中的线程，即先让线程全部结束run方法，然后终止线程，然后终止服务
     */
    public void onDestroyHeartService() {

        LogUtil.i(tag,"onDestroyHeartService");
        GlobalConfig.PERSON_CURRENT_STATE = GlobalConfig.ACTION_PERSON_OFFLINE;
        EventBus.getDefault().post(new ChatServiceSettingEvent(GlobalConfig.ACTION_PERSON_EXIT));
//        ctx.stopService(intent_ChatService);
    }

}
