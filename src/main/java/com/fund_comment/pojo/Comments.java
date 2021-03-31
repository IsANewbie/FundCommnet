/**
  * Copyright 2021 json.cn 
  */
package com.fund_comment.pojo;
import lombok.Data;

import java.util.List;

/**
 * Auto-generated: 2021-03-28 17:10:31
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
public class Comments {

    private long Id;
    private long TopicId;
    private List<String> reply_pic;
    private int LouCeng;
    private String Code;
    private String PassportId;
    private String NiCheng;
    private String TextEnd;
    private List<String> ReplyKeyWords;
    private String IP;
    private String HuifuDetails;

}