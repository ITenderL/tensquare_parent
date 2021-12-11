package com.tensquare.notice.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.notice.client.ArticleClient;
import com.tensquare.notice.client.UserClient;
import com.tensquare.notice.dao.NoticeDao;
import com.tensquare.notice.dao.NoticeFreshDao;
import com.tensquare.notice.pojo.Notice;
import com.tensquare.notice.pojo.NoticeFresh;
import com.tensquare.notice.service.NoticeService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-07 17:24
 * @Description:
 */
@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private NoticeDao noticeDao;
    @Autowired
    private NoticeFreshDao noticeFreshDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private UserClient userClient;
    @Autowired
    private ArticleClient articleClient;

    /**
     * 完善消息内容
     * @param notice
     */
    private void getInfo(Notice notice) {
        Result userResult = userClient.selectById(notice.getOperatorId());
        HashMap userMap = (HashMap) userResult.getData();
        notice.setOperatorName(userMap.get("nickname").toString());

        Result articleResult = articleClient.findById(notice.getTargetId());
        HashMap articleMap = (HashMap) articleResult.getData();
        notice.setTargetName(articleMap.get("title").toString());
    }
    /**
     * 新增消息通知
     * @param notice
     */
    @Override
    public void save(Notice notice) {
        String id = idWorker.nextId() + "";
        notice.setId(id);
        notice.setState("0"); // 0:未读1:已读
        notice.setCreatetime(new Date());
        noticeDao.insert(notice);
        // 待推送消息入库
        //NoticeFresh noticeFresh = new NoticeFresh();
        //noticeFresh.setNoticeId(notice.getId()); // 消息id
        //noticeFresh.setUserId(notice.getReceiverId()); // 接收人id
        //noticeFreshDao.insert(noticeFresh);
    }

    /**
     * 修改消息通知
     * @param notice
     */
    @Override
    public void updateById(Notice notice) {
        noticeDao.updateById(notice);
    }

    /**
     * 分页查询消息通知
     * @param notice
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Notice> selectByPage(Notice notice, Integer page, Integer size) {
        Page<Notice> pageData = new Page<>(page, size);
        List<Notice> notices = noticeDao.selectPage(pageData, new EntityWrapper<>(notice));
        for (Notice n : notices) {
            getInfo(n);
        }
        pageData.setRecords(notices);
        return pageData;
    }

    @Override
    public Notice selectById(String id) {
        Notice notice = noticeDao.selectById(id);
        getInfo(notice);
        return notice;
    }
    /**
     * 删除待推送消息
     * @param noticeFresh
     */
    @Override
    public void freshDelete(NoticeFresh noticeFresh) {
        noticeFreshDao.delete(new EntityWrapper<>(noticeFresh));
    }

    /**
     * 根据用户id分页查询带推送消息
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<NoticeFresh> freshPage(String userId, Integer page, Integer size) {
        Page<NoticeFresh> freshData = new Page<>(page, size);
        NoticeFresh noticeFresh = new NoticeFresh();
        noticeFresh.setUserId(userId);
        List<NoticeFresh> noticeFreshes = noticeFreshDao.selectPage(freshData, new EntityWrapper<>(noticeFresh));
        freshData.setRecords(noticeFreshes);
        return freshData;
    }
}
