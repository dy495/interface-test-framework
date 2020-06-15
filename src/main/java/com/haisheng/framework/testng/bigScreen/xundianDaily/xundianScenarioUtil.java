package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class xundianScenarioUtil extends TestCaseCommon {

    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     *
     * */

    private static volatile xundianScenarioUtil instance = null;

    private xundianScenarioUtil() {}


    public static xundianScenarioUtil getInstance() {

        if (null == instance) {
            synchronized (xundianScenarioUtil.class) {
                if (null == instance) {
                    instance = new xundianScenarioUtil();
                }
            }
        }

        return instance;
    }


    /***
     * 方法区，不同产品的测试场景各不相同，自行更改
     */
    public String IpPort = "";



}
