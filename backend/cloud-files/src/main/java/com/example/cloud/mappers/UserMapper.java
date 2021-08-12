package com.example.cloud.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.cloud.entity.UserBean;

public interface UserMapper extends BaseMapper<UserBean> {
    int insertUser(UserBean userBean);
    int insertUserRole(long userId, long roleId);
}
