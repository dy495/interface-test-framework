
package com.haisheng.framework.testng.bigScreen;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.CheckUtil;
import com.haisheng.framework.util.QADbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;


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
            Preconditions.checkArgument(code2==1000 && message2.equals("成功"),"使用层级ID"+scopeID+"搜索失败，当前状态码为" + code + "提示语为： " + message);
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
     * 添加层级2，指定父层级id 4116
     */
    @Test
    public void authListNotNullChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：添加层级2时指定父层级id\n";

        String key = "";

        try {

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
            Preconditions.checkArgument(code2==1000 && message2.equals("成功"),"使用层级ID"+scopeID+"搜索失败，当前状态码为" + code + "提示语为： " + message);
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
     * 查看层级，层级id与类型不对应, --失败
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
            Preconditions.checkArgument(code2==1000 && message2.equals("成功"),"使用层级ID"+scopeID+"搜索失败，当前状态码为" + code + "提示语为： " + message);


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
     * 删除层级，层级下无设备 --失败
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
            //先创建一个购物中心级别的层级
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


//    ---------------------------------------------------复用方法--------------------------------------------------------------

    /**
     *身份认证失败 --刷脸
     */
    private void identify_fail_face() throws Exception {
        String device = "";//存在的设备id
        String type = "FACE";
        String identify_str = "sdfghyjutrewas";//不存在的人脸base64
        int code = menjin.identify(device,type,identify_str).getInteger("code");
        Preconditions.checkArgument(code!=1000,"刷脸认证失败，状态码为"+ code);
    }

    /**
     *身份认证失败 --刷卡
     */
    private void identify_fail_card() throws Exception {
        String device = "";//存在的设备id
        String type = "CARD";
        String identify_str = "sdfghyjutrewas";//不存在的卡号
        int code = menjin.identify(device,type,identify_str).getInteger("code");
        Preconditions.checkArgument(code!=1000,"刷卡认证失败，状态码为"+ code);
    }

    /**
     *身份认证失败 --二维码
     */
    private void identify_fail_QR() throws Exception {
        String device = "";//存在的设备id
        String type = "QR_CODE";
        String identify_str = "sdfghyjutrewas";//不存在的QR_CODE
        int code = menjin.identify(device,type,identify_str).getInteger("code");
        Preconditions.checkArgument(code!=1000,"扫码认证失败，状态码为"+ code);
    }

    /**
     *身份认证成功 -> 任何设备未绑定
     * 层级下新建用户 -> 不配置权限，直接设备+人查询通行权限列表，返回空
     */
    private void peopleWithNoDevice() throws Exception { //写了一半
        String userid = Long.toString(System.currentTimeMillis());
        String face_image = ""; //注测用的人脸

    }

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






}