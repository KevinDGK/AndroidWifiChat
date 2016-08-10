package com.dgk.wifichat.net;

/**
 * Url管理类
 */
public class Url {

    /** 服务器地址 */
//    public static final String baseurl = "http://sp.wangpos.com/";    //正式环境
    public static String baseurl = "http://123.57.30.156:8080/fork-app-serv/comm";     // 测试环境
//    public static String baseurl = "http://46.onpos.cn/app-serv/comm";     // 易建波-本地

    /** 发送验证码 */
    public static String sendVerifyCode = "ProcessorPublic.sendVerifyCode";

    /** 用户登录 */
    public static String login = "ProcessorUserb.login";

    /** 查询团队列表 */
    public static String getTeamList = "ProcessorTeam.getTeamList";

    /** 用户已加入的团队列表 */
    public static String getTeamListByUser = "ProcessorTeam.getTeamListByUser";

    /** 查询团队应用列表 */
    public static String getTeamAppList = "ProcessorTeam.getTeamAppList";

    /** 加入团队网络 */
    public static String joinNetwork = "ProcessorTeam.joinNetwork";

    /** 离开团队网络 */
    public static String leaveNetwork = "ProcessorTeam.leaveNetwork";

    /** 团队成员列表 */
    public static String getTeamMemberList = "ProcessorTeam.getTeamMemberList";

    public static String userList;


}
