package com.tensquare.notice.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-08 10:53
 * @Description:
 */
@FeignClient("tensquare-article")
@RequestMapping(value = "article")
public interface ArticleClient {
    /**
     * 根据ID查询文章
     * @param articleId
     * @return
     */
    @GetMapping("/{articleId}")
    Result findById(@PathVariable(value = "articleId")String articleId);
}
