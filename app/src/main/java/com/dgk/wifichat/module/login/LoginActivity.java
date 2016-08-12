package com.dgk.wifichat.module.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.dgk.wifichat.R;
import com.dgk.wifichat.app.GlobalConfig;
import com.dgk.wifichat.app.MyApplication;
import com.dgk.wifichat.base.BaseActivity;
import com.dgk.wifichat.model.sp.SPConstants;
import com.dgk.wifichat.model.sp.SPUtils;
import com.dgk.wifichat.module.main.MainActivity;
import com.dgk.wifichat.module.main.MainContract;
import com.dgk.wifichat.utils.CommonUtil;
import com.dgk.wifichat.utils.LogUtil;

import java.nio.charset.Charset;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginContract.IView, LoginPresenter> implements LoginContract.IView {

    private static String tag = "【LoginActivity】";

    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.et_name)
    EditText etName;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        etId.clearFocus();
        etName.clearFocus();
    }

    @Override
    protected void setListener() {

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String name = etName.getText().toString();
                int length = name.getBytes(GlobalConfig.ENCODING_STYLE).length;
                LogUtil.i(tag,"Name：" + name + " , length：" + name.length() + " , 字节数组长度：" + length + " , 字节转换成String：" + new String(name.getBytes(GlobalConfig.ENCODING_STYLE)));
                if (length > 20) {
                    etName.setText(name.substring(0,20));
                    etName.setSelection(20);
                    CommonUtil.toast("昵称长度不能超过20个字节!");
                    CommonUtil.toast("好吧，小白，再说一遍哦~");
                    CommonUtil.toast("昵称长度不能超过6个汉字或者20个ABC!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {

        String id = SPUtils.getString(MyApplication.getContext(), SPConstants.USER_ID, "");
        String name = SPUtils.getString(MyApplication.getContext(), SPConstants.USER_NAME, "");


        if (!TextUtils.isEmpty(id)) {
            etId.setText(id);
        }

        if (!TextUtils.isEmpty(name)) {
            etName.setText(name);
        }
    }

    @OnClick(R.id.tv_login)
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_login:
                String id = etId.getText().toString().trim();
                String name = etName.getText().toString().trim();
                presenter.login(id,name);
                break;
        }
    }

    @Override
    public void showError(String s) {
        CommonUtil.toast(s);
    }

    @Override
    public void launchActivity() {
        MainActivity.launch(this);
        finish();
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
