package com.fund_comment.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zoubin
 * @description: TODO
 * @date 2021/4/12 11:54 下午
 */
@Data
@AllArgsConstructor
public class PostPageInfo {
    int pageSize;
    int totalCount;
    int pageCount;

}
