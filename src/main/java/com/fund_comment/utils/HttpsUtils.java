package com.fund_comment.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.*;


@Slf4j
public class HttpsUtils {
    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 7000;


    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());
        // Validate connections after 1 sec of inactivity
        connMgr.setValidateAfterInactivity(1000);
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);

        requestConfig = configBuilder.build();
    }

    /**
     * 发送 GET 请求（HTTP），不带输入数据
     *
     * @param url
     * @return
     */
    public static JSONObject doGet(String url) {
        return doGet(url, new HashMap<String, Object>());
    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     *
     * @param url
     * @param params
     * @return
     */
    public static JSONObject doGet(String url, Map<String, Object> params) {
        String apiUrl = url;
        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0) {
                param.append("?");
            } else {
                param.append("&");
            }
            param.append(key).append("=").append(params.get(key));
            i++;
        }
        apiUrl += param;
        String result = null;
        HttpClient httpClient = null;
        if (apiUrl.startsWith("https")) {
            httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        } else {
            httpClient = HttpClients.createDefault();
        }
        try {
            HttpGet httpGet = new HttpGet(apiUrl);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                result = IOUtils.toString(instream, "UTF-8");
            }
        } catch (IOException e) {
            log.error("doGet Map error", e, e);
        }
        return JSON.parseObject(result);
    }

    public static String doGet_String_Result(String url, Map<String, Object> params) {
        String apiUrl = url;
        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0) {
                param.append("?");
            } else {
                param.append("&");
            }
            param.append(key).append("=").append(params.get(key));
            i++;
        }
        apiUrl += param;
        String result = null;
        HttpClient httpClient = null;
        if (apiUrl.startsWith("https")) {
            httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        } else {
            httpClient = HttpClients.createDefault();
        }
        try {
            HttpGet httpGet = new HttpGet(apiUrl);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                result = IOUtils.toString(instream, "UTF-8");
            }
        } catch (IOException e) {
            log.error("doGet Map error", e, e);
        }
        return result;
    }

    /**
     * 发送 POST 请求（HTTP），不带输入数据
     *
     * @param apiUrl
     * @return
     */
    public static JSONObject doPost(String apiUrl) {
        return doPost(apiUrl, new HashMap<String, Object>());
    }

    /**
     * 发送 POST 请求，K-V形式
     *
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return
     */
    public static JSONObject doPost(String apiUrl, Map<String, Object> params) {
        CloseableHttpClient httpClient = null;
        if (apiUrl.startsWith("https")) {
            httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        } else {
            httpClient = HttpClients.createDefault();
        }
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;

        try {
            httpPost.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            log.error(" doPost Map error", e, e);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    log.error(" doPost Map error", e, e);
                }
            }
        }
        return JSON.parseObject(httpStr);
    }

    /**
     * 发送 POST 请求，JSON形式
     *
     * @param apiUrl
     * @param json   json对象
     * @return
     */
    public static JSONObject doPost(String apiUrl, String json) {
        CloseableHttpClient httpClient = null;
        if (apiUrl.startsWith("https")) {
            httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        } else {
            httpClient = HttpClients.createDefault();
        }
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;

        try {
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");// 解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            log.error(" doPost error", e, e);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    log.error(" doPost error", e, e);

                }
            }
        }
        return JSON.parseObject(httpStr);
    }

    /**
     * 创建SSL安全连接
     *
     * @return
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, (arg0, arg1) -> true);
        } catch (GeneralSecurityException e) {
            log.error(" createSSLConnSocketFactory error", e, e);
        }
        return sslsf;
    }

    public static void main(String[] args) {
        ss("005968");

    }

    @Data
    @AllArgsConstructor
    static class PostPageInfo {
        int pageSize;
        int totalCount;
        int pageCount;

    }

    private static void ss(String fundCode) {
        Random random = new Random();
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
        String url = "http://guba.eastmoney.com/list,of162411,f_1.html";

        try {
            Document document = Jsoup.connect(postListStartUrl).get();
            PostPageInfo pageInfo = tt(document);
            List<ProxyInfo> proxyInfoList = ip();
            for (int i = 1; i <= pageInfo.pageCount; i++) {
                String currentPageUrl = String.format("%s%s,f_%s.html", postListBaseUrl, fundCode, i);
//                System.out.println("currentPageUrl：" + currentPageUrl);
                int randomIndex = random.nextInt(proxyInfoList.size());
                ProxyInfo proxyInfo = proxyInfoList.get(randomIndex);
                Document currentPage = Jsoup.connect(currentPageUrl)
                        .timeout(5000)
                        .proxy(proxyInfo.ip, proxyInfo.port)
                        .ignoreContentType(true)
                        .userAgent(ua[random.nextInt(ua.length)])
                        .header("referer",String.format("%s%s,f_%s.html", postListBaseUrl, fundCode, i-1))
                        .get();
                Element articleListNew = currentPage.getElementById("articlelistnew");
                Elements postList = articleListNew.getElementsByClass("normal_post");
                postList.forEach(o -> {
                    String clickCount = o.select("span.l1").get(0).text();
                    String commentCount = o.select("span.l2").get(0).text();
                    String author = o.select("span.l4 font").get(0).text();
                    String fundInfoUrl = String.format("%s%s", baseUrl, o.select("span.l3 a").get(0).attr("href"));
//                    System.out.println("fundInfoUrl："+fundInfoUrl);
                    /*try {
                        Document fundInfo = Jsoup.connect(fundInfoUrl).get();
                        String postTime = fundInfo.select("div.zwfbtime").get(0).text();
                        String title = fundInfo.select("div#zwconttbt").get(0).text();
                        String context = fundInfo.select("div.stockcodec").get(0).text();
                        System.out.println(String.format("postTime:%s,title:%S,context:%s", postTime, title, context));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                });
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static List<ProxyInfo> ip() {
        List<ProxyInfo> postPageInfos = new ArrayList<>();
        String result = doGet_String_Result("http://106.55.44.17:9000/get_all", new HashMap<>());
        JSONArray array = JSON.parseArray(result);
        array.forEach(o -> {
            ProxyInfo proxyInfo = new ProxyInfo();
            JSONObject jsonObject = JSON.parseObject(o.toString());
            String proxy = jsonObject.getString("proxy");
            if (proxy.contains(":")) {
                String[] split = proxy.split(":");
                proxyInfo.setIp(split[0]);
                proxyInfo.setPort(Integer.parseInt(split[1]));
                postPageInfos.add(proxyInfo);
            }
            proxyInfo.setIp(proxy);
            proxyInfo.setPort(80);
            postPageInfos.add(proxyInfo);
        });
        return postPageInfos;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class ProxyInfo {
        String ip;
        int port;
    }

    private static PostPageInfo tt(Document rootDocument) {

        System.out.println(rootDocument.toString());
        Elements select = rootDocument.select("#articlelistnew > div.pager > span");
        String attr = select.attr("data-pager");
        System.out.println(attr);
        String[] split = attr.split("\\|");
        int totalCount = Integer.parseInt(split[1]);
        int pageSize = Integer.parseInt(split[2]);
        int pageCount = (totalCount / pageSize) + 1;
        return new PostPageInfo(pageSize, totalCount, pageCount);


    }

    private static JSONObject uu() {
        String apiUrl = "http://fund.eastmoney.com/data/rankhandler.aspx?op=ph&dt=kf&ft=all&rs=&gs=0&sc=6yzf&st=desc&sd=2020-04-10&ed=2021-04-10&qdii=&tabSubtype=,,,,,&pi=1&pn=50&dx=1&v=0.7200184137818171";
        StringBuffer param = new StringBuffer();
        int i = 0;
//        for (String key : params.keySet()) {
//            if (i == 0) {
//                param.append("?");
//            } else {
//                param.append("&");
//            }
//            param.append(key).append("=").append(params.get(key));
//            i++;
//        }
        apiUrl += param;
        String result = null;
        HttpClient httpClient = null;
        if (apiUrl.startsWith("https")) {
            httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        } else {
            httpClient = HttpClients.createDefault();
        }
        try {
            HttpGet httpGet = new HttpGet(apiUrl);
            httpGet.addHeader("Cookie", "qgqp_b_id=cea1b4b641c1ecc26dec8280607a5e59; st_si=78704035733485; st_asi=delete; ASP.NET_SessionId=zr0xwia4cdv4bulfw0mqbtlh; EMFUND1=null; EMFUND2=null; EMFUND3=null; EMFUND4=null; EMFUND5=null; EMFUND6=null; EMFUND7=null; EMFUND8=null; EMFUND0=null; _adsame_fullscreen_18503=1; EMFUND9=04-11 16:05:08@#$%u534E%u5B9D%u6807%u666E%u6CB9%u6C14%u4E0A%u6E38%u80A1%u7968%u4EBA%u6C11%u5E01A@%23%24162411; st_pvi=48448320328906; st_sp=2021-04-10%2005%3A45%3A54; st_inirUrl=https%3A%2F%2Fwww.baidu.com%2Flink; st_sn=38; st_psi=20210411160508179-112200305282-1666906699");
            httpGet.addHeader("Host", "fund.eastmoney.com");
            httpGet.addHeader("Referer", "http://fund.eastmoney.com/data/fundranking.html");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_0_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36 Edg/89.0.774.68");
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                result = IOUtils.toString(instream, "UTF-8");
            }
        } catch (IOException e) {
            log.error("doGet Map error", e, e);
        }
        System.out.println(result);
        String[] split = result.split("= ");
        String replace = split[1].replace(";", "");
        return JSON.parseObject(replace);

    }


}