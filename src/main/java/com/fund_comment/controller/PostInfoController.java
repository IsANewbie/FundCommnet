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
