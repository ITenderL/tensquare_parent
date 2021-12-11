package com.tensquaer.user.controller;

import com.tensquaer.user.pojo.User;
import com.tensquaer.user.service.UserService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-07 14:15
 * @Description:
 */
@RestController
@RequestMapping(value = "user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public Result selectById(@PathVariable(value = "id") String id) {
        User user = userService.selectById(id);
        return new Result(true, StatusCode.OK, "查询成功！", user);
    }
}
