package com.neu.cloudfiles.ui.login;

import com.neu.cloudfiles.base.BasePresenter;
import com.neu.cloudfiles.bean.DataResponse;
import com.neu.cloudfiles.bean.UserLoginVo;
import com.neu.cloudfiles.bean.UserVo;
import com.neu.cloudfiles.net.ApiService;
import com.neu.cloudfiles.net.RetrofitManager;
import com.neu.cloudfiles.constant.Constant;
import com.neu.cloudfiles.utils.RxSchedulers;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2018/1/24.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    @Inject
    public LoginPresenter() {
    }

    @Override
    public void login(String username, String password) {
        Object a = RetrofitManager.create(ApiService.class)
                .login(username, password)
                .compose(RxSchedulers.<DataResponse<Map<String, UserLoginVo>>>applySchedulers())
                .compose(mView.<DataResponse<Map<String, UserLoginVo>>>bindToLife())
                .subscribe(new Consumer<DataResponse<Map<String, UserLoginVo>>>() {
                    @Override
                    public void accept(DataResponse<Map<String, UserLoginVo>> response) throws Exception {
                        if (response.getCode() == Constant.SUCCESS_RESPONSE) {
                            mView.loginSuccess(response.getData().get("user"));
                        } else {
                            mView.showFailed(String.valueOf(response.getMessage()));
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
    public void checkLogin() {
        Object a = RetrofitManager.create(ApiService.class)
                .checkLogin()
                .compose(RxSchedulers.<DataResponse<Map<String, UserVo>>>applySchedulers())
                .compose(mView.<DataResponse<Map<String, UserVo>>>bindToLife())
                .subscribe(new Consumer<DataResponse<Map<String, UserVo>>>() {
                    @Override
                    public void accept(DataResponse<Map<String, UserVo>> response) throws Exception {
                        if (response.getCode() == Constant.SUCCESS_RESPONSE) {
                            mView.checkLoginCb(response.getData().get("userInfo"));
                        } else {
                            mView.showFailed(response.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showFailed(throwable.getMessage());
                    }
                });
    }
}
