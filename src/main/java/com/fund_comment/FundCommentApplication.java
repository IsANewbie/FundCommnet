package com.fund_comment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.fund_comment.mapper")
@EnableScheduling
public class FundCommentApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundCommentApplication.class, args);
    }

}
