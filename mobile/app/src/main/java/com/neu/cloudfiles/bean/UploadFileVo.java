package com.neu.cloudfiles.bean;

import java.util.List;

public class UploadFileVo {

    private String timeStampName;
    private boolean skipUpload;
    private boolean needMerge;
    private List<Integer> uploaded;

    public UploadFileVo(String timeStampName, boolean skipUpload, boolean needMerge, List<Integer> uploaded) {
        this.timeStampName = timeStampName;
        this.skipUpload = skipUpload;
        this.needMerge = needMerge;
        this.uploaded = uploaded;
    }

    public String getTimeStampName() {
        return timeStampName;
    }

    public void setTimeStampName(String timeStampName) {
        this.timeStampName = timeStampName;
    }

    public boolean isSkipUpload() {
        return skipUpload;
    }

    public void setSkipUpload(boolean skipUpload) {
        this.skipUpload = skipUpload;
    }

    public boolean isNeedMerge() {
        return needMerge;
    }

    public void setNeedMerge(boolean needMerge) {
        this.needMerge = needMerge;
    }

    public List<Integer> getUploaded() {
        return uploaded;
    }

    public void setUploaded(List<Integer> uploaded) {
        this.uploaded = uploaded;
    }
}