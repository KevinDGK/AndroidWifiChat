package com.dgk.wifichat.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.zhy.autolayout.AutoLayoutActivity;

/**
 *
 * View层主要负责UI交互，不处理业务逻辑：
 *  1.将响应事件传递给对应的Presenter处理；
 *  2.修改UI界面；
 *
 * View层的基类
 * @param <V>  实现的接口，用于和对应的Presenter交互
 * @param <T>  对应的Presenter
 */
public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AutoLayoutActivity {

    protected T presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //禁止屏幕旋转 - 保持竖屏
        setContentView(getContentViewResId());

        presenter = initPresenter();    // View持有Presenter的引用 - 后期可以使用Dagger实现解耦
        presenter.onCreate((V) this);   // Presenter持有View的引用

        initView();
        setListener();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();  // 释放资源
        presenter = null;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            final View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /** 判定是否需要隐藏软键盘 */
    protected boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /** 隐藏软件盘 */
    protected void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /** 显示软件盘 */
    protected void showSoftInput(View token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.showSoftInput(token,0);
        }
    }

    /** 获取布局资源Id */
    protected abstract int getContentViewResId();

    /** 初始化Presenter */
    protected abstract T initPresenter();

    /** 初始化界面控件 */
    protected abstract void initView();

    /** 初始化监听器 */
    protected abstract void setListener();

    /** 初始化数据 */
    protected abstract void initData();

}
