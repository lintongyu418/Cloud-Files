package com.example.cloud.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description="分享类型VO")
public class ShareTypeVo {
    @Schema(description="0公共，1私密，2好友")
    private Integer shareType;
}
