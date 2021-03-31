package com.fund_comment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fund_comment.entity.CommentInfo;
import com.fund_comment.mapper.CommentInfoMapper;
import com.fund_comment.pojo.JsonRootBean;
import com.fund_comment.service.CommentInfoService;
import com.fund_comment.spider.FundPostSpider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zoubin
 * @since 2021-03-28
 */
@Service
public class CommentInfoServiceImpl extends ServiceImpl<CommentInfoMapper, CommentInfo> implements CommentInfoService {

    @Autowired
    FundPostSpider postSpider;


    @Override
    public List<CommentInfo> getComments(String fundCode) {
        List<JsonRootBean> byFundCode = postSpider.getByFundCode(fundCode);
        List<CommentInfo> commentInfos = new ArrayList<>();
        byFundCode.forEach(o -> {
            String postId = o.getNewsId();
            o.getComments().forEach(k -> {
                CommentInfo commentInfo = new CommentInfo();
                commentInfo.setWriterName(k.getNiCheng());
                commentInfo.setTopicId(postId);
                commentInfo.setComment(k.getTextEnd());
                commentInfos.add(commentInfo);
            });
        });
        return commentInfos;
    }

}
