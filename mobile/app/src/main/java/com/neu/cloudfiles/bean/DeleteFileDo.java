package com.neu.cloudfiles.bean;

public class DeleteFileDo {
    private Long userFileId;

    public DeleteFileDo(Long userFileId) {
        this.userFileId = userFileId;
    }

    public Long getUserFileId() {
        return userFileId;
    }

    public void setUserFileId(Long userFileId) {
        this.userFileId = userFileId;
    }
}
