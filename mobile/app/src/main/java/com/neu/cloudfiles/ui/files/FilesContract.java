package com.neu.cloudfiles.ui.files;

import com.neu.cloudfiles.base.BaseContract;
import com.neu.cloudfiles.bean.FileListVo;

import java.util.List;

public interface FilesContract {

    interface View extends BaseContract.BaseView {

        void setUserFiles(List<FileListVo> files);

        String getUserCurrentFolderPath();
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadUserFiles(String filePath);

        void refresh();

        void loadMore();

        void setFilePath(String path);

        String getFilePath();
    }
}
