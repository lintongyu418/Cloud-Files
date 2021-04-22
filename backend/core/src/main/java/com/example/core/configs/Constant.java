package com.example.core.configs;

public class Constant {
    public static final String ROOT_PATH = "upload";
    public static final String FILE_SEPARATOR = "/";

    public static final int FILE_MAX_SIZE = 10000000;// 文件大小限制，单位KB

    public static final int EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;
    // 加密密文，私钥
    public static final String JWT_SECRET = "Jack";

    public static final String[] IMG_FILE = {"bmp", "jpg", "png", "tif", "gif", "jpeg"};
    public static final String[] DOC_FILE = {"doc", "docx", "ppt", "pptx", "xls", "xlsx", "txt", "hlp", "wps", "rtf", "html", "pdf"};
    public static final String[] VIDEO_FILE = {"avi", "mp4", "mpg", "mov", "swf"};
    public static final String[] MUSIC_FILE = {"wav", "aif", "au", "mp3", "ram", "wma", "mmf", "amr", "aac", "flac"};
    public static final int ALL_TYPE = 0;
    public static final int IMAGE_TYPE = 1;
    public static final int DOC_TYPE = 2;
    public static final int VIDEO_TYPE = 3;
    public static final int MUSIC_TYPE = 4;
    public static final int OTHER_TYPE = 5;
    public static final int SHARE_FILE = 6;
    public static final int RECYCLE_FILE = 7;

    public static final String DEFAULT_STORAGE_TYPE = "local";

    public static final String R_SUCCESS = "success";
    public static final String R_ERROR = "error";

    public static final int SUCCESS_RESPONSE = 20000;
    public static final int ERROR_UNKNOWN = 20001;
    public static final int ERROR_PARAMETER = 20002;
    public static final int ERROR_NULL_POINTER = 20003;
    public static final int ERROR_INDEX_OUT_OF_BOUND = 20004;
    public static final int ERROR_TIMEOUT = 20005;
    public static final int ERROR_NOT_LOGGED_IN = 20006;
    public static final int ERROR_REGISTER_USER = 20007;
    public static final int ERROR_USER_NOT_EXIST = 20008;
    public static final int ERROR_LOGIN = 20009;
    public static final int ERROR_NO_PERMISSION = 20010;
    public static final int ERROR_FILE_ALREADY_EXIST = 20011;
}
