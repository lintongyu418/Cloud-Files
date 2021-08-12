package com.neu.cloudfiles.bean;

public class ShareFileListVo {
    // @Schema(description = "文件id")
    private Long fileId;
    // @Schema(description = "文件时间戳姓名")
    private String timeStampName;
    // @Schema(description = "文件url")
    private String fileUrl;
    // @Schema(description = "文件大小")
    private Long fileSize;
    // @Schema(description = "是否sso存储")
    @Deprecated
    private Integer isOSS;
    // @Schema(description = "存储类型")
    private Integer storageType;
    // @Schema(description = "用户文件id")
    private Long userFileId;
    //
    private Long userId;

    // @Schema(description = "文件名")
    private String fileName;
    // @Schema(description = "文件路径")
    private String filePath;
    // @Schema(description = "文件扩展名")
    private String extendName;
    // @Schema(description = "是否是目录 0-否， 1-是")
    private Integer isDir;
    // @Schema(description = "上传时间")
    private String uploadTime;
    // @Schema(description = "分享文件路径")
    private String shareFilePath;
    //
    private Long shareId;
    //
    // @Schema(description = "分享时间")
    private String shareTime;
    // @Schema(description = "过期时间")
    private String endTime;
    // @Schema(description = "提取密码")
    private String extractionCode;
    // @Schema(description = "分享链接")
    private String shareBatchNum;
    private Integer shareType;//0公共，1私密，2好友
    private Integer shareStatus;//0正常，1已失效，2已撤销
    private String username;

    public ShareFileListVo(Long fileId, String timeStampName, String fileUrl, Long fileSize, Integer isOSS, Integer storageType, Long userFileId, Long userId, String fileName, String filePath, String extendName, Integer isDir, String uploadTime, String shareFilePath, Long shareId, String shareTime, String endTime, String extractionCode, String shareBatchNum, Integer shareType, Integer shareStatus, String username) {
        this.fileId = fileId;
        this.timeStampName = timeStampName;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.isOSS = isOSS;
        this.storageType = storageType;
        this.userFileId = userFileId;
        this.userId = userId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.extendName = extendName;
        this.isDir = isDir;
        this.uploadTime = uploadTime;
        this.shareFilePath = shareFilePath;
        this.shareId = shareId;
        this.shareTime = shareTime;
        this.endTime = endTime;
        this.extractionCode = extractionCode;
        this.shareBatchNum = shareBatchNum;
        this.shareType = shareType;
        this.shareStatus = shareStatus;
        this.username = username;
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

    public Integer getIsOSS() {
        return isOSS;
    }

    public void setIsOSS(Integer isOSS) {
        this.isOSS = isOSS;
    }

    public Integer getStorageType() {
        return storageType;
    }

    public void setStorageType(Integer storageType) {
        this.storageType = storageType;
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

    public String getShareFilePath() {
        return shareFilePath;
    }

    public void setShareFilePath(String shareFilePath) {
        this.shareFilePath = shareFilePath;
    }

    public Long getShareId() {
        return shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }

    public String getShareTime() {
        return shareTime;
    }

    public void setShareTime(String shareTime) {
        this.shareTime = shareTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getExtractionCode() {
        return extractionCode;
    }

    public void setExtractionCode(String extractionCode) {
        this.extractionCode = extractionCode;
    }

    public String getShareBatchNum() {
        return shareBatchNum;
    }

    public void setShareBatchNum(String shareBatchNum) {
        this.shareBatchNum = shareBatchNum;
    }

    public Integer getShareType() {
        return shareType;
    }

    public void setShareType(Integer shareType) {
        this.shareType = shareType;
    }

    public Integer getShareStatus() {
        return shareStatus;
    }

    public void setShareStatus(Integer shareStatus) {
        this.shareStatus = shareStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
