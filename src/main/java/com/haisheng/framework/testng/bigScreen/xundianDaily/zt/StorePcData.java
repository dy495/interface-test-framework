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
    public static final int size = 100;
    XundianScenarioUtil xd = XundianScenarioUtil.getInstance();
    StoreScenarioUtil md = StoreScenarioUtil.getInstance();
    MendianInfo info = new MendianInfo();

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


    //定检巡查展示图片类型==返回的图片类型
    @Test
    public void picScheduled1() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int pictotal = md.picturePage("SCHEDULED","","","",null,1,8).getInteger("total");
            int pages = md.picturePage("SCHEDULED","","","",null,1,8).getInteger("pages");
            for(int i=1;i<=pages;i++){
                JSONArray list = md.picturePage("SCHEDULED","","","",null,i,8).getJSONArray("list");
                for(int j=0;j<list.size();j++){
                    String tips = list.getJSONObject(j).getString("tips");
//                    int a = tips.indexOf("定检巡店");
                    checkArgument(tips.contains("定检巡店"), "定检巡查" + "!=图片返回的类型" + tips);
                }

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("定检巡查展示图片类型==返回的图片类型");
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



    //新建预置位、新建预置位不加名称、新建预置位不加时间、新建预置位后列表+1、删除一个预置位列表-1
    @Test(dataProvider = "device_id")
    public void createPreset(String device_id) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取预置位列表
            JSONArray list = md.cameraList(device_id,"PRESET").getJSONArray("list");
            int num = list.size();

            //新建一个预置位
            String name = "预置位_" + CommonUtil.getRandom(2);
            JSONObject a = md.creatPreset(device_id,name,60);
            String message = a.getString("message");
            JSONObject data = a.getJSONObject("data");
            String waring = data.getString("waring");
            checkArgument(message.equals("success"), "创建不成功原因" + waring);


            //获取创建预置位后的预置位列表
            JSONArray list1 = md.cameraList(device_id,"PRESET").getJSONArray("list");
            int num1 = list1.size();
            int s = num1-num;
            checkArgument(s==1, "新建一个预置位列表实际增加的" + s);


            //创建名称为空的预置位
            JSONObject kong = md.creatPreset(device_id,"",60);
            String m = kong.getString("message");
            checkArgument(m.equals("success"), "没有名称却创建成功了" + m);

            //创建时间为0的预置位
            JSONObject s1 = md.creatPreset(device_id,name,0);
            String m2 = s1.getString("message");
            checkArgument(m2.equals("success"), "没有时间却创建成功了" + m2);

            //创建名称>20字符的预置位
            JSONObject size = md.creatPreset(device_id,"这是名称的长度需要大于20所以她是不合格的创建不成功",60);
            String m20 = kong.getString("message");
            checkArgument(m20.equals("success"), "名称>20却创建成功了" + m20);


            //获取第一个预置位并删除预置位
            JSONArray list2 = md.cameraList(device_id,"PRESET").getJSONArray("list");
            int num2 = list2.size();

            int preset = list.getJSONObject(0).getInteger("preset_index");
            JSONObject b = md.deletePreset(device_id,preset);
            String message1 = b.getString("message");
            JSONObject data1 = b.getJSONObject("data");
            String waring1 = data1.getString("waring");
            checkArgument(message1.equals("success"), "删除不成功原因" + waring1);

            //获取删除预置位后的预置位列表
            JSONArray list3 = md.cameraList(device_id,"PRESET").getJSONArray("list");
            int num3 = list3.size();
            int d = num2-num3;
            checkArgument(d==1, "删除一个预置位列表实际减少了" + d);



        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建预置位、新建预置位不加名称、名称过长、新建预置位不加时间、新建预置位后列表+1、删除一个预置位列表-1");
        }
    }



    //新建看守位，可以通过dataProvider和新建预置位合在一起
    @Test(dataProvider = "device_id")
    public void createGuard(String device_id) throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取看守位列表
            JSONArray list = md.cameraList(device_id,"GUARD").getJSONArray("list");
            int num = list.size();

            //新建一个看守位
            JSONObject a = md.Guard(device_id);
            String message = a.getString("message");
            JSONObject data = a.getJSONObject("data");
            String waring = data.getString("waring");
            checkArgument(message.equals("success"), "创建不成功原因" + waring);
            //获取创建看守位后的看守位列表
            JSONArray list1 = md.cameraList(device_id,"GUARD").getJSONArray("list");
            int num1 = list1.size();
            int s = num1-num;
            checkArgument(s==1, "新建一个看守位列表实际增加的" + s);



            //获取第一个预置位并删除预置位
            JSONArray list2 = md.cameraList(device_id,"GUARD").getJSONArray("list");
            int num2 = list2.size();

            int preset = list2.getJSONObject(0).getInteger("preset_index");
            JSONObject b = md.deleteGuard(device_id);
            String message1 = b.getString("message");
            JSONObject data1 = b.getJSONObject("data");
            String waring1 = data1.getString("waring");
            checkArgument(message1.equals("success"), "删除不成功原因" + waring1);

            //获取删除预置位后的预置位列表
            JSONArray list3 = md.cameraList(device_id,"GUARD").getJSONArray("list");
            int num3 = list3.size();
            int d = num2-num3;
            checkArgument(d==1, "删除一个看守位列表实际减少了" + d);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建看守位、新建看守位后列表+1、删除一个看守位列表-1");
        }
    }


    //调用看守位
    @Test(dataProvider = "device_id")
    public void backGuard(String device_id) throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject a = md.dyGuard(device_id);
            String message = a.getString("message");
            JSONObject data = a.getJSONObject("data");
            String waring = data.getString("waring");
            checkArgument(message.equals("success"), "调用不成功原因" + waring);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("调用看守位 ");
        }
    }


    //调用预置位
    @Test(dataProvider = "device_id")
    public void backPreset(String device_id) throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取预置位列表
            JSONArray list = md.cameraList(device_id,"PRESET").getJSONArray("list");
            for(int i=0;i<=list.size();i++){
                int index = list.getJSONObject(i).getInteger("preset_index");
                JSONObject a = md.dyPreset(device_id,index);
                String message = a.getString("message");
                JSONObject data = a.getJSONObject("data");
                String waring = data.getString("waring");
                checkArgument(message.equals("success"), "调用不成功原因" + waring);
                Thread.sleep(1000);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("调用看守位 ");
        }
    }



    @DataProvider(name = "device_id")
    public Object[] cameraId() {

        return new String[][]{
                {"8061349193417728"},
                {"8097818264503296"},
                {"8058611994690560"}

        };
    }

}




