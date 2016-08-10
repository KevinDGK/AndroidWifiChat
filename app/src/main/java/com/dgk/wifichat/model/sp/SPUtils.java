package com.dgk.wifichat.model.sp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Kevin on 2016/7/27.
 * SharedPreferences 工具类
 *  -默认SP名称：SPConstants.SP_NAME
 *  -支持类型：int、Long、String、Boolean
 *  -默认调用apply()，防止遗忘
 */
public class SPUtils {

    private static SharedPreferences sp;
    
    public static SharedPreferences getInstance(Context ctx){
        if (sp==null) {
            sp = ctx.getSharedPreferences(SPConstants.SP_NAME, Context.MODE_PRIVATE);
        }
        return sp;
    }

    /** 保存boolean信息 */
    public static void putBoolean(Context ctx,String key,boolean value){
        if (sp==null) {
            sp = ctx.getSharedPreferences(SPConstants.SP_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).apply();
    }

    /** 获取boolean信息 */
    public static boolean getBoolean(Context ctx,String key,boolean defValue){
        if (sp==null) {
            sp = ctx.getSharedPreferences(SPConstants.SP_NAME, Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }

    /** 保存String信息 */
    public static void putString(Context ctx,String key,String value){
        if (sp==null) {
            sp = ctx.getSharedPreferences(SPConstants.SP_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).apply();
    }

    /** 获取String信息 */
    public static String getString(Context ctx,String key,String defValue){
        if (sp==null) {
            sp = ctx.getSharedPreferences(SPConstants.SP_NAME, Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }

    /** 保存int信息 */
    public static void putInt(Context ctx,String key,int value){
        if (sp==null) {
            sp = ctx.getSharedPreferences(SPConstants.SP_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key, value).apply();
    }

    /** 获取int信息 */
    public static int getInt(Context ctx,String key,int defValue){
        if (sp==null) {
            sp = ctx.getSharedPreferences(SPConstants.SP_NAME, Context.MODE_PRIVATE);
        }
        return sp.getInt(key, defValue);
    }

    /** 保存long信息 */
    public static void putLong(Context ctx,String key,long value){
        if (sp==null) {
            sp = ctx.getSharedPreferences(SPConstants.SP_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putLong(key, value).apply();
    }

    /** 获取long信息 */
    public static long getLong(Context ctx,String key,long defValue){
        if (sp==null) {
            sp = ctx.getSharedPreferences(SPConstants.SP_NAME, Context.MODE_PRIVATE);
        }
        return sp.getLong(key, defValue);
    }
}
