package com.neu.cloudfiles.ui.shares;

import android.app.Dialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.neu.cloudfiles.R;
import com.neu.cloudfiles.base.BaseFragment;
import com.neu.cloudfiles.bean.BannerVo;
import com.neu.cloudfiles.bean.ShareFileListDo;
import com.neu.cloudfiles.bean.ShareFileListVo;
import com.neu.cloudfiles.utils.GlideImageLoader;
import com.neu.cloudfiles.utils.GsonUtils;
import com.neu.cloudfiles.utils.QRCodeUtil;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class SharesFragment extends BaseFragment<SharesPresenter>
        implements SharesContract.View, SwipeRefreshLayout.OnRefreshListener,
        SharesAdapter.OnItemClickListener, SharesAdapter.OnItemChildClickListener {

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.shareList)
    RecyclerView sharesList;
    @Inject
    SharesAdapter sharesAdapter;
    private com.youth.banner.Banner mBannerAds;
    private View mHomeBannerHeadView;
    Dialog openQRDialog = null;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shares;
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected void initView(View view) {
        sharesList.setLayoutManager(new LinearLayoutManager(getContext()));
        sharesList.setAdapter(sharesAdapter);
        sharesAdapter.setOnItemChildClickListener(this);
        sharesAdapter.setOnItemClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        /**设置BannerHeadView*/
        mHomeBannerHeadView = LayoutInflater.from(getContext()).inflate(R.layout.shares_banner_head, null);
        mBannerAds = (com.youth.banner.Banner) mHomeBannerHeadView.findViewById(R.id.banner_ads);
        sharesAdapter.addHeaderView(mHomeBannerHeadView);

        openQRDialog = new Dialog(getContext(), R.style.edit_AlertDialog_style);
        openQRDialog.setContentView(R.layout.dialog_qr);
        ImageView imageView = (ImageView) openQRDialog.findViewById(R.id.dialog_preview_qr);
        openQRDialog.setCanceledOnTouchOutside(true); // Sets whether this dialog is
        Window w = openQRDialog.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        openQRDialog.onWindowAttributesChanged(lp);
        imageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openQRDialog.dismiss();
                    }
                });

        mPresenter.getShares();
        mPresenter.getBanners();
    }

    @Override
    public void getSharesCb(List<ShareFileListVo> files) {
        sharesAdapter.setNewData(files);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getBannersCb(List<BannerVo> banners) {
        List<String> images = new ArrayList();
        List<String> titles = new ArrayList();
        for (BannerVo banner : banners) {
            images.add(banner.getImagePath());
            titles.add(banner.getTitle());
        }
        mBannerAds.setImages(images)
                .setBannerTitles(titles)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .setImageLoader(new GlideImageLoader())
                .start();
        mBannerAds.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
            }
        });
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ShareFileListVo fileVo = (ShareFileListVo) adapter.getData().get(position);
        if (view.getId() == R.id.share_save) {
            if (fileVo.getShareType() == 1) {
                ToastUtils.showShort("Private share, cannot save!");
            } else {
                ShareFileListDo shareFileListDo = new ShareFileListDo(fileVo.getShareBatchNum(), fileVo.getShareFilePath());
                ARouter.getInstance().build("/cloudFile/SharedFileList")
                        .withObject("shareSecret", shareFileListDo)
                        .navigation();
            }
        } else if (view.getId() == R.id.share_love) {

        } else if (view.getId() == R.id.share_copy_btn) {
            openShareQEDialog(GsonUtils.toJson(fileVo));
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }


    public static SharesFragment newInstance() {
        return new SharesFragment();
    }

    @Override
    public void onRefresh() {
        mPresenter.getShares();
        mPresenter.getBanners();
    }

    private void openShareQEDialog(String content) {
        ImageView imageView = (ImageView) openQRDialog.findViewById(R.id.dialog_preview_qr);
        imageView.setImageBitmap(QRCodeUtil.createQRCodeBitmap(content, 200, 200));
        openQRDialog.show();
    }
}
