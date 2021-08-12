package com.neu.cloudfiles.ui.shareList;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.neu.cloudfiles.R;
import com.neu.cloudfiles.base.BaseActivity;
import com.neu.cloudfiles.bean.SaveShareFileDo;
import com.neu.cloudfiles.bean.ShareFileListDo;
import com.neu.cloudfiles.bean.ShareFileListVo;
import com.neu.cloudfiles.constant.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/cloudFile/SharedFileList")
public class ShareListActivity extends BaseActivity<ShareListPresenter>
        implements ShareListContract.View {


    @BindView(R.id.share_save)
    Button shareSaveBut;

    @BindView(R.id.file_name)
    TextView fileName;
    @BindView(R.id.file_time)
    TextView fileUploadTime;
    @BindView(R.id.file_user)
    TextView fileUser;
    @BindView(R.id.share_type)
    TextView shareType;
    @BindView(R.id.share_code)
    TextView shareCode;
    @BindView(R.id.file_image)
    ImageView fileImg;

    @Autowired(name = "shareSecret")
    ShareFileListDo shareFileListDo;

    private ShareFileListVo fileListVo;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_share_list;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected boolean showHomeAsUp() {
        return true;
    }

    @Override
    protected void initView() {
        mPresenter.getShareBatchFiles(shareFileListDo);
    }

    @Override
    public void getShareBatchSuccess(List<ShareFileListVo> files) {
        fileName.setText(files.get(0).getFileName());
        fileUploadTime.setText(files.get(0).getUploadTime());
        fileUser.setText(files.get(0).getUsername());
        shareCode.setText(files.get(0).getExtractionCode());
        shareType.setText(files.get(0).getShareType() == 0 ? "PUBLIC" : "PRIVATE");
        int imgId = Constant.FILE_TYPE_MAP.containsKey(files.get(0).getExtendName())
                ? Constant.FILE_TYPE_MAP.get(files.get(0).getExtendName())
                : Constant.FILE_TYPE_MAP.get("unknown");
        fileImg.setImageResource(imgId);
        fileListVo = files.get(0);
    }

    @OnClick(R.id.share_save)
    public void saveFile() {
        SaveShareFileDo shareFileDo = new SaveShareFileDo(fileListVo.getUserFileId(), "/");
        mPresenter.saveFile(shareFileDo);
    }


    @Override
    public void saveFileSuccess() {
        ToastUtils.showShort("Successfully saved the shared file!");
        this.finish();
    }
}
