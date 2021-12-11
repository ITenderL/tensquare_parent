package com.tensquaer.user.service.impl;

import com.tensquaer.user.dao.UserDao;
import com.tensquaer.user.pojo.User;
import com.tensquaer.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-07 14:16
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @Override
    public User selectById(String id) {
        return userDao.selectById(id);
    }
}
