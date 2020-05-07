
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

            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/1.png");

            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"user");

            //人物查询
            JSONObject single = menjin.userInfo(scope,user_id).getJSONObject("data");
            String search_card = single.getString("card_key");
            String search_name = single.getString("user_name");
            Preconditions.checkArgument(search_card.equals(user_id) && search_name.equals("user"),"注册后信息不一致");

            //人物更新
            String new_card = "new" + System.currentTimeMillis();

            menjin.userUpdate(scope,user_id,image_type,face_image,new_card,"usertwo");
            //人物查询
            JSONObject single2 = menjin.userInfo(scope,user_id).getJSONObject("data");
            String search_card2 = single2.getString("card_key");
            String search_name2 = single2.getString("user_name");
            Preconditions.checkArgument(search_card2.equals(new_card) && search_name2.equals("usertwo"),"更新后信息不一致");
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
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/1.png");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"用户");

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
            menjin.userDelete(scope,user_id);

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
            Long recordstart = System.currentTimeMillis(); //记录开始时间
            //人物
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/1.png");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");

            //配置通行权限

            String device_id = menjin.device;

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
            menjin.userDelete(scope,user_id);
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

            //人物
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/1.png");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");

            //配置通行权限1

            String device_id = menjin.device;
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
            menjin.userDelete(scope,user_id);
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
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/1.png");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");

            //配置通行权限1

            String device_id = menjin.device;
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
            //删除人物
            menjin.userDelete(scope,user_id);
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
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/1.png");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"");

            //配置人物通行权限

            String device_id = menjin.device;
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

            //删除人物
            menjin.userDelete(scope,user_id);

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



    //---------------删除用户/设备/权限/配置权限-------------
    @Test
    public void delUserBatch() throws Exception {
        List<Integer> inputList=null;
        try (FileReader reader = new FileReader("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/c.txt");
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;

            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                String[] inputdatas=line.split("    ");
                for(String s:inputdatas){
                    String[] a = s.split("\\s+");
                    //System.out.println(a[0]);
                    //System.out.println(a[1]);
                    menjin.userDelete(a[0],a[1]);
                    System.out.println("del" + a[1]);
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void deleteuser() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "删除人物\n";

        String key = "";

        try {
            //menjin.userDelete(menjin.scopeUser,"lvxueqing");
            menjin.userDelete(menjin.scopeUser,"user1588839382262");

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
    public void adddeviceauth() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "配置设备权限\n";

        String key = "";

        try {
            //配置权限

            int pass_num = 0;
            //Long start_time = menjin.todayStartLong();
            //Long end_time = start_time + 86400000;
            //Long start_time = -1L;
            //Long end_time = -1L;
            //Long start_time = menjin.todayStartLong() - 86400000 -86400000;
            //Long end_time = start_time + 86400000;

            String start_time = "00:00:00";
            String end_time = "23:59:00";

            //JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");

            menjin.authAdd("7404475132150784","","","DEVICE",config);
            //menjin.authAdd("152","","","DEVICE",config);

            // menjin.deviceauthDelete("7404475132150784");
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
    public void deldeviceauth() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "删除设备权限\n";

        String key = "";

        try {
            menjin.deviceauthDelete("7404475132150784","");
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
    public void adduser() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "\n";

        String key = "";

        try {
            //人物注册

            String scope = menjin.scopeUser;
            String user_id = "lvxueqing";
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"177BDC49","");

            //menjin.userAdd(menjin.EnDevice,"existpeopletest","BASE64",getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/existtest.png"),"","别删");

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
    public void adduserauth() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "配置用户权限\n";

        String key = "";

        try {
            //配置权限

            int pass_num = -1;
            //Long start_time = menjin.todayStartLong()-86400000-86400000;
            //Long end_time = start_time + 86400000;
            //Long start_time = menjin.todayStartLong();
            //Long end_time = start_time + 86400000;
            //Long start_time = -1L;
            //Long end_time = -1L;
            //JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");



            String start_time = "10:21:00";
            String end_time = "22:22:00";

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");

            menjin.authAdd("7404475132150784",menjin.scopeUser,"lvxueqing","USER",config);

            // menjin.deviceauthDelete("7404475132150784");
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
    public void deluserauth() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "删除用户权限\n";

        String key = "";

        try {
            menjin.authDelete("10349");
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

    //@Test
    public void adddevice() throws Exception {
        menjin.deviceAdd(menjin.EnDevice,"Testdevice2");
        menjin.deviceAdd(menjin.EnDevice,"Testdevice3");
        menjin.deviceAdd(menjin.EnDevice,"Testdevice4");
        menjin.deviceAdd(menjin.EnDevice,"Testdevice5");
        menjin.deviceAdd(menjin.EnDevice,"Testdevice6");
        menjin.deviceAdd(menjin.EnDevice,"Testdevice7");
        menjin.deviceAdd(menjin.EnDevice,"Testdevice8");
        menjin.deviceAdd(menjin.EnDevice,"Testdevice9");
        menjin.deviceAdd(menjin.EnDevice,"Testdevice10");
    }

    @Test
    public void operatordevice() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "禁用设备\n";

        String key = "";

        try {
            //menjin.operateDevice("7404475132150784","DISABLE");
            menjin.operateDevice("7404475132150784","ENABLE");
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
    public void tb() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "禁用设备\n";

        String key = "";

        try {

            //menjin.operateDevice("7404475132150784","ENABLE");
            menjin.fullSync("7404475132150784","AUTH_USER");
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

    //@Test
    public void test() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "配置用户权限设备id空\n";

        String key = "";

        try {
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/w1.png");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"testcrf");

            //配置人物通行权限
            int pass_num = 2;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd("null",scope,user_id,"USER",config);

            //删除人物
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
    public void delscope() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "删除层级\n";

        String key = "";

        try {
            menjin.scopeDelete("14876","1");
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
    public void updateuser() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "\n";

        String key = "";

        try {
            //人物更新

            String scope = menjin.scopeUser;
            String user_id = "lvxueqing";
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userUpdate(scope,user_id,image_type,face_image,"177BDC49","小吕今天也是美女");
            //menjin.userAdd(menjin.EnDevice,"existpeopletest","BASE64",getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/existtest.png"),"","别删");

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
    public void qrcode() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "\n";

        String key = "";

        try {
            //人物更新

            String scope = menjin.scopeUser;
            String user_id = "lvxueqing";
            menjin.userQRCode(scope,user_id);
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

      // public static void main(String[] args) throws Exception {// ---不用理我！
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


   // }


}