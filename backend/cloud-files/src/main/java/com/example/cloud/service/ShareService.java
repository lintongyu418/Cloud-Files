package com.example.cloud.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.cloud.entity.ShareBean;
import com.example.cloud.pojo.ShareListDo;
import com.example.cloud.vo.ShareListVo;

import java.util.List;

public interface ShareService extends IService<ShareBean> {

    List<ShareListVo> selectShareList(ShareListDo shareListDTO, Long userId);

    int selectShareListTotalCount(ShareListDo shareListDTO, Long userId);
}
