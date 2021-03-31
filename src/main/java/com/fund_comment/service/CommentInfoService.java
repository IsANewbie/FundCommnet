package com.fund_comment.service;

import com.fund_comment.entity.CommentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zoubin
 * @since 2021-03-28
 */
public interface CommentInfoService extends IService<CommentInfo> {
    List<CommentInfo> getComments(String fundCode);
}
