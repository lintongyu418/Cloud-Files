package com.example.cloud.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "校验过期时间DTO",required = true)
public class CheckEndTimeDo {
    @Schema(description="批次号")
    private String shareBatchNum;

}