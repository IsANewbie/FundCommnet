package com.fund_comment.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fund_comment.entity.FundInfo;
import com.fund_comment.service.FundInfoService;
import com.fund_comment.utils.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FundInfoController控制器
 *
 * @author zoubin
 * @Date 2021-03-28
 */
@Controller
@RequestMapping("/fundInfo")
@Api(value = "FundInfoController-controller", description = "FundInfoController")
public class FundInfoController {

    @Autowired
    private FundInfoService fundInfoService;


    /**
     * 获取FundInfo列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取FundInfo列表", notes = "获取FundInfo列表")
    public Object list(String condition) {
        QueryWrapper<FundInfo> query = new QueryWrapper<>();
        query.in("status", 0, 1);
        List<FundInfo> list = fundInfoService.list(query);
        return JsonUtil.createPageObject(list);
    }

    /**
     * 新增FundInfo
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增FundInfo", notes = "新增FundInfo")
    public String add(String fundInfoStr, Model model) {
        String[] split = fundInfoStr.split("-");
        String code = split[0];
        String name = split[1];
        FundInfo fundInfo = fundInfoService.getByFundCode(code);
        QueryWrapper<FundInfo> query = new QueryWrapper<>();
        query.in("status", 0, 1);
        List<FundInfo> list = fundInfoService.list(query);
        model.addAttribute("list", list);
        if (fundInfo != null) {
            if (fundInfo.getStatus() >= 0) {
                model.addAttribute("error", name + "（" + code + "）已存在任务列表中");
            } else {
                fundInfo.setStatus(1);
                fundInfoService.updateById(fundInfo);
                model.addAttribute("success", name + "（" + code + "）已添加至任务列表");
            }
            list = fundInfoService.list(query);
            model.addAttribute("list", list);
            return "index";
        }
        FundInfo findInfo = new FundInfo();
        findInfo.setFundCode(code);
        findInfo.setFundName(name);
        fundInfoService.save(findInfo);
        model.addAttribute("success", name + "（" + code + "）已添加至任务列表");
        return "index";
    }


    /**
     * FundInfo详情
     */
    @RequestMapping(value = "/detail/{fundInfoId}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "FundInfo详情", notes = "FundInfo详情")
    public Object detail(@PathVariable("fundInfoId") Integer fundInfoId) {
        FundInfo obj = fundInfoService.getById(fundInfoId);
        return JsonUtil.createOkObject(obj);
    }


    @RequestMapping(value = "/changeStatus", method = RequestMethod.GET)
    @ApiOperation(value = "FundInfo详情", notes = "FundInfo详情")
    public String changeStatus(String fundCode) {
        FundInfo fundInfo = fundInfoService.getByFundCode(fundCode);
        fundInfo.setStatus(1 - fundInfo.getStatus());
        fundInfoService.updateById(fundInfo);
        return "redirect:/index";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    @ApiOperation(value = "FundInfo详情", notes = "FundInfo详情")
    public String remove(String fundCode) {
        FundInfo fundInfo = fundInfoService.getByFundCode(fundCode);
        fundInfo.setStatus(-1);
        fundInfoService.updateById(fundInfo);
        return "redirect:/index";
    }

}
