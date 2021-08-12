package com.example.core.file.ops.impls;

import com.example.core.file.ops.Deleter;
import com.example.core.file.ops.Downloader;
import com.example.core.file.ops.FileOperationFactory;
import com.example.core.file.ops.Uploader;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LocalStorageOperationFactory implements FileOperationFactory {

    @Resource
    LocalStorageUploader localStorageUploader;

    @Resource
    LocalStorageDownloader localStorageDownloader;

    @Resource
    LocalStorageDeleter localStorageDeleter;

    @Override
    public Uploader getUploader() {
        return localStorageUploader;
    }

    @Override
    public Downloader getDownloader() {
        return localStorageDownloader;
    }

    @Override
    public Deleter getDeleter() {
        return localStorageDeleter;
    }
}
