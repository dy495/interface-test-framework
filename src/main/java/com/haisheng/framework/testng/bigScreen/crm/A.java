package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSON;
import com.haisheng.framework.util.checker.ApiChecker;
import com.haisheng.framework.util.operator.EnumOperator;
import org.testng.annotations.Test;

/**
 * @author wangmin
 * @date 2020/7/23 16:45
 */
public class A {
    @Test
    public void test() {
        String json1 = "{\"errcode\":0,\"errmsg\":\"ok\"}";
        String json2 = "{\"errcode\":0,\"errmsg\":\"yes\"}";
        new ApiChecker.Builder()
                .responseJson(JSON.parseObject(json1))
                .responseJson1(JSON.parseObject(json2))
                .checkPlus("errcode", EnumOperator.EQ, "errcode")
                .checkPlus("errmsg", EnumOperator.EQ, "errmsg")
                .build().checkPlus();
    }
}
