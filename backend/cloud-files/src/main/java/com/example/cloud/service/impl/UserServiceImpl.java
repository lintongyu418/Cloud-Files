package com.example.cloud.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cloud.entity.UserBean;
import com.example.cloud.mappers.UserMapper;
import com.example.cloud.service.UserService;
import com.example.core.configs.Constant;
import com.example.core.utils.DateUtil;
import com.example.core.utils.JwtUtils;
import com.example.core.utils.PasswordUtil;
import com.example.core.utils.R;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, UserBean> implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public UserBean getUserBeanByToken(String token) {
        Claims claims = null;
        try {
            log.debug("user token: " + token);
            claims = JwtUtils.parseJWT(token);
        } catch (Exception e) {
            log.error("解码异常");
            return null;
        }
        if (claims == null) {
            log.debug("解码为空");
            return null;
        }
        String subject = claims.getSubject();
        log.debug("解析结果：" + subject);
        UserBean tokenUserBean = JSON.parseObject(subject, UserBean.class);
        UserBean saveUserBean = new UserBean();
        String tokenPassword = "";
        String savePassword = "";
        if (StringUtils.isNotEmpty(tokenUserBean.getPassword())) {
            saveUserBean = findUserInfoByTelephone(tokenUserBean.getTelephone());
            if (saveUserBean == null) {
                return null;
            }
            tokenPassword = tokenUserBean.getPassword();
            savePassword = saveUserBean.getPassword();
        }
        if (StringUtils.isEmpty(tokenPassword) || StringUtils.isEmpty(savePassword)) {
            return null;
        }
        if (tokenPassword.equals(savePassword)) {
            return saveUserBean;
        }
        return null;
    }

    @Override
    public R registerUser(UserBean userBean) {
        if (userBean.getTelephone() == null || "".equals(userBean.getTelephone())) {
            return R.error().code(Constant.ERROR_REGISTER_USER).message("手机号不能为空！");
        }
        if (userBean.getPassword() == null || "".equals(userBean.getPassword())) {
            return R.error().code(Constant.ERROR_REGISTER_USER).message("密码不能为空！");
        }

        if (userBean.getUsername() == null || "".equals(userBean.getUsername())) {
            return R.error().code(Constant.ERROR_REGISTER_USER).message("用户名不能为空！");
        }
        if (isUserNameExit(userBean)) {
            return R.error().code(Constant.ERROR_REGISTER_USER).message("用户名已存在！");
        }
        if (isPhoneExit(userBean)) {
            return R.error().code(Constant.ERROR_REGISTER_USER).message("手机号已存在！");
        }
        String salt = PasswordUtil.getSaltValue();
        String newPassword = new SimpleHash("MD5", userBean.getPassword(), salt, 1024).toHex();
        userBean.setSalt(salt);
        userBean.setPassword(newPassword);
        userBean.setRegisterTime(DateUtil.getCurrentTime());
        int result = userMapper.insertUser(userBean);
        userMapper.insertUserRole(userBean.getUserId(), 2);
        if (result == 1) {
            return R.ok();
        } else {
            return R.error().code(Constant.ERROR_REGISTER_USER).message("注册用户失败，请检查输入信息！");
        }
    }

    @Override
    public UserBean findUserInfoByTelephone(String telephone) {
        LambdaQueryWrapper<UserBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserBean::getTelephone, telephone);
        return userMapper.selectOne(lambdaQueryWrapper);
    }

    private Boolean isUserNameExit(UserBean userBean) {
        LambdaQueryWrapper<UserBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserBean::getUsername, userBean.getUsername());
        List<UserBean> list = userMapper.selectList(lambdaQueryWrapper);
        return list != null && !list.isEmpty();
    }

    private Boolean isPhoneExit(UserBean userBean) {

        LambdaQueryWrapper<UserBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserBean::getTelephone, userBean.getTelephone());
        List<UserBean> list = userMapper.selectList(lambdaQueryWrapper);
        return list != null && !list.isEmpty();
    }
}
