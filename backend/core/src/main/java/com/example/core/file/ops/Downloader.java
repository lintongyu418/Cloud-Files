package com.example.core.file.ops;

import com.example.core.file.dto.DownloadFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

public abstract class Downloader {
    /**
     * 下载文件
     */
    public abstract void download(HttpServletResponse response, DownloadFile downloadFile);

    /**
     * note ???
     */
    public abstract InputStream getInputStream(DownloadFile downloadFile);

}
