package com.neu.cloudfiles.bean;

import com.neu.cloudfiles.constant.Constant;

public class DownloadFileVo {
    private String fileName;
    private int imgId;
    private long userFileId;
    private float percentage;
    private String status;
    private String filePath;
    private long fileSize;

    public DownloadFileVo(String fileName, int imgId, long userFileId, float percentage, String status, long fileSize, String filePath) {
        this.fileName = fileName;
        this.imgId = imgId;
        this.userFileId = userFileId;
        this.percentage = percentage;
        this.status = status;
        this.fileSize = fileSize;
        this.filePath = filePath;
    }

    public DownloadFileVo(String fileName, int imgId, long userFileId, long fileSize) {
        this.fileName = fileName;
        this.imgId = imgId;
        this.userFileId = userFileId;
        this.percentage = 0;
        this.status = Constant.DOWNLOAD_ING;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public long getUserFileId() {
        return userFileId;
    }

    public void setUserFileId(long userFileId) {
        this.userFileId = userFileId;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
