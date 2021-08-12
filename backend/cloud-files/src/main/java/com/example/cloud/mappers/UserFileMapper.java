package com.example.cloud.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.cloud.entity.UserFileBean;
import com.example.cloud.vo.FileListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFileMapper extends BaseMapper<UserFileBean> {
    List<FileListVo> userFileList(UserFileBean userFile, Long beginCount, Long pageCount);

    FileListVo checkFileExist(Long userId, String fileIdentifier);

    Long folderSize(@Param("userId") Long userId, String folderPath);

    Long selectStorageSizeByUserId(@Param("userId") Long userId);

    List<FileListVo> selectByExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    Long countFileByExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    List<FileListVo> selectFileNotInExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    Long countFileByNotInExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    boolean selectUserFileByNameAndPath(String fileName, String filePath, Long userId);

    void replaceFolderPath(String filePath, String oldFilePath, Long userId);

    void updateFilePathByPathAndName(String oldFilePath, String newFilePath, String fileName, String extendName, long userId);
    void updateFilePathByFilePath(String oldFilePath, String newFilePath, long userId);
}
