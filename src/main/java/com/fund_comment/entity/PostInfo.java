package com.fund_comment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zoubin
 * @since 2021-03-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PostInfo extends Model<PostInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 主题ID
     */
    private String topicId;
    /**
     * 基金代码
     */
    private String fundCode;
    /**
     * 帖子
     */
    private String post;
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    /**
     * 发布昵称
     */
    private String writerName;
    /**
     * 评论数
     */
    private Long commentCount;
    /**
     * 点击数
     */
    private Long clickCount;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
