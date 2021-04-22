package com.example.cloud.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.cloud.entity.StorageBean;

public interface StorageMapper extends BaseMapper<StorageBean> {
    void updateStorage(Long size, Long userId);
}
