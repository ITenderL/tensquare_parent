package com.tensquare.article.service;

import com.tensquare.article.pojo.Comment;

import java.util.List;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-04 15:53
 * @Description:
 */
public interface CommentService {

    /**
     * 点赞操作
     * @param commentId
     */
    void thumbup(String commentId);
    /**
     * 查询评论集合
     * @return
     */
    List<Comment> findAll();

    /**
     * 根据id查询评论
     * @param id
     * @return
     */
    Comment findById(String id);

    /**
     * 新增评论
     * @param comment
     */
    void save(Comment comment);

    /**
     * 修改评论
     * @param comment
     */
    void updateById(Comment comment);

    /**
     * 删除评论
     * @param commentId
     */
    void deleteById(String commentId);

    /**
     * 根据文章查询评论
     * @param articleId
     * @return
     */
    List<Comment> findByArticleId(String articleId);

}
