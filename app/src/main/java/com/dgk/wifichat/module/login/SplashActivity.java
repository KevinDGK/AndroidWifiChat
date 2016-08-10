package com.dgk.wifichat.module.login;

import android.widget.TextView;
import com.dgk.wifichat.R;
import com.dgk.wifichat.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity<SplashContract.IView, SplashPresenter> implements SplashContract.IView {


    @BindView(R.id.tv_version_name)
    TextView tvVersionName;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected SplashPresenter initPresenter() {
        return new SplashPresenter();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        presenter.getVersionName();
    }

    @Override
    public void setVersionName(String versionName) {
        tvVersionName.setText("版本号：" + versionName);
    }

    @Override
    public void lauchActivity() {
        LoginActivity.launch(this); // 打开登陆界面
        finish();
    }
}
