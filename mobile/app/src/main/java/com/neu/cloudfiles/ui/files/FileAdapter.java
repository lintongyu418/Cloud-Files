package com.neu.cloudfiles.ui.files;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.neu.cloudfiles.R;
import com.neu.cloudfiles.bean.FileListVo;
import com.neu.cloudfiles.constant.Constant;

import javax.inject.Inject;

public class FileAdapter extends BaseQuickAdapter<FileListVo, BaseViewHolder> {

    @Inject
    public FileAdapter() {
        super(R.layout.item_file, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, FileListVo item) {
        helper.setText(R.id.file_name, item.getFileName());
        helper.setText(R.id.file_time, item.getUploadTime());
        String extendName = item.getIsDir() == 1 ? "dir" : item.getExtendName();
        int imgId = Constant.FILE_TYPE_MAP.containsKey(extendName)
                ? Constant.FILE_TYPE_MAP.get(extendName)
                : Constant.FILE_TYPE_MAP.get("unknown");
        helper.setImageResource(R.id.file_image, imgId);
    }
}
