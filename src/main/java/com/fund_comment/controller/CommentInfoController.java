package com.fund_comment.controller;

import com.fund_comment.utils.JsonUtil;
import com.fund_comment.entity.CommentInfo;
import com.fund_comment.service.CommentInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * CommentInfoController控制器
 *
 * @author zoubin
 * @Date 2021-03-28
 */
@Controller
@RequestMapping("/commentInfo")
@Api(value = "CommentInfoController-controller", description = "CommentInfoController")
public class CommentInfoController   {

    @Autowired
    private CommentInfoService commentInfoService;



    /**
     * 获取CommentInfo列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取CommentInfo列表", notes = "获取CommentInfo列表")
    public Object list(String condition) {
        List<CommentInfo> list = commentInfoService.list(null);
        return JsonUtil.createPageObject(list);
    }

    /**
     * 新增CommentInfo
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "新增CommentInfo", notes = "新增CommentInfo")
    public Object add(CommentInfo commentInfo) {
        commentInfoService.save(commentInfo);
        return JsonUtil.createOkJson();
    }

    /**
     * 删除CommentInfo
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "删除CommentInfo", notes = "删除CommentInfo")
    public Object delete(@RequestParam Integer commentInfoId) {
        commentInfoService.removeById(commentInfoId);
        return JsonUtil.createOkJson();
    }

    /**
     * 修改CommentInfo
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "修改CommentInfo", notes = "修改CommentInfo")
    public Object update(CommentInfo commentInfo) {
        commentInfoService.updateById(commentInfo);
        return JsonUtil.createOkJson();
    }

    /**
     * CommentInfo详情
     */
    @RequestMapping(value = "/detail/{commentInfoId}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "CommentInfo详情", notes = "CommentInfo详情")
    public Object detail(@PathVariable("commentInfoId") Integer commentInfoId) {
        CommentInfo obj = commentInfoService.getById(commentInfoId);
        return  JsonUtil.createOkObject(obj);
    }
}
