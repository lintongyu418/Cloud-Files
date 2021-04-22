package com.example.cloud.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "用户注册data object", required = true)
public class RegisterDo {
    @Schema(description = "用户名")
    private String username;

    @Schema(description = "手机号")
    private String telephone;

    @Schema(description = "password")
    private String password;
}
