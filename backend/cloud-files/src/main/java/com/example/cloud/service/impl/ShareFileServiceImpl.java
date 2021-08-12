package com.example.cloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cloud.mappers.ShareFileMapper;
import com.example.cloud.mappers.UserFileMapper;
import com.example.cloud.pojo.ShareFileBean;
import com.example.cloud.service.ShareFileService;
import com.example.cloud.vo.ShareFileListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor= Exception.class)
public class ShareFileServiceImpl extends ServiceImpl<ShareFileMapper, ShareFileBean> implements ShareFileService {
    @Resource
    ShareFileMapper shareFileMapper;
    @Resource
    UserFileMapper userFileMapper;
    @Override
    public void batchInsertShareFile(List<ShareFileBean> shareFiles) {
        shareFileMapper.batchInsertShareFile(shareFiles);
    }

    @Override
    public List<ShareFileListVo> selectShareFileList(String shareBatchNum, String filePath) {
        return shareFileMapper.selectShareFileList(shareBatchNum, filePath);
    }

    @Override
    public List<ShareFileListVo> queryFileShares(long userFileId) {
        return shareFileMapper.queryFileShares(userFileId);
    }

    @Override
    public List<ShareFileListVo> queryAllShares() {
        return shareFileMapper.queryAllShares();
    }

}
