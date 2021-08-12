package com.example.core.file.ops.impls;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.example.core.file.dto.UploadFile;
import com.example.core.file.ops.Uploader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.example.core.exception.NotSameFileException;
import com.example.core.utils.ImageThumbUtil;
import org.apache.commons.codec.digest.DigestUtils;
import com.example.core.exception.UploadException;
import com.example.core.utils.FileUtil;
import com.example.core.utils.PathUtil;

import static com.example.core.configs.Constant.FILE_SEPARATOR;

@Component
public class LocalStorageUploader extends Uploader {
    @Override
    public List<UploadFile> upload(HttpServletRequest request, UploadFile uploadFile) {
        List<UploadFile> saveUploadFileList = new ArrayList<>();
        StandardMultipartHttpServletRequest standardMultipartHttpServletRequest
                = (StandardMultipartHttpServletRequest) request;
        boolean isMultipart = ServletFileUpload.isMultipartContent(standardMultipartHttpServletRequest);
        if (!isMultipart) {
            throw new UploadException("未包含文件上传域");
        }
        try {
            Iterator<String> iterator = standardMultipartHttpServletRequest.getFileNames();
            while (iterator.hasNext()) {
                saveUploadFileList = doUpload(standardMultipartHttpServletRequest, iterator, uploadFile);
            }
        } catch (IOException e) {
            throw new UploadException("上传过程中失败");
        } catch (NotSameFileException exception) {
            exception.printStackTrace();
        }
        return saveUploadFileList;
    }

    private List<UploadFile> doUpload(StandardMultipartHttpServletRequest request, Iterator<String> iterator, UploadFile uploadFile)
            throws IOException, NotSameFileException {
        String savePath = getLocalFileSavePath();
        List<UploadFile> saveUploadFileList = new ArrayList<>();
        MultipartFile multipartFile = request.getFile(iterator.next());

        String timeStampName = uploadFile.getIdentifier();
        assert multipartFile != null;
        String originalName = multipartFile.getOriginalFilename();

        assert originalName != null;
        String fileName = getFileName(originalName);

        String fileType = FileUtil.getFileExtendName(originalName);

        uploadFile.setFileName(fileName);
        uploadFile.setFileType(fileType);
        uploadFile.setTimeStampName(timeStampName);

        String saveFilePath = savePath + FILE_SEPARATOR + timeStampName + "." + fileType;
        String tempFilePath = savePath + FILE_SEPARATOR + timeStampName + "." + fileType + "_tmp";
        String minFilePath = savePath + FILE_SEPARATOR + timeStampName + "_min" + "." + fileType;
        String confFilePath = savePath + FILE_SEPARATOR + timeStampName + "." + "conf";

        File file = new File(PathUtil.getStaticPath() + FILE_SEPARATOR + saveFilePath);
        File tempFile = new File(PathUtil.getStaticPath() + FILE_SEPARATOR + tempFilePath);
        File minFile = new File(PathUtil.getStaticPath() + FILE_SEPARATOR + minFilePath);
        File confFile = new File(PathUtil.getStaticPath() + FILE_SEPARATOR + confFilePath);

        uploadFile.setStorageType(0);
        uploadFile.setUrl(saveFilePath);

        if (StringUtils.isEmpty(uploadFile.getTaskId())) {
            uploadFile.setTaskId(UUID.randomUUID().toString());
        }

        //第一步 打开将要写入的文件
        RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
        //第二步 打开通道
        FileChannel fileChannel = raf.getChannel();
        //第三步 计算偏移量
        long position = (uploadFile.getChunkNumber() - 1) * uploadFile.getChunkSize();
        //第四步 获取分片数据
        byte[] fileData = multipartFile.getBytes();
        //第五步 写入数据
        fileChannel.position(position);
        fileChannel.write(ByteBuffer.wrap(fileData));
        fileChannel.force(true);
        fileChannel.close();
        raf.close();
        //判断是否完成文件的传输并进行校验与重命名
        boolean isComplete = checkUploadStatus(uploadFile, confFile);
        if (isComplete) {
            FileInputStream fileInputStream = new FileInputStream(tempFile.getPath());
            String md5 = DigestUtils.md5Hex(fileInputStream);
            fileInputStream.close();
            if (StringUtils.isNotBlank(md5) && !md5.equals(uploadFile.getIdentifier())) {
                throw new NotSameFileException();
            }
            boolean result = tempFile.renameTo(file);
            if (FileUtil.isImageFile(uploadFile.getFileType())) {
                ImageThumbUtil.thumbnailsImage(file, minFile, 300);
            }

            uploadFile.setSuccess(1);
            uploadFile.setMessage("上传成功");
        } else {
            uploadFile.setSuccess(0);
            uploadFile.setMessage("未完成");
        }
        uploadFile.setFileSize(uploadFile.getTotalSize());
        saveUploadFileList.add(uploadFile);
        return saveUploadFileList;
    }

    private synchronized boolean checkUploadStatus(UploadFile param, File confFile) throws IOException {
        RandomAccessFile confAccessFile = new RandomAccessFile(confFile, "rw");
        //设置文件长度
        confAccessFile.setLength(param.getTotalChunks());
        //设置起始偏移量
        confAccessFile.seek(param.getChunkNumber() - 1);
        //将指定的一个字节写入文件中 127，
        confAccessFile.write(Byte.MAX_VALUE);
        byte[] completeStatusList = FileUtils.readFileToByteArray(confFile);
        confAccessFile.close();//不关闭会造成无法占用
        //创建conf文件文件长度为总分片数，每上传一个分块即向conf文件中写入一个127，那么没上传的位置就是默认的0,已上传的就是127
        for (byte b : completeStatusList) {
            if (b != Byte.MAX_VALUE) {
                return false;
            }
        }
        boolean result = confFile.delete();
        return true;
    }

    private String getFileName(String fileName) {
        if (!fileName.contains(".")) {
            return fileName;
        }
        return fileName.substring(0, fileName.lastIndexOf("."));
    }
}
