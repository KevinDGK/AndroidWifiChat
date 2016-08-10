package com.dgk.wifichat.module.login;

/**
 * Created by Kevin on 2016/8/9.
 */
public class LoginContract {

    /**
     * View接口：界面显示
     */
    interface IView {

        void showError(String s);

        void launchActivity();

    }

    /**
     * Presenter接口：业务逻辑
     */
    interface IPresenter {

        void login(String id,String name);
    }

}
