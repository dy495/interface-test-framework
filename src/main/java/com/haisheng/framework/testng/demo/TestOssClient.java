package com.haisheng.framework.testng.demo;

import com.haisheng.framework.util.OssClientUtil;
import org.testng.annotations.Test;

public class TestOssClient {



    @Test
    private void testOss() {
        OssClientUtil ossClientUtil = new OssClientUtil();

        String url = ossClientUtil.genUrl("QA_TEST/xuyan.jpg");
        System.out.println("url: " + url);

        //目前OSS上测试的文件夹内只存有xuyan和yanghang两张照片
        //QA_TEST--在oss上创建的测试使用的文件夹
        //QA_TEST/xuyan.jpg  徐艳照片路径
        //QA_TEST/yanghang.jpg 杨航照片路径

    }
}
