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
import com.dgk.wifichat.model.event.HeartWorkingEvent;
import com.dgk.wifichat.model.service.ServiceModel;
import com.dgk.wifichat.utils.CommonUtil;

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

    @OnClick({R.id.tv_online,R.id.tv_offline})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_online:    // 上线
                GlobalConfig.PERSON_CURRENT_STATE = GlobalConfig.ACTION_PERSON_ONLINE;
                EventBus.getDefault().post(new HeartWorkingEvent(true));
//                ServiceModel.getInstance().startHeartService();
                break;

            case R.id.tv_offline:   // 下线
                GlobalConfig.PERSON_CURRENT_STATE = GlobalConfig.ACTION_PERSON_OFFLINE;
                EventBus.getDefault().post(new HeartWorkingEvent(false));
//                ServiceModel.getInstance().startHeartService();
                break;
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
