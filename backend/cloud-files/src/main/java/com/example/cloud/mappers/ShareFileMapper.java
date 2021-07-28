package com.example.cloud.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.cloud.pojo.ShareFileBean;
import com.example.cloud.vo.ShareFileListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShareFileMapper extends BaseMapper<ShareFileBean> {
    void batchInsertShareFile(List<ShareFileBean> shareFiles);
    List<ShareFileListVo> selectShareFileList(@Param("shareBatchNum") String shareBatchNum, @Param("shareFilePath") String filePath);
    List<ShareFileListVo> queryFileShares(@Param("userFileId") long userFileId);
    List<ShareFileListVo> queryAllShares();
}
