package com.fund_comment.service;

import com.fund_comment.entity.FundInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fund_comment.pojo.vo.Funds;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zoubin
 * @since 2021-03-28
 */
public interface FundInfoService extends IService<FundInfo> {
    List<Funds> search(String keyword);

    boolean fundExist(String fundCode);

    FundInfo getByFundCode(String fundCode);


}
