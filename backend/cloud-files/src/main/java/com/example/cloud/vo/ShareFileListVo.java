package com.example.cloud.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "分享文件列表VO")
@Data
public class ShareFileListVo {
    @Schema(description = "文件id")
    private Long fileId;
    @Schema(description = "文件时间戳姓名")
    private String timeStampName;
    @Schema(description = "文件url")
    private String fileUrl;
    @Schema(description = "文件大小")
    private Long fileSize;
    @Schema(description = "是否sso存储")
    @Deprecated
    private Integer isOSS;
    @Schema(description = "存储类型")
    private Integer storageType;
    @Schema(description = "用户文件id")
    private Long userFileId;
//
    private Long userId;

    @Schema(description = "文件名")
    private String fileName;
    @Schema(description = "文件路径")
    private String filePath;
    @Schema(description = "文件扩展名")
    private String extendName;
    @Schema(description = "是否是目录 0-否， 1-是")
    private Integer isDir;
    @Schema(description = "上传时间")
    private String uploadTime;
    @Schema(description = "分享文件路径")
    private String shareFilePath;
//
    private Long shareId;
//
    @Schema(description = "分享时间")
    private String shareTime;
    @Schema(description = "过期时间")
    private String endTime;
    @Schema(description = "提取密码")
    private String extractionCode;
    @Schema(description = "分享链接")
    private String shareBatchNum;
    private Integer shareType;//0公共，1私密，2好友
    private Integer shareStatus;//0正常，1已失效，2已撤销
    private String username;
}
