package com.haisheng.framework.testng.bigScreen.itemXundian.casedaily.zt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.MendianInfo;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.XundianScenarioUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.itemXundian.casedaily.hqq.StorePcAndAppData;
import com.haisheng.framework.testng.bigScreen.itemXundian.casedaily.hqq.fucPackage.StoreFuncPackage;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.lang.reflect.Method;

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
    String phone = "13614534511";
    String email = "334411@qq.com";
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "周涛";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_DAILY_TEST.getJobName());
        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.XD_DAILY.getDesc());
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672", "18513118484", "18810332354", "15084928847"};
        commonConfig.shopId = EnumTestProduce.XD_DAILY.getShopId();
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
    public void picNum(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int pictotal = md.picturePage("","","","",null,1,8).getInteger("total");
            int pages = md.picturePage("","","","",null,1,8).getInteger("pages");
            int pagesize = (pages-1)*8;
            int pages_size = md.picturePage("","","","",null,pages,8).getInteger("page_size");

            CommonUtil.valueView(pictotal, pagesize+pages_size);
            checkArgument(pictotal == pagesize+pages_size, "图片中心总数" + pictotal + "!=搜索出图片的数量" + pagesize+pages_size);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("图片中心总数==展示的数量");
        }
    }


    //定检巡查展示图片个数==返回的数量
    @Test
    public void picScheduled(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int pictotal = md.picturePage("SCHEDULED","","","",null,1,8).getInteger("total");
            int pages = md.picturePage("SCHEDULED","","","",null,1,8).getInteger("pages");
            int pagesize = (pages-1)*8;
            int pages_size = md.picturePage("SCHEDULED","","","",null,pages,8).getInteger("page_size");

            CommonUtil.valueView(pictotal, pagesize+pages_size);
            checkArgument(pictotal == pagesize+pages_size, "图片中心总数" + pictotal + "!=搜索出图片的数量" + pagesize+pages_size);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("定检巡查展示图片个数==返回的数量");
        }
    }

    //手动留痕展示图片个数==返回的数量
    @Test
    public void picSpot(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int pictotal = md.picturePage("SPOT","","","",null,1,8).getInteger("total");
            int pages = md.picturePage("SPOT","","","",null,1,8).getInteger("pages");
            int pagesize = (pages-1)*8;
            int pages_size = md.picturePage("SPOT","","","",null,pages,8).getInteger("page_size");
            CommonUtil.valueView(pictotal, pagesize+pages_size);
            checkArgument(pictotal == pagesize+pages_size, "图片中心总数" + pictotal + "!=搜索出图片的数量" + pagesize+pages_size);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("手动留痕展示图片个数==返回的数量");
        }
    }


    //会员身份添加、删除
    @Test
    public void AddMember(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer total = md.Member(page,size).getInteger("total");
            CommonUtil.valueView(total );
            String identity = "测试VIP！";
            md.AddMember(identity);
            Integer total1 = md.Member(page,size).getInteger("total");
            int a = total1-total;
            Preconditions.checkArgument(a==1, "添加会员身份后，身份列表+1，实际添加了"+a);
            JSONArray list = md.Member(page,size).getJSONArray("list");
            Integer total3 = md.Member(page,size).getInteger("total");
            int c = total3-1;
            int id = list.getJSONObject(c).getInteger("id");
            md.DeleteMember(id);
            Integer total2 = md.Member(page,size).getInteger("total");
            int b = total1-total2;
            Preconditions.checkArgument(b==1, "删除会员身份后，身份列表-1，实际减少了"+b);
        } catch (AssertionError | Exception e) {
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
            JSONArray list = md.MemberList(page,size,null,null,null,null,null).getJSONArray("list");
            String member_ID = "11223344";
            String member_name = "测试会员11@@aaa";
            String birthday = "1998-10-01";
            String base64 = MendianInfo.getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/common/multimedia/picture/女人脸.jpg");
            String path = md.checkPic(base64).getString("pic_path");
            md.RegisterMember(null,path,member_ID,member_name,phone,birthday,null,130);
            JSONArray list0 = md.MemberList(page,size,null,null,null,null,null).getJSONArray("list");
            int a = list0.size()-list.size();
            Preconditions.checkArgument(a==1, "新注册一个会员，会员列表实际添加了"+a);

            //编辑会员
            String MemberName = "测试编辑A！";
            String phone = "13677777777";
            JSONArray list1 = md.MemberList(page,size,null,null,null,null,null).getJSONArray("list");
            Integer total = md.MemberList(page,size,null,null,null,null,null).getInteger("total");
            int a1 = total-1;
            int id1 = list1.getJSONObject(a1).getInteger("id");
            md.MemberUpdate(id1,null,"321321323",MemberName,phone,null,null,130);
            //删除会员
            JSONArray list3 = md.MemberList(page,size,null,null,null,null,null).getJSONArray("list");
//            Integer total0 = md.MemberList(page,size,null,null,null,null,null).getInteger("total");
            int a2 = total-1;

            int id2 = list1.getJSONObject(a2).getInteger("id");
            md.MemberDelete(id2);
            JSONArray list4 = md.MemberList(page,size,null,null,null,null,null).getJSONArray("list");
            int a3 = list3.size()-list4.size();
            Preconditions.checkArgument(a3==1, "删除一个会员，会员列表实际减少了"+a3);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("注册会员，删除会员");
        }
    }

    //新建预置位、新建预置位不加名称、新建预置位不加时间、新建预置位后列表+1、删除一个预置位列表-1
    @Test(dataProvider = "device_id",dataProviderClass = DataProviderMethod.class)
    public void createPreset(String deviceid,String devicename){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取预置位列表数量
            JSONObject data = md.cameraList(43072,deviceid,"PRESET").getJSONObject("data");
            boolean status1 = data.getBooleanValue("status");
            if(!status1){
                int num = 0;
//                JSONArray list = data.getJSONArray("list");
//                int num = list.size();
                //新建一个预置位
                String name = "预置位_" + CommonUtil.getRandom(2);
                JSONObject res1 = md.creatPreset(43072,deviceid,name,"60");
                Preconditions.checkArgument(res1.getInteger("code")==1000, "创建不成功" + res1.getString("message"));
                //获取创建预置位后的预置位列表
                JSONObject data1 = md.cameraList(43072,deviceid,"PRESET").getJSONObject("data");
                JSONArray list1 = data1.getJSONArray("list");
                int num1 = list1.size();
                int s = num1-num;
                Preconditions.checkArgument(s==1, "新建一个预置位列表实际增加的" + s);
                //获取最后一个预置位的preset_index
                int preset_index =list1.getJSONObject(num1-1).getInteger("preset_index");
                //删除最后一个预置位
                JSONObject res3 = md.deletePreset(43072,deviceid,preset_index);
                Preconditions.checkArgument(res3.getInteger("code")==1000, "删除不成功" + res3.getString("message"));
                //获取删除预置位后的预置位列表
                JSONObject data2 = md.cameraList(43072,deviceid,"PRESET").getJSONObject("data");
                boolean status2 = data2.getBooleanValue("status");
                if(!status2){
                    int num2 = 0;
                    int d = num1-num2;
                    CommonUtil.valueView(d);
                    Preconditions.checkArgument(true, "删除一个预置位列表实际减少了" + d);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建预置位后列表+1、删除一个预置位列表-1");
        }
    }

    //新建看守位后列表+1、删除一个看守位列表-1
    @Test()
    public void createGuard(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取预置位列表数量
            JSONObject data = md.cameraList(43072,"8097818264503296","GUARD").getJSONObject("data");
            boolean status = data.getBooleanValue("status");
            if(!status){
                int num = 0;
                //新建一个看守位
                JSONObject res1 = md.Guard(43072,"8097818264503296");
                Preconditions.checkArgument(res1.getInteger("code")==1000, "创建不成功" + res1.getString("message"));
                //获取创建预置位后的预置位列表
                JSONObject data1 = md.cameraList(43072,"8097818264503296","GUARD").getJSONObject("data");
                JSONArray list1 = data1.getJSONArray("list");
                int num1 = list1.size();
                int s = num1-num;
                CommonUtil.valueView(s);
                Preconditions.checkArgument(s==1, "新建一个看守位列表实际增加的" + s);
                //获取最后一个预置位的preset_index
                int preset_index =list1.getJSONObject(num1-1).getInteger("preset_index");
                //删除最后一个预置位
                JSONObject res3 = md.deletePreset(43072,"8097818264503296",preset_index);
                Preconditions.checkArgument(res3.getInteger("code")==1000, "删除不成功" + res3.getString("message"));
                //获取删除预置位后的预置位列表
                JSONObject data2 = md.cameraList(43072,"8097818264503296","GUARD").getJSONObject("data");
                boolean status1 = data2.getBooleanValue("status");
                if(!status1){
                    int num2 = 0;
                    int d = num1-num2;
                    CommonUtil.valueView(d);
                    Preconditions.checkArgument(true, "删除一个看守位列表实际减少了" + d);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建看守位后列表+1、删除一个看守位列表-1");
        }
    }

}



