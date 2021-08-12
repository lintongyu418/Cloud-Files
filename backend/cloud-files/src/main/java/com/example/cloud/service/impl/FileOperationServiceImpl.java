package com.example.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.cloud.entity.FileBean;
import com.example.cloud.entity.StorageBean;
import com.example.cloud.entity.UserFileBean;
import com.example.cloud.mappers.FileMapper;
import com.example.cloud.mappers.StorageMapper;
import com.example.cloud.mappers.UserFileMapper;
import com.example.cloud.pojo.DownloadFileDo;
import com.example.cloud.pojo.UploadFileDo;
import com.example.cloud.service.FileOperationService;
import com.example.cloud.service.UserFileService;
import com.example.core.configs.Constant;
import com.example.core.exception.UploadException;
import com.example.core.file.dto.DownloadFile;
import com.example.core.file.dto.UploadFile;
import com.example.core.file.ops.Downloader;
import com.example.core.file.ops.FileOperationFactory;
import com.example.core.file.ops.Uploader;

import com.example.core.utils.DateUtil;
import com.example.core.utils.PathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cn.hutool.core.bean.BeanUtil;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FileOperationServiceImpl implements FileOperationService {

    @Resource
    FileOperationFactory localStorageOperationFactory;

    @Resource
    FileMapper fileMapper;

    @Resource
    UserFileMapper userFileMapper;

    @Resource
    StorageMapper storageMapper;

    @Override
    public void uploadFile(HttpServletRequest request, UploadFileDo uploadFileDo, Long userId) {
        Uploader uploader = null;
        UploadFile uploadFile = new UploadFile();
        uploadFile.setChunkNumber(uploadFileDo.getChunkNumber());
        uploadFile.setChunkSize(uploadFileDo.getChunkSize());
        uploadFile.setTotalChunks(uploadFileDo.getTotalChunks());
        uploadFile.setIdentifier(uploadFileDo.getIdentifier());
        uploadFile.setTotalSize(uploadFileDo.getTotalSize());
        uploadFile.setCurrentChunkSize(uploadFileDo.getCurrentChunkSize());

        String storageType = Constant.DEFAULT_STORAGE_TYPE;
        synchronized (FileOperationService.class) {
            switch (storageType) {
                case "local":
                    uploader = localStorageOperationFactory.getUploader();
                    break;
                case "fastfds":
                    break;
                default:
                    break;
            }
        }
        if (uploader == null) {
            log.error("上传失败，请检查storageType是否配置正确，当前storageType为：" + storageType);
            throw new UploadException("没有制定存储类型");
        }
        List<UploadFile> uploadFileList = uploader.upload(request, uploadFile);
        for (UploadFile uploadedFile : uploadFileList) {
            FileBean fileBean = new FileBean();
            BeanUtil.copyProperties(uploadFileDo, fileBean);
            fileBean.setTimeStampName(uploadedFile.getTimeStampName());
            if (uploadedFile.getSuccess() == 1) {
                fileBean.setFileUrl(uploadedFile.getUrl());
                fileBean.setFileSize(uploadedFile.getFileSize());

                fileBean.setStorageType(uploadedFile.getStorageType());
                fileBean.setPointCount(1);
                fileMapper.insert(fileBean);

                UserFileBean userFile = new UserFileBean();
                userFile.setFileId(fileBean.getFileId());
                userFile.setExtendName(uploadedFile.getFileType());
                userFile.setFileName(uploadedFile.getFileName());
                userFile.setFilePath(uploadFileDo.getFilePath());
                userFile.setDeleteFlag(0);
                userFile.setUserId(userId);
                userFile.setIsDir(0);
                userFile.setUploadTime(DateUtil.getCurrentTime());
                userFileMapper.insert(userFile);
                synchronized (FileOperationService.class) {
                    StorageBean storageBean = selectStorage(new StorageBean(userId));
                    if (storageBean == null) {
                        StorageBean storage = new StorageBean(userId);
                        storage.setStorageSize(fileBean.getFileSize());
                        insertStorage(storage);
                    } else {
                        storageBean.setStorageSize(storageBean.getStorageSize() + uploadedFile.getFileSize());
                        updateStorage(storageBean);
                    }
                }
            }
        }
    }

    @Override
    public void downloadFile(HttpServletResponse httpServletResponse, DownloadFileDo downloadFileDo) {
        Downloader downloader = localStorageOperationFactory.getDownloader();
        if (downloader == null) {
            log.error("not downloader is specified");
            throw new UploadException("failed to download");
        }
        UserFileBean userFileBean = userFileMapper.selectById(downloadFileDo.getUserFileId());
        if (userFileBean.getIsDir() == 0) {
            // 单个文件直接下载
            FileBean fileBean = fileMapper.selectById(userFileBean.getFileId());
            DownloadFile downloadFile = new DownloadFile();
            downloadFile.setFileUrl(fileBean.getFileUrl());
            downloader.download(httpServletResponse, downloadFile);
        } else {
            // 下载整个文件夹
            LambdaQueryWrapper<UserFileBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper
                    .likeRight(UserFileBean::getFilePath, userFileBean.getFilePath() + userFileBean.getFileName() + Constant.FILE_SEPARATOR)
                    .eq(UserFileBean::getUserId, userFileBean.getUserId())
                    .eq(UserFileBean::getIsDir, 0)
                    .eq(UserFileBean::getDeleteFlag, 0);
            List<UserFileBean> userFileList = userFileMapper.selectList(lambdaQueryWrapper);
            String staticPath = PathUtil.getStaticPath();
            String tempPath = staticPath + "temp" + Constant.FILE_SEPARATOR;
            File tempFile = new File(tempPath);
            if (!tempFile.exists()) {
                boolean result = tempFile.mkdirs();
            }
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(tempPath + userFileBean.getFileName() + ".zip");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            CheckedOutputStream checkedOutputStream = new CheckedOutputStream(fileOutputStream, new Adler32());
            ZipOutputStream zipOutputStream = new ZipOutputStream(checkedOutputStream);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(zipOutputStream);
            for (UserFileBean fileBean : userFileList) {
                FileBean currentFileBean = fileMapper.selectById(fileBean.getFileId());
                DownloadFile downloadFile = new DownloadFile();
                downloadFile.setFileUrl(currentFileBean.getFileUrl());
                InputStream inputStream = downloader.getInputStream(downloadFile);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                try {
                    String fileName = fileBean.getFilePath().replace(userFileBean.getFilePath(), "/");
                    fileName += fileBean.getFileName() + "." + fileBean.getExtendName();
                    zipOutputStream.putNextEntry(new ZipEntry(fileName));
                    byte[] buffer = new byte[1024];
                    int i = bufferedInputStream.read(buffer);
                    while (i != -1) {
                        bufferedOutputStream.write(buffer, 0, i);
                        i = bufferedInputStream.read(buffer);
                    }
                } catch (IOException e) {
                    log.error("读取文件失败，创建压缩文件失败");
                    e.printStackTrace();
                } finally {
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        bufferedOutputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            downloader = localStorageOperationFactory.getDownloader();
            DownloadFile downloadFile = new DownloadFile();
            downloadFile.setFileUrl("temp" + Constant.FILE_SEPARATOR + userFileBean.getFileName() + ".zip");
            downloader.download(httpServletResponse, downloadFile);
        }
    }

    @Override
    public void deleteFile(FileBean file) {

    }

    @Override
    public StorageBean selectStorageBean(StorageBean storageBean) {
        return null;
    }

    @Override
    public void insertStorageBean(StorageBean storageBean) {

    }

    @Override
    public void updateStorageBean(StorageBean storageBean) {

    }

    @Override
    public StorageBean selectStorage(StorageBean storageBean) {
        LambdaQueryWrapper<StorageBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StorageBean::getUserId, storageBean.getUserId());
        return storageMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public void insertStorage(StorageBean storageBean) {
        storageMapper.insert(storageBean);
    }

    @Override
    public void updateStorage(StorageBean storageBean) {
        LambdaUpdateWrapper<StorageBean> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(StorageBean::getStorageSize, storageBean.getStorageSize())
                .eq(StorageBean::getStorageId, storageBean.getStorageId())
                .eq(StorageBean::getUserId, storageBean.getUserId());
        storageMapper.update(null, lambdaUpdateWrapper);
    }

    @Override
    public Long selectStorageSizeByUserId(Long userId) {
        return userFileMapper.selectStorageSizeByUserId(userId);
    }

    @Override
    public StorageBean selectStorageByUser(StorageBean storageBean) {
        LambdaQueryWrapper<StorageBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StorageBean::getUserId, storageBean.getUserId());
        return storageMapper.selectOne(lambdaQueryWrapper);
    }
}
