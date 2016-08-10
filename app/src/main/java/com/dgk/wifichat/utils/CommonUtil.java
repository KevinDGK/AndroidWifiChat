package com.dgk.wifichat.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

import com.dgk.wifichat.app.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Kevin on 2016/7/27.
 * 公共的工具类
 */
public class CommonUtil {

    /**
     * 获取UUID
     * - 可以作为数据库表的主键
     *
     * @return
     */
    public static String getUUID() {
        LogUtil.i("【CommonUtil】", "getUUID");
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String temp = str.replace("-", "");
        temp = temp.substring(0, 26);
        return temp;
    }

    /**
     * 获取当前版本的名称
     */
    public static String getVersionName() {
        LogUtil.i("【CommonUtil】", "getVersionName");
        try {
            PackageManager packageManager = MyApplication.getContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(MyApplication.getContext().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Toast
     */
    public static void toast(String content) {
        Toast.makeText(MyApplication.getContext(), content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 某些危险性比较高的权限，在android6.0以后需要动态申请，该方法用于判断是否有该权限，
     * 暂时默认低于6.0时，写在Manifest.xml中的就会生效。
     */
    public static boolean isAllowPermission(String permission) {
        LogUtil.i("【CommonUtil】", "isAllowPermission");
        if (Build.VERSION.SDK_INT >= 23) {
            LogUtil.i("【isAllowPermission】", "Build.VERSION.SDK_INT：" + Build.VERSION.SDK_INT);
            return MyApplication.getContext().checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED;
        }
        LogUtil.i("【isAllowPermission】", "Build.VERSION.SDK_INT：" + Build.VERSION.SDK_INT);
        return true;
    }

    /**
     * 将Ip地址改成字符串表示格式
     */
    public static String IntToIp(int i) {
        LogUtil.i("【CommonUtil】", "IntToIp");
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    /**
     * 判断设备是否有网络
     * -如果为True则表示当前Android手机已经联网，可能是WiFi或手机网络等等
     * -手机、POS均可以
     */
    public static boolean isNetworkConnected(Context context) {
        LogUtil.i("【CommonUtil】", "isNetworkConnected：判断当前的所有网络连接是否可用~");

        try {
            //1. 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivityManager == null) {
                return false;
            } else {
                //2. 获取网络链接对象的信息
                NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

                if (networkInfo != null && networkInfo.length > 0) {
                    for (int i = 0; i < networkInfo.length; i++) {
                        LogUtil.i("【CommonUtil】", "State:" + networkInfo[i].getState()
                                + "\nType:" + networkInfo[i].getTypeName());
                        //3. 判断该网络状态是否为连接状态
                        if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) return true;
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.i("【CommonUtil】", "isNetworkConnected：请检查是否添加网络相关权限!");
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 获取当前时间
     * 时间格式：2016-06-01 12:00:00
     */
    public static String getCurrentTime() {
        long mTime = System.currentTimeMillis();
        Date date = new Date(mTime);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        return time.substring(0, time.length());
    }

}
