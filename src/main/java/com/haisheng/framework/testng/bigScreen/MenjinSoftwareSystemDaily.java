
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


    @AfterClass //还没改
    public void clean() {
        qaDbUtil.closeConnection();
        dingPushFinal();
    }

    @BeforeMethod //还没改
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }



    /**
     * 添加层级1 不指定父层级id
     * 添加成功后，搜索结果与添加时保持一致
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
            String scopename = "层级" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"品牌客户","");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code!=1000,"添加失败，当前状态码为" + code + "提示语为： " + message);
            String scopeID = single.getJSONObject("data").getString("scope");
            Preconditions.checkArgument(!scopeID.equals(""),"返回的层级id为" + scopeID);
            //使用层级id进行搜索，有结果
            JSONObject single2 = menjin.scopeList(scopeID,"品牌客户");
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1000 && message2.equals("成功"),"使用层级ID"+scopeID+"搜索失败，当前状态码为" + code2 + "提示语为： " + message2);
            String search_scopeID = single2.getJSONObject("data").getJSONArray("list").getJSONObject(0).getString("scope");
            String search_scopeName = single2.getJSONObject("data").getJSONArray("list").getJSONObject(0).getString("scope_name");
            Preconditions.checkArgument(search_scopeID.equals(scopeID) && search_scopeName.equals(scopename),"添加层级时，层级名称=" + scopename+"使用id进行搜索，结果中层级名称=" + search_scopeName);

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
            //添加层级
            String scopename = "层级" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"品牌客户","");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code!=1000,"添加失败，当前状态码为" + code + "提示语为： " + message);

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
            String scopename = "层级" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"购物中心","");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1000,"添加失败，当前状态码为" + code + "提示语为： " + message);

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
     * 添加层级2，指定父层级id 4116
     */
    @Test
    public void authListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：添加层级2时指定父层级id，再删除\n";

        String key = "";

        try {
            //添加层级
            String scopename = "层级" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"购物中心","4116");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code!=1000,"添加失败，当前状态码为" + code + "提示语为： " + message);
            String scopeID = single.getJSONObject("data").getString("scope");
            Preconditions.checkArgument(!scopeID.equals(""),"返回的层级id为" + scopeID);

            //使用层级id进行搜索，有结果
            JSONObject single2 = menjin.scopeList(scopeID,"购物中心");
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1000 && message2.equals("成功"),"使用层级ID"+scopeID+"搜索失败，当前状态码为" + code2 + "提示语为： " + message2);
            String search_scopeID = single2.getJSONObject("data").getJSONArray("list").getJSONObject(0).getString("scope");
            String search_scopeName = single2.getJSONObject("data").getJSONArray("list").getJSONObject(0).getString("scope_name");
            Preconditions.checkArgument(search_scopeID.equals(scopeID) && search_scopeName.equals(scopename),"添加层级时，层级名称=" + scopename+"使用id进行搜索，结果中层级名称=" + search_scopeName);

            //删除无设备层级
            JSONObject single3 = menjin.scopeDelete(scopeID,"购物中心");
            int code3 = single3.getInteger("code");
            String message3 = single3.getString("message");
            Preconditions.checkArgument(code3==1000 && message3.equals("成功"),"删除无设备层级"+scopeID+"失败，当前状态码为" + code3 + "提示语为： " + message3);


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
     * 查看层级，层级id与类型不对应, --失败？无结果
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
            String scopename = "层级" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"品牌客户","");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code!=1000,"添加失败，当前状态码为" + code + "提示语为： " + message);
            String scopeID = single.getJSONObject("data").getString("scope");
            Preconditions.checkArgument(!scopeID.equals(""),"返回的层级id为" + scopeID);
            //查看层级列表
            JSONObject single2 = menjin.scopeList(scopeID,"门店");
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1000 && message2.equals("成功"),"使用层级ID"+scopeID+"搜索失败，当前状态码为" + code2 + "提示语为： " + message2);
            JSONArray list = single2.getJSONObject("data").getJSONArray("list");
            Preconditions.checkArgument(list.size() == 0, "有搜索记录");


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
            JSONObject single = menjin.scopeDelete("lxq12345678","项目");
            int code = single.getInteger("code");
            String message = single.getString("meaasge");
            Preconditions.checkArgument(code!=1000,"删除失败，当前状态码为" + code + "提示语为： " + message);

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
     * 新建购物中心级别设备 4116
     */
    @Test
    public void adddeviceInShopping() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：在购物中心级别下创建设备 再搜索\n";

        String key = "";

        try {
            //先创建一个购物中心级别的层级
            String scopename = "层级" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"购物中心","4116");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code!=1000,"层级添加失败，当前状态码为" + code + "提示语为： " + message);
            String scopeID = single.getJSONObject("data").getString("scope");
            System.out.println("层级id   " + scopeID);
            Preconditions.checkArgument(!scopeID.equals(""),"返回的层级id为" + scopeID);

            //在该层级下创建设备
            String devicename = "设备" + System.currentTimeMillis();
            JSONObject single2 = menjin.deviceAdd(scopeID,devicename);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1000 && message2.equals("成功"),"创建设备失败，当前状态码为" + code2 + "提示语为： " + message2);
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

            JSONArray devicelist = menjin.deviceList(scopeID).getJSONObject("data").getJSONArray("device_list");
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
            String scopename = "层级" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"品牌客户","");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code!=1000,"层级添加失败，当前状态码为" + code + "提示语为： " + message);
            String scopeID = single.getJSONObject("data").getString("scope");
            System.out.println("层级id   " + scopeID);
            Preconditions.checkArgument(!scopeID.equals(""),"返回的层级id为" + scopeID);

            //在该层级下创建设备
            String devicename = "设备" + System.currentTimeMillis();
            JSONObject single2 = menjin.deviceAdd(scopeID,devicename);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1000 && message2.equals("成功"),"创建设备失败，当前状态码为" + code2 + "提示语为： " + message2);
            String deviceID = single2.getJSONObject("data").getString("device_id");
            System.out.println("设备id   " + deviceID);
            Preconditions.checkArgument(!deviceID.equals(""),"返回的设备id为" + deviceID);

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
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1000 && message.equals("成功"),"查询失败，当前状态码为" + code + "提示语为： " + message);

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
            JSONObject single = menjin.deviceList("lxq123456789");
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
     * 改变设备状态
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
            String scopename = "层级" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"购物中心","4116");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code!=1000,"添加失败，当前状态码为" + code + "提示语为： " + message);
            String scopeID = single.getJSONObject("data").getString("scope");

            //层级下创建设备
            String devicename = "设备" + System.currentTimeMillis();
            JSONObject single2 = menjin.deviceAdd(scopeID,devicename);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1000 && message2.equals("成功"),"创建设备失败，当前状态码为" + code2 + "提示语为： " + message2);
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
            String message3 = single3.getString("message");
            Preconditions.checkArgument(code3==1000 && message3.equals("成功"),"设备启用失败，当前状态码为" + code3 + "提示语为： " + message3);

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
            String message5 = single5.getString("message");
            Preconditions.checkArgument(code5==1000 && message5.equals("成功"),"设备启用后再次启用失败，当前状态码为" + code5 + "提示语为： " + message5);

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
            String message4 = single4.getString("message");
            Preconditions.checkArgument(code4==1000 && message4.equals("成功"),"设备停止失败，当前状态码为" + code4 + "提示语为： " + message4);

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
            String message6 = single6.getString("message");
            Preconditions.checkArgument(code6==1000 && message6.equals("成功"),"设备停止后再次停止失败，当前状态码为" + code6 + "提示语为： " + message6);

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
            String message7 = single7.getString("message");
            Preconditions.checkArgument(code7==1000 && message7.equals("成功"),"设备停止后启用失败，当前状态码为" + code7 + "提示语为： " + message7);

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
     *删除层级时，该层级下的设备全部停止
     */
    @Test
    public void deletescopeWithAllDisable() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：设备全部停止时删除层级\n";

        String key = "";

        try {
            String scopename = "层级" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"购物中心","4116");
            String scopeID = single.getJSONObject("data").getString("scope");
            System.out.println("层级id   " + scopeID);

            //在该层级下创建设备1
            String devicename = "设备one" + System.currentTimeMillis();
            JSONObject single2 = menjin.deviceAdd(scopeID,devicename);
            String deviceID = single2.getJSONObject("data").getString("device_id");
            System.out.println("设备1 id   " + deviceID);

            //在该层级下创建设备2
            String devicename3 = "设备two" + System.currentTimeMillis();
            JSONObject single3 = menjin.deviceAdd(scopeID,devicename3);
            String deviceID3 = single3.getJSONObject("data").getString("device_id");
            System.out.println("设备1 id   " + deviceID3);

            //停止两个设备
            menjin.operateDevice(deviceID,"DISABLE");
            menjin.operateDevice(deviceID3,"DISABLE");

            //删除层级
            JSONObject single4 = menjin.scopeDelete(scopeID,"购物中心");
            int code = single4.getInteger("code");
            String message = single4.getString("message");
            Preconditions.checkArgument(code==1000 && message.equals("成功"),"删除层级" + scopeID + "失败，状态码" + code + " , 提示语为" + message);

            //删除后，使用层级id进行搜索
            JSONObject single5 = menjin.scopeDelete(scopeID,"购物中心");
            int code2 = single5.getInteger("code");
            String message2 = single4.getString("message");
            Preconditions.checkArgument(code2==1000 && message2.equals("成功"),"查询层级" + scopeID + "失败，状态码" + code2 + " , 提示语为" + message2);
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
     *注册用户，填写全部必填项，不存在的user_id，一张人脸base64 4116
     */
    @Test
    public void useraddUniqueId() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：注册用户填写全部必填项，不存在的user_id，人物图片为一张人脸base64\n";

        String key = "";

        try {
            //人物注册
            String scope = "4116";
            String user_id = "用户" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            JSONObject single = menjin.userAdd(scope,user_id,image_type,face_image,"","");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1000 && message.equals("成功"),"创建用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);

            //人物查询
            JSONObject single2 = menjin.userInfo(scope,user_id);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1000 && message2.equals("成功"),"查询用户" + user_id + "失败，状态码" + code2 + " , 提示语为" + message2);
            JSONObject data = single2.getJSONObject("data");
            String search_user_id = data.getString("user_id");
            String search_qr_iamge_url = data.getString("qr_iamge_url");
            String search_card_key = data.getString("card_key");
            JSONArray search_face_list = data.getJSONArray("face_list"); //为啥是list？
            boolean hasfaceurl = false;
            for (int i = 0; i < search_face_list.size() ; i++){
                JSONObject single3 = search_face_list.getJSONObject(i);
                String face_url = single3.getString("face_url");
                System.out.println("faceurl:  " + face_url);
                if (face_url.equals(face_image)){
                    hasfaceurl = true;
                    break;
                }
            }
            Preconditions.checkArgument(search_user_id.equals(user_id),"注册时user_id=" + user_id + " ， 根据id查询时展示user_id = " + search_user_id);
            Preconditions.checkArgument(!search_qr_iamge_url.equals(""),"搜索时二维码为空");
            Preconditions.checkArgument(search_card_key.equals(""),"cardkey注册时未填写，搜索时展示为" + search_card_key);
            Preconditions.checkArgument(hasfaceurl == true, "注册时的人脸，不在face_list中");


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
            String scope = "4116";
            String user_id = "用户" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            JSONObject single = menjin.userAdd(scope,user_id,image_type,face_image,user_id,user_id);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1000 && message.equals("成功"),"创建用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);

            //人物查询
            JSONObject single2 = menjin.userInfo(scope,user_id);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1000 && message2.equals("成功"),"查询用户" + user_id + "失败，状态码" + code2 + " , 提示语为" + message2);
            JSONObject data = single2.getJSONObject("data");
            String search_user_id = data.getString("user_id");
            String search_qr_iamge_url = data.getString("qr_iamge_url");
            String search_card_key = data.getString("card_key");
            JSONArray search_face_list = data.getJSONArray("face_list"); //为啥是list？
            boolean hasfaceurl = false;
            for (int i = 0; i < search_face_list.size() ; i++){
                JSONObject single3 = search_face_list.getJSONObject(i);
                String face_url = single3.getString("face_url");
                System.out.println("faceurl:  " + face_url);
                if (face_url.equals(face_image)){
                    hasfaceurl = true;
                    break;
                }
            }
            Preconditions.checkArgument(search_user_id.equals(user_id),"注册时user_id=" + user_id + " ， 根据id查询时展示user_id = " + search_user_id);
            Preconditions.checkArgument(!search_qr_iamge_url.equals(""),"搜索时二维码为空");
            Preconditions.checkArgument(search_card_key.equals(user_id),"cardkey注册时为"+user_id+"，搜索时展示为" + search_card_key);
            Preconditions.checkArgument(hasfaceurl == true, "注册时的人脸，不在face_list中");

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
     *使用已存在的userid注册，一个有cardkey，一个没有，一张人脸base64 4116 --应失败
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
            String scope = "4116";
            String user_id = "用户" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            JSONObject single = menjin.userAdd(scope,user_id,image_type,face_image,"","");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1000 && message.equals("成功"),"创建用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);

            //第二个人物注册

            String face_image2 = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/廖祥茹.jpg");
            JSONObject single2 = menjin.userAdd(scope,user_id,image_type,face_image2,user_id,"祥茹");
            int code2 = single.getInteger("code");
            String message2 = single.getString("message");
            Preconditions.checkArgument(code2==0,"使用已存在的user_id" + user_id + "创建用户失败，状态码" + code2 + " , 提示语为" + message2);


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
     *注册用户，人脸分辨率较低base64 4116 --应失败
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
            String scope = "4116";
            String user_id = "用户" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/分辨率较低.png");
            JSONObject single = menjin.userAdd(scope,user_id,image_type,face_image,"","");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==0,"使用分辨率较低照片创建用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);

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
     *注册用户，非人脸（猫脸）base64 4116 --应失败
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
            String scope = "4116";
            String user_id = "用户" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/猫.png");
            JSONObject single = menjin.userAdd(scope,user_id,image_type,face_image,"","");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==0,"使用分辨率较低照片创建用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);

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
     *注册用户，风景图 base64 4116 --应失败
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
            String scope = "4116";
            String user_id = "用户" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/风景.png");
            JSONObject single = menjin.userAdd(scope,user_id,image_type,face_image,"","");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==0,"使用分辨率较低照片创建用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);

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
            String scope = "4116";
            String user_id = "用户" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/单人遮挡.png");
            JSONObject single = menjin.userAdd(scope,user_id,image_type,face_image,"","");
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==0,"使用分辨率较低照片创建用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);

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
            String scope = "4116";
            String user_id = "用户" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/李婷婷.jpg");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");
            //删除
            JSONObject single = menjin.userDelete(scope,user_id);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1000 && message.equals("成功"),"删除已存在用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);
            //再查询 应无结果
            JSONObject single2 = menjin.userInfo(scope,user_id);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1000 && message2.equals("成功"),"删除后再查询用户" + user_id + "失败，状态码" + code2 + " , 提示语为" + message2);
            JSONObject data = single2.getJSONObject("data");
            System.out.println(data);
            Preconditions.checkArgument(data.equals(""),"人物删除后有结果");

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
     *删除不存在的用户id
     */
    @Test
    public void userdeleteNotExist() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：删除已存在的用户id\n";

        String key = "";

        try {
            String scope = "4116";
            String user_id = "lxq12345678";
            //查询不存在的id
            JSONObject single = menjin.userDelete(scope,user_id);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==0,"删除不存在用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);


            //删除
            JSONObject single2 = menjin.userDelete(scope,user_id);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==0,"删除不存在用户" + user_id + "失败，状态码" + code2 + " , 提示语为" + message2);

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
            //添加权限
            List<String> device_id = new ArrayList<String>();
            device_id.add("");// 填入已存在的设备id
            List<String> user_id = new ArrayList<String>();
            user_id.add("");// 填入已存在的人物id
            String scope = "4116"; //用户注册的scope
            String pass_num = "-1";
            String start_time = "-1";
            String end_time = "-1";
            String config = "{\n" +
                    "   \"pass_num\":\"" + pass_num + "\",\n" +  //通行次数, 若为-1则无次数限制
                    "   \"start_time\":\"" + start_time + "\",\n"  + //Long型时间戳, 通行时间限制. 若为-1则无时间限制
                    "   \"end_time\":\"" + end_time + "\"\n" + //Long型时间戳, 通行时间限制. 若为-1则无时间限制
                    "            }";
            JSONObject auth_config = JSON.parseObject(config);
            JSONObject single = menjin.authAdd(device_id,scope,user_id,"人员",auth_config);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1000 && message.equals("成功"),"配置权限失败，状态码" + code + " , 提示语为" + message);

            //查询权限
            JSONObject single2 = menjin.authList(device_id,user_id);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==1000 && message2.equals("成功"),"查询权限失败，状态码" + code2 + " , 提示语为" + message2);
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
            device_id.add("lxq123456789098765");// 填入bu存在的设备id
            List<String> user_id = new ArrayList<String>();
            user_id.add("");// 填入已存在的人物id
            String scope = "4116"; //用户注册的scope
            String pass_num = "-1";
            String start_time = "-1";
            String end_time = "-1";
            String config = "{\n" +
                    "   \"pass_num\":\"" + pass_num + "\",\n" +  //通行次数, 若为-1则无次数限制
                    "   \"start_time\":\"" + start_time + "\",\n"  + //Long型时间戳, 通行时间限制. 若为-1则无时间限制
                    "   \"end_time\":\"" + end_time + "\"\n" + //Long型时间戳, 通行时间限制. 若为-1则无时间限制
                    "            }";
            JSONObject auth_config = JSON.parseObject(config);
            JSONObject single = menjin.authAdd(device_id,scope,user_id,"人员",auth_config);
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
            device_id.add("");// 填入已存在的设备id
            List<String> user_id = new ArrayList<String>();
            user_id.add("");// 填入已存在的人物id
            String scope = "4116"; //用户注册的scope
            String pass_num = "-1";
            String start_time = "-1";
            String end_time = "-1";
            String config = "";
            JSONObject auth_config = JSON.parseObject(config);
            JSONObject single = menjin.authAdd(device_id,scope,user_id,"人员",auth_config);
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
            String pass_num = "-1";
            String start_time = "-1";
            String end_time = "-1";
            String config = "";
            JSONObject auth_config = JSON.parseObject(config);
            JSONObject single = menjin.authAdd(device_id,scope,user_id,"人员",auth_config);
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
     *权限类=USER，user_id不为空\存在\有权限，删除权限 4116
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
            String scope = "4116"; //层级
            String user_id = "用户" + System.currentTimeMillis();
            String image_type = "URL";
            String face_image = "https://thumbnail0.baidupcs.com/thumbnail/a096cb0e3p286ff77a27687d8fa3f6f8?fid=4209926431-250528-714156240075946&time=1586401200&rt=sh&sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-aKKjXIEXnuKAKQdvvJQwl7pKUss%3D&expires=8h&chkv=0&chkbd=0&chkpc=&dp-logid=2306939513356739217&dp-callid=0&size=c710_u400&quality=100&vuk=-&ft=video";
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //配置权限
            List<String> device_id = new ArrayList<String>();
            device_id.add("");// 填入已存在的设备id
            List<String> user_idlist = new ArrayList<String>();
            user_idlist.add(user_id);// 填入已存在的人物id
            String pass_num = "-1";
            String start_time = "-1";
            String end_time = "-1";
            String config = "{\n" +
                    "   \"pass_num\":\"" + pass_num + "\",\n" +  //通行次数, 若为-1则无次数限制
                    "   \"start_time\":\"" + start_time + "\",\n"  + //Long型时间戳, 通行时间限制. 若为-1则无时间限制
                    "   \"end_time\":\"" + end_time + "\"\n" + //Long型时间戳, 通行时间限制. 若为-1则无时间限制
                    "            }";
            JSONObject auth_config = JSON.parseObject(config);
            menjin.authAdd(device_id,scope,user_idlist,"人员",auth_config);

            //删除通行权限
            List<String> user_id2 = new ArrayList<String>();// 人物id为空
            user_id2.add(user_id);//添加已存在的userid
            JSONObject single2 = menjin.authDelete(user_id2);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==0,"删除权限失败，状态码" + code2 + " , 提示语为" + message2);


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
     *权限类=USER，user_id不为空\存在\无权限，删除权限 4116
     * */
    @Test
    public void authdeleteExistUserNoAuth() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：删除权限时，权限类=USER，user_id存在、非空、无权限\n";

        String key = "";

        try {

            //人物注册
            String scope = "4116"; //层级
            String user_id = "用户" + System.currentTimeMillis();
            String image_type = "URL";
            String face_image = "https://thumbnail0.baidupcs.com/thumbnail/a096cb0e3p286ff77a27687d8fa3f6f8?fid=4209926431-250528-714156240075946&time=1586401200&rt=sh&sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-aKKjXIEXnuKAKQdvvJQwl7pKUss%3D&expires=8h&chkv=0&chkbd=0&chkpc=&dp-logid=2306939513356739217&dp-callid=0&size=c710_u400&quality=100&vuk=-&ft=video";
            menjin.userAdd(scope,user_id,image_type,face_image,"","");


            //删除通行权限
            List<String> user_id2 = new ArrayList<String>();// 人物id为空
            user_id2.add(user_id);//添加已存在的userid
            JSONObject single2 = menjin.authDelete(user_id2);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==0,"删除权限失败，状态码" + code2 + " , 提示语为" + message2);


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
     *权限类=USER，user_id不为空\不存在，删除权限 4116
     * */
    @Test
    public void authdeleteNotExistUserID() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：删除权限时，权限类=USER，user_id不为空但不存在\n";

        String key = "";

        try {


            String scope = "4116"; //层级
            String user_id = "lxq09876543"; //不存在的userid

            //删除通行权限
            List<String> user_id2 = new ArrayList<String>();// 人物id为空
            user_id2.add(user_id);//添加已存在的userid
            JSONObject single2 = menjin.authDelete(user_id2);
            int code2 = single2.getInteger("code");
            String message2 = single2.getString("message");
            Preconditions.checkArgument(code2==0,"删除权限失败，状态码" + code2 + " , 提示语为" + message2);


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
            String device_id = "";//设备id
            String scope = "4116";
            //新建人物
            String user_id = "用户" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"","");

            //边缘端识别
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==1000 && message.equals("成功"),"识别用户" + user_id + "失败，状态码" + code + " , 提示语为" + message);
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
            String device_id = "";//设备id
            String scope = "4116";

            String face_image = "sdfrewasdfrewdr";


            //边缘端识别
            JSONObject  single = menjin.edgeidentify(device_id,"FACE",face_image);
            int code = single.getInteger("code");
            String message = single.getString("message");
            Preconditions.checkArgument(code==0,"识别不存在的base64失败，状态码" + code + " , 提示语为" + message);
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



//    ---------------------------------------------------复用方法--------------------------------------------------------------


//    ---------------------------------------------------通用方法--------------------------------------------------------------

    private void saveData(Case aCase, String ciCaseName, String caseName, String caseDescription) {
        //setBasicParaToDB(aCase, ciCaseName, caseName, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
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
        String path = "src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/分辨率较低.png";
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

    }




}