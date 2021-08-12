package com.neu.cloudfiles.bean;

public class FileListDo {
    private String filePath;
    private Long currentPage;
    private Long pageCount;
    private int fileType;

    public FileListDo(String filePath, Long currentPage, Long pageCount, int fileType) {
        this.filePath = filePath;
        this.currentPage = currentPage;
        this.pageCount = pageCount;
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }

    public Long getPageCount() {
        return pageCount;
    }

    public void setPageCount(Long pageCount) {
        this.pageCount = pageCount;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }
}
