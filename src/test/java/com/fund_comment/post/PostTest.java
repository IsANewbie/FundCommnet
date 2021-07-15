package com.fund_comment.post;

import com.fund_comment.BaseTest;
import com.fund_comment.service.PostInfoNewService;
import com.fund_comment.spider.FundPostSpider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    public void exportTest() {
        /*String excelPath = "H://fund_comments//";
        FundInfoNew fundInfoNew = new FundInfoNew();
        fundInfoNew.setFundName("易方达瑞恒灵活配置混合");
        fundInfoNew.setFundCode("001832");
        List<PostInfoNew> postInfoNew = fundPostSpider.getPostInfoNew(fundInfoNew);
        List<PostInfoExport> postInfoExports = postInfoNew.stream().map(k -> {
            PostInfoExport output = new PostInfoExport();
            BeanUtils.copyProperties(k, output);
            return output;
        }).collect(Collectors.toList());
        String fileName = String.format("%s(%s).xlsx", fundInfoNew.getFundName(), fundInfoNew.getFundCode());
        EasyExcel.write(excelPath + fileName, PostInfoExport.class).sheet("评论列表").doWrite(postInfoExports);*/
    }

    @Test
    public void exportBatch() {
        // // TODO: 2021/4/14 你用双数页，一次爬完手动输入  共12212/2000 7页
//        int[] j = {1,2,3,4,5,6,7};
        /*for (int i = 1; i <= 18; i++) {
            postInfoNewService.export(i, 500);
        }*/

    }

    @Test
    public void test() {
        /*String excelPath = "H://fund_comments//new//";
        FundInfoNew fundInfoNew = new FundInfoNew();
        fundInfoNew.setFundName("易方达中小盘混合");
        fundInfoNew.setFundCode("110011");
        List<JsonRootBean> byFundCodeStartInfo = fundPostSpider.getByFundCodeStartInfo(fundInfoNew.getFundCode());
        String timePointStr = byFundCodeStartInfo.get(byFundCodeStartInfo.size() - 1).getTimePointStr();
        boolean fetchPost = true;
        int i = 2;
        while (fetchPost) {
            System.out.println("第" + i++ + "次");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<JsonRootBean> byFundCode = fundPostSpider.getByFundCode(fundInfoNew.getFundCode(), timePointStr);
            if (byFundCode != null && byFundCode.size() > 0) {
                timePointStr = byFundCode.get(byFundCode.size() - 1).getTimePointStr();
                byFundCodeStartInfo.addAll(byFundCode);

            } else {
                fetchPost = false;
            }
        }
        System.out.println("共有帖子：" + byFundCodeStartInfo.size());
        List<PostInfoNew> collect = byFundCodeStartInfo.stream()
                .filter(o -> o != null && o.getPostUserInfo() != null)
                .map(o -> PostInfoNew.builder()
                        .post(o.getContent())
                        .publishTime(o.getPublishTime())
                        .fundName(fundInfoNew.getFundName())
                        .fundCode(fundInfoNew.getFundCode())
                        .clickCount(o.getPostClickCount() + "")
                        .commentCount(o.getPostCommentCount() + "")
                        .writerName(o.getPostUserInfo().getNiCheng())
                        .build())
                .collect(Collectors.toList());
        String fileName = String.format("%s(%s).xlsx", fundInfoNew.getFundName(), fundInfoNew.getFundCode());
        EasyExcel.write(excelPath + fileName, PostInfoExport.class).sheet("评论列表").doWrite(collect);*/

    }
}
