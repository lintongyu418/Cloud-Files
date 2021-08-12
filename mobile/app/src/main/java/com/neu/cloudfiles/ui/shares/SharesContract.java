package com.neu.cloudfiles.ui.shares;

import com.neu.cloudfiles.base.BaseContract;
import com.neu.cloudfiles.bean.BannerVo;
import com.neu.cloudfiles.bean.ShareFileListVo;

import java.util.List;

public interface SharesContract {
    interface View extends BaseContract.BaseView {
        void getSharesCb(List<ShareFileListVo> files);
        void getBannersCb(List<BannerVo> banners);
    }

    interface Presenter extends BaseContract.BasePresenter<SharesContract.View> {
        void getShares();
        void getBanners();
    }
}