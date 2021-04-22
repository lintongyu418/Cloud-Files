package com.example.cloud.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.example.cloud.entity.UserBean;
import com.example.cloud.pojo.RegisterDo;
import com.example.cloud.service.UserService;
import com.example.cloud.vo.UserLoginVo;
import com.example.cloud.vo.UserVo;
import com.example.core.configs.Constant;
import com.example.core.utils.JwtUtils;
import com.example.core.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Tag(name = "user", description = "用户相关api")
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    public static final String CURRENT_MODULE = "用户管理";

    @Operation(summary = "用户注册", description = "注册账号", tags = {"user"})
    @PostMapping(value = "/create")
    public R createUser(@RequestBody RegisterDo register) {
        UserBean userBean = new UserBean();
        BeanUtils.copyProperties(register, userBean);
        return userService.registerUser(userBean);
    }

    @Operation(summary = "user login", description = "user login", tags = {"user"})
    @GetMapping(value = "/login")
    public R userLogin(
            @Parameter(description = "username") String username,
            @Parameter(description = "password") String password) {
        UserBean savedUserBean = userService.findUserInfoByTelephone(username);
        if (savedUserBean == null) {
            return R.error().code(Constant.ERROR_USER_NOT_EXIST).message("user not exist");
        }
        String jwt = "";
        try {
            jwt = JwtUtils.createJWT("neu", "jack", JSON.toJSONString(savedUserBean));
        } catch (Exception e) {
            log.info("登陆失败", e);
            return R.error().code(Constant.ERROR_LOGIN).message("登陆失败");
        }
        String passwordHash = new SimpleHash("MD5", password, savedUserBean.getSalt(), 1024).toHex();
        if (passwordHash.equals(savedUserBean.getPassword())) {
            UserLoginVo userLoginVo = new UserLoginVo();
            BeanUtil.copyProperties(savedUserBean, userLoginVo);
            userLoginVo.setToken(jwt);
            return R.ok().code(Constant.SUCCESS_RESPONSE).data("user", userLoginVo);
        } else {
            return R.error().code(Constant.ERROR_LOGIN).message("手机号或者密码错误");
        }
    }

    @Operation(summary = "检查用户登录信息", description = "验证token的有效性", tags = {"user"})
    @GetMapping("/check")
    @ResponseBody
    public R checkUserLoginInfo(@RequestHeader("token") String token) {
        if ("undefined".equals(token) || StringUtils.isEmpty(token)) {
            return R.error().code(Constant.ERROR_LOGIN).message("用户暂未登录");
        }
        UserBean sessionUserBean = userService.getUserBeanByToken(token);
        if (sessionUserBean != null) {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(sessionUserBean, userVo);
            return R.ok().code(Constant.SUCCESS_RESPONSE).data("userInfo", userVo);
        } else {
            return R.error().code(Constant.ERROR_LOGIN).message("用户暂未登录");
        }

    }
}
