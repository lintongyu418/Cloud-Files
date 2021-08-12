package com.example.cloud.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "创建文件Do",required = true)
public class CreateFolderDo {
    @Schema(description="文件名")
    private String fileName;
    @Schema(description="文件路径")
    private String filePath;
}