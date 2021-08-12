package com.example.cloud.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;

@Data
@Schema(name = "User Vo",required = true)
public class UserVo {

    private String username;

    private String telephone;

    private String email;

    private String sex;

    private String birthday;

    @Column(columnDefinition = "varchar(100)")
    private String imageUrl;

    @Column(columnDefinition = "varchar(30)")
    private String registerTime;

}