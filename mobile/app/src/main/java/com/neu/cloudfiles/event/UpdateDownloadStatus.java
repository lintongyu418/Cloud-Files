package com.neu.cloudfiles.event;

public class UpdateDownloadStatus {
    private long userFileId;
    private String status;
    private String filePath;

    public UpdateDownloadStatus(long userFileId, String status, String filePath) {
        this.userFileId = userFileId;
        this.status = status;
        this.filePath = filePath;
    }

    public long getUserFileId() {
        return userFileId;
    }

    public void setUserFileId(long userFileId) {
        this.userFileId = userFileId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
