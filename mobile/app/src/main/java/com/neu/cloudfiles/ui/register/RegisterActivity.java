package com.neu.cloudfiles.ui.register;

import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.neu.cloudfiles.R;
import com.neu.cloudfiles.base.BaseActivity;
import com.neu.cloudfiles.event.RegisterSuccessEvent;
import com.neu.cloudfiles.utils.RxBus;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/cloudFile/Register")
public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View {
    @BindView(R.id.username)
    EditText mEtUsername;
    @BindView(R.id.password)
    EditText mEtPassword;
    @Override
    public void registerSuccess(String result) {
        ARouter.getInstance().build("/cloudFile/Login").navigation();
        String username = mEtUsername.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        RxBus.getInstance().post(new RegisterSuccessEvent(username, password));
        this.finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected boolean showHomeAsUp() {
        return true;
    }

    @OnClick(R.id.btn_register)
    public void register() {
        String username = mEtUsername.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        this.mPresenter.register(username, password);
    }

}
