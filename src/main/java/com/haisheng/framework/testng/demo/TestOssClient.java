package com.haisheng.framework.testng.demo;

import com.haisheng.framework.util.OssClientUtil;
import org.testng.annotations.Test;

public class TestOssClient {



    @Test
    private void testOss() {
        OssClientUtil ossClientUtil = new OssClientUtil();

        String url = ossClientUtil.genUrl("QA_TEST/xuyan.jpg");
        System.out.println("url: " + url);


    }
}
