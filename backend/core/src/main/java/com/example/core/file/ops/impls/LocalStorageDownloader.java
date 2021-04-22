package com.example.core.file.ops.impls;

import com.example.core.file.dto.DownloadFile;
import com.example.core.file.ops.Downloader;
import com.example.core.utils.FileOperations;
import com.example.core.utils.PathUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Component
public class LocalStorageDownloader extends Downloader {
    @Override
    public void download(HttpServletResponse response, DownloadFile downloadFile) {
        BufferedInputStream bufferedInputStream = null;
        byte[] buffer = new byte[1024];
        // 设置文件路径
        File file = FileOperations.newFile(PathUtil.getStaticPath() + downloadFile.getFileUrl());
        if (file.exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                OutputStream outputStream = response.getOutputStream();
                int i = bufferedInputStream.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bufferedInputStream.read(buffer);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            } finally {
                if (bufferedInputStream != null) {
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public InputStream getInputStream(DownloadFile downloadFile) {
        File file = FileOperations.newFile(PathUtil.getStaticPath() + downloadFile.getFileUrl());
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}
