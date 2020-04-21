
package com.haisheng.framework.testng.bigScreen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.aliyun.openservices.shade.org.apache.commons.codec.binary.Base64;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.CheckUtil;
import com.haisheng.framework.util.QADbUtil;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import sun.misc.BASE64Decoder;


/**
 * @author : lvxueqing
 * @date :  2020/04/08  11:21
 */

public class MenjinSoftwareSystemDaily {


    Menjin menjin = new Menjin();
    //    ----------------------------------------------变量定义--------------------------------------------------------------------
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String failReason = "";

    private String response = "";

    private boolean FAIL = false;

    private Case aCase = new Case();

    CheckUtil checkUtil = new CheckUtil();

    private QADbUtil qaDbUtil = new QADbUtil();

    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;

    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_MENJIN_BE_DAILY_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/menjin-daily-test/buildWithParameters?case_name=";

    private String DEBUG = System.getProperty("DEBUG", "true");


    private void checkResult(String result, String... checkColumnNames) {
        logger.info("result = {}", result);
        JSONObject res = JSONObject.parseObject(result);
        if (!res.getInteger("code").equals(1000)) {
            throw new RuntimeException("result code is " + res.getInteger("code") + " not success code");
        }
        for (String checkColumn : checkColumnNames) {
            Object column = res.getJSONObject("data").get(checkColumn);
            logger.info("{} : {}", checkColumn, column);
            if (column == null) {
                throw new RuntimeException("result does not contains column " + checkColumn);
            }
        }
    }




    private void setBasicParaToDB(Case aCase, String ciCaseName, String caseName, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("于海生");
        aCase.setExpect("code==1000");
        aCase.setResponse(response);

        if (!StringUtils.isEmpty(failReason) || !StringUtils.isEmpty(aCase.getFailReason())) {
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }


    @AfterClass //还没改
    public void clean() {
        //qaDbUtil.closeConnection();
        dingPushFinal();
    }

    @BeforeMethod //还没改
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }

//--------------------------------层级管理  创建后要删掉！！！-------------------------------

    /**
     * 添加层级1 不指定父层级id
     * 添加成功后，搜索结果与添加时保持一致
     * 再删掉
     */
    @Test
    public void addscopeOneNoparent() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：添加层级1 不指定父层级id\n";

        String key = "";

        try {
            //添加层级
            String scopename = "scope" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"1","");
            System.out.println(single);
            int code = single.getInteger("code");
            System.out.println("1");
            Preconditions.checkArgument(code==1000,"添加失败，当前状态码为" + code );
            String scopeID = single.getJSONObject("data").getString("scope");
            System.out.println(scopeID);
            Preconditions.checkArgument(!scopeID.equals(""),"返回的层级id为" + scopeID);
            //使用层级id进行搜索，有结果
            JSONObject single2 = menjin.scopeList("");
            int code2 = single2.getInteger("code");
            System.out.println("2");
            Preconditions.checkArgument(code2==1000,"使用层级ID"+scopeID+"搜索失败，当前状态码为" + code2 );
            String search_scopeID = single2.getJSONObject("data").getJSONArray("list").getJSONObject(0).getString("scope");
            String search_scopeName = single2.getJSONObject("data").getJSONArray("list").getJSONObject(0).getString("scope_name");
            Preconditions.checkArgument(search_scopeID.equals(scopeID) && search_scopeName.equals(scopename),"添加层级时，层级名称=" + scopename+"使用id进行搜索，结果中层级名称=" + search_scopeName);

            //删掉层级
            int code3 = menjin.scopeDelete(scopeID,"1").getInteger("code");
            Preconditions.checkArgument(code3==1000,"删除层级"+scopeID+"失败，当前状态码为" + code3 );
            //再查无结果
            JSONObject single3 = menjin.scopeList("");
            String search_scopeID2 = single3.getJSONObject("data").getJSONArray("list").getJSONObject(0).getString("scope");
            Preconditions.checkArgument(!search_scopeID2.equals(scopeID),"删除后层级" + scopeID+"仍存在");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 添加层级1 指定父层级id
     * 层级1 不管父层级指定什么 默认不生效
     */
    @Test
    public void addscopeOneWithparent() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：添加层级1 指定父层级id\n";

        String key = "";

        try {
            //父层级id
            String scopename = "scope" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"1","");
            String parentid = single.getJSONObject("data").getString("scope");

            //添加层级1 指定父层级
            String scopename2 = "scope2" + System.currentTimeMillis();
            JSONObject single2 = menjin.scopeAdd(scopename2,"1",parentid);
            int code2 = single2.getInteger("code");
            String scopeid = single2.getJSONObject("data").getString("scope");
            Preconditions.checkArgument(code2==1000,"添加失败，当前状态码为" + code2 );

            menjin.scopeList("");
            //再删除两个层级 先删子层级，再删父层级
            menjin.scopeDelete(scopeid,"1");
            menjin.scopeDelete(parentid,"1");




        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    /**
     *添加层级2，不填写父层级id
     */
    @Test
    public void scope2WithoutParent() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：添加层级2时不填写父层级id\n";

        String key = "";

        try {
            String scopename = "scope" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"2","");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1001,"应添加失败，当前状态码为" + code + "提示语为： " + message);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 添加层级2，指定父层级id          列表
     */
    @Test
    public void scope2WithParent() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：添加层级2时指定父层级id，再删除\n";

        String key = "";

        try {

            //父层级id
            String scopename1 = "scope" + System.currentTimeMillis();
            JSONObject single1 = menjin.scopeAdd(scopename1,"1","");
            String parentid = single1.getJSONObject("data").getString("scope");

            //添加层级
            String scopename = "scope" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"2",parentid);
            int code = single.getInteger("code");

            Preconditions.checkArgument(code==1000,"添加失败，当前状态码为" + code );
            String scopeID = single.getJSONObject("data").getString("scope");
            Preconditions.checkArgument(!scopeID.equals(""),"返回的层级id为" + scopeID);

            //使用层级id进行搜索，有结果
            JSONObject single2 = menjin.scopeList(Long.parseLong(parentid));
            System.out.println(single2);
            int code2 = single2.getInteger("code");

            Preconditions.checkArgument(code2==1000,"使用层级ID"+scopeID+"搜索失败，当前状态码为" + code2);
            JSONArray list = single2.getJSONObject("data").getJSONArray("list");
            Preconditions.checkArgument(list.size()>0,"搜索层级"+scopeID+"结果为空");

            //删除层级1，层级2 也被删除
            menjin.scopeDelete(parentid,"1");
            menjin.scopeList(parentid);
            menjin.scopeList("");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 添加层级2，指定父层级也是层级2
     */
    @Test
    public void scope2WithParentS2() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：添加层级2时指定的父层级为层级2\n";

        String key = "";

        try {

            //父层级id
            String scopename1 = "scope" + System.currentTimeMillis();
            JSONObject single1 = menjin.scopeAdd(scopename1,"1","");
            String parentid = single1.getJSONObject("data").getString("scope");

            //添加层级2
            String scopename = "scope" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"2",parentid);
            String scopeID = single.getJSONObject("data").getString("scope");

            //层级2下添加新层级，应失败
            String scopename2 = "scope" + System.currentTimeMillis();
            JSONObject single2 = menjin.scopeAdd(scopename2,"2",scopeID);
            int code2 = single2.getInteger("code");
            Preconditions.checkArgument(code2==1001,"添加失败，当前状态码为" + code2 );

            //删除父层级，子层级还在不在 不在
            menjin.scopeDelete(parentid,"1");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 查看层级，存在的层级id
     */
    @Test
    public void listScopeExist() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：查看层级时，使用存在的层级id\n";

        String key = "";

        try {
            //添加层级
            String scopename = "scope" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"1","");
            int code = single.getInteger("code");
            Preconditions.checkArgument(code==1000,"添加失败，当前状态码为" + code);
            String scopeID = single.getJSONObject("data").getString("scope");
            Preconditions.checkArgument(!scopeID.equals(""),"返回的层级id为" + scopeID);
            //查看层级列表
            JSONObject single2 = menjin.scopeList(Long.parseLong(scopeID));
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1000,"使用层级ID"+scopeID+"搜索失败，当前状态码为" + code2 + "提示语为： " + message2);
            JSONArray list = single2.getJSONObject("data").getJSONArray("list");
            Preconditions.checkArgument(list.size() == 0, "有搜索记录");

            //删除层级
            menjin.scopeDelete(scopeID,"1");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 删除不存在的层级
     */
    @Test
    public void deleteNotExistScope() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：删除不存在的层级\n";

        String key = "";

        try {
            JSONObject single = menjin.scopeDelete("1234567800000000","2");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1001,"期待状态码1001，当前状态码为" + code + "提示语为： " + message);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *删除层级时，该层级下存在设备
     */
    @Test
    public void deletescopeWithDevice() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：删除层级时层级下存在设备\n";

        String key = "";

        try {
            String scopename = "scope" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"2","13693");
            String scopeID = single.getJSONObject("data").getString("scope");
            System.out.println("层级id   " + scopeID);

            //在该层级下创建设备1
            String devicename = "deviceone" + System.currentTimeMillis();
            JSONObject single2 = menjin.deviceAdd(scopeID,devicename);
            String deviceID = single2.getJSONObject("data").getString("device_id");
            System.out.println("设备1 id   " + deviceID);

            //删除层级
            JSONObject single4 = menjin.scopeDelete(scopeID,"2");
            int code = single4.getInteger("code");
            Preconditions.checkArgument(code==1001,"期待状态码1001，实际" + code);




        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *删除层级时，该层级下存在人物
     */
    @Test
    public void deletescopeWithPeople() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：删除层级时层级下存在人物\n";

        String key = "";

        try {
            String scopename = "scope" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"2","13693");
            String scopeID = single.getJSONObject("data").getString("scope");
            System.out.println("层级id   " + scopeID);

            //在该层级下注册人物

            String user_id = "user" + System.currentTimeMillis();

            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scopeID,user_id,image_type,face_image,"","");

            //删除层级
            JSONObject single4 = menjin.scopeDelete(scopeID,"2");
            int code = single4.getInteger("code");
            //String message = single4.getString("message");
            Preconditions.checkArgument(code==1001,"期待状态码1001，实际" + code);

            //删除人物
            menjin.userDelete(scopeID,user_id);
            //删除层级
            JSONObject single5 = menjin.scopeDelete(scopeID,"2");
            int code2 = single5.getInteger("code");
            //String message = single4.getString("message");
            Preconditions.checkArgument(code2==1000,"期待状态码1000，实际" + code);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    //--------------------------------设备管理-------------------------------

    /**
     * 新建购物中心级别设备 13691
     */
    @Test
    public void adddeviceInShopping() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：在购物中心级别下创建设备 再搜索\n";

        String key = "";

        try {
            //存在的层级2
            String scopeID = "13691";


            //在该层级下创建设备
            String devicename = "device" + System.currentTimeMillis();
            JSONObject single2 = menjin.deviceAdd(scopeID,devicename);
            int code2 = single2.getInteger("code");
            Preconditions.checkArgument(code2==1000,"创建设备失败，当前状态码为" + code2);
            String deviceID = single2.getJSONObject("data").getString("device_id");
            System.out.println("设备id   " + deviceID);
            Preconditions.checkArgument(!deviceID.equals(""),"返回的设备id为" + deviceID);

            //填写scope查看设备列表，使用存在的scope进行查询

            boolean has_device = false;
            String search_name = "";
            String search_type_name = "";
            String search_device_status = "";
            String search_status_name = "";
            String search_run_status = "";

            JSONObject single = menjin.deviceList(scopeID);
            System.out.println(single);
            System.out.println("2");
            JSONArray devicelist = single.getJSONObject("data").getJSONArray("device_list");
            for (int i = 0; i < devicelist.size(); i ++){
                JSONObject devicesingle = devicelist.getJSONObject(i);
                if (devicesingle.getString("device_id").equals(deviceID)){
                    has_device = true;
                    search_name = devicesingle.getString("name");
                    search_type_name = devicesingle.getString("type_name");
                    search_device_status = devicesingle.getString("device_status");
                    search_status_name = devicesingle.getString("status_name");
                    search_run_status = devicesingle.getString("run_status");
                    break;
                }
            }
            System.out.println("deviceID" + deviceID + "      ,,,,,scopeID" + scopeID);
            System.out.println("search_name" + search_name);
            System.out.println("search_type_name" + search_type_name);
            System.out.println("search_device_status" + search_device_status);
            System.out.println("search_status_name" + search_status_name);
            System.out.println("search_run_status" + search_run_status);
            Preconditions.checkArgument(has_device==true,"无搜索结果");
            Preconditions.checkArgument(search_name.equals(devicename),"创建时名称为" + devicename+" , 搜索时名称为 " + search_name +"search_type_name" + search_type_name + " , " + "search_device_status" + search_device_status + " , search_status_name" + search_status_name + " , search_run_status" + search_run_status);



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 在非购物中心级别创建设备 --失败
     */
    @Test
    public void adddeviceInScopeone() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：设备所属非购物中心级别\n";

        String key = "";

        try {
            //先创建一个非购物中心级别的层级
            String scopename = "scope" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"1","");
            String scopeID = single.getJSONObject("data").getString("scope");
            System.out.println("层级id   " + scopeID);


            //在该层级下创建设备
            String devicename = "device" + System.currentTimeMillis();
            JSONObject single2 = menjin.deviceAdd(scopeID,devicename);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1001,"创建设备失败，当前状态码为" + code2 + "提示语为： " + message2);




        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 不存在的层级下创建设备
     */
    @Test
    public void adddeviceInNotExist() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：在不存在的层级下创建设备\n";

        String key = "";

        try {
            //不存在的层级
            String scopeID = "" + System.currentTimeMillis();


            //在该层级下创建设备
            String devicename = "device" + System.currentTimeMillis();
            JSONObject single2 = menjin.deviceAdd(scopeID,devicename);
            int code2 = single2.getInteger("code");
            Preconditions.checkArgument(code2==1001,"期待状态码1001，实际" + code2);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *查看全部创建过的设备 不支持查看全部，暂时只能查看指定的
     */
    //@Test
    public void alldevice() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：设备列表不填写scope\n";

        String key = "";

        try {
            JSONObject single = menjin.deviceList("");
            System.out.println("single " + single);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1000,"查询失败，当前状态码为" + code + "提示语为： " + message);

            JSONArray devicelist = single.getJSONObject("data").getJSONArray("device_list");
            int alldevice = devicelist.size();
            System.out.println("共"+ alldevice + "个设备" );


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *使用不存在的层级id进行查询  --失败
     */
    @Test
    public void scopeIDnotexist() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：使用不存在的层级id查询设备列表\n";

        String key = "";

        try {
            JSONObject single = menjin.deviceList("1234567890000000");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1001,"查询应失败，期待状态码1001，当前为" + code + "提示语为： " + message);


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 改变设备状态 13691
     */
    @Test
    public void changedevicestatus() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：启用/停止设备，查询时状态正确\n";

        String key = "";

        try {
            //创建层级2
            String scopeID = "13691";

            //层级下创建设备
            String devicename = "device" + System.currentTimeMillis();
            JSONObject single2 = menjin.deviceAdd(scopeID,devicename);
            int code2 = single2.getInteger("code");
            Preconditions.checkArgument(code2==1000,"创建设备失败，当前状态码为" + code2);
            String deviceID = single2.getJSONObject("data").getString("device_id");
            System.out.println("设备id   " + deviceID);
            Preconditions.checkArgument(!deviceID.equals(""),"返回的设备id为" + deviceID);

            //创建设备后查看状态
            //填写scope查看设备列表，使用存在的scope进行查询


            String search_run_status = "";

            JSONArray devicelist = menjin.deviceList(scopeID).getJSONObject("data").getJSONArray("device_list");
            for (int i = 0; i < devicelist.size(); i ++){
                JSONObject devicesingle = devicelist.getJSONObject(i);
                if (devicesingle.getString("device_id").equals(deviceID)){

                    search_run_status = devicesingle.getString("run_status");
                    System.out.println("刚创建的设备run_status为" + search_run_status);
                    break;
                }
            }

            //创建后->设备启用
            JSONObject single3 = menjin.operateDevice(deviceID,"ENABLE");
            int code3 = single3.getInteger("code");
            Preconditions.checkArgument(code3==1000,"设备启用失败，当前状态码为" + code3);

            JSONArray devicelist2 = menjin.deviceList(scopeID).getJSONObject("data").getJSONArray("device_list");
            for (int i = 0; i < devicelist2.size(); i ++){
                JSONObject devicesingle2 = devicelist2.getJSONObject(i);
                if (devicesingle2.getString("device_id").equals(deviceID)){
                    search_run_status = devicesingle2.getString("run_status");
                    System.out.println("刚创建的设备，启用后run_status为" + search_run_status);
                    Preconditions.checkArgument(search_run_status.equals("ENABLE"),"设备启用后，状态为" + search_run_status);
                    break;
                }
            }

            //设备启用 -> 设备启用
            JSONObject single5 = menjin.operateDevice(deviceID,"ENABLE");
            int code5 = single5.getInteger("code");
            Preconditions.checkArgument(code5==1000,"设备启用后再次启用失败，当前状态码为" + code5);

            JSONArray devicelist4 = menjin.deviceList(scopeID).getJSONObject("data").getJSONArray("device_list");
            for (int i = 0; i < devicelist4.size(); i ++){
                JSONObject devicesingle4 = devicelist4.getJSONObject(i);
                if (devicesingle4.getString("device_id").equals(deviceID)){
                    search_run_status = devicesingle4.getString("run_status");
                    System.out.println("启用的设备，再次启用后run_status为" + search_run_status);
                    Preconditions.checkArgument(search_run_status.equals("ENABLE"),"已启用的设备再次启用后，状态为" + search_run_status);
                    break;
                }
            }

            //设备启用 -> 设备禁用
            JSONObject single4 = menjin.operateDevice(deviceID,"DISABLE");
            int code4 = single4.getInteger("code");
            Preconditions.checkArgument(code4==1000,"设备停止失败，当前状态码为" + code4);

            JSONArray devicelist3 = menjin.deviceList(scopeID).getJSONObject("data").getJSONArray("device_list");
            for (int i = 0; i < devicelist3.size(); i ++){
                JSONObject devicesingle3 = devicelist3.getJSONObject(i);
                if (devicesingle3.getString("device_id").equals(deviceID)){
                    search_run_status = devicesingle3.getString("run_status");
                    System.out.println("启用的设备，停止后run_status为" + search_run_status);
                    Preconditions.checkArgument(search_run_status.equals("DISABLE"),"设备停止后，状态为" + search_run_status);
                    break;
                }
            }

            //设备禁用 -> 设备禁用
            JSONObject single6 = menjin.operateDevice(deviceID,"DISABLE");
            int code6 = single6.getInteger("code");
            Preconditions.checkArgument(code6==1000,"设备停止后再次停止失败，当前状态码为" + code6);

            JSONArray devicelist5 = menjin.deviceList(scopeID).getJSONObject("data").getJSONArray("device_list");
            for (int i = 0; i < devicelist5.size(); i ++){
                JSONObject devicesingle5 = devicelist5.getJSONObject(i);
                if (devicesingle5.getString("device_id").equals(deviceID)){
                    search_run_status = devicesingle5.getString("run_status");
                    System.out.println("已停止的设备，再次停止后run_status为" + search_run_status);
                    Preconditions.checkArgument(search_run_status.equals("DISABLE"),"已停止的设备再次停止后，状态为" + search_run_status);
                    break;
                }
            }

            //设备禁用 -> 设备启用
            JSONObject single7 = menjin.operateDevice(deviceID,"ENABLE");
            int code7 = single7.getInteger("code");

            Preconditions.checkArgument(code7==1000,"设备停止后启用失败，当前状态码为" + code7);

            JSONArray devicelist6 = menjin.deviceList(scopeID).getJSONObject("data").getJSONArray("device_list");
            for (int i = 0; i < devicelist6.size(); i ++){
                JSONObject devicesingle6 = devicelist6.getJSONObject(i);
                if (devicesingle6.getString("device_id").equals(deviceID)){
                    search_run_status = devicesingle6.getString("run_status");
                    System.out.println("停止的设备，启用后run_status为" + search_run_status);
                    Preconditions.checkArgument(search_run_status.equals("ENABLE"),"停止的设备启用后，状态为" + search_run_status);
                    break;
                }
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


//--------------------------------人物管理-------------------------------

    /**
     *仅查询存在的用户，填写全部必填项 user1587022238426
     */
    @Test
    public void userinfo() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：不注册，仅查询用户\n";

        String key = "";

        try {
            String scope = menjin.scopeUser;
            String user_id = "user1587022238426";
            //人物查询
            JSONObject single2 = menjin.userInfo(scope,user_id);
            System.out.println(single2);
            int code2 = single2.getInteger("code");
            Preconditions.checkArgument(code2==1000,"查询用户" + "user1586504273667" + "失败，状态码" + code2 );

            JSONObject data = single2.getJSONObject("data");
            Preconditions.checkArgument(!data.equals(""),"搜索无结果");
            String qr_image_url = data.getString("qr_image_url"); //搜索二维码
            String face_url = data.getJSONArray("face_list").getJSONObject(0).getString("face_url");//搜索人脸
            Preconditions.checkArgument(!qr_image_url.equals(""),"搜索时二维码为空");
            Preconditions.checkArgument(!face_url.equals(""),"搜索时人脸URL为空");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *仅查询不存在的用户，填写全部必填项 code = 1001 message = [user_id]不存在,请新建
     */
    @Test
    public void userinfoNotExist() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：不注册，仅查询不存在的用户\n";

        String key = "";

        try {
            String scope = menjin.scopeUser;
            String user_id = "12345678";
            //人物查询
            JSONObject single2 = menjin.userInfo(scope,user_id);
            System.out.println(single2);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1001,"查询用户12345678失败，状态码期待1001，实际" + code2 );
            Preconditions.checkArgument(message2.equals("[user_id]不存在,请新建"),"提示语原为「[user_id]不存在,请新建」，现在为"+ message2);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    /**
     *注册用户，填写全部必填项，不存在的user_id, 一张人脸base64 4116
     */
    @Test
    public void useraddwithBase64() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册用户填写全部必填项，不存在的user_id, 人物图片为一张人脸base64\n";

        String key = "";

        try {
            //人物注册

            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();

            String image_type = "BASE64";
            //String image_type = "URL";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            //String face_image = menjin.lxq;
            JSONObject single = menjin.userAdd(scope,user_id,image_type,face_image,"","");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1000,"创建用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);
            System.out.println("人物注册"+single);


            //人物查询
            JSONObject single2 = menjin.userInfo(scope,user_id);
            //JSONObject single2 = menjin.userInfo("4116","user1586499210988");
            System.out.println(single2);
            int code2 = single2.getInteger("code");
            Preconditions.checkArgument(code2==1000,"查询用户" + "user1586499210988" + "失败，状态码" + code2 );
            JSONObject data = single2.getJSONObject("data");
            Preconditions.checkArgument(!data.equals(""),"搜索无结果");

            String qr_image_url = data.getString("qr_image_url"); //搜索二维码
            String face_url = data.getJSONArray("face_list").getJSONObject(0).getString("face_url");//搜索人脸
            Preconditions.checkArgument(!qr_image_url.equals(""),"搜索时二维码为空");
            Preconditions.checkArgument(!face_url.equals(""),"搜索时二维码为空");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *注册用户，填写全部必填项，不存在的user_id, 一张人脸 url
     */
    @Test
    public void useraddwithURL() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册用户填写全部必填项，不存在的user_id, 人物图片为一张人脸url\n";

        String key = "";

        try {
            //人物注册

            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();

            String image_type = "URL";
            String face_image = menjin.lxq;
            JSONObject single = menjin.userAdd(scope,user_id,image_type,face_image,"","");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1000,"创建用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);
            System.out.println("人物注册"+single);


            //人物查询
            JSONObject single2 = menjin.userInfo(scope,user_id);
            System.out.println(single2);


            int code2 = single2.getInteger("code");

            Preconditions.checkArgument(code2==1000,"查询用户" + "user1586499210988" + "失败，状态码" + code2 );

            JSONObject data = single2.getJSONObject("data");

            Preconditions.checkArgument(!data.equals(""),"搜索无结果");

            String qr_image_url = data.getString("qr_image_url"); //搜索二维码
            String face_url = data.getJSONArray("face_list").getJSONObject(0).getString("face_url");//搜索人脸
            Preconditions.checkArgument(!qr_image_url.equals(""),"搜索时二维码为空");
            Preconditions.checkArgument(!face_url.equals(""),"搜索时二维码为空");


            System.out.println("ok");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *注册用户，不存在的user_id，存在的card_key  状态码1001 , 提示语为[card_key]已绑定其它用户,请勿重复绑定
     */
    @Test
    public void useraddSameCard() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册用户 不存在的user_key 已存在的card_id\n";

        String key = "";

        try {
            //人物注册

            String scope = menjin.scopeUser;
            String user_id = "user1" + System.currentTimeMillis();
            String user_id2 = "user2" + System.currentTimeMillis();

            String image_type = "BASE64";
            //String image_type = "URL";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            //String face_image = menjin.lxq;
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");

            JSONObject single = menjin.userAdd(scope,user_id2,image_type,face_image,user_id,"");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1001,"创建用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);
            Preconditions.checkArgument(message.equals("[card_key]已绑定其它用户,请勿重复绑定"),"提示语原为「[card_key]已绑定其它用户,请勿重复绑定」，现在为： " + message);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *注册用户，填写非必填项，不存在的user_id，一张人脸base64
     * cardKey=user_id; username = user_id
     */
    @Test
    public void useraddUwithnotNecessary() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册用户填写非必填项，不存在的user_id，人物图片为一张人脸base64\n";

        String key = "";

        try {
            //注册用户
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            JSONObject single = menjin.userAdd(scope,user_id,image_type,face_image,user_id,user_id);
            //System.out.println("addquan" + single);
            int code = single.getInteger("code");
            Preconditions.checkArgument(code==1000,"创建用户" + user_id + "失败，状态码" + code );

            //人物查询
            JSONObject single2 = menjin.userInfo(scope,user_id);
            int code2 = single2.getInteger("code");

            //System.out.println("chaxun    "+single2);
            Preconditions.checkArgument(code2==1000,"查询用户" + user_id + "失败，状态码" + code2);
            JSONObject data = single2.getJSONObject("data");
            String search_user_id = data.getString("user_id");
            String search_qr_iamge_url = data.getString("qr_image_url");
            String search_card_key = data.getString("card_key");
            String search_face_list = data.getJSONArray("face_list").getJSONObject(0).getString("face_url");

            Preconditions.checkArgument(search_user_id.equals(user_id),"注册时user_id=" + user_id + " ， 根据id查询时展示user_id = " + search_user_id);
            Preconditions.checkArgument(!search_qr_iamge_url.equals(""),"搜索时二维码为空");
            Preconditions.checkArgument(search_card_key.equals(user_id),"cardkey注册时为"+user_id+"，搜索时展示为" + search_card_key);
            Preconditions.checkArgument(!search_face_list.equals(""), "无人脸信息");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *使用已存在的userid注册，一个有cardkey，一个没有，一张人脸base64 4116 1001 [user_id]已存在,请勿重复添加
     */
    @Test
    public void useraddSameId() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：同一层级使用已存在的user_id注册\n";

        String key = "";

        try {
            //第一个人物注册
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            JSONObject single = menjin.userAdd(scope,user_id,image_type,face_image,"","");
            int code = single.getInteger("code");

            Preconditions.checkArgument(code==1000,"创建用户" + user_id + "失败，状态码" + code);

            //第二个人物注册

            String face_image2 = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/廖祥茹.jpg");
            JSONObject single2 = menjin.userAdd(scope,user_id,image_type,face_image2,user_id,"祥茹");
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1001,"使用" + user_id + "创建用户失败，状态码" + code2 + " , 提示语为" + message2);


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *注册用户，人脸分辨率较低base64 4116 1001 人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG 5.单人脸),请更换图片
     */
    @Test
    public void useraddLowQuality() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册用户时人脸base64分辨率较低\n";

        String key = "";

        try {
            //人物注册
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/分辨率较低.png");
            JSONObject single = menjin.userAdd(scope,user_id,image_type,face_image,"","");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1001,"创建用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);
            Preconditions.checkArgument(message.equals("人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG 5.单人脸),请更换图片"),"提示："+ message);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *注册用户，非人脸（猫脸）base64 4116 1001 人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG 5.单人脸),请更换图片
     */
    @Test
    public void useraddCat() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册用户时非人脸base64（猫脸）\n";

        String key = "";

        try {
            //人物注册
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/猫.png");
            JSONObject single = menjin.userAdd(scope,user_id,image_type,face_image,"","");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1001,"创建用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);
            Preconditions.checkArgument(message.equals("人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG 5.单人脸),请更换图片"),"提示："+ message);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *注册用户，风景图 base64 4116 1001 人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG 5.单人脸),请更换图片
     */
    @Test
    public void useraddView() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册用户时使用风景图base64\n";

        String key = "";

        try {
            //人物注册
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/风景.png");
            JSONObject single = menjin.userAdd(scope,user_id,image_type,face_image,"","");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1001,"创建用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);
            Preconditions.checkArgument(message.equals("人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG 5.单人脸),请更换图片"),"提示："+ message);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *注册用户，单人遮挡 base64 4116
     */
    @Test
    public void useraddPersonWithMask() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册用户时使用单人遮挡图片base64\n";

        String key = "";

        try {
            //人物注册
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/单人遮挡.png");
            JSONObject single = menjin.userAdd(scope,user_id,image_type,face_image,"","");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1000,"创建用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *注册用户，多人脸不遮挡 base64 4116 --应失败
     */
    @Test
    public void useraddPeople() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册用户时使用多人脸图片base64\n";

        String key = "";

        try {
            //人物注册
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/多人脸.JPG");
            JSONObject single = menjin.userAdd(scope,user_id,image_type,face_image,"","");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1001,"创建用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *更新已存在用户 cardkey+图片
     */
    @Test
    public void userUpdateCardImage() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：更新用户cardKey/照片\n";

        String key = "";

        try {
            //人物注册

            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();

            String image_type = "BASE64";
            //String image_type = "URL";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            //String face_image = menjin.lxq;
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //查询用户
            JSONObject single0 = menjin.userInfo(scope,user_id);

            String face0 = single0.getJSONObject("data").getJSONArray("face_list").getJSONObject(0).getString("face_url");

            //更新用户
            String new_card = "1234567";
            String newface = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/李婷婷.jpg");
            JSONObject single3 = menjin.userUpdate(scope,user_id,image_type,newface,new_card,"");
            int code = single3.getInteger("code");

            System.out.println(single3);
            Preconditions.checkArgument(code==1000, "状态码不正确" +code);


            //人物查询
            JSONObject single2 = menjin.userInfo(scope,user_id);
            //JSONObject single2 = menjin.userInfo("4116","user1586499210988");
            System.out.println(single2);
            int code2 = single2.getInteger("code");

            Preconditions.checkArgument(code2==1000,"查询失败，状态码" + code2 );
            String search_card_key = single2.getJSONObject("data").getString("card_key");
            String face2 = single2.getJSONObject("data").getJSONArray("face_list").getJSONObject(0).getString("face_url");

            Preconditions.checkArgument(search_card_key.equals(new_card),"CardKey更新失败");
            Preconditions.checkArgument(!face2.equals(face0),"图片未更新");




        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *更新已存在用户 cardkey + username
     * //传image，没有图片
     */
    @Test
    public void userUpdateCardKey() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：更新用户cardKey+username\n";

        String key = "";

        try {
            //人物注册

            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();

            String image_type = "BASE64";
            //String image_type = "URL";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            //String face_image = menjin.lxq;
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //查询用户
            JSONObject single0 = menjin.userInfo(scope,user_id);

            String face0 = single0.getJSONObject("data").getJSONArray("face_list").getJSONObject(0).getString("face_url");

            //更新用户
            String new_card = "1234567";
            //String newface = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/李婷婷.jpg");
            JSONObject single3 = menjin.userUpdate(scope,user_id,image_type,"",new_card,"lvxueqing");
            int code = single3.getInteger("code");

            System.out.println(single3);
            Preconditions.checkArgument(code==1000, "状态码不正确" +code);


            //人物查询
            JSONObject single2 = menjin.userInfo(scope,user_id);
            //JSONObject single2 = menjin.userInfo("4116","user1586499210988");
            System.out.println(single2);
            int code2 = single2.getInteger("code");

            Preconditions.checkArgument(code2==1000,"查询失败，状态码" + code2 );
            String search_card_key = single2.getJSONObject("data").getString("card_key");
            String search_user_name = single2.getJSONObject("data").getString("user_name");

            //String face2 = single2.getJSONObject("data").getJSONArray("face_list").getJSONObject(0).getString("face_url");

            Preconditions.checkArgument(search_card_key.equals(new_card),"CardKey更新失败");
            Preconditions.checkArgument(search_user_name.equals("lvxueqing"),"user_name更新失败");

            //Preconditions.checkArgument(face2.equals(face0),"图片不应更新");




        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *更新已存在用户 图片
     */
    @Test
    public void userUpdateImage() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：更新用户照片\n";

        String key = "";

        try {
            //人物注册

            String scope = "4116";
            String user_id = "user" + System.currentTimeMillis();

            String image_type = "BASE64";
            //String image_type = "URL";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            //String face_image = menjin.lxq;
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //查询用户
            JSONObject single0 = menjin.userInfo(scope,user_id);

            String face0 = single0.getJSONObject("data").getJSONArray("face_list").getJSONObject(0).getString("face_url");

            //更新用户
            //String new_card = "1234567";
            String newface = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/李婷婷.jpg");
            JSONObject single3 = menjin.userUpdate(scope,user_id,image_type,newface,"","");
            int code = single3.getInteger("code");

            System.out.println(single3);
            Preconditions.checkArgument(code==1000, "状态码不正确" +code);


            //人物查询
            JSONObject single2 = menjin.userInfo(scope,user_id);
            //JSONObject single2 = menjin.userInfo("4116","user1586499210988");
            System.out.println(single2);
            int code2 = single2.getInteger("code");

            Preconditions.checkArgument(code2==1000,"查询失败，状态码" + code2 );
            String search_card_key = single2.getJSONObject("data").getString("card_key");
            String face2 = single2.getJSONObject("data").getJSONArray("face_list").getJSONObject(0).getString("face_url");

            //Preconditions.checkArgument(search_card_key.equals(new_card),"CardKey更新失败");
            Preconditions.checkArgument(!face2.equals(face0),"图片未更新");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *更新已存在用户 cardkey+图片都没有
     */
    @Test
    public void userUpdateNull() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：更新用户无照片和card参数\n";

        String key = "";

        try {
            //人物注册

            String scope = "4116";
            String user_id = "user" + System.currentTimeMillis();

            String image_type = "BASE64";
            //String image_type = "URL";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            //String face_image = menjin.lxq;
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //查询用户
            JSONObject single0 = menjin.userInfo(scope,user_id);

            String face0 = single0.getJSONObject("data").getJSONArray("face_list").getJSONObject(0).getString("face_url");

            //更新用户
            //String new_card = "1234567";
            //String newface = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/李婷婷.jpg");
            JSONObject single3 = menjin.userUpdate(scope,user_id,image_type,"","","");
            menjin.userInfo(scope,user_id);
            int code = single3.getInteger("code");

            System.out.println(single3);
            Preconditions.checkArgument(code==1001, "期待状态码1001，实际" +code);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *更新已存在用户 cardkey+层级
     */
    @Test
    public void userUpdatescope() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：更新用户层级和card参数\n";

        String key = "";

        try {
            //人物注册

            String scope = "4116";
            String user_id = "user" + System.currentTimeMillis();

            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //更新用户

            JSONObject single3 = menjin.userUpdate(menjin.scopeUser,user_id,image_type,"","123","");
            menjin.userInfo(scope,user_id);
            int code = single3.getInteger("code");

            System.out.println(single3);
            Preconditions.checkArgument(code==1001, "期待1001，实际" +code);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *更新不存在用户
     */
    @Test
    public void userUpdateNotExist() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：更新不存在用户\n";

        String key = "";

        try {

            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "URL";
            String face_image = menjin.lxq;
            //更新用户
            JSONObject single3 = menjin.userUpdate(scope,user_id,image_type,face_image,"","");
            int code = single3.getInteger("code");
            String message = single3.getString("message");
            System.out.println(single3);
            Preconditions.checkArgument(code==1001, "状态码期待1001，实际" +code+ " ， 提示语为" + message);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    /**
     *删除存在的用户id，再查询
     */
    @Test
    public void userdeleteExist() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：删除已存在的用户id\n";

        String key = "";

        try {
            //人物注册
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/李婷婷.jpg");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //删除
            JSONObject single = menjin.userDelete(scope,user_id);
            //JSONObject single = menjin.userDelete(scope,"user1586510478477");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1000,"删除已存在用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);
            //再查询 应无结果
            JSONObject single2 = menjin.userInfo(scope,user_id);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1001,"删除后再查询用户" + user_id + "失败，状态码" + code2 + " , 提示语为" + message2);

            System.out.println(single2);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *删除不存在的用户id 状态码1001 , 提示语为[user_id]不存在,请新建
     */
    @Test
    public void userdeleteNotExist() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：删除不存在的用户id\n";

        String key = "";

        try {
            String scope = "4116";
            String user_id = "lxq12345678";
            //查询不存在的id
            JSONObject single = menjin.userInfo(scope,user_id);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1001,"查询不存在用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);


            //删除
            JSONObject single2 = menjin.userDelete(scope,user_id);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1001,"删除不存在用户" + user_id + "失败，状态码" + code2 + " , 提示语为" + message2);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *人物二维码获取
     */
    @Test
    public void qrcode() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：二维码获取\n";

        String key = "";

        try {
            //新建用户
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,user_id);


            //获取二维码
            JSONObject single = menjin.userQRCode(scope,user_id);

            String qr = single.getString("qr_image_url");

            Preconditions.checkArgument(!qr.equals(""),"二维码为空");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *人物二维码获取 不填写层级id
     */
    //@Test
    public void qrcodeWithNull() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：二维码获取，必填字段为空\n";

        String key = "";

        try {
            //新建用户
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,user_id);


            //获取二维码
            JSONObject single = menjin.userQRCode(scope,"");

            String qr = single.getString("qr_image_url");

            Preconditions.checkArgument(!qr.equals(""),"二维码为空");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    /**
     *存在的门禁卡信息删除
     */
    @Test
    public void cardDeleteExist() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：删除存在的门禁卡信息\n";

        String key = "";

        try {
            //人物注册
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");

            //搜索人物
            menjin.userInfo(scope,user_id);

            //删除门禁卡信息
            JSONObject single2 = menjin.userInfoDelete(scope,user_id,"CARD_KEY");
            int code2 = single2.getInteger("code");

            Preconditions.checkArgument(code2==1000,"删除失败，状态码" + code2);

            //使用人物id进行搜索
            Boolean isexist = menjin.userInfo(scope,user_id).getJSONObject("data").containsKey("card_key");
            Preconditions.checkArgument(isexist==false,"删除门禁卡信息后，存在card_key");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *不存在的门禁卡信息删除
     */
    @Test
    public void cardDeleteNotExist() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：删除不存在的门禁卡信息（人物注册时无门禁卡信息）\n";

        String key = "";

        try {
            //人物注册
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //删除门禁卡信息
            JSONObject single2 = menjin.userInfoDelete(scope,user_id,"CARD_KEY");
            int code2 = single2.getInteger("code");

            Preconditions.checkArgument(code2==1000,"删除失败，状态码" + code2);

            //使用人物id进行搜索
            Boolean isexist = menjin.userInfo(scope,user_id).getJSONObject("data").containsKey("card_key");
            Preconditions.checkArgument(isexist==false,"删除门禁卡信息后，存在card_key");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

//--------------------------------通行管理-------------------------------
    /**
     *存在的设备id，配置单个权限（时间次数无限制）
     */
    @Test
    public void authaddExistDevID() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：存在的设备id，配置人员权限\n";

        String key = "";

        try {
            //注册人物
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();

            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,user_id);

            //添加权限
            String device_id = "7362126091322368";
            int pass_num = -1;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");

            JSONObject single = menjin.authAdd(device_id,scope,user_id,"USER",config);
            int code = single.getInteger("code");
            String auth_id = single.getJSONObject("data").getString("auth_id");
            Preconditions.checkArgument(code==1000,"配置权限失败，状态码" + code);

            //查询权限

            JSONObject single2 = menjin.authList(device_id,user_id);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1000,"查询权限失败，状态码" + code2 + " , 提示语为" + message2);
            JSONArray list = single2.getJSONObject("data").getJSONArray("list");
            JSONObject list_single = list.getJSONObject(0);
            String search_device_id = list_single.getString("device_id");
            String search_user_id = list_single.getString("user_id");
            JSONObject search_auth_config = list_single.getJSONObject("auth_config");
            int search_pass_num = search_auth_config.getInteger("pass_num");
            Long search_start_time = search_auth_config.getLong("start_time");
            Long search_end_time = search_auth_config.getLong("end_time");
            String auth_type = list_single.getString("auth_type");

            Preconditions.checkArgument(search_device_id.equals(device_id) && search_user_id.equals(user_id),"deviceID和userid创建后在搜索结果不一致");
            Preconditions.checkArgument(search_pass_num== pass_num,"创建时passnum=" + pass_num  + " , 搜索时=" + search_pass_num);
            Preconditions.checkArgument(search_start_time.equals(start_time),"创建时start_time=" + start_time  + " , 搜索时=" + search_start_time);
            Preconditions.checkArgument(search_end_time.equals(end_time),"创建时end_time=" + end_time  + " , 搜索时=" + search_end_time);
            Preconditions.checkArgument(auth_type.equals("USER"),"创建时auth_type=" + "人员"  + " , 搜索时=" + auth_type);

            //删除通行权限
            JSONObject single3 = menjin.authDelete(auth_id);
            int code3 = single3.getInteger("code");
            String message3 = single2.getString("message");
            Preconditions.checkArgument(code3==1000,"删除权限失败，状态码" + code3 + " , 提示语为" + message3);


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *不存在的设备id，配置单个权限（时间次数无限制）  这期先不改 BUG1638
     */
    //@Test
    public void authaddNotExistDevID() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：不存在的设备id，配置人员权限\n";

        String key = "";

        try {
            //注册人物
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();

            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,user_id);
            //添加权限
            String device_id = "lxq123456789098765";

            int pass_num = -1;
            Long start_time = 1586519251000L;
            Long end_time = 1586519251000L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");

            JSONObject single = menjin.authAdd(device_id,scope,user_id,"USER",config);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==0,"配置权限失败，状态码" + code + " , 提示语为" + message);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *存在的设备id，配置人员权限，权限传空
     */
    @Test
    public void authaddNullAuth() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：存在的设备id，配置人员权限为空\n";

        String key = "";

        try {
            //注册人物
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();

            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,user_id);

            //添加权限
            String device_id = menjin.device;
            JSONObject auth_config = JSON.parseObject("");
            JSONObject single = menjin.authAdd(device_id,scope,user_id,"人员",auth_config);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1001,"配置权限失败，状态码" + code + " , 提示语为" + message);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    /**
     *不存在的权限id，删除权限
     * */
    @Test
    public void authdeleteNotExistID() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：删除不存在的权限id\n";

        String key = "";

        try {

            //删除通行权限
            String auth_id = "12345678"; //不存在的权限id
            JSONObject single = menjin.authDelete(auth_id);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1001,"删除权限失败，状态码" + code + " , 提示语为" + message);


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *权限类=USER，user_id为空，删除权限
     * */
    @Test
    public void authdeleteNullUserID() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：删除权限时，权限类=USER，user_id为空\n";

        String key = "";

        try {

            //删除通行权限
            String scope = menjin.scopeUser;
            List<String> user_id = new ArrayList<String>();// 人物id为空
            JSONObject single = menjin.authDelete(user_id,scope);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1001,"期待1001，实际" + code + " , 提示语为" + message);


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *权限类=USER，user_id不为空\存在\有权限，删除权限
     * */
    @Test
    public void authdeleteExistUserID() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：删除权限时，权限类=USER，user_id存在、非空、有权限\n";

        String key = "";

        try {

            //人物注册
            String scope = menjin.scopeUser; //层级
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "URL";
            String face_image = "https://thumbnail0.baidupcs.com/thumbnail/a096cb0e3p286ff77a27687d8fa3f6f8?fid=4209926431-250528-714156240075946&time=1586401200&rt=sh&sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-aKKjXIEXnuKAKQdvvJQwl7pKUss%3D&expires=8h&chkv=0&chkbd=0&chkpc=&dp-logid=2306939513356739217&dp-callid=0&size=c710_u400&quality=100&vuk=-&ft=video";
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //添加权限
            String device_id = menjin.device;

            int pass_num = -1;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            JSONObject single = menjin.authAdd(device_id,scope,user_id,"USER",config);
            String authid = single.getJSONObject("data").getString("auth_id");
            //删除通行权限
            List<String> user_id2 = new ArrayList<String>();// 人物id为空
            user_id2.add("\""+user_id+"\"");//添加已存在的userid
            JSONObject single2 = menjin.authDelete(user_id2,scope);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1000,"删除权限失败，状态码" + code2 + " , 提示语为" + message2);
            //查询人物权限
            menjin.authListuser(user_id);
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *权限类=USER，user_id不为空\存在\无权限，删除权限  删除不存在的权限，bug
     * */
    //@Test
    public void authdeleteExistUserNoAuth() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：删除权限时，权限类=USER，user_id存在、非空、无权限\n";

        String key = "";

        try {
            //人物注册
            String scope = "4116"; //层级
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "URL";
            String face_image = "https://thumbnail0.baidupcs.com/thumbnail/a096cb0e3p286ff77a27687d8fa3f6f8?fid=4209926431-250528-714156240075946&time=1586401200&rt=sh&sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-aKKjXIEXnuKAKQdvvJQwl7pKUss%3D&expires=8h&chkv=0&chkbd=0&chkpc=&dp-logid=2306939513356739217&dp-callid=0&size=c710_u400&quality=100&vuk=-&ft=video";
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //删除通行权限，该用户未配置权限
            List<String> user_id2 = new ArrayList<String>();// 人物id为空
            user_id2.add("\""+user_id+"\"");//添加已存在的userid
            JSONObject single2 = menjin.authDelete(user_id2,scope); /////////要改！！！！！！
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1001,"期待1001，实际" + code2 + " , 提示语为" + message2);


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     *权限类=USER，user_id不为空\不存在，删除权限 删除不存在的权限，bug
     * */
    //@Test
    public void authdeleteNotExistUserID() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：删除权限时，权限类=USER，user_id不存在\n";

        String key = "";

        try {

            String scope = menjin.scopeUser; //层级

            //删除通行权限
            List<String> user_id2 = new ArrayList<String>();// 人物id为空
            user_id2.add("\"lxq09876543\"");//不存在的userid
            JSONObject single2 = menjin.authDelete(user_id2,scope);
            int code2 = single2.getInteger("code");

            Preconditions.checkArgument(code2==1001,"期待1001，实际" + code2);


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


//--------------------------------边缘端-------------------------------
    /**
     * 边缘端识别存在的base64
     * */
    @Test
    public void EdgeExistBase64() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：边缘端识别存在的base64\n";

        String key = "";

        try {
            String device_id = menjin.device;
            String scope = menjin.scopeUser;
            //新建人物
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //配置权限



            int pass_num = -1;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");

            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //边缘端识别
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1000,"识别用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            JSONObject user_info = data.getJSONObject("user_info");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    /**
     * 边缘端识别不存在的base64
     * */
    @Test
    public void EdgeNotExistBase64() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：边缘端识别存在的base64\n";

        String key = "";

        try {
            String device_id = "7376096262751232";//设备id
            String scope = menjin.scopeUser;

            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/廖祥茹.jpg");

            //边缘端识别
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            boolean hasauth = single.getJSONObject("data").getBoolean("has_auth");
            Preconditions.checkArgument(hasauth==false,"应无权限");
            /*
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            JSONObject user_info = data.getJSONObject("user_info");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");

             */


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 边缘端识别存在的QRcode
     * */
    @Test
    public void EdgeExistQR() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：边缘端识别存在的QRcode\n";

        String key = "";

        try {
            String device_id = "7376096262751232";//设备id
            String scope = "4116";
            //新建人物
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "URL";
            String face_image = menjin.yhs;
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //配置权限

            int pass_num = -1;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //获取二维码
            JSONObject QRdata = menjin.userQRCode(scope,user_id);
            String qrurl = QRdata.getString("qr_code");

            //边缘端识别
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrurl);
            //JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE","f359c1dd-26ed-40d7-a2b2-dee9a82ea3f5");
            int code = single.getInteger("code");

            Preconditions.checkArgument(code==1000,"识别用户" + user_id + "失败，状态码" + code);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            JSONObject user_info = data.getJSONObject("user_info");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 边缘端识别不存在的QRcode
     * */
    @Test
    public void EdgeNotExistQR() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：边缘端识别不存在的QRcode\n";

        String key = "";

        try {
            String device_id = menjin.device;
            String qrurl = "1234567890";

            //边缘端识别
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrurl);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("false"),"用户应无权限");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 边缘端识别存在的卡号
     * */
    @Test
    public void EdgeExistCardKey() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：边缘端识别存在的CardKey\n";

        String key = "";

        try {
            String device_id = menjin.device;
            String scope = menjin.scopeUser;
            //新建人物
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "URL";
            String face_image = menjin.lxq;
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");

            //配置权限


            int pass_num = -1;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);


            //边缘端识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1000,"识别用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            JSONObject user_info = data.getJSONObject("user_info");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 边缘端识别不存在的卡号
     * */
    @Test
    public void EdgeNotExistCardKey() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：边缘端识别不存在的卡号\n";

        String key = "";

        try {
            String device_id = menjin.device;
            String card_id = "1234567890";
            //边缘端识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",card_id);
            boolean hasauth = single.getJSONObject("data").getBoolean("has_auth");
            Preconditions.checkArgument(hasauth==false,"期待无权限，实际" + hasauth);
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }



//    ---------------------------------------------------基本流程--------------------------------------------------------------
    //--------------------设备未配置权限----------------------
//--------人脸-------

    @Test
    public void Face_1a() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间限制内/通行次数限制内-刷脸\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/crf.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限

            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            Long recordend = System.currentTimeMillis(); //通行时间
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");


            //通行记录上传
            menjin.passageUpload(device_id,user_id,recordend,"FACE");

            Long recordend2 = System.currentTimeMillis(); //通行时间
            //再次通行
            menjin.edgeidentify(device_id,"FACE",face_image);
            //通行记录上传
            menjin.passageUpload(device_id,user_id,recordend2,"FACE");

            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");

                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("FACE"),"通行类型不正确，期待人脸，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");
            //再次查询
            menjin.passRecdList(recordstart,recordend,device_id,user_id);


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_1b() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间内/通行次数限制外-刷脸\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/gtt.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限

            int pass_num = 0;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_1c() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间内/无通行次数限制-刷脸\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/lq.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限

            int pass_num = -1;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            Long recordend = System.currentTimeMillis(); //通行时间
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"应有权限");

            //通行记录上传
            menjin.passageUpload(device_id,user_id,recordend,"FACE");

            Long recordend2 = System.currentTimeMillis(); //通行时间
            //再次通行
            menjin.edgeidentify(device_id,"FACE",face_image);
            //通行记录上传
            menjin.passageUpload(device_id,user_id,recordend2,"FACE");

            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");

                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("FACE"),"通行类型不正确，期待人脸，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_1d() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间外/有通行次数限制-刷脸\n";

        String key = "";

        try {
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/lb.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限
            int pass_num = 10;
            Long start_time = System.currentTimeMillis() - 86400000;
            Long end_time = System.currentTimeMillis() - 86000000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_1e() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间外/通行次数限制外-刷脸\n";

        String key = "";

        try {
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/ltt.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限
            int pass_num = 0;
            Long start_time = System.currentTimeMillis() - 86400000;
            Long end_time = System.currentTimeMillis() - 86000000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_1f() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间外/无通行次数限制-刷脸\n";

        String key = "";

        try {
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/dyb.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限
            int pass_num = -1;
            Long start_time = System.currentTimeMillis() - 86400000;
            Long end_time = System.currentTimeMillis() - 86000000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_1g() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-无通行时间/通行次数限制内-刷脸\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/lxr.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限
            int pass_num = 10;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            Long recordend = System.currentTimeMillis(); //记录结束时间
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");


            //通行记录上传
            menjin.passageUpload(device_id,user_id,recordend,"FACE");
            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");

                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("FACE"),"通行类型不正确，期待人脸，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");
                }
            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_1h() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-无通行时间/通行次数限制外-刷脸\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/nl.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限
            int pass_num = 0;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");

            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_1i() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-无通行时间/通行次数限制-刷脸\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/任亮.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限
            int pass_num = -1;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            Long recordend = System.currentTimeMillis(); //记录结束时间
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");


            //通行记录上传
            menjin.passageUpload(device_id,user_id,recordend,"FACE");
            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");

                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("FACE"),"通行类型不正确，期待人脸，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_1j() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间限制内/通行次数限制内-刷脸\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/crf.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限

            int pass_num = 10;
            String start_time = menjin.HHmmss(1);
            String end_time = menjin.HHmmss(-1);

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);
            Long recordend = System.currentTimeMillis(); //通行时间
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");


            //通行记录上传
            menjin.passageUpload(device_id,user_id,recordend,"FACE");

            Long recordend2 = System.currentTimeMillis(); //通行时间
            //再次通行
            menjin.edgeidentify(device_id,"FACE",face_image);
            //通行记录上传
            menjin.passageUpload(device_id,user_id,recordend2,"FACE");

            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");

                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("FACE"),"通行类型不正确，期待人脸，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");
            //再次查询
            menjin.passRecdList(recordstart,recordend,device_id,user_id);


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_1k() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间限制内/通行次数限制外-刷脸\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/gtt.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限

            int pass_num = 0;
            String start_time = menjin.HHmmss(1);
            String end_time = menjin.HHmmss(-1);

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_1l() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间限制内/无通行次数限制-刷脸\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/lq.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限

            int pass_num = -1;
            String start_time = menjin.HHmmss(1);
            String end_time = menjin.HHmmss(-1);

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            Long recordend = System.currentTimeMillis(); //通行时间
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"应有权限");

            //通行记录上传
            menjin.passageUpload(device_id,user_id,recordend,"FACE");

            Long recordend2 = System.currentTimeMillis(); //通行时间
            //再次通行
            menjin.edgeidentify(device_id,"FACE",face_image);
            //通行记录上传
            menjin.passageUpload(device_id,user_id,recordend2,"FACE");

            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");

                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("FACE"),"通行类型不正确，期待人脸，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_1m() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间限制外/有通行次数限制-刷脸\n";

        String key = "";

        try {
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/lb.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限
            int pass_num = 10;
            String start_time = menjin.HHmmss(2);
            String end_time = menjin.HHmmss(1);

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_1n() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间限制外/通行次数限制外-刷脸\n";

        String key = "";

        try {
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/ltt.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限
            int pass_num = 0;
            String start_time = menjin.HHmmss(2);
            String end_time = menjin.HHmmss(1);

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_1o() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间限制外/无通行次数限制-刷脸\n";

        String key = "";

        try {
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/dyb.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限
            int pass_num = -1;
            String start_time = menjin.HHmmss(2);
            String end_time = menjin.HHmmss(1);

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_1p() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 无通行时间/通行次数限制内-刷脸\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/lxr.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限
            int pass_num = 10;
            String start_time = "00:00:00";
            String end_time = "23:59:59";
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            Long recordend = System.currentTimeMillis(); //记录结束时间
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");


            //通行记录上传
            menjin.passageUpload(device_id,user_id,recordend,"FACE");
            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");

                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("FACE"),"通行类型不正确，期待人脸，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");
                }
            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_1q() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 无通行时间/通行次数限制外-刷脸\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/nl.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限
            int pass_num = 0;
            String start_time = "00:00:00";
            String end_time = "23:59:59";
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");

            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_1r() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 无通行时间/通行次数限制-刷脸\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/任亮.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限
            int pass_num = -1;
            String start_time = "00:00:00";
            String end_time = "23:59:59";
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            Long recordend = System.currentTimeMillis(); //记录结束时间
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");


            //通行记录上传
            menjin.passageUpload(device_id,user_id,recordend,"FACE");
            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");

                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("FACE"),"通行类型不正确，期待人脸，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_2a() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-无通行时间/通行次数限制-设备禁用-刷脸\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = menjin.device;

            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //设备禁用
            menjin.operateDevice(device_id,"DISABLE");

            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");


            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_3a() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-有通行时间/通行次数限制-人物删除-刷脸\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/甘甜甜.jpg");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限

            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //人物删除
            menjin.userDelete(scope,user_id).getInteger("code");

            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_4a() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-有通行时间/通行次数限制-通行权限删除-刷脸\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/甘甜甜.jpg");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限
            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            JSONObject authsingle = menjin.authAdd(device_id,scope,user_id,"USER",config);
            String authid = authsingle.getJSONObject("data").getString("auth_id");

            //通行权限删除
            menjin.authDelete(authid);

            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //人物删除
            menjin.userDelete(scope,user_id);


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    //通行次数=1，通行两次
    @Test
    public void Face_twice() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：通行后，通行次数=0，再次通行，应失败\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/hx.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //启用设备
            String device_id = menjin.device;
            menjin.operateDevice(device_id,"ENABLE");

            //配置通行权限
            int pass_num = 1;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;



            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            JSONObject authsingle = menjin.authAdd(device_id,scope,user_id,"USER",config);
            String authid = authsingle.getJSONObject("data").getString("auth_id");

            //通行第一次
            menjin.edgeidentify(device_id,"FACE",face_image);
            //通行第二次
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //人物删除
            menjin.userDelete(scope,user_id);

            menjin.edgeidentify(device_id,"FACE",face_image);


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_oneMore() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：批量配置权限：一个用户 多设备\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/hx.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //设备1
            String device_scope =  menjin.EnDevice;
            String devicename1 = "devone" + System.currentTimeMillis();
            String devicename2 = "devtwo" + System.currentTimeMillis();
            //创建设备
            String device_id1 = menjin.deviceAdd(device_scope,devicename1).getJSONObject("data").getString("device_id");
            String device_id2 = menjin.deviceAdd(device_scope,devicename2).getJSONObject("data").getString("device_id");

            //启用设备
            menjin.operateDevice(device_id1,"ENABLE");
            menjin.operateDevice(device_id2,"ENABLE");

            //配置通行权限
            int pass_num = 3; //每个设备3次
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");

            List listdevice = new ArrayList();
            listdevice.add("\"" + device_id1+ "\"");
            listdevice.add("\"" + device_id2+ "\"");

            List listuser = new ArrayList();
            listuser.add("\"" + user_id+ "\"");

            JSONObject authsingle = menjin.authAddBatch(listdevice,scope,listuser,"USER",config);
            String authid = authsingle.getJSONObject("data").getString("auth_id");

            //通行第一次
            menjin.edgeidentify(device_id1,"FACE",face_image);
            //通行第2次
            menjin.edgeidentify(device_id1,"FACE",face_image);
            //通行第3次
            menjin.edgeidentify(device_id2,"FACE",face_image);
            //通行第4次
            JSONObject  single = menjin.edgeidentify(device_id2,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");

            //人物删除
            menjin.userDelete(scope,user_id);




        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_Moreone() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：批量配置权限：多个用户 一个设备\n";

        String key = "";

        try {

            //注册人物1，单一人脸
            String scope = menjin.scopeUser;
            String user_id1 = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/hx.png");
            menjin.userAdd(scope,user_id1,image_type,face_image,"","");

            //注册人物2，单一人脸
            String user_id2 = "user" + System.currentTimeMillis();
            String face_image2 = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/ym.png");
            menjin.userAdd(scope,user_id2,image_type,face_image2,"","");

            //设备1
            String device_scope =  menjin.EnDevice;
            String devicename1 = "devone" + System.currentTimeMillis();
            //创建设备
            String device_id1 = menjin.deviceAdd(device_scope,devicename1).getJSONObject("data").getString("device_id");

            //启用设备
            menjin.operateDevice(device_id1,"ENABLE");

            //配置通行权限
            int pass_num = 1; //每个人1次
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");

            List listdevice = new ArrayList();
            listdevice.add("\"" + device_id1+ "\"");

            List listuser = new ArrayList();
            listuser.add("\"" + user_id1+ "\"");
            listuser.add("\"" + user_id2+ "\"");

            menjin.authAddBatch(listdevice,scope,listuser,"USER",config);


            //通行第一次
            menjin.edgeidentify(device_id1,"FACE",face_image);
            //通行第2次
            menjin.edgeidentify(device_id1,"FACE",face_image2);

            //通行第2次
            JSONObject  single = menjin.edgeidentify(device_id1,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id1+"应无权限");

            //人物删除
            menjin.userDelete(scope,user_id1);
            menjin.userDelete(scope,user_id2);




        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Face_MoreMore() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：批量配置权限：多个用户 多个设备\n";

        String key = "";

        try {

            //注册人物1，单一人脸
            String scope = menjin.scopeUser;
            String user_id1 = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/hx.png");
            menjin.userAdd(scope,user_id1,image_type,face_image,"","");

            //注册人物2，单一人脸
            String user_id2 = "user" + System.currentTimeMillis();
            String face_image2 = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/ym.png");
            menjin.userAdd(scope,user_id2,image_type,face_image2,"","");

            //设备1
            String device_scope =  menjin.EnDevice;
            String devicename1 = "devone" + System.currentTimeMillis();
            String devicename2 = "devtwo" + System.currentTimeMillis();
            //创建设备
            String device_id1 = menjin.deviceAdd(device_scope,devicename1).getJSONObject("data").getString("device_id");
            String device_id2 = menjin.deviceAdd(device_scope,devicename2).getJSONObject("data").getString("device_id");

            //启用设备
            menjin.operateDevice(device_id1,"ENABLE");
            menjin.operateDevice(device_id2,"ENABLE");

            //配置通行权限
            int pass_num = 1; //每个人1次
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");

            List listdevice = new ArrayList();
            listdevice.add("\"" + device_id1+ "\"");
            listdevice.add("\"" + device_id2+ "\"");

            List listuser = new ArrayList();
            listuser.add("\"" + user_id1+ "\"");
            listuser.add("\"" + user_id2+ "\"");

            menjin.authAddBatch(listdevice,scope,listuser,"USER",config);


            //通行第一次
            String has_auth1 = menjin.edgeidentify(device_id1,"FACE",face_image).getJSONObject("data").getString("has_auth");
            Preconditions.checkArgument(has_auth1.equals("true"),"用户1设备1"+user_id1+"无权限");
            String has_auth2 = menjin.edgeidentify(device_id2,"FACE",face_image).getJSONObject("data").getString("has_auth");
            Preconditions.checkArgument(has_auth2.equals("true"),"用户1设备2"+user_id1+"无权限");
            String has_auth3 = menjin.edgeidentify(device_id1,"FACE",face_image2).getJSONObject("data").getString("has_auth");
            Preconditions.checkArgument(has_auth3.equals("true"),"用户2设备1"+user_id1+"无权限");
            String has_auth4 = menjin.edgeidentify(device_id2,"FACE",face_image2).getJSONObject("data").getString("has_auth");
            Preconditions.checkArgument(has_auth4.equals("true"),"用户2设备2"+user_id1+"无权限");

            //人物删除
            menjin.userDelete(scope,user_id1);
            menjin.userDelete(scope,user_id2);




        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }




//--------刷卡-------

    @Test
    public void Card_1a() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间限制内/通行次数限制内-刷卡\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = menjin.device;

            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);
            Long recordend = System.currentTimeMillis(); //记录结束时间
            //门卡识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");
            //上传记录
            menjin.passageUpload(device_id,user_id,recordend,"CARD");
            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");
                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("CARD"),"通行类型不正确，期待刷卡，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Card_1b() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间内/通行次数限制外-刷卡\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = menjin.device;

            int pass_num = 0;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //门卡识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Card_1c() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：校验：注册-通行时间内/无通行次数限制-刷卡\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = menjin.device;

            int pass_num = -1;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);
            Long recordend = System.currentTimeMillis(); //记录结束时间
            //门卡识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");
            //上传记录
            menjin.passageUpload(device_id,user_id,recordend,"CARD");
            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");
                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("CARD"),"通行类型不正确，期待刷卡，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Card_1d() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间限制外/通行次数限制内-刷卡\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = menjin.device;

            int pass_num = 10;
            Long start_time = System.currentTimeMillis() - 86400000;
            Long end_time = System.currentTimeMillis() - 86000000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //门卡识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Card_1e() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间限制外/通行次数限制外-刷卡\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = menjin.device;

            int pass_num = 0;
            Long start_time = System.currentTimeMillis() - 86400000;
            Long end_time = System.currentTimeMillis() - 86000000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //门卡识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Card_1f() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间限制外/通行次数无限制-刷卡\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = menjin.device;

            int pass_num = -1;
            Long start_time = System.currentTimeMillis() - 86400000;
            Long end_time = System.currentTimeMillis() - 86000000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //门卡识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Card_1g() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-无通行时间限制/通行次数限制内-刷卡\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = menjin.device;

            int pass_num = 10;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);
            Long recordend = System.currentTimeMillis(); //记录结束时间
            //门卡识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");

            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");
            //上传记录
            menjin.passageUpload(device_id,user_id,recordend,"CARD");
            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");
                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("CARD"),"通行类型不正确，期待刷卡，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Card_1h() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-无通行时间限制/通行次数限制外-刷卡\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = menjin.device;

            int pass_num = 0;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);
            Long recordend = System.currentTimeMillis(); //记录结束时间
            //门卡识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");

            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Card_1i() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：刷卡-身份认证通过-人设备已绑定-无通行时间/通行次数限制\n";

        String key = "";

        try {

            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = menjin.device;

            int pass_num = -1;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);
            Long recordend = System.currentTimeMillis(); //记录结束时间
            //门卡识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");

            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");
            //上传记录
            menjin.passageUpload(device_id,user_id,recordend,"CARD");
            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");
                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("CARD"),"通行类型不正确，期待刷卡，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");




        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Card_1j() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间限制内/通行次数限制内-刷卡\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = menjin.device;

            int pass_num = 10;
            String start_time = menjin.HHmmss(1);
            String end_time = menjin.HHmmss(-1);

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);
            Long recordend = System.currentTimeMillis(); //记录结束时间
            //门卡识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");
            //上传记录
            menjin.passageUpload(device_id,user_id,recordend,"CARD");
            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");
                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("CARD"),"通行类型不正确，期待刷卡，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Card_1k() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间内/通行次数限制外-刷卡\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = menjin.device;

            int pass_num = 0;
            String start_time = menjin.HHmmss(1);
            String end_time = menjin.HHmmss(-1);

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //门卡识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Card_1l() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：校验：注册-每天 通行时间内/无通行次数限制-刷卡\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = menjin.device;

            int pass_num = -1;
            String start_time = menjin.HHmmss(1);
            String end_time = menjin.HHmmss(-1);

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);
            Long recordend = System.currentTimeMillis(); //记录结束时间
            //门卡识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");
            //上传记录
            menjin.passageUpload(device_id,user_id,recordend,"CARD");
            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");
                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("CARD"),"通行类型不正确，期待刷卡，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Card_1m() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间限制外/通行次数限制内-刷卡\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = menjin.device;

            int pass_num = 10;
            String start_time = menjin.HHmmss(2);
            String end_time = menjin.HHmmss(1);

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //门卡识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Card_1n() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间限制外/通行次数限制外-刷卡\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = menjin.device;

            int pass_num = 0;
            String start_time = menjin.HHmmss(2);
            String end_time = menjin.HHmmss(1);

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //门卡识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Card_1o() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间限制外/通行次数无限制-刷卡\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = menjin.device;

            int pass_num = -1;
            String start_time = menjin.HHmmss(2);
            String end_time = menjin.HHmmss(1);

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //门卡识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Card_1p() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 无通行时间限制/通行次数限制内-刷卡\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = menjin.device;

            int pass_num = 10;
            String start_time = "00:00:00";
            String end_time = "23:59:59";
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);
            Long recordend = System.currentTimeMillis(); //记录结束时间
            //门卡识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");

            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");
            //上传记录
            menjin.passageUpload(device_id,user_id,recordend,"CARD");
            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");
                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("CARD"),"通行类型不正确，期待刷卡，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Card_1q() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 无通行时间限制/通行次数限制外-刷卡\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = menjin.device;

            int pass_num = 0;
            String start_time = "00:00:00";
            String end_time = "23:59:59";
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);
            Long recordend = System.currentTimeMillis(); //记录结束时间
            //门卡识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");

            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void Card_1r() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：刷卡-身份认证通过-人设备已绑定-无通行时间/通行次数限制\n";

        String key = "";

        try {

            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = menjin.device;

            int pass_num = -1;
            String start_time = "00:00:00";
            String end_time = "23:59:59";
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);
            Long recordend = System.currentTimeMillis(); //记录结束时间
            //门卡识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");

            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");
            //上传记录
            menjin.passageUpload(device_id,user_id,recordend,"CARD");
            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");
                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("CARD"),"通行类型不正确，期待刷卡，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");




        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


//--------扫码-------

    @Test
    public void QRcode_1a() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间限制内/通行次数限制内-扫码\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = menjin.device;


            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time+ 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getString("qr_code");
            Long recordend = System.currentTimeMillis(); //记录结束时间
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");

            //上传记录
            menjin.passageUpload(device_id,user_id,recordend,"QR_CODE");

            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");

                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("QR_CODE"),"通行类型不正确，期待扫码，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void QRcode_1b() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间限制内/通行次数限制外-扫码\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = menjin.device;


            int pass_num = 0;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time+ 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getString("qr_code");
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void QRcode_1c() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间限制内/无通行次数限制-扫码\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = menjin.device;


            int pass_num = -1;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time+ 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getString("qr_code");
            Long recordend = System.currentTimeMillis(); //记录结束时间
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");

            //上传记录
            menjin.passageUpload(device_id,user_id,recordend,"QR_CODE");

            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");

                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("QR_CODE"),"通行类型不正确，期待扫码，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void QRcode_1d() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间限制外/通行次数限制内-扫码\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = menjin.device;


            int pass_num = 10;
            Long start_time = System.currentTimeMillis() - 86400000;
            Long end_time = System.currentTimeMillis() - 86000000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getString("qr_code");
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void QRcode_1e() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间限制外/通行次数限制外-扫码\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = menjin.device;


            int pass_num = 0;
            Long start_time = System.currentTimeMillis() - 86400000;
            Long end_time = System.currentTimeMillis() - 86000000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getString("qr_code");
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void QRcode_1f() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间限制外/通行次数无限制-扫码\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = menjin.device;


            int pass_num = 0;
            Long start_time = System.currentTimeMillis() - 86400000;
            Long end_time = System.currentTimeMillis() - 86000000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getString("qr_code");
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void QRcode_1g() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间无限制/通行次数限制内-扫码\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = menjin.device;


            int pass_num = 10;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getString("qr_code");
            Long recordend = System.currentTimeMillis(); //记录结束时间
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");

            //上传记录
            menjin.passageUpload(device_id,user_id,recordend,"QR_CODE");

            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");

                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("QR_CODE"),"通行类型不正确，期待扫码，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void QRcode_1h() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间无限制/通行次数限制外-扫码\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = menjin.device;


            int pass_num = 0;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getString("qr_code");
            Long recordend = System.currentTimeMillis(); //记录结束时间
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void QRcode_1i() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-通行时间无限制/通行次数无限制-扫码\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = menjin.device;


            int pass_num = -1;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getString("qr_code");
            Long recordend = System.currentTimeMillis(); //记录结束时间
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");

            //上传记录
            menjin.passageUpload(device_id,user_id,recordend,"QR_CODE");

            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");

                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("QR_CODE"),"通行类型不正确，期待扫码，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void QRcode_1j() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间限制内/通行次数限制内-扫码\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = menjin.device;


            int pass_num = 10;
            String start_time = menjin.HHmmss(1);
            String end_time = menjin.HHmmss(-1);

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getString("qr_code");
            Long recordend = System.currentTimeMillis(); //记录结束时间
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");

            //上传记录
            menjin.passageUpload(device_id,user_id,recordend,"QR_CODE");

            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");

                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("QR_CODE"),"通行类型不正确，期待扫码，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void QRcode_1k() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间限制内/通行次数限制外-扫码\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = menjin.device;


            int pass_num = 0;
            String start_time = menjin.HHmmss(1);
            String end_time = menjin.HHmmss(-1);

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getString("qr_code");
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void QRcode_1l() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间限制内/无通行次数限制-扫码\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = menjin.device;


            int pass_num = -1;
            String start_time = menjin.HHmmss(1);
            String end_time = menjin.HHmmss(-1);

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getString("qr_code");
            Long recordend = System.currentTimeMillis(); //记录结束时间
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");

            //上传记录
            menjin.passageUpload(device_id,user_id,recordend,"QR_CODE");

            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");

                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("QR_CODE"),"通行类型不正确，期待扫码，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void QRcode_1m() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间限制外/通行次数限制内-扫码\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = menjin.device;


            int pass_num = 10;
            String start_time = menjin.HHmmss(2);
            String end_time = menjin.HHmmss(1);

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getString("qr_code");
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void QRcode_1n() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间限制外/通行次数限制外-扫码\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = menjin.device;


            int pass_num = 0;
            String start_time = menjin.HHmmss(2);
            String end_time = menjin.HHmmss(1);

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getString("qr_code");
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void QRcode_1o() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间限制外/通行次数无限制-扫码\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = menjin.device;


            int pass_num = 0;
            String start_time = menjin.HHmmss(2);
            String end_time = menjin.HHmmss(1);

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getString("qr_code");
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void QRcode_1p() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间无限制/通行次数限制内-扫码\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = menjin.device;


            int pass_num = 10;
            String start_time = "00:00:00";
            String end_time = "23:59:59";
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getString("qr_code");
            Long recordend = System.currentTimeMillis(); //记录结束时间
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");

            //上传记录
            menjin.passageUpload(device_id,user_id,recordend,"QR_CODE");

            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");

                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("QR_CODE"),"通行类型不正确，期待扫码，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void QRcode_1q() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间无限制/通行次数限制外-扫码\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = menjin.device;


            int pass_num = 0;
            String start_time = "00:00:00";
            String end_time = "23:59:59";
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getString("qr_code");
            Long recordend = System.currentTimeMillis(); //记录结束时间
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void QRcode_1r() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-每天 通行时间无限制/通行次数无限制-扫码\n";

        String key = "";

        try {
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = menjin.device;


            int pass_num = -1;
            String start_time = "00:00:00";
            String end_time = "23:59:59";
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getString("qr_code");
            Long recordend = System.currentTimeMillis(); //记录结束时间
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");

            //上传记录
            menjin.passageUpload(device_id,user_id,recordend,"QR_CODE");

            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(i);
                Long time = single2.getLong("time");

                if (time > recordstart && time < recordend){
                    String pass_type = single2.getString("pass_type");
                    String search_device_id = single2.getString("device_id");
                    String search_user_id = single2.getString("user_id");
                    Preconditions.checkArgument(pass_type.equals("QR_CODE"),"通行类型不正确，期待扫码，实际："+ pass_type);
                    Preconditions.checkArgument(search_device_id.equals(device_id),"deviceid不正确");
                    Preconditions.checkArgument(search_user_id.equals(user_id),"userid不正确");

                }

            }
            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    //--------------------设备有权限----------------------

    @Test
    public void device_auth1() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-设备通行时间限制内/通行次数限制内/外-人物有权限-刷脸\n";

        String key = "";

        try {
            String device_scope =  menjin.EnDevice;
            String devicename = "dev" + System.currentTimeMillis();
            //创建设备
            String device_id = menjin.deviceAdd(device_scope,devicename).getJSONObject("data").getString("device_id");
            //启用设备
            menjin.operateDevice(device_id,"ENABLE");

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/crf.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //配置人物通行权限
            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //配置设备通行权限
            int pass_num2 = 1;
            JSONObject config2 = menjin.authconfig(pass_num2,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,"","","DEVICE",config2);

            //人物通行
            menjin.edgeidentify(device_id,"FACE",face_image);

            //再次通行
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");

            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void device_auth2() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-设备通行时间限制外/通行次数限制内-人物有权限-刷脸\n";

        String key = "";

        try {
            String device_scope =  menjin.EnDevice;
            String devicename = "dev" + System.currentTimeMillis();
            //创建设备
            String device_id = menjin.deviceAdd(device_scope,devicename).getJSONObject("data").getString("device_id");
            //启用设备
            menjin.operateDevice(device_id,"ENABLE");

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/crf.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //配置人物通行权限
            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //配置设备通行权限
            int pass_num2 = 1;
            Long start_time2 = menjin.todayStartLong() - 86400000 - 86400000;
            Long end_time2 = start_time - 86400000;
            JSONObject config2 = menjin.authconfig(pass_num2,start_time2,end_time2,"FOREVER");
            menjin.authAdd(device_id,"","","DEVICE",config2);

            //人物通行

            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");

            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void device_auth3() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-设备通行时间无限制/通行次数限制内-人物有权限-刷脸\n";

        String key = "";

        try {
            String device_scope =  menjin.EnDevice;
            String devicename = "dev" + System.currentTimeMillis();
            //创建设备
            String device_id = menjin.deviceAdd(device_scope,devicename).getJSONObject("data").getString("device_id");
            //启用设备
            menjin.operateDevice(device_id,"ENABLE");

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/crf.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //配置人物通行权限
            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //配置设备通行权限
            int pass_num2 = 10;
            Long start_time2 = -1L;
            Long end_time2 = -1L;
            JSONObject config2 = menjin.authconfig(pass_num2,start_time2,end_time2,"FOREVER");
            menjin.authAdd(device_id,"","","DEVICE",config2);

            //人物通行

            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");

            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void device_auth4() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-设备每天通行时间限制内/通行次数限制内/外-人物有权限-刷脸\n";

        String key = "";

        try {
            String device_scope =  menjin.EnDevice;
            String devicename = "dev" + System.currentTimeMillis();
            //创建设备
            String device_id = menjin.deviceAdd(device_scope,devicename).getJSONObject("data").getString("device_id");
            //启用设备
            menjin.operateDevice(device_id,"ENABLE");

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/crf.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //配置人物通行权限
            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //配置设备通行权限
            int pass_num2 = 1;
            String start_time2 = menjin.HHmmss(1);
            String end_time2 = menjin.HHmmss(-1);
            JSONObject config2 = menjin.authconfig(pass_num2,start_time2,end_time2,"DAY");
            menjin.authAdd(device_id,"","","DEVICE",config2);
            //人物通行
            menjin.edgeidentify(device_id,"FACE",face_image);
            //人物再次通行
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void device_auth5() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-设备每天通行时间限制外/通行次数限制内-人物有权限-刷脸\n";

        String key = "";

        try {
            String device_scope =  menjin.EnDevice;
            String devicename = "dev" + System.currentTimeMillis();
            //创建设备
            String device_id = menjin.deviceAdd(device_scope,devicename).getJSONObject("data").getString("device_id");
            //启用设备
            menjin.operateDevice(device_id,"ENABLE");

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/crf.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //配置人物通行权限
            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //配置设备通行权限
            int pass_num2 = 1;
            String start_time2 = menjin.HHmmss(2);
            String end_time2 = menjin.HHmmss(1);
            JSONObject config2 = menjin.authconfig(pass_num2,start_time2,end_time2,"DAY");
            menjin.authAdd(device_id,"","","DEVICE",config2);
            //人物通行
            menjin.edgeidentify(device_id,"FACE",face_image);
            //人物再次通行
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void device_auth6() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-设备每天通行时间限制外/通行次数限制内-人物无权限-刷脸\n";

        String key = "";

        try {
            String device_scope =  menjin.EnDevice;
            String devicename = "dev" + System.currentTimeMillis();
            //创建设备
            String device_id = menjin.deviceAdd(device_scope,devicename).getJSONObject("data").getString("device_id");
            //启用设备
            menjin.operateDevice(device_id,"ENABLE");

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/crf.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //配置人物通行权限
            int pass_num = 0;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //配置设备通行权限
            int pass_num2 = 1;
            String start_time2 = menjin.HHmmss(2);
            String end_time2 = menjin.HHmmss(1);
            JSONObject config2 = menjin.authconfig(pass_num2,start_time2,end_time2,"DAY");
            menjin.authAdd(device_id,"","","DEVICE",config2);

            //人物再次通行
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void device_auth7() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-设备每天通行时间限制外/通行次数限制外-人物有权限-刷脸\n";

        String key = "";

        try {
            String device_scope =  menjin.EnDevice;
            String devicename = "dev" + System.currentTimeMillis();
            //创建设备
            String device_id = menjin.deviceAdd(device_scope,devicename).getJSONObject("data").getString("device_id");
            //启用设备
            menjin.operateDevice(device_id,"ENABLE");

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/crf.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //配置人物通行权限
            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //配置设备通行权限
            int pass_num2 = 0;
            String start_time2 = menjin.HHmmss(2);
            String end_time2 = menjin.HHmmss(1);
            JSONObject config2 = menjin.authconfig(pass_num2,start_time2,end_time2,"DAY");
            menjin.authAdd(device_id,"","","DEVICE",config2);
            //人物通行
            menjin.edgeidentify(device_id,"FACE",face_image);
            //人物再次通行
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void device_auth8() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-设备每天通行时间限制外/通行次数限制外-人物无权限-刷脸\n";

        String key = "";

        try {
            String device_scope =  menjin.EnDevice;
            String devicename = "dev" + System.currentTimeMillis();
            //创建设备
            String device_id = menjin.deviceAdd(device_scope,devicename).getJSONObject("data").getString("device_id");
            //启用设备
            menjin.operateDevice(device_id,"ENABLE");

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/crf.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //配置人物通行权限
            int pass_num = 0;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //配置设备通行权限
            int pass_num2 = 0;
            String start_time2 = menjin.HHmmss(2);
            String end_time2 = menjin.HHmmss(1);
            JSONObject config2 = menjin.authconfig(pass_num2,start_time2,end_time2,"DAY");
            menjin.authAdd(device_id,"","","DEVICE",config2);

            //人物再次通行
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void device_auth9() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-设备每天通行时间限制外/通行次数无限制-人物有权限-刷脸\n";

        String key = "";

        try {
            String device_scope =  menjin.EnDevice;
            String devicename = "dev" + System.currentTimeMillis();
            //创建设备
            String device_id = menjin.deviceAdd(device_scope,devicename).getJSONObject("data").getString("device_id");
            //启用设备
            menjin.operateDevice(device_id,"ENABLE");

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/crf.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //配置人物通行权限
            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //配置设备通行权限
            int pass_num2 = -1;
            String start_time2 = menjin.HHmmss(2);
            String end_time2 = menjin.HHmmss(1);
            JSONObject config2 = menjin.authconfig(pass_num2,start_time2,end_time2,"DAY");
            menjin.authAdd(device_id,"","","DEVICE",config2);
            //人物通行
            menjin.edgeidentify(device_id,"FACE",face_image);
            //人物再次通行
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void device_auth10() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册-设备每天通行时间限制外/通行次数无限制-人物无权限-刷脸\n";

        String key = "";

        try {
            String device_scope =  menjin.EnDevice;
            String devicename = "dev" + System.currentTimeMillis();
            //创建设备
            String device_id = menjin.deviceAdd(device_scope,devicename).getJSONObject("data").getString("device_id");
            //启用设备
            menjin.operateDevice(device_id,"ENABLE");

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/crf.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //配置人物通行权限
            int pass_num = 0;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_id,"USER",config);

            //配置设备通行权限
            int pass_num2 = -1;
            String start_time2 = menjin.HHmmss(2);
            String end_time2 = menjin.HHmmss(1);
            JSONObject config2 = menjin.authconfig(pass_num2,start_time2,end_time2,"DAY");
            menjin.authAdd(device_id,"","","DEVICE",config2);

            //人物再次通行
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //删除人物
            int code = menjin.userDelete(scope,user_id).getInteger("code");
            Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void device_auth11() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：批量配置设备权限：一个用户 多设备\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/hx.png");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //设备1
            String device_scope =  menjin.EnDevice;
            String devicename1 = "devone" + System.currentTimeMillis();
            String devicename2 = "devtwo" + System.currentTimeMillis();
            //创建设备
            String device_id1 = menjin.deviceAdd(device_scope,devicename1).getJSONObject("data").getString("device_id");
            String device_id2 = menjin.deviceAdd(device_scope,devicename2).getJSONObject("data").getString("device_id");

            //启用设备
            menjin.operateDevice(device_id1,"ENABLE");
            menjin.operateDevice(device_id2,"ENABLE");

            //配置人物通行权限
            int pass_num = 3;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");

            List listdevice = new ArrayList();
            listdevice.add("\"" + device_id1+ "\"");
            listdevice.add("\"" + device_id2+ "\"");

            List listuser = new ArrayList();
            listuser.add("\"" + user_id+ "\"");

            menjin.authAddBatch(listdevice,scope,listuser,"USER",config);

            //配置设备通行权限
            int pass_num1 = 0;
            JSONObject config1 = menjin.authconfig(pass_num1,start_time,end_time,"FOREVER");
            List listusernull = new ArrayList();
            menjin.authAddBatch(listdevice,"",listusernull,"DEVICE",config1);


            JSONObject  single = menjin.edgeidentify(device_id2,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            Preconditions.checkArgument(has_auth.equals("false"),"用户"+user_id+"应无权限");

            //人物删除
            //menjin.userDelete(scope,user_id);




        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }



    //--------------------------------scope/deviceid特殊字符校验-------------------------------
    @Test
    public void useradd_specialscope() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人物注册-层级传递非法字符\n";

        String key = "";

        try {
            String scope = "层级我1！@#：{}?><Ms";
            String userid = "123456";
            String username = "123456";
            String imagetype = "BASE64";
            String faceimage = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            String cardid = ""+System.currentTimeMillis();
            JSONObject single = menjin.userAdd(scope,userid,imagetype,faceimage,cardid,username);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际" + code);
            Preconditions.checkArgument(message.contains("scope"),"提示语为：" + message);



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void useradd_specialid() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人物注册-用户id传递非法字符\n";

        String key = "";

        try {
            String scope = menjin.scopeUser;
            String userid = "我1！@#：{}?><Ms";
            String username = "123456";
            String imagetype = "BASE64";
            String faceimage = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            String cardid = ""+System.currentTimeMillis();
            JSONObject single = menjin.userAdd(scope,userid,imagetype,faceimage,cardid,username);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际" + code);
            Preconditions.checkArgument(message.contains("user_id"),"提示语为：" + message);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void useradd_specialcard() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人物注册-cardkey传递非法字符\n";

        String key = "";

        try {
            String scope = menjin.scopeUser;
            String userid = "" + System.currentTimeMillis();
            String username = "123456";
            String imagetype = "BASE64";
            String faceimage = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            String cardid = "我1！@#：{}?><Ms";
            JSONObject single = menjin.userAdd(scope,userid,imagetype,faceimage,cardid,username);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际" + code);
            Preconditions.checkArgument(message.contains("card"),"提示语为：" + message);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void useradd_specialimage() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人物注册-imagetype类型不正确\n";

        String key = "";

        try {
            String scope = menjin.scopeUser;
            String userid = "" + System.currentTimeMillis();
            String imagetype = "asdf";
            String faceimage = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            JSONObject single = menjin.userAdd(scope,userid,imagetype,faceimage,"","");
            int code = single.getInteger("code");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际" + code);


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void authadd_specialauthtype() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：单个通行权限配置-权限类型传递非期待字符\n";

        String key = "";

        try {
            String deviceid = "";
            String authtype = "DS";
            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");

            JSONObject single = menjin.authAdd(deviceid,"","",authtype,config);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际" + code);
            //Preconditions.checkArgument(message.contains(""),"提示语为：" + message);



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void authadd_typeUSER() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：单个通行权限配置-权限类型为user，不传递scope/userid\n";

        String key = "";

        try {
            String deviceid = menjin.scopeUser;
            String authtype = "USER";
            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");

            JSONObject single = menjin.authAdd(deviceid,"","",authtype,config);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际" + code);
            Preconditions.checkArgument(message.equals("[scope]不可为空"),"提示语为：" + message);



        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void edgeidentify_wrongtype() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人脸识别/门禁卡识别-识别类型非期待值\n";

        String key = "";

        try {
            String deviceid = menjin.scopeUser;
            String type = "QR";
            String identify = "12345";

            JSONObject single = menjin.edgeidentify(deviceid,type,identify);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际" + code);
            //Preconditions.checkArgument(message.equals("[type] cannot be null"),"提示语为：" + message);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void authsync_special() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：配置权限信息同步-特殊字符\n";

        String key = "";

        try {
            String deviceid = "我1！@#：{}?><Ms";

            JSONObject single = menjin.authSync(deviceid);
            int code = single.getInteger("code");
            //String message = single.getString("message");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际" + code);
            //Preconditions.checkArgument(message.equals("[type] cannot be null"),"提示语为：" + message);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void userinfodel_special() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人物信息解绑deletetype包含特殊字符\n";

        String key = "";

        try {
            //人物注册
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //信息解绑
            JSONObject single = menjin.userInfoDelete(scope,user_id,"CARD");
            int code = single.getInteger("code");
            Preconditions.checkArgument(code==1001,"期待状态码1001，实际" + code);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }







//    ---------------------------------------------------通用方法--------------------------------------------------------------

    private void saveData(Case aCase, String ciCaseName, String caseName, String caseDescription) {
        setBasicParaToDB(aCase, ciCaseName, caseName, caseDescription);
        //qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason())) {
            logger.error(aCase.getFailReason());
            dingPush("门禁日常-系统场景 \n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
        }
    }

    private void dingPush(String msg) {
        AlarmPush alarmPush = new AlarmPush();
        if (DEBUG.trim().toLowerCase().equals("false")) {
//            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        }
        msg = msg.replace("java.lang.Exception: ", "异常：");
        msg = msg.replace("java.lang.IllegalArgumentException:", "异常：");
        alarmPush.dailyRgn(msg);
        this.FAIL = true;
        Assert.assertNull(aCase.getFailReason());
    }

    private void dingPushFinal() {
        if (DEBUG.trim().toLowerCase().equals("false") && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
//            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

            //15898182672 华成裕
            //18513118484 杨航
            //15011479599 谢志东
            //18600872221 蔡思明
            String[] rd = {"18513118484", //杨航
                    "15011479599", //谢志东
                    "15898182672"}; //华成裕
            alarmPush.alarmToRd(rd);
        }
    }


    private static String getImgStr(String imgFile) { //图片转base64
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(data));
    }

       //public static void main(String[] args) throws Exception {// ---不用理我！
        //String path = "src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/分辨率较低.png";
        //String imgbese = getImgStr(path);
        //String img_path="data:image/jpeg;base64,"+imgbese ;
        //System.out.println(imgbese);
        //System.out.println(img_path);

           /**
           String config = "{\n" +
                   "                \"pass_num\":\"通行次数, 若为-1则无次数限制\",\n" +
                   "                    \"start_time\":\"Long型时间戳, 通行时间限制. 若为-1则无时间限制\",\n" +
                   "                    \"end_time\":\"Long型时间戳, 通行时间限制. 若为-1则无时间限制\"\n" +
                   "            }";
           JSONObject auth_config = JSON.parseObject(config);
           System.out.println(auth_config);

            */
    //}
    @Test
    public void deleteuser() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "删除人物\n";

        String key = "";

        try {
            menjin.userDelete(menjin.scopeUser,"user1587210837594");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }




}