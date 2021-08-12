package com.neu.cloudfiles.ui.download;

import com.neu.cloudfiles.base.BaseContract;
import com.neu.cloudfiles.bean.DownloadFileDo;
import com.neu.cloudfiles.bean.DownloadFileVo;
import com.neu.cloudfiles.event.UpdateDownloadStatus;

public interface DownloadListContract {
    interface View extends BaseContract.BaseView {
        void updateTaskProgress(UpdateDownloadStatus updateDownloadStatus);

        void addNewTaskCb(DownloadFileVo fileVo);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void addTask(DownloadFileDo fileVo);
    }
}
