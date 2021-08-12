package com.example.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.core.configs.Constant.*;

public class FileUtil {

    public static List<String> getFileExtendsByType(int fileType) {

        List<String> fileExtends;
        switch (fileType) {
            case IMAGE_TYPE:
                fileExtends = Arrays.asList(IMG_FILE);
                break;
            case DOC_TYPE:
                fileExtends = Arrays.asList(DOC_FILE);
                break;
            case VIDEO_TYPE:
                fileExtends = Arrays.asList(VIDEO_FILE);
                break;
            case MUSIC_TYPE:
                fileExtends = Arrays.asList(MUSIC_FILE);
                break;
            default:
                fileExtends = new ArrayList<>();
                break;


        }
        return fileExtends;
    }

    /**
     * 判断是否为图片文件
     *
     * @param extendName 文件扩展名
     * @return 是否为图片文件
     */
    public static boolean isImageFile(String extendName) {
        for (int i = 0; i < IMG_FILE.length; i++) {
            if (extendName.equalsIgnoreCase(IMG_FILE[i])) {
                return true;
            }
        }
        return false;
    }


    public static String pathSplitFormat(String filePath) {
        String path = filePath.replace("///", "/")
                .replace("//", "/")
                .replace("\\\\\\", "\\")
                .replace("\\\\", "\\");
        return path;
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 文件扩展名
     */
    public static String getFileExtendName(String fileName) {
        if (fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 获取不包含扩展名的文件名
     *
     * @param fileName 文件名
     * @return 文件名（不带扩展名）
     */
    public static String getFileNameNotExtend(String fileName) {
        String fileType = getFileExtendName(fileName);
        return fileName.replace("." + fileType, "");
    }


}
