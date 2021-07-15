package com.fund_comment.fund;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fund_comment.BaseTest;
import com.fund_comment.entity.FundEstimateNavInfo;
import com.fund_comment.pojo.vo.TTJJEstimateNavInfoVo;
import com.fund_comment.service.FundEstimateNavInfoService;
import com.fund_comment.service.FundInfoNewService;
import com.fund_comment.utils.HttpsUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zoubin
 * @description: TODO
 * @date 2021/4/13 22:53
 */
public class FundTest extends BaseTest {
    @Autowired
    FundInfoNewService fundInfoNewService;
    @Autowired
    FundEstimateNavInfoService estimateNavInfoService;

    @Test
    public void testSava() {
//        fundInfoNewService.savaList();
    }

    public void initCommonParams(Map<String, Object> params) {
        params.put("uid", "8118305875161670");
        params.put("product", "EFund");
        params.put("appType", "ttjj");
        params.put("Sort", "desc");
        params.put("gToken", "ceaf-d4053ed3f1cc50c4388cf9dcebf8a5d0");
        params.put("DataConstraintType", "0");
        params.put("FundType", "0");
        params.put("BUY", "true");
        params.put("SortColumn", "RZDF");
        params.put("version", "6.4.0");
        params.put("deviceid", "725dbd0e85055ad52109fbff07ebab09%7C%7Ciemi_tluafed_me");
        params.put("MobileKey", "3493045e08460ca286a02fe6ef9d11dc%7C%7Ciemi_tluafed_me");
        params.put("plat", "Android");
        params.put("ISABNORMAL", "ture");
    }

    @Test
    public void setFundInfoNewService() {

        String url = "https://fundmobapi.eastmoney.com/FundMNewApi/FundMNRank";
        String url_1 = "https://fundmobapi.eastmoney.com/FundMNewApi/FundMNValuationList";
        String paramsStr = "appType=ttjj&Sort=desc&product=EFund&gToken=ceaf-d4053ed3f1cc50c4388cf9dcebf8a5d0&version=6.4.5&ctoken=&ESTABDATE=&deviceid=3493045e08460ca286a02fe6ef9d11dc%7C%7Ciemi_tluafed_me&ENDNAV=&FundType=0&BUY=false&pageIndex=4&RLEVEL_SZ=&RISKLEVEL=&DISCOUNT=&utoken=&CompanyId=&SortColumn=GSZZL&pageSize=30&MobileKey=3493045e08460ca286a02fe6ef9d11dc%7C%7Ciemi_tluafed_me&TOPICAL=&plat=Android&ISABNORMAL=false";
        Map<String, Object> params = new HashMap<>();
        Arrays.asList(paramsStr.split("&")).forEach(o -> {
            String[] split1 = o.split("=");
            params.put(split1[0], split1.length == 1 ? "" : split1[1]);
        });

        params.put("pageIndex", 1);
        params.put("pageSize", 30);
        JSONObject json = HttpsUtils.doPost(url_1, params);
        int totalCount = Integer.parseInt(json.getString("TotalCount") == null ? "0" : json.getString("TotalCount"));
        List<FundEstimateNavInfo> save = new ArrayList<>();
        int pageNo = 1;
        while (pageNo <= totalCount / 30 + 1) {
            params.put("pageIndex", pageNo);
            JSONObject j = HttpsUtils.doPost(url_1, params);
            JSONArray datas = j.getJSONArray("Datas");
            List<TTJJEstimateNavInfoVo> ttjjEstimateNavInfoVos = JSON.parseArray(datas.toJSONString(), TTJJEstimateNavInfoVo.class);
            List<FundEstimateNavInfo> tempList = ttjjEstimateNavInfoVos.stream().map(o -> FundEstimateNavInfo.builder()
                    .id(UUID.randomUUID().toString())
                    .fundCode(o.getFcode())
                    .fundName(o.getShortname())
                    .isBuy(o.getIsbuy().equals("1"))
                    .isLastTradeDay(o.getIslisttrade().equals("1"))
                    .estimateDate(o.getGztime())
                    .estimateNav(BigDecimal.valueOf(o.getGsz()))
                    .estimateNavRise(String.valueOf(o.getGszzl()))
                    .build()
            ).collect(Collectors.toList());
            save.addAll(tempList);
            if (pageNo%30 == 0 || pageNo == totalCount / 30 + 1) {
                System.out.printf("第%s页，共有数据%s条%n\n", pageNo, save.size());
                estimateNavInfoService.saveBatch(save);
                save.clear();
            }
            pageNo++;
        }

    }

    public static void main(String[] args) {
    }
}
