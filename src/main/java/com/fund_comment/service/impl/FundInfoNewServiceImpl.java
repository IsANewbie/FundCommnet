package com.fund_comment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fund_comment.entity.FundInfoNew;
import com.fund_comment.mapper.FundInfoNewMapper;
import com.fund_comment.service.FundInfoNewService;
import com.fund_comment.spider.FundPostSpider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Monkey
 * @since 2021-04-12
 */
@Service
public class FundInfoNewServiceImpl extends ServiceImpl<FundInfoNewMapper, FundInfoNew> implements FundInfoNewService {

    @Autowired
    private FundPostSpider fundPostSpider;

    @Override
    public void savaList() {
        List<FundInfoNew> allFundInfo = fundPostSpider.getAllFundInfo();
        this.saveBatch(allFundInfo);
    }
}
