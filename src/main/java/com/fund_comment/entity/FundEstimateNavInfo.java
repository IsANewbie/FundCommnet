package com.fund_comment.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author Monkey
 * @since 2021-07-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
public class FundEstimateNavInfo extends Model<FundEstimateNavInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;
    /**
     * 基金代码
     */
    private String fundCode;
    /**
     * 基金名称
     */
    private String fundName;
    /**
     * 估值日期
     */
    private String estimateDate;
    /**
     * 估值
     */
    private BigDecimal estimateNav;
    /**
     * 估值涨跌
     */
    private String estimateNavRise;
    /**
     * 净值
     */
    private BigDecimal nav;
    /**
     * 净值涨跌
     */
    private String navRise;
    /**
     * 是否可交易
     */
    private Boolean isBuy;
    /**
     * 是否是最后一个交易日
     */
    private Boolean isLastTradeDay;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
