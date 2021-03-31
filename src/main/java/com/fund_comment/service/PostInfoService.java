package com.fund_comment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fund_comment.entity.PostInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zoubin
 * @since 2021-03-28
 */
public interface PostInfoService extends IService<PostInfo> {
    void batchSave(List<PostInfo> posts);
    List<PostInfo> getPostListByFundCode(String fundCode);
    PostInfo getLastPost(String fundCode);
    IPage<PostInfo> getPage(String fundCode, Integer pageSize, Integer current);
}
