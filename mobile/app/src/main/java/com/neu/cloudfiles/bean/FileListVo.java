package com.neu.cloudfiles.bean;

public class FileListVo {
    private Long fileId;

    private String timeStampName;

    private String fileUrl;

    private Long fileSize;

    private Integer storageType;

    private Integer pointCount;

    private String identifier;

    private Long userFileId;

    private Long userId;

    private String fileName;

    private String filePath;

    private String extendName;

    private Integer isDir;

    private String uploadTime;

    private Integer deleteFlag;

    private String deleteTime;

    private String deleteBatchNum;

    public FileListVo(Long fileId, String timeStampName, String fileUrl, Long fileSize, Integer storageType, Integer pointCount, String identifier, Long userFileId, Long userId, String fileName, String filePath, String extendName, Integer isDir, String uploadTime, Integer deleteFlag, String deleteTime, String deleteBatchNum) {
        this.fileId = fileId;
        this.timeStampName = timeStampName;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.storageType = storageType;
        this.pointCount = pointCount;
        this.identifier = identifier;
        this.userFileId = userFileId;
        this.userId = userId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.extendName = extendName;
        this.isDir = isDir;
        this.uploadTime = uploadTime;
        this.deleteFlag = deleteFlag;
        this.deleteTime = deleteTime;
        this.deleteBatchNum = deleteBatchNum;
    }

    public FileListVo() {
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getTimeStampName() {
        return timeStampName;
    }

    public void setTimeStampName(String timeStampName) {
        this.timeStampName = timeStampName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getStorageType() {
        return storageType;
    }

    public void setStorageType(Integer storageType) {
        this.storageType = storageType;
    }

    public Integer getPointCount() {
        return pointCount;
    }

    public void setPointCount(Integer pointCount) {
        this.pointCount = pointCount;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Long getUserFileId() {
        return userFileId;
    }

    public void setUserFileId(Long userFileId) {
        this.userFileId = userFileId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getExtendName() {
        return extendName;
    }

    public void setExtendName(String extendName) {
        this.extendName = extendName;
    }

    public Integer getIsDir() {
        return isDir;
    }

    public void setIsDir(Integer isDir) {
        this.isDir = isDir;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(String deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getDeleteBatchNum() {
        return deleteBatchNum;
    }

    public void setDeleteBatchNum(String deleteBatchNum) {
        this.deleteBatchNum = deleteBatchNum;
    }
}
