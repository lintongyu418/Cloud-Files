package com.neu.cloudfiles.ui.login;

import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.neu.cloudfiles.R;
import com.neu.cloudfiles.base.BaseActivity;
import com.neu.cloudfiles.bean.UserLoginVo;
import com.neu.cloudfiles.bean.UserVo;
import com.neu.cloudfiles.constant.Constant;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lw on 2018/1/24.
 */
@Route(path = "/cloudFile/Login")
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    @BindView(R.id.username)
    EditText mEtUsername;
    @BindView(R.id.password)
    EditText mEtPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void initView() {
        mEtUsername.setText(SPUtils.getInstance(Constant.SHARED_NAME).getString(Constant.USERNAME_KEY, "13429903248"));
        mEtPassword.setText(SPUtils.getInstance(Constant.SHARED_NAME).getString(Constant.PASSWORD_KEY, "wjc123"));
        this.mPresenter.checkLogin();
    }

    @Override
    protected boolean showHomeAsUp() {
        return false;
    }

    @OnClick(R.id.btn_login)
    public void login() {
        String username = mEtUsername.getText().toString();
        String password = mEtPassword.getText().toString();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            ToastUtils.showShort(R.string.the_username_or_password_can_not_be_empty);
            return;
        }
        mPresenter.login(username, password);
    }

    @Override
    public void loginSuccess(UserLoginVo user) {
        SPUtils.getInstance(Constant.SHARED_NAME).put(Constant.LOGIN_KEY, true);
        SPUtils.getInstance(Constant.SHARED_NAME).put(Constant.USERNAME_KEY, user.getTelephone());
        SPUtils.getInstance(Constant.SHARED_NAME).put(Constant.TOKEN_KEY, user.getToken());
        /**登陆成功通知其他界面刷新*/
//        RxBus.getInstance().post(new LoginEvent());
        ARouter.getInstance().build("/cloudFile/MainActivity").navigation();
        this.finish();
    }

    @Override
    public void checkLoginCb(UserVo userVo) {
        SPUtils.getInstance(Constant.SHARED_NAME).put(Constant.LOGIN_KEY, true);
        SPUtils.getInstance(Constant.SHARED_NAME).put(Constant.USERNAME_KEY, userVo.getUsername());
        ARouter.getInstance().build("/cloudFile/MainActivity").navigation();
        this.finish();
    }

}
