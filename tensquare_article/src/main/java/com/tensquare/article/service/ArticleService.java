package com.tensquare.article.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.article.pojo.Article;

import java.util.Map;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-04 10:55
 * @Description:
 */
public interface ArticleService {
    /**
     * 文章点赞
     * @param articleId
     * @param userId
     */
    void thumbup(String articleId, String userId);

    /**
     * 根据id查询文章
     * @param articleId
     * @return
     */
    Article findById(String articleId);

    /**
     * 新增文章
     * @param article
     */
    void save(Article article);

    /**
     * 修改文章
     * @param article
     */
    void updateById(Article article);

    /**
     * 删除文章
     * @param articleId
     */
    void deleteById(String articleId);

    /**
     * 分页查询文章
     * @param map
     * @param page
     * @param size
     * @return
     */
    Page<Article> findByPage(Map<String, Object> map, Integer page, Integer size);

    /**
     * 订阅操作
     * @param articleId
     * @param userId
     * @return
     */
    Boolean subscribe(String articleId, String userId);
}
