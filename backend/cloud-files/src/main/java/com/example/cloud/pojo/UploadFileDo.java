package com.example.cloud.pojo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(name = "上传文件", required = true)
public class UploadFileDo {
    @Schema(description = "文件路径")
    private String filePath;

    /**
     * 上传时间
     */
    @Schema(description = "上传时间")
    private String uploadTime;

    /**
     * 扩展名
     */
    @Schema(description = "扩展名")
    private String extendName;


    @Schema(description = "文件名")
    private String filename;

    /**
     * 文件大小
     */
    @Schema(description = "文件大小")
    private Long fileSize;

    @Schema(description = "切片数量")
    private int chunkNumber;

    @Schema(description = "切片大小")
    private long chunkSize;

    @Schema(description = "所有切片")
    private int totalChunks;

    @Schema(description = "总大小")
    private long totalSize;

    @Schema(description = "当前切片大小")
    private long currentChunkSize;

    @Schema(description = "md5码")
    private String identifier;

}
