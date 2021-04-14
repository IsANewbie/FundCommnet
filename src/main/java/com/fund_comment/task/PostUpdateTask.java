package com.fund_comment.task;

import com.fund_comment.service.FundInfoService;
import com.fund_comment.service.PostInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author zoubin
 * @date 2021/3/30
 */
@Component
@Slf4j
public class PostUpdateTask {
    @Autowired
    PostInfoService postInfoService;
    @Autowired
    FundInfoService fundInfoService;

    @Scheduled(cron = "0 */30 * * * ?")
    private void updatePost(){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        log.info("{}开始定时任务", LocalDateTime.now());
        doTask();
    }

    void doTask(){
//        List<FundInfo> list = fundInfoService.list();
//        list.stream().filter(o -> o.getStatus().equals(1)).forEach(o -> {
//            List<PostInfo> postListByFundCode = postInfoService.getPostListByFundCode(o.getFundCode());
//            postInfoService.batchSave(postListByFundCode);
//        });
    }
}
