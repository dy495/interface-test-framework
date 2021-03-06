package com.haisheng.framework.testng.bigScreen.itemXundian.caseonline.zt;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.MendianInfo;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.MendianInfoOnline;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.StoreScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.XundianScenarioUtilOnline;

import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.WechatScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class InsPcData extends TestCaseCommon implements TestCaseStd {
    //    public static final Logger log = LoggerFactory.getLogger(StorePcAndAppData.class);
    public static final int page = 1;
    public static final int size = 100;
    private static final EnumTestProduct PRODUCE = EnumTestProduct.INS_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    XundianScenarioUtilOnline xd = XundianScenarioUtilOnline.getInstance();
    StoreScenarioUtilOnline md = StoreScenarioUtilOnline.getInstance();
    WechatScenarioUtilOnline wx = WechatScenarioUtilOnline.getInstance();
    MendianInfoOnline info = new MendianInfoOnline();


    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "??????";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_ONLINE_TEST.getJobName());
        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduct.INS_ONLINE.getDesc());
        commonConfig.dingHook = DingWebhook.ONLINE_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"13604609869", "15084928847"};
//        commonConfig.shopId = EnumTestProduce. INS_DAILY.getShopId();
        commonConfig.setReferer(PRODUCE.getReferer()).setRoleId(PRODUCE.getRoleId()).setProduct(PRODUCE.getAbbreviation());
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
        xd.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);

    }


    /**
     * -------------------------------------INS??????---------------------------------------------------------------------------
     */


//?????????????????????????????????+1???-1ok
    @Test()
    public void shopAdd() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            //???????????????????????????
            JSONArray applet = wx.nearshop(null, null, 116.29845, 39.95933).getJSONArray("list");
            int appsize = applet.size();
            xd.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
            String shopName = "???????????????";
            String label = "?????????";
            String openingTime = "00:00:00";
            String closingTime = "23:59:59";
            String managerName = "?????????1";
            String managerPhone = "13666666666";
            String city = "110105";
            String address = "?????????";
            double longitude = 23.99;
            double latitude = 17.22;
            String tripartite_shop_id = "4321";
            int recommended = 70;
            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/common/multimedia/picture/INS.jpg";
            String base64 = MendianInfoOnline.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
            //???????????????????????????
            int mdsum0 = md.searchShop(null, null, null, null, 1, 100).getInteger("total");
            md.createShop(path, shopName, label, openingTime, closingTime, managerName, managerPhone, city, address, longitude, latitude, tripartite_shop_id, recommended);
            //??????????????????????????????
            int mdsum1 = md.searchShop(null, null, null, null, 1, 100).getInteger("total");
            int add = mdsum1 - mdsum0;
            Preconditions.checkArgument(add == 1, "????????????+1???????????????" + add);

            //?????????
            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            JSONArray applet1 = wx.nearshop(null, null, 116.29845, 39.95933).getJSONArray("list");
            int appletsize2 = applet1.size();
            int appletsize3 = appletsize2 - appsize;
            Preconditions.checkArgument(appletsize3 == 1, "????????????+1???????????????" + add);

            //??????????????????
            xd.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
            int t = md.searchShop(null, null, null, null, 1, 100).getInteger("total");
            int t1 = t - 1;
            int id = md.searchShop(null, null, null, null, 1, 100).getJSONArray("list").getJSONObject(t1).getInteger("id");
            md.updateStatus(id, false);

            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            JSONArray appstatus = wx.nearshop(null, null, 116.29845, 39.95933).getJSONArray("list");
            int statusnum1 = appstatus.size();
            int statusnum2 = appletsize2 - statusnum1;
            Preconditions.checkArgument(statusnum2 == 1, "????????????-1???????????????" + statusnum2);


            //??????????????????
            xd.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
            int t2 = md.searchShop(null, null, null, null, 1, 100).getInteger("total");
            int t3 = t2 - 1;
            int id0 = md.searchShop(null, null, null, null, 1, 100).getJSONArray("list").getJSONObject(t3).getInteger("id");
            md.updateStatus(id0, true);

            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            JSONArray appstatus0 = wx.nearshop(null, null, 116.29845, 39.95933).getJSONArray("list");
            int statusnum3 = appstatus0.size();
            int statusnum4 = statusnum3 - statusnum1;
            Preconditions.checkArgument(statusnum4 == 1, "????????????+1???????????????" + statusnum4);

            xd.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
            int total = md.searchShop("???????????????", null, null, null, 1, 100).getInteger("total");
            int a = total - 1;
            int id1 = md.searchShop("???????????????", null, null, null, 1, 100).getJSONArray("list").getJSONObject(a).getInteger("id");
            md.deleteShop(id1);
            int mdsum3 = md.searchShop(null, null, null, null, 1, 100).getInteger("total");
            int delete = mdsum1 - mdsum3;
            Preconditions.checkArgument(delete == 1, "????????????-1???????????????" + delete);
            //?????????
            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            JSONArray appnum = wx.nearshop(null, null, 116.29845, 39.95933).getJSONArray("list");
            int appnum1 = appnum.size();
            int appnum3 = appletsize2 - appnum1;
            CommonUtil.valueView(appnum1, appnum3, appletsize2);
            Preconditions.checkArgument(appnum3 == 1, "????????????-1???????????????" + appnum3);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("?????????????????????????????????+-1");
        }
    }


    //????????????????????????+-1ok
    @Test()
    public void create_level() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            visitor.setToken(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray wechatlist = wx.wechatlevel(null).getJSONArray("list");
            int wechatsize = wechatlist.size();

            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/common/multimedia/picture/INS.jpg";
            String base64 = MendianInfo.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
            int pctotal = md.member_level_page(null, 1, 100).getInteger("total");
            int levelenum = md.level_enum(null, null, 1, 10).getInteger("total");
            md.member_level_add0("??????", path, 10, "??????", "aa", 9, 10, 10, false);

            int pctotaladd = md.member_level_page(null, 1, 100).getInteger("total");
            int pcadd = pctotaladd - pctotal;
            Preconditions.checkArgument(pcadd == 1, "????????????1?????????" + pcadd);
            int levelnum1 = md.level_enum(null, null, 1, 10).getInteger("total");
            int lnum = levelnum1 - levelenum;
            Preconditions.checkArgument(lnum == 1, "????????????1?????????" + lnum);
            visitor.setToken(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray wechatlist0 = wx.wechatlevel(null).getJSONArray("list");
            int wechatsize0 = wechatlist0.size();
            int wechatnum1 = wechatsize0 - wechatsize;
            Preconditions.checkArgument(wechatnum1 == 1, "????????????1?????????" + wechatnum1);


            //??????false
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            Integer t1 = md.member_level_page(null, 1, 10).getInteger("total");
            JSONArray list0 = md.member_level_page(null, 1, 10).getJSONArray("list");
            int a0 = t1 - 1;
            //????????????????????????id
            int id0 = list0.getJSONObject(a0).getInteger("id");
            md.hide_update(null, id0, true);
            int levelnum3 = md.level_enum(null, null, 1, 10).getInteger("total");
            int lnum2 = levelnum1 - levelnum3;
            Preconditions.checkArgument(lnum2 == 1, "????????????1?????????" + lnum);

            visitor.setToken(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray wechatlist1 = wx.wechatlevel(null).getJSONArray("list");
            int wechatsize1 = wechatlist1.size();
            int wechatnum2 = wechatsize0 - wechatsize1;
            Preconditions.checkArgument(wechatnum2 == 1, "????????????1?????????" + wechatnum2);

            //??????true
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            Integer t2 = md.member_level_page(null, 1, 10).getInteger("total");
            JSONArray list1 = md.member_level_page(null, 1, 10).getJSONArray("list");
            int a1 = t2 - 1;
            //????????????????????????id
            int id1 = list1.getJSONObject(a1).getInteger("id");
            md.hide_update(null, id1, false);
            int levelnum4 = md.level_enum(null, null, 1, 10).getInteger("total");
            int lnum3 = levelnum4 - levelnum3;
            Preconditions.checkArgument(lnum3 == 1, "????????????1?????????" + lnum3);

            visitor.setToken(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray wechatlist2 = wx.wechatlevel(null).getJSONArray("list");
            int wechatsize2 = wechatlist2.size();
            int wechatnum3 = wechatsize2 - wechatsize1;
            Preconditions.checkArgument(wechatnum3 == 1, "????????????1?????????" + wechatnum3);


            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            Integer total = md.member_level_page(null, 1, 10).getInteger("total");
            JSONArray list = md.member_level_page(null, 1, 10).getJSONArray("list");
            int a = total - 1;
            //????????????????????????id
            int id = list.getJSONObject(a).getInteger("id");
            //????????????
            md.member_level_delete0(id);

            Integer t3 = md.member_level_page(null, 1, 10).getInteger("total");
            int pcdelete = pctotaladd - t3;
            Preconditions.checkArgument(pcdelete == 1, "????????????1?????????" + pcdelete);
            int levelnum5 = md.level_enum(null, null, 1, 10).getInteger("total");
            int lnum4 = levelnum1 - levelnum5;
            Preconditions.checkArgument(lnum4 == 1, "????????????1?????????" + lnum4);
            //????????????????????????id
            visitor.setToken(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray wechatdelete = wx.wechatlevel(null).getJSONArray("list");
            int deletenum = wechatsize0 - wechatdelete.size();
            Preconditions.checkArgument(deletenum == 1, "????????????1?????????" + deletenum);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("????????????????????????+-1");
        }
    }


    //??????????????????ok
    @Test()
    public void addFeedType() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            //?????????????????????????????????
            JSONArray appletlist = wx.queryAll().getJSONArray("list");
            int applistnum = appletlist.size();

            xd.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
            //????????????????????????????????????
            int feedtotal = md.feedList(null, 1, 100).getInteger("total");
            //??????????????????????????????
            JSONArray feedlist = md.feedbackTypeAll().getJSONArray("list");
            int feednum = feedlist.size();
            md.feedback_add0(EnumDesc.DESC_BETWEEN_5_10.getDesc(), EnumDesc.DESC_BETWEEN_5_10.getDesc());
            int feedtotal0 = md.feedList(null, 1, 100).getInteger("total");
            //??????????????????????????????
            JSONArray feedlist0 = md.feedbackTypeAll().getJSONArray("list");
            int feednum0 = feedlist0.size();
            int add0 = feedtotal0 - feedtotal;
            int applet0 = feednum0 - feednum;
            Preconditions.checkArgument(add0 == 1 && applet0 == 1, "?????????????????????????????????+1,????????????" + add0 + "???????????????????????????+1??????????????????" + applet0);

            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            //?????????????????????????????????
            JSONArray appletlist0 = wx.queryAll().getJSONArray("list");
            int applistnum0 = appletlist0.size();
            int appnum = applistnum0 - applistnum;
            Preconditions.checkArgument(appnum == 1, "?????????????????????????????????+1,????????????" + appnum);

            xd.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
            Integer total = md.feedList(null, 1, 10).getInteger("total");
            int a = total - 1;
            int id = md.feedList(null, 1, 10).getJSONArray("list").getJSONObject(a).getInteger("id");
            md.feedback_type_delete(id);
            int deletetotal = md.feedList(null, 1, 100).getInteger("total");
            JSONArray deletelist = md.feedbackTypeAll().getJSONArray("list");
            int deletenum = deletelist.size();
            int delete0 = feedtotal0 - deletetotal;
            int appdelete = feednum0 - deletenum;
            Preconditions.checkArgument(delete0 == 1 && appdelete == 1, "?????????????????????????????????-1,????????????" + delete0 + "???????????????????????????-1??????????????????" + appdelete);
            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            //?????????????????????????????????
            JSONArray deleteletlist0 = wx.queryAll().getJSONArray("list");
            int deletelistnum0 = deleteletlist0.size();
            int deletenum0 = applistnum0 - deletelistnum0;
            Preconditions.checkArgument(deletenum0 == 1, "?????????????????????????????????-1,????????????" + deletenum0);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("????????????????????????????????????+-1");
        }

    }

    //?????????????????????pc??????????????????+1
    @Test()
    public void submitFeedback() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total = md.feedbackList(null, null, 1, 100).getInteger("total");
            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            JSONArray querylist = wx.queryAll().getJSONArray("list");
            int feedback_type_id = querylist.getJSONObject(1).getInteger("feedback_type_id");
            wx.submitFeedback(feedback_type_id, 5, "12321312");
            xd.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
            int total0 = md.feedbackList(null, null, 1, 100).getInteger("total");
            int a = total0 - total;
            CommonUtil.valueView(total, total0);
            Preconditions.checkArgument(a == 1, "???????????????????????????????????????pc????????????????????????+1???????????????" + a);

            //????????????
            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            JSONArray giflist = wx.awardFeedback(null).getJSONArray("list");
            int gifnum = giflist.size();
            xd.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
            int t0 = md.feedbackList(null, null, 1, 100).getInteger("total");
            int aa0 = t0 - 1;
            int id0 = md.feedbackList(null, null, 1, 100).getJSONArray("list").getJSONObject(aa0).getInteger("id");
            md.addGift(id0, "???????????????");
            String name = md.feedbackList(null, null, 1, 100).getJSONArray("list").getJSONObject(aa0).getString("user_name");
            String feedback_gift = md.feedbackList(null, null, 1, 100).getJSONArray("list").getJSONObject(aa0).getString("feedback_gift");
            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            JSONArray giflist0 = wx.awardFeedback(null).getJSONArray("list");
            int gifnum0 = giflist0.size();
            //????????????????????????????????????20???????????????????????????????????????pc????????????????????????
            if (gifnum0 < 20) {
                int gif = gifnum0 - gifnum;
                Preconditions.checkArgument(gif == 1, "pc????????????????????????+1???????????????" + gif);
            } else {
                JSONArray feedlist = wx.awardFeedback(null).getJSONArray("list");
                String appletname = feedlist.getJSONObject(0).getString("user_name");
                String appletgift = feedlist.getJSONObject(0).getString("feedback_gift");
                Preconditions.checkArgument(name.equals(appletname) && feedback_gift.equals(appletgift), "pc??????????????????" + name + "??????????????????" + feedback_gift + "?????????????????????????????????" + appletname + "??????????????????" + appletgift);
            }

            xd.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
            int t = md.feedbackList(null, null, 1, 100).getInteger("total");
            int aa = t - 1;
            int id = md.feedbackList(null, null, 1, 100).getJSONArray("list").getJSONObject(aa).getInteger("id");
            md.feedback_delete(id);
            int s = md.feedbackList(null, null, 1, 100).getInteger("total");
            int s1 = total0 - s;
            Preconditions.checkArgument(s1 == 1, "pc?????????????????????pc????????????-1???????????????" + s1);
            if (gifnum0 < 20) {
                visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
                JSONArray giflist1 = wx.awardFeedback(null).getJSONArray("list");
                int gifnum1 = giflist1.size();
                int gif1 = gifnum0 - gifnum1;
                Preconditions.checkArgument(gif1 == 1, "pc????????????????????????????????????-1???????????????" + gif1);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????????????????pc??????????????????+1");
        }
    }

    //    ???????????????????????????
    @Test()
    public void tasteAdd() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            //????????????????????????
            JSONArray sortlist = wx.tasteSort(null).getJSONArray("list");
            int sortnum = sortlist.size();
            xd.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/common/multimedia/picture/INS.jpg";
            String base64 = MendianInfoOnline.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
            //?????????pc????????????
            int pctotal = md.taste_search(null, 1, 100).getInteger("total");
            //??????????????????
            md.taste_add(path, path, path, "??????", "1", 1, true);
            //?????????pc????????????
            int pctotal0 = md.taste_search(null, 1, 100).getInteger("total");

            int pcsort = pctotal0 - pctotal;
            Preconditions.checkArgument(pcsort == 1, "???????????????????????????????????????1???????????????" + pcsort);
            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            //????????????????????????
            JSONArray sortlist0 = wx.tasteSort(null).getJSONArray("list");
            int sortnum0 = sortlist0.size();
            int appsort = sortnum0 - sortnum;
            Preconditions.checkArgument(appsort == 1, "????????????????????????????????????1???????????????" + appsort);


            //????????????
            xd.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
            int update = md.taste_search(null, 1, 100).getInteger("total");
            int u = update - 1;
            int id = md.taste_search(null, 1, 10).getJSONArray("list").getJSONObject(u).getInteger("id");
            md.updateRecommend(id, false);
            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            JSONArray sortlist1 = wx.tasteSort(null).getJSONArray("list");
            int sortnum1 = sortlist1.size();
            int appsort0 = sortnum0 - sortnum1;
            Preconditions.checkArgument(appsort0 == 1, "????????????????????????????????????1???????????????" + appsort0);
            //????????????
            xd.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
            md.updateRecommend(id, true);
            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            JSONArray sortlist2 = wx.tasteSort(null).getJSONArray("list");
            int sortnum2 = sortlist2.size();
            int appsort2 = sortnum2 - sortnum1;
            Preconditions.checkArgument(appsort2 == 1, "????????????????????????????????????1???????????????" + appsort2);


            //????????????????????????
            JSONArray apptaste = wx.newProduct(id).getJSONArray("list");
            int tastesize = apptaste.size();
            xd.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
            //??????????????????????????????
            int comtotal = md.taste_search_comment(id, 1, 100, null).getInteger("total");
            //????????????
            JSONArray piclist = new JSONArray();
            piclist.add(path);
            md.taste_add_comment(id, path, "1234", "1234", 4, true, piclist);
            int comtotal0 = md.taste_search_comment(id, 1, 100, null).getInteger("total");
            int comnum = comtotal0 - comtotal;
            Preconditions.checkArgument(comnum == 1, "?????????????????????pc??????1???????????????" + comnum);
            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            JSONArray apptaste0 = wx.newProduct(id).getJSONArray("list");
            int tastesize0 = apptaste0.size();
            int tastenum = tastesize0 - tastesize;
            Preconditions.checkArgument(tastenum == 1, "????????????????????????????????????1???????????????" + tastenum);
//            JSONArray list = md.

            //??????????????????
            xd.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
            int t1 = md.taste_search_comment(id, 1, 100, null).getInteger("total");
            int t2 = t1 - 1;
            int tasteid = md.taste_search_comment(id, 1, 100, null).getJSONArray("list").getJSONObject(t2).getInteger("id");
            md.seachCommentVisible(tasteid, false);
            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            JSONArray apptaste1 = wx.newProduct(id).getJSONArray("list");
            int tastesize1 = apptaste1.size();
            int tastenum1 = tastesize0 - tastesize1;
            Preconditions.checkArgument(tastenum1 == 1, "????????????????????????????????????1???????????????" + tastenum1);

            //??????????????????
            xd.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
            md.seachCommentVisible(tasteid, true);
            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            JSONArray apptaste2 = wx.newProduct(id).getJSONArray("list");
            int tastesize2 = apptaste2.size();
            int tastenum2 = tastesize2 - tastesize1;
            Preconditions.checkArgument(tastenum2 == 1, "????????????????????????????????????1???????????????" + tastenum2);

            //??????????????????
            xd.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
            md.deleteComment(tasteid);
            int comtotal2 = md.taste_search_comment(id, 1, 100, null).getInteger("total");
            int comtotal3 = comtotal0 - comtotal2;
            Preconditions.checkArgument(comtotal3 == 1, "?????????????????????pc??????1???????????????" + tastenum2);
            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            JSONArray deletetaste = wx.newProduct(id).getJSONArray("list");
            int deletetaste1 = deletetaste.size();
            int deletetaste2 = tastesize2 - deletetaste1;
            Preconditions.checkArgument(deletetaste2 == 1, "????????????????????????????????????1???????????????" + deletetaste2);


            xd.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
            md.taste_delete(id).getInteger("code");
            int delete = md.taste_search(null, 1, 100).getInteger("total");
            int dd = pctotal0 - delete;
            Preconditions.checkArgument(dd == 1, "?????????????????????pc??????1???????????????" + dd);
            visitor.setToken(EnumAppletToken.INS_ZT_ONLINE.getToken());
            JSONArray dlist0 = wx.tasteSort(null).getJSONArray("list");
            int num0 = dlist0.size();
            int appsd = sortnum0 - num0;
            Preconditions.checkArgument(appsd == 1, "????????????????????????????????????1???????????????" + appsort);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("????????????????????????????????????+-1");
        }
    }
//
//    //????????????????????????????????????????????????????????????????????????+-1
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
//            Preconditions.checkArgument(result.equals("true"), "??????????????????" + result);
//
//            JSONArray list0 =  wx.tasteSort(20).getJSONArray("list");
//            int a1 = list.size();
//            int b = a1-a;
//            Preconditions.checkArgument(b==1, "??????????????????????????????????????????" +b );
//
//            String result0 = md.updateRecommend(id,true).getString("result");
//            Preconditions.checkArgument(result0.equals("true"), "??????????????????" + result0);
//            JSONArray list1 = wx.tasteSort(20).getJSONArray("list");
//            int a2 = list1.size();
//            int b2 = a2-a1;
//            Preconditions.checkArgument(b2==1, "??????????????????????????????????????????" + b2);
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("?????????????????????????????????????????????+-1");
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
//            //????????????
//            String path = "src/main/java/com/haisheng/framework/testng/bigScreen/itemXundian/common/multimedia/picture/??????.jpg";
//            List<String> pathList = new ArrayList<String>();
//            pathList.add(path);
//            String result = md.taste_add_comment(id,path,"?????????","?????????????????????",4,true,pathList).getString("result");
//            Preconditions.checkArgument(result.equals("true"), "??????????????????" + result);
//
//            Integer total0 = md.taste_search_comment(id,1,10,null).getInteger("total");
//            int a = total0-total;
//            Preconditions.checkArgument(a==1, "??????????????????????????????????????????" + a);
//            JSONArray newProduct01 =wx.newProduct(id).getJSONArray("list");
//            int n1 = newProduct01.size();
//            int x = n1-n;
//            Preconditions.checkArgument(x==1, "????????????????????????????????????????????????" + x);
//
//            //??????
//            JSONArray tasteList =  md.taste_search_comment(id,1,10,null).getJSONArray("list");
//            int tasteId = tasteList.getJSONObject(0).getInteger("id");
//            String result0 = md.deleteComment(tasteId).getString("result");
//            Preconditions.checkArgument(result0.equals("true"), "??????????????????" + result0);
//            Integer total1 = md.taste_search_comment(id,1,10,null).getInteger("total");
//            int b = total0-total1;
//            Preconditions.checkArgument(b==1, "??????????????????????????????" + b);
//
//            JSONArray newProduct =wx.newProduct(id).getJSONArray("list");
//            int c = newProduct.size();
//            int d = n-c;
//            Preconditions.checkArgument(d==1, "???????????????????????????????????????" + d);
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("???????????????????????????????????????????????????+-1");
//        }
//    }
//
//    //????????????????????????
//    @Test()
//    public void updateVisible() throws Exception {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            wx.loginApplet(EnumAppletToken.INS_ZT_DAILY.getToken());
//            JSONArray sortList =wx.tasteSort(20).getJSONArray("list");
//            //????????????id
//            int sortId = sortList.getJSONObject(0).getInteger("id");
//            //???????????????????????????
//            int a = sortList.size();
//
//            //pc???????????????
//            JSONArray tastelist = md.taste_search_comment(sortId, 1, 10, null).getJSONArray("list");
//            //????????????id
//            int visibleId = tastelist.getJSONObject(0).getInteger("id");
//            //????????????
//            String reslut = md.seachCommentVisible(visibleId, false).getString("result");
//            Preconditions.checkArgument(reslut.equals("true"), "??????????????????" + reslut);
//            JSONArray sortList0 = wx.tasteSort(20).getJSONArray("list");
//            int b = sortList0.size();
//            int c = b - a;
//            Preconditions.checkArgument(c == 1, "???????????????????????????????????????" + c);
//
//            //????????????
//            String reslut0 = md.seachCommentVisible(visibleId, true).getString("result");
//            Preconditions.checkArgument(reslut0.equals("true"), "??????????????????" + reslut);
//            JSONArray sortList1 =wx.tasteSort(20).getJSONArray("list");
//            int b1 = sortList1.size();
//            int c1 = b1 - b;
//            Preconditions.checkArgument(c1 == 1, "???????????????????????????????????????" + c1);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("?????????????????????????????????????????????????????????+-1");
//        }
//    }


}
