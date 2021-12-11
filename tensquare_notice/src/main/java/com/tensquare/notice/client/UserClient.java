package com.tensquare.notice.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-08 10:55
 * @Description:
 */
@FeignClient("tensquare-user")
@RequestMapping(value = "user")
public interface UserClient {
    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    Result selectById(@PathVariable(value = "id") String id);
}
