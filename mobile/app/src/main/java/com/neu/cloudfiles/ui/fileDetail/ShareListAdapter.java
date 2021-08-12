package com.neu.cloudfiles.ui.fileDetail;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.neu.cloudfiles.R;
import com.neu.cloudfiles.bean.ShareFileListVo;

import javax.inject.Inject;

public class ShareListAdapter extends BaseQuickAdapter<ShareFileListVo, BaseViewHolder> {

    @Inject
    public ShareListAdapter() {
        super(R.layout.item_share, null);
    }
    @Override
    protected void convert(BaseViewHolder helper, ShareFileListVo item) {
        helper.addOnClickListener(R.id.share_qr_btn);
        helper.addOnClickListener(R.id.share_copy_btn);
        helper.setText(R.id.share_expired_date, "Share on: " + item.getUploadTime());
        helper.setText(R.id.share_time_stamp, "Expire on:" +  item.getEndTime());
        helper.setImageResource(R.id.share_type,
                item.getShareType() == 0 ? R.drawable.share_public
                : R.drawable.share_private);
    }
}
