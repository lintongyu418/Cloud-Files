package com.neu.cloudfiles.ui.download;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.neu.cloudfiles.R;
import com.neu.cloudfiles.bean.DownloadFileVo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

public class DownloadFileAdapter extends BaseQuickAdapter<DownloadFileVo, BaseViewHolder> {

    @Inject
    public DownloadFileAdapter() {
        super(R.layout.item_download, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, DownloadFileVo item) {
        helper.setText(R.id.download_file_name, item.getFileName());
        helper.setText(R.id.download_status, item.getStatus());
        helper.setImageResource(R.id.download_img, item.getImgId());
        helper.addOnClickListener(R.id.download_view_but);
    }

    @Override
    public void addData(@NonNull Collection<? extends DownloadFileVo> newData) {
        Set<Long> ids = new HashSet<>();
        for (DownloadFileVo innerFile : this.getData()) {
            ids.add(innerFile.getUserFileId());
        }
        List<DownloadFileVo> append = new ArrayList<>();
        for (DownloadFileVo file : newData) {
            if (ids.contains(file.getUserFileId())) continue;
            append.add(file);
        }
        super.addData(append);
    }

    @Override
    public void addData(@NonNull DownloadFileVo data) {
        Set<Long> ids = new HashSet<>();
        for (DownloadFileVo innerFile : this.getData()) {
            ids.add(innerFile.getUserFileId());
        }
        if (ids.contains(data.getUserFileId())) return;
        super.addData(data);
    }

    int getItemPositionByUserFileId(long userFileId) {
        for (int i = 0; i < this.getData().size(); i++) {
            if (this.getData().get(i).getUserFileId() == userFileId) return i;
        }
        return -1;
    }
}