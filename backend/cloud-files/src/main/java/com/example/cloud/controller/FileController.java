package com.example.cloud.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.cloud.entity.FileBean;
import com.example.cloud.entity.ShareBean;
import com.example.cloud.entity.UserBean;
import com.example.cloud.entity.UserFileBean;
import com.example.cloud.pojo.*;
import com.example.cloud.service.*;
import com.example.cloud.vo.FileListVo;
import com.example.cloud.vo.ShareFileVo;
import com.example.cloud.vo.TreeNodeVo;
import com.example.cloud.vo.UploadFileVo;
import com.example.core.configs.Constant;
import com.example.core.utils.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Tag(name = "file", description = "该接口为文件传输接口，主要用来做文件的上传和下载")
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    FileOperationService fileOperationService;

    @Resource
    FileService fileService;

    @Resource
    UserService userService;

    @Resource
    UserFileService userFileService;

    public static final String CURRENT_MODULE = "文件传输接口";

    @Operation(summary = "极速上传", description = "校验文件MD5判断文件是否存在，如果存在直接上传成功并返回skipUpload=true，如果不存在返回skipUpload=false需要再次调用该接口的POST方法", tags = {"filetransfer"})
    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    @ResponseBody
    public R uploadFileSpeed(UploadFileDo uploadFileDto, @RequestHeader("token") String token) {
//        UserBean sessionUserBean = userService.getUserBeanByToken(token);
//        if (sessionUserBean == null) {
//            return R.error().code(Constant.ERROR_LOGIN).message("not login");
//        }
//
//        R result = operationCheck(token);
//        if (!result.isSuccess()){
//            return R.error().code(Constant.ERROR_NO_PERMISSION).message("没权限，请联系管理员！");
//        }
//        UploadFileVo uploadFileVo = new UploadFileVo();
//        Map<String, Object> param = new HashMap<String, Object>();
//        param.put("identifier", uploadFileDto.getIdentifier());
//        synchronized (FileController.class) {
//            List<FileBean> list = fileService.listByMap(param);
//            if (list != null && !list.isEmpty()) {
//                FileBean file = list.get(0);
//
//                UserFileBean userFile = new UserFileBean();
//                userFile.setFileId(file.getFileId());
//                userFile.setUserId(sessionUserBean.getUserId());
//                userFile.setFilePath(uploadFileDto.getFilePath());
//                String fileName = uploadFileDto.getFilename();
//                userFile.setFileName(fileName.substring(0, fileName.lastIndexOf(".")));
//                userFile.setExtendName(FileUtil.getFileExtendName(fileName));
//                userFile.setDeleteFlag(0);
//                userFile.setIsDir(0);
//                userFile.setUploadTime(DateUtil.getCurrentTime());
//                userFileService.save(userFile);
//                fileService.increaseFilePointCount(file.getFileId());
//                uploadFileVo.setSkipUpload(true);
//
//            } else {
//                uploadFileVo.setSkipUpload(false);
//
//            }
//        }
//        return R.ok().data("file", uploadFileVo);
        return R.error();
    }

    /**
     * 上传文件
     */
    @Operation(summary = "上传文件", description = "真正的上传文件接口", tags = {"file operations"})
    @PostMapping(value = "/upload")
    @ResponseBody
    public R uploadFile(HttpServletRequest request, UploadFileDo uploadFileDto, @RequestHeader("token") String token) {

        UserBean sessionUserBean = userService.getUserBeanByToken(token);
        R checkResult = this.checkUserLoginAndPermission(sessionUserBean, token);
        if (checkResult != null) return checkResult;

        // 检查 文件是否已经存在
        boolean hasSame = userFileService.checkFileExist(sessionUserBean.getUserId(), uploadFileDto.getFilename(), uploadFileDto.getFilePath());
        if (hasSame) {
            return R.error().code(Constant.ERROR_FILE_ALREADY_EXIST).message("文件已经存在");
        }
        fileOperationService.uploadFile(request, uploadFileDto, sessionUserBean.getUserId());
        UploadFileVo uploadFileVo = new UploadFileVo();
        return R.ok().data("file", uploadFileVo);

    }

    @Operation(summary = "list all files")
    @GetMapping(value = "/listFiles")
    @ResponseBody
    public R getAllFiles(FileListDo fileListDo, @RequestHeader("token") String token) throws UnsupportedEncodingException {
        UserBean sessionUserBean = userService.getUserBeanByToken(token);
        R checkResult = this.checkUserLoginAndPermission(sessionUserBean, token);
        if (checkResult != null) return checkResult;

        UserFileBean userFile = new UserFileBean();
        userFile.setUserId(sessionUserBean.getUserId());

        if (fileListDo.getCurrentPage() == null || fileListDo.getPageCount() == null) {
            fileListDo.setCurrentPage(1L);
            fileListDo.setPageCount(10L);
        }

        R result = R.error().message("unknown file category");
        ;
        switch (fileListDo.getFileType()) {
            case Constant.ALL_TYPE:
                userFile.setFilePath(PathUtil.urlDecode(fileListDo.getFilePath()));
                result = this.getAllFiles(fileListDo, userFile);
                break;
            case Constant.IMAGE_TYPE:
            case Constant.DOC_TYPE:
            case Constant.VIDEO_TYPE:
            case Constant.MUSIC_TYPE:
            case Constant.OTHER_TYPE:
            case Constant.SHARE_FILE:
            case Constant.RECYCLE_FILE:
                result = this.getFilesByType(fileListDo, userFile);
            default:
                break;
        }
        return result;
    }

    /**
     * 下载文件
     */
    @Operation(summary = "下载文件", description = "下载文件接口", tags = {"filetransfer"})
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse httpServletResponse, DownloadFileDo downloadFileDo) {
        httpServletResponse.setContentType("application/force-download");// 设置强制下载不打开
        UserFileBean userFile = userFileService.getById(downloadFileDo.getUserFileId());
        String fileName = "";
        if (userFile.getIsDir() == 1) {
            fileName = userFile.getFileName() + ".zip";
        } else {
            fileName = userFile.getFileName() + "." + userFile.getExtendName();

        }
        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        httpServletResponse.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
        fileOperationService.downloadFile(httpServletResponse, downloadFileDo);
    }

    @Operation(summary = "preview file")
    @GetMapping("/preview")
    public void preview(HttpServletRequest request, HttpServletResponse response, PreviewDo previewDo) {
        UserFileBean userFile = userFileService.getById(previewDo.getUserFileId());
        FileBean fileBean = fileService.getById(userFile.getFileId());
        String mime = MimeUtils.getMime(userFile.getExtendName());
        response.setHeader("Content-Type", mime);
        String rangeString = request.getHeader("Range");//如果是video标签发起的请求就不会为null
        if (StringUtils.isNotEmpty(rangeString)) {
            long range = Long.parseLong(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
            response.setContentLength(Math.toIntExact(fileBean.getFileSize()));
            response.setHeader("Content-Range", String.valueOf(range + (Math.toIntExact(fileBean.getFileSize()) - 1)));
        }
        response.setHeader("Accept-Ranges", "bytes");
        String fileName = userFile.getFileName() + "." + userFile.getExtendName();
        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        response.addHeader("Content-Disposition", "fileName=" + fileName);// 设置文件名
        DownloadFileDo downloadFileDo = new DownloadFileDo();
        downloadFileDo.setUserFileId(previewDo.getUserFileId());
        try {
            fileOperationService.downloadFile(response, downloadFileDo);
        } catch (Exception e) {
            //org.apache.catalina.connector.ClientAbortException: java.io.IOException: 你的主机中的软件中止了一个已建立的连接。
            log.error("该异常忽略不做处理");
        }
    }

    @Operation(summary = "创建文件夹")
    @PostMapping("/createFolder")
    public R createFolder(@RequestBody CreateFolderDo createFolderDo, @RequestHeader("token") String token) {
        UserBean sessionUserBean = userService.getUserBeanByToken(token);
        R checkResult = this.checkUserLoginAndPermission(sessionUserBean, token);
        if (checkResult != null) return checkResult;

        List<UserFileBean> userFiles = userFileService.selectUserFileByNameAndPath(createFolderDo.getFileName(), createFolderDo.getFilePath(), sessionUserBean.getUserId());
        if (userFiles != null && !userFiles.isEmpty()) {
            return R.error().code(Constant.ERROR_FILE_ALREADY_EXIST).message("同名文件夹已存在");
        }
        UserFileBean userFile = new UserFileBean();
        userFile.setUserId(sessionUserBean.getUserId());
        userFile.setFileName(createFolderDo.getFileName());
        userFile.setFilePath(createFolderDo.getFilePath());
        userFile.setDeleteFlag(0);
        userFile.setIsDir(1);
        userFile.setUploadTime(DateUtil.getCurrentTime());
        userFileService.save(userFile);
        return R.ok().message("successfully created a folder");
    }

    @Operation(summary = "rename file")
    @RequestMapping(value = "/rename", method = RequestMethod.POST)
    @ResponseBody
    public R renameFile(@RequestBody RenameFileDo renameFileDto, @RequestHeader("token") String token) {
        UserBean sessionUserBean = userService.getUserBeanByToken(token);
        R checkResult = this.checkUserLoginAndPermission(sessionUserBean, token);
        if (checkResult != null) return checkResult;

        boolean hasSame = userFileService.checkFileExist(sessionUserBean.getUserId(), renameFileDto.getFileName(), renameFileDto.getFilePath());
        if (hasSame) return R.error().code(Constant.ERROR_FILE_ALREADY_EXIST).message("already exist");

        UserFileBean targetFile = userFileService.getById(renameFileDto.getUserFileId());

        if (targetFile.getIsDir() == 1) {
            LambdaUpdateWrapper<UserFileBean> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.set(UserFileBean::getFileName, renameFileDto.getFileName())
                    .eq(UserFileBean::getUserFileId, renameFileDto.getUserFileId());
            userFileService.update(lambdaUpdateWrapper);
            userFileService.replaceFolderPath(
                    targetFile.getFilePath() + renameFileDto.getFilePath() + Constant.FILE_SEPARATOR,
                    targetFile.getFilePath() + targetFile.getFileName() + Constant.FILE_SEPARATOR,
                    sessionUserBean.getUserId());
        } else {
            LambdaUpdateWrapper<UserFileBean> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.set(UserFileBean::getFileName, renameFileDto.getFileName())
                    .eq(UserFileBean::getUserFileId, renameFileDto.getUserFileId());
            userFileService.update(lambdaUpdateWrapper);
        }
        return R.ok();
    }

    @Operation(summary = "删除文件", description = "可以删除文件或者目录", tags = {"file"})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public R deleteFile(@RequestBody DeleteFileDo deleteFileDo, @RequestHeader("token") String token) {
        UserBean sessionUserBean = userService.getUserBeanByToken(token);
        R checkResult = this.checkUserLoginAndPermission(sessionUserBean, token);
        if (checkResult != null) return checkResult;

        userFileService.deleteUserFile(deleteFileDo.getUserFileId(), sessionUserBean.getUserId());
        return R.ok();
    }

    @Operation(summary = "删除文件", description = "可以删除文件或者目录", tags = {"file"})
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public R testLikeRight() {
        userFileService.doTest();
        return R.ok();
    }

    @Operation(summary = "文件移动", description = "可以移动文件或者目录", tags = {"file"})
    @RequestMapping(value = "/move", method = RequestMethod.POST)
    @ResponseBody
    public R moveFile(@RequestBody MoveFileDo moveFileDto, @RequestHeader("token") String token) {
        UserBean sessionUserBean = userService.getUserBeanByToken(token);
        R checkResult = this.checkUserLoginAndPermission(sessionUserBean, token);
        if (checkResult != null) return checkResult;

        String oldFilePath = moveFileDto.getOldFilePath();
        String newFilePath = moveFileDto.getFilePath();
        String fileName = moveFileDto.getFileName();
        String extendName = moveFileDto.getExtendName();
        userFileService.updateFilePathByFilePath(oldFilePath, newFilePath, fileName, extendName, sessionUserBean.getUserId());
        return R.ok();
    }


    @Operation(summary = "获取文件树", description = "文件移动的时候需要用到该接口，用来展示目录树", tags = {"file"})
    @RequestMapping(value = "/fileTree", method = RequestMethod.GET)
    @ResponseBody
    public R getFileTree(@RequestHeader("token") String token) {
        UserBean sessionUserBean = userService.getUserBeanByToken(token);
        R checkResult = this.checkUserLoginAndPermission(sessionUserBean, token);
        if (checkResult != null) return checkResult;

        List<UserFileBean> userFileList = userFileService.selectFilePathTreeByUserId(sessionUserBean.getUserId());
        TreeNodeVo resultTreeNode = new TreeNodeVo();
        resultTreeNode.setLabel("/");
        resultTreeNode.setId(0L);
        long id = 1;
        for (UserFileBean userFile : userFileList) {
            String filePath = userFile.getFilePath() + userFile.getFileName() + "/";
            Queue<String> queue = new LinkedList<>();

            String[] strArr = filePath.split("/");
            for (String s : strArr) {
                if (!"".equals(s) && s != null) {
                    queue.add(s);
                }
            }
            if (queue.size() == 0) {
                continue;
            }
            insertTreeNode(resultTreeNode, id++, "/", queue);
        }
        List<TreeNodeVo> treeNodeList = resultTreeNode.getChildren();
        Collections.sort(treeNodeList, new Comparator<TreeNodeVo>() {
            @Override
            public int compare(TreeNodeVo o1, TreeNodeVo o2) {
                long i = o1.getId() - o2.getId();
                return (int) i;
            }
        });
        return R.ok().data("tree", resultTreeNode);
    }

    private void insertTreeNode(TreeNodeVo treeNode, long id, String filePath, Queue<String> nodeNameQueue) {
        List<TreeNodeVo> childrenTreeNodes = treeNode.getChildren();
        String currentNodeName = nodeNameQueue.peek();
        if (currentNodeName == null) {
            return;
        }

        filePath = filePath + currentNodeName + "/";

        if (!isExistPath(childrenTreeNodes, currentNodeName)) {  //1、判断有没有该子节点，如果没有则插入
            //插入
            TreeNodeVo resultTreeNode = new TreeNodeVo();
            resultTreeNode.setFilePath(filePath);
            resultTreeNode.setLabel(nodeNameQueue.poll());
            resultTreeNode.setId(++id);
            childrenTreeNodes.add(resultTreeNode);
        } else {  //2、如果有，则跳过
            nodeNameQueue.poll();
        }
        if (nodeNameQueue.size() != 0) {
            for (int i = 0; i < childrenTreeNodes.size(); i++) {
                TreeNodeVo childrenTreeNode = childrenTreeNodes.get(i);
                if (currentNodeName.equals(childrenTreeNode.getLabel())) {
                    insertTreeNode(childrenTreeNode, id * 10, filePath, nodeNameQueue);
                    childrenTreeNodes.remove(i);
                    childrenTreeNodes.add(childrenTreeNode);
                    treeNode.setChildren(childrenTreeNodes);
                }
            }
        } else {
            treeNode.setChildren(childrenTreeNodes);
        }
    }

    private boolean isExistPath(List<TreeNodeVo> childrenTreeNodes, String path) {
        boolean isExistPath = false;
        for (TreeNodeVo childrenTreeNode : childrenTreeNodes) {
            if (path.equals(childrenTreeNode.getLabel())) {
                isExistPath = true;
            }
        }
        return isExistPath;
    }

    private R getFilesByType(FileListDo fileListDo, UserFileBean userFile) {
        List<String> extendNames = new ArrayList<>();
        List<FileListVo> fileList = new ArrayList<>();
        Long total = 0L;
        if (fileListDo.getFileType() == Constant.OTHER_TYPE) {
            extendNames.addAll(Arrays.asList(Constant.DOC_FILE));
            extendNames.addAll(Arrays.asList(Constant.IMG_FILE));
            extendNames.addAll(Arrays.asList(Constant.VIDEO_FILE));
            extendNames.addAll(Arrays.asList(Constant.MUSIC_FILE));
            Long beginCount = (fileListDo.getCurrentPage() - 1) * fileListDo.getPageCount();
            fileList = userFileService.selectFileNotInExtendNames(extendNames, beginCount, fileListDo.getPageCount(), userFile.getUserId());
            total = userFileService.countFileByNotInExtendNames(extendNames, beginCount, fileListDo.getPageCount(), userFile.getUserId());
        } else {
            extendNames = FileUtil.getFileExtendsByType(fileListDo.getFileType());
            Long beginCount = (fileListDo.getCurrentPage() - 1) * fileListDo.getPageCount();
            fileList = userFileService.selectByExtendNames(extendNames, beginCount, fileListDo.getPageCount(), userFile.getUserId());
            total = userFileService.countFileByExtendNames(extendNames, beginCount, fileListDo.getPageCount(), userFile.getUserId());
        }
        return R.ok().data("total", total).data("files", fileList);
    }

    private R getAllFiles(FileListDo fileListDo, UserFileBean userFile) {
        Long beginCount = (fileListDo.getCurrentPage() - 1) * fileListDo.getPageCount();
        List<FileListVo> fileList = userFileService.userFileList(userFile, beginCount, fileListDo.getPageCount());
        LambdaQueryWrapper<UserFileBean> userFileLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userFileLambdaQueryWrapper.eq(UserFileBean::getUserId, userFile.getUserId())
                .eq(UserFileBean::getFilePath, userFile.getFilePath())
                .eq(UserFileBean::getDeleteFlag, 0);
        int total = userFileService.count(userFileLambdaQueryWrapper);
        return R.ok().data("total", total).data("files", fileList);
    }

    private R checkUserLoginAndPermission(UserBean sessionUserBean, String token) {
        if (sessionUserBean == null) {
            return R.error().code(Constant.ERROR_LOGIN).message("not login");
        }
        R result = this.operationCheck(token);
        if (!result.isSuccess()) {
            return R.error().code(Constant.ERROR_NO_PERMISSION).message("没权限，请联系管理员！");
        }
        return null;
    }

    private R operationCheck(String token) {
        UserBean sessionUserBean = userService.getUserBeanByToken(token);
        if (sessionUserBean == null) {
            return R.error().code(Constant.ERROR_LOGIN).message("not login");
        }
        return R.ok();
    }
}
