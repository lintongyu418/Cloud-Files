package com.example.core.utils;

import com.example.core.configs.Constant;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class PathUtil {

    /**
     * 获取项目所在的根目录路径 resources路径
     */
    public static String getProjectRootPath() {
        String absolutePath = null;
        try {
            String url = ResourceUtils.getURL("classpath:").getPath();
            absolutePath = urlDecode(new File(url).getAbsolutePath()) + File.separator;
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return absolutePath;
    }

    /**
     * 得到static路径
     */
    public static String getStaticPath() {
        String projectRootAbsolutePath = getProjectRootPath();
        int index = projectRootAbsolutePath.indexOf("file:");
        if (index != -1) {
            projectRootAbsolutePath = projectRootAbsolutePath.substring(0, index);
        }
        return projectRootAbsolutePath + "static" + File.separator;
    }

    /**
     * 路径解码
     */
    public static String urlDecode(String url) throws UnsupportedEncodingException {
        String decodeUrl = null;
        decodeUrl = URLDecoder.decode(url, StandardCharsets.UTF_8.toString());
        return decodeUrl;
    }

    public static String getParentPath(String path) {
        return path.substring(0, path.lastIndexOf(Constant.FILE_SEPARATOR));
    }

    public static void main(String[] args) {
        String path = "aaa/bbb/ccc/";
        System.out.println(getParentPath(path));
        String fileName = path.substring(path.lastIndexOf("/"));
        System.out.println(fileName);
    }

}
