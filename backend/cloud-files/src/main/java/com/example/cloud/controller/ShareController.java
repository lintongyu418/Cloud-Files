package com.example.cloud.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cloud.entity.ShareBean;
import com.example.cloud.entity.UserBean;
import com.example.cloud.entity.UserFileBean;
import com.example.cloud.pojo.*;
import com.example.cloud.service.ShareFileService;
import com.example.cloud.service.ShareService;
import com.example.cloud.service.UserFileService;
import com.example.cloud.service.UserService;
import com.example.cloud.vo.ShareFileListVo;
import com.example.cloud.vo.ShareFileVo;
import com.example.cloud.vo.ShareListVo;
import com.example.cloud.vo.ShareTypeVo;
import com.example.core.configs.Constant;
import com.example.core.utils.DateUtil;
import com.example.core.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

@Tag(name = "share", description = "该接口为文件分享接口")
@RestController
@Slf4j
@RequestMapping("/share")
public class ShareController {

    @Resource
    UserService userService;

    @Resource
    UserFileService userFileService;

    @Resource
    ShareService shareService;

    @Resource
    ShareFileService shareFileService;

    @Operation(summary = "分享文件", description = "分享文件统一接口", tags = {"share"})
    @PostMapping(value = "/shareFile")
    @ResponseBody
    public R shareFile(@RequestBody ShareFileDo shareSecretDo, @RequestHeader("token") String token) {
        UserBean sessionUserBean = userService.getUserBeanByToken(token);
        if (sessionUserBean == null) {
            return R.error().code(Constant.ERROR_LOGIN).message("not login");
        }
        ShareFileVo shareSecretVO = new ShareFileVo();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        ShareBean share = new ShareBean();
        BeanUtil.copyProperties(shareSecretDo, share);
        share.setShareTime(DateUtil.getCurrentTime());
        share.setUserId(sessionUserBean.getUserId());
        share.setShareStatus(0);
        if (shareSecretDo.getShareType() == 1) {
            String extractionCode = RandomUtil.randomNumbers(6);
            share.setExtractionCode(extractionCode);
            shareSecretVO.setExtractionCode(share.getExtractionCode());
        }

        share.setShareBatchNum(uuid);
        shareService.save(share);

        List<ShareFileBean> fileList = JSON.parseArray(shareSecretDo.getFiles(), ShareFileBean.class);
        List<ShareFileBean> saveFileList = new ArrayList<>();
        for (ShareFileBean shareFile : fileList) {
            UserFileBean userFile = userFileService.getById(shareFile.getUserFileId());
            if (userFile.getUserId().compareTo(sessionUserBean.getUserId()) != 0) {
                return R.error().message("您只能分享自己的文件");
            }
            if (userFile.getIsDir() == 1) {
                List<UserFileBean> userfileList = userFileService.selectFileListLikeRightFilePath(userFile.getFilePath() + userFile.getFileName() + "/", sessionUserBean.getUserId());
                for (UserFileBean userFile1 : userfileList) {
                    ShareFileBean shareFile1 = new ShareFileBean();
                    shareFile1.setUserFileId(userFile1.getUserFileId());
                    shareFile1.setShareBatchNum(uuid);
                    shareFile1.setShareFilePath(userFile1.getFilePath().replaceFirst(userFile.getFilePath(), "/"));
                    saveFileList.add(shareFile1);
                }
            }
            shareFile.setShareFilePath("/");
            shareFile.setShareBatchNum(uuid);
            saveFileList.add(shareFile);


        }
        shareFileService.batchInsertShareFile(saveFileList);
        shareSecretVO.setShareBatchNum(uuid);

        return R.ok().data("share", shareSecretVO);
    }

    @Operation(summary = "分享文件列表", description = "分享列表", tags = {"share"})
    @GetMapping(value = "/shareFileList")
    @ResponseBody
    public R shareFileList(ShareFileListDo shareFileListBySecretDTO) {
        String shareBatchNum = shareFileListBySecretDTO.getShareBatchNum();
        String shareFilePath = shareFileListBySecretDTO.getShareFilePath();
        List<ShareFileListVo> list = shareFileService.selectShareFileList(shareBatchNum, shareFilePath);
        for (ShareFileListVo shareFileListVO : list) {
            shareFileListVO.setShareFilePath(shareFilePath);
        }
        return R.ok().data("list", list);
    }

    @Operation(summary = "分享类型", description = "可用此接口判断是否需要提取码", tags = {"share"})
    @GetMapping(value = "/shareType")
    @ResponseBody
    public R shareType(ShareTypeDo shareTypeDTO) {
        LambdaQueryWrapper<ShareBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShareBean::getShareBatchNum, shareTypeDTO.getShareBatchNum());
        ShareBean share = shareService.getOne(lambdaQueryWrapper);
        ShareTypeVo shareTypeVO = new ShareTypeVo();
        shareTypeVO.setShareType(share.getShareType());
        return R.ok().data("shareType", shareTypeVO.getShareType());
    }

    @Operation(summary = "校验提取码", description = "校验提取码", tags = {"share"})
    @GetMapping(value = "/checkCode")
    @ResponseBody
    public R checkExtractionCode(CheckExtractionCodeDo checkExtractionCodeDTO) {
        LambdaQueryWrapper<ShareBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShareBean::getShareBatchNum, checkExtractionCodeDTO.getShareBatchNum())
                .eq(ShareBean::getExtractionCode, checkExtractionCodeDTO.getExtractionCode());
        List<ShareBean> list = shareService.list(lambdaQueryWrapper);
        if (list.isEmpty()) {
            return R.error().message("校验失败");
        } else {
            return R.ok();
        }
    }

    @Operation(summary = "校验过期时间", description = "校验过期时间", tags = {"share"})
    @GetMapping(value = "/checkEndTime")
    @ResponseBody
    public R checkEndTime(CheckEndTimeDo checkEndTimeDTO) {
        LambdaQueryWrapper<ShareBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShareBean::getShareBatchNum, checkEndTimeDTO.getShareBatchNum());
        ShareBean share = shareService.getOne(lambdaQueryWrapper);
        String endTime = share.getEndTime();
        Date endTimeDate = null;
        try {
            endTimeDate = DateUtil.getDateByFormatString(endTime, "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            log.error("日期解析失败：{}", e);
        }
        assert endTimeDate != null;
        if (new Date().after(endTimeDate)) {
            return R.error().message("分享已过期");
        } else {
            return R.ok();
        }
    }

    @Operation(summary = "查看已分享列表", description = "查看已分享列表", tags = {"share"})
    @GetMapping(value = "/shareList")
    @ResponseBody
    public R shareList(ShareListDo shareListDTO, @RequestHeader("token") String token) {
        UserBean sessionUserBean = userService.getUserBeanByToken(token);
        if (sessionUserBean == null) {
            return R.error().code(Constant.ERROR_LOGIN).message("not login");
        }
        List<ShareListVo> shareList = shareService.selectShareList(shareListDTO, sessionUserBean.getUserId());
        int total = shareService.selectShareListTotalCount(shareListDTO, sessionUserBean.getUserId());
        return R.ok().data("total", total).data("list", shareList);
    }
}
