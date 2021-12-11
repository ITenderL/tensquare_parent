package com.tensquare.article.service.impl;

import com.tensquare.article.pojo.Comment;
import com.tensquare.article.repository.CommentRepository;
import com.tensquare.article.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;
import java.util.List;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-04 15:53
 * @Description:
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 评论点赞
     * @param commentId
     */
    @Override
    public void thumbup(String commentId) {
        //Comment comment = findById(commentId);
        //comment.setThumbup(comment.getThumbup() + 1);
        //commentRepository.save(comment);
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(commentId));
        Update update = new Update();
        update.inc("thumbup", 1);
        /**
         * 第一个参数修改条件
         * 第二个参数修改数值
         */
        mongoTemplate.updateFirst(query, update, "comment");
    }

    /**
     * 查询评论集合
     * @return
     */
    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    /**
     * 根据id查询评论
     * @param id
     * @return
     */
    @Override
    public Comment findById(String id) {
        return commentRepository.findById(id).get();
    }

    /**
     * 新增评论
     * @param comment
     */
    @Override
    public void save(Comment comment) {
        String id = idWorker.nextId() + "";
        comment.set_id(id);
        comment.setThumbup(0);
        comment.setPublishdate(new Date());
        commentRepository.save(comment);
    }

    @Override
    public void updateById(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void deleteById(String commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<Comment> findByArticleId(String articleId) {
        return commentRepository.findByArticleid(articleId);
    }
}
