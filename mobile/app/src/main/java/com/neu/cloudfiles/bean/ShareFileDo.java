package com.neu.cloudfiles.bean;

public class ShareFileDo {
    // @Schema(description = "文件集合")
    private String files;
    // @Schema(description = "过期日期", example = "2020-05-23 22:10:33")
    private String endTime;
    // @Schema(description = "分享类型", example = "0公共分享，1私密分享，2好友分享")
    private Integer shareType;
    // @Schema(description = "备注", example = "")
    private String remarks;

    public ShareFileDo(String files, String endTime, Integer shareType, String remarks) {
        this.files = files;
        this.endTime = endTime;
        this.shareType = shareType;
        this.remarks = remarks;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getShareType() {
        return shareType;
    }

    public void setShareType(Integer shareType) {
        this.shareType = shareType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}