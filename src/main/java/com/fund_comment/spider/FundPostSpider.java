package com.fund_comment.spider;

import com.alibaba.fastjson.JSONObject;
import com.fund_comment.entity.FundInfoNew;
import com.fund_comment.entity.PostInfoNew;
import com.fund_comment.pojo.FundSearchInfo;
import com.fund_comment.pojo.JsonRootBean;
import com.fund_comment.pojo.PostPageInfo;
import com.fund_comment.pojo.ProxyInfo;
import com.fund_comment.utils.HttpsUtils;
import com.fund_comment.utils.ProxyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
@Slf4j
public class FundPostSpider {

    private static final String GET_FUND_POST_COUNT = "https://jijinbaapi.eastmoney.com/FundMCApi/FundMBNew/FundBarInfo";

    private static final String GET_FUND_POST_PAGE = "https://jijinbaapi.eastmoney.com/FundMCApi/FundMBNew/FundBarAllList";

    private static final String SEARCH_FUND = "https://appsuggest.1234567.com.cn/FundMSearchApi/FundSearch46Funds";


    private Map<String, List<ProxyInfo>> proxyInfosMap;

    public void initCommonParams(Map<String, Object> params) {
        params.put("uid", "8118305875161670");
        params.put("product", "EFund");
//        params.put("appVersion", "6.4.0");
//        params.put("ServerVersion", "6.4.0");
        params.put("UserId", "e5392d1889734e9d8b7dfaf29419d56d");
        params.put("PassportID", "8118305875161670");
        params.put("plat", "Web");
        params.put("UToken", "j1rrjfc8jceuu-urcucacakfd-188j88x8exuu5b.2");
        params.put("version", "6.4.0");
        params.put("deviceid", "725dbd0e85055ad52109fbff07ebab09%7C%7Ciemi_tluafed_me");
        params.put("CToken", "eur6eqddjcjjk-je6unnekuak8arh-ef.2");
    }

    // todo 分页配置，目前最多查询500条
    public List<JsonRootBean> getByFundCodeStartInfo(String fundCode) {
        Map<String, Object> params = new HashMap<>(16);
        initCommonParams(params);
        params.put("fundCode", fundCode);
        params.put("TimePoint", "0");
        params.put("pageSize", 5000);
        params.put("sort", "1");
        JSONObject json = HttpsUtils.doPost(GET_FUND_POST_PAGE, params);
        String data = json.getString("Data");
        List<JsonRootBean> posts = JSONObject.parseArray(data, JsonRootBean.class);

        return posts;
    }

    // todo 分页配置，目前最多查询500条
    public List<JsonRootBean> getByFundCode(String fundCode, String lastTimePoint) {
        Map<String, Object> params = new HashMap<>(16);
        initCommonParams(params);
        params.put("fundCode", fundCode);
        params.put("TimePoint", lastTimePoint);
        params.put("pageSize", 5000);
        params.put("sort", "1");
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
    public List<FundInfoNew> getAllFundInfo(){
        try {
            Connection.Response resp = Jsoup.connect("http://fund.eastmoney.com/allfund.html")
                    .timeout(60000)
                    .method(Connection.Method.GET)
                    .maxBodySize(2048*10240)
                    .followRedirects(false)
                    .execute();
            InputStream in = new ByteArrayInputStream(resp.bodyAsBytes());
            Document document = Jsoup.parse(in,"GBK","http://fund.eastmoney.com/allfund.html");
            Elements select = document.select("#code_content");
            Elements numBox = select.get(0).select(".num_box");
            List<FundInfoNew> fundInfos = new ArrayList<>();
            numBox.stream().parallel().forEach(element -> {
                Element currentNumFundList = element.select("ul.num_right").get(0);
                Elements liList = currentNumFundList.select("li");
                log.info("当前有基金{}支", liList.size());
                for (int i = 1; i <= liList.size() ; i++) {
                    FundInfoNew fund = new FundInfoNew();
                    String fundInfoSelector = String.format("li:nth-child(%s) > div > a:nth-child(1)",i);
                    String fundInfo = currentNumFundList.select(fundInfoSelector).text();
                    if (StringUtils.isBlank(fundInfo) || !fundInfo.contains("）")){
                        continue;
                    }
                    String[] split = fundInfo.split("）");
                    String fundCode = split[0];
                    String fundName = split[1];
                    fund.setFundCode(fundCode.substring(1));
                    fund.setStatus(2);
                    fund.setFundName(fundName);
                    fundInfos.add(fund);
                }
            });
            log.info("共有{}支基金",fundInfos.size());
            return fundInfos;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PostInfoNew> getPostInfoNew(FundInfoNew fund) {
        String fundCode = fund.getFundCode();
        String fundName = fund.getFundName();
        Random random = new Random();
        List<PostInfoNew> postInfoNewList = new ArrayList<>();
        String[] ua = {"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36 OPR/37.0.2178.32",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586",
                "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
                "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
                "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)",
                "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0)",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 BIDUBrowser/8.3 Safari/537.36",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.277.400 QQBrowser/9.4.7658.400",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 UBrowser/5.6.12150.8 Safari/537.36",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36 TheWorld 7",
                "Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/60.0"};
        String baseUrl = "http://guba.eastmoney.com";
        String postListBaseUrl = "http://guba.eastmoney.com/list,of";
        String postListStartUrl = String.format("%s%s,f_1.html", postListBaseUrl, fundCode);
        try {
            log.info("当前爬取地址:{}", postListStartUrl);
            List<ProxyInfo> proxyInfoList = ProxyUtils.getUsefulIp(postListStartUrl, fundName);
            log.info("可用代理数：{}", proxyInfoList.size());
            int randomIndex = random.nextInt(proxyInfoList.size());
            Document document = Jsoup.connect(postListStartUrl)
                    .proxy(proxyInfoList.get(randomIndex).getIp(), proxyInfoList.get(randomIndex).getPort())
                    .userAgent(ua[random.nextInt(ua.length)])
                    .get();
            if (!document.text().contains(fundName+"股吧")){
                return new ArrayList<>();
            }
            PostPageInfo pageInfo = getPageInfo(document);
            log.info("当前基金: {},共有帖子：{}，共有页数：{}", fundName, pageInfo.getTotalCount(), pageInfo.getPageCount());
            for (int i = 1; i <= pageInfo.getPageCount(); i++) {
                Thread.sleep(1000*8);
                String currentPageUrl = String.format("%s%s,f_%s.html", postListBaseUrl, fundCode, i);
//                randomIndex = random.nextInt(proxyInfoList.size());
//                ProxyInfo proxyInfo = proxyInfoList.get(randomIndex);
//                log.info("使用IP:{}", proxyInfo);
                Document currentPage = Jsoup.connect(currentPageUrl)
//                        .timeout(5000)
//                        .proxy(proxyInfo.getIp(), proxyInfo.getPort())
//                        .ignoreContentType(true)
                        .userAgent(ua[random.nextInt(ua.length)])
                        .header("referer",String.format("%s%s,f_%s.html", postListBaseUrl, fundCode, i-1))
                        .get();
                System.out.println("第"+i+"页");
//                System.out.println(document.toString());
                Element articleListNew = currentPage.getElementById("articlelistnew");
                Elements postList = articleListNew.getElementsByClass("normal_post");
                for (int j = 0; j < postList.size(); j++) {
                    Element element = postList.get(j);
                    String clickCount = element.select("span.l1").size() > 0 ? element.select("span.l1").get(0).text():"";
                    String commentCount = element.select("span.l2").size() > 0? element.select("span.l2").get(0).text():"";
                    String postTime = element.select("span.l5").size() > 0? element.select("span.l5").get(0).text():"";
                    String author = element.select("span.l4 font").size() > 0?element.select("span.l4 font").get(0).text():"";
                    String context = element.select("span.l3 a").size()>0 ? element.select("span.l3 a").get(0).text():"";
                    String fundInfoUrl = String.format("%s%s", baseUrl, element.select("span.l3 a").get(0).attr("href"));
                    System.out.println(fundInfoUrl);
                    PostInfoNew postInfoNew = PostInfoNew.builder()
                            .post(context)
                            .publishTime(postTime)
                            .fundName(fundName)
                            .fundCode(fundCode)
                            .clickCount(clickCount)
                            .commentCount(commentCount)
                            .writerName(author)
                            .build();
                    postInfoNewList.add(postInfoNew);
                }
//                postList.forEach(element -> {
//
//                });
            }
            return postInfoNewList;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private PostPageInfo getPageInfo(Document rootDocument) {
        Elements select = rootDocument.select("#articlelistnew > div.pager > span");
        String attr = select.attr("data-pager");
        String[] split = attr.split("\\|");
        int totalCount = Integer.parseInt(split[1]);
        int pageSize = Integer.parseInt(split[2]);
        int pageCount = (totalCount / pageSize) + 1;
        return new PostPageInfo(pageSize, totalCount, pageCount);


    }

}
