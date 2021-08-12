package com.neu.cloudfiles.bean;

import android.graphics.Bitmap;

public class ShareFileVo {
    // @Schema(description = "批次号")
    private String shareBatchNum;
    // @Schema(description = "提取编码")
    private String extractionCode;

    private Bitmap fileQR = null;

    public ShareFileVo(String shareBatchNum, String extractionCode) {
        this.shareBatchNum = shareBatchNum;
        this.extractionCode = extractionCode;
    }

    public String getShareBatchNum() {
        return shareBatchNum;
    }

    public void setShareBatchNum(String shareBatchNum) {
        this.shareBatchNum = shareBatchNum;
    }

    public String getExtractionCode() {
        return extractionCode;
    }

    public void setExtractionCode(String extractionCode) {
        this.extractionCode = extractionCode;
    }

    public Bitmap getFileQR() {
        return fileQR;
    }

    public void setFileQR(Bitmap fileQR) {
        this.fileQR = fileQR;
    }
}