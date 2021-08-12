package com.example.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cloud.entity.FileBean;
import com.example.cloud.entity.UserFileBean;
import com.example.cloud.mappers.FileMapper;
import com.example.cloud.mappers.StorageMapper;
import com.example.cloud.mappers.UserFileMapper;
import com.example.cloud.service.UserFileService;
import com.example.cloud.vo.FileListVo;
import com.example.core.configs.Constant;
import com.example.core.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserFileServiceImpl extends ServiceImpl<UserFileMapper, UserFileBean> implements UserFileService {
    @Resource
    UserFileMapper userFileMapper;
    @Resource
    FileMapper fileMapper;
    @Resource
    StorageMapper storageMapper;

    public static Executor executor = Executors.newFixedThreadPool(20);

    @Override
    public List<FileListVo> userFileList(UserFileBean userFile, Long beginCount, Long pageCount) {
        return userFileMapper.userFileList(userFile, beginCount, pageCount);
    }

    @Override
    public boolean checkFileExist(Long userId, String fileName, String filePath) {
        String fileExtendName = FileUtil.getFileExtendName(fileName);
        String fileNameWithoutExtend = FileUtil.getFileNameNotExtend(fileName);
        LambdaQueryWrapper<UserFileBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserFileBean::getUserId, userId)
                .eq(UserFileBean::getFileName, fileNameWithoutExtend)
                .eq(UserFileBean::getFilePath, filePath)
                .eq(UserFileBean::getDeleteFlag, 0)
                .eq(UserFileBean::getExtendName, fileExtendName);
        List<UserFileBean> fileBeanList = userFileMapper.selectList(lambdaQueryWrapper);
        return fileBeanList.size() > 0;
    }

    @Override
    public List<FileListVo> selectByExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId) {
        return userFileMapper.selectByExtendNames(fileNameList, beginCount, pageCount, userId);
    }

    @Override
    public Long countFileByExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId) {
        return userFileMapper.countFileByExtendNames(fileNameList, beginCount, pageCount, userId);
    }

    @Override
    public List<FileListVo> selectFileNotInExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId) {
        return userFileMapper.selectFileNotInExtendNames(fileNameList, beginCount, pageCount, userId);
    }

    @Override
    public Long countFileByNotInExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId) {
        return userFileMapper.countFileByNotInExtendNames(fileNameList, beginCount, pageCount, userId);
    }

    @Override
    public List<UserFileBean> selectUserFileByNameAndPath(String fileName, String filePath, Long userId) {
        LambdaQueryWrapper<UserFileBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserFileBean::getFileName, fileName)
                .eq(UserFileBean::getFilePath, filePath)
                .eq(UserFileBean::getUserId, userId)
                .eq(UserFileBean::getDeleteFlag, "0");
        return userFileMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void replaceFolderPath(String filePath, String oldFilePath, Long userId) {
        userFileMapper.replaceFolderPath(filePath, oldFilePath, userId);
    }

    @Override
    public void deleteUserFile(Long userFileId, Long sessionUserId) {
        UserFileBean targetFile = userFileMapper.selectById(userFileId);
        if (targetFile.getIsDir() == 1) {
            String folderPath = targetFile.getFilePath() + targetFile.getFileName() + Constant.FILE_SEPARATOR;
            this.getFolderPath(folderPath);
            this.updateFilePointCountByFilePath(folderPath, sessionUserId);
            long folderSize = userFileMapper.folderSize(sessionUserId, folderPath);
            System.out.println("foldersize " + folderSize);
            storageMapper.updateStorage(-folderSize, sessionUserId);
        } else {
            // 找到 对应的file file count - 1
            FileBean fileBean = fileMapper.selectById(targetFile.getFileId());
            LambdaUpdateWrapper<FileBean> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.set(FileBean::getPointCount, fileBean.getPointCount() - 1)
                    .eq(FileBean::getFileId, fileBean.getFileId());
            fileMapper.update(null, lambdaUpdateWrapper);
            storageMapper.updateStorage(-fileBean.getFileSize(), sessionUserId);
        }
        LambdaQueryWrapper<UserFileBean> userFileLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userFileLambdaQueryWrapper.eq(UserFileBean::getUserFileId, userFileId);
        userFileMapper.delete(userFileLambdaQueryWrapper);
    }

    private void updateFilePointCountByFilePath(String filePath, Long userId) {
        new Thread(() -> {
            List<UserFileBean> fileList = selectFileListLikeRightFilePath(filePath, userId);
            for (UserFileBean userFileTemp : fileList) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (userFileTemp.getIsDir() != 1) {
                            FileBean fileBean = fileMapper.selectById(userFileTemp.getFileId());
                            if (fileBean.getPointCount() != null) {
                                LambdaUpdateWrapper<FileBean> fileBeanLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                                fileBeanLambdaUpdateWrapper.set(FileBean::getPointCount, fileBean.getPointCount() - 1)
                                        .eq(FileBean::getFileId, fileBean.getFileId());
                                fileMapper.update(null, fileBeanLambdaUpdateWrapper);
                            }
                        }
                        LambdaQueryWrapper<UserFileBean> userFileLambdaQueryWrapper = new LambdaQueryWrapper<>();
                        userFileLambdaQueryWrapper.eq(UserFileBean::getUserFileId, userFileTemp.getUserFileId());
                        userFileMapper.delete(userFileLambdaQueryWrapper);
                    }
                });
            }
        }).start();
    }

    private void getFolderPath(String filePath) {
        filePath = filePath.replace("\\", "\\\\\\\\");
        filePath = filePath.replace("'", "\\'");
        filePath = filePath.replace("%", "\\%");
        filePath = filePath.replace("_", "\\_");
    }

    @Override
    public List<UserFileBean> selectFileListLikeRightFilePath(String filePath, long userId) {
        // important 这里 做这些是为了啥？处理转义字符
        this.getFolderPath(filePath);

        LambdaQueryWrapper<UserFileBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        log.info("查询文件路径：" + filePath);
        lambdaQueryWrapper.eq(UserFileBean::getUserId, userId)
                .likeRight(UserFileBean::getFilePath, filePath)
                .eq(UserFileBean::getDeleteFlag, 0);
        List<UserFileBean> list = userFileMapper.selectList(lambdaQueryWrapper);
        for (UserFileBean bean : list) {
            System.out.println(bean.toString());
            System.out.println("***********************************");
        }
        return list;
    }

    @Override
    public void doTest() {
        LambdaQueryWrapper<UserFileBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserFileBean::getUserId, 7)
                .likeRight(UserFileBean::getFilePath, "/aaa")
                .eq(UserFileBean::getDeleteFlag, 0);
        List<UserFileBean> list = userFileMapper.selectList(lambdaQueryWrapper);
        for (UserFileBean bean : list) {
            System.out.println(bean.toString());
            System.out.println("***********************************");
        }
    }

    @Override
    public void updateFilePathByFilePath(String oldFilePath, String newFilePath, String fileName, String extendName, long userId) {
        if ("null".equals(extendName)){
            extendName = null;
        }
        //移动根目录
        userFileMapper.updateFilePathByPathAndName(oldFilePath, newFilePath, fileName, extendName, userId);
        //移动子目录
        oldFilePath = oldFilePath + fileName + "/";
        newFilePath = newFilePath + fileName + "/";
        oldFilePath = oldFilePath.replace("\\", "\\\\\\\\");
        oldFilePath = oldFilePath.replace("'", "\\'");
        oldFilePath = oldFilePath.replace("%", "\\%");
        oldFilePath = oldFilePath.replace("_", "\\_");
        if (extendName == null) { //为null说明是目录，则需要移动子目录
            userFileMapper.updateFilePathByFilePath(oldFilePath, newFilePath, userId);
        }
    }

    @Override
    public List<UserFileBean> selectFilePathTreeByUserId(Long userId) {
        LambdaQueryWrapper<UserFileBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserFileBean::getUserId, userId)
                .eq(UserFileBean::getIsDir, 1)
                .eq(UserFileBean::getDeleteFlag, 0);
        return userFileMapper.selectList(lambdaQueryWrapper);
    }
}
