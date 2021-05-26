package com.haisheng.framework.testng.bigScreen.xundianDaily.zt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.MendianInfo;
import com.haisheng.framework.testng.bigScreen.xundianDaily.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.WechatScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.XundianScenarioUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import static com.google.common.base.Preconditions.checkArgument;

public class InsPcData extends TestCaseCommon implements TestCaseStd {
//    public static final Logger log = LoggerFactory.getLogger(StorePcAndAppData.class);
    public static final int page = 1;
    public static final int size = 100;
    private static final EnumTestProduce PRODUCE = EnumTestProduce.INS_DAILY;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    XundianScenarioUtil xd = XundianScenarioUtil.getInstance();
    StoreScenarioUtil md = StoreScenarioUtil.getInstance();
    WechatScenarioUtil wx = WechatScenarioUtil.getInstance();
    MendianInfo info = new MendianInfo();


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
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.INS_DAILY.getDesc());
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672", "18513118484", "18810332354", "15084928847"};
//        commonConfig.shopId = EnumTestProduce. INS_DAILY.getShopId();
        commonConfig.referer = PRODUCE.getReferer();
        beforeClassInit(commonConfig);
        logger.debug("xundian " + xd);


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
        xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);

    }


    /**
     * -------------------------------------INS相关---------------------------------------------------------------------------
     */


//添加门店、删除门店列表+1、-1ok
    @Test()
    public void shopAdd() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            //小程序初始门店数量
            JSONArray applet = wx.nearshop(null,null,116.29845,39.95933).getJSONArray("list");
            int appsize = applet.size();
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            String shopName = "两杆大烟枪啊";
            String label = "明星店";
            String openingTime = "00:00:00";
            String closingTime = "23:59:59";
            String managerName = "联系人1";
            String managerPhone = "13666666666";
            String city = "110105";
            String address = "圆明园";
            double longitude = 23.99;
            double latitude = 17.22;
            String tripartite_shop_id = "4321";
            int recommended = 70;
            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/INS.jpg";
            String base64 = MendianInfo.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
            //获得初始的门店数量
            int mdsum0 = md.searchShop(null, null, null, null, 1, 100).getInteger("total");
            md.createShop(path, shopName, label, openingTime, closingTime, managerName, managerPhone, city, address, longitude, latitude, tripartite_shop_id, recommended);
            //获得创建后门店的数量
            int mdsum1 = md.searchShop(null, null, null, null, 1, 100).getInteger("total");
            int add = mdsum1-mdsum0;
            Preconditions.checkArgument(add==1,"期望数量+1实际增加了" + add);

            //小程序
            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray applet1 = wx.nearshop(null,null,116.29845,39.95933).getJSONArray("list");
            int appletsize2 = applet1.size();
            int appletsize3 = appletsize2-appsize;
            Preconditions.checkArgument(appletsize3==1,"期望数量+1实际增加了" + add);

            //改变门店状态
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            int pages = md.searchShop(null, null, null, null, 1, 10).getInteger("pages");
            int page_size = md.searchShop(null, null, null, null, pages, 10).getInteger("page_size");
//            int t = md.searchShop(null, null, null, null, 1, 100).getInteger("total");
            int t1 = page_size-1;
            int id = md.searchShop(null, null, null, null, 1, 100).getJSONArray("list").getJSONObject(t1).getInteger("id");
            md.updateStatus(id,false);

            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray appstatus = wx.nearshop(null,null,116.29845,39.95933).getJSONArray("list");
            int statusnum1 = appstatus.size();
            int statusnum2 = appletsize2-statusnum1;
            Preconditions.checkArgument(statusnum2==1,"期望数量-1实际减少了" + statusnum2);



            //改变门店状态
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            int pages0 = md.searchShop(null, null, null, null, 1, 10).getInteger("pages");
            int page_size0 = md.searchShop(null, null, null, null, pages0, 10).getInteger("page_size");
//            int t2 = md.searchShop(null, null, null, null, 1, 100).getInteger("total");
            int t3 = page_size0-1;
            int id0 = md.searchShop(null, null, null, null, 1, 100).getJSONArray("list").getJSONObject(t3).getInteger("id");
            md.updateStatus(id0,true);

            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray appstatus0 = wx.nearshop(null,null,116.29845,39.95933).getJSONArray("list");
            int statusnum3 = appstatus0.size();
            int statusnum4 = statusnum3-statusnum1;
            Preconditions.checkArgument(statusnum4==1,"期望数量+1实际增加了" + statusnum4);

            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            int total = md.searchShop("两杆大烟枪啊", null, null, null, 1, 100).getInteger("total");
            int a = total-1;
            int id1 = md.searchShop("两杆大烟枪啊", null, null, null, 1, 100).getJSONArray("list").getJSONObject(a).getInteger("id");
            md.deleteShop(id1);
            int mdsum3 = md.searchShop(null, null, null, null, 1, 100).getInteger("total");
            int delete = mdsum1-mdsum3;
            Preconditions.checkArgument(delete==1,"期望数量-1实际减少了" + delete);
            //小程序
            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray appnum = wx.nearshop(null,null,116.29845,39.95933).getJSONArray("list");
            int appnum1 = appnum.size();
            int appnum3 = appletsize2-appnum1;
            CommonUtil.valueView(appnum1,appnum3,appletsize2);
            Preconditions.checkArgument(appnum3==1,"期望数量-1实际减少了" + appnum3);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加门店，删除门店列表+-1");
        }
    }


    //新建会员等级列表+-1ok
    @Test()
    public void create_level(){
        logger.logCaseStart(caseResult.getCaseName());
        try {

            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray wechatlist = wx.wechatlevel(null).getJSONArray("list");
            int wechatsize = wechatlist.size();

            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/INS.jpg";
            String base64 = MendianInfo.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
            int pctotal = md.member_level_page(null,1,100).getInteger("total");
            int levelenum = md.level_enum(null,null,1,10).getInteger("total");
            md.member_level_add0("等级", path, 10, "嗷嗷", "aa", 9, 10, 10, false);

            int pctotaladd = md.member_level_page(null,1,100).getInteger("total");
            int pcadd = pctotaladd-pctotal;
            Preconditions.checkArgument(pcadd==1, "期待增加1，实际" + pcadd);
            int levelnum1 = md.level_enum(null,null,1,10).getInteger("total");
            int lnum = levelnum1-levelenum;
            Preconditions.checkArgument(lnum==1, "期待增加1，实际" + lnum);
            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray wechatlist0 = wx.wechatlevel(null).getJSONArray("list");
            int wechatsize0 = wechatlist0.size();
            int wechatnum1 = wechatsize0-wechatsize;
            Preconditions.checkArgument(wechatnum1==1, "期待增加1，实际" + wechatnum1);


            //状态false
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            Integer t1 = md.member_level_page(null, 1, 10).getInteger("total");
            JSONArray list0 = md.member_level_page(null, 1, 10).getJSONArray("list");
            int a0 = t1 - 1;
            //获取刚创建等级的id
            int id0 = list0.getJSONObject(a0).getInteger("id");
            md.hide_update(null,id0,true);
            int levelnum3 = md.level_enum(null,null,1,10).getInteger("total");
            int lnum2 = levelnum1-levelnum3;
            Preconditions.checkArgument(lnum2==1, "期待减少1，实际" + lnum);

            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray wechatlist1 = wx.wechatlevel(null).getJSONArray("list");
            int wechatsize1 = wechatlist1.size();
            int wechatnum2 = wechatsize0-wechatsize1;
            Preconditions.checkArgument(wechatnum2==1, "期待减少1，实际" + wechatnum2);

            //状态true
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            Integer t2 = md.member_level_page(null, 1, 10).getInteger("total");
            JSONArray list1 = md.member_level_page(null, 1, 10).getJSONArray("list");
            int a1 = t2 - 1;
            //获取刚创建等级的id
            int id1 = list1.getJSONObject(a1).getInteger("id");
            md.hide_update(null,id1,false);
            int levelnum4 = md.level_enum(null,null,1,10).getInteger("total");
            int lnum3 = levelnum4-levelnum3;
            Preconditions.checkArgument(lnum3==1, "期待减少1，实际" + lnum3);

            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray wechatlist2 = wx.wechatlevel(null).getJSONArray("list");
            int wechatsize2 = wechatlist2.size();
            int wechatnum3 = wechatsize2-wechatsize1;
            Preconditions.checkArgument(wechatnum3==1, "期待增加1，实际" + wechatnum3);


            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            Integer total = md.member_level_page(null, 1, 10).getInteger("total");
            JSONArray list = md.member_level_page(null, 1, 10).getJSONArray("list");
            int a = total - 1;
            //获取刚创建等级的id
            int id = list.getJSONObject(a).getInteger("id");
            //删除等级
            md.member_level_delete0(id);

            Integer t3 = md.member_level_page(null, 1, 10).getInteger("total");
            int pcdelete = pctotaladd-t3;
            Preconditions.checkArgument(pcdelete==1, "期待减少1，实际" + pcdelete);
            int levelnum5 = md.level_enum(null,null,1,10).getInteger("total");
            int lnum4 = levelnum1-levelnum5;
            Preconditions.checkArgument(lnum4==1, "期待减少1，实际" + lnum4);
            //获取刚创建等级的id
            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray wechatdelete = wx.wechatlevel(null).getJSONArray("list");
            int deletenum =  wechatsize0-wechatdelete.size();
            Preconditions.checkArgument(deletenum== 1, "期待减少1，实际" + deletenum);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建会员等级列表+-1");
        }
    }


    //新增反馈类型ok
    @Test()
    public void addFeedType(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            //获取小程序所有反馈类型
            JSONArray appletlist = wx.queryAll().getJSONArray("list");
            int applistnum = appletlist.size();

            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            //获取反馈类型列表初始数量
            int feedtotal = md.feedList(null,1,100).getInteger("total");
            //获取下来框返回的数量
            JSONArray feedlist = md.feedbackTypeAll().getJSONArray("list");
            int feednum = feedlist.size();
            md.feedback_add0(EnumDesc.DESC_BETWEEN_5_10.getDesc(), EnumDesc.DESC_BETWEEN_5_10.getDesc());
            int feedtotal0 = md.feedList(null,1,100).getInteger("total");
            //获取下来框返回的数量
            JSONArray feedlist0 = md.feedbackTypeAll().getJSONArray("list");
            int feednum0 = feedlist0.size();
            int add0 = feedtotal0-feedtotal;
            int applet0 = feednum0-feednum;
            Preconditions.checkArgument(add0==1&&applet0==1, "添加了一个反馈类型期望+1,实际添加" + add0+"用户反馈下拉框期望+1，实际添加了"+applet0);

            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            //获取小程序所有反馈类型
            JSONArray appletlist0 = wx.queryAll().getJSONArray("list");
            int applistnum0 = appletlist0.size();
            int appnum = applistnum0-applistnum;
            Preconditions.checkArgument(appnum==1, "添加了一个反馈类型期望+1,实际添加" + appnum);

            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            Integer total = md.feedList(null, 1, 10).getInteger("total");
            int a = total - 1;
            int id = md.feedList(null, 1, 10).getJSONArray("list").getJSONObject(a).getInteger("id");
            md.feedback_type_delete(id);
            int deletetotal = md.feedList(null,1,100).getInteger("total");
            JSONArray deletelist = md.feedbackTypeAll().getJSONArray("list");
            int deletenum = deletelist.size();
            int delete0 =feedtotal0- deletetotal;
            int appdelete =feednum0- deletenum;
            Preconditions.checkArgument(delete0==1&&appdelete==1, "删除了一个反馈类型期望-1,实际减少" + delete0+"用户反馈下拉框期望-1，实际减少了"+appdelete);
            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            //获取小程序所有反馈类型
            JSONArray deleteletlist0 = wx.queryAll().getJSONArray("list");
            int deletelistnum0 = deleteletlist0.size();
            int deletenum0 = applistnum0-deletelistnum0;
            Preconditions.checkArgument(deletenum0==1, "删除了一个反馈类型期望-1,实际减少" + deletenum0);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新增、删除反馈类型，数量+-1");
        }

    }

    //会员提交反馈，pc反馈列表数量+1
    @Test()
    public void submitFeedback(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total = md.feedbackList(null,null,1,100).getInteger("total");
            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray querylist = wx.queryAll().getJSONArray("list");
            int feedback_type_id = querylist.getJSONObject(1).getInteger("feedback_type_id");
            wx.submitFeedback(feedback_type_id,5,"12321312");
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            int total0 = md.feedbackList(null,null,1,100).getInteger("total");
            int a = total0-total;
            CommonUtil.valueView(total,total0);
            Preconditions.checkArgument(a==1, "小程序提交了一个用户反馈，pc反馈类型数量期望+1，实际添加" + a);

            //添加礼品
            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray giflist = wx.awardFeedback(null).getJSONArray("list");
            int gifnum = giflist.size();
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            int t0 = md.feedbackList(null,null,1,100).getInteger("total");
            int aa0 = t0-1;
            int id0 = md.feedbackList(null,null,1,100).getJSONArray("list").getJSONObject(aa0).getInteger("id");
            md.addGift(id0,"阿斯顿马丁");
            String name = md.feedbackList(null,null,1,100).getJSONArray("list").getJSONObject(aa0).getString("user_name");
            String feedback_gift = md.feedbackList(null,null,1,100).getJSONArray("list").getJSONObject(aa0).getString("feedback_gift");
            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray giflist0 = wx.awardFeedback(null).getJSONArray("list");
            int gifnum0 = giflist0.size();
            //如果小程序反馈奖励已经有20条，判断最新一条是不是刚刚pc端添加的人和礼物
            if(gifnum0<20){
                int gif = gifnum0-gifnum;
                Preconditions.checkArgument(gif==1, "pc添加一个礼品期望+1，实际添加" + gif);
            }else {
                JSONArray feedlist = wx.awardFeedback(null).getJSONArray("list");
                String appletname = feedlist.getJSONObject(0).getString("user_name");
                String appletgift = feedlist.getJSONObject(0).getString("feedback_gift");
                Preconditions.checkArgument(name.equals(appletname)&&feedback_gift.equals(appletgift),"pc的用户昵称是"+name+"添加的礼物是"+feedback_gift+"小程序接受到用户昵称是"+appletname+"接受的礼物是"+appletgift);
            }

            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            int t = md.feedbackList(null,null,1,100).getInteger("total");
            int aa = t-1;
            int id = md.feedbackList(null,null,1,100).getJSONArray("list").getJSONObject(aa).getInteger("id");
            md.feedback_delete(id);
            int s = md.feedbackList(null,null,1,100).getInteger("total");
            int s1 = total0-s;
            Preconditions.checkArgument(s1 == 1, "pc删除一个反馈，pc列表期望-1，实际减少" + s1);
            if(gifnum0<20) {
                visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
                JSONArray giflist1 = wx.awardFeedback(null).getJSONArray("list");
                int gifnum1 = giflist1.size();
                int gif1 = gifnum0 - gifnum1;
                Preconditions.checkArgument(gif1 == 1, "pc删除一个反馈，小程序期望-1，实际减少" + gif1);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交一条反馈，pc用户反馈列表+1");
        }
    }

//    添加口味，删除口味
    @Test()
    public void tasteAdd(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            //获取初始口味数量
            JSONArray sortlist = wx.tasteSort(null).getJSONArray("list");
            int sortnum = sortlist.size();
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/INS.jpg";
            String base64 = MendianInfo.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
            //创建前pc口味数量
            int pctotal = md.taste_search(null,1,100).getInteger("total");
            //创建一个口味
            md.taste_add(path, path, path, "芒果", "1", 1, true);
            //创建后pc口味数量
            int pctotal0 = md.taste_search(null,1,100).getInteger("total");

            int pcsort = pctotal0-pctotal;
            Preconditions.checkArgument(pcsort == 1, "添加一个口味，列表应该增加1，实际增加" + pcsort);
            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            //创建后小程序数量
            JSONArray sortlist0 = wx.tasteSort(null).getJSONArray("list");
            int sortnum0 = sortlist0.size();
            int appsort = sortnum0-sortnum;
            Preconditions.checkArgument(appsort == 1, "添加一个口味，小程序增加1，实际增加" + appsort);


            //关闭口味
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            int update = md.taste_search(null, 1, 100).getInteger("total");
            int u = update - 1;
            int id = md.taste_search(null, 1, 10).getJSONArray("list").getJSONObject(u).getInteger("id");
            md.updateRecommend(id,false);
            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray sortlist1 = wx.tasteSort(null).getJSONArray("list");
            int sortnum1 = sortlist1.size();
            int appsort0 = sortnum0-sortnum1;
            Preconditions.checkArgument(appsort0 == 1, "关闭一个口味，小程序减少1，实际减少" + appsort0);
            //开启口味
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            md.updateRecommend(id,true);
            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray sortlist2 = wx.tasteSort(null).getJSONArray("list");
            int sortnum2 = sortlist2.size();
            int appsort2 = sortnum2-sortnum1;
            Preconditions.checkArgument(appsort2 == 1, "开启一个口味，小程序增加1，实际增加" + appsort2);


            //给此口味添加评价
            JSONArray apptaste = wx.newProduct(id).getJSONArray("list");
            int tastesize = apptaste.size();
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            //获取初始评论列表数量
            int comtotal = md.taste_search_comment(id,1,100,null).getInteger("total");
            //添加评论
            JSONArray piclist = new JSONArray();
            piclist.add(path);
            md.taste_add_comment(id,path,"1234","1234",4,true,piclist);
            int comtotal0 = md.taste_search_comment(id,1,100,null).getInteger("total");
            int comnum = comtotal0-comtotal;
            Preconditions.checkArgument(comnum == 1, "添加一个评论，pc增加1，实际增加" + comnum);
            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray apptaste0 = wx.newProduct(id).getJSONArray("list");
            int tastesize0 = apptaste0.size();
            int tastenum = tastesize0-tastesize;
            Preconditions.checkArgument(tastenum == 1, "添加一个评论，小程序增加1，实际增加" + tastenum);
//            JSONArray list = md.

            //关闭一个评论
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            int t1 = md.taste_search_comment(id,1,100,null).getInteger("total");
            int t2 = t1-1;
            int tasteid = md.taste_search_comment(id,1,100,null).getJSONArray("list").getJSONObject(t2).getInteger("id");
            md.seachCommentVisible(tasteid,false);
            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray apptaste1 = wx.newProduct(id).getJSONArray("list");
            int tastesize1 = apptaste1.size();
            int tastenum1 = tastesize0-tastesize1;
            Preconditions.checkArgument(tastenum1 == 1, "关闭一个评论，小程序减少1，实际减少" + tastenum1);

            //开启一个评论
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            md.seachCommentVisible(tasteid,true);
            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray apptaste2 = wx.newProduct(id).getJSONArray("list");
            int tastesize2 = apptaste2.size();
            int tastenum2 = tastesize2-tastesize1;
            Preconditions.checkArgument(tastenum2 == 1, "开启一个评论，小程序增加1，实际增加" + tastenum2);

            //删除一个评论
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            md.deleteComment(tasteid);
            int comtotal2 = md.taste_search_comment(id,1,100,null).getInteger("total");
            int comtotal3 = comtotal0-comtotal2;
            Preconditions.checkArgument(comtotal3 == 1, "删除一个评论，pc减少1，实际减少" + tastenum2);
            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray deletetaste = wx.newProduct(id).getJSONArray("list");
            int  deletetaste1 =  deletetaste.size();
            int  deletetaste2 = tastesize2-deletetaste1;
            Preconditions.checkArgument(deletetaste2 == 1, "删除一个评论，小程序减少1，实际减少" + deletetaste2);


            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            md.taste_delete(id).getInteger("code");
            int delete = md.taste_search(null, 1, 100).getInteger("total");
            int dd = pctotal0-delete;
            Preconditions.checkArgument(dd==1, "删除一个口味，pc减少1个实际减少" + dd);
            visitor.login(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray dlist0 = wx.tasteSort(null).getJSONArray("list");
            int num0 = dlist0.size();
            int appsd = sortnum0-num0;
            Preconditions.checkArgument(appsd == 1, "删除一个口味，小程序减少1个实际减少" + appsort);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加口味，删除口味，列表+-1");
        }
    }
//
//    //将开启的口味关闭，再见关闭的口味开启，小程序列表+-1
//    @Test()
//    public void tasteUpdate() throws Exception {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            wx.loginApplet(EnumAppletToken.INS_ZT_DAILY.getToken());
//            JSONArray list = wx.tasteSort(20).getJSONArray("list");
//            int a = list.size();
//
//            JSONArray listId = md.taste_search_comment(1,1,10,null).getJSONArray("list");
//            int id = listId.getJSONObject(0).getInteger("id");
//            String result = md.updateRecommend(id,false).getString("result");
//            Preconditions.checkArgument(result.equals("true"), "关闭口味失败" + result);
//
//            JSONArray list0 =  wx.tasteSort(20).getJSONArray("list");
//            int a1 = list.size();
//            int b = a1-a;
//            Preconditions.checkArgument(b==1, "关闭口味后小程序口味实际减少" +b );
//
//            String result0 = md.updateRecommend(id,true).getString("result");
//            Preconditions.checkArgument(result0.equals("true"), "开启口味失败" + result0);
//            JSONArray list1 = wx.tasteSort(20).getJSONArray("list");
//            int a2 = list1.size();
//            int b2 = a2-a1;
//            Preconditions.checkArgument(b2==1, "开启口味后小程序口味实际增加" + b2);
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("开启口味，关闭口味，小程序列表+-1");
//        }
//    }
//
//    @Test()
//    public void commetnAdd() throws Exception {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            wx.loginApplet(EnumAppletToken.INS_ZT_DAILY.getToken());
//            JSONArray list = md.taste_search(null,1,10).getJSONArray("list");
//            int id = list.getJSONObject(0).getInteger("id");
//            Integer total = md.taste_search_comment(id,1,10,null).getInteger("total");
//            JSONArray newProduct0 = wx.newProduct(id).getJSONArray("list");
//            int n = newProduct0.size();
//
//            //新增评论
//            String path = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/正脸.jpg";
//            List<String> pathList = new ArrayList<String>();
//            pathList.add(path);
//            String result = md.taste_add_comment(id,path,"王路飞","王路飞觉得很赞",4,true,pathList).getString("result");
//            Preconditions.checkArgument(result.equals("true"), "新增评论失败" + result);
//
//            Integer total0 = md.taste_search_comment(id,1,10,null).getInteger("total");
//            int a = total0-total;
//            Preconditions.checkArgument(a==1, "新增评论后，评价列表实际增加" + a);
//            JSONArray newProduct01 =wx.newProduct(id).getJSONArray("list");
//            int n1 = newProduct01.size();
//            int x = n1-n;
//            Preconditions.checkArgument(x==1, "新增评论后，小程序评论实际添加了" + x);
//
//            //删除
//            JSONArray tasteList =  md.taste_search_comment(id,1,10,null).getJSONArray("list");
//            int tasteId = tasteList.getJSONObject(0).getInteger("id");
//            String result0 = md.deleteComment(tasteId).getString("result");
//            Preconditions.checkArgument(result0.equals("true"), "删除评论失败" + result0);
//            Integer total1 = md.taste_search_comment(id,1,10,null).getInteger("total");
//            int b = total0-total1;
//            Preconditions.checkArgument(b==1, "删除评论后实际减少了" + b);
//
//            JSONArray newProduct =wx.newProduct(id).getJSONArray("list");
//            int c = newProduct.size();
//            int d = n-c;
//            Preconditions.checkArgument(d==1, "删除评论后小程序实际减少了" + d);
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("新增评论口味，删除口味，小程序列表+-1");
//        }
//    }
//
//    //编辑评论是否可见
//    @Test()
//    public void updateVisible() throws Exception {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            wx.loginApplet(EnumAppletToken.INS_ZT_DAILY.getToken());
//            JSONArray sortList =wx.tasteSort(20).getJSONArray("list");
//            //获取口味id
//            int sortId = sortList.getJSONObject(0).getInteger("id");
//            //获取小程序评论数量
//            int a = sortList.size();
//
//            //pc搜索评论页
//            JSONArray tastelist = md.taste_search_comment(sortId, 1, 10, null).getJSONArray("list");
//            //获取评论id
//            int visibleId = tastelist.getJSONObject(0).getInteger("id");
//            //关闭评论
//            String reslut = md.seachCommentVisible(visibleId, false).getString("result");
//            Preconditions.checkArgument(reslut.equals("true"), "评论关闭失败" + reslut);
//            JSONArray sortList0 = wx.tasteSort(20).getJSONArray("list");
//            int b = sortList0.size();
//            int c = b - a;
//            Preconditions.checkArgument(c == 1, "关闭评论后小程序实际减少了" + c);
//
//            //开启评论
//            String reslut0 = md.seachCommentVisible(visibleId, true).getString("result");
//            Preconditions.checkArgument(reslut0.equals("true"), "评论开启失败" + reslut);
//            JSONArray sortList1 =wx.tasteSort(20).getJSONArray("list");
//            int b1 = sortList1.size();
//            int c1 = b1 - b;
//            Preconditions.checkArgument(c1 == 1, "开启评论后小程序实际增加了" + c1);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("关闭评论口味，开启口味评论，小程序列表+-1");
//        }
//    }


}
