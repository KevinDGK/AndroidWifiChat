package com.dgk.wifichat.module.login;

import android.os.SystemClock;

import com.dgk.wifichat.app.MyApplication;
import com.dgk.wifichat.base.BasePresenter;
import com.dgk.wifichat.utils.CommonUtil;
import com.dgk.wifichat.utils.LogUtil;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Kevin on 2016/8/9.
 */
public class SplashPresenter extends BasePresenter<SplashContract.IView> implements SplashContract.IPresenter {

    @Override
    protected void onCreate(SplashContract.IView mView) {
        super.onCreate(mView);

        Observable.create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {

                        LogUtil.i("【SplashPresenter】","call");
                        SystemClock.sleep(2000);

                        subscriber.onNext(null);
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i("【SplashPresenter】","onCompleted");

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {
                        LogUtil.i("【SplashPresenter】","onNext");

                        if (CommonUtil.isNetworkConnected(MyApplication.getContext())) {
                            CommonUtil.toast("大人，初始化完成！");

                        } else {
                            CommonUtil.toast("大人，请检查手机是否联网！");
                        }

                        IView.lauchActivity();
                    }
                });
    }

    @Override
    public void getVersionName() {
        String versionName = CommonUtil.getVersionName();
        IView.setVersionName(versionName);
    }
}
