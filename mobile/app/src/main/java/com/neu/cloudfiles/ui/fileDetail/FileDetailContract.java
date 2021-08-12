package com.neu.cloudfiles.ui.fileDetail;

import com.neu.cloudfiles.base.BaseContract;
import com.neu.cloudfiles.bean.DownloadFileDo;
import com.neu.cloudfiles.bean.ShareFileDo;
import com.neu.cloudfiles.bean.ShareFileListVo;
import com.neu.cloudfiles.bean.ShareFileVo;

import java.util.List;

public interface FileDetailContract {
    interface View extends BaseContract.BaseView {
        void downloadFileSuccess();
        void deleteFileSuccess();
        void shareFileSuccess(ShareFileVo fileVo);
        void getFileShareSuccess(List<ShareFileListVo> shareFileListVoList);
    }

    interface Presenter extends BaseContract.BasePresenter<FileDetailContract.View> {
        void downloadFile(DownloadFileDo downloadFileDo);
        void deleteFile(long userFileId);
        void shareFile(ShareFileDo fileDo);
        void getFileShares(Long userFileId);
    }
}
