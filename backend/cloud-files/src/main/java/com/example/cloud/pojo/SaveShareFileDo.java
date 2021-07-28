package com.example.cloud.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "保存分享文件DTO",required = true)
public class SaveShareFileDo {
    @Schema(description="文件集合", example = "[{\"userFileId\":12},{\"userFileId\":13}]")
    private Long userFileId;
    @Schema(description = "文件路径")
    private String filePath;
}
