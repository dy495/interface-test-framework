package com.haisheng.framework.testng.bigScreen.xundianDaily.zt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.xundianDaily.MendianInfo;
import com.haisheng.framework.testng.bigScreen.xundianDaily.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.XundianScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.StorePcAndAppData;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.fucPackage.StoreFuncPackage;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.testng.service.ApiRequest;
import com.haisheng.framework.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class StorePcData extends TestCaseCommon implements TestCaseStd {
    public static final Logger log = LoggerFactory.getLogger(StorePcAndAppData.class);
    public static final int page = 1;
    public static final int size = 100;
    XundianScenarioUtil xd = XundianScenarioUtil.getInstance();
    StoreScenarioUtil md = StoreScenarioUtil.getInstance();
    StoreFuncPackage mds = StoreFuncPackage.getInstance();
    MendianInfo info = new MendianInfo();
    String name = "zdh";
    String phone = "18888888888";
    String email = "334411@qq.com";
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.QQ.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_DAILY_TEST.getJobName());
        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.MENDIAN_DAILY.getDesc());
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672", "18513118484", "18810332354", "15084928847"};
        commonConfig.shopId = EnumTestProduce.MENDIAN_DAILY.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("xundian " + xd);
        xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");

    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }




    //图片中心总数==展示的数量
    @Test
    public void picNum()throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int pictotal = md.picturePage("","","","",null,1,8).getInteger("total");
            int pages = md.picturePage("","","","",null,1,8).getInteger("pages");
            int pagesize = (pages-1)*8;
            int pages_size = md.picturePage("","","","",null,pages,8).getInteger("page_size");

            CommonUtil.valueView(pictotal, pagesize+pages_size);
            checkArgument(pictotal == pagesize+pages_size, "图片中心总数" + pictotal + "!=搜索出图片的数量" + pagesize+pages_size);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("图片中心总数==展示的数量");
        }
    }


    //定检巡查展示图片个数==返回的数量
    @Test
    public void picScheduled() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int pictotal = md.picturePage("SCHEDULED","","","",null,1,8).getInteger("total");
            int pages = md.picturePage("SCHEDULED","","","",null,1,8).getInteger("pages");
            int pagesize = (pages-1)*8;
            int pages_size = md.picturePage("SCHEDULED","","","",null,pages,8).getInteger("page_size");

            CommonUtil.valueView(pictotal, pagesize+pages_size);
            checkArgument(pictotal == pagesize+pages_size, "图片中心总数" + pictotal + "!=搜索出图片的数量" + pagesize+pages_size);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("定检巡查展示图片个数==返回的数量");
        }
    }




    //定检巡查展示图片类型+日期==返回的图片类型+日期
    @Test
    public void picScheduled2() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String start_time = "2021-01-19";
            String end_time = "2021-01-22";
            int pictotal = md.picturePage("SCHEDULED",start_time,end_time,"",null,1,8).getInteger("total");
            int pages = md.picturePage("SCHEDULED",start_time,end_time,"",null,1,8).getInteger("pages");
            for(int i=1;i<=pages;i++){
                JSONArray list = md.picturePage("SCHEDULED",start_time,end_time,"",null,i,8).getJSONArray("list");
                for(int j=0;j<list.size();j++){
                    String tips = list.getJSONObject(j).getString("tips");
                    String date_time = list.getJSONObject(j).getString("date_time");
                    String s = date_time.substring(0,10);
                    int a = s.compareTo(start_time);
                    int b = s.compareTo(end_time);
                    checkArgument(tips.contains("定检巡店"), "定检巡查" + "!=图片返回的类型" + tips);
                    checkArgument(a>=0&&b<=0, "定检巡查+日期" + "!=图片返回日期" + date_time);
                }

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("定检巡查展示图片类型+日期==返回的图片类型+日期");
        }
    }


    //定检巡查展示图片类型+日期+门店名称+异常==返回的图片类型+日期+门店名称+异常
    @Test
    public void picScheduled3() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String start_time = "2021-01-19";
            String end_time = "2021-01-22";
            int pictotal = md.picturePage("SCHEDULED",start_time,end_time,"",1,1,8).getInteger("total");
            int pages = md.picturePage("SCHEDULED",start_time,end_time,info.shop_id_01_chin,1,1,8).getInteger("pages");
            for(int i=1;i<=pages;i++){
                JSONArray list = md.picturePage("SCHEDULED",start_time,end_time,info.shop_id_01_chin,1,i,8).getJSONArray("list");
                for(int j=0;j<list.size();j++){
                    String tips = list.getJSONObject(j).getString("tips");
                    String date_time = list.getJSONObject(j).getString("date_time");
                    String title = list.getJSONObject(j).getString("title");
                    int isAbnormal = list.getJSONObject(j).getInteger("is_abnormal");
                    String s = date_time.substring(0,10);
                    int a = s.compareTo(start_time);
                    int b = s.compareTo(end_time);
                    checkArgument(tips.contains("定检巡店"), "定检巡查" + "!=图片返回的类型" + tips);
                    checkArgument(a>=0&&b<=0, "定检巡查+日期" + "!=图片返回日期" + date_time);
                    checkArgument(title.contains(info.shop_id_01_chin), "输入的门店名称" + "!=返回的门店名称" + title);
                    checkArgument(isAbnormal==1, "异常图片" + "!=图片返回的状态" + isAbnormal);
                }

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("定检巡查展示图片类型+日期+门店名称+异常==返回的图片类型+日期+门店名称+异常");
        }
    }



    //定检巡查展示图片类型+日期+门店名称+非异常==返回的图片类型+日期+门店名称+非异常
    @Test
    public void picScheduled4() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String start_time = "2021-01-19";
            String end_time = "2021-01-22";
            int pictotal = md.picturePage("SCHEDULED",start_time,end_time,"",1,1,8).getInteger("total");
            int pages = md.picturePage("SCHEDULED",start_time,end_time,info.shop_id_01_chin,0,1,8).getInteger("pages");
            for(int i=1;i<=pages;i++){
                JSONArray list = md.picturePage("SCHEDULED",start_time,end_time,info.shop_id_01_chin,0,i,8).getJSONArray("list");
                for(int j=0;j<list.size();j++){
                    String tips = list.getJSONObject(j).getString("tips");
                    String date_time = list.getJSONObject(j).getString("date_time");
                    String title = list.getJSONObject(j).getString("title");
                    int isAbnormal = list.getJSONObject(j).getInteger("is_abnormal");
                    String s = date_time.substring(0,10);
                    int a = s.compareTo(start_time);
                    int b = s.compareTo(end_time);
                    checkArgument(tips.contains("定检巡店"), "定检巡查" + "!=图片返回的类型" + tips);
                    checkArgument(a>=0&&b<=0, "定检巡查+日期" + "!=图片返回日期" + date_time);
                    checkArgument(title.contains(info.shop_id_01_chin), "输入的门店名称" + "!=返回的门店名称" + title);
                    checkArgument(isAbnormal==0, "异常图片" + "!=图片返回的状态" + isAbnormal);
                }

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("定检巡查展示图片类型+日期+门店名称+异常==返回的图片类型+日期+门店名称+异常");
        }
    }


    //手动留痕展示图片个数==返回的数量
    @Test
    public void picSpot() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int pictotal = md.picturePage("SPOT","","","",null,1,8).getInteger("total");
            int pages = md.picturePage("SPOT","","","",null,1,8).getInteger("pages");
            int pagesize = (pages-1)*8;
            int pages_size = md.picturePage("SPOT","","","",null,pages,8).getInteger("page_size");

            CommonUtil.valueView(pictotal, pagesize+pages_size);
            checkArgument(pictotal == pagesize+pages_size, "图片中心总数" + pictotal + "!=搜索出图片的数量" + pagesize+pages_size);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("手动留痕展示图片个数==返回的数量");
        }
    }


    //手动留痕展示图片类型==返回的图片类型
    @Test
    public void picSpot1() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int pictotal = md.picturePage("SPOT","","","",null,1,8).getInteger("total");
            int pages = md.picturePage("SPOT","","","",null,1,8).getInteger("pages");
            for(int i=1;i<=pages;i++){
                JSONArray list = md.picturePage("SPOT","","","",null,i,8).getJSONArray("list");
                for(int j=0;j<list.size();j++){
                    String tips = list.getJSONObject(j).getString("tips");
//                    int a = tips.indexOf("定检巡店");
                    checkArgument(tips.contains("现场巡店")||tips.contains("远程巡店"), "手动巡查" + "!=图片返回的类型" + tips);
                }

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("手动留痕展示图片类型==返回的图片类型");
        }
    }



    //手动展示图片类型+日期+门店名称+非异常==返回的图片类型+日期+门店名称+非异常
    @Test
    public void picSpot4() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String start_time = "2021-01-19";
            String end_time = "2021-01-22";
            int pages = md.picturePage("SPOT",start_time,end_time,info.shop_id_01_chin,0,1,8).getInteger("pages");
            for(int i=1;i<=pages;i++){
                JSONArray list = md.picturePage("SPOT",start_time,end_time,info.shop_id_01_chin,0,i,8).getJSONArray("list");
                for(int j=0;j<list.size();j++){
                    String tips = list.getJSONObject(j).getString("tips");
                    String date_time = list.getJSONObject(j).getString("date_time");
                    String title = list.getJSONObject(j).getString("title");
                    int isAbnormal = list.getJSONObject(j).getInteger("is_abnormal");
                    String s = date_time.substring(0,10);
                    int a = s.compareTo(start_time);
                    int b = s.compareTo(end_time);
                    checkArgument(tips.contains("现场巡店")||tips.contains("远程巡店"), "定检巡查" + "!=图片返回的类型" + tips);
                    checkArgument(a>=0&&b<=0, "手动留痕+日期" + "!=图片返回日期" + date_time);
                    checkArgument(title.contains(info.shop_id_01_chin), "输入的门店名称" + "!=返回的门店名称" + title);
                    checkArgument(isAbnormal==0, "异常图片" + "!=图片返回的状态" + isAbnormal);
                }

            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("手动展示图片类型+日期+门店名称+异常==返回的图片类型+日期+门店名称+异常");
        }
    }

    //非自定义导出报表
    @Test
    public void ReportExport() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = xd.reportList(1,100,null,null,null,null).getJSONArray("list");
            if (list.size()>0){
                JSONArray shop_id_List = new JSONArray();
                shop_id_List.add("");
                int id = list.getJSONObject(0).getInteger("id");
                int code = xd.customizeReportExport(id,"","",shop_id_List,"","","").getInteger("code");
                Preconditions.checkArgument(code==1000,"非自定义导出报表 id="+id+", 状态码"+code);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC非自定义导出报表");
        }
    }

    //自定义导出报表
    @Test
    public void customizeReport() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray shop_id_List = new JSONArray();
            shop_id_List.add("43072");
            String start_time = "2021-03-11";
            String end_time = "2021-03-14";
            JSONObject res = xd.customizeReportExport(2,"CHECK_REPORT","MONTH",shop_id_List,start_time,end_time,"aaa");
            checkArgument(res.getInteger("code") == 1000, "自定义导出报表状态码"+res.getInteger("code"));
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("自定义导出报表");
        }
    }


    //新建角色
    @Test
    public void user_add() {
        logger.logCaseStart(caseResult.getCaseName());

        try {

            String description = "自动化新建角色";
            JSONArray moduleId = new JSONArray();
            moduleId.add(7);
            moduleId.add(9);

            //新增一个角色
            Integer roleNum = md.organizationRolePage("", page, size).getInteger("total");
            JSONObject res = md.organizationRoleAddTwo(name, 1,description, moduleId);
            Integer code = res.getInteger("code");
            Integer role_id = md.organizationRolePage(name, page, size).getJSONArray("list").getJSONObject(0).getInteger("role_id");
            checkArgument(code == 1000, "新增角色失败了");
            Integer roleNum1 = md.organizationRolePage("", page, size).getInteger("total");
            checkArgument(roleNum1-roleNum == 1, "新增角色后，角色列表没有+1");
            //编辑角色
            String name1 = "自动化在编辑";
            Integer code1 = md.organizationRoleEditTwo(role_id, 1,name1, description, moduleId).getInteger("code");
            checkArgument(code1 == 1000, "编辑角色的信息失败了");
            //列表中编辑过的角色是否已更新
            JSONArray list1 = md.organizationRolePage(name1, page, size).getJSONArray("list");
            String role_name = list1.getJSONObject(0).getString("role_name");
            checkArgument(name1.equals(role_name), "编辑过的角色没有更新在列表");
            //新建成功以后删除新建的账号
            if (name.equals(role_name)) {
                Integer code2 = md.organizationRoleDelete(role_id).getInteger("code");
                Integer roleNum2 = md.organizationRolePage("", page, size).getInteger("total");
                checkArgument(code2 == 1000, "删除角色:" + role_id + "失败了");
                checkArgument(roleNum1-roleNum2 == 1, "删除角色后，角色列表没有-1");
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新增删改查角色");
        }
    }



    @Test
    public void user_add_work() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray moduleId = new JSONArray();
            moduleId.add(7);
            moduleId.add(9);
            //新增角色名称20个字的角色
            String description = "青青测试给店长自动化用的角色";
            JSONObject res = md.organizationRoleAddTwo("这是一个二十字的角色名称是的是的是的是的",1, description, moduleId);
            checkArgument(res.getInteger("code") == 1000, "角色名称为20个字，创建失败");
            //新增角色名称20个字英文+中文+数字的角色
            JSONObject res1 = md.organizationRoleAddTwo("这是一个二十字的角色名称AABB1111",1, description, moduleId);
            checkArgument(res1.getInteger("code") == 1000, "角色名称为中文+字母+数字，创建失败");
            //新增角色名称20个字英文+中文+数字+字符的角色
            JSONObject res2 = md.organizationRoleAddTwo("这是一个二十字的角色名称AABB11.。",1, description, moduleId);
            checkArgument(res2.getInteger("code") == 1000, "角色名称为中文+字母+数字+字符，创建失败");
            //新增角色名称21个字角色
            JSONObject res3 = md.organizationRoleAddTwo("这是一个二十一字的角色名称是的是的是的是的", 1,description, moduleId);
            checkArgument(res3.getString("message").equals("角色名称需要在1-20个字内"), "角色名称为21个字，创建成功");
            //新增重复角色名称的角色
            JSONObject res4 = md.organizationRoleAddTwo("这是一个二十字的角色名称AABB11.。",1,description, moduleId);
            checkArgument(res4.getString("message").equals("新增角色异常:当前角色名称已存在！请勿重复添加"), "重复的角色名称，创建成功");
            //将账户使用次数为0的角色删除
            mds.deleteRole();
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新增角色(名称校验)");
        }

    }

    /**
     * ====================新增角色(权限说明校验)======================
     */
    @Test
    public void user_add_work1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray moduleId = new JSONArray();
            moduleId.add(7);
            moduleId.add(9);
            moduleId.add(10);
            //新增角色权限说明50个字的角色
            JSONObject res = md.organizationRoleAddTwo("auto名字3", 1,"不是这是一个二十字的角色名称是的是的是的不是的的不是的好的好还需要二十个字现在是三十七了吧刚好五个字", moduleId);
            checkArgument(res.getInteger("code") == 1000, "角色权限说明为50个字，创建失败");
            //新增角色权限说明角色字英文+中文+数字的角色
            JSONObject res1 = md.organizationRoleAddTwo("auto名字1",1, "22一个二十字的角色名称AABB", moduleId);
            checkArgument(res1.getInteger("code") == 1000, "角色权限说明中文+字母+数字，创建失败");
            //新增角色权限说明角色英文+中文+数字+字符的角色
            JSONObject res2 = md.organizationRoleAddTwo("auto名字2",1, "这是一个二十字色名称BB11.。", moduleId);
            checkArgument(res2.getInteger("code") == 1000, "角色权限说明为中文+字母+数字+字符，创建失败");
            //新增角色权限说明51个字的角色
            JSONObject res3 = md.organizationRoleAddTwo("auto名字4",1, "不是这是一个二十字的角色名称是的是的是的不是的的不是的好的好还需要二十个字现在是三十七了吧刚好五个字多", moduleId);
            checkArgument(res3.getString("message").equals("角色名称需要在1-50个字内"), "角色权限说明为51个字，创建成功");
            mds.deleteRole();
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新增角色(权限说明校验)");
        }

    }



    /**
     * ====================新增账号======================
     */
    @Test
    public void accountAdd_Phone() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray roleIdList = new JSONArray();
            roleIdList.add(2);
            JSONArray shopIdList = new JSONArray();
            shopIdList.add(4116);
            int status = 1;
            String type = "PHONE";
            //用phone新增一个账号
            JSONObject res = md.organizationAccountAddTwo("",name,"123456","uid_ef6d2de5", type,"", phone,status,roleIdList,shopIdList);
            Integer code = res.getInteger("code");
            checkArgument(code == 1000, "用手机号:" + phone + "新增一个账号失败了");
            //从列表获取刚刚新增的那个账户的名称进行搜获获取她的account
            JSONArray accountList = md.organizationAccountPage(name, "", "", phone, "", "", page, size).getJSONArray("list");
            String account = accountList.getJSONObject(0).getString("account");
            //新建后编辑账号
            JSONObject res1 = md.organizationAccountEditTwo(account,"qqqqq","111","uid_ef6d2de5",type,"",phone,status,roleIdList,shopIdList);
            Integer code2 = res1.getInteger("code");
            checkArgument(code2 == 1000, "用姓名:" + "qqqqq" + "编辑一个账号失败了");
            //新建成功以后删除新建的账号
            Integer code1 = md.organizationAccountDelete(account).getInteger("code");
            checkArgument(code1 == 1000, "删除手机号的账号:" + phone + "失败了");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("用手机号新增账号,编辑账号,删除账号");
        }
    }

    @Test
    public void accountAdd_Email() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray roleIdList = new JSONArray();
            roleIdList.add(2);
            JSONArray shopIdList = new JSONArray();
            shopIdList.add(4116);
            int status = 1;
            String type = "EMAIL";
            //用email新增一个账号
            JSONObject res = md.organizationAccountAddTwo("",name,"123456","uid_ef6d2de5", type,email, "",status,roleIdList,shopIdList);
            Integer code = res.getInteger("code");
            checkArgument(code == 1000, "用邮箱号:" + email + "新增一个账号失败了");
            //从列表获取刚刚新增的那个账户的名称进行搜获获取她的account
            JSONArray accountList = md.organizationAccountPage(name, "", email,"", "", "", page, size).getJSONArray("list");
            String account = accountList.getJSONObject(0).getString("account");
            //新建后编辑账号
            JSONObject res1 = md.organizationAccountEditTwo(account,"qqqqq","111","uid_ef6d2de5",type,"",phone,status,roleIdList,shopIdList);
            Integer code2 = res1.getInteger("code");
            checkArgument(code2 == 1000, "用姓名:" + "qqqqq" + "编辑一个账号失败了");
            //新建成功以后删除新建的账号
            Integer code1 = md.organizationAccountDelete(account).getInteger("code");
            checkArgument(code1 == 1000, "删邮箱的账号:" + email + "失败了");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("用邮箱号新增账号,编辑账号,删除账号");
        }
    }



    //会员身份添加、删除
    @Test
    public void AddMember(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer total = md.Member(page,size).getInteger("total");
            String identity = "测试VIP！";
            JSONObject res =  md.AddMember(identity);
            checkArgument(res.getInteger("code") == 1000, "添加会员身份成功");
            Integer total1 = md.Member(page,size).getInteger("total");
            int a = total1-total;
            checkArgument(a==1, "添加会员身份后，身份列表+1，实际添加了"+a);
            JSONArray list = md.Member(page,size).getJSONArray("list");
            int id = list.getInteger(0);
            JSONObject res1 = md.DeleteMember(id);
            checkArgument(res1.getInteger("code") == 1000, "删除会员身份成功");
            Integer total2 = md.Member(page,size).getInteger("total");
            int b = total1-total2;
            checkArgument(b==1, "删除会员身份后，身份列表-1，实际减少了"+b);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员身份添加，删除，");

        }
    }


    //注册会员，会员管理列表+1，通过搜索框进行搜索
    @Test(dataProvider = "FACE_URL",dataProviderClass = DataProviderMethod.class)
    public void MemberList(String face_url){
        logger.logCaseStart(caseResult.getCaseName());
        try {
           //通过搜索框搜索会员
            JSONArray list = md.MemberList(page,size,"","","","","").getJSONArray("list");
            String member_ID = "11223344";
            String member_name = "测试会员11@@aaa";
            String birthday = "1998-10-01";
            JSONObject res = md.RegisterMember(face_url,member_ID,member_name,phone,birthday,"",1);
            checkArgument(res.getInteger("code") == 1000, "添加会员成功");

            JSONArray list0 = md.MemberList(page,size,"","","","","").getJSONArray("list");
            Integer a = list0.size()-list.size();
            checkArgument(a==1, "新注册一个会员，会员列表实际添加了"+a);

            JSONArray list1 = md.MemberList(page,size,member_ID,member_name,phone,"","").getJSONArray("list");
//            会员id
            String memberId = list1.getString(2);
            String memberName = list1.getString(3);
            String memberPhone = list1.getString(4);
            checkArgument(member_ID.equals(memberId), "输入的会员id:" + member_ID + "返回的会员id"+ memberId);
            checkArgument(member_name.equals(memberName), "输入的会员姓名:" + member_name + "返回的会员姓名"+ memberName);
            checkArgument(memberPhone.equals(phone), "输入的会员电话:" + phone + "返回的会员电话"+ memberPhone);

            //获取会员列表页第一个会员的id
            JSONArray list2 = md.MemberList(page,size,"","","","","").getJSONArray("list");
            Integer id = list2.getInteger(0);


            //为了进到会员编辑页
            JSONObject res1 =  md.MemberDetail(id);
            checkArgument(res1.getInteger("code") == 1000, "查看会员详情成功");
            //编辑会员
            String Base64 = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/三分之二脸.jpg";
            String MemberId = "221122";
            String MemberName = "测试编辑A！";
            String phone = "13666666666";
            String Birthday = "1996-01-01";
            String uesrId = "000";
            JSONObject res2 = md.MemberUpdate(Base64,MemberId,MemberName,phone,Birthday,uesrId,1);
            checkArgument(res2.getInteger("code") == 1000, "编辑会员成功");


            //删除会员
            JSONArray list3 = md.MemberList(page,size,"","","","","").getJSONArray("list");
            Integer id2 = list3.getInteger(0);
            JSONObject res3 = md.MemberDelete(id2);
            checkArgument(res3.getInteger("code") == 1000, "删除会员成功");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("注册会员，并且通过搜索框搜索会员，编辑会员，删除会员");
        }
    }



//    //新建预置位、新建预置位不加名称、新建预置位不加时间、新建预置位后列表+1、删除一个预置位列表-1
//    @Test(dataProvider = "device_id",dataProviderClass = DataProviderMethod.class)
//    public void createPreset(String device_id) {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //获取预置位列表
//            JSONArray list = md.cameraList(device_id,"PRESET").getJSONArray("list");
//            int num = list.size();
//
//            //新建一个预置位
//            String name = "预置位_" + CommonUtil.getRandom(2);
//            JSONObject a = md.creatPreset(device_id,name,60);
//            String message = a.getString("message");
//            JSONObject data = a.getJSONObject("data");
//            String waring = data.getString("waring");
//            checkArgument(message.equals("success"), "创建不成功原因" + waring);
//
//
//            //获取创建预置位后的预置位列表
//            JSONArray list1 = md.cameraList(device_id,"PRESET").getJSONArray("list");
//            int num1 = list1.size();
//            int s = num1-num;
//            checkArgument(s==1, "新建一个预置位列表实际增加的" + s);
//
//
//            //创建名称为空的预置位
//            JSONObject kong = md.creatPreset(device_id,"",60);
//            String m = kong.getString("message");
//            checkArgument(m.equals("success"), "没有名称却创建成功了" + m);
//
//            //创建时间为0的预置位
//            JSONObject s1 = md.creatPreset(device_id,name,0);
//            String m2 = s1.getString("message");
//            checkArgument(m2.equals("success"), "没有时间却创建成功了" + m2);
//
//            //创建名称>20字符的预置位
//            JSONObject size = md.creatPreset(device_id,"这是名称的长度需要大于20所以她是不合格的创建不成功",60);
//            String m20 = kong.getString("message");
//            checkArgument(m20.equals("success"), "名称>20却创建成功了" + m20);
//
//
//            //获取第一个预置位并删除预置位
//            JSONArray list2 = md.cameraList(device_id,"PRESET").getJSONArray("list");
//            int num2 = list2.size();
//
//            int preset = list.getJSONObject(0).getInteger("preset_index");
//            JSONObject b = md.deletePreset(device_id,preset);
//            String message1 = b.getString("message");
//            JSONObject data1 = b.getJSONObject("data");
//            String waring1 = data1.getString("waring");
//            checkArgument(message1.equals("success"), "删除不成功原因" + waring1);
//
//            //获取删除预置位后的预置位列表
//            JSONArray list3 = md.cameraList(device_id,"PRESET").getJSONArray("list");
//            int num3 = list3.size();
//            int d = num2-num3;
//            checkArgument(d==1, "删除一个预置位列表实际减少了" + d);
//
//
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("新建预置位、新建预置位不加名称、名称过长、新建预置位不加时间、新建预置位后列表+1、删除一个预置位列表-1");
//        }
//    }
//
//
//
//    //新建看守位，可以通过dataProvider和新建预置位合在一起
//    @Test(dataProvider = "device_id",dataProviderClass = DataProviderMethod.class)
//    public void createGuard(String device_id) throws Exception{
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //获取看守位列表
//            JSONArray list = md.cameraList(device_id,"GUARD").getJSONArray("list");
//            int num = list.size();
//
//            //新建一个看守位
//            JSONObject a = md.Guard(device_id);
//            String message = a.getString("message");
//            JSONObject data = a.getJSONObject("data");
//            String waring = data.getString("waring");
//            checkArgument(message.equals("success"), "创建不成功原因" + waring);
//            //获取创建看守位后的看守位列表
//            JSONArray list1 = md.cameraList(device_id,"GUARD").getJSONArray("list");
//            int num1 = list1.size();
//            int s = num1-num;
//            checkArgument(s==1, "新建一个看守位列表实际增加的" + s);
//
//
//
//            //获取第一个预置位并删除预置位
//            JSONArray list2 = md.cameraList(device_id,"GUARD").getJSONArray("list");
//            int num2 = list2.size();
//
//            int preset = list2.getJSONObject(0).getInteger("preset_index");
//            JSONObject b = md.deleteGuard(device_id);
//            String message1 = b.getString("message");
//            JSONObject data1 = b.getJSONObject("data");
//            String waring1 = data1.getString("waring");
//            checkArgument(message1.equals("success"), "删除不成功原因" + waring1);
//
//            //获取删除预置位后的预置位列表
//            JSONArray list3 = md.cameraList(device_id,"GUARD").getJSONArray("list");
//            int num3 = list3.size();
//            int d = num2-num3;
//            checkArgument(d==1, "删除一个看守位列表实际减少了" + d);
//
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("新建看守位、新建看守位后列表+1、删除一个看守位列表-1");
//        }
//    }
//
//
//    //调用看守位
//    @Test(dataProvider = "device_id",dataProviderClass = DataProviderMethod.class)
//    public void backGuard(String device_id) throws Exception{
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONObject a = md.dyGuard(device_id);
//            String message = a.getString("message");
//            JSONObject data = a.getJSONObject("data");
//            String waring = data.getString("waring");
//            checkArgument(message.equals("success"), "调用不成功原因" + waring);
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("调用看守位 ");
//        }
//    }
//
//
//    //调用预置位
//    @Test(dataProvider = "device_id",dataProviderClass = DataProviderMethod.class)
//    public void backPreset(String device_id) throws Exception{
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //获取预置位列表
//            JSONArray list = md.cameraList(device_id,"PRESET").getJSONArray("list");
//            for(int i=0;i<=list.size();i++){
//                int index = list.getJSONObject(i).getInteger("preset_index");
//                JSONObject a = md.dyPreset(device_id,index);
//                String message = a.getString("message");
//                JSONObject data = a.getJSONObject("data");
//                String waring = data.getString("waring");
//                checkArgument(message.equals("success"), "调用不成功原因" + waring);
//                Thread.sleep(1000);
//            }
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("调用看守位 ");
//        }
//    }



}




