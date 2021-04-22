package com.example.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.cloud.entity.FileBean;

public interface FileService extends IService<FileBean> {
    void increaseFilePointCount(Long fileId);
}
