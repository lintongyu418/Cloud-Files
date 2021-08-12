package com.neu.cloudfiles.ui.download;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.neu.cloudfiles.base.BaseFragment;
import com.neu.cloudfiles.R;
import com.neu.cloudfiles.bean.DownloadFileDo;
import com.neu.cloudfiles.bean.DownloadFileVo;
import com.neu.cloudfiles.event.UpdateDownloadStatus;
import com.neu.cloudfiles.utils.FileUtils;
import com.neu.cloudfiles.utils.RxBus;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

public class DownloadListFragment extends BaseFragment<DownloadListPresenter>
        implements DownloadListContract.View, DownloadFileAdapter.OnItemChildClickListener {

    @BindView(R.id.downloadList)
    RecyclerView mDownloadList;
    @Inject
    DownloadFileAdapter mDownloadFileAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_download_list;
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected void initView(View view) {
        // init views
        mDownloadList.setLayoutManager(new LinearLayoutManager(getContext()));
        mDownloadList.setAdapter(mDownloadFileAdapter);
        mDownloadFileAdapter.setOnItemChildClickListener(this);

        // listener to new download task event
        Object a = RxBus.getInstance().toFlow(DownloadFileDo.class)
                .subscribe(new Consumer<DownloadFileDo>() {
            @Override
            public void accept(DownloadFileDo downloadFileVo) throws Exception {
                // add a new download task
                DownloadFileVo vo = new DownloadFileVo(downloadFileVo.getFileName(), downloadFileVo.getImgId(), downloadFileVo.getUserFileId(), downloadFileVo.getFileSize());
                mDownloadFileAdapter.addData(vo);
                mPresenter.addTask(downloadFileVo);
                ToastUtils.showShort("Successfully add a new download task");
            }
        });

        Object b = RxBus.getInstance().toFlow(UpdateDownloadStatus.class)
                .subscribe(new Consumer<UpdateDownloadStatus>() {
                    @Override
                    public void accept(final UpdateDownloadStatus updateDownloadStatus) throws Exception {
                        // 更新数据
                        updateTaskProgress(updateDownloadStatus);
                    }
                });
    }

    @Override
    public void updateTaskProgress(UpdateDownloadStatus updateDownloadStatus) {
        int position = mDownloadFileAdapter.getItemPositionByUserFileId(updateDownloadStatus.getUserFileId());
        mDownloadFileAdapter.getData().get(position).setStatus(updateDownloadStatus.getStatus());
        mDownloadFileAdapter.getData().get(position).setFilePath(updateDownloadStatus.getFilePath());
        mDownloadFileAdapter.notifyItemChanged(position);
    }

    @Override
    public void addNewTaskCb(DownloadFileVo fileVo) {
        mDownloadFileAdapter.addData(fileVo);
    }

    public static DownloadListFragment newInstance() {
        return new DownloadListFragment();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.download_view_but) {
            DownloadFileVo fileVo = (DownloadFileVo) adapter.getData().get(position);
            FileUtils.openFile(getContext(), fileVo.getFilePath());
        } else if (view.getId() == R.id.download_retry) {

        }
    }
}
