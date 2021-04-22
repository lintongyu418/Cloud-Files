package com.example.cloud.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "下载文件", required = true)
public class DownloadFileDo {
    private Long userFileId;
}
