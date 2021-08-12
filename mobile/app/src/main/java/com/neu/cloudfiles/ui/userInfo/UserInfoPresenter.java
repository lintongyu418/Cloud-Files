package com.neu.cloudfiles.ui.userInfo;

import com.alibaba.android.arouter.launcher.ARouter;
import com.neu.cloudfiles.base.BasePresenter;
import com.neu.cloudfiles.bean.DataResponse;
import com.neu.cloudfiles.bean.UserLoginVo;
import com.neu.cloudfiles.bean.UserVo;
import com.neu.cloudfiles.constant.Constant;
import com.neu.cloudfiles.net.ApiService;
import com.neu.cloudfiles.net.RetrofitManager;
import com.neu.cloudfiles.utils.RxSchedulers;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2018/1/24.
 */

public class UserInfoPresenter extends BasePresenter<UserInfoContract.View> implements UserInfoContract.Presenter {
    @Inject
    public UserInfoPresenter() {
    }

    @Override
    public void getUserInfo(String token) {
        Object a = RetrofitManager.create(ApiService.class)
                .checkLogin()
                .compose(RxSchedulers.<DataResponse<Map<String, UserVo>>>applySchedulers())
                .compose(mView.<DataResponse<Map<String, UserVo>>>bindToLife())
                .subscribe(new Consumer<DataResponse<Map<String, UserVo>>>() {
                    @Override
                    public void accept(DataResponse<Map<String, UserVo>> response) throws Exception {
                        if (response.getCode() == Constant.SUCCESS_RESPONSE) {
                            mView.getUserInfoSuccess(response.getData().get("userInfo"));
                        } else {
                            mView.showFailed(response.getMessage());
                            ARouter.getInstance().build("/cloudFile/Login").navigation();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showFailed(throwable.getMessage());
                    }
                });
    }

    @Override
    public void logout() {

    }
}
