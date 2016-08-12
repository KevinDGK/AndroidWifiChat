package com.dgk.wifichat.module.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dgk.wifichat.R;
import com.dgk.wifichat.app.GlobalConfig;
import com.dgk.wifichat.base.BaseActivity;
import com.dgk.wifichat.model.bean.HeartBean;
import com.dgk.wifichat.model.event.EndLoadingEvent;
import com.dgk.wifichat.model.event.ExitEvent;
import com.dgk.wifichat.model.ServiceModel;
import com.dgk.wifichat.module.main.recycleview.DividerItemDecoration;
import com.dgk.wifichat.module.main.recycleview.MainPersonAdapter;
import com.dgk.wifichat.module.main.recycleview.RecycleItemTouchListener;
import com.dgk.wifichat.utils.CommonUtil;
import com.dgk.wifichat.utils.LogUtil;
import com.dgk.wifichat.view.LoadingRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainContract.IView, MainPresenter> implements MainContract.IView {

    @BindView(R.id.rl_loading)
    LoadingRelativeLayout rlLoading;
    @BindView(R.id.tv_online)
    TextView tvOnline;
    @BindView(R.id.tv_offline)
    TextView tvOffline;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.rv_group)
    RecyclerView rvGroup;
    @BindView(R.id.rv_person)
    RecyclerView rvPerson;

    private static String tag  = "【MainActivity】";
    private MainPersonAdapter personAdapter;
    private ArrayList<HeartBean> personList = new ArrayList<>();
    private ArrayList<HeartBean> groupList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
        ButterKnife.bind(this);

        // 初始化群的RecycleView
        // TODO: 2016/8/12 开始模拟群数据，暂时有一个永远存在的群
//        groupList.add(new HeartBean());


        // 初始化个人的RecycleView
        personAdapter = new MainPersonAdapter();
        personAdapter.addAllData(personList);

        rvPerson.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL,false)); // 设置布局管理器
        rvPerson.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST)); // 添加分割线：应用的是系统的listDivider属性，可以在style中定制自己的分割线
        rvPerson.setItemAnimator(new DefaultItemAnimator()); // 设置item增删的动画，这个是默认的动画，另外可以引用
        rvPerson.setAdapter(personAdapter);
    }

    @Override
    protected void setListener() {

        // 添加条目监听的第一种方法：通过判断手势来传递条目点击事件，使用比较简单，可以实现整个条目的监听
        rvPerson.addOnItemTouchListener(new RecycleItemTouchListener(rvPerson) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int position) {

            }

            @Override
            public void onLongClick(RecyclerView.ViewHolder vh, int position) {

            }
        });

        // 添加条目监听的第二种方法：通过写接口，可以实现条目内部的控件的点击事件的监听
//        personAdapter.setOnItemClickLitener(new MainPersonAdapter.OnItemClickLitener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                CommonUtil.toast("点击:" + position);
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//                CommonUtil.toast("长按:" + position);
//                personAdapter.removeData(position);
//            }
//        });
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.tv_online, R.id.tv_offline, R.id.tv_exit})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_online:    // 上线
                if (GlobalConfig.PERSON_CURRENT_STATE == GlobalConfig.ACTION_PERSON_ONLINE) {
                    CommonUtil.toast("大人，您已经在线上了，喵~");
                }else {
                    startLoading();
                    if (GlobalConfig.PERSON_CURRENT_STATE == GlobalConfig.ACTION_PERSON_OFFLINE) {
                        ServiceModel.getInstance().onStartChatService();
                    }
                    if (GlobalConfig.PERSON_CURRENT_STATE == GlobalConfig.ACTION_PERSON_EXIT) {
                        ServiceModel.getInstance().onCreateChatService();
                    }
                }

                break;

            case R.id.tv_offline:   // 下线
                if (GlobalConfig.PERSON_CURRENT_STATE == GlobalConfig.ACTION_PERSON_ONLINE) {
                    startLoading();
                    ServiceModel.getInstance().onStopHeartService();
                    personAdapter.removeAllData();
                } else {
                    CommonUtil.toast("大人，您已经从线上滚下来啦，喵!");
                }
                break;

            case R.id.tv_exit:      // 退出
                startLoading();
                if (GlobalConfig.PERSON_CURRENT_STATE == GlobalConfig.ACTION_PERSON_ONLINE) {
                    ServiceModel.getInstance().onStopHeartService();
                    personAdapter.removeAllData();
                }
                ServiceModel.getInstance().onDestroyHeartService();
        }
    }

    /** 开始显示Loading */
    private void startLoading() {
        rlLoading.setVisibility(View.VISIBLE);
        tvOnline.setClickable(false);
        tvOffline.setClickable(false);
        tvExit.setClickable(false);
    }

    /** 停止显示Loading */
    private void endLoading() {
        rlLoading.setVisibility(View.GONE);
        tvOnline.setClickable(true);
        tvOffline.setClickable(true);
        tvExit.setClickable(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        android.os.Process.killProcess(android.os.Process.myPid()); // 杀死当前进程!
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onExitEvent(ExitEvent ExitEvent) {
        LogUtil.i(tag,"onExitEvent");
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEndLoadingEvent(EndLoadingEvent event) {
        LogUtil.i(tag,"onEndLoadingEvent");
        endLoading();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void personEvent(HeartBean heartBean) {

        LogUtil.i(tag,"personEvent：接收到上下线提示:" + heartBean.getName() + " , action:" + heartBean.getAction());

        if (heartBean.getAction() == GlobalConfig.ACTION_PERSON_ONLINE) {
            if (!onLine(heartBean)) {
                // 有个人上线提示
                CommonUtil.toast(heartBean.getName() + "大人来啦，汪汪汪！");
                personAdapter.addData(heartBean);
            }
        } else {
            // 有个人下线提示
            CommonUtil.toast(heartBean.getName() + "大人滚了，汪汪汪！");
            personAdapter.removeData(heartBean);
        }

    }

    /** 检查界面上是否显示在线 */
    private boolean onLine(HeartBean heartBean) {
        ArrayList<HeartBean> data = personAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(heartBean.getId())) {
                return true;
            }
        }
        return false;
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

}
