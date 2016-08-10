package com.dgk.wifichat.base;

/**
 *
 * Presenter层主要负责：
 *
 *   1.与View层进行交互：
 *       ①对View层传递过来的各种响应事件进行处理：
 *         -业务逻辑
 *         -非业务逻辑处理：计算、临时存储页面相关数据；
 *       ②调用View层方法去修改界面；
 *   2.与Model层进行交互：
 *      ①调用Model层方法去获取数据；
 *
 *  Presenter创建和Activity主要的生命周期同名的方法，然后在Activity对应的生命周期方法里调用。
 *  这样的话，就可以将view层对应的业务逻辑移到了presenter层，实现了界面和业务逻辑的分离。
 *  注：基类里面抽出来的是公共的方法，如果需要重写，直接在子类中覆盖即可。
 *
 *  Presenter基类
 * @param <V>   对应View实现的接口
 *
 */
public abstract class BasePresenter<V> {

    protected V IView;

    protected void onCreate(V mView) {
        this.IView = mView;
    }

    protected void onStart() {
    }

    protected void onResume() {
    }

    protected void onPause() {
    }


    protected void onStop() {
    }

    protected void onDestroy() {
        IView = null;  // 释放资源
    }
}
