package com.haisheng.framework.testng.bigScreen.crm.commonDs;

import com.alibaba.fastjson.JSONPath;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import java.util.List;

public class JsonPathUtil {
    public static void main(String[] args) throws Exception{

          String json="{\"code\":1000,\"data\":{\"list\":[{\"role_name\":\"超级管理员\",\"role_id\":9},{\"role_name\":\"总经理\",\"role_id\":10},{\"role_name\":\"销售总监\",\"role_id\":11},{\"role_name\":\"销售经理\",\"role_id\":12},{\"role_name\":\"销售顾问\",\"role_id\":13},{\"role_name\":\"销售前台\",\"role_id\":14},{\"role_name\":\"定损顾问\",\"role_id\":15},{\"role_name\":\"服务顾问\",\"role_id\":16},{\"role_name\":\"服务总监\",\"role_id\":18},{\"role_name\":\"市场总监\",\"role_id\":20},{\"role_name\":\"DCC销售顾问\",\"role_id\":23}]},\"source\":\"BUSINESS_PORSCHE\",\"message\":\"成功\",\"request_id\":\"d462259c-cbec-486f-9072-1d46813b5499\"}";
        String jsonpath = "$.code==1000&&$.data.list[*].role_name&&$.data.list[*].role_id";
//          String json="{\"list\":[{\"role_name\":\"超级管理员\",\"role_id\":9},{\"role_name\":\"总经理\",\"role_id\":10},{\"role_name\":\"销售总监\",\"role_id\":11},{\"role_name\":\"销售经理\",\"role_id\":12},{\"role_name\":\"销售顾问\",\"role_id\":13},{\"role_name\":\"销售前台\",\"role_id\":14},{\"role_name\":\"定损顾问\",\"role_id\":15},{\"role_name\":\"服务顾问\",\"role_id\":16},{\"role_name\":\"服务总监\",\"role_id\":18},{\"role_name\":\"市场总监\",\"role_id\":20},{\"role_name\":\"DCC销售顾问\",\"role_id\":23}]}";
//          String jsonpath = "$.list[*].role_name0&&$.list[*].role_id";
//          spiltString(json,jsonpath);
        String json1="{\"er_code\" : \"\"}";
        String jsonpath1="$.er_code";
        spiltString(json1,jsonpath1);



    }
    public static void spiltString(String json, String str) throws Exception {
        for (String retval : str.split("&&")) {
            System.out.println("1:"+retval);
            if (retval.contains("==")) {
                checkequal(json,retval);
            } else if(retval.contains("!=")){
                checknotequal(json,retval);
            }
            else if(retval.contains("<=")||retval.contains(">")||retval.contains("<")){
                throw new Exception("暂不支持小于等于判断");
            }else {
                checkNotnull(json,retval);
            }
        }
    }

    public static void checkNotnull(String json, String retval) throws Exception {
        if (retval.contains("*")) {
            ReadContext context = JsonPath.parse(json);
            List<Object> result = context.read(retval);
            for (Object ob : result) {
                System.out.println(ob);
                if (ob == null || result.size() == 0){
                    throw new Exception("except:not null"+ ",actual:"+ob);
                }
            }
        } else {
            Object result = JSONPath.read(json, retval);
            if (result==null||result.equals("")) {
                throw new Exception("except:not null" + ",actual:"+result );
            }
        }
    }

    public static void checkequal(String json, String retval) throws Exception {
        String arr[] = retval.split("==");
        if (arr[0].contains("*")) {
            ReadContext context = JsonPath.parse(json);
            List<Object> result = context.read(arr[0]);
            for (Object ob : result) {
                if (!ob.equals(arr[1])) {
                    throw new Exception("except:" + arr[1] + ",actual:" + result);
                }
            }
        } else {
            Object result = JSONPath.read(json, arr[0]);
            if (!result.toString().equals(arr[1])) {
                throw new Exception("except:" + arr[1] + ",actual:" + result);
            }
        }
    }
    public static void checknotequal(String json, String retval) throws Exception {
        String arr[] = retval.split("!=");
        if (arr[0].contains("*")) {
            ReadContext context = JsonPath.parse(json);
            List<Object> result = context.read(arr[0]);
            for (Object ob : result) {
                if (ob.equals(arr[1])) {
                    throw new Exception("except:" + arr[1] + ",actual:" + result);
                }
            }
        } else {
            Object result = JSONPath.read(json, arr[0]);
            if (result.toString().equals(arr[1])) {
                throw new Exception("except:" + arr[1] + ",actual:" + result);
            }
        }
    }

    public static void checkebig(String json, String retval) throws Exception {
        String arr[] = retval.split(">=");
        if (arr[0].contains("*")) {
            ReadContext context = JsonPath.parse(json);
            List<Object> result = context.read(arr[0]);
            for (Object ob : result) {
                if (Long.parseLong(ob.toString()) >= Long.parseLong(arr[1])) {
                    throw new Exception("except:" + arr[1] + ",actual:" + ob);
                }
            }
        } else {
            Object result = JSONPath.read(json, arr[0]);
            if (Long.parseLong(result.toString()) >= Long.parseLong(arr[1])) {
                throw new Exception("except:" + arr[1] + ",actual:" + result);
            }
        }
    }

}
