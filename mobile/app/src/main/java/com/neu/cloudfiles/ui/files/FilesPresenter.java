package com.neu.cloudfiles.ui.files;

import com.google.gson.Gson;
import com.neu.cloudfiles.base.BasePresenter;
import com.neu.cloudfiles.bean.DataResponse;
import com.neu.cloudfiles.bean.FileListVo;
import com.neu.cloudfiles.constant.Constant;
import com.neu.cloudfiles.net.ApiService;
import com.neu.cloudfiles.net.RetrofitManager;
import com.neu.cloudfiles.utils.RxSchedulers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class FilesPresenter extends BasePresenter<FilesContract.View> implements FilesContract.Presenter {

    private boolean mIsRefresh;
    private String mFilePath;
    private static final Map<String, Object> queryMap = new HashMap<String, Object>() {{
        put("filePath", Constant.FILE_PATH_DEFAULT);
        put("currentPage", Constant.FILE_PAGE);
        put("pageCount", Constant.FILE_PAGE_SIZE);
        put("fileType", Constant.ALL_TYPE);
    }};
    private static final FileListVo PrevFolder = new FileListVo();

    @Inject
    public FilesPresenter() {
        this.mIsRefresh = false;
        PrevFolder.setIsDir(Constant.PREV_DIR);
        PrevFolder.setFilePath(Constant.FILE_PATH_DEFAULT);
        PrevFolder.setFileName("prev folder");
        PrevFolder.setExtendName("prev");
        PrevFolder.setUploadTime("");
    }

    @Override
    public void loadUserFiles(final String filePath) {
        PrevFolder.setFilePath(this.mFilePath);
        this.mFilePath = filePath;
        queryMap.put("filePath", this.mFilePath);
        Object a = RetrofitManager.create(ApiService.class)
                .getUserFiles(queryMap)
                .compose(RxSchedulers.<DataResponse<Map<String, Object>>>applySchedulers())
                .compose(mView.<DataResponse<Map<String, Object>>>bindToLife())
                .subscribe(new Consumer<DataResponse<Map<String, Object>>>() {
                    @Override
                    public void accept(DataResponse<Map<String, Object>> dataResponse) throws Exception {
                        List<Object> files = (List<Object>) dataResponse.getData().get("files");
                        Gson gson = new Gson();
                        List<FileListVo> result = new ArrayList<>();
                        if (!mFilePath.equals(Constant.FILE_PATH_DEFAULT)) {
                            result.add(PrevFolder);
                        }
                        for (Object file : files) {
                            result.add(gson.fromJson(gson.toJsonTree(file), FileListVo.class));
                        }
                        mView.setUserFiles(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showFailed(throwable.getMessage());
                    }
                });
    }


    @Override
    public void refresh() {
        loadUserFiles(mFilePath);
    }

    @Override
    public void loadMore() {

    }

    @Override
    public void setFilePath(String path) {
        PrevFolder.setFilePath(this.mFilePath);
        this.mFilePath = path;
    }

    @Override
    public String getFilePath() {
        return this.mFilePath;
    }
}
