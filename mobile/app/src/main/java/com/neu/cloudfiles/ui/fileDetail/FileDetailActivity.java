package com.neu.cloudfiles.ui.fileDetail;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.neu.cloudfiles.R;
import com.neu.cloudfiles.base.BaseActivity;
import com.neu.cloudfiles.bean.DownloadFileDo;
import com.neu.cloudfiles.bean.FileListVo;
import com.neu.cloudfiles.bean.ShareFileDo;
import com.neu.cloudfiles.bean.ShareFileListDo;
import com.neu.cloudfiles.bean.ShareFileListVo;
import com.neu.cloudfiles.bean.ShareFileVo;
import com.neu.cloudfiles.event.RefreshUserFileListEvent;
import com.neu.cloudfiles.event.UserLogoutEvent;
import com.neu.cloudfiles.ui.userInfo.UserInfoActivity;
import com.neu.cloudfiles.utils.CircleButton;
import com.neu.cloudfiles.utils.CommonDialog;
import com.neu.cloudfiles.utils.FileUtils;
import com.neu.cloudfiles.utils.QRCodeUtil;
import com.neu.cloudfiles.utils.RxBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

@Route(path = "/cloudFile/FileDetailActivity")
public class FileDetailActivity extends BaseActivity<FileDetailPresenter>
        implements FileDetailContract.View, ShareListAdapter.OnItemChildClickListener {
    private final static Gson StaticJson = new Gson();
    @Autowired(name = "fileDetail")
    FileListVo fileDetail;

    @BindView(R.id.file_name)
    TextView fileName;
    @BindView(R.id.file_upload_time)
    TextView fileUploadTime;
    @BindView(R.id.img_preview)
    ImageView fileImage;
    @BindView(R.id.file_download)
    CircleButton fileDownloadBtn;
    @BindView(R.id.file_delete)
    CircleButton fileDeleteBtn;
    @BindView(R.id.file_share)
    CircleButton fileShareBut;
    @BindView(R.id.file_size)
    TextView fileSize;
    @BindView(R.id.share_list)
    RecyclerView recyclerView;
    @BindView(R.id.file_refresh)
    CircleButton shareRefreshBt;

    @Inject
    ShareListAdapter shareListAdapter;

    Dialog openQRDialog = null;
    AlertDialog confirmDialog = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_file_detail;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(shareListAdapter);
        shareListAdapter.setOnItemChildClickListener(this);
        if (fileDetail != null) {
            fileName.setText(fileDetail.getFileName() + "." + fileDetail.getExtendName());
            fileUploadTime.setText(fileDetail.getUploadTime());
            fileSize.setText(fileDetail.getFileSize().toString());
            fileImage.setImageResource(FileUtils.getFileTypeImg(fileDetail.getIsDir(), fileDetail.getExtendName()));
            mPresenter.getFileShares(fileDetail.getUserFileId());
        }
        initDialog();
        Object a = RxBus.getInstance().toFlow(UserLogoutEvent.class)
                .subscribe(new Consumer<UserLogoutEvent>() {
                    @Override
                    public void accept(UserLogoutEvent a) throws Exception {
                        // add a new download task
                        FileDetailActivity.this.finish();
                    }
                });
    }

    private void initDialog() {
        openQRDialog = new Dialog(this, R.style.edit_AlertDialog_style);
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


        confirmDialog = new AlertDialog.Builder(this).setTitle("Delete file!")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确定触发的事件
                        mPresenter.deleteFile(fileDetail.getUserFileId());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击取消触发的事件
                    }
                }).create();
    }

    @Override
    protected boolean showHomeAsUp() {
        return true;
    }

    @Override
    public void downloadFileSuccess() {

    }

    @Override
    public void deleteFileSuccess() {
        RxBus.getInstance().post(new RefreshUserFileListEvent());
        ToastUtils.showLong("Successfully deleted a file!");
        this.finish();
    }

    @Override
    public void shareFileSuccess(ShareFileVo fileVo) {
        mPresenter.getFileShares(fileDetail.getUserFileId());
    }

    @OnClick(R.id.file_refresh)
    public void refresh() {
        if (null != fileDetail) mPresenter.getFileShares(fileDetail.getUserFileId());
    }

    @Override
    public void getFileShareSuccess(List<ShareFileListVo> shareFileListVoList) {
        this.shareListAdapter.setNewData(shareFileListVoList);
    }

    @OnClick(R.id.file_download)
    public void downloadFile() {
        mPresenter.downloadFile(new DownloadFileDo(fileDetail.getFileName() + "." + fileDetail.getExtendName(),
                FileUtils.getFileTypeImg(fileDetail.getIsDir(), fileDetail.getExtendName()),
                fileDetail.getUserFileId(),
                fileDetail.getFileSize()));
    }

    @OnClick(R.id.file_delete)
    public void deleteFile() {
        if (null != fileDetail) confirmDialog.show();
    }

    @OnClick(R.id.file_share)
    public void shareFile() {
        final CommonDialog dialog = new CommonDialog(this);
        dialog.setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPositiveClick(boolean publicShare, Date expire) {
                String pattern = "MM-dd-yyyy HH:mm:ss";
                DateFormat df = new SimpleDateFormat(pattern);
                String time = df.format(expire);
                ShareFileDo shareFileDo = new ShareFileDo(
                        "[{\"userFileId\":" + fileDetail.getUserFileId() + "}]",
                        time, publicShare ? 0 : 1,
                        "mobile share");
                mPresenter.shareFile(shareFileDo);
                dialog.dismiss();
            }

            @Override
            public void onNegativeClick() {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ShareFileListVo file = (ShareFileListVo) adapter.getData().get(position);
        if (view.getId() == R.id.share_qr_btn) {
            ShareFileListDo shareFileListDo = new ShareFileListDo(
                    file.getShareBatchNum(), file.getShareFilePath()
            );
            openShareQEDialog(StaticJson.toJson(shareFileListDo));
        } else if (view.getId() == R.id.share_copy_btn) {
            ClipboardManager copy = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            copy.setText(file.getExtractionCode());
            ToastUtils.showLong("Copy share link to clipboard, " + file.getExtractionCode());
        }
    }

    private void openShareQEDialog(String content) {
        ImageView imageView = (ImageView) openQRDialog.findViewById(R.id.dialog_preview_qr);
        imageView.setImageBitmap(QRCodeUtil.createQRCodeBitmap(content, 240, 240));
        openQRDialog.show();
    }
}
