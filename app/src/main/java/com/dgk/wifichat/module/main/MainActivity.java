package com.dgk.wifichat.module.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dgk.wifichat.R;
import com.dgk.wifichat.app.GlobalConfig;
import com.dgk.wifichat.base.BaseActivity;
import com.dgk.wifichat.model.bean.HeartBean;
import com.dgk.wifichat.model.event.ChatServiceSettingEvent;
import com.dgk.wifichat.model.service.ServiceModel;
import com.dgk.wifichat.utils.CommonUtil;
import com.dgk.wifichat.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainContract.IView, MainPresenter> implements MainContract.IView {

    @BindView(R.id.tv_online)
    TextView tvOnline;
    @BindView(R.id.tv_offline)
    TextView tvOffline;
    @BindView(R.id.tv_exit)
    TextView tvExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter initPresenter() {
        return new MainPresenter();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.tv_online, R.id.tv_offline, R.id.tv_exit})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_online:    // 上线
                if (GlobalConfig.PERSON_CURRENT_STATE == GlobalConfig.ACTION_PERSON_ONLINE) {
                    CommonUtil.toast("大人，您已经在线上了~");
                }
                if (GlobalConfig.PERSON_CURRENT_STATE == GlobalConfig.ACTION_PERSON_OFFLINE) {
                    ServiceModel.getInstance().onStartChatService();
                }
                if (GlobalConfig.PERSON_CURRENT_STATE == GlobalConfig.ACTION_PERSON_EXIT) {
                    ServiceModel.getInstance().onCreateChatService();
                }
                break;

            case R.id.tv_offline:   // 下线
                if (GlobalConfig.PERSON_CURRENT_STATE == GlobalConfig.ACTION_PERSON_ONLINE) {
                    ServiceModel.getInstance().onStopHeartService();
                } else {
                    CommonUtil.toast("大人，您已经从线上滚下来啦!");
                }
                break;

            case R.id.tv_exit:      // 退出
                ServiceModel.getInstance().onDestroyHeartService();
                finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void personEvent(HeartBean heartBean) {

        if (heartBean.getAction() == GlobalConfig.ACTION_PERSON_ONLINE) {
            // 个人上线
            CommonUtil.toast(heartBean.getName() + "大人来啦！");
        } else {
            // 个人下线
            CommonUtil.toast(heartBean.getName() + "大人滚了！");
        }

    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

}
