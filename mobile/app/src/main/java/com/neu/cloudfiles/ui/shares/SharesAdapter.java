package com.neu.cloudfiles.ui.shares;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.neu.cloudfiles.R;
import com.neu.cloudfiles.bean.ShareFileListVo;
import com.neu.cloudfiles.constant.Constant;


import javax.inject.Inject;

public class SharesAdapter extends BaseQuickAdapter<ShareFileListVo, BaseViewHolder> {

    @Inject
    public SharesAdapter() {
        super(R.layout.item_shares, null);
    }
    @Override
    protected void convert(BaseViewHolder helper, ShareFileListVo item) {
        helper.setText(R.id.file_name, item.getFileName() == null ? "Unknown" : item.getFileName());
        helper.setText(R.id.username, item.getUsername() == null ? "Anonymous" : item.getUsername());
        helper.setImageResource(R.id.share_user_img, getAvatar());
        int imgId = null != item.getExtendName() && Constant.FILE_TYPE_MAP.containsKey(item.getExtendName())
                ? Constant.FILE_TYPE_MAP.get(item.getExtendName())
                : Constant.FILE_TYPE_MAP.get("unknown");
        helper.setImageResource(R.id.share_image, imgId);
        helper.setImageResource(R.id.share_type, item.getShareType() == 0 ? R.drawable.share_public : R.drawable.share_private);
        helper.addOnClickListener(R.id.share_save);
        helper.addOnClickListener(R.id.share_copy_btn);
    }

    private int getAvatar() {
        int number = (int) (Math.random() * 13 + 1);
        return Constant.AVATAR_MAP.get(number);
    }
}
