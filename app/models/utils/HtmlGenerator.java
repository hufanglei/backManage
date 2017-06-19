package models.utils;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


/**
 * 静态页面引擎技术  网页静态化 有效解决并发访问  和访问时间问题
 * 通过提前访问一次数据库 生成html静态资源用于外部访问无需再与数据库交换
 * Created by pwj  on 2015/11/24 0024.
 *
 * @author pwj
 */
public class HtmlGenerator{

    /**
     * 根据模版及参数产生静态页面  通过提前访问一次数据库 生成html静态资源用于外部访问无需再与数据库交换
     */
    public static String createHtmlPage(String url, String htmlFileName, String webappname) {
        String htmlstr = "";//要获取的html数据
        //创建HttpClientBuider对象
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //创建HttpClient对象
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        //创建POST请求对象 参数是访问服务器地址
        HttpPost httpPost = new HttpPost(url);//要访问的资源url
        httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");

        //执行请求 获取服务器返还的响应对象
        try {
            //执行get请求
            CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpPost);
            //响应状态

            //检查响应的状态是否正常 检查状态码的值是否等于200
            int code = httpResponse.getStatusLine().getStatusCode();
            if (code == 200) {
                //获取响应消息实体
                HttpEntity entity = httpResponse.getEntity();
                //判断响应实体是否为空
                if (entity != null) {
                    System.out.println();
                    htmlstr = EntityUtils.toString(entity, "UTF-8");
                    //将页面中的相对路径替换成绝对路径，以确保页面资源正常访问
                    htmlstr = formatPage(htmlstr, webappname);
                    //将解析结果写入指定的静态HTML文件中，实现静态HTML生成
                    writeHtml(htmlFileName, htmlstr);
                }
                return htmlstr;
            }
        } catch (Exception e) {
            return "";
        } finally {
            try {
                //关闭流并释放资源
                closeableHttpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return htmlstr;
    }


    //将解析结果写入指定的静态HTML文件中
    private static synchronized void writeHtml(String htmlFileName, String content) throws Exception {
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(htmlFileName), "UTF-8");
        out.write(content);
        if (out != null)
            out.close();
    }

    //将页面中的相对路径替换成绝对路径，以确保页面资源正常访问
    public static String formatPage(String page, String webappname) {
        page = page.replaceAll("\\.\\./\\.\\./\\.\\./", webappname + "/");
        page = page.replaceAll("\\.\\./\\.\\./", webappname + "/");
        page = page.replaceAll("\\.\\./", webappname + "/");
        return page;
    }

    //测试方法
    public static void stacicTest() {
        HtmlGenerator h = new HtmlGenerator();
        //h.createHtmlPage("http://192.168.0.26:8080/JuHuaWang/index/pcindex?=1", "e:/juhuawang.html", "JuHuaWang");
        h.createHtmlPage("http://127.0.0.1:9000/admin/contract/showContract/81", "e://hetong.html", "backManage");
        System.out.println("静态页面已经生成到e:/hetong.html");

    }

}