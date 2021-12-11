package com.tensquare.article.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.article.pojo.Article;
import com.tensquare.article.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-04 10:54
 * @Description:
 */
@RestController
@RequestMapping(value = "article")
@CrossOrigin
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 文章点赞操作
     * @param articleId
     * @return
     */
    @PutMapping("/thumbup/{articleId}")
    public Result thumbup(@PathVariable("articleId") String articleId) {
        String userId = "5";
        String key = "thumbup_article_" + userId;
        Object flag = redisTemplate.opsForValue().get(key);
        if (flag == null) {
            articleService.thumbup(articleId, userId);
            redisTemplate.opsForValue().set(key, 1);
            return new Result(true, StatusCode.OK, "点赞成功");
        }else {
            return new Result(true, StatusCode.OK, "不能重复点赞");
        }
    }


    @PostMapping(value = "/subscribe")
    public Result subscribe(@RequestBody Map map) {
        Boolean flag = articleService.subscribe(map.get("articleId").toString(), map.get("userId").toString());
        if (flag == true) {
            return new Result(true, StatusCode.OK, "订阅成功！");
        }else {
            return new Result(true, StatusCode.OK, "取消订阅成功！");
        }
    }

    /**
     * 根据ID查询文章
     * @param articleId
     * @return
     */
    @GetMapping("/{articleId}")
    public Result findById(@PathVariable(value = "articleId")String articleId) {
        Article article = articleService.findById(articleId);
        return new Result(true, StatusCode.OK, "查询成功！", article);
    }

    /**
     * 新增文章
     * @param article
     * @return
     */
    @PostMapping
    public Result save(@RequestBody Article article) {
        articleService.save(article);
        return new Result(true, StatusCode.OK, "新增成功！");
    }

    /**
     * 修改
     * @param articleId
     * @param article
     * @return
     */
    @PutMapping("/{articleId}")
    public Result updateById(@PathVariable("articleId") String articleId, @RequestBody Article article) {
        // 设置id
        article.setId(articleId);
        articleService.updateById(article);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     * @param articleId
     * @return
     */
    @DeleteMapping("/{articleId}")
    public Result deleteById(@PathVariable("articleId") String articleId) {
        articleService.deleteById(articleId);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @param map
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result findByPage(@PathVariable("page") Integer page,
                             @PathVariable("size") Integer size,
                             @RequestBody Map<String, Object> map){
        // 根据分页条件查询数据
        Page<Article> pageData = articleService.findByPage(map, page, size);

        // 封装结果集
        PageResult<Article> pageResult = new PageResult<Article>(
                pageData.getTotal(), pageData.getRecords()
        );

        // 返回
        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }
}
