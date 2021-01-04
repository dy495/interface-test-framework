package com.haisheng.framework.testng.bigScreen.xundianDaily.hqq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.xundianDaily.MendianInfo;
import com.haisheng.framework.testng.bigScreen.xundianDaily.XundianScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.fucPackage.StoreFuncPackage;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.fucPackage.XdPackageData;
import com.haisheng.framework.testng.bigScreen.xundianOnline.StoreScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.ImageUtil;
import org.springframework.util.StringUtils;
import org.testng.annotations.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description :app 相关case
 * @date :2020/12/24 14:26
 **/


public class XundianAppCase extends TestCaseCommon implements TestCaseStd {

    XundianScenarioUtil xd = XundianScenarioUtil.getInstance();
    XdPackageData xds = XdPackageData.getInstance();
    MendianInfo info = new MendianInfo();
    int page = 1;
    int size = 50;
    String dealer = "8888@qq.com";
    String dealer_psw = "cf79ae6addba60ad018347359bd144d2";
    String checker ="2222@qq.com";
    String checker_psw = "934b535800b1cba8f96a5d72f72f1611";
    String comment = "自动化在进行处理，闲人走开";
    Long shop_id = 43072L;
    String comment1 = "我们班有些同bai学一听到写作文头就大，但是我不一样，习作对于我来说简直就是一种乐趣。一般我都是越往下写我就越想写，也就觉得越有意思，还可以增加我对写作的喜爱。可为什么我们班同学就那么不喜欢写作文呢哈哈哈哈";
    public String filepath = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/卡券图.jpg";  //巡店不合格图片base64


    //读取文件内容
    public String texFile(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String str = in.readLine();
        return str;
    }


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "青青";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "xundian-daily-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店 日常");
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"13581630214", "15084928847"};
        commonConfig.shopId = getXundianShop(); //要改！！！
        beforeClassInit(commonConfig);
        logger.debug("store " + xd);
        xd.login(checker, checker_psw);


    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @Override
    public void createFreshCase(Method method) {

    }
    @Test(description = "远程巡店为下述造处理事件")
    public void remote () {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long patrol_id=  xds.Scheduled(shop_id,0,null,"REMOTE",2,0);
            int code = xd.checks_submit(shop_id,patrol_id,"自动化处理远程巡店全部不合格").getInteger("code");
            Preconditions.checkArgument(code ==1000, "远程巡店提交失败"+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("[APP]远程巡店为下述造处理事件");
        }
    }

    @Test(description = "[APP]个人中心待办事项中远程巡店进行处理(100字说明、不带照片)提交，巡检员复核提交说明为101个字")
    public void deal_unfinished_remoteFalse() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            xd.login(dealer, dealer_psw);//自动化专用店长账号
            JSONArray list = (JSONArray) xds.getTab_total(page, size, 0, null).get("list");
            Long shop_id = xds.getId_ShopId(list,"REMOTE_UNQUALIFIED").get("shop_id");
            Long id =  xds.getId_ShopId(list,"REMOTE_UNQUALIFIED").get("id");
            Integer code = xd.task_step_submit(shop_id, id, null, null,comment).getInteger("code");
            Preconditions.checkArgument(code == 1000, "[APP]个人中心待办事项中远程巡店进行处理(100字说明、不带照片)提交失败，失败code="+code);
            xd.login(checker, checker_psw);
            JSONArray dataList = xd.task_list(page,size,0,null).getJSONArray("list");
            Long id1 = dataList.getJSONObject(0).getLong("id");
            Integer code3 = xd.task_step_submit(shop_id, id1, null, 1,comment1).getInteger("code");
            Preconditions.checkArgument(code3 == 1001, "[APP]个人中心待办事项中远程巡店不合格项处理以后，巡检员复核提交说明为101个字,提交成功，成功code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("[APP]个人中心待办事项中远程巡店进行处理(100字说明、不带照片)提交，巡检员复核提交说明为101个字");
        }
    }

    @Test(description = "[APP]个人中心待办事项中远程巡店进行处理(100字说明、不带照片)提交，巡检员进行处理为合格")
    public void deal_unfinished_remote() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            xd.login(dealer, dealer_psw);//自动化专用店长账号
            JSONArray list = (JSONArray) xds.getTab_total(page, size, 0, null).get("list");
            Long shop_id = xds.getId_ShopId(list,"REMOTE_UNQUALIFIED").get("shop_id");
            Long id =  xds.getId_ShopId(list,"REMOTE_UNQUALIFIED").get("id");
            Integer code = xd.task_step_submit(shop_id, id, null, null,comment).getInteger("code");
            Preconditions.checkArgument(code == 1000, "[APP]个人中心待办事项中远程巡店进行处理(100字说明、不带照片)提交失败，失败code="+code);
            xd.login(checker, checker_psw);
            JSONArray dataList = xd.task_list(page,size,0,null).getJSONArray("list");
            Long id1 = dataList.getJSONObject(0).getLong("id");
            Integer code1 = xd.task_step_submit(shop_id, id1, null, 1,comment).getInteger("code");
            Preconditions.checkArgument(code1 == 1000, "[APP]个人中心待办事项中远程巡店不合格项处理以后，巡检员复核结果为合格,提交失败，失败code="+code1);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("[APP]个人中心待办事项中远程巡店进行处理(100字说明、不带照片)提交，巡检员进行处理为合格");
        }
    }

    @Test(description = "[APP]个人中心待办事项中远程巡店不合格项进行处理(101字说明、不带照片)提交&&留痕6张提交")
    public void deal_unfinished_remote1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            xd.login(dealer, dealer_psw);//自动化专用店长账号
            JSONArray list = (JSONArray) xds.getTab_total(page, size, 0, null).get("list");
            Long shop_id = xds.getId_ShopId(list,"REMOTE_UNQUALIFIED").get("shop_id");
            Long id =  xds.getId_ShopId(list,"REMOTE_UNQUALIFIED").get("id");
            Integer code = xd.task_step_submit(shop_id, id, null, null,comment1).getInteger("code");
            Preconditions.checkArgument(code == 1001, "[APP]个人中心待办事项中远程巡店不合格项进行处理(101字说明)提交成功，成功code="+code);
            JSONArray pic_list= xds.getPicPath1();
            Integer code1 = xd.task_step_submit(shop_id, id, pic_list, null,comment).getInteger("code");
            Preconditions.checkArgument(code1 == 1001, "[APP]个人中心待办事项中远程巡店不合格项进行处理，留痕六张提交成功，成功code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("[APP]个人中心待办事项中远程巡店不合格项进行处理(101字说明、不带照片)提交&&留痕6张提交");
        }
    }
    @Test(description = "[APP]个人中心待办事项中远程巡店不合格项进行处理(100字说明、带5张照片)提交，巡检员进行处理为不合格，店长再处理（40字+1张留痕）提交，巡检员处理为合格")
    public void deal_unfinished_remote2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            xd.login(dealer, dealer_psw);//自动化专用店长账号
            JSONArray list = (JSONArray) xds.getTab_total(page, size, 0, null).get("list");
            JSONArray pic_list= xds.getPicPath();
            Long shop_id = xds.getId_ShopId(list,"REMOTE_UNQUALIFIED").get("shop_id");
            Long id =  xds.getId_ShopId(list,"REMOTE_UNQUALIFIED").get("id");
            Integer code = xd.task_step_submit(shop_id, id, null, null,comment).getInteger("code");
            Preconditions.checkArgument(code == 1000, "[APP]个人中心待办事项中远程巡店不合格项进行处理，提交失败，失败code="+code);
            xd.login(checker, checker_psw);
            JSONArray dataList = xd.task_list(page,size,0,null).getJSONArray("list");
            Long id1 = dataList.getJSONObject(0).getLong("id");
            Integer code1 = xd.task_step_submit(shop_id, id1, null, 0,comment).getInteger("code");
            Preconditions.checkArgument(code1 == 1000, "[APP]个人中心待办事项中远程巡店不合格项的复核结果，巡检员复核结果为不合格，提交失败，失败code="+code);
            xd.login(dealer, dealer_psw);//自动化专用店长账号
            JSONArray pic_list1= xds.getPicPath2();
            Integer code2 = xd.task_step_submit(shop_id, id, pic_list1, null,comment).getInteger("code");
            Preconditions.checkArgument(code2 == 1000, "[APP]个人中心待办事项中远程巡店不合格项复核不合格后，再次进行处理后提交失败，失败code="+code);
            xd.login(checker, checker_psw);
            JSONArray dataList1 = xd.task_list(page,size,0,null).getJSONArray("list");
            Long id2 = dataList1.getJSONObject(0).getLong("id");
            Integer code3 = xd.task_step_submit(shop_id, id2, null, 0,comment).getInteger("code");
            Preconditions.checkArgument(code3 == 1000, "[APP]个人中心待办事项中远程巡店不合格项的再次处理结果，巡检员进行再次复核后，结果为合格提交失败，失败code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("[APP]个人中心待办事项中远程巡店不合格项进行处理(100字说明、带5张照片)提交，巡检员进行处理为不合格，店长再处理（40字+1张留痕）提交，巡检员处理为合格");
        }
    }
    @Test(description = "[APP]个人中心待办事项中现场巡店不合格项进行处理(100字说明、带5张照片)提交，巡检员进行处理为不合格，店长再处理（40字+1张留痕）提交，巡检员处理为合格")
    public void deal_unfinished_spot2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            xd.login(dealer, dealer_psw);//自动化专用店长账号
            JSONArray list = (JSONArray) xds.getTab_total(page, size, 0, null).get("list");
            JSONArray pic_list= xds.getPicPath();
            Long shop_id = xds.getId_ShopId(list,"SPOT_UNQUALIFIED").get("shop_id");
            Long id =  xds.getId_ShopId(list,"SPOT_UNQUALIFIED").get("id");
            Integer code = xd.task_step_submit(shop_id, id, pic_list, null,comment).getInteger("code");
            Preconditions.checkArgument(code == 1000, "[APP]个人中心待办事项中现场巡店不合格项进行处理，提交失败，失败code="+code);
            xd.login(checker, checker_psw);
            JSONArray dataList = xd.task_list(page,size,0,null).getJSONArray("list");
            Long id1 = dataList.getJSONObject(0).getLong("id");
            Integer code1 = xd.task_step_submit(shop_id, id1, null, 0,comment).getInteger("code");
            Preconditions.checkArgument(code1 == 1000, "[APP]个人中心待办事项中现场巡店不合格项的复核结果，巡检员复核结果为不合格，提交失败，失败code="+code);
            xd.login(dealer, dealer_psw);//自动化专用店长账号
            JSONArray pic_list1= xds.getPicPath2();
            Integer code2 = xd.task_step_submit(shop_id, id, pic_list1, null,comment).getInteger("code");
            Preconditions.checkArgument(code2 == 1000, "[APP]个人中心待办事项中现场巡店不合格项复核不合格后，再次进行处理后提交失败，失败code="+code);
            xd.login(checker, checker_psw);
            JSONArray dataList2 = xd.task_list(page,size,0,null).getJSONArray("list");
            Long id2 = dataList2.getJSONObject(0).getLong("id");
            Integer code3 = xd.task_step_submit(shop_id, id2, null, 0,comment).getInteger("code");
            Preconditions.checkArgument(code3 == 1000, "[APP]个人中心待办事项中现场巡店不合格项的再次处理结果，巡检员进行再次复核后，结果为合格提交失败，失败code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("[APP]个人中心待办事项中现场巡店不合格项进行处理(100字说明、带5张照片)提交，巡检员进行处理为不合格，店长再处理（40字+1张留痕）提交，巡检员处理为合格");
        }
    }
    @Test(description = "[APP]个人中心待办事项中定检巡店不合格项进行处理(100字说明、带5张照片)提交，巡检员进行处理为不合格，店长再处理（40字+1张留痕）提交，巡检员处理为合格")
    public void deal_unfinished_schedule() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            xd.login(dealer, dealer_psw);//自动化专用店长账号
            JSONArray list = (JSONArray) xds.getTab_total(page, size, 0, null).get("list");
            JSONArray pic_list= xds.getPicPath();
            Long shop_id = xds.getId_ShopId(list,"SCHEDULE_UNQUALIFIED").get("shop_id");
            Long id =  xds.getId_ShopId(list,"SCHEDULE_UNQUALIFIED").get("id");
            Integer code = xd.task_step_submit(shop_id, id, pic_list, null,comment).getInteger("code");
            Preconditions.checkArgument(code == 1000, "[APP]个人中心待办事项中定检巡店不合格项进行处理，提交失败，失败code="+code);
            xd.login(checker, checker_psw);
            JSONArray dataList = xd.task_list(page,size,0,null).getJSONArray("list");
            Long id1 = dataList.getJSONObject(0).getLong("id");
            Integer code1 = xd.task_step_submit(shop_id, id1, null, 0,comment).getInteger("code");
            Preconditions.checkArgument(code1 == 1000, "[APP]个人中心待办事项中定检巡店不合格项的复核结果，巡检员复核结果为不合格，提交失败，失败code="+code);
            xd.login(dealer, dealer_psw);//自动化专用店长账号
            JSONArray pic_list1= xds.getPicPath2();
            Integer code2 = xd.task_step_submit(shop_id, id, pic_list1, null,comment).getInteger("code");
            Preconditions.checkArgument(code2 == 1000, "[APP]个人中心待办事项中定检巡店不合格项复核不合格后，再次进行处理后提交失败，失败code="+code);
            xd.login(checker, checker_psw);
            JSONArray dataList1 = xd.task_list(page,size,0,null).getJSONArray("list");
            Long id2 = dataList1.getJSONObject(0).getLong("id");
            Integer code3 = xd.task_step_submit(shop_id, id2, null, 0,comment).getInteger("code");
            Preconditions.checkArgument(code3 == 1000, "[APP]个人中心待办事项中现场定检不合格项的再次处理结果，巡检员进行再次复核后，结果为合格提交失败，失败code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("[APP]个人中心待办事项中现场定检不合格项进行处理(100字说明、带5张照片)提交，巡检员进行处理为不合格，店长再处理（40字+1张留痕）提交，巡检员处理为合格");
        }
    }

    @Test(description = "将【待办事项】中[未完成]中的定检任务中是否有图片")
    public void dealAfterData_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = xd.schedule_pic_list(shop_id,"8112290559198208","").getJSONArray("list");//设备为152
            JSONObject pic = list.getJSONObject(0).getJSONObject("pic");
            String show_url = pic.getString("show_url");
            Preconditions.checkArgument(!show_url.isEmpty(), "将【待办事项】中[未完成]中的定检任务中没有生成定检图片");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("将【待办事项】中[未完成]中的定检任务中是否有图片");
        }
    }
    /**
     * description = "APP【个人中心】人脸检测（正常脸检测）"
     * ***/
    @Test(dataProvider = "FACE_URL",dataProviderClass = XdPackageData.class)
    public void face_check(String face_url ) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
             String filepath1 = face_url ;  //人脸合格base64
            String picture = new ImageUtil().getImageBinary(filepath1);
             String message = xd.face_check(picture).getString("message");
             Preconditions.checkArgument(message.equals("面容信息检测合格"), "APP【个人中心】人脸检测（正常脸检测），结果为："+message);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("APP【个人中心】人脸检测（正常脸检测）");
        }
    }

    /**
     * description = "APP【个人中心】人脸检测（非正常脸检测）"
     * ***/
    @Test(dataProvider = "FACE_URL1",dataProviderClass = XdPackageData.class)
    public void face_check1(String face_url1 ) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String filepath1 = face_url1 ;  //人脸合格base64
            String picture = new ImageUtil().getImageBinary(filepath1);
            String message = xd.face_check(picture).getString("message");
            Preconditions.checkArgument(message.equals("面容信息不合格，请重试"), "APP【个人中心】人脸检测（非正常脸检测）"+face_url1+"结果为："+message);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("APP【个人中心】人脸检测（非正常脸检测）");
        }
    }


}
