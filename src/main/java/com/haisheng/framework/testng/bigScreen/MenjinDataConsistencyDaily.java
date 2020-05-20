
package com.haisheng.framework.testng.bigScreen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.codec.binary.Base64;
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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author : lvxueqing
 * @date :  2020/04/26  11:10
 */

public class MenjinDataConsistencyDaily {


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


    @AfterClass
    public void clean() {
        if (DEBUG.equals("true")) {
            return;
        }
        qaDbUtil.closeConnection();
        dingPushFinal();
    }

    @BeforeClass
    public void initial() {
        if (DEBUG.equals("true")) {
            return;
        }
        qaDbUtil.openConnection();
    }

    @BeforeMethod //还没改
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }

//------------------层级------------------------
    /**
     * 添加层级1 搜索结果中有该层级id
     */
    @Test
    public void addscopeOne() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：添加层级1 搜索有结果，层级列表+1\n";

        String key = "";

        try {
            //添加前层级数量
            int before = menjin.scopeList("").getJSONObject("data").getJSONArray("list").size();

            //添加层级
            String scopename = "scope" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"1","");
            String scopeID = single.getJSONObject("data").getString("scope");

            //使用层级id进行搜索，有结果
            JSONArray single2 = menjin.scopeList("").getJSONObject("data").getJSONArray("list");
            int after = single2.size();
            Boolean has1 = false;
            for (int i = 0 ; i < after; i++){
                String search_id = single2.getJSONObject(i).getString("scope");
                if (search_id.equals(scopeID)){
                    has1 = true;
                    break;
                }
            }
            Preconditions.checkArgument(has1==true,"新建后无搜索结果");
            int change = after - before;
            Preconditions.checkArgument(change==1,"新建后层级列表期待+1，实际增肌" + change);

            //删掉层级
            menjin.scopeDelete(scopeID,"1").getInteger("code");

            //再查无结果
            JSONArray single3 = menjin.scopeList("").getJSONObject("data").getJSONArray("list");
            Boolean has2 = false;
            for (int i = 0 ; i < single3.size(); i++){
                String search_id = single3.getJSONObject(i).getString("scope");
                if (search_id.equals(scopeID)){
                    has2 = true;
                    break;
                }
            }
            Preconditions.checkArgument(has2==false,"删除后应无搜索结果");


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
     * 添加层级2 搜索结果中有该层级id
     */
    @Test
    public void addscopeTwo() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：添加层级2 搜索有结果，层级列表+1\n";

        String key = "";

        try {
            //添加前层级数量
            int before = menjin.scopeList(menjin.brand).getJSONObject("data").getJSONArray("list").size();

            //添加层级
            String scopename = "scope" + System.currentTimeMillis();
            JSONObject single = menjin.scopeAdd(scopename,"2",menjin.brand);
            String scopeID = single.getJSONObject("data").getString("scope");

            //使用层级id进行搜索，有结果
            JSONArray single2 = menjin.scopeList(menjin.brand).getJSONObject("data").getJSONArray("list");
            int after = single2.size();
            Boolean has1 = false;
            for (int i = 0 ; i < after; i++){
                String search_id = single2.getJSONObject(i).getString("scope");
                if (search_id.equals(scopeID)){
                    has1 = true;
                    break;
                }
            }
            Preconditions.checkArgument(has1==true,"新建后无搜索结果");
            int change = after - before;
            Preconditions.checkArgument(change==1,"新建后层级列表期待+1，实际增肌" + change);

            //删掉层级
            menjin.scopeDelete(scopeID,"2");

            //再查无结果
            JSONArray single3 = menjin.scopeList(menjin.brand).getJSONObject("data").getJSONArray("list");
            Boolean has2 = false;
            for (int i = 0 ; i < single3.size(); i++){
                String search_id = single3.getJSONObject(i).getString("scope");
                if (search_id.equals(scopeID)){
                    has2 = true;
                    break;
                }
            }
            Preconditions.checkArgument(has2==false,"删除后应无搜索结果");


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
     *注册用户 搜索结果中的姓名/cardID与注册时一致
     */
    @Test
    public void useraddsearch() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：用户注册/更新后，搜索结果中的姓名/cardID与注册/更新后一致\n";

        String key = "";

        try {
            //人物注册

            //人物注册
            String a [] = addUserwithCard();
            String scope = a[0];
            String user_id = a[1];
            String face_image = a[2];

            //人物查询
            JSONObject single = menjin.userInfo(scope,user_id).getJSONObject("data");
            String search_card = single.getString("card_key");
            String search_name = single.getString("user_name");
            Preconditions.checkArgument(search_card.equals(user_id) && search_name.equals("user"),"注册后信息不一致");

            //人物更新
            String new_card = "new" + System.currentTimeMillis();

            menjin.userUpdate(scope,user_id,"BASE64",face_image,new_card,"usertwo");
            //人物查询
            JSONObject single2 = menjin.userInfo(scope,user_id).getJSONObject("data");
            String search_card2 = single2.getString("card_key");
            String search_name2 = single2.getString("user_name");
            Preconditions.checkArgument(search_card2.equals(new_card) && search_name2.equals("usertwo"),"更新后信息不一致");
            //人物删除
            delPeopleScope(scope,user_id);

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
     *删除用户 通行权限被删除 通行记录还在
     */
    @Test
    public void deluserCheckAuth() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：用户删除后，权限列表为空，通行记录不为空\n";

        String key = "";

        try {
            //人物注册
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            String [] a = addScopeUserDevImg(recordstart);
            String scope = a[0];
            String user_id = a[1];
            String device_id = a[2];
            String face_image = a[3];

            //创建设备
            String deviceid = menjin.deviceAdd(menjin.EnDevice,user_id).getJSONObject("data").getString("device_id");
            Long start = System.currentTimeMillis();
            //配置权限
            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd(deviceid,scope,user_id,"USER",config);

            //通行
            menjin.edgeidentify(deviceid,"FACE",face_image);
            Long end = System.currentTimeMillis();
            //上传记录
            menjin.passageUpload(deviceid,user_id,end,"FACE",face_image,"true");

            //人物删除
            delPeopleScope(scope,user_id);

            //查询权限应为空
            JSONArray list = menjin.authList(deviceid,user_id).getJSONObject("data").getJSONArray("list");
            Preconditions.checkArgument(list.size()==0,"删除人物"+ user_id+"后，权限列表非空");

            //通行记录不为空
            JSONArray list2 = menjin.passRecdList(start,end,deviceid,user_id).getJSONObject("data").getJSONArray("list");
            Preconditions.checkArgument(list2.size()>0,"删除人物"+ user_id+"后，通行记录为空");

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
     *创建设备后，层级下设备数量+1
     */
    @Test
    public void addDevCheckscope() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：创建设备后，该层级下设备数量+1\n";

        String key = "";

        try {
            //查询设备列表
            String scope = menjin.EnDevice;
            int before = menjin.deviceList(scope).getJSONObject("data").getJSONArray("device_list").size();
            String name = "" + System.currentTimeMillis();
            //创建设备
            String deviceid = menjin.deviceAdd(scope,name).getJSONObject("data").getString("device_id");
            //再次查询
            int after = menjin.deviceList(scope).getJSONObject("data").getJSONArray("device_list").size();
            int change = after - before;
            Preconditions.checkArgument(change==1,"设备列表增加了" + change);

            //改变设备状态
            menjin.operateDevice(deviceid,"DISABLE");
            int after2 = menjin.deviceList(scope).getJSONObject("data").getJSONArray("device_list").size();
            Preconditions.checkArgument(after2==after,"设备状态改变后，列表数量改变");

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
     *人物通过后停用设备，通行记录>0
     */
    @Test
    public void passCheckRcd() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：人物通过后停用设备，通行记录>0\n";

        String key = "";

        try {

            //人物
            //人物注册
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            String [] a = addScopeUserDevImg(recordstart);
            String scope = a[0];
            String user_id = a[1];
            String device_id = a[2];
            //配置通行权限



            int pass_num = 10;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            String authid = menjin.authAdd(device_id,scope,user_id,"USER",config).getJSONObject("data").getString("auth_id");

            Long recordend = System.currentTimeMillis(); //记录结束时间
            //门卡识别
            menjin.edgeidentify(device_id,"CARD",user_id);//卡号用了userid

            //设备禁用
            menjin.operateDevice(device_id,"DISABLE");
            //上传记录
            menjin.passageUpload(device_id,user_id,recordend,"CARD",user_id,"true");

            //通行记录查询
            JSONArray recordlist = menjin.passRecdList(recordstart,recordend,device_id,user_id).getJSONObject("data").getJSONArray("list");
            Preconditions.checkArgument(recordlist.size()>0,"无记录");
            //删除人物
            delPeopleScope(scope,user_id);
            //启用设备
            menjin.operateDevice(device_id,"ENABLE");


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
     *同一人物在同一设备上配置多次权限，只有一条权限
     */
    @Test
    public void personOneDevAuths() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：同一人物在同一设备上配置多次权限，只有一条权限\n";

        String key = "";

        try {

            //人物注册
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            String [] a = addScopeUserDevImg(recordstart);
            String scope = a[0];
            String user_id = a[1];
            String device_id = a[2];

            //配置通行权限1


            int pass_num1 = 10;
            Long start_time1 = menjin.todayStartLong();
            Long end_time1 = start_time1 + 86400000;
            JSONObject config1 = menjin.authconfig(pass_num1,start_time1,end_time1,"FOREVER");
            String authid1 = menjin.authAdd(device_id,scope,user_id,"USER",config1).getJSONObject("data").getString("auth_id");

            //配置通行权限2

            int pass_num2 = 100;
            Long start_time2 = menjin.todayStartLong() - 86400000;
            Long end_time2 = start_time2 + 86400000 + 86400000 + 86400000;
            JSONObject config2 = menjin.authconfig(pass_num2,start_time2,end_time2,"FOREVER");
            String authid2 = menjin.authAdd(device_id,scope,user_id,"USER",config2).getJSONObject("data").getString("auth_id");

            //查询权限
            JSONObject authlist = menjin.authList(device_id,user_id).getJSONObject("data").getJSONArray("list").getJSONObject(0);

            JSONObject authconfig = authlist.getJSONObject("auth_config");
            int pass_num = authconfig.getInteger("pass_num");
            String start_time = authconfig.getString("start_time");
            String end_time = authconfig.getString("end_time");
            //删除人物
            delPeopleScope(scope,user_id);
            //删除通行权限
            menjin.authDelete(authid1);
            menjin.authDelete(authid2);

            Preconditions.checkArgument(authid1.equals(authid2),"权限id改变");
            Preconditions.checkArgument(pass_num==pass_num2,"passnum应为" + pass_num2 + "实际" + pass_num);
            Preconditions.checkArgument(start_time.equals(Long.toString(start_time2)),"starttime应为"+start_time2+ "实际" + start_time);
            Preconditions.checkArgument(end_time.equals(Long.toString(end_time2)),"endtime应为"+end_time2+ "实际" + end_time);




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
     *同一人物在多设备上配置多次权限，有多条权限
     */
    @Test
    public void personTwoDevAuths() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：同一人物在两个设备上配置权限，有两条权限\n";

        String key = "";

        try {
            //人物注册
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            String [] a = addScopeUserDevImg(recordstart);
            String scope = a[0];
            String user_id = a[1];
            String device_id = a[2];

            //配置通行权限1


            int pass_num1 = 10;
            Long start_time1 = menjin.todayStartLong();
            Long end_time1 = start_time1 + 86400000;
            JSONObject config1 = menjin.authconfig(pass_num1,start_time1,end_time1,"FOREVER");
            String authid1 = menjin.authAdd(device_id,scope,user_id,"USER",config1).getJSONObject("data").getString("auth_id");

            //配置通行权限2
            String device_id2 = menjin.beiyongdevice;
            int pass_num2 = 100;
            Long start_time2 = menjin.todayStartLong() - 86400000;
            Long end_time2 = start_time2 + 86400000 + 86400000 + 86400000;
            JSONObject config2 = menjin.authconfig(pass_num2,start_time2,end_time2,"FOREVER");
            String authid2 = menjin.authAdd(device_id2,scope,user_id,"USER",config2).getJSONObject("data").getString("auth_id");

            //查询权限
            JSONArray authlist = menjin.authListuser(user_id).getJSONArray("list");
            delPeopleScope(scope,user_id);
            //删除通行权限
            menjin.authDelete(authid1);
            menjin.authDelete(authid2);

            Preconditions.checkArgument(authlist.size() == 2,"权限数量实际为" + authlist.size());


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
     *删除设备权限，人物在该设备的权限还存在
     */
    @Test
    public void delDevAuthCheckPeople() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：删除设备权限，人物在该设备的权限还存在\n";

        String key = "";

        try {
            //人物注册
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            String [] a = addScopeUserDevImg(recordstart);
            String scope = a[0];
            String user_id = a[1];
            String device_id = a[2];

            //配置人物通行权限


            int pass_num1 = 10;
            Long start_time1 = menjin.todayStartLong();
            Long end_time1 = start_time1 + 86400000;
            JSONObject config1 = menjin.authconfig(pass_num1,start_time1,end_time1,"FOREVER");
            String authid1 = menjin.authAdd(device_id,scope,user_id,"USER",config1).getJSONObject("data").getString("auth_id");

            //配置设备通行权限

            int pass_num2 = 100;
            Long start_time2 = menjin.todayStartLong() - 86400000;
            Long end_time2 = start_time2 + 86400000 + 86400000 + 86400000;
            JSONObject config2 = menjin.authconfig(pass_num2,start_time2,end_time2,"FOREVER");
            String authid2 = menjin.authAdd(device_id,"","","DEVICE",config2).getJSONObject("data").getString("auth_id");

            int beforedel = menjin.authListdevice(device_id).getJSONArray("list").size();
            //删除设备通行权限
            menjin.authDelete(authid2);
            int afterdel = menjin.authListdevice(device_id).getJSONArray("list").size();
            int change = beforedel - afterdel;
            //查询权限
            JSONArray authlist = menjin.authListuser(user_id).getJSONArray("list");

            delPeopleScope(scope,user_id);

            Preconditions.checkArgument(authlist.size() >0,"人物权限被删除");
            Preconditions.checkArgument(change==1,"设备权限未被删除");


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
     *同一设备配置n个权限，查询后有n个权限
     */
    @Test
    public void deviceAddAuths() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：同一设备配置2个权限，查询后有2个权限\n";

        String key = "";

        try {
            String device_id = menjin.device;
            int beforedel = menjin.authListdevice(device_id).getJSONArray("list").size();

            //配置设备通行权限1
            int pass_num1 = 10;
            Long start_time1 = menjin.todayStartLong();
            Long end_time1 = start_time1 + 86400000;
            JSONObject config1 = menjin.authconfig(pass_num1,start_time1,end_time1,"FOREVER");
            String authid1 = menjin.authAdd(device_id,"","","DEVICE",config1).getJSONObject("data").getString("auth_id");

            //配置设备通行权限2
            int pass_num2 = 100;
            Long start_time2 = menjin.todayStartLong() - 86400000;
            Long end_time2 = start_time2 + 86400000 + 86400000 + 86400000;
            JSONObject config2 = menjin.authconfig(pass_num2,start_time2,end_time2,"FOREVER");
            String authid2 = menjin.authAdd(device_id,"","","DEVICE",config2).getJSONObject("data").getString("auth_id");

            int afterdel = menjin.authListdevice(device_id).getJSONArray("list").size();
            int change = afterdel - beforedel;


            //删除设备通行权限
            menjin.authDelete(authid2);
            menjin.authDelete(authid1);

            Preconditions.checkArgument(change==2,"设备权限数量="+ change);


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
        if (DEBUG.equals("true")) {
            return;
        }
        setBasicParaToDB(aCase, ciCaseName, caseName, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason())) {
            logger.error(aCase.getFailReason());
            dingPush("门禁日常-数据一致性 \n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
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

//            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

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


    private  String[] addScopeUserDevImg(Long recordstart) throws Exception {
        String [] scopeUserDev = new String[4];
        //添加层级
        String scope = menjin.scopeAdd(Long.toString(recordstart),"2",menjin.brand).getJSONObject("data").getString("scope");

        //注册人物，单一人脸
        String user_id = "user" + System.currentTimeMillis();
        String image_type = "BASE64";
        String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/于海生.jpg.png");
        JSONObject obj = menjin.userAdd(scope,user_id,image_type,face_image,"","");
        checkCode(obj);

        //启用设备
        String device_id = menjin.device;
        menjin.operateDevice(device_id,"ENABLE");
        //删除device权限
        menjin.authListdevice(device_id);
        scopeUserDev[0] = scope;
        scopeUserDev[1] = user_id;
        scopeUserDev[2] = device_id;
        scopeUserDev[3] = face_image;
        return scopeUserDev;
    }
    private static void checkCode(JSONObject obj){
        int codee = obj.getInteger("code");
        if (codee!=1000){
            String message = obj.getString("message");
            String req = obj.getString("request_id");
            Preconditions.checkArgument(1==2,"新建人物失败"+ message +"\nrequest_id  "+ req);

        }
    }
    private  void delPeopleScope(String scope,String user_id) throws Exception {
        //删除人物
        int code = menjin.userDelete(scope,user_id).getInteger("code");
        Preconditions.checkArgument(code==1000,"人物"+user_id+"删除失败");

        //删除层级
        menjin.scopeDelete(scope,"2");
    }

    private  String[] addUserwithCard() throws Exception {
        String [] scopeUserDev = new String[3];
        //添加层级
        String name = ""+ System.currentTimeMillis();
        String scope = menjin.scopeAdd(name,"2",menjin.brand).getJSONObject("data").getString("scope");

        //注册人物，单一人脸
        String user_id = "user" + System.currentTimeMillis();
        String image_type = "BASE64";
        String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/于海生.jpg.png");
        JSONObject obj = menjin.userAdd(scope,user_id,image_type,face_image,user_id,"user");
        checkCode(obj);

        scopeUserDev[0] = scope;
        scopeUserDev[1] = user_id;
        scopeUserDev[2] = face_image;
        return scopeUserDev;
    }

       //public static void main(String[] args) throws Exception {// ---不用理我！
        //String path = "src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/分辨率较低.png";
        //String imgbese = getImgStr(path);




    //}


}