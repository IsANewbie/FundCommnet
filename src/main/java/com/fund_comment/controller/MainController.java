package com.fund_comment.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fund_comment.entity.FundInfo;
import com.fund_comment.service.FundInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * PostInfoController控制器
 *
 * @author zoubin
 * @Date 2021-03-28
 */
@Controller
@Api(value = "PostInfoController-controller", description = "PostInfoController")
public class MainController {

    @Autowired
    private FundInfoService fundInfoService;

    /**
     * 获取PostInfo列表
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @ApiOperation(value = "获取PostInfo列表", notes = "获取PostInfo列表")
    public String list(Model model) {
        QueryWrapper<FundInfo> query = new QueryWrapper<>();
        query.in("status", 0, 1);
        List<FundInfo> list = fundInfoService.list(query);
        model.addAttribute("list", list);
        return "index";
    }


}
