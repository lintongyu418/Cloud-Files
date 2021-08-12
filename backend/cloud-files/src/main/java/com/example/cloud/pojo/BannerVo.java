package com.example.cloud.pojo;

import lombok.Data;

@Data
public class BannerVo {
    private int id;
    private String url;
    private String imagePath;
    private String title;
    private String desc;
    private int isVisible;
    private int order;
    private int type;
}
