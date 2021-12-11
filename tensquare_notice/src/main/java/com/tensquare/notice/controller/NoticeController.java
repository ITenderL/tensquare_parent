package com.tensquare.notice.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.notice.pojo.Notice;
import com.tensquare.notice.pojo.NoticeFresh;
import com.tensquare.notice.service.NoticeService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-06-07 17:22
 * @Description:
 */
@RestController
@RequestMapping(value = "notice")
@CrossOrigin
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    /**
     * 删除待推送消息
     * @param noticeFresh
     * @return
     */
    @DeleteMapping(value = "/fresh")
    public Result freshDelete(@RequestBody NoticeFresh noticeFresh) {
        noticeService.freshDelete(noticeFresh);
        return new Result(true, StatusCode.OK, "删除待推送消息成功！");
    }
    /**
     * 根据userId分页查询待推送消息
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/fresh/{userId}/{page}/{size}")
    public Result selectFreshPage(@PathVariable(value = "userId") String userId,
                               @PathVariable(value = "page") Integer page,
                               @PathVariable(value = "size") Integer size) {
        Page<NoticeFresh> pageData = noticeService.freshPage(userId, page, size);
        PageResult<NoticeFresh> result = new PageResult<>(pageData.getTotal(), pageData.getRecords());
        return new Result(true, StatusCode.OK, "分页查询消息通知成功！", result);
    }
    /**
     * 修改消息通知
     * @param notice
     * @return
     */
    @PutMapping
    public Result updateById(@RequestBody Notice notice){
        noticeService.updateById(notice);
        return new Result(true, StatusCode.OK, "修改消息通知成功！");
    }
    /**
     * 新增消息通知
     * @param notice
     * @return
     */
    @PostMapping
    public Result save(@RequestBody Notice notice) {
        noticeService.save(notice);
        return new Result(true, StatusCode.OK, "新增通知成功！");
    }
    @PostMapping(value = "/search/{page}/{size}")
    public Result selectByPage(@RequestBody Notice notice,
                               @PathVariable(value = "page") Integer page,
                               @PathVariable(value = "size") Integer size) {
        Page<Notice> pageData = noticeService.selectByPage(notice, page, size);
        PageResult<Notice> result = new PageResult<>(pageData.getTotal(), pageData.getRecords());
        return new Result(true, StatusCode.OK, "分页查询消息通知成功！", result);
    }
    @GetMapping(value = "/{id}")
    public Result selectById(@PathVariable(value = "id") String id) {
        Notice notice = noticeService.selectById(id);
        return new Result(true, StatusCode.OK, "查询成功！", notice);
    }
}
