package com.example.cloud.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.cloud.entity.ShareBean;
import com.example.cloud.vo.ShareListVo;

import java.util.List;

public interface ShareMapper extends BaseMapper<ShareBean> {
    List<ShareListVo> selectShareList(String shareFilePath, String shareBatchNum, Long beginCount, Long pageCount, Long userId);
    int selectShareListTotalCount(String shareFilePath,String shareBatchNum, Long userId);
}
