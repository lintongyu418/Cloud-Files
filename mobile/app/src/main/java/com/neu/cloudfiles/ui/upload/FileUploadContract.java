package com.neu.cloudfiles.ui.upload;

import com.neu.cloudfiles.base.BaseContract;
import com.neu.cloudfiles.bean.UploadFileVo;

public interface FileUploadContract {


    interface View extends BaseContract.BaseView {
        void uploadFileCb(UploadFileVo fileVo);
        void uploadError();
    }

    interface Presenter extends BaseContract.BasePresenter<FileUploadContract.View> {
        void uploadFile(String filePath, String targetPath);
    }
}