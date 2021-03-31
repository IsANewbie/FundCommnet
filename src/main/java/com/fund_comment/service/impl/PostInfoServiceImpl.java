package com.fund_comment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fund_comment.entity.PostInfo;
import com.fund_comment.mapper.PostInfoMapper;
import com.fund_comment.pojo.JsonRootBean;
import com.fund_comment.service.PostInfoService;
import com.fund_comment.spider.FundPostSpider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zoubin
 * @since 2021-03-28
 */
@Service
@Slf4j
public class PostInfoServiceImpl extends ServiceImpl<PostInfoMapper, PostInfo> implements PostInfoService {

    @Autowired
    FundPostSpider postSpider;

    @Override
    public void batchSave(List<PostInfo> posts) {
        saveBatch(posts);
    }

    @Override
    public List<PostInfo> getPostListByFundCode(String fundCode) {
        List<JsonRootBean> posts = postSpider.getByFundCode(fundCode);
        log.info("基金代码：{}，帖子数目前爬取到：{}", fundCode, posts.size());
        PostInfo lastPost = getLastPost(fundCode);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime lastPublishTime = lastPost == null ? LocalDateTime.parse("1997-01-01 00:00", fmt) : lastPost.getPublishTime();
        return posts.stream().filter(o -> LocalDateTime.parse(o.getPublishTime(), fmt).isAfter(lastPublishTime)).map(o -> {
            String postId = o.getNewsId();
            PostInfo postInfo = new PostInfo();
            postInfo.setTopicId(postId);
            postInfo.setPost(o.getContent());
            postInfo.setFundCode(fundCode);
            postInfo.setClickCount(Long.valueOf(o.getPostClickCount()));
            postInfo.setCommentCount(Long.valueOf(o.getPostCommentCount()));
            postInfo.setPublishTime(LocalDateTime.parse(o.getPublishTime(), fmt));
            postInfo.setWriterName(o.getPostUserInfo().getNiCheng());
            return postInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public PostInfo getLastPost(String fundCode) {
        QueryWrapper<PostInfo> query = new QueryWrapper<>();
        query.orderByDesc("publish_time");
        query.eq("fund_code", fundCode);
        query.last("limit 1");
        return baseMapper.selectOne(query);
    }

    @Override
    public IPage<PostInfo> getPage(String fundCode, Integer pageSize, Integer current) {
        IPage<PostInfo> page = new Page<>(current, pageSize);
        QueryWrapper<PostInfo> query = new QueryWrapper<>();
        query.eq("fund_code", fundCode);
        query.orderByDesc("publish_time");
        return baseMapper.selectPage(page, query);
    }
}
