package com.example.core.file.ops;

public interface FileOperationFactory {
    Uploader getUploader();

    Downloader getDownloader();

    Deleter getDeleter();
}
