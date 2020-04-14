
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
import java.util.ArrayList;
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

    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_DAILY_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-daily-test/buildWithParameters?case_name=";

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


    //@AfterClass //还没改
    public void clean() {
        qaDbUtil.closeConnection();
        dingPushFinal();
    }

    //@BeforeMethod //还没改
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
            JSONObject single2 = menjin.scopeList(scopeID,"1");
            int code2 = single2.getInteger("code");
            System.out.println("2");
            Preconditions.checkArgument(code2==1000,"使用层级ID"+scopeID+"搜索失败，当前状态码为" + code2 );
            String search_scopeID = single2.getJSONObject("data").getJSONArray("list").getJSONObject(0).getString("scope");
            String search_scopeName = single2.getJSONObject("data").getJSONArray("list").getJSONObject(0).getString("scope_name");
            Preconditions.checkArgument(search_scopeID.equals(scopeID) && search_scopeName.equals(scopename),"添加层级时，层级名称=" + scopename+"使用id进行搜索，结果中层级名称=" + search_scopeName);

            //删掉层级
            int code3 = menjin.scopeDelete(scopeID,"1").getInteger("code");
            Preconditions.checkArgument(code3==1000,"删除层级"+scopeID+"失败，当前状态码为" + code3 );

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

            //再删除两个层级
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
    public void addscopeTwoNoParent() {
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
            Preconditions.checkArgument(code==1,"应添加失败，当前状态码为" + code + "提示语为： " + message);

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
    public void authListNotNullChk() {
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
            JSONObject single2 = menjin.scopeList(scopeID,"2");
            System.out.println(single2);
            int code2 = single2.getInteger("code");

            Preconditions.checkArgument(code2==1000,"使用层级ID"+scopeID+"搜索失败，当前状态码为" + code2);
            JSONArray list = single2.getJSONObject("data").getJSONArray("list");
            Preconditions.checkArgument(list.size()>0,"搜索层级"+scopeID+"结果为空");
            String search_scopeID = single2.getJSONObject("data").getJSONArray("list").getJSONObject(0).getString("scope");
            String search_scopeName = single2.getJSONObject("data").getJSONArray("list").getJSONObject(0).getString("scope_name");
            Preconditions.checkArgument(search_scopeID.equals(scopeID) && search_scopeName.equals(scopename),"添加层级时，层级名称=" + scopename+"使用id进行搜索，结果中层级名称=" + search_scopeName);

            //删除无设备层级
            //JSONObject single3 = menjin.scopeDelete(scopeID,"2");
            menjin.scopeDelete(parentid,"1");
            //int code3 = single3.getInteger("code");

            //Preconditions.checkArgument(code3==1000,"删除无设备层级"+scopeID+"失败，当前状态码为" + code3 );




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
     * 查看层级，层级id与类型不对应, --失败？无结果             列表
     */
    @Test
    public void listDiffIdAndType() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：查看层级时，层级id与类型不对应\n";

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
            JSONObject single2 = menjin.scopeList(scopeID,"2");
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
    public void identifyNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：删除不存在的层级\n";

        String key = "";

        try {
            JSONObject single = menjin.scopeDelete("1234567800000000","2");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1000,"删除失败，当前状态码为" + code + "提示语为： " + message);

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
    public void adddeviceNotInShopping() {
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
            Preconditions.checkArgument(code2==1000,"创建设备失败，当前状态码为" + code2 + "提示语为： " + message2);




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
     *查看全部创建过的设备
     */
    @Test
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
            Preconditions.checkArgument(code==1000,"查询失败，当前状态码为" + code + "提示语为： " + message);


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

    /**
     *删除层级时，该层级下的设备全部停止 13693
     */
    @Test
    public void deletescopeWithAllDisable() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：设备全部停止时删除层级\n";

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

            //在该层级下创建设备2
            String devicename3 = "devicetwo" + System.currentTimeMillis();
            JSONObject single3 = menjin.deviceAdd(scopeID,devicename3);
            String deviceID3 = single3.getJSONObject("data").getString("device_id");
            System.out.println("设备2 id   " + deviceID3);

            //停止两个设备
            menjin.operateDevice(deviceID,"DISABLE");
            menjin.operateDevice(deviceID3,"DISABLE");

            //查询状态
            menjin.scopeList(scopeID,"2");

            //删除层级
            JSONObject single4 = menjin.scopeDelete(scopeID,"2");
            int code = single4.getInteger("code");
            String message = single4.getString("message");
            Preconditions.checkArgument(code==1000,"删除层级" + scopeID + "失败，状态码" + code + " , 提示语为" + message);

            //删除后，使用层级id进行搜索
            JSONObject single5 = menjin.scopeList(scopeID,"2");
            int code2 = single5.getInteger("code");

            Preconditions.checkArgument(code2==1000,"查询层级" + scopeID + "失败，状态码" + code2);
            JSONArray list = single5.getJSONObject("data").getJSONArray("list");
            Preconditions.checkArgument(list.size() ==0 ,"层级"+scopeID+"删除后在搜索有结果");



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
     *删除层级时，该层级下有运行中的设备 13687
     */
    @Test
    public void deletescopeWithEnable() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：有运行设备时删除层级\n";

        String key = "";

        try {

            String scopeID = "13687";
            //在该层级下创建设备1
            String devicename = "deviceone" + System.currentTimeMillis();
            JSONObject single2 = menjin.deviceAdd(scopeID,devicename);
            String deviceID = single2.getJSONObject("data").getString("device_id");
            System.out.println("设备1 id   " + deviceID);

            //在该层级下创建设备2
            String devicename3 = "devicetwo" + System.currentTimeMillis();
            JSONObject single3 = menjin.deviceAdd(scopeID,devicename3);
            String deviceID3 = single3.getJSONObject("data").getString("device_id");
            System.out.println("设备2 id   " + deviceID3);

            //停止一个设备
            menjin.operateDevice(deviceID,"DISABLE");
            //启用一个设备
            menjin.operateDevice(deviceID3,"ENABLE");

            //删除层级
            JSONObject single4 = menjin.scopeDelete(scopeID,"2");
            int code = single4.getInteger("code");
            String message = single4.getString("message");
            Preconditions.checkArgument(code==1,"删除层级" + scopeID + "应失败，状态码" + code + " , 提示语为" + message);


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
     *仅查询存在的用户，填写全部必填项 user1586858767046
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
            String user_id = "user1586858767046";
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
            //JSONObject single2 = menjin.userInfo("4116","user1586499210988");

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
            System.out.println("人物注册"+single);



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
     *注册用户，填写非必填项，不存在的user_id，一张人脸base64 4116
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
     *注册用户，人脸分辨率较低base64 4116 1001 人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG),请更换图片
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
            Preconditions.checkArgument(message.equals("人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG),请更换图片"),"提示："+ message);

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
     *注册用户，非人脸（猫脸）base64 4116 1001 人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG),请更换图片
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
            Preconditions.checkArgument(message.equals("人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG),请更换图片"),"提示："+ message);

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
     *注册用户，风景图 base64 4116 1001 人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG),请更换图片
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
            Preconditions.checkArgument(message.equals("人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG),请更换图片"),"提示："+ message);

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
     *注册用户，单人遮挡 base64 4116 --应失败
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
            Preconditions.checkArgument(code==1001,"创建用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);
            Preconditions.checkArgument(message.equals("人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG),请更换图片"),"提示："+ message);

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
            JSONObject single3 = menjin.userUpdate(scope,user_id,image_type,newface,new_card);
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
     *更新已存在用户 cardkey
     * //传image，没有图片
     */
    @Test
    public void userUpdateCardKey() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：更新用户cardKey\n";

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
            JSONObject single3 = menjin.userUpdate(scope,user_id,image_type,face_image,new_card);
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
            //String face2 = single2.getJSONObject("data").getJSONArray("face_list").getJSONObject(0).getString("face_url");

            Preconditions.checkArgument(search_card_key.equals(new_card),"CardKey更新失败");
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
            JSONObject single3 = menjin.userUpdate(scope,user_id,image_type,newface,"");
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
     *更新已存在用户 cardkey+图片都没有 BUG1634
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
            JSONObject single3 = menjin.userUpdate(scope,user_id,image_type,"","");
            int code = single3.getInteger("code");

            System.out.println(single3);
            Preconditions.checkArgument(code==1, "应失败，状态码" +code);

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
            JSONObject single3 = menjin.userUpdate(scope,user_id,image_type,face_image,"");
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
            JSONObject single = menjin.userQRCode("",user_id);

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
     *存在的设备id，配置人员权限（时间次数无限制）
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
            //String user_id = "user" + System.currentTimeMillis();
            String user_id = "user1586866205804";

            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,user_id);

            //添加权限
            List<String> device_id = new ArrayList<String>();
            device_id.add("\"7362126091322368\"");// 填入已存在的设备id
            List<String> user_idlist = new ArrayList<String>();
            user_idlist.add("\"user1586866205804\"");// 填入已存在的人物id


            int pass_num = -1;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");

            JSONObject single = menjin.authAdd(device_id,scope,user_idlist,"USER",config);
            int code = single.getInteger("code");

            Preconditions.checkArgument(code==1000,"配置权限失败，状态码" + code);

            System.out.println("123456");

            //查询权限
            JSONObject single2 = menjin.authList(device_id,user_idlist);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1000,"查询权限失败，状态码" + code2 + " , 提示语为" + message2);
            JSONArray list = single2.getJSONObject("data").getJSONArray("list");
            JSONObject list_single = list.getJSONObject(0);
            String search_device_id = list_single.getString("device_id");
            String search_user_id = list_single.getString("user_id");
            JSONObject search_auth_config = list_single.getJSONObject("auth_config");
            String search_pass_num = search_auth_config.getString("pass_num");
            String search_start_time = search_auth_config.getString("start_time");
            String search_end_time = search_auth_config.getString("end_time");
            String auth_type = list_single.getString("auth_type");

            Preconditions.checkArgument(search_device_id.equals(device_id) && search_user_id.equals(user_id),"deviceID和userid创建后在搜索结果不一致");
            Preconditions.checkArgument(search_pass_num.equals(pass_num),"创建时passnum=" + pass_num  + " , 搜索时=" + search_pass_num);
            Preconditions.checkArgument(search_start_time.equals(start_time),"创建时start_time=" + start_time  + " , 搜索时=" + search_start_time);
            Preconditions.checkArgument(search_end_time.equals(end_time),"创建时end_time=" + end_time  + " , 搜索时=" + search_end_time);
            Preconditions.checkArgument(auth_type.equals("人员"),"创建时auth_type=" + "人员"  + " , 搜索时=" + auth_type);


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
     *不存在的设备id，配置人员权限（时间次数无限制）
     */
    @Test
    public void authaddNotExistDevID() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：不存在的设备id，配置人员权限\n";

        String key = "";

        try {
            //添加权限
            List<String> device_id = new ArrayList<String>();
            device_id.add("\"lxq123456789098765\"");// 填入bu存在的设备id
            List<String> user_id = new ArrayList<String>();
            user_id.add("\"user1586858767046\"");// 填入已存在的人物id
            String scope = "4116"; //用户注册的scope
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
            //添加权限
            List<String> device_id = new ArrayList<String>();
            device_id.add("\"7362126091322368\"");// 填入已存在的设备id
            List<String> user_id = new ArrayList<String>();
            user_id.add("\"user1586858767046\"");// 填入已存在的人物id
            String scope =menjin.scopeUser; //用户注册的scope

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
     *存在的权限id，删除权限
     * */
    @Test
    public void authdeleteExistID() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：删除存在的权限id\n";

        String key = "";

        try {
            //添加权限
            List<String> device_id = new ArrayList<String>();
            device_id.add("");// 填入已存在的设备id
            List<String> user_id = new ArrayList<String>();
            user_id.add("");// 填入已存在的人物id
            String scope = "4116"; //用户注册的scope
            int pass_num = -1;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");

            JSONObject single = menjin.authAdd(device_id,scope,user_id,"人员",config);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==0,"配置权限失败，状态码" + code + " , 提示语为" + message);
            String auth_id = single.getJSONObject("data").getString("auth_id");
            //删除通行权限
            JSONObject single2 = menjin.authDelete(auth_id);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1000 && message2.equals("成功"),"删除权限失败，状态码" + code2 + " , 提示语为" + message2);


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
            String auth_id = "lxq12345678"; //不存在的权限id
            JSONObject single = menjin.authDelete(auth_id);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==0,"删除权限失败，状态码" + code + " , 提示语为" + message);


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
            List<String> user_id = new ArrayList<String>();// 人物id为空
            JSONObject single = menjin.authDelete(user_id);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==0,"删除权限失败，状态码" + code + " , 提示语为" + message);


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
/*
            //人物注册
            String scope = menjin.scopeUser; //层级
            String user_id = "用户" + System.currentTimeMillis();
            String image_type = "URL";
            String face_image = "https://thumbnail0.baidupcs.com/thumbnail/a096cb0e3p286ff77a27687d8fa3f6f8?fid=4209926431-250528-714156240075946&time=1586401200&rt=sh&sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-aKKjXIEXnuKAKQdvvJQwl7pKUss%3D&expires=8h&chkv=0&chkbd=0&chkpc=&dp-logid=2306939513356739217&dp-callid=0&size=c710_u400&quality=100&vuk=-&ft=video";
            menjin.userAdd(scope,user_id,image_type,face_image,"","");



 */
            //配置权限

            //添加权限
            List<String> device_id = new ArrayList<String>();
            device_id.add("\"7362126091322368\"");// 填入已存在的设备id
            List<String> user_idlist = new ArrayList<String>();
            user_idlist.add("\"user1586862421727\"");// 填入已存在的人物id

            String scope = menjin.scopeUser;
            int pass_num = -1;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_id,scope,user_idlist,"人员",config);

            //删除通行权限
            List<String> user_id2 = new ArrayList<String>();// 人物id为空
            user_id2.add("\"user1586862421727\"");//添加已存在的userid
            JSONObject single2 = menjin.authDelete(user_id2);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1000,"删除权限失败，状态码" + code2 + " , 提示语为" + message2);


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
     *权限类=USER，user_id不为空\存在\无权限，删除权限
     * */
    @Test
    public void authdeleteExistUserNoAuth() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：删除权限时，权限类=USER，user_id存在、非空、无权限\n";

        String key = "";

        try {

            /*
            //人物注册
            String scope = "4116"; //层级
            String user_id = "用户" + System.currentTimeMillis();
            String image_type = "URL";
            String face_image = "https://thumbnail0.baidupcs.com/thumbnail/a096cb0e3p286ff77a27687d8fa3f6f8?fid=4209926431-250528-714156240075946&time=1586401200&rt=sh&sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-aKKjXIEXnuKAKQdvvJQwl7pKUss%3D&expires=8h&chkv=0&chkbd=0&chkpc=&dp-logid=2306939513356739217&dp-callid=0&size=c710_u400&quality=100&vuk=-&ft=video";
            menjin.userAdd(scope,user_id,image_type,face_image,"","");


             */

            //删除通行权限
            List<String> user_id2 = new ArrayList<String>();// 人物id为空
            user_id2.add("\"user1586862360913\"");//添加已存在的userid
            JSONObject single2 = menjin.authDelete(user_id2);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1000,"删除权限失败，状态码" + code2 + " , 提示语为" + message2);


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
     *权限类=USER，user_id不为空\不存在，删除权限
     * */
    @Test
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
            JSONObject single2 = menjin.authDelete(user_id2);
            int code2 = single2.getInteger("code");

            Preconditions.checkArgument(code2==1000,"删除权限失败，状态码" + code2);


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
            String device_id = "7376096262751232";//设备id
            String scope = menjin.scopeUser;
            //新建人物
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //配置权限

            List<String> device_idlist = new ArrayList<String>();
            device_idlist.add("\"7376096262751232\"");// 填入已存在的设备id
            List<String> user_idlist = new ArrayList<String>();
            user_idlist.add("\"" + user_id + "\"");// 填入已存在的人物id


            int pass_num = -1;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");

            menjin.authAdd(device_idlist,scope,user_idlist,"USER",config);

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
            List<String> device_idlist = new ArrayList<String>();
            device_idlist.add("\"" + device_id + "\"");// 填入已存在的设备id
            List<String> user_idlist = new ArrayList<String>();
            user_idlist.add("\"" + user_id+ "\"");// 填入已存在的人物id

            int pass_num = -1;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_idlist,scope,user_idlist,"USER",config);

            System.out.println("0");
            //获取二维码
            JSONObject QRdata = menjin.userQRCode(scope,user_id);
            String qrurl = QRdata.getString("qr_image_url");

            System.out.println("1");

            //边缘端识别
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrurl);
            int code = single.getInteger("code");

            System.out.println("2");
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
            String device_id = "";//设备id
            String scope = "4116";

            String qrurl = "1234567890";

            //边缘端识别
            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrurl);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==0,"识别失败，状态码" + code + " , 提示语为" + message);



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
            String device_id = "";//设备id
            String scope = "4116";
            //新建人物
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "URL";
            String face_image = menjin.lxq;
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");

            //配置权限
            List<String> device_idlist = new ArrayList<String>();
            device_idlist.add("\""+device_id+"\"");// 填入已存在的设备id
            List<String> user_idlist = new ArrayList<String>();
            user_idlist.add("\""+user_id+"\"");// 填入已存在的人物id


            int pass_num = -1;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_idlist,scope,user_idlist,"USER",config);


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
            String device_id = "";//设备id
            String card_id = "1234567890";
            //边缘端识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",card_id);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1000 && message.equals("成功"),"识别失败，状态码" + code + " , 提示语为" + message);
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
//--------人脸-------
    /**
     *身份认证通过-人设备已绑定-通行时间内/通行次数限制内 （人脸）
     *最后 不取消绑定的设备 直接删除人物
     */
    @Test
    public void Face_1a() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：刷脸-身份认证通过-人设备已绑定-无通行时间/通行次数限制\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = "4116";
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = "";// 填入已存在的设备id---------------
            List<String> device_idlist = new ArrayList<String>();
            device_idlist.add(device_id);
            List<String> user_idlist = new ArrayList<String>();
            user_idlist.add(user_id);// 填入已存在的人物id

            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = menjin.todayEndLong();
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_idlist,scope,user_idlist,"人员",config);

            Long recordstart = System.currentTimeMillis(); //记录开始时间

            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");
            Thread.sleep(200);
            Long recordend = System.currentTimeMillis(); //记录结束时间
            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(0);
                Long time = single2.getLong("time");
                //没写完！！
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

    /**
     *身份认证通过-人设备已绑定-无通行时间/通行次数限制 （人脸）
     *最后 不取消绑定的设备 直接删除人物
     */
    @Test
    public void Face_1i() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：刷脸-身份认证通过-人设备已绑定-无通行时间/通行次数限制\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = "4116";
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = "";// 填入已存在的设备id---------------
            List<String> device_idlist = new ArrayList<String>();
            device_idlist.add(device_id);
            List<String> user_idlist = new ArrayList<String>();
            user_idlist.add(user_id);// 填入已存在的人物id

            int pass_num = -1;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_idlist,scope,user_idlist,"人员",config);

            Long recordstart = System.currentTimeMillis(); //记录开始时间

            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");
            Thread.sleep(200);
            Long recordend = System.currentTimeMillis(); //记录结束时间
            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(0);
                Long time = single2.getLong("time");
                //没写完！！
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



//--------刷卡-------
    /**
     *身份认证通过-人设备已绑定-通行时间内/通行次数限制内 （刷卡）
     *最后 不取消绑定的设备 直接删除人物
     */
    @Test
    public void Card_1a() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：刷卡-身份认证通过-人设备已绑定-通行时间内/通行次数限制内\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = "4116";
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = "";// 填入已存在的设备id---------------
            List<String> device_idlist = new ArrayList<String>();
            device_idlist.add(device_id);
            List<String> user_idlist = new ArrayList<String>();
            user_idlist.add(user_id);// 填入已存在的人物id


            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = menjin.todayEndLong();
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_idlist,scope,user_idlist,"人员",config);

            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //人脸识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");
            Thread.sleep(200);
            Long recordend = System.currentTimeMillis(); //记录结束时间
            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(0);
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

    /**
     *身份认证通过-人设备已绑定-无通行时间/通行次数限制 （刷卡）
     *最后 不取消绑定的设备 直接删除人物
     */
    @Test
    public void Card_1i() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：刷卡-身份认证通过-人设备已绑定-无通行时间/通行次数限制\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = "4116";
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");
            //配置通行权限

            String device_id = "";// 填入已存在的设备id---------------
            List<String> device_idlist = new ArrayList<String>();
            device_idlist.add(device_id);
            List<String> user_idlist = new ArrayList<String>();
            user_idlist.add(user_id);// 填入已存在的人物id

            int pass_num = -1;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_idlist,scope,user_idlist,"人员",config);

            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //人脸识别
            JSONObject  single = menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");
            Thread.sleep(200);
            Long recordend = System.currentTimeMillis(); //记录结束时间
            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(0);
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
    /**
     *身份认证通过-人设备已绑定-通行时间内/通行次数限制内 （二维码）
     *最后 不取消绑定的设备 直接删除人物
     */
    @Test
    public void QRcode_1a() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：二维码-身份认证通过-人设备已绑定-通行时间内/通行次数限制内\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = "4116";
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = "";// 填入已存在的设备id---------------
            List<String> device_idlist = new ArrayList<String>();
            device_idlist.add(device_id);
            List<String> user_idlist = new ArrayList<String>();
            user_idlist.add(user_id);// 填入已存在的人物id

            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = menjin.todayEndLong();
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");
            menjin.authAdd(device_idlist,scope,user_idlist,"人员",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getJSONObject("data").getString("qr_image_url");

            Long recordstart = System.currentTimeMillis(); //记录开始时间

            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");
            Thread.sleep(200);
            Long recordend = System.currentTimeMillis(); //记录结束时间
            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(0);
                Long time = single2.getLong("time");
                //没写完！！
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

    /**
     *身份认证通过-人设备已绑定-无通行时间/通行次数限制 （二维码）
     *最后 不取消绑定的设备 直接删除人物
     */
    @Test
    public void QRcode_1i() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：二维码-身份认证通过-人设备已绑定-无通行时间/通行次数限制\n";

        String key = "";

        try {

            //注册人物，单一人脸
            String scope = "4116";
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //配置通行权限

            String device_id = "";// 填入已存在的设备id---------------
            List<String> device_idlist = new ArrayList<String>();
            device_idlist.add(device_id);
            List<String> user_idlist = new ArrayList<String>();
            user_idlist.add(user_id);// 填入已存在的人物id
            int pass_num = -1;
            Long start_time = -1L;
            Long end_time = -1L;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(device_idlist,scope,user_idlist,"人员",config);

            //获取二维码
            String qrcode = menjin.userQRCode(scope,user_id).getJSONObject("data").getString("qr_image_url");

            Long recordstart = System.currentTimeMillis(); //记录开始时间

            JSONObject  single = menjin.edgeidentify(device_id,"QR_CODE",qrcode);
            JSONObject data = single.getJSONObject("data");
            String has_auth = data.getString("has_auth");
            System.out.println(data);
            Preconditions.checkArgument(has_auth.equals("true"),"用户"+user_id+"无权限");
            Thread.sleep(200);
            Long recordend = System.currentTimeMillis(); //记录结束时间
            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < recordlist.size(); i ++){
                JSONObject single2 = recordlist.getJSONObject(0);
                Long time = single2.getLong("time");
                //没写完！！
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

//    ---------------------------------------------------通用方法--------------------------------------------------------------

    private void saveData(Case aCase, String ciCaseName, String caseName, String caseDescription) {
        //setBasicParaToDB(aCase, ciCaseName, caseName, caseDescription);
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

       public static void main(String[] args) throws Exception {// ---不用理我！
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
           //新建人物
           String user_id = "user" + System.currentTimeMillis();
           String image_type = "URL";


           //配置权限
           List<String> list = new ArrayList<String>();
           list.add("\\user_id\\");// 填入已存在的设备id
           System.out.println(list);

    }




}