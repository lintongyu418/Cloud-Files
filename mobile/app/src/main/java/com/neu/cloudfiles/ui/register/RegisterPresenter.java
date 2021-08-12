package com.neu.cloudfiles.ui.register;

import com.blankj.utilcode.util.SPUtils;
import com.neu.cloudfiles.base.BasePresenter;
import com.neu.cloudfiles.bean.DataResponse;
import com.neu.cloudfiles.bean.RegisterDo;
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

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {
    @Inject
    public RegisterPresenter() {
    }


    @Override
    public void register(final String username, final String password) {
        RegisterDo registerDo = new RegisterDo(username, username, password);
        Object a = RetrofitManager.create(ApiService.class)
                .register(registerDo)
                .compose(RxSchedulers.<DataResponse<Map<String, Object>>>applySchedulers())
                .compose(mView.<DataResponse<Map<String, Object>>>bindToLife())
                .subscribe(new Consumer<DataResponse<Map<String, Object>>>() {
                    @Override
                    public void accept(DataResponse<Map<String, Object>> response) throws Exception {
                        if (response.getCode() == Constant.SUCCESS_RESPONSE) {
                            SPUtils.getInstance(Constant.SHARED_NAME).put(Constant.USERNAME_KEY, username);
                            SPUtils.getInstance(Constant.SHARED_NAME).put(Constant.PASSWORD_KEY, password);
                            mView.registerSuccess("success");
                        } else {
                            mView.showFailed("Failed to register!");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showFailed("Failed to register!");
                    }
                });
    }
}
