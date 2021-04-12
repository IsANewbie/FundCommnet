package com.fund_comment.spider;

import com.alibaba.fastjson.JSONObject;
import com.fund_comment.pojo.FundSearchInfo;
import com.fund_comment.pojo.JsonRootBean;
import com.fund_comment.utils.HttpsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class FundPostSpider {

    private static final String GET_FUND_POST_COUNT = "https://jijinbaapi.eastmoney.com/FundMCApi/FundMBNew/FundBarInfo";

    private static final String GET_FUND_POST_PAGE = "https://jijinbaapi.eastmoney.com/FundMCApi/FundMBNew/FundBarAllList";

    private static final String SEARCH_FUND = "https://appsuggest.1234567.com.cn/FundMSearchApi/FundSearch46Funds";


    public void initCommonParams(Map<String, Object> params) {
        params.put("uid", "8118305875161670");
        params.put("product", "EFund");
        params.put("appVersion", "6.4.0");
        params.put("ServerVersion", "6.4.0");
        params.put("UserId", "e5392d1889734e9d8b7dfaf29419d56d");
        params.put("PassportID", "8118305875161670");
        params.put("plat", "Android");
        params.put("UToken", "j1rrjfc8jceuu-urcucacakfd-188j88x8exuu5b.2");
        params.put("version", "6.4.0");
        params.put("deviceid", "725dbd0e85055ad52109fbff07ebab09%7C%7Ciemi_tluafed_me");
        params.put("CToken", "eur6eqddjcjjk-je6unnekuak8arh-ef.2");
    }

    // todo 分页配置，目前最多查询500条
    public List<JsonRootBean> getByFundCode(String fundCode) {
        Integer postCount = getPostCount(fundCode);
        log.info("基金代码：{}，帖子数共有：{}",fundCode, postCount);
        Map<String, Object> params = new HashMap<>(16);
        initCommonParams(params);
        params.put("fundCode", fundCode);
        params.put("TimePoint", "0");
        params.put("pageSize", postCount);
        params.put("sort", "0");
        JSONObject json = HttpsUtils.doPost(GET_FUND_POST_PAGE, params);
        String data = json.getString("Data");
        List<JsonRootBean> posts = JSONObject.parseArray(data, JsonRootBean.class);

        return posts;
    }

    public Integer getPostCount(String fundCode) {
        Map<String, Object> params = new HashMap<>(16);
        initCommonParams(params);
        params.put("fundCode", fundCode);
        JSONObject count = HttpsUtils.doPost(GET_FUND_POST_COUNT, params);
        String postCount = count.getJSONObject("Data").get("PostCount").toString();
        if (postCount.contains("万")) {
            String[] s = postCount.split("万");
            Double counts = Double.valueOf(s[0]) * 10000;
            return counts.intValue();
        }
        return Integer.parseInt(postCount);
    }

    public List<FundSearchInfo> searchFundInfo(String fundCode){
        Map<String, Object> params = new HashMap<>(16);
        initCommonParams(params);
        params.put("key", fundCode);
        params.put("pageSize", 20);
        params.put("CustomerNo","e5392d1889734e9d8b7dfaf29419d56d");
        params.put("pageIndex", "1");
        params.put("MobileKey", "725dbd0e85055ad52109fbff07ebab09%7C%7Ciemi_tluafed_me");
        params.put("OSVersion", "10");
        params.put("FundbarChannal", "Appstore");
        JSONObject json = HttpsUtils.doPost(SEARCH_FUND, params);
        String data = json.getString("Datas");
        return JSONObject.parseArray(data, FundSearchInfo.class);
    }

    // // TODO: 2021/4/10 查询全网基金信息 天天基金

}
