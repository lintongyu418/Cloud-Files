package com.neu.cloudfiles.ui.fileDetail;

import com.google.gson.Gson;
import com.neu.cloudfiles.base.BasePresenter;
import com.neu.cloudfiles.bean.DataResponse;
import com.neu.cloudfiles.bean.DeleteFileDo;
import com.neu.cloudfiles.bean.DownloadFileDo;
import com.neu.cloudfiles.bean.ShareFileDo;
import com.neu.cloudfiles.bean.ShareFileListVo;
import com.neu.cloudfiles.bean.ShareFileVo;
import com.neu.cloudfiles.net.ApiService;
import com.neu.cloudfiles.net.RetrofitManager;
import com.neu.cloudfiles.constant.Constant;
import com.neu.cloudfiles.utils.QRCodeUtil;
import com.neu.cloudfiles.utils.RxBus;
import com.neu.cloudfiles.utils.RxSchedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;


public class FileDetailPresenter extends BasePresenter<FileDetailContract.View>
        implements FileDetailContract.Presenter {
    @Inject
    public FileDetailPresenter() {
    }

    @Override
    public void downloadFile(final DownloadFileDo fileDo) {
        RxBus.getInstance().post(fileDo);
    }

    @Override
    public void deleteFile(long userFileId) {
        Object a = RetrofitManager.create(ApiService.class)
                .deleteFile(new DeleteFileDo(userFileId))
                .compose(RxSchedulers.<DataResponse<Map<String, Object>>>applySchedulers())
                .compose(mView.<DataResponse<Map<String, Object>>>bindToLife())
                .subscribe(new Consumer<DataResponse<Map<String, Object>>>() {
                    @Override
                    public void accept(DataResponse<Map<String, Object>> dataResponse) throws Exception {
                        if (dataResponse.getCode() == Constant.SUCCESS_RESPONSE) {
                            mView.deleteFileSuccess();
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
    public void shareFile(final ShareFileDo fileDo) {
        Object a = RetrofitManager.create(ApiService.class)
                .shareFile(fileDo)
                .compose(RxSchedulers.<DataResponse<Map<String, Object>>>applySchedulers())
                .compose(mView.<DataResponse<Map<String, Object>>>bindToLife())
                .subscribe(new Consumer<DataResponse<Map<String, Object>>>() {
                    @Override
                    public void accept(DataResponse<Map<String, Object>> dataResponse) throws Exception {
                        if (dataResponse.getCode() == Constant.SUCCESS_RESPONSE) {
                            Gson gson = new Gson();
                            ShareFileVo fileVo = gson.fromJson(
                                    gson.toJsonTree(dataResponse.getData().get("share")),
                                    ShareFileVo.class);
                            fileVo.setFileQR(QRCodeUtil.createQRCodeBitmap(
                                    Constant.SHARE_LINK_PREFIX + fileVo.getShareBatchNum(),
                                    160, 160
                            ));
                            mView.shareFileSuccess(fileVo);
                            mView.showFailed("Successfully share the file!");
                        } else {
                            mView.showFailed("Failed to share file!");
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
    public void getFileShares(Long userFileId) {
        Object a = RetrofitManager.create(ApiService.class)
                .getFileShares(userFileId)
                .compose(RxSchedulers.<DataResponse<Map<String, Object>>>applySchedulers())
                .compose(mView.<DataResponse<Map<String, Object>>>bindToLife())
                .subscribe(new Consumer<DataResponse<Map<String, Object>>>() {
                    @Override
                    public void accept(DataResponse<Map<String, Object>> dataResponse) throws Exception {
                        if (dataResponse.getCode() == Constant.SUCCESS_RESPONSE) {
                            Gson gson = new Gson();
                            List<ShareFileListVo> shareFileListVos = new ArrayList<>();
                            List dataList = (List) dataResponse.getData().get("shares");
                            for (Object item: dataList) {
                                shareFileListVos.add(gson.fromJson(gson.toJsonTree(item), ShareFileListVo.class));
                            }
                            mView.getFileShareSuccess(shareFileListVos);
                            mView.showFailed("Successfully get all share links for this file!");
                        } else {
                            mView.showFailed("Failed to get all share links!");
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
