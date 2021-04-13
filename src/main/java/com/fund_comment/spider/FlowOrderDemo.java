import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 流量订单Demo
 * @ClassName FlowOrderDemo
 * @Description
 * @Date 2019/10/25 15:10
 **/
public class FlowOrderDemo {
    public static String orderId = "O21041400433011116655";
    public static String secret = "97aef7fc15234975ab7a817a2a40d8a1";
    public static String proxyHost = "flow.hailiangip.com";
    public static int proxyPort = 14223;

    public static String createPwd(String orderId, String secret) {
        String signTemplate = "orderId={orderId}&secret={secret}&time={time}";
        String passwordTemplate = "orderId={orderId}&sign={sign}&time={time}&pid=-1&cid=-1&uid=&sip=0&nd=1";
        long time = System.currentTimeMillis() / 1000;
        String str1 = signTemplate.replace("{orderId}", orderId)
                .replace("{secret}", secret)
                .replace("{time}", String.valueOf(time));
        String sign = org.apache.commons.codec.digest.DigestUtils.md5Hex(str1).toLowerCase();
        return passwordTemplate.replace("{orderId}", orderId)
                .replace("{sign}", sign)
                .replace("{time}", String.valueOf(time));
    }

    public static void httpProxyWithPassRequest(String destUrl,String proxyHost, int proxyPort, String proxyUserName, String proxyPassword) throws IOException {
        HttpHost host = new HttpHost(proxyHost, proxyPort);
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(new AuthScope(host), new UsernamePasswordCredentials(proxyUserName, proxyPassword));

        CloseableHttpClient client = HttpClients.custom()
                .setDefaultCredentialsProvider(provider).build();

        HttpGet httpGet = new HttpGet(destUrl);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(6000)
                .setSocketTimeout(6000).setProxy(host).build();

        httpGet.setConfig(requestConfig);
        HttpResponse httpResponse = client.execute(httpGet);
        System.out.println("http代理请求状态码: " + httpResponse.getStatusLine());
        System.out.println("http代理请求响应内容: " +EntityUtils.toString(httpResponse.getEntity()));

    }

    public static String destUrl = "http://guba.eastmoney.com/";
    public static void main(String[] args) {
        try {
            httpProxyWithPassRequest(destUrl, proxyHost , proxyPort, "proxy", createPwd(orderId, secret));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
