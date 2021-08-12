package com.example.cloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cloud.entity.ShareBean;
import com.example.cloud.mappers.ShareMapper;
import com.example.cloud.pojo.ShareListDo;
import com.example.cloud.service.ShareService;
import com.example.cloud.vo.ShareListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor= Exception.class)
public class ShareServiceImpl extends ServiceImpl<ShareMapper, ShareBean> implements ShareService {
    @Resource
    ShareMapper shareMapper;

    @Override
    public List<ShareListVo> selectShareList(ShareListDo shareListDTO, Long userId) {
        long beginCount = (shareListDTO.getCurrentPage() - 1) * shareListDTO.getPageCount();
        return shareMapper.selectShareList(shareListDTO.getShareFilePath(),
                shareListDTO.getShareBatchNum(),
                beginCount, shareListDTO.getPageCount(), userId);
    }

    @Override
    public int selectShareListTotalCount(ShareListDo shareListDTO, Long userId) {
        return shareMapper.selectShareListTotalCount(shareListDTO.getShareFilePath(), shareListDTO.getShareBatchNum(), userId);
    }
}
