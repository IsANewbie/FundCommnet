package com.fund_comment.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fund_comment.entity.FundInfoNew;
import com.fund_comment.entity.PostInfoNew;
import com.fund_comment.mapper.FundInfoNewMapper;
import com.fund_comment.mapper.PostInfoNewMapper;
import com.fund_comment.pojo.JsonRootBean;
import com.fund_comment.pojo.PostInfoExport;
import com.fund_comment.service.FundInfoNewService;
import com.fund_comment.service.PostInfoNewService;
import com.fund_comment.spider.FundPostSpider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Monkey
 * @since 2021-04-12
 */
@Service
@Slf4j
public class PostInfoNewServiceImpl extends ServiceImpl<PostInfoNewMapper, PostInfoNew> implements PostInfoNewService {

    @Autowired
    FundInfoNewService fundInfoNewService;
    @Autowired
    FundInfoNewMapper fundInfoNewMapper;
    @Autowired
    FundPostSpider fundPostSpider;

    @Override
    public void save() {
        String excelPath = "H://fund_comments//";
        Page<FundInfoNew> page = new Page<>(1, 1000);
        IPage<FundInfoNew> pages = fundInfoNewMapper.selectPage(page, null);
        pages.getRecords().forEach(o -> {
            try {

                List<PostInfoNew> postInfoNew = fundPostSpider.getPostInfoNew(o);
                if (postInfoNew != null && postInfoNew.size() > 0) {
                    List<PostInfoExport> postInfoExports = postInfoNew.stream().map(k -> {
                        PostInfoExport output = new PostInfoExport();
                        BeanUtils.copyProperties(k, output);
                        return output;
                    }).collect(Collectors.toList());
                    String fileName = String.format("%s(%s).xlsx", o.getFundName(), o.getFundCode());
                    EasyExcel.write(excelPath + fileName, PostInfoExport.class).sheet("评论").doWrite(postInfoExports);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void export(int pageNum, int pageSize) {
        // // TODO: 2021/4/14 改成你的盘 
        String excelPath = "H://fund_comments//new//";
        Page<FundInfoNew> page = new Page<>(pageNum, pageSize);
        QueryWrapper<FundInfoNew> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("id", 2533);
        IPage<FundInfoNew> pages = fundInfoNewMapper.selectPage(page, queryWrapper);
        pages.getRecords().forEach(fundInfoNew -> {
            try {
                log.info("当前拉取基金：{}", fundInfoNew);
                Thread.sleep(5000);
                List<JsonRootBean> byFundCodeStartInfo = fundPostSpider.getByFundCodeStartInfo(fundInfoNew.getFundCode());
                String timePointStr = byFundCodeStartInfo.get(byFundCodeStartInfo.size() - 1).getTimePointStr();
                boolean fetchPost = true;
//                int i = 2;
                while (fetchPost) {
//                    log.info("第{}次", i++);
                    Thread.sleep(500);
                    List<JsonRootBean> byFundCode = fundPostSpider.getByFundCode(fundInfoNew.getFundCode(), timePointStr);
                    if (byFundCode != null && byFundCode.size() > 0) {
                        timePointStr = byFundCode.get(byFundCode.size() - 1).getTimePointStr();
                        byFundCodeStartInfo.addAll(byFundCode);
                    } else {
                        fetchPost = false;
                    }
                }
                log.info("共有帖子：{}", byFundCodeStartInfo.size());
                List<PostInfoExport> exportList = byFundCodeStartInfo.stream()
                        .filter(o -> o != null && o.getPostUserInfo() != null)
                        .map(o -> {
                            PostInfoNew postInfoNew = PostInfoNew.builder()
                                    .post(o.getContent())
                                    .publishTime(o.getPublishTime())
                                    .fundName(fundInfoNew.getFundName())
                                    .fundCode(fundInfoNew.getFundCode())
                                    .clickCount(o.getPostClickCount() + "")
                                    .commentCount(o.getPostCommentCount() + "")
                                    .writerName(o.getPostUserInfo().getNiCheng())
                                    .build();
                            PostInfoExport output = new PostInfoExport();
                            BeanUtils.copyProperties(postInfoNew, output);
                            return output;
                        }).collect(Collectors.toList());
                String fileName = String.format("%s(%s).xlsx", fundInfoNew.getFundName(), fundInfoNew.getFundCode());
                EasyExcel.write(excelPath + fileName, PostInfoExport.class).sheet("评论列表").doWrite(exportList);
            } catch (Exception e) {
                log.error("炸了,废物基金：{}，id：{}", fundInfoNew.getFundName(), fundInfoNew.getId());
            }
        });

    }


}
