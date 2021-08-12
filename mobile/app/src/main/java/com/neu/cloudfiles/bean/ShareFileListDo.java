package com.neu.cloudfiles.bean;

import java.util.HashMap;
import java.util.Map;

public class ShareFileListDo {
    // @Schema(description="批次号")
    private String shareBatchNum;
    // @Schema(description="分享文件路径")
    private String shareFilePath;

    public ShareFileListDo(String shareBatchNum, String shareFilePath) {
        this.shareBatchNum = shareBatchNum;
        this.shareFilePath = shareFilePath;
    }

    public String getShareBatchNum() {
        return shareBatchNum;
    }

    public void setShareBatchNum(String shareBatchNum) {
        this.shareBatchNum = shareBatchNum;
    }

    public String getShareFilePath() {
        return shareFilePath;
    }

    public void setShareFilePath(String shareFilePath) {
        this.shareFilePath = shareFilePath;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("shareBatchNum", this.shareBatchNum);
        map.put("shareFilePath", this.shareFilePath);
        return map;
    }
}