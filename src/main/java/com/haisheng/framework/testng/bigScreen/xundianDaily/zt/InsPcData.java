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
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.MendianInfo;
import com.haisheng.framework.testng.bigScreen.xundianDaily.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.WechatScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.XundianScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.StorePcAndAppData;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.fucPackage.StoreFuncPackage;
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
        commonConfig.shopId = EnumTestProduce. INS_DAILY.getShopId();
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


    /**
     * -------------------------------------INS相关---------------------------------------------------------------------------
     */


//添加门店、删除门店列表+1、-1
    @Test()
    public void shopAdd() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            double longitude = 54.2315123451324134;
            double latitude = 11.3214213532452345;
            String phone = "13666666666";
            String pic = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/INS.jpg";
            String base64 = info.getImgStr(pic);
            String path = md.pcFileUpload(base64).getString("pic_path");
            String shopName = "INS门店1";
            String label = "这是一家门店";
            //登录小程序
            wx.loginApplet(EnumAppletToken.INS_ZT_DAILY.getToken());
            //获取初始pc门店数量
            JSONArray shop_list = md.searchShop(null,null,null,null,1,10).getJSONArray("shop_list");
            int a = shop_list.size();
            //获取初始小程序门店数量
            JSONArray list0 = wx.nearShops(null,longitude,latitude).getJSONArray("list");
            int a0 = list0.size();
            //添加一个门店
            String result = md.createShop(path, shopName, label, "00:00", "23:00", "hh", phone, "北京", "中关村soho", longitude, latitude, "123", 20).getString("result");
            Preconditions.checkArgument(result.equals("true"), "新建门店不成功" + result);
            //创建后的门店列表
            JSONArray shop_list0 = md.getAuthI_shopId().getJSONArray("shop_list");
            int b = shop_list0.size();
            int s = b - a;
            Preconditions.checkArgument(s == 1, "新建门店成功后列表实际添加了" + s);
            //创建后的小程序门店列表
            JSONArray list1 = wx.nearShops(null,longitude,latitude).getJSONArray("list");
            int a1 = list1.size();
            int x = a1-a0;
            Preconditions.checkArgument(x == 1, "新建门店成功后小程序实际添加了" + x);
            //搜索门店
            JSONArray list2 = md.searchShop(null,null,null,null,1,10).getJSONArray("list");
            int id = list2.getJSONObject(0).getInteger("id");
            //删除一门店
            String result1 = md.deleteShop(id).getString("result");
            Preconditions.checkArgument(result1.equals("true"), "删除门店不成功" + result1);

            //pc端
            JSONArray shop_list1 = md.getAuthI_shopId().getJSONArray("shop_list");
            int a2 = shop_list1.size();
            int c1 = b-a2;
            Preconditions.checkArgument(c1 == 1, "删除门店成功pc实际减少了" + c1);
            //小程序端
            JSONArray list3 = wx.nearShops(null,longitude,latitude).getJSONArray("list");
            int a3 = list3.size();
            int c2 = a1-a3;
            Preconditions.checkArgument(c2 == 1, "删除门店成功后小程序实际减少了" + c1);

            //pc编辑将开启的门店关闭，小程序附近门店-1
            String result3 = md.updateStatus(id,false).getString("result");
            Preconditions.checkArgument(result3.equals("true"), "门店状态关闭失败" + result3);
            JSONArray list4 = wx.nearShops(null,longitude,latitude).getJSONArray("list");
            int a4 = list4.size();
            int c3 = a0-a4;
            Preconditions.checkArgument(c3 == 1, "关闭门店成功后小程序实际减少了" + c3);

            String result4 = md.updateStatus(id,true).getString("result");
            Preconditions.checkArgument(result4.equals("true"), "门店状态开启失败" + result4);
            JSONArray list5 = wx.nearShops(null,longitude,latitude).getJSONArray("list");
            int a5 = list5.size();
            int c4 = a5-a0;
            Preconditions.checkArgument(c4 == 1, "关闭门店成功后小程序实际减少了" + c4);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加门店，删除门店列表+-1");
        }
    }

    //添加口味，删除口味
    @Test()
    public void tasteAdd() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            wx.loginApplet(EnumAppletToken.INS_ZT_DAILY.getToken());
            //pc口味列表
            JSONArray list0 = md.taste_search(null,1,10).getJSONArray("list");
            int a0 = list0.size();
            //小程序口味列表
            JSONArray list1 = wx.tasteSort(20).getJSONArray("list");
            int a1 = list1.size();
            //新增口味
            String path = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/INS1.jpg";
            String path1 = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/女人脸.jpg";
            String result = md.taste_add(path,path1,path,"苹果味","苹果味粉好",10000,true).getString("result");
            Preconditions.checkArgument(result.equals("true"), "新增口味失败" + result);

            JSONArray list3 = md.taste_search(null,1,10).getJSONArray("list");
            int b0 = list3.size();
            int c0 = b0-a0;
            Preconditions.checkArgument(c0==1, "新增口味后列表实际添加了" + c0);

            JSONArray list4 = wx.tasteSort(20).getJSONArray("list");
            int b1 = list4.size();
            int c1 = b1-a1;
            Preconditions.checkArgument(c1==1, "新增口味后小程序实际添加了" + c1);

            //获取口味id
            JSONArray idList = md.taste_search("苹果味",1,10).getJSONArray("list");
            int tast_id = idList.getJSONObject(0).getInteger("id");
            //删除口味
            String result0 = md.taste_delete(tast_id).getString("result");
            Preconditions.checkArgument(result0.equals("true"), "删除失败" +result0);

            JSONArray list5 = md.taste_search(null,1,10).getJSONArray("list");
            int b2 = list5.size();
            int c2 = b2-b0;
            Preconditions.checkArgument(c2==1, "删除口味后小程序实际减少了" + c2);

            JSONArray list6 = wx.tasteSort(20).getJSONArray("list");
            int b3 = list6.size();
            int c3 = b3-b1;
            Preconditions.checkArgument(c3==1, "删除口味后小程序实际减少了" + c3);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加口味，删除口味，列表+-1");
        }
    }

    //将开启的口味关闭，再见关闭的口味开启，小程序列表+-1
    @Test()
    public void tasteUpdate() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            wx.loginApplet(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray list = wx.tasteSort(20).getJSONArray("list");
            int a = list.size();

            JSONArray listId = md.taste_search_comment(1,1,10,null).getJSONArray("list");
            int id = listId.getJSONObject(0).getInteger("id");
            String result = md.updateRecommend(id,false).getString("result");
            Preconditions.checkArgument(result.equals("true"), "关闭口味失败" + result);

            JSONArray list0 =  wx.tasteSort(20).getJSONArray("list");
            int a1 = list.size();
            int b = a1-a;
            Preconditions.checkArgument(b==1, "关闭口味后小程序口味实际减少" +b );

            String result0 = md.updateRecommend(id,true).getString("result");
            Preconditions.checkArgument(result0.equals("true"), "开启口味失败" + result0);
            JSONArray list1 = wx.tasteSort(20).getJSONArray("list");
            int a2 = list1.size();
            int b2 = a2-a1;
            Preconditions.checkArgument(b2==1, "开启口味后小程序口味实际增加" + b2);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("开启口味，关闭口味，小程序列表+-1");
        }
    }

    @Test()
    public void commetnAdd() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            wx.loginApplet(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray list = md.taste_search(null,1,10).getJSONArray("list");
            int id = list.getJSONObject(0).getInteger("id");
            Integer total = md.taste_search_comment(id,1,10,null).getInteger("total");
            JSONArray newProduct0 = wx.newProduct(id).getJSONArray("list");
            int n = newProduct0.size();

            //新增评论
            String path = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/正脸.jpg";
            List<String> pathList = new ArrayList<String>();
            pathList.add(path);
            String result = md.taste_add_comment(id,path,"王路飞","王路飞觉得很赞",4,true,pathList).getString("result");
            Preconditions.checkArgument(result.equals("true"), "新增评论失败" + result);

            Integer total0 = md.taste_search_comment(id,1,10,null).getInteger("total");
            int a = total0-total;
            Preconditions.checkArgument(a==1, "新增评论后，评价列表实际增加" + a);
            JSONArray newProduct01 =wx.newProduct(id).getJSONArray("list");
            int n1 = newProduct01.size();
            int x = n1-n;
            Preconditions.checkArgument(x==1, "新增评论后，小程序评论实际添加了" + x);

            //删除
            JSONArray tasteList =  md.taste_search_comment(id,1,10,null).getJSONArray("list");
            int tasteId = tasteList.getJSONObject(0).getInteger("id");
            String result0 = md.deleteComment(tasteId).getString("result");
            Preconditions.checkArgument(result0.equals("true"), "删除评论失败" + result0);
            Integer total1 = md.taste_search_comment(id,1,10,null).getInteger("total");
            int b = total0-total1;
            Preconditions.checkArgument(b==1, "删除评论后实际减少了" + b);

            JSONArray newProduct =wx.newProduct(id).getJSONArray("list");
            int c = newProduct.size();
            int d = n-c;
            Preconditions.checkArgument(d==1, "删除评论后小程序实际减少了" + d);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新增评论口味，删除口味，小程序列表+-1");
        }
    }

    //编辑评论是否可见
    @Test()
    public void updateVisible() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            wx.loginApplet(EnumAppletToken.INS_ZT_DAILY.getToken());
            JSONArray sortList =wx.tasteSort(20).getJSONArray("list");
            //获取口味id
            int sortId = sortList.getJSONObject(0).getInteger("id");
            //获取小程序评论数量
            int a = sortList.size();

            //pc搜索评论页
            JSONArray tastelist = md.taste_search_comment(sortId, 1, 10, null).getJSONArray("list");
            //获取评论id
            int visibleId = tastelist.getJSONObject(0).getInteger("id");
            //关闭评论
            String reslut = md.seachCommentVisible(visibleId, false).getString("result");
            Preconditions.checkArgument(reslut.equals("true"), "评论关闭失败" + reslut);
            JSONArray sortList0 = wx.tasteSort(20).getJSONArray("list");
            int b = sortList0.size();
            int c = b - a;
            Preconditions.checkArgument(c == 1, "关闭评论后小程序实际减少了" + c);

            //开启评论
            String reslut0 = md.seachCommentVisible(visibleId, true).getString("result");
            Preconditions.checkArgument(reslut0.equals("true"), "评论开启失败" + reslut);
            JSONArray sortList1 =wx.tasteSort(20).getJSONArray("list");
            int b1 = sortList1.size();
            int c1 = b1 - b;
            Preconditions.checkArgument(c1 == 1, "开启评论后小程序实际增加了" + c1);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("关闭评论口味，开启口味评论，小程序列表+-1");
        }
    }

    //新增反馈类型
    @Test()
    public void addFeedType() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            wx.loginApplet(EnumAppletToken.INS_ZT_DAILY.getToken());
            //获取小程序所有反馈类型
            JSONArray wxTypeAllList = wx.queryAll().getJSONArray("list");
            int wxNum1 = wxTypeAllList.size();
            //获取pc所有反馈类型
            JSONArray pcTypeAllList = md.feedbackTypeAll().getJSONArray("list");
            int pcNum1 = pcTypeAllList.size();
            //新加一个反馈类型
            String typeResult = md.feedback_add("地点反馈123","地点").getString("result");
            Preconditions.checkArgument(typeResult.equals("true"), "新增反馈类型失败" + typeResult);
            //获取新增后小程序所有反馈类型
            JSONArray wxTypeAllList0 = wx.queryAll().getJSONArray("list");
            int wxNum2 = wxTypeAllList0.size();
            //获取新增后pc所有反馈类型
            JSONArray pcTypeAllList0 = md.feedbackTypeAll().getJSONArray("list");
            int pcNum2 = pcTypeAllList0.size();

            int wx1 = wxNum2-wxNum1;
            int pc1 = pcNum2-pcNum1;
            Preconditions.checkArgument(wx1==1, "新增反馈类型,小程序下拉框实际添加" + wx1);
            Preconditions.checkArgument(pc1==1, "新增反馈类型,pc下拉框实际添加" + pc1);

            //获取反馈类型id
            JSONArray typeList = md.feedbackList(null,1,10).getJSONArray("list");
            int typeID = typeList.getJSONObject(0).getInteger("id");
            //删除反馈类型
            String deleteResult = md.feedback_delete(typeID).getString("result");
            Preconditions.checkArgument(deleteResult.equals("true"), "删除反馈类型失败" + deleteResult);

            //获取删除后小程序所有反馈类型
            JSONArray wxTypeAllList1 = wx.queryAll().getJSONArray("list");
            int wxNum3 = wxTypeAllList1.size();
            //获取删除后pc所有反馈类型
            JSONArray pcTypeAllList1 = md.feedbackTypeAll().getJSONArray("list");
            int pcNum3 = pcTypeAllList1.size();

            int wx2 = wxNum2-wxNum3;
            int pc3 = pcNum2-pcNum3;
            Preconditions.checkArgument(wx2==1, "删除反馈类型,小程序下拉框实际减少" + wx2);
            Preconditions.checkArgument(pc3==1, "删除反馈类型,pc下拉框实际减少" + pc3);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新增、删除反馈类型，数量+-1");
        }

    }
    
    //会员提交反馈，pc反馈列表数量+1
    @Test()
    public void submitFeedback() throws Exception {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            wx.loginApplet(EnumAppletToken.INS_ZT_DAILY.getToken());
            //小程序反馈奖励数量查询
            JSONArray awardList = wx.awardFeedback(20).getJSONArray("list");
            int awardListNum = awardList.size();
            //pc反馈列表查询
            int total = md.feedbackList(null,null,1,10).getInteger("total");
            JSONArray feedList = md.feedbackList(null,null,1,10).getJSONArray("list");
            int feedId = feedList.getJSONObject(0).getInteger("id");
            String result = wx.submitFeedback(feedId,5,"adsadsa").getString("result");
            Preconditions.checkArgument(result.equals("true"), "提交反馈失败" + result);
            int total0 = md.feedbackList(null,null,1,10).getInteger("total");
            int a = total0-total;
            Preconditions.checkArgument(a==1, "小程序提交反馈后，pc页实际增加了" + a);

            //添加礼品
            String giftresult = md.addGift(feedId,"满清10大酷刑一套").getString("result");
            Preconditions.checkArgument(giftresult.equals("true"), "添加礼品失败" + giftresult);
            JSONArray awardList0 = wx.awardFeedback(20).getJSONArray("list");
            int awardListNum0 = awardList0.size();
            int b = awardListNum0-awardListNum;
            Preconditions.checkArgument(b==1, "添加礼品后，小程序反馈列表实际添加" + b);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交一条反馈，pc用户反馈列表+1");
        }
    }
}
