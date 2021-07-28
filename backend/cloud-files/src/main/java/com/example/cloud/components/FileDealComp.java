package com.example.cloud.components;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cloud.entity.UserFileBean;
import com.example.cloud.mappers.UserFileMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class FileDealComp {
    @Resource
    UserFileMapper userFileMapper;

    public String getRepeatFileName(UserFileBean userFile, String savefilePath) {
        String fileName = userFile.getFileName();
        String extendName = userFile.getExtendName();
        Integer deleteFlag = userFile.getDeleteFlag();
        Long userId = userFile.getUserId();
        LambdaQueryWrapper<UserFileBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserFileBean::getFilePath, savefilePath)
                .eq(UserFileBean::getDeleteFlag, deleteFlag)
                .eq(UserFileBean::getUserId, userId)
                .eq(UserFileBean::getFileName, fileName);
        if (userFile.getIsDir() == 0) {
            lambdaQueryWrapper.eq(UserFileBean::getExtendName, extendName);
        }
        List<UserFileBean> list = userFileMapper.selectList(lambdaQueryWrapper);
        if (list == null) {
            return fileName;
        }
        if (list.isEmpty()) {
            return fileName;
        }
        int i = 0;

        while (list != null && !list.isEmpty()) {
            i++;
            LambdaQueryWrapper<UserFileBean> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(UserFileBean::getFilePath, savefilePath)
                    .eq(UserFileBean::getDeleteFlag, deleteFlag)
                    .eq(UserFileBean::getUserId, userId)
                    .eq(UserFileBean::getFileName, fileName + "(" + i + ")");
            if (userFile.getIsDir() == 0) {
                lambdaQueryWrapper1.eq(UserFileBean::getExtendName, extendName);
            }
            list = userFileMapper.selectList(lambdaQueryWrapper1);
        }

        return fileName + "(" + i + ")";

    }
}
