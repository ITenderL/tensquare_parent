package com.tensquare.article.repository;

import com.tensquare.article.pojo.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-03-22 15:06
 * @Description:
 */
public interface CommentRepository extends MongoRepository<Comment, String> {
    // 支持方法名查询方式
    List<Comment> findByArticleid(String articleId);
}
