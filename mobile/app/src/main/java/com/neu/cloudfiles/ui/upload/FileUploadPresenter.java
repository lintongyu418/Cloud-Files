package com.neu.cloudfiles.ui.upload;

import com.google.gson.Gson;
import com.neu.cloudfiles.base.BasePresenter;
import com.neu.cloudfiles.bean.DataResponse;
import com.neu.cloudfiles.bean.UploadFileVo;
import com.neu.cloudfiles.event.RefreshUserFileListEvent;
import com.neu.cloudfiles.net.ApiService;
import com.neu.cloudfiles.net.RetrofitManager;
import com.neu.cloudfiles.constant.Constant;
import com.neu.cloudfiles.utils.FileUtils;
import com.neu.cloudfiles.utils.RxBus;
import com.neu.cloudfiles.utils.RxSchedulers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUploadPresenter extends BasePresenter<FileUploadContract.View>
        implements FileUploadContract.Presenter {

    @Inject
    public FileUploadPresenter() {
    }

    @Override
    public void uploadFile(String filePath, String targetPath) {
        File file = new File(filePath);
        Map<String, RequestBody> stringMap = new HashMap<>();
        Map<String, Long> longMap = new HashMap<>();
        Map<String, Integer> integerMap = new HashMap<>();
        stringMap.put("filePath", RequestBody.create(MediaType.parse("plain/txt"), targetPath));
        stringMap.put("uploadTime", RequestBody.create(MediaType.parse("plain/txt"), ""));
        stringMap.put("extendName", RequestBody.create(MediaType.parse("plain/txt"), ""));
        stringMap.put("filename", RequestBody.create(MediaType.parse("plain/txt"), file.getName()));
        longMap.put("fileSize", file.length());
        integerMap.put("chunkNumber", 1);
        longMap.put("chunkSize", file.length());
        integerMap.put("totalChunks", 1);
        longMap.put("totalSize", file.length());
        longMap.put("currentChunkSize", file.length());
        stringMap.put("identifier", RequestBody.create(MediaType.parse("plain/txt"), FileUtils.getFileMD5(file)));

        RequestBody requestBody = RequestBody.create(MediaType.parse(FileUtils.getMimeType(file)), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);


        Object a = RetrofitManager.create(ApiService.class)
                .uploadFile(stringMap, longMap, integerMap, part)
                .compose(RxSchedulers.<DataResponse<Map<String, Object>>>applySchedulers())
                .compose(mView.<DataResponse<Map<String, Object>>>bindToLife())
                .subscribe(new Consumer<DataResponse<Map<String, Object>>>() {
                    @Override
                    public void accept(DataResponse<Map<String, Object>> dataResponse) throws Exception {
                        if (dataResponse.getCode() == Constant.SUCCESS_RESPONSE) {
                            Gson gson = new Gson();
                            mView.uploadFileCb(gson.fromJson(
                                    gson.toJsonTree(dataResponse.getData().get("files")),
                                    UploadFileVo.class));
                            RxBus.getInstance().post(new RefreshUserFileListEvent());
                            mView.showSuccess("Upload file successfully");
                        } else {
                            mView.showFailed("Failed to upload file!");
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
