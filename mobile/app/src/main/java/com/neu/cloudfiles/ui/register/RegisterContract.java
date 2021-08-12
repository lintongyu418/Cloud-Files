package com.neu.cloudfiles.ui.register;

import com.neu.cloudfiles.base.BaseContract;
import com.neu.cloudfiles.bean.UserLoginVo;
import com.neu.cloudfiles.bean.UserVo;

/**
 * Created by lw on 2018/1/24.
 */

public interface RegisterContract {
    interface View extends BaseContract.BaseView {
        void registerSuccess(String result);
    }

    interface Presenter extends BaseContract.BasePresenter<RegisterContract.View> {
        void register(String username, String password);
    }
}
