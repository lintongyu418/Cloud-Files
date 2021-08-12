package com.neu.cloudfiles.bean;

public class DownloadFileDo {

    private String fileName;

    private int imgId;
    private long userFileId;

    private long fileSize;

    public DownloadFileDo(String fileName, int imgId, long userFileId, long fileSize) {
        this.fileName = fileName;
        this.imgId = imgId;
        this.userFileId = userFileId;
        this.fileSize = fileSize;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getUserFileId() {
        return userFileId;
    }

    public void setUserFileId(long userFileId) {
        this.userFileId = userFileId;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

}
