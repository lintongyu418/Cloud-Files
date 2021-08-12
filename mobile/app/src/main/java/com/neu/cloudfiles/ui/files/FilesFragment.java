package com.neu.cloudfiles.ui.files;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.neu.cloudfiles.R;
import com.neu.cloudfiles.base.BaseFragment;
import com.neu.cloudfiles.bean.FileListVo;
import com.neu.cloudfiles.constant.Constant;
import com.neu.cloudfiles.event.RefreshUserFileListEvent;
import com.neu.cloudfiles.utils.FileUtils;
import com.neu.cloudfiles.utils.GridSpacingItemDecoration;
import com.neu.cloudfiles.utils.RxBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

public class FilesFragment extends BaseFragment<FilesPresenter>
        implements FilesContract.View,
        SwipeRefreshLayout.OnRefreshListener,
        FileAdapter.OnItemClickListener {

    @BindView(R.id.filesList)
    RecyclerView mHomeFileList;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Inject
    FileAdapter mFileListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_files;
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected void initView(View view) {
        /**设置RecyclerView*/
        int spanCount = 2; // 2 columns
        mHomeFileList.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        mHomeFileList.addItemDecoration(new GridSpacingItemDecoration(spanCount, 30, true));
        mHomeFileList.setAdapter(mFileListAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mFileListAdapter.setOnItemClickListener(this);

        /**请求数据*/
        mPresenter.loadUserFiles(Constant.FILE_PATH_DEFAULT);

        RxBus.getInstance().toFlow(RefreshUserFileListEvent.class)
                .subscribe(new Consumer<RefreshUserFileListEvent>() {
                    @Override
                    public void accept(RefreshUserFileListEvent event) throws Exception {
                        mPresenter.refresh();
                    }
                });
    }

    @Override
    public void setUserFiles(List<FileListVo> files) {
        mFileListAdapter.setNewData(files);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public String getUserCurrentFolderPath() {
        return mPresenter.getFilePath();
    }

    @Override
    public void onRefresh() {
        mPresenter.refresh();
    }

    public static FilesFragment newInstance() {
        return new FilesFragment();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        FileListVo item = mFileListAdapter.getItem(position);
        if (item.getIsDir() == Constant.PREV_DIR) {
            mPresenter.loadUserFiles(item.getFilePath());
        } else if (item.getIsDir() == Constant.IS_DIR) {
            mPresenter.loadUserFiles(FileUtils.getPath(item.getFilePath(), item.getFileName()));
        } else {
            ARouter.getInstance().build("/cloudFile/FileDetailActivity")
                    .withObject("fileDetail", item).navigation();
        }
    }
}
