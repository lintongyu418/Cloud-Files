package com.example.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.cloud.pojo.ShareFileBean;
import com.example.cloud.vo.ShareFileListVo;

import java.util.List;

public interface ShareFileService extends IService<ShareFileBean> {
    void batchInsertShareFile(List<ShareFileBean> shareFiles);

    List<ShareFileListVo> selectShareFileList(String shareBatchNum, String filePath);
}
