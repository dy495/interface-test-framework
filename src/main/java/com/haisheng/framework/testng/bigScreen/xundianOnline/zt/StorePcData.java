package com.haisheng.framework.testng.bigScreen.xundianOnline.zt;

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
import com.haisheng.framework.testng.bigScreen.xundianDaily.zt.DataProviderMethod;
import com.haisheng.framework.testng.bigScreen.xundianOnline.MendianInfoOnline;
import com.haisheng.framework.testng.bigScreen.xundianOnline.StoreScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.xundianOnline.XundianScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkArgument;

public class StorePcData extends TestCaseCommon implements TestCaseStd {

    public static final Logger log = LoggerFactory.getLogger(StorePcAndAppData.class);
    public static final int page = 1;
    public static final int size = 100;
    XundianScenarioUtilOnline xd = XundianScenarioUtilOnline.getInstance();
    StoreScenarioUtilOnline md = StoreScenarioUtilOnline.getInstance();
    MendianInfoOnline info = new MendianInfoOnline();
    String phone = "13666666666";
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "周涛";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "mendian-online-test");
        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店(巡店) 线上");
        commonConfig.dingHook = DingWebhook.ONLINE_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"13604609869", "15084928847"};
        commonConfig.shopId = getXunDianShopOnline(); //要改！！！
        beforeClassInit(commonConfig);
        logger.debug("xundian " + xd);
        xd.login("storedemo@winsense.ai","b0581aa73b04d9fe6e3057a613e6f363");

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
            int pages = md.picturePage("SPOT","2021-03-01","2021-03-13","",null,1,8).getInteger("pages");
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
    public void ReportExport() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = xd.reportList(1,100,null,null,"MONTH",null).getJSONArray("list");
            if (list.size()>0){
                int id = list.getJSONObject(0).getInteger("id");
                xd.customizeReportExport(id,null,null,null,null,null,null);
//                Preconditions.checkArgument(code==1000,"非自定义导出报表 id="+id+", 状态码"+code);
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
//    @Test
//    public void ReportExport1() throws Exception{
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONArray list = xd.reportList(1,100,null,null,"CUSTOM",null).getJSONArray("list");
//            int id = list.getJSONObject(0).getInteger("id");
//            xd.customizeReportExport(id,null,null,null,"2021-03-23","2021-03-23",null);
////                Preconditions.checkArgument(code==1000,"非自定义导出报表 id="+id+", 状态码"+code);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC自定义导出报表");
//        }
//    }

    //会员身份添加、删除
    @Test
    public void AddMember() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer total = md.Member(page,size).getInteger("total");
            CommonUtil.valueView(total );
            String identity = "测试VIP！";
            md.AddMember(identity);
////            Preconditions.checkArgument(res.getInteger("code") == 1000, "添加会员身份成功");
            Integer total1 = md.Member(page,size).getInteger("total");
            int a = total1-total;
            Preconditions.checkArgument(a==1, "添加会员身份后，身份列表+1，实际添加了"+a);
            JSONArray list = md.Member(page,size).getJSONArray("list");
            Integer total3 = md.Member(page,size).getInteger("total");
            int c = total3-1;
            int id = list.getJSONObject(c).getInteger("id");
            md.DeleteMember(id);
//            Preconditions. checkArgument(res1.getInteger("code") == 1000, "删除会员身份成功");
            Integer total2 = md.Member(page,size).getInteger("total");
            int b = total1-total2;
            Preconditions.checkArgument(b==1, "删除会员身份后，身份列表-1，实际减少了"+b);

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
    public void MemberList(String face_url) throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //通过搜索框搜索会员
            JSONArray list = md.MemberList(page,size,null,null,null,null,null).getJSONArray("list");
            String member_ID = "11223344";
            String member_name = "测试会员11@@aaa";
            String birthday = "1998-10-01";
            String base64 = info.getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/女人脸.jpg");
            String path = md.checkPic(base64).getString("pic_path");
            md.RegisterMember(null,path,member_ID,member_name,phone,birthday,null,130);

            JSONArray list0 = md.MemberList(page,size,null,null,null,null,null).getJSONArray("list");
            Integer a = list0.size()-list.size();
            Preconditions.checkArgument(a==1, "新注册一个会员，会员列表实际添加了"+a);

//            //编辑会员
            String MemberName = "测试编辑A！";
            String phone = "13677777777";
            JSONArray list1 = md.MemberList(page,size,null,null,null,null,null).getJSONArray("list");
            Integer total = md.MemberList(page,size,null,null,null,null,null).getInteger("total");
            int a1 = total-1;
            int id1 = list1.getJSONObject(a1).getInteger("id");
            md.MemberUpdate(id1,null,"321321323",MemberName,phone,null,null,130);

//
//
            //删除会员
            JSONArray list3 = md.MemberList(page,size,null,null,null,null,null).getJSONArray("list");
            Integer total0 = md.MemberList(page,size,null,null,null,null,null).getInteger("total");
            int a2 = total-1;

            int id2 = list1.getJSONObject(a2).getInteger("id");
            md.MemberDelete(id2);
            JSONArray list4 = md.MemberList(page,size,null,null,null,null,null).getJSONArray("list");
            Integer a3 = list3.size()-list4.size();
            Preconditions.checkArgument(a3==1, "删除一个会员，会员列表实际减少了"+a3);
////            Preconditions.checkArgument(res3.getInteger("code") == 1000, "删除会员失败时状态码"+res3.getInteger("code"));

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("注册会员，删除会员");
        }
    }

    //注册会员的异常情况
    @Test(dataProvider = "FACE_URL",dataProviderClass = DataProviderMethod.class)
    public void regMemError(String face_url) throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //注册会员时会员id重复也成功
            String base64 = info.getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/女人脸.jpg");
            String path = md.checkPic(base64).getString("pic_path");
            JSONObject res0 = md.RegisterMember1(null,path,"uid_2cd5f8b4","qq11啊","13656788899","1998-10-01",null,130);
            Preconditions.checkArgument(res0.getString("message").equals("当前会员ID已经存在！"), "注册会员重复会员id也成功了message"+res0.getString("message"));

            //注册会员时会员电话重复
            JSONObject res1 = md.RegisterMember1(null,path,"212","这是一个二","13604609869","1998-10-01",null,130);
            Preconditions.checkArgument(res1.getString("message").equals("当前手机号已经存在！"), "注册会员时电话重复也注册成功了返回的message"+res1.getString("message"));
//
            //注册会员时会员名称过长
            JSONObject res2 = md.RegisterMember1(null,path,"213","这是一个二十一字的名字我也不知道怎么说就是123213","13656788899","1998-10-01",null,130);
            Preconditions.checkArgument(res2.getInteger("code") != 1000, "注册会员时名字长度超过也注册成功了返回的message"+res2.getString("message"));
//
            //注册会员时电话是12位
            JSONObject res3 = md.RegisterMember1(null,path,"214","啊","136567888991","1998-10-01",null,130);
            Preconditions.checkArgument(res3.getInteger("code") != 1000, "注册会员时电话12位也注册成功了返回的message"+res3.getString("message"));
//
            //注册会员时电话是10位
            JSONObject res4 = md.RegisterMember1(null,path,"215","啊1","1365678889","1998-10-01",null,130);
            Preconditions.checkArgument(res4.getInteger("code") != 1000, "注册会员时电话10位也注册成功了返回的message"+res4.getString("message"));
//
            //注册会员时电话有英文中文特殊字符
            JSONObject res5 = md.RegisterMember1(null,face_url,"216","啊2","1365678￥啊a89","1998-10-01",null,130);
            Preconditions.checkArgument(res5.getInteger("code") != 1000, "注册会员时电话有英文等也注册成功了返回的message"+res5.getString("message"));
//
            //注册会员时选择未来日期
            JSONObject res6 = md.RegisterMember1(null,path,"217","啊3","1365678889","2998-10-01",null,130);
            Preconditions.checkArgument(res6.getInteger("code") != 1000, "注册会员时选择未来日期message"+res6.getString("message"));
//
            //注册会员时全都为空
            JSONObject res7 = md.RegisterMember1(null,null,null,null,null,null,null,0);
            Preconditions.checkArgument(res7.getInteger("code") != 1000, "注册会员时全都为空"+res7.getString("message"));
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("注册会员的异常情况");
        }
    }


    //添加会员身份的异常情况
    @Test()
    public void identity() throws Exception{
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //身份名称为空
            JSONObject res = md.AddMember1(null);
            Preconditions.checkArgument(res.getInteger("code")==1001, "身份名称为空也添加成功"+res.getInteger("code"));

            //身份名称过长
            JSONObject res1 = md.AddMember1("11111111111111111111111111111111111");
            Preconditions.checkArgument(res1.getInteger("code")==1009, "身份名称过长也添加成功"+res.getInteger("code"));

            //身份名称重复
            JSONObject res2 = md.AddMember1("VIP");
            Preconditions.checkArgument(res2.getInteger("code")==1001, "身份名称重复也添加成功"+res.getInteger("code"));
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加会员身份特殊情况");
        }
    }

//        //新建预置位、新建预置位不加名称、新建预置位不加时间、新建预置位后列表+1、删除一个预置位列表-1
//        @Test(dataProvider = "device_id",dataProviderClass = com.haisheng.framework.testng.bigScreen.xundianDaily.zt.DataProviderMethod.class)
//        public void createPreset(String device_id) {
//            logger.logCaseStart(caseResult.getCaseName());
//            try {
//                //获取预置位列表
//                JSONArray list = md.cameraList(device_id,"PRESET").getJSONArray("list");
//                int num = list.size();
//
//                //新建一个预置位
//                String name = "预置位_" + CommonUtil.getRandom(2);
//                JSONObject a = md.creatPreset(device_id,name,60);
//                String message = a.getString("message");
//                JSONObject data = a.getJSONObject("data");
//                String waring = data.getString("waring");
//                checkArgument(message.equals("success"), "创建不成功原因" + waring);
//
//
//                //获取创建预置位后的预置位列表
//                JSONArray list1 = md.cameraList(device_id,"PRESET").getJSONArray("list");
//                int num1 = list1.size();
//                int s = num1-num;
//                checkArgument(s==1, "新建一个预置位列表实际增加的" + s);
//
//
//                //创建名称为空的预置位
//                JSONObject kong = md.creatPreset(device_id,"",60);
//                String m = kong.getString("message");
//                checkArgument(m.equals("success"), "没有名称却创建成功了" + m);
//
//                //创建时间为0的预置位
//                JSONObject s1 = md.creatPreset(device_id,name,0);
//                String m2 = s1.getString("message");
//                checkArgument(m2.equals("success"), "没有时间却创建成功了" + m2);
//
//                //创建名称>20字符的预置位
//                JSONObject size = md.creatPreset(device_id,"这是名称的长度需要大于20所以她是不合格的创建不成功",60);
//                String m20 = kong.getString("message");
//                checkArgument(m20.equals("success"), "名称>20却创建成功了" + m20);
//
//
//                //获取第一个预置位并删除预置位
//                JSONArray list2 = md.cameraList(device_id,"PRESET").getJSONArray("list");
//                int num2 = list2.size();
//
//                int preset = list.getJSONObject(0).getInteger("preset_index");
//                JSONObject b = md.deletePreset(device_id,preset);
//                String message1 = b.getString("message");
//                JSONObject data1 = b.getJSONObject("data");
//                String waring1 = data1.getString("waring");
//                checkArgument(message1.equals("success"), "删除不成功原因" + waring1);
//
//                //获取删除预置位后的预置位列表
//                JSONArray list3 = md.cameraList(device_id,"PRESET").getJSONArray("list");
//                int num3 = list3.size();
//                int d = num2-num3;
//                checkArgument(d==1, "删除一个预置位列表实际减少了" + d);
//
//
//
//            } catch (AssertionError e) {
//                appendFailReason(e.toString());
//            } catch (Exception e) {
//                appendFailReason(e.toString());
//            } finally {
//                saveData("新建预置位、新建预置位不加名称、名称过长、新建预置位不加时间、新建预置位后列表+1、删除一个预置位列表-1");
//            }
//        }
//
//
//
//        //新建看守位，可以通过dataProvider和新建预置位合在一起
//        @Test(dataProvider = "device_id",dataProviderClass = com.haisheng.framework.testng.bigScreen.xundianDaily.zt.DataProviderMethod.class)
//        public void createGuard(String device_id) throws Exception{
//            logger.logCaseStart(caseResult.getCaseName());
//            try {
//                //获取看守位列表
//                JSONArray list = md.cameraList(device_id,"GUARD").getJSONArray("list");
//                int num = list.size();
//
//                //新建一个看守位
//                JSONObject a = md.Guard(device_id);
//                String message = a.getString("message");
//                JSONObject data = a.getJSONObject("data");
//                String waring = data.getString("waring");
//                checkArgument(message.equals("success"), "创建不成功原因" + waring);
//                //获取创建看守位后的看守位列表
//                JSONArray list1 = md.cameraList(device_id,"GUARD").getJSONArray("list");
//                int num1 = list1.size();
//                int s = num1-num;
//                checkArgument(s==1, "新建一个看守位列表实际增加的" + s);
//
//
//
//                //获取第一个预置位并删除预置位
//                JSONArray list2 = md.cameraList(device_id,"GUARD").getJSONArray("list");
//                int num2 = list2.size();
//
//                int preset = list2.getJSONObject(0).getInteger("preset_index");
//                JSONObject b = md.deleteGuard(device_id);
//                String message1 = b.getString("message");
//                JSONObject data1 = b.getJSONObject("data");
//                String waring1 = data1.getString("waring");
//                checkArgument(message1.equals("success"), "删除不成功原因" + waring1);
//
//                //获取删除预置位后的预置位列表
//                JSONArray list3 = md.cameraList(device_id,"GUARD").getJSONArray("list");
//                int num3 = list3.size();
//                int d = num2-num3;
//                checkArgument(d==1, "删除一个看守位列表实际减少了" + d);
//
//
//            } catch (AssertionError e) {
//                appendFailReason(e.toString());
//            } catch (Exception e) {
//                appendFailReason(e.toString());
//            } finally {
//                saveData("新建看守位、新建看守位后列表+1、删除一个看守位列表-1");
//            }
//        }
//
//
//        //调用看守位
//        @Test(dataProvider = "device_id",dataProviderClass = com.haisheng.framework.testng.bigScreen.xundianDaily.zt.DataProviderMethod.class)
//        public void backGuard(String device_id) throws Exception{
//            logger.logCaseStart(caseResult.getCaseName());
//            try {
//                JSONObject a = md.dyGuard(device_id);
//                String message = a.getString("message");
//                JSONObject data = a.getJSONObject("data");
//                String waring = data.getString("waring");
//                checkArgument(message.equals("success"), "调用不成功原因" + waring);
//            } catch (AssertionError e) {
//                appendFailReason(e.toString());
//            } catch (Exception e) {
//                appendFailReason(e.toString());
//            } finally {
//                saveData("调用看守位 ");
//            }
//        }
//
//
//        //调用预置位
//        @Test(dataProvider = "device_id",dataProviderClass = DataProviderMethod.class)
//        public void backPreset(String device_id) throws Exception{
//            logger.logCaseStart(caseResult.getCaseName());
//            try {
//                //获取预置位列表
//                JSONArray list = md.cameraList(device_id,"PRESET").getJSONArray("list");
//                for(int i=0;i<=list.size();i++){
//                    int index = list.getJSONObject(i).getInteger("preset_index");
//                    JSONObject a = md.dyPreset(device_id,index);
//                    String message = a.getString("message");
//                    JSONObject data = a.getJSONObject("data");
//                    String waring = data.getString("waring");
//                    checkArgument(message.equals("success"), "调用不成功原因" + waring);
//                    Thread.sleep(1000);
//                }
//
//            } catch (AssertionError e) {
//                appendFailReason(e.toString());
//            } catch (Exception e) {
//                appendFailReason(e.toString());
//            } finally {
//                saveData("调用看守位 ");
//            }
//        }
//
//    }

}
