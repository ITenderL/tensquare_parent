package com.tensquare.article.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.article.client.NoticeClient;
import com.tensquare.article.dao.ArticleDao;
import com.tensquare.article.pojo.Article;
import com.tensquare.article.pojo.Notice;
import com.tensquare.article.service.ArticleService;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import util.IdWorker;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-04 10:55
 * @Description:
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private NoticeClient noticeClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 文章订阅操作
     *
     * @param articleId
     * @param userId
     * @return
     */
    @Override
    public Boolean subscribe(String articleId, String userId) {
        Article article = findById(articleId);
        String authorId = article.getUserid();
        // 1.创建rabbitMQ管理器
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate.getConnectionFactory());

        // 2.声明direct类型交换机，处理新增文章的消息
        DirectExchange exchange = new DirectExchange("article_subscribe");
        rabbitAdmin.declareExchange(exchange);

        // 3.创建队列，每个用户都有自己的队列，id区分
        Queue queue = new Queue("article_subscribe_" + userId, true);

        // 4.声明交换机和队列绑定关系，需要队列只收到对应作者的新增文章消息
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(authorId);

        // 用户订阅集合
        String userKey = "article_subscribe_" + userId;
        // 作者订阅者集合
        String authorKey = "article_author_" + authorId;
        // 判断用户的的订阅集合中是否已经订阅过该作者
        Boolean flag = redisTemplate.boundSetOps(userKey).isMember(authorId);
        if (flag == true) {
            // 如果已经订阅，则取消订阅
            // redisTemplate.setKeySerializer(new StringRedisSerializer());
            // 1.删除用户订阅集合中的该作者
            redisTemplate.boundSetOps(userKey).remove(authorId);
            // 2.删除作者订阅者集合中的改用户
            redisTemplate.boundSetOps(authorKey).remove(userId);
            // 删除队列绑定
            rabbitAdmin.removeBinding(binding);
            return false;
        } else {
            // 如果没有订阅该作者，则订阅该作者
            // 1.用户订阅集合中添加该作者
            redisTemplate.boundSetOps(userKey).add(authorId);
            // 2.作者的订阅者集合中添加该用户
            redisTemplate.boundSetOps(authorKey).add(userId);
            // 声明队列
            rabbitAdmin.declareQueue(queue);
            // 声明绑定
            rabbitAdmin.declareBinding(binding);
            return true;
        }
    }

    /**
     * 文章点赞
     *
     * @param articleId
     */
    @Override
    public void thumbup(String articleId, String userId) {
        Article article = articleDao.selectById(articleId);
        article.setThumbup(article.getThumbup() + 1);
        articleDao.updateById(article);
        // 点赞之后，给文章作者发送通知
        Notice notice = new Notice();

        notice.setReceiverId(article.getUserid());
        notice.setOperatorId(userId);
        notice.setAction("publish");
        notice.setTargetType("article");
        notice.setTargetId(articleId);
        notice.setType("user");
        noticeClient.save(notice);
        // 点赞发送消息
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate.getConnectionFactory());
        Queue queue = new Queue("article_thumbup_" + article.getUserid());
        rabbitAdmin.declareQueue(queue);
        rabbitTemplate.convertAndSend("article_thumbup_" + article.getUserid(), articleId);

    }

    /**
     * 根据文章id查询文章信息
     *
     * @param articleId
     * @return
     */
    @Override
    public Article findById(String articleId) {
        return articleDao.selectById(articleId);
    }

    /**
     * 新增文章
     *
     * @param article
     */
    @Override
    public void save(Article article) {
        // 设置作者id
        String userId = "3";
        article.setUserid(userId);

        String id = idWorker.nextId() + "";
        article.setId(id);
        article.setThumbup(0);
        article.setComment(0);
        article.setVisits(0);
        articleDao.insert(article);
        // 用户订阅集合
        // String userKey = "article_subscribe_" + userId;
        // 新增文章后，创建消息，通知给订阅者
        // 作者订阅者集合
        String authorKey = "article_author_" + article.getUserid();
        Set<String> set = redisTemplate.boundSetOps(authorKey).members();
        if (!CollectionUtils.isEmpty(set)) {
            Notice notice = null;
            for (String uid : set) {
                // 设置消息通知
                notice = new Notice();
                notice.setReceiverId(uid);
                notice.setAction("publish");
                notice.setOperatorId(userId);
                notice.setTargetType("article");
                notice.setTargetId(id);
                notice.setType("sys");
                noticeClient.save(notice);
            }
        }
        // 发消息给rabbitMQ
        // 第一个参数：交换机名称，第二个参数：路由键routingkey，第三个参数：消息内容
        rabbitTemplate.convertAndSend("article_subscribe", userId, id);
    }

    /**
     * 修改文章
     *
     * @param article
     */
    @Override
    public void updateById(Article article) {
        articleDao.updateById(article);
    }

    /**
     * 删除文章
     *
     * @param articleId
     */
    @Override
    public void deleteById(String articleId) {
        articleDao.deleteById(articleId);
    }

    /**
     * 条件分页查询文章
     *
     * @param map
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Article> findByPage(Map<String, Object> map, Integer page, Integer size) {
        // 设置查询条件
        EntityWrapper<Article> wrapper = new EntityWrapper<>();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
//            if (map.get(key) != null){
//                wrapper.eq(key, map.get(key));
//            }
            // 把参数加到查询条件中
            wrapper.eq(map.get(key) != null, key, map.get(key));
        }
        // 分页参数
        Page<Article> pageData = new Page<>(page, size);
        // 执行查询
        // 第一个参数分页参数，第二个是查询条件
        List<Article> list = articleDao.selectPage(pageData, wrapper);
        pageData.setRecords(list);
        // 返回结果
        return pageData;
    }

}
