package com.dgk.wifichat.module.login;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import com.dgk.wifichat.app.GlobalConfig;
import com.dgk.wifichat.app.MyApplication;
import com.dgk.wifichat.base.BasePresenter;
import com.dgk.wifichat.model.ServiceModel;
import com.dgk.wifichat.model.sp.SPConstants;
import com.dgk.wifichat.model.sp.SPUtils;
import com.dgk.wifichat.utils.CommonUtil;
import com.dgk.wifichat.utils.LogUtil;

import java.net.InetAddress;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Kevin on 2016/8/9.
 */
public class LoginPresenter extends BasePresenter<LoginContract.IView> implements LoginContract.IPresenter {

    private static String tag = "【LoginPresenter】";

    @Override
    public void login(String id, String name) {

        String telRegex = "^1[34578][0-9]{9}$";

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name)) {

            IView.showError("手机号和昵称不能为空!");
        } else {

            if (!id.matches(telRegex)) {

                IView.showError("手机号不符合要求!");
            } else {

                SPUtils.putString(MyApplication.getContext(), SPConstants.USER_ID, id);
                SPUtils.putString(MyApplication.getContext(), SPConstants.USER_NAME, name);

                startService(); // 开启服务：心跳、群聊、单聊等

                IView.launchActivity();
            }
        }
    }

    /**
     * 开启服务
     */
    private void startService() {

        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {

                try {

                    // 本机信息
                    InetAddress address = InetAddress.getLocalHost();
                    String hostAddress = address.getHostAddress();
                    String hostName = address.getHostName();
                    String canonicalHostName = address.getCanonicalHostName();

                    Log.i("【本机网络信息】", "本机Ip：" + hostAddress
                            + "\n本机HostName：" + hostName
                            + "\n本机CanonicalHostName：" + canonicalHostName
                            + "\n设备信息：" + GlobalConfig.devInfo);

                    // 局域网信息
                    WifiManager wifiManager = (WifiManager) MyApplication.getContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    String LocalIpAddress = CommonUtil.IntToIp(wifiInfo.getIpAddress());
                    int networkId = wifiInfo.getNetworkId();
                    String ssid = wifiInfo.getSSID();

                    GlobalConfig.localIpAddress = wifiInfo.getIpAddress();

                    Log.i("【本机局域网网络信息】", "本机局域网Ip：" + LocalIpAddress
                            + "\n局域网NetworkId：" + networkId
                            + "\n局域网SSID：" + ssid);

                    subscriber.onNext(null);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.i(tag,"初始化网络信息失败");
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Object o) {

                        // 开启聊天服务
                        ServiceModel.getInstance().onCreateChatService();
                    }
                });
    }
}
