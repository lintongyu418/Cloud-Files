package com.example.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.cloud.entity.UserFileBean;
import com.example.cloud.pojo.UploadFileDo;
import com.example.cloud.vo.FileListVo;

import java.util.List;

public interface UserFileService extends IService<UserFileBean> {
    List<FileListVo> userFileList(UserFileBean userFile, Long beginCount, Long pageCount);

    boolean checkFileExist(Long userId, String fileName, String filePath);

    boolean checkFileExist(Long userId, String fileNameWithoutExtend, String fileExtendName, String filePath);

    List<FileListVo> selectByExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    Long countFileByExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    List<FileListVo> selectFileNotInExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    Long countFileByNotInExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    List<UserFileBean> selectUserFileByNameAndPath(String fileName, String filePath, Long userId);

    void replaceFolderPath(String filePath, String oldFilePath, Long userId);

    void deleteUserFile(Long userFileId, Long sessionUserId);

    List<UserFileBean> selectFileListLikeRightFilePath(String filePath, long userId);

    void doTest();

    void updateFilePathByFilePath(String oldFilePath, String newFilePath, String fileName, String extendName, long userId);

    List<UserFileBean> selectFilePathTreeByUserId(Long userId);
}
