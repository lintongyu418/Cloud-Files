package com.neu.cloudfiles.ui.shareList;//package com.neu.cloudfiles2.ui.shareList;
//
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.neu.cloudfiles2.R;
//import com.neu.cloudfiles2.bean.ShareFileListVo;
//import com.neu.cloudfiles2.utils.Constant;
//
//
//import javax.inject.Inject;
//
//public class ShareFileListAdapter extends BaseQuickAdapter<ShareFileListVo, BaseViewHolder> {
//
//    @Inject
//    public ShareFileListAdapter() {
//        super(R.layout.item_shared_file, null);
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, ShareFileListVo item) {
//        helper.setText(R.id.file_name, item.getFileName());
//        helper.setText(R.id.file_time, item.getShareTime());
//        helper.setText(R.id.file_user, item.getUsername());
//        int imgId = Constant.FILE_TYPE_MAP.containsKey(item.getExtendName())
//                ? Constant.FILE_TYPE_MAP.get(item.getExtendName())
//                : Constant.FILE_TYPE_MAP.get("unknown");
//        helper.setImageResource(R.id.file_image, imgId);
//    }
//}
