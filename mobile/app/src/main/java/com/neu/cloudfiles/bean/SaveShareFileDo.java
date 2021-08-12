package com.neu.cloudfiles.bean;

public class SaveShareFileDo {
    // @Schema(description="文件集合", example = "[{\"userFileId\":12},{\"userFileId\":13}]")
    private Long userFileId;
    // @Schema(description = "文件路径")
    private String filePath;

    public SaveShareFileDo(Long userFileId, String filePath) {
        this.userFileId = userFileId;
        this.filePath = filePath;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getUserFileId() {
        return userFileId;
    }

    public void setUserFileId(Long userFileId) {
        this.userFileId = userFileId;
    }
}