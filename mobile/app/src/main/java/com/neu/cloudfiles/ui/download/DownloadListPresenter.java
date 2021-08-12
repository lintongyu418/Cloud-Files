package com.neu.cloudfiles.ui.download;

import com.blankj.utilcode.util.ToastUtils;
import com.neu.cloudfiles.base.BasePresenter;
import com.neu.cloudfiles.bean.DownloadFileDo;
import com.neu.cloudfiles.bean.DownloadFileVo;
import com.neu.cloudfiles.net.ApiService;
import com.neu.cloudfiles.net.RetrofitManager;
import com.neu.cloudfiles.utils.DownloadTask;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadListPresenter extends BasePresenter<DownloadListContract.View> implements DownloadListContract.Presenter {

    @Inject
    public DownloadListPresenter() {
    }

    @Override
    public void addTask(final DownloadFileDo fileDo) {
        // add a download task
        final DownloadFileVo vo = new DownloadFileVo(fileDo.getFileName(), fileDo.getImgId(), fileDo.getUserFileId(), fileDo.getFileSize());
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("userFileId", fileDo.getUserFileId());
        Call<ResponseBody> call = RetrofitManager.create(ApiService.class).downloadFile(queryMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ToastUtils.showShort("downloading file");
                    DownloadTask downloadTask = new DownloadTask(vo);
                    downloadTask.execute(response.body());
                } else {
                    ToastUtils.showShort("failed to download");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        mView.addNewTaskCb(vo);
    }
}
