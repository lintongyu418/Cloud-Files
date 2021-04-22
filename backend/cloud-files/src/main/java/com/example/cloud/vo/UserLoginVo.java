package com.example.cloud.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "用户登录Vo", required = true)
public class UserLoginVo {
    @Schema(description = "用户id", example = "1")
    private long userId;
    @Schema(description = "用户名", example = "nihao")
    private String username;
    @Schema(description = "手机号", example = "13429903248")
    private String telephone;
    @Schema(description = "邮箱", example = "116****483@qq.com")
    private String email;
    @Schema(description = "性别", example = "男")
    private String sex;
    @Schema(description = "生日", example = "1994-05-06")
    private String birthday;
    @Schema(description = "用户头像地址", example = "\\upload\\20200405\\93811586079860974.png")
    private String imageUrl;
    @Schema(description = "注册时间", example = "2019-12-23 14:21:52")
    private String registerTime;
    @Schema(description = "最后登录时间", example = "2019-12-23 14:21:52")
    private String lastLoginTime;
    @Schema(description = "Token 接口访问凭证")
    private String token;
}
