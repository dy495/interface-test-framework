package com.haisheng.framework.testng.bigScreen.crm.wm.base.command;

import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.haisheng.framework.model.bean.Case;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public abstract class BaseCommand {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    public static Case caseResult = null;
    private static HttpClient client;
    private static Header[] headers;

    public Response execute(Api api, String url) {
        if (api == null || StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("请求信息不能为空");
        }
        initHttp(api);
        HttpConfig config = HttpConfig.custom();
        config.headers(headers).client(client).url(url);
        long start = System.currentTimeMillis();
        String response = buildRequest(config, api);
        long end = System.currentTimeMillis();
        logger.info("{} time used {} ms", url, end - start);
        return JSONObject.parseObject(response).toJavaObject(Response.class);
    }

    public void initHttp(Api api) {
        try {
            client = HCB.custom().pool(50, 10).retry(3).build();
        } catch (HttpProcessException e) {
            String failReason = "初始化http配置异常" + "\n" + e;
            caseResult.setFailReason(failReason);
        }
        HttpHeader httpHeader = HttpHeader.custom()
                .contentType(api.getContentType())
                .authorization(api.getAuthorization());
        for (String key : api.getHeaders().keySet()) {
            httpHeader.other(key, api.getHeaders().get(key));
        }
        headers = httpHeader.build();
        logger.info(Arrays.toString(headers));
    }

    public abstract String buildRequest(HttpConfig config, Api api);
}
