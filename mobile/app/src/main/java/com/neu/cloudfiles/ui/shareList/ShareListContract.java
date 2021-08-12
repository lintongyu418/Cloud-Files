package com.neu.cloudfiles.ui.shareList;

import com.neu.cloudfiles.base.BaseContract;
import com.neu.cloudfiles.bean.SaveShareFileDo;
import com.neu.cloudfiles.bean.ShareFileListDo;
import com.neu.cloudfiles.bean.ShareFileListVo;

import java.util.List;

public interface ShareListContract {
    interface View extends BaseContract.BaseView {
        void getShareBatchSuccess(List<ShareFileListVo> files);
        void saveFileSuccess();
    }

    interface Presenter extends BaseContract.BasePresenter<ShareListContract.View> {
        void getShareBatchFiles(ShareFileListDo fileListDo);
        void saveFile(SaveShareFileDo shareFileDo);
    }
}
