package com.neu.cloudfiles.ui.upload;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.neu.cloudfiles.R;
import com.neu.cloudfiles.base.BaseActivity;
import com.neu.cloudfiles.bean.UploadFileVo;
import com.neu.cloudfiles.utils.FileUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/cloudFile/FileUploadActivity")
public class FileUploadActivity extends BaseActivity<FileUploadPresenter>
        implements FileUploadContract.View {

    @Autowired
    String targetFolderPath;

    @BindView(R.id.upload_file_btn)
    Button uploadBut;

    @BindView(R.id.file_select)
    Button selectFileBut;

    @BindView(R.id.upload_file_name)
    TextView fileName;
    @BindView(R.id.upload_file_path)
    TextView filePath;

    @BindView(R.id.upload_file_size)
    TextView fileSize;

    @BindView(R.id.upload_file_status)
    TextView fileStatus;

    private String selectedFilePath = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_upload;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void initView() {
        fileName.setText("File not selected");
        filePath.setText("File not selected");
        fileSize.setText(0 + " bytes");
        fileStatus.setText("Select a file to upload!");
        uploadBut.setEnabled(false);
    }

    @OnClick(R.id.file_select)
    public void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.upload_file_btn)
    public void uploadFile() {
        if (!selectedFilePath.isEmpty()) {
            fileStatus.setText("uploading please wait!");
            uploadBut.setEnabled(false);
            mPresenter.uploadFile(selectedFilePath, targetFolderPath);
            selectedFilePath = "";
            ToastUtils.showShort("Start uploading!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String path;
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                //使用第三方应用打开
                selectedFilePath = uri.getPath();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                selectedFilePath = FileUtils.getPath(this, uri);
            } else {//4.4以下下系统调用方法
                selectedFilePath = FileUtils.getRealPathFromURI(this, uri);
            }
            if (!selectedFilePath.isEmpty()) {
                filePath.setText(selectedFilePath);
                File file = new File(selectedFilePath);
                fileSize.setText(file.length() + " bytes");
                fileName.setText(file.getName());
                fileStatus.setText("File ready!");
                uploadBut.setEnabled(true);
            }
        }
    }

    @Override
    protected boolean showHomeAsUp() {
        return true;
    }

    @Override
    public void uploadFileCb(UploadFileVo fileVo) {
        fileStatus.setText("Uploaded!");
        uploadBut.setEnabled(true);
    }

    @Override
    public void uploadError() {
        uploadBut.setEnabled(true);
        fileStatus.setText("error occurred!");
    }

    @Override
    public void finish() {
        super.finish();
    }
}
