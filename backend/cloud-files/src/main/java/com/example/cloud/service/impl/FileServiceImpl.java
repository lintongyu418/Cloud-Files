package com.example.cloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cloud.entity.FileBean;
import com.example.cloud.mappers.FileMapper;
import com.example.cloud.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@org.springframework.stereotype.Service
@Transactional(rollbackFor = Exception.class)
public class FileServiceImpl extends ServiceImpl<FileMapper, FileBean> implements FileService {

    @Resource
    FileMapper fileMapper;

    @Override
    public void increaseFilePointCount(Long fileId) {
        FileBean fileBean = fileMapper.selectById(fileId);
        if (fileBean == null) {
            log.error("文件不存在，fileId : {}", fileId );
            return;
        }
        fileBean.setPointCount(fileBean.getPointCount()+1);
        fileMapper.updateById(fileBean);
    }
}
