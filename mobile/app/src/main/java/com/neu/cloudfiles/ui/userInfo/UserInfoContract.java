package com.neu.cloudfiles.ui.userInfo;

import com.neu.cloudfiles.base.BaseContract;
import com.neu.cloudfiles.bean.UserLoginVo;
import com.neu.cloudfiles.bean.UserVo;

/**
 * Created by lw on 2018/1/24.
 */

public interface UserInfoContract {
    interface View extends BaseContract.BaseView {
        void getUserInfoSuccess(UserVo user);
        void logoutSuccess();
    }

    interface Presenter extends BaseContract.BasePresenter<UserInfoContract.View> {
        void getUserInfo(String token);
        void logout();
    }
}
