package com.example.core.file.ops;

import com.example.core.configs.Constant;
import com.example.core.file.dto.UploadFile;
import com.example.core.utils.PathUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Slf4j
public abstract class Uploader {

    public abstract List<UploadFile> upload(HttpServletRequest request, UploadFile uploadFile);

    protected String getLocalFileSavePath() {
        String path = Constant.ROOT_PATH;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyMMdd");
        path = Constant.FILE_SEPARATOR + path + Constant.FILE_SEPARATOR + formatter.format(new Date());
        String staticPath = PathUtil.getStaticPath();

        File dir = new File(staticPath + path);
        if (!dir.exists()) {
            try {
                boolean isSuccessMakeDir = dir.mkdirs();
                if (!isSuccessMakeDir) {
                    log.error("目录创建失败:" + PathUtil.getStaticPath() + path);
                }
            } catch (Exception e) {
                log.error("目录创建失败" + PathUtil.getStaticPath() + path);
                return "";
            }
        }
        return path;
    }
}
