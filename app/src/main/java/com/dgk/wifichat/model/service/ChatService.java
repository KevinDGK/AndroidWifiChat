package com.dgk.wifichat.model.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;

import com.dgk.wifichat.app.GlobalConfig;
import com.dgk.wifichat.app.MyApplication;
import com.dgk.wifichat.model.bean.HeartBean;
import com.dgk.wifichat.model.bean.HeartDataPackage;
import com.dgk.wifichat.model.event.ChatServiceSettingEvent;
import com.dgk.wifichat.model.sp.SPConstants;
import com.dgk.wifichat.model.sp.SPUtils;
import com.dgk.wifichat.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Kevin on 2016/8/9.
 * 聊天服务
 * - 管理心跳发送和接收的线程
 * - 管理单聊发动核接收的线程
 * - 管理
 */
public class ChatService extends Service {

    private static final int HEART_ACTION = 1000;
    private static String tag = "【ChatService】";

    private MulticastSocket socket;               // 组播Socket，socket貌似是全双工的，可以同时发送和接收，暂时没错
    private InetAddress broadcastAddress;         // 组播地址

    private SendHeartThread sendHeartThread;
    private ReceiveHeartThread receiveHeartThread;

    private ArrayList<HeartBean> heartPersonList = new ArrayList<>();

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case HEART_ACTION:                      // 检查发送的心跳包对应的id(个人/团队)是否在线
                    check((HeartBean) msg.obj);
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        LogUtil.i(tag, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.i(tag, "onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        LogUtil.i(tag, "onStart()");

        try {
            LogUtil.i(tag, "初始化心跳Socket，并加入多播组：" + GlobalConfig.multicastIpAddress);

            //1. 创建多播Socket对象
            socket = new MulticastSocket(GlobalConfig.multicastHeartPort);

            //2. 将该Socket加入多播组
            broadcastAddress = InetAddress.getByName(GlobalConfig.multicastIpAddress);
            socket.joinGroup(broadcastAddress);

            //3. 设置本多播Socket发送的数据报是否会被回送到自身:false表示会接收
            socket.setLoopbackMode(false);

            startAllThread();

        } catch (IOException e) {
            LogUtil.i(tag, "初始化心跳Socket或者开启线程失败！");
            e.printStackTrace();
        }

    }

    /**
     * 创建并开启所有的线程
     */
    private void startAllThread() {

        //1. 创建线程
        sendHeartThread = new SendHeartThread();
        receiveHeartThread = new ReceiveHeartThread();

        //2. 开始执行线程，即执行run方法
        sendHeartThread.start();
        receiveHeartThread.start();

        //3. 设置线程开始工作，即执行run方法里面的while方法里面的if的逻辑代码
        setHeartThreadWorking(true,true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.i(tag, "onBind()");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        LogUtil.i(tag, "onDestroy()");
        try {

            heartPersonList.clear();

            if (sendHeartThread != null && sendHeartThread.isAlive()) {
                sendHeartThread.setRunning(false);
                sendHeartThread.interrupt();
            }
            if (receiveHeartThread != null && receiveHeartThread.isAlive()) {
                receiveHeartThread.setRunning(false);
                receiveHeartThread.interrupt();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置服务中的线程是否工作
     * 不是创建和终止线程，仅仅是开始和停止线程中的run方法里面的while循环中的if逻辑代码的执行
     */
    private void setHeartThreadWorking(boolean send, boolean receive) {

        LogUtil.i(tag, "设置服务中的线程是否working：" + send + " , " + receive);
        if (sendHeartThread != null && sendHeartThread.isAlive()) {
            sendHeartThread.setWorking(send);
        }
        if (receiveHeartThread != null && receiveHeartThread.isAlive()) {
            receiveHeartThread.setWorking(receive);
        }

        heartPersonList.clear();
    }

    /**
     * 检查当前心跳包是否在集合中，是否需要修改在线状态
     *
     * @param heartBean
     */
    private void check(HeartBean heartBean) {

        switch (heartBean.getAction()) {

            case 0:
            case 1:
                checkPerson(heartBean);
                break;
            case 2:
            case 3:
                checkGroup(heartBean);
                break;
        }
    }

    /**
     * 检查个人是否在线
     * 上线的情况：
     * 列表中没有；
     * 或者是当前在线人员列表为空；
     * 下线的情况：
     * 心跳包中的状态和列表中的状态不相符
     *
     * @param heartBean
     */
    private void checkPerson(HeartBean heartBean) {

        if (GlobalConfig.PERSON_CURRENT_STATE != GlobalConfig.ACTION_PERSON_ONLINE) return;

        LogUtil.i(tag, "接收到个人心跳包：" + heartBean.toString());
        LogUtil.i(tag, "目前在线列表中的人员数量：" + heartPersonList.size());

        for (int i = 0; i < heartPersonList.size(); i++) {
            if (heartBean.getId().equals(heartPersonList.get(i).getId())) {
                LogUtil.i(tag, "已经在线人员列表中:" + i + " , 内容:" + heartPersonList.get(i).toString());
                // 如果收到的心跳包的时间比保存的时间要新，并且当前的心跳动作为下线
                if ((heartBean.getTime()>heartPersonList.get(i).getTime()) && (heartBean.getAction()==GlobalConfig.ACTION_PERSON_OFFLINE)) {
                    LogUtil.i(tag, "个人下线:" + heartBean.getName());
                    heartPersonList.remove(i);
                    EventBus.getDefault().post(heartBean);
                }
                return;
            }
        }

        LogUtil.i(tag, "不在在线人员列表中");
        if (heartBean.getAction() == GlobalConfig.ACTION_PERSON_ONLINE) {
            LogUtil.i(tag, "添加到在线人员列表中");
            heartPersonList.add(heartBean);
            EventBus.getDefault().post(heartBean);
        }

    }

    /**
     * 检查群是否在线
     *
     * @param heartBean
     */
    private void checkGroup(HeartBean heartBean) {


    }

    /**
     * 发送心跳数据包的线程
     */
    private class SendHeartThread extends Thread {

        private String tag = "【SendHeartThread】";
        private boolean isRunning = true;   // 线程是否执行run方法里面的逻辑循环，初始化执行run方法
        private boolean isWorking = false;  // 线程是否可以工作(发送/接收数据)
        private byte[] body = new byte[GlobalConfig.heartBodyLength];

        public SendHeartThread() {

        }

        @Override
        public void run() {
            super.run();

            LogUtil.i(tag, "线程开始运行");

            while (isRunning) {

                if (isWorking) {

                    try {

                        //1. 创建发送的消息内容
                        body[0] = GlobalConfig.PERSON_CURRENT_STATE;

                        //2. 创建心跳数据包
                        HeartDataPackage heart = new HeartDataPackage();
                        heart.setID(SPUtils.getString(MyApplication.getContext(), SPConstants.USER_ID, "250"));
                        heart.setName(SPUtils.getString(MyApplication.getContext(), SPConstants.USER_NAME, "Guest"));
                        heart.setIpAddress(GlobalConfig.localIpAddress);
                        heart.setTime(System.currentTimeMillis());
                        heart.setExtend(GlobalConfig.extend);
                        heart.setBody(body);

                        //3. 将心跳数据包封装成标准数据包创建DatagramPacket
                        DatagramPacket sendDatagramPacket = new DatagramPacket(heart.getAllData(), GlobalConfig.heartDataLength,
                                broadcastAddress, GlobalConfig.multicastHeartPort);

                        //4. 发送标准数据包
                        socket.send(sendDatagramPacket);

                        //5. 心跳间隔
                        SystemClock.sleep(GlobalConfig.HEART_INTERVAL);

                    } catch (Exception e) {
                        LogUtil.i(tag, "发送数据出错");
                        e.printStackTrace();
                    }

                }

            }

        }

        /**
         * 停止run()方法
         * 当想要停止线程的时候，为了避免死锁或者其他异常情况的产生，需要先停止run方法，
         * 本方法通过改变标记来停止run方法的运行，然后就可以正常的停止线程。
         *
         * @param flag false 停止run方法
         */
        public void setRunning(Boolean flag) {
            isRunning = flag;
        }

        /**
         * 设置run方法里面的while循环里面的逻辑是否执行
         * 如果需要线程发送和接收数据的时候，将该标记置为true；
         * 如果不需要，置为false。
         */
        public void setWorking(Boolean flag) {
            isWorking = flag;
        }
    }

    /**
     * 接收心跳数据包的线程
     */
    private class ReceiveHeartThread extends Thread {

        private String tag = "【ReceiveHeartThread】";
        private boolean isRunning = true;   // 线程是否执行run方法里面的逻辑循环，初始化执行run方法
        private boolean isWorking = false;  // 线程是否可以工作(发送/接收数据)

        private byte[] heart = new byte[GlobalConfig.heartDataLength];

        public ReceiveHeartThread() {

        }

        @Override
        public void run() {
            super.run();

            LogUtil.i(tag, "线程开始运行");

            while (isRunning) {

                if (isWorking) {

                    try {

                        //1. 创建接收数据的标准数据包DatagramPacket
                        DatagramPacket receiveDatagramPacket = new DatagramPacket(heart, GlobalConfig.heartDataLength);

                        //2. 使用标准的数据包接收数据
                        socket.receive(receiveDatagramPacket);

                        //3. 解析自己的数据包数据
                        byte[] head = new byte[GlobalConfig.baseHeadLength];
                        byte[] body = new byte[GlobalConfig.heartBodyLength];
                        for (int i = 0; i < GlobalConfig.baseHeadLength; i++) {
                            head[i] = heart[i];
                        }
                        for (int i = GlobalConfig.baseHeadLength; i < GlobalConfig.heartDataLength; i++) {
                            body[i - GlobalConfig.baseHeadLength] = heart[i];
                        }
                        HeartDataPackage data = new HeartDataPackage(head,body);

                        String id = data.getID();
                        String name = data.getName();
                        int ipAddress = data.getIpAddress();
                        long time = data.getTime();
                        String extend = data.getExtend();
                        byte action = data.getAction();

                        //4. 将接收到的心跳包使用handler发送给本线程的MessageQueue，进行排队，转换成单线程处理
                        Message msg = Message.obtain();
                        msg.what = HEART_ACTION;
                        msg.obj = new HeartBean(id, name, ipAddress, time, extend, action);
                        handler.sendMessage(msg);

                    } catch (Exception e) {
                        LogUtil.i(tag, "接收数据出错");
                        e.printStackTrace();
                    }

                }

            }

        }

        /**
         * 停止run()方法
         * 当想要停止线程的时候，为了避免死锁或者其他异常情况的产生，需要先停止run方法，
         * 本方法通过改变标记来停止run方法的运行，然后就可以正常的停止线程。
         *
         * @param flag false 停止run方法
         */
        public void setRunning(Boolean flag) {
            isRunning = flag;
        }

        /**
         * 设置run方法里面的while循环里面的逻辑是否执行
         * 如果需要线程发送和接收数据的时候，将该标记置为true；
         * 如果不需要，置为false。
         */
        public void setWorking(Boolean flag) {
            isWorking = flag;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void personEvent(final ChatServiceSettingEvent event) {

        final int flag = event.isFlag();

        if (flag == GlobalConfig.ACTION_PERSON_ONLINE) {
            setHeartThreadWorking(true,true);
        } else if (flag == GlobalConfig.ACTION_PERSON_OFFLINE) {
            setHeartThreadWorking(true,false);
            handlePersonEvent(flag);
        } else if (flag == GlobalConfig.ACTION_PERSON_EXIT) {
            ChatService.this.onDestroy();
        }

    }

    private void handlePersonEvent(final int flag) {

        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {

                // 当接收到的是本机需要下线的消息的时候，将在线标志改为下线，然后发送下线的心跳数据包
                SystemClock.sleep((long) (GlobalConfig.HEART_INTERVAL * 2.1));

                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
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
                        setHeartThreadWorking(false,false);
                    }
                });
    }

}