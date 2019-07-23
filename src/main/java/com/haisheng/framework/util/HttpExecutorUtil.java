package com.haisheng.framework.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Closer;
import com.google.common.net.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HttpExecutorUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpExecutorUtil.class.getName());
    private static String PROXY = "10.32.140.181";
    private static int PORT = 80;
    private String response;
    private int statusCode;


    public String getResponse() {
        return response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String proxyGet(String url)throws IOException {
        Closer closer = Closer.create();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        closer.register(httpClient);
        HttpGet httpGet = new HttpGet(url);
        HttpHost proxy = new HttpHost(PROXY, PORT, "http");
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        httpGet.setConfig(config);
        logger.info("最终url：{}", url);
        HttpResponse response = httpClient.execute(httpGet);
        this.response = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        this.statusCode = response.getStatusLine().getStatusCode();
        closer.close();
        return url;
    }

    public String proxyGet(String url, Map<String, Object> map)throws IOException {
        Closer closer = Closer.create();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        closer.register(httpClient);
        UrlEncodedFormEntity entity = buildFormParams(map);
        url += "?" + EntityUtils.toString(entity);
        HttpGet httpGet = new HttpGet(url);
        HttpHost proxy = new HttpHost(PROXY, PORT, "http");
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        httpGet.setConfig(config);
        logger.info("最终url：{}", url);
        HttpResponse response = httpClient.execute(httpGet);
        this.response = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        this.statusCode = response.getStatusLine().getStatusCode();
        closer.close();
        return url;
    }

    public String proxyPost(String url, Map<String, Object> map) throws IOException{
        Closer closer = Closer.create();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        closer.register(httpClient);
        UrlEncodedFormEntity entity = buildFormParams(map);
        HttpPost httpPost = new HttpPost(url);
        HttpHost proxy = new HttpHost(PROXY, PORT, "http");
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        httpPost.setConfig(config);
        httpPost.setEntity(entity);
        logger.info("最终url：{}", url);
        HttpResponse response = httpClient.execute(httpPost);
        this.response = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        this.statusCode = response.getStatusLine().getStatusCode();
        closer.close();
        return url;
    }

    public String proxyPostJson(String url, Map<String, Object> map) throws IOException{
        Closer closer = Closer.create();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        closer.register(httpClient);
        HttpEntity entity = buildJsonParams(map);
        HttpPost httpPost = new HttpPost(url);
        HttpHost proxy = new HttpHost(PROXY, PORT, "http");
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        httpPost.setConfig(config);
        httpPost.setEntity(entity);
        logger.info("最终url：{}", url);
        HttpResponse response = httpClient.execute(httpPost);
        this.response = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        this.statusCode = response.getStatusLine().getStatusCode();
        closer.close();
        return url;
    }

    public String proxyDelete(String url) throws IOException{
        Closer closer = Closer.create();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        closer.register(httpClient);
        HttpDelete httpDelete = new HttpDelete(url);
        HttpHost proxy = new HttpHost(PROXY, PORT, "http");
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        httpDelete.setConfig(config);
        logger.info("最终url：{}", url);
        HttpResponse response = httpClient.execute(httpDelete);
        this.response = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        this.statusCode = response.getStatusLine().getStatusCode();
        closer.close();
        return url;
    }

    public String doGet(String url)throws IOException {
        Closer closer = Closer.create();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        closer.register(httpClient);
        HttpGet httpGet = new HttpGet(url);
        logger.info("最终url：{}", url);
        HttpResponse response = httpClient.execute(httpGet);
        this.response = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        this.statusCode = response.getStatusLine().getStatusCode();
        closer.close();
        return url;
    }

    public String doGet(String url, Map<String, Object> map)throws IOException {
        Closer closer = Closer.create();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        closer.register(httpClient);
        UrlEncodedFormEntity entity = buildFormParams(map);
        url += "?" + EntityUtils.toString(entity);
        HttpGet httpGet = new HttpGet(url);
        logger.info("最终url：{}", url);
        HttpResponse response = httpClient.execute(httpGet);
        this.response = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        this.statusCode = response.getStatusLine().getStatusCode();
        closer.close();
        return url;
    }

    public String doPostJson(String url, Map<String, Object> queryMap) throws IOException{
        Closer closer = Closer.create();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        closer.register(httpClient);
        HttpEntity entity = buildJsonParams(queryMap);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        for(Header header : httpPost.getAllHeaders()) {
            logger.info("headers:{}", header.toString());
        }
        logger.info("最终url：{}", url);
        HttpResponse response = httpClient.execute(httpPost);
        this.response = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        this.statusCode = response.getStatusLine().getStatusCode();
        logger.info("response：{}", this.response);
        closer.close();
        return url;
    }

    public String doPostJson(String url, String json) throws IOException{
        Closer closer = Closer.create();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        closer.register(httpClient);
        HttpEntity entity = buildJsonString(json);
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
        httpPost.setEntity(entity);
        logger.info("url：{}", url);
        for(Header header : httpPost.getAllHeaders()) {
            logger.info("headers:{}", header.toString());
        }
        HttpResponse response = httpClient.execute(httpPost);
        this.response = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        this.statusCode = response.getStatusLine().getStatusCode();
        logger.info("response：{}", this.response);
        closer.close();
        return url;
    }

    public String doPostJsonWithHeaders(String url, String json, Map<String, Object> headers) throws IOException{
        Closer closer = Closer.create();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        closer.register(httpClient);
        HttpEntity entity = buildJsonString(json);
//        HttpEntity entity = buildJsonString(json, ContentType.APPLICATION_JSON);
        HttpPost httpPost = new HttpPost(url);
        for(Map.Entry<String, Object> entry : headers.entrySet()) {
            httpPost.addHeader(entry.getKey(), (String) entry.getValue());
        }
        httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
        httpPost.setEntity(entity);
        for(Header header : httpPost.getAllHeaders()) {
            logger.info("headers:{}", header.toString());
        }
        logger.info("url：{}", url);
        HttpResponse response = httpClient.execute(httpPost);
        this.response = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        this.statusCode = response.getStatusLine().getStatusCode();
        logger.info("response：{}", this.response);
        closer.close();
        return url;
    }


    public String doPostJsonWithBasicAuth(String url, Map<String, Object> queryMap, Map<String, String> headers) throws IOException{
        Closer closer = Closer.create();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        closer.register(httpClient);
        HttpEntity entity = buildJsonParams(queryMap);
        HttpPost httpPost = new HttpPost(url);
        for(Map.Entry<String, String> entry : headers.entrySet()) {
            httpPost.addHeader(entry.getKey(), entry.getValue());
        }
        httpPost.setEntity(entity);
        for(Header header : httpPost.getAllHeaders()) {
            logger.info("headers:{}", header.toString());
        }
        logger.info("最终url：{}", url);
        HttpResponse response = httpClient.execute(httpPost);
        this.response = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        this.statusCode = response.getStatusLine().getStatusCode();
        closer.close();
        return url;
    }

    public String doPostJsonArrayWithBasicAuth(String url, List<Map<String, Object>> maps, Map<String, String> headers) throws IOException{
        Closer closer = Closer.create();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        closer.register(httpClient);
        HttpEntity entity = buildJsonArray(maps);
        HttpPost httpPost = new HttpPost(url);
        for(Map.Entry<String, String> entry : headers.entrySet()) {
            httpPost.addHeader(entry.getKey(), entry.getValue());
        }
        httpPost.setEntity(entity);
        for(Header header : httpPost.getAllHeaders()) {
            logger.info("headers:{}", header.toString());
        }
        logger.info("最终url：{}", url);
        HttpResponse response = httpClient.execute(httpPost);
        this.response = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        this.statusCode = response.getStatusLine().getStatusCode();
        closer.close();
        return url;
    }

    public String doPostJsonArrayWithBasicAuth(String url, JSONArray jsonArray, Map<String, String> headers) throws IOException{
        Closer closer = Closer.create();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        closer.register(httpClient);
        HttpEntity entity = new StringEntity(JSONArray.toJSONString(jsonArray), Charsets.UTF_8);
        HttpPost httpPost = new HttpPost(url);
        for(Map.Entry<String, String> entry : headers.entrySet()) {
            httpPost.addHeader(entry.getKey(), entry.getValue());
        }
        httpPost.setEntity(entity);
        for(Header header : httpPost.getAllHeaders()) {
            logger.info("headers:{}", header.toString());
        }
        logger.info("最终url：{}", url);
        HttpResponse response = httpClient.execute(httpPost);
        this.response = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        this.statusCode = response.getStatusLine().getStatusCode();
        closer.close();
        return url;
    }

    public String doPostJsonStrWithBasicAuth(String url, String json, Map<String, String> headers) throws IOException{
        json = json.replaceAll("\n\\s*", "");
        Closer closer = Closer.create();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        closer.register(httpClient);
        HttpEntity entity = new StringEntity(JSON.toJSONString(json), Charsets.UTF_8);
        HttpPost httpPost = new HttpPost(url);
        for(Map.Entry<String, String> entry : headers.entrySet()) {
            httpPost.addHeader(entry.getKey(), entry.getValue());
        }
        httpPost.setEntity(entity);
        for(Header header : httpPost.getAllHeaders()) {
            logger.info("headers:{}", header.toString());
        }
        logger.info("最终url：{}", url);
        HttpResponse response = httpClient.execute(httpPost);
        this.response = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        this.statusCode = response.getStatusLine().getStatusCode();
        closer.close();
        return url;
    }

    public String doPost(String url, Map<String, Object> map) throws IOException{
        Closer closer = Closer.create();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        closer.register(httpClient);
        UrlEncodedFormEntity entity = buildFormParams(map);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        logger.info("最终url：{}", url);
        HttpResponse response = httpClient.execute(httpPost);
        this.response = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        this.statusCode = response.getStatusLine().getStatusCode();
        closer.close();
        return url;
    }

    public String doPostWithHeaders(String url, String json, Map<String, String> headers) throws IOException{
        Closer closer = Closer.create();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        closer.register(httpClient);
        HttpEntity entity = buildJsonString(json);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        for(Map.Entry<String, String> entry : headers.entrySet()) {
            httpPost.addHeader(entry.getKey(), entry.getValue());
        }
        for(Header header : httpPost.getAllHeaders()) {
            logger.info("headers:{}", header.toString());
        }
        logger.info("最终url：{}", url);
        HttpResponse response = httpClient.execute(httpPost);
        this.response = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        this.statusCode = response.getStatusLine().getStatusCode();
        logger.info("response：{}", this.response);
        closer.close();
        return url;
    }

    public String doPostWithHeaderAppJson(String url, String json) throws IOException{
        Closer closer = Closer.create();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        closer.register(httpClient);
        HttpEntity entity = buildJsonString(json);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        httpPost.addHeader("Content-Type", "application/json");
        for(Header header : httpPost.getAllHeaders()) {
            logger.info("headers:{}", header.toString());
        }
        logger.info("最终url：{}", url);
        HttpResponse response = httpClient.execute(httpPost);
        this.response = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        this.statusCode = response.getStatusLine().getStatusCode();
        logger.info("response：{}", this.response);
        closer.close();
        return url;
    }

    public String doGetWithBasicAuth(String url, Map<String, String> headers) throws IOException{
        Closer closer = Closer.create();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        closer.register(httpClient);
        HttpGet httpGet = new HttpGet(url);
        for(Map.Entry<String, String> entry : headers.entrySet()) {
            httpGet.addHeader(entry.getKey(), entry.getValue());
        }
        for(Header header : httpGet.getAllHeaders()) {
            logger.info("headers:{}", header.toString());
        }
        logger.info("最终url：{}", url);
        HttpResponse response = httpClient.execute(httpGet);
        this.response = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        this.statusCode = response.getStatusLine().getStatusCode();
        closer.close();
        return url;
    }

    public String doGetWithBasicAuth(String url, Map<String, Object> queryMap, Map<String, String> headers) throws IOException{
        Closer closer = Closer.create();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        closer.register(httpClient);
        UrlEncodedFormEntity entity = buildFormParams(queryMap);
        url += "?" + EntityUtils.toString(entity);
        HttpGet httpGet = new HttpGet(url);
        for(Map.Entry<String, String> entry : headers.entrySet()) {
            httpGet.addHeader(entry.getKey(), entry.getValue());
        }
        for(Header header : httpGet.getAllHeaders()) {
            logger.info("headers:{}", header.toString());
        }
        logger.info("最终url：{}", url);
        HttpResponse response = httpClient.execute(httpGet);
        this.response = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        this.statusCode = response.getStatusLine().getStatusCode();
        closer.close();
        return url;
    }

    public HttpEntity buildJsonString(String json, ContentType contentType) throws IOException{
        JSONObject jsonObject = JSONObject.parseObject(json);
        logger.info("参数：{}", jsonObject);
        return new StringEntity(jsonObject.toJSONString(), contentType);
    }

    public HttpEntity buildJsonString(String json) throws IOException{
        JSONObject jsonObject = JSONObject.parseObject(json);
        logger.info("参数：{}", jsonObject);
        return new StringEntity(jsonObject.toJSONString(), Charsets.UTF_8);
    }

    public HttpEntity buildJsonParams(Map<String, Object> map) throws IOException{
        logger.info("参数：{}", JSON.toJSONString(map));
        return new StringEntity(JSON.toJSONString(map), Charsets.UTF_8);
    }

    public HttpEntity buildJsonArray(List<Map<String, Object>> maps) throws IOException{
        return new StringEntity(JSONArray.toJSONString(maps), Charsets.UTF_8);
    }

    public UrlEncodedFormEntity buildFormParams(Map<String, Object> map) throws IOException{
        List<NameValuePair> argList = Lists.newArrayList();
        for(Map.Entry<String, Object> entry:map.entrySet()) {
            NameValuePair pair = new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue()));
            argList.add(pair);
        }
        logger.info("参数：{}", argList.toString());
        return new UrlEncodedFormEntity(argList, Charsets.UTF_8);
    }


    public boolean compareImageRequest(String url, String picAURL, String picBPath ) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("session_token", "123456");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        // 把文件加到HTTP的post请求中
        File pictureAFile = new File(picAURL);
        builder.addBinaryBody(
                "pictureA",
                new FileInputStream(pictureAFile),
                ContentType.APPLICATION_OCTET_STREAM,
                pictureAFile.getName()
        );
        File pictureBFile = new File(picBPath);
        builder.addBinaryBody(
                "pictureB",
                new FileInputStream(pictureBFile),
                ContentType.APPLICATION_OCTET_STREAM,
                pictureBFile.getName()
        );
        builder.addTextBody("isImageA", "true", ContentType.TEXT_PLAIN);
        builder.addTextBody("isImageB", "false", ContentType.TEXT_PLAIN);

        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        this.response = EntityUtils.toString(responseEntity, "UTF-8");
        logger.info("response: " + this.response);

        if (null != this.response && null != JSON.parseArray(this.response)) {
            return true;
        } else {
            return false;
        }
    }
}
