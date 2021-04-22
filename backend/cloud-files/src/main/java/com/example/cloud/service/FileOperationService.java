package com.example.cloud.service;

import com.example.cloud.entity.FileBean;
import com.example.cloud.entity.StorageBean;
import com.example.cloud.pojo.DownloadFileDo;
import com.example.cloud.pojo.UploadFileDo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FileOperationService {
    /**
     * note ？？？
     */
    StorageBean selectStorage(StorageBean storage);

    /**
     * note ???
     */
    void insertStorage(StorageBean storage);

    /**
     * note ???
     */
    void updateStorage(StorageBean storage);

    /**
     * note ???
     */
    Long selectStorageSizeByUserId(Long userId);

    /**
     * note ???
     */
    StorageBean selectStorageByUser(StorageBean storageBean);

    /**
     * 上传文件
     *
     * @param request      请求
     * @param UploadFileDo 文件信息
     */
    void uploadFile(HttpServletRequest request, UploadFileDo UploadFileDo, Long userId);

    /**
     * 下载文件
     *
     * @param httpServletResponse 请求
     * @param downloadFileDo      文件信息
     */
    void downloadFile(HttpServletResponse httpServletResponse, DownloadFileDo downloadFileDo);

    /**
     * 删除文件
     *
     * @param fileBean 问价信息
     */
    void deleteFile(FileBean fileBean);

    StorageBean selectStorageBean(StorageBean storageBean);

    void insertStorageBean(StorageBean storageBean);

    void updateStorageBean(StorageBean storageBean);

}
