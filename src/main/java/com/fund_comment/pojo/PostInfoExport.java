package com.fund_comment.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author Monkey
 * @since 2021-04-12
 */
@Data
@NoArgsConstructor
public class PostInfoExport {

    private static final long serialVersionUID = 1L;

    /**
     * 基金名称
     */
    @ExcelProperty(value = "基金名称",index = 0)
    private String fundName;
    /**
     * 基金代码
     */
    @ExcelProperty(value = "基金代码",index = 1)
    private String fundCode;
    /**
     * 帖子
     */
    @ExcelProperty(value = "评论内容",index = 2)
    private String post;
    /**
     * 发布时间
     */
    @ExcelProperty(value = "发布时间",index = 4)
    private String publishTime;
    /**
     * 发布昵称
     */
    @ExcelProperty(value = "发布者昵称",index = 5)
    private String writerName;
    /**
     * 评论数
     */
    @ExcelProperty(value = "评论数",index = 6)
    private String commentCount;
    /**
     * 点击数
     */
    @ExcelProperty(value = "点击数",index = 7)
    private String clickCount;

}
