package com.tensquare.article.controller;

import com.tensquare.article.pojo.Comment;
import com.tensquare.article.service.CommentService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-04 15:54
 * @Description:
 */
@RestController
@RequestMapping(value = "comment")
@CrossOrigin
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 评论点赞
     * @param commentId
     * @return
     */
    @PutMapping(value = "/thumbup/{commentId}")
    public Result thumbup(@PathVariable(value = "commentId") String commentId) {
        String userId = "123";
        Object flag = redisTemplate.opsForValue().get("Thumbup_" + userId + "_" + commentId);
        if (flag == null) {
            redisTemplate.opsForValue().set("Thumbup_" + userId + "_" + commentId, 1);
            commentService.thumbup(commentId);
            return new Result(true, StatusCode.OK, "点赞成功！");
        }
        return new Result(true, StatusCode.OK, "请不要重复点赞！");
    }

    /**
     * 根据文章id查询评论
     * @param articleId
     * @return
     */
    @GetMapping(value = "/article/{articleId}")
    public Result findByArticleId(@PathVariable(value = "articleId") String articleId) {
        List<Comment> comments = commentService.findByArticleId(articleId);
        return new Result(true, StatusCode.OK, "根据文章查询评论成功！",comments);
    }

    /**
     * 查询评论集合
     * @return
     */
    @GetMapping
    public Result findAll() {
        List<Comment> comments = commentService.findAll();
        return new Result(true, StatusCode.OK, "查询评论集合成功！", comments);
    }

    /**
     * 根据id查询评论
     * @return
     */
    @GetMapping(value = "/{id}")
    public Result findById(@PathVariable(value = "id") String id) {
        Comment comment = commentService.findById(id);
        return new Result(true, StatusCode.OK, "查询评论成功！", comment);
    }

    /**
     * 新增评论
     * @param comment
     * @return
     */
    @PostMapping
    public Result save(@RequestBody Comment comment) {
        commentService.save(comment);
        return new Result(true, StatusCode.OK, "新增评论成功！");
    }

    /**
     * 修改评论
     * @param commentId
     * @param comment
     * @return
     */
    @PutMapping(value = "/{commentId}")
    public Result update(@PathVariable(value = "commentId") String commentId, @RequestBody Comment comment) {
        comment.set_id(commentId);
        commentService.updateById(comment);
        return new Result(true, StatusCode.OK, "修改评论成功");
    }

    /**
     * 删除评论
     * @param commentId
     * @return
     */
    @DeleteMapping
    public Result deleteById(@PathVariable(value = "commentId") String commentId) {
        commentService.deleteById(commentId);
        return new Result(true, StatusCode.OK, "删除评论成功");
    }
}
