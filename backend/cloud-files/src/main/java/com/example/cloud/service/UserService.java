package com.example.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.cloud.entity.UserBean;
import com.example.core.utils.R;

public interface UserService extends IService<UserBean> {
    UserBean getUserBeanByToken(String token);

    R registerUser(UserBean userBean);

    UserBean findUserInfoByTelephone(String telephone);
}
