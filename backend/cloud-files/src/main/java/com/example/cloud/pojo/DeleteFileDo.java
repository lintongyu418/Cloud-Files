package com.example.cloud.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "删除文件DTO", required = true)
public class DeleteFileDo {
    @Schema(description = "用户文件id")
    private Long userFileId;
}
