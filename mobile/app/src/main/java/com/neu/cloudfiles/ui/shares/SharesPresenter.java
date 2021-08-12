package com.neu.cloudfiles.ui.shares;

import com.google.gson.Gson;
import com.neu.cloudfiles.base.BasePresenter;
import com.neu.cloudfiles.bean.BannerVo;
import com.neu.cloudfiles.bean.DataResponse;
import com.neu.cloudfiles.bean.FileListVo;
import com.neu.cloudfiles.bean.ShareFileListVo;
import com.neu.cloudfiles.constant.Constant;
import com.neu.cloudfiles.net.ApiService;
import com.neu.cloudfiles.net.RetrofitManager;
import com.neu.cloudfiles.utils.RxSchedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class SharesPresenter extends BasePresenter<SharesContract.View> implements SharesContract.Presenter {
    @Inject
    public SharesPresenter() {
    }
    @Override
    public void getShares() {
        Object a = RetrofitManager.create(ApiService.class)
                .getAllShares()
                .compose(RxSchedulers.<DataResponse<Map<String, Object>>>applySchedulers())
                .compose(mView.<DataResponse<Map<String, Object>>>bindToLife())
                .subscribe(new Consumer<DataResponse<Map<String, Object>>>() {
                    @Override
                    public void accept(DataResponse<Map<String, Object>> dataResponse) throws Exception {
                        List<Object> files = (List<Object>) dataResponse.getData().get("shares");
                        Gson gson = new Gson();
                        List<ShareFileListVo> result = new ArrayList<>();
                        for (Object file : files) {
                            result.add(gson.fromJson(gson.toJsonTree(file), ShareFileListVo.class));
                        }
                        mView.getSharesCb(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showFailed(throwable.getMessage());
                    }
                });
    }

    @Override
    public void getBanners() {
        Object a = RetrofitManager.create(ApiService.class)
                .getBanners()
                .compose(RxSchedulers.<DataResponse<Map<String, Object>>>applySchedulers())
                .compose(mView.<DataResponse<Map<String, Object>>>bindToLife())
                .subscribe(new Consumer<DataResponse<Map<String, Object>>>() {
                    @Override
                    public void accept(DataResponse<Map<String, Object>> dataResponse) throws Exception {
                        List<Object> files = (List<Object>) dataResponse.getData().get("banners");
                        Gson gson = new Gson();
                        List<BannerVo> result = new ArrayList<>();
                        for (Object file : files) {
                            result.add(gson.fromJson(gson.toJsonTree(file), BannerVo.class));
                        }
                        mView.getBannersCb(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showFailed(throwable.getMessage());
                    }
                });
    }
}
