package com.dgk.wifichat.app;

import java.nio.charset.Charset;

/**
 * Created by Kevin on 2016/8/3.
 * 配置文件
 *
 *  - 通讯网络配置
 *  - 对讲配置
 *
 *
 */
public class GlobalConfig {

    /**
     * 多播组地址
     *   多播组地址即群聊使用的组播地址，由于多播Ip地址的范围为：224.0.2.0~238.255.255.255，所以在此处取一个比较
     *   合适作为本应用默认的群聊使用的多播地址。注意，暂时所有的群聊信息都是使用此多播地址，然后根据GroupId进行区分
     *   是哪个群聊组的信息。
     */
    public static String multicastIpAddress = "233.233.233.233";

    /**
     * 多播组目标主机端口
     *   即群聊使用的端口，端口的使用暂时如下：
     *   - 设置相关消息(心跳包等)  端口 20001
     *   - 群聊 端口 20002
     *   - 单聊 端口 20003
     */
    public static int multicastHeartPort = 20000;

    /** 群聊目标主机端口 */
    public static int multicastMultiChatPort = 20001;

    /** 单聊目标主机端口 */
    public static int multicastSingleChatPort = 20002;

    /** 本机ip地址：路由器分配的ip地址 */
    public static int localIpAddress;

    /** 标准数据包头Head大小 */
    public static int baseHeadLength = 100;   // byte

    /** 标准数据包头的扩展字段 */
    public static String extend = "I'm a KaWaYi Cat!";

    /** 心跳数据包Body大小 */
    public static int heartBodyLength = 1;

    /** 心跳数据包总大小 */
    public static int heartDataLength = baseHeadLength + heartBodyLength;

    /** 心跳数据包发送的间隔 */
    public static int HEART_INTERVAL = 1000;    // ms

    /** 设备信息 */
    public static String devInfo = android.os.Build.MODEL + " Android " + android.os.Build.VERSION.RELEASE;

    /**
     * 数据包的编码方式
     *   英文字母和英文字符是1个字节，中文的是3个字节
     */
    public static Charset ENCODING_STYLE = Charset.forName("UTF-8");

    public static byte PERSON_CURRENT_STATE;

    /** Action:个人上线 */
    public static byte ACTION_PERSON_ONLINE = 0;
    /** Action:个人下线 */
    public static byte ACTION_PERSON_OFFLINE = 1;
    /** Action:个人退出 */
    public static byte ACTION_PERSON_EXIT = 2;
    /** Action:群创建 */
    public static byte ACTION_GROUP_ONLINE = 3;
    /** Action:群删除 */
    public static byte ACTION_GROUP_OFFLINE = 4;

    private static GlobalConfig config;

    public static GlobalConfig getInstance() {

        if (config == null) {
            synchronized (GlobalConfig.class) {
                if (config == null) {
                    config = new GlobalConfig();
                }
            }

        }
        return config;
    }
}
