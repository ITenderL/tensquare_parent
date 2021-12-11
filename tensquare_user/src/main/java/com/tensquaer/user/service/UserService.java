package com.tensquaer.user.service;

import com.tensquaer.user.pojo.User;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-07 14:15
 * @Description:
 */
public interface UserService {
    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    User selectById(String id);
}
