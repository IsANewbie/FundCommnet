package com.fund_comment.fund;

import com.fund_comment.BaseTest;
import com.fund_comment.service.FundInfoNewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zoubin
 * @description: TODO
 * @date 2021/4/13 22:53
 */
public class FundTest extends BaseTest {
    @Autowired
    FundInfoNewService fundInfoNewService;

    @Test
    public void testSava(){
        fundInfoNewService.savaList();
    }



    public static void main(String[] args) {
        System.out.println("(123456".substring(1));
    }
}
