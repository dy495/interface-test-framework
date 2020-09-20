package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import org.springframework.util.StringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;


/**
 * @author : yu
 * @date :  2020/05/30
 */

public class XundianCase extends TestCaseCommon implements TestCaseStd {
    XundianScenarioUtil xd = XundianScenarioUtil.getInstance();
    String xjy4="uid_663ad653";
    String test = "uid_ef6d2de5";
    int page = 1;
    int size =50;




    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     *
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_XUNDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "青青";

        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "xundian-daily-test");

        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "巡店 日常");


        //set shop id
        commonConfig.shopId = getXundianShop(); //要改！！！
        beforeClassInit(commonConfig);

        logger.debug("xundian " + xd);

        xd.login("yuexiu@test.com","f5b3e737510f31b88eb2d4b5d0cd2fb4");


    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     *
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }
    /**
     *
     * ====================定检规则======================
     * */
    @Test
    public void checkListAdd() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //新增一个执行清单
            int startM=2;
            String name= dt.getHHmm(startM)+"qingqing";
            String desc="是青青创建的哦，为了测试用的";
            JSONArray  items=new JSONArray();//new一个数组
            JSONObject jsonObject = new JSONObject();//数组里面是JSONObject
            jsonObject.put("order",0);
            jsonObject.put("title","我是青青第一线");
            jsonObject.put("comment","要怎么检查啊啊");
            items.add(0,jsonObject);
            JSONArray  shoplist=new JSONArray();
              shoplist.add(0,28764);
              JSONObject res = xd.checkListAdd(name,desc,items,shoplist);
              int code_add = res.getInteger("code");


              //获取执行清单列表，取第一个执行清单的id值
             JSONArray list = xd.checklistPage(page,size).getJSONArray("list");
             long id = list.getJSONObject(0).getInteger("id");


              //编辑一个执行清单
            String name_one= dt.getHHmm(startM)+"qingqingb";
            JSONObject res_one = xd.checkListEdit((long) id,name_one,desc,items,shoplist);
            int code_edit = res_one.getInteger("code");


            //删除一个执行清单
            JSONObject res_delete = xd.checkListDelete(id);
            int code_delete = res_delete.getInteger("code");



            Preconditions.checkArgument(code_add == 1000,"新建执行清单失败，code="+code_add);
            Preconditions.checkArgument(!StringUtils.isEmpty(list) ,"执行清单列表获取异常");
            Preconditions.checkArgument(code_edit == 1000,"编辑执行清单失败，code="+code_edit);
            Preconditions.checkArgument(code_delete == 1000,"删除执行清单失败，code="+code_delete);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("PC端巡检配置新建定检任务");
        }

    }

    /**
     *
     * ====================定检任务======================
     * */
    @Test
    public void scheduleRuleAdd() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //新建定检任务
            int startM=1;
            String names= "qingqing" + dt.getHHmm(startM);
            String cycle="WEEK";
            JSONArray  jal=new JSONArray();
            jal.add(0,"MON");
            jal.add(0,"TUES");
            jal.add(0,"WED");
            jal.add(0,"THUR");
            jal.add(0,"FRI");
            jal.add(0,"SAT");
            jal.add(0,"SUN");
            String send_time=dt.getHHmm(0);
            String valid_start=dt.getHistoryDate(0); ;
            String valid_end=dt.getHistoryDate(startM); ;
            JSONArray  shoplist=new JSONArray();
            shoplist.add(0,28760);
            JSONObject res = xd.scheduleCheckAdd(names,cycle,jal,send_time,valid_start,valid_end,test,shoplist);
            int code_add=res.getInteger("code");

            //获取定检任务列表，取第一个执行清单的id值
            JSONArray list = xd.scheduleCheckPage(page,size).getJSONArray("list");
            long id = list.getJSONObject(0).getInteger("id");

            //编辑一个定检任务
            String name_one= dt.getHHmm(startM)+"qingqinga";
            JSONObject res_one = xd.scheduleCheckEdit(id,name_one,cycle,jal,send_time,valid_start,valid_end,test,shoplist);
            int code_edit = res_one.getInteger("code");


           //删除一个定检任务
            JSONObject res_delete = xd.scheduleCheckDelete(id);
            int code_delete = res_delete.getInteger("code");


            Preconditions.checkArgument(code_add == 1000,"新建定检任务失败，code="+code_add);
            Preconditions.checkArgument(!StringUtils.isEmpty(list) ,"定检任务列表获取异常");
            Preconditions.checkArgument(code_edit == 1000,"编辑定检任务失败，code="+code_edit);
            Preconditions.checkArgument(code_delete == 1000,"删除定检任务失败，code="+code_delete);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("PC端巡检配置新建定检任务");
        }

    }

    /**
     *
     * ====================定检规则======================
     * */
    @Test
    public void createdScheduleCheck() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            String end_time= "03:00";//获取当前时间
            int  interval_hour= 1; //间隔时间;
            String start_time="09:00"; //今天日期;;
            String name = "青青测试用";
            JSONArray  shoplist=new JSONArray();
            shoplist.add(0,4116);

            //新建定检规则
            JSONObject res =xd.scheduleRuleAdd(name,start_time,end_time,interval_hour,shoplist);
            int code = res.getInteger("code");



            //----------------------获取列表刚刚我新建的定检规则的id
            JSONObject response = xd.scheduleRuleList(page,size);
            JSONArray list = response.getJSONArray("list");
            int id = 0;
            int status = 0;
            for(int i=0;i<list.size();i++){
                String the_name = list.getJSONObject(i).getString("name");
                if(the_name.equals(name)){
                    id = list.getJSONObject(i).getInteger("id");
                    status = list.getJSONObject(i).getInteger("status");
                }
            }

            //将新建的定检规则设置为开或者关(0为关，1为开)
            int code_swi = 0;
            if(status == 0){
                 code_swi = xd.scheduleRuleSwith(id,status).getInteger("code");
            }else {
                code_swi = xd.scheduleRuleSwith(id,status).getInteger("code");
            }


            //编辑定检规则
            int interval_hours = 2;
            JSONObject response_two = xd.scheduleRuleEdit(name,start_time,end_time,interval_hours,shoplist,id);
            int code_two = response_two.getInteger("code");


            //删除定检规则
            JSONArray  rule_ids=new JSONArray();
            rule_ids.add(0,id);
            JSONObject res_thr = xd.scheduleRuleDelete(rule_ids);
            int code_thr = res_thr.getInteger("code");


            Preconditions.checkArgument(code == 1000,"新建定检规则失败，code="+code);
            Preconditions.checkArgument(code_swi == 1000,"定检规则的开关按钮启动失败，code="+code_swi);
            Preconditions.checkArgument(code_two == 1000,"编辑定检规则失败，code="+code_two);
            Preconditions.checkArgument(code_thr == 1000,"删除定检规则失败，code="+code_thr);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("PC端巡检配置新建定检任务");
        }

    }
    /**
     *
     * ====================巡店中心列表======================
     * */
    @Test
    public void patrolShopPage() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            int page=1;
            int size=10;

            xd.ShopPage(page,size);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("PC端巡店中心列表");
        }

    }
}
