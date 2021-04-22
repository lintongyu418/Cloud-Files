package com.example.cloud.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "预览文件DTO",required = true)
public class PreviewDo {
    private Long userFileId;
}
