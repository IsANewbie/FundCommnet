package com.fund_comment.post;

import com.alibaba.excel.EasyExcel;
import com.fund_comment.BaseTest;
import com.fund_comment.entity.FundInfoNew;
import com.fund_comment.entity.PostInfoNew;
import com.fund_comment.pojo.PostInfoExport;
import com.fund_comment.service.PostInfoNewService;
import com.fund_comment.spider.FundPostSpider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zoubin
 * @description: TODO
 * @date 2021/4/13 23:43
 */
public class PostTest extends BaseTest {

    @Autowired
    FundPostSpider fundPostSpider;

    @Autowired
    PostInfoNewService postInfoNewService;

    @Test
    public void exportTest(){
        String excelPath = "H://fund_comments//";
        FundInfoNew fundInfoNew = new FundInfoNew();
        fundInfoNew.setFundName("易方达瑞恒灵活配置混合");
        fundInfoNew.setFundCode("001832");
        List<PostInfoNew> postInfoNew = fundPostSpider.getPostInfoNew(fundInfoNew);
        List<PostInfoExport> postInfoExports = postInfoNew.stream().map(k -> {
            PostInfoExport output = new PostInfoExport();
            BeanUtils.copyProperties(k, output);
            return output;
        }).collect(Collectors.toList());
        String fileName = String.format("%s(%s).xlsx",fundInfoNew.getFundName(), fundInfoNew.getFundCode());
        EasyExcel.write(excelPath+fileName,PostInfoExport.class).sheet("评论列表").doWrite(postInfoExports);
    }

    @Test
    public void exportBatch() {
        postInfoNewService.save();
    }
}
