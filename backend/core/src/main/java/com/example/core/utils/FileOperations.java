package com.example.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

@Slf4j
public class FileOperations {
    private static Logger logger = LoggerFactory.getLogger(FileOperations.class);

    /**
     * 创建文件
     *
     * @param fileUrl 文件路径
     * @return 新文件
     */
    public static File newFile(String fileUrl) {
        return new File(fileUrl);
    }

    /**
     * 删除文件
     *
     * @param file 文件/文件夹
     * @return 是否删除成功
     */
    public static boolean deleteFile(File file) {
        if (file == null) return false;
        if (!file.exists()) return false;
        if (file.isFile()) return file.delete();
        for (File childFile : Objects.requireNonNull(file.listFiles())) {
            deleteFile(childFile);
        }
        return file.delete();
    }

    /**
     * 删除文件
     *
     * @param fileUrl 文件路径
     * @return 删除是否成功
     */
    public static boolean deleteFile(String fileUrl) {
        File file = newFile(fileUrl);
        return deleteFile(file);
    }

    /**
     * 得到文件大小
     *
     * @param fileUrl 文件路径
     * @return 文件大小
     */
    public static long getFileSize(String fileUrl) {
        File file = newFile(fileUrl);
        if (file.exists()) return file.length();
        return 0;
    }

    /**
     * 得到文件大小
     *
     * @param file 文件
     * @return 文件大小
     */
    public static long getFileSize(File file) {
        if (file == null) {
            return 0;
        }
        return file.length();
    }

    /**
     * 创建目录
     *
     * @param file 文件
     * @return 是否创建成功
     */
    public static boolean mkdir(File file) {
        if (file == null) {
            return false;
        }

        if (file.exists()) {
            return true;
        }

        return file.mkdirs();
    }

    /**
     * 创建目录
     *
     * @param fileUrl 文件路径
     * @return 是否创建成功
     */
    public static boolean mkdir(String fileUrl) {
        if (fileUrl == null) {
            return false;
        }
        File file = newFile(fileUrl);
        if (file.exists()) {
            return true;
        }

        return file.mkdirs();
    }

    /**
     * 拷贝文件
     *
     * @param fileInputStream  文件输入流
     * @param fileOutputStream 文件输出流
     * @throws IOException io异常
     */
    public static void copyFile(FileInputStream fileInputStream, FileOutputStream fileOutputStream) throws IOException {
        try {
            byte[] buf = new byte[4096];  //8k的缓冲区

            int len = fileInputStream.read(buf); //数据在buf中，len代表向buf中放了多少个字节的数据，-1代表读不到
            while (len != -1) {
                fileOutputStream.write(buf, 0, len); //读多少个字节，写多少个字节
                len = fileInputStream.read(buf);
            }
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 拷贝文件
     *
     * @param src  源文件
     * @param dest 目的文件
     * @throws IOException io异常
     */
    public static void copyFile(File src, File dest) throws IOException {
        FileInputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dest);
        copyFile(in, out);
    }

    /**
     * 拷贝文件
     *
     * @param srcUrl  源路径
     * @param destUrl 目的路径
     * @throws IOException io异常
     */
    public static void copyFile(String srcUrl, String destUrl) throws IOException {
        if (srcUrl == null || destUrl == null) {
            return;
        }
        File srcFile = newFile(srcUrl);
        File descFile = newFile(destUrl);
        copyFile(srcFile, descFile);
    }

    /**
     * 文件解压缩
     *
     * @param sourceFile  需要解压的文件
     * @param destDirPath 目的路径
     * @return 解压目录列表
     */
    public static List<String> unzip(File sourceFile, String destDirPath) {
        return null;
    }

    /**
     * 解压rar
     *
     * @param sourceFile  需要解压的文件
     * @param destDirPath 目的路径
     * @throws Exception
     */
    public static List<String> unRar(File sourceFile, String destDirPath) throws Exception {
        return null;
    }

    public static long deleteFileFromDisk(String url) {
        String fileUrl = PathUtil.getStaticPath() + url;
        String extendName = FileUtil.getFileExtendName(fileUrl);
        String minFileUrl = fileUrl.replace("." + extendName, "_min." + extendName);
        long fileSize = getFileSize(fileUrl);

        FileOperations.deleteFile(fileUrl);
        FileOperations.deleteFile(minFileUrl);
        return fileSize;
    }

}
