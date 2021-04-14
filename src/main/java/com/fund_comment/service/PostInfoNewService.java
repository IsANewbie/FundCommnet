package com.fund_comment.service;

import com.fund_comment.entity.PostInfoNew;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Monkey
 * @since 2021-04-12
 */
public interface PostInfoNewService extends IService<PostInfoNew> {
    void save();

    void export(int pageNum, int pageSize);
}
