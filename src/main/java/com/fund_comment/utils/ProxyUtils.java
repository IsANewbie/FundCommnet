package com.fund_comment.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fund_comment.pojo.ProxyInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zoubin
 * @description: TODO
 * @date 2021/4/12 8:38 下午
 */
public class ProxyUtils {

    public static List<ProxyInfo> getUsefulIp(String targetUrl){
        List<ProxyInfo> list = ip();
        System.out.println(list.size());
        List<ProxyInfo> usefulProxy =
        list.stream().parallel().filter(o -> check(o, targetUrl)).collect(Collectors.toList());
        System.out.println(usefulProxy.size());
        return usefulProxy;
    }

    private static List<ProxyInfo> ip() {
        List<ProxyInfo> postPageInfos = new ArrayList<>();
        String result = HttpsUtils.doGet_String_Result("http://106.55.44.17:9000/get_all", new HashMap<>());
        JSONArray array = JSON.parseArray(result);
        array.forEach(o -> {
            ProxyInfo proxyInfo = new ProxyInfo();
            JSONObject jsonObject = JSON.parseObject(o.toString());
            String proxy = jsonObject.getString("proxy");
            if (proxy.contains(":")) {
                String[] split = proxy.split(":");
                proxyInfo.setIp(split[0]);
                proxyInfo.setPort(Integer.parseInt(split[1]));
            }else {
                proxyInfo.setIp(proxy);
                proxyInfo.setPort(80);
            }
            postPageInfos.add(proxyInfo);
        });
        return postPageInfos;
    }

    private static boolean check(ProxyInfo proxyInfo, String targetUrl){
        String ip = proxyInfo.getIp();
        int port = proxyInfo.getPort();
        Random r = new Random();
        boolean result = false;
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
        int i = r.nextInt(14);
        System.out.println(String.format("检测中------ %s:%s",ip,port) );
        try {
            long a = System.currentTimeMillis();
            //爬取的目标网站，url记得换下。。。！！！
            Document doc = Jsoup.connect(targetUrl)
                    .timeout(5000)
                    .proxy(ip, port)
                    .ignoreContentType(true)
                    .userAgent(ua[i])
                    .header("referer",targetUrl)//这个来源记得换..
                    .get();
            System.out.println(ip+":"+port+"访问时间:"+(System.currentTimeMillis() -a) + "   访问结果: "+doc.text());
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
