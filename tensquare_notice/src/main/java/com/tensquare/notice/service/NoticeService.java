package com.tensquare.notice.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.notice.pojo.Notice;
import com.tensquare.notice.pojo.NoticeFresh;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-07 17:23
 * @Description:
 */
public interface NoticeService {

    /**
     * 删除待推送消息
     * @param noticeFresh
     */
    void freshDelete(NoticeFresh noticeFresh);
    /**
     * 根据用户id分页查询带推送消息
     * @param userId
     * @param page
     * @param size
     * @return
     */
    Page<NoticeFresh> freshPage(String userId, Integer page, Integer size);
    /**
     * 新增通知
     * @param notice
     */
    void save(Notice notice);
    /**
     * 修改消息通知
     * @param notice
     */
    void updateById(Notice notice);
    /**
     * 分页查询消息通知
     * @param notice
     * @param page
     * @param size
     * @return
     */
    Page<Notice> selectByPage(Notice notice, Integer page, Integer size);
    /**
     * 根据id查询消息通知
     * @param id
     * @return
     */
    Notice selectById(String id);
}
