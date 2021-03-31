package com.fund_comment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fund_comment.entity.FundInfo;
import com.fund_comment.entity.PostInfo;
import com.fund_comment.service.FundInfoService;
import com.fund_comment.service.PostInfoService;
import com.fund_comment.utils.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PostInfoController控制器
 *
 * @author zoubin
 * @Date 2021-03-28
 */
@Controller
@RequestMapping("/postInfo")
@Api(value = "PostInfoController-controller", description = "PostInfoController")
public class PostInfoController {

    @Autowired
    private PostInfoService postInfoService;

    @Autowired
    private FundInfoService fundInfoService;


    /**
     * 获取PostInfo列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取PostInfo列表", notes = "获取PostInfo列表")
    public Object list(String condition) {
        List<PostInfo> list = postInfoService.list(null);
        return JsonUtil.createPageObject(list);
    }

    /**
     * 新增PostInfo
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "新增PostInfo", notes = "新增PostInfo")
    public Object add(PostInfo postInfo) {
        postInfoService.save(postInfo);
        return JsonUtil.createOkJson();
    }

    /**
     * 删除PostInfo
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "删除PostInfo", notes = "删除PostInfo")
    public Object delete(@RequestParam Integer postInfoId) {
        postInfoService.removeById(postInfoId);
        return JsonUtil.createOkJson();
    }

    /**
     * 修改PostInfo
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "修改PostInfo", notes = "修改PostInfo")
    public Object update(PostInfo postInfo) {
        postInfoService.updateById(postInfo);
        return JsonUtil.createOkJson();
    }

    /**
     * PostInfo详情
     */
    @RequestMapping(value = "/detail/{postInfoId}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "PostInfo详情", notes = "PostInfo详情")
    public Object detail(@PathVariable("postInfoId") Integer postInfoId) {
        PostInfo obj = postInfoService.getById(postInfoId);
        return JsonUtil.createOkObject(obj);
    }

    /**
     * PostInfo详情
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ApiOperation(value = "PostInfo详情", notes = "PostInfo详情")
    public String get(String fundCode, Model model, @RequestParam(value = "current", defaultValue = "1") int current,
                      @RequestParam(value = "size", defaultValue = "12") int size) {
        FundInfo fundInfo = fundInfoService.getByFundCode(fundCode);
        model.addAttribute("fund", fundInfo);
        IPage<PostInfo> page = postInfoService.getPage(fundCode, size, current);
        model.addAttribute("page", page);
        return "details";
    }

    /**
     * PostInfo详情
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "PostInfo详情", notes = "PostInfo详情")
    public Object test(String fundCode) {
        List<PostInfo> postListByFundCode = postInfoService.getPostListByFundCode(fundCode);
        postInfoService.batchSave(postListByFundCode);
        return JsonUtil.createOkObject(postListByFundCode);
    }

    /**
     * PostInfo详情
     */
    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "PostInfo详情", notes = "PostInfo详情")
    public Object refresh() {
        List<FundInfo> list = fundInfoService.list();
        list.stream().filter(o -> o.getStatus().equals(1)).forEach(o -> {
            List<PostInfo> postListByFundCode = postInfoService.getPostListByFundCode(o.getFundCode());
            postInfoService.batchSave(postListByFundCode);
        });
        return JsonUtil.createOkObject(1);
    }
}
