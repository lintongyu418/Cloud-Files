package com.neu.cloudfiles.constant;

import com.neu.cloudfiles.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lw on 2018/1/19.
 */

public class Constant {
    public static final String REQUEST_BASE_URL = "http://161.35.224.251:10000";
    public static final String SHARE_LINK_PREFIX = REQUEST_BASE_URL + "/share/";

    public static final String SAVE_USER_LOGIN_KEY = "user/login";
    public static final String SAVE_USER_REGISTER_KEY = "user/register";
    public static final String SHARED_NAME = "_preferences";
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String TOKEN_KEY = "token";
    public static final String LOGIN_KEY = "login";
    public static final String USER_KEY = "user";
    public static final int SUCCESS_RESPONSE = 20000;

    public static final int ALL_TYPE = 0;
    public static final long FILE_PAGE_SIZE = 10000;
    public static final long FILE_PAGE = 1;
    public static final int IS_DIR = 1;
    public static final int PREV_DIR = 3;
    public static final String FILE_PATH_DEFAULT = "/";

    public static final String[] IMG_FILE = {"bmp", "jpg", "png", "tif", "gif", "jpeg"};
    public static final String[] DOC_FILE = {"doc", "docx", "ppt", "pptx", "xls", "xlsx", "txt", "hlp", "wps", "rtf", "html", "pdf"};
    public static final String[] VIDEO_FILE = {"avi", "mp4", "mpg", "mov", "swf"};
    public static final String[] MUSIC_FILE = {"wav", "aif", "au", "mp3", "ram", "wma", "mmf", "amr", "aac", "flac"};

    public static final Map<String, Integer> FILE_TYPE_MAP = new HashMap<String, Integer>() {{
        put("dir", R.drawable.dir);
        put("chm", R.drawable.file_chm);
        put("css", R.drawable.file_css);
        put("csv", R.drawable.file_csv);
        put("png", R.drawable.file_pic);
        put("jpg", R.drawable.file_pic);
        put("jpeg", R.drawable.file_pic);
        put("docx", R.drawable.file_word);
        put("doc", R.drawable.file_word);
        put("ppt", R.drawable.file_ppt);
        put("pptx", R.drawable.file_ppt);
        put("xls", R.drawable.file_excel);
        put("xlsx", R.drawable.file_excel);
        put("mp4", R.drawable.file_video);
        put("avi", R.drawable.file_avi);
        put("rar", R.drawable.file_rar);
        put("zip", R.drawable.file_zip);
        put("dmg", R.drawable.file_dmg);
        put("mp3", R.drawable.file_music);
        put("open", R.drawable.file_open);
        put("pdf", R.drawable.file_pdf);
        put("rtf", R.drawable.file_rtf);
        put("txt", R.drawable.file_txt);
        put("oa", R.drawable.file_oa);
        put("unknown", R.drawable.file_unknown);
        put("js", R.drawable.file_js);
        put("html", R.drawable.file_html);
        put("img", R.drawable.file_img);
        put("sql", R.drawable.file_sql);
        put("jar", R.drawable.file_jar);
        put("svg", R.drawable.file_svg);
        put("gif", R.drawable.file_gif);
        put("json", R.drawable.file_json);
        put("exe", R.drawable.file_exe);
        put("prev", R.drawable.prev);
    }};
    public static final String FILE_SEPARATOR = "/";

    public static final String DOWNLOAD_ING = "downloading";
    public static final String DOWNLOAD_FAILED = "download failed";
    public static final String DOWNLOAD_SUCCESS = "download success";

    public static final String SAVE_FOLDER_NAME = "cloudFileDownload";

    public static final String DATA_TYPE_ALL = "*/*";//未指定明确的文件类型，不能使用精确类型的工具打开，需要用户选择
    public static final String DATA_TYPE_APK = "application/vnd.android.package-archive";
    public static final String DATA_TYPE_VIDEO = "video/*";
    public static final String DATA_TYPE_AUDIO = "audio/*";
    public static final String DATA_TYPE_HTML = "text/html";
    public static final String DATA_TYPE_IMAGE = "image/*";
    public static final String DATA_TYPE_PPT = "application/vnd.ms-powerpoint";
    public static final String DATA_TYPE_EXCEL = "application/vnd.ms-excel";
    public static final String DATA_TYPE_WORD = "application/msword";
    public static final String DATA_TYPE_CHM = "application/x-chm";
    public static final String DATA_TYPE_TXT = "text/plain";
    public static final String DATA_TYPE_PDF = "application/pdf";
    public static final int REQ_QR_CODE = 10010;
    /**
     * 每页数量
     */
    public static final int PAGE_SIZE = 20;

    public static final Map<Integer, Integer> AVATAR_MAP = new HashMap<Integer, Integer>() {{
        put(1, R.drawable.avatar_1);
        put(2, R.drawable.avatar_2);
        put(3, R.drawable.avatar_3);
        put(4, R.drawable.avatar_4);
        put(5, R.drawable.avatar_5);
        put(6, R.drawable.avatar_6);
        put(7, R.drawable.avatar_7);
        put(8, R.drawable.avatar_8);
        put(9, R.drawable.avatar_9);
        put(10, R.drawable.avatar_10);
        put(11, R.drawable.avatar_11);
        put(12, R.drawable.avatar_12);
        put(13, R.drawable.avatar_13);
        put(14, R.drawable.avatar_14);
    }};
}
