package com.neu.cloudfiles.ui.login;

import com.neu.cloudfiles.base.BaseContract;
import com.neu.cloudfiles.bean.UserLoginVo;
import com.neu.cloudfiles.bean.UserVo;

/**
 * Created by lw on 2018/1/24.
 */

public interface LoginContract {
    interface View extends BaseContract.BaseView {
        void loginSuccess(UserLoginVo user);
        void checkLoginCb(UserVo userVo);
    }

    interface Presenter extends BaseContract.BasePresenter<LoginContract.View> {
        void login(String username, String password);
        void checkLogin();
    }
}
