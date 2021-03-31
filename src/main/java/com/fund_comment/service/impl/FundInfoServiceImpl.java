package com.fund_comment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fund_comment.entity.FundInfo;
import com.fund_comment.mapper.FundInfoMapper;
import com.fund_comment.pojo.FundSearchInfo;
import com.fund_comment.pojo.vo.Funds;
import com.fund_comment.service.FundInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fund_comment.spider.FundPostSpider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zoubin
 * @since 2021-03-28
 */
@Service
public class FundInfoServiceImpl extends ServiceImpl<FundInfoMapper, FundInfo> implements FundInfoService {

    @Autowired
    private FundPostSpider fundPostSpider;

    @Override
    public List<Funds> search(String keyword) {
        List<FundSearchInfo> fundSearchInfos = fundPostSpider.searchFundInfo(keyword);
        return fundSearchInfos.stream().map(o -> {
            Funds funds = new Funds();
            funds.setCode(o.getFCODE());
            funds.setName(o.getSHORTNAME());
            funds.setType(o.getFTYPE());
            return funds;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean fundExist(String fundCode) {
        QueryWrapper<FundInfo> query = new QueryWrapper<>();
        query.eq("fund_code", fundCode);
        return getOne(query) != null;
    }

    @Override
    public FundInfo getByFundCode(String fundCode) {
        QueryWrapper<FundInfo> query = new QueryWrapper<>();
        query.eq("fund_code", fundCode);
        return getOne(query);
    }
}
