package com.dgk.wifichat.module.login;

/**
 * Created by Kevin on 2016/8/9.
 */
public class SplashContract {

    /**
     * View接口：界面显示
     */
    interface IView {

        void setVersionName(String versionName);

        void lauchActivity();
    }

    /**
     * Presenter接口：业务逻辑
     */
    interface IPresenter {

        void getVersionName();
    }

}
