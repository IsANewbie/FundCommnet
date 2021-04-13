package com.fund_comment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author Monkey
 * @since 2021-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
public class PostInfoNew extends Model<PostInfoNew> {

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
     * 基金名称
     */
    private String fundName;
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
    private String publishTime;
    /**
     * 发布昵称
     */
    private String writerName;
    /**
     * 评论数
     */
    private String commentCount;
    /**
     * 点击数
     */
    private String clickCount;
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
