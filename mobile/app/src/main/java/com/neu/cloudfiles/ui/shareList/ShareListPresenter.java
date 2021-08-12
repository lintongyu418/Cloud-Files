package com.neu.cloudfiles.ui.shareList;

import com.google.gson.Gson;
import com.neu.cloudfiles.base.BasePresenter;
import com.neu.cloudfiles.bean.DataResponse;
import com.neu.cloudfiles.bean.SaveShareFileDo;
import com.neu.cloudfiles.bean.ShareFileListDo;
import com.neu.cloudfiles.bean.ShareFileListVo;
import com.neu.cloudfiles.net.ApiService;
import com.neu.cloudfiles.net.RetrofitManager;
import com.neu.cloudfiles.constant.Constant;
import com.neu.cloudfiles.utils.RxSchedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class ShareListPresenter extends BasePresenter<ShareListContract.View>
        implements ShareListContract.Presenter {

    @Inject
    public ShareListPresenter() {
    }

    @Override
    public void getShareBatchFiles(ShareFileListDo fileListDo) {
        Object a = RetrofitManager.create(ApiService.class)
                .getShareContent(fileListDo.toMap())
                .compose(RxSchedulers.<DataResponse<Map<String, Object>>>applySchedulers())
                .compose(mView.<DataResponse<Map<String, Object>>>bindToLife())
                .subscribe(new Consumer<DataResponse<Map<String, Object>>>() {
                    @Override
                    public void accept(DataResponse<Map<String, Object>> dataResponse) throws Exception {
                        if (dataResponse.getCode() == Constant.SUCCESS_RESPONSE) {
                            Gson gson = new Gson();
                            List<ShareFileListVo> shareFileListVos = new ArrayList<>();
                            List dataList = (List) dataResponse.getData().get("list");
                            for (Object item: dataList) {
                                shareFileListVos.add(gson.fromJson(gson.toJsonTree(item), ShareFileListVo.class));
                            }
                            mView.getShareBatchSuccess(shareFileListVos);
                        } else {
                            mView.showFailed("Failed to delete file!");
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
    public void saveFile(SaveShareFileDo shareFileDo) {
        Object a = RetrofitManager.create(ApiService.class)
                .saveShareFile(shareFileDo)
                .compose(RxSchedulers.<DataResponse<Map<String, Object>>>applySchedulers())
                .compose(mView.<DataResponse<Map<String, Object>>>bindToLife())
                .subscribe(new Consumer<DataResponse<Map<String, Object>>>() {
                    @Override
                    public void accept(DataResponse<Map<String, Object>> dataResponse) throws Exception {
                        if (dataResponse.getCode() == Constant.SUCCESS_RESPONSE) {
                            mView.saveFileSuccess();
                        } else {
                            mView.showFailed("Failed to save, " + dataResponse.getMessage());
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
