package com.fund_comment.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fund_comment.entity.FundInfoNew;
import com.fund_comment.entity.PostInfoNew;
import com.fund_comment.mapper.FundInfoNewMapper;
import com.fund_comment.mapper.PostInfoNewMapper;
import com.fund_comment.pojo.PostInfoExport;
import com.fund_comment.service.FundInfoNewService;
import com.fund_comment.service.PostInfoNewService;
import com.fund_comment.spider.FundPostSpider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Monkey
 * @since 2021-04-12
 */
@Service
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
            if (postInfoNew != null && postInfoNew.size() > 0){
                List<PostInfoExport> postInfoExports = postInfoNew.stream().map(k -> {
                    PostInfoExport output = new PostInfoExport();
                    BeanUtils.copyProperties(k, output);
                    return output;
                }).collect(Collectors.toList());
                String fileName = String.format("%s(%s).xlsx",o.getFundName(), o.getFundCode());
                EasyExcel.write(excelPath+fileName,PostInfoExport.class).sheet("评论").doWrite(postInfoExports);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
