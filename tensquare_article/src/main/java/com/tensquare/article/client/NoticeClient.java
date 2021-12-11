package com.tensquare.article.client;

import com.tensquare.article.pojo.Notice;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-08 13:57
 * @Description:
 */
@FeignClient("tensquare-notice")
@RequestMapping(value = "/notice")
public interface NoticeClient {
    /**
     * 新增消息通知
     * @param notice
     * @return
     */
    @PostMapping
    Result save(@RequestBody Notice notice);
}
