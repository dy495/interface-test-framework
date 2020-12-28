package com.haisheng.framework.testng.bigScreen.xundianDaily.hqq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.xundianDaily.XundianScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.fucPackage.StoreFuncPackage;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.fucPackage.XdPackageData;
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
    int page = 1;
    int size = 50;
    String comment = "自动化在进行处理，闲人走开";
    String comment1 = "我们班有些同bai学一听到写作文头就大，但是我不一样，习作对于我来说简直就是一种乐趣。一般我都是越往下写我就越想写，也就觉得越有意思，还可以增加我对写作的喜爱。可为什么我们班同学就那么不喜欢写作文呢哈哈哈哈";
    public String filepath = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/卡券图.jpg";  //巡店不合格图片base64

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
        xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");


    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @Override
    public void createFreshCase(Method method) {

    }
    @Test(description = "[APP]个人中心待办事项中远程巡店进行处理(100字说明、不带照片)提交，巡检员复核提交说明为101个字")
    public void deal_unfinished_remoteFalse() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            xd.login("8888@qq.com", "cf79ae6addba60ad018347359bd144d2");//自动化专用店长账号
            JSONArray list = (JSONArray) xds.getTab_total(page, size, 0, null).get("list");
            Long shop_id = xds.getId_ShopId(list,"REMOTE_UNQUALIFIED").get("shop_id");
            Long id =  xds.getId_ShopId(list,"REMOTE_UNQUALIFIED").get("id");
            Integer code = xd.task_step_submit(shop_id, id, null, null,comment).getInteger("code");
            Preconditions.checkArgument(code == 1000, "[APP]个人中心待办事项中远程巡店进行处理(100字说明、不带照片)提交失败，失败code="+code);
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            Integer code3 = xd.MstepSumit(shop_id, id, comment1, null, 1).getInteger("code");
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
            xd.login("8888@qq.com", "cf79ae6addba60ad018347359bd144d2");//自动化专用店长账号
            JSONArray list = (JSONArray) xds.getTab_total(page, size, 0, null).get("list");
            Long shop_id = xds.getId_ShopId(list,"REMOTE_UNQUALIFIED").get("shop_id");
            Long id =  xds.getId_ShopId(list,"REMOTE_UNQUALIFIED").get("id");
            Integer code = xd.task_step_submit(shop_id, id, null, null,comment).getInteger("code");
            Preconditions.checkArgument(code == 1000, "[APP]个人中心待办事项中远程巡店进行处理(100字说明、不带照片)提交失败，失败code="+code);
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            Integer code1 = xd.MstepSumit(shop_id, id, comment, null, 1).getInteger("code");
            Preconditions.checkArgument(code1 == 1000, "[APP]个人中心待办事项中远程巡店不合格项处理以后，巡检员复核结果为合格,提交失败，失败code="+code);
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
            xd.login("8888@qq.com", "cf79ae6addba60ad018347359bd144d2");//自动化专用店长账号
            JSONArray list = (JSONArray) xds.getTab_total(page, size, 0, null).get("list");
            Long shop_id = xds.getId_ShopId(list,"REMOTE_UNQUALIFIED").get("shop_id");
            Long id =  xds.getId_ShopId(list,"REMOTE_UNQUALIFIED").get("id");
            Integer code = xd.task_step_submit(shop_id, id, null, null,comment1).getInteger("code");
            Preconditions.checkArgument(code == 1001, "[APP]个人中心待办事项中远程巡店不合格项进行处理(101字说明)提交成功，成功code="+code);
            JSONArray pic_list= xds.getPicPath1();
            Integer code1 = xd.MstepSumit(shop_id, id, comment, pic_list, null).getInteger("code");
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
            xd.login("8888@qq.com", "cf79ae6addba60ad018347359bd144d2");//自动化专用店长账号
            JSONArray list = (JSONArray) xds.getTab_total(page, size, 0, null).get("list");
            JSONArray pic_list= xds.getPicPath();
            Long shop_id = xds.getId_ShopId(list,"REMOTE_UNQUALIFIED").get("shop_id");
            Long id =  xds.getId_ShopId(list,"REMOTE_UNQUALIFIED").get("id");
            Integer code = xd.task_step_submit(shop_id, id, null, null,comment).getInteger("code");
            Preconditions.checkArgument(code == 1000, "[APP]个人中心待办事项中远程巡店不合格项进行处理，提交失败，失败code="+code);
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            Integer code1 = xd.MstepSumit(shop_id, id, comment, null, 0).getInteger("code");
            Preconditions.checkArgument(code1 == 1000, "[APP]个人中心待办事项中远程巡店不合格项的复核结果，巡检员复核结果为不合格，提交失败，失败code="+code);
            xd.login("8888@qq.com", "cf79ae6addba60ad018347359bd144d2");//自动化专用店长账号
            JSONArray pic_list1= xds.getPicPath2();
            Integer code2 = xd.MstepSumit(shop_id, id, comment, pic_list1, null).getInteger("code");
            Preconditions.checkArgument(code2 == 1000, "[APP]个人中心待办事项中远程巡店不合格项复核不合格后，再次进行处理后提交失败，失败code="+code);
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            Integer code3 = xd.MstepSumit(shop_id, id, comment, null, 0).getInteger("code");
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
            xd.login("8888@qq.com", "cf79ae6addba60ad018347359bd144d2");//自动化专用店长账号
            JSONArray list = (JSONArray) xds.getTab_total(page, size, 0, null).get("list");
            JSONArray pic_list= xds.getPicPath();
            Long shop_id = xds.getId_ShopId(list,"SPOT_UNQUALIFIED").get("shop_id");
            Long id =  xds.getId_ShopId(list,"SPOT_UNQUALIFIED").get("id");
            Integer code = xd.task_step_submit(shop_id, id, pic_list, null,comment).getInteger("code");
            Preconditions.checkArgument(code == 1000, "[APP]个人中心待办事项中现场巡店不合格项进行处理，提交失败，失败code="+code);
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            Integer code1 = xd.task_step_submit(shop_id, id, null, 0,comment).getInteger("code");
            Preconditions.checkArgument(code1 == 1000, "[APP]个人中心待办事项中现场巡店不合格项的复核结果，巡检员复核结果为不合格，提交失败，失败code="+code);
            xd.login("8888@qq.com", "cf79ae6addba60ad018347359bd144d2");//自动化专用店长账号
            JSONArray pic_list1= xds.getPicPath2();
            Integer code2 = xd.task_step_submit(shop_id, id, pic_list1, null,comment).getInteger("code");
            Preconditions.checkArgument(code2 == 1000, "[APP]个人中心待办事项中现场巡店不合格项复核不合格后，再次进行处理后提交失败，失败code="+code);
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            Integer code3 = xd.task_step_submit(shop_id, id, null, 0,comment).getInteger("code");
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
            xd.login("8888@qq.com", "cf79ae6addba60ad018347359bd144d2");//自动化专用店长账号
            JSONArray list = (JSONArray) xds.getTab_total(page, size, 0, null).get("list");
            JSONArray pic_list= xds.getPicPath();
            Long shop_id = xds.getId_ShopId(list,"SCHEDULE_UNQUALIFIED").get("shop_id");
            Long id =  xds.getId_ShopId(list,"SCHEDULE_UNQUALIFIED").get("id");
            Integer code = xd.task_step_submit(shop_id, id, pic_list, null,comment).getInteger("code");
            Preconditions.checkArgument(code == 1000, "[APP]个人中心待办事项中定检巡店不合格项进行处理，提交失败，失败code="+code);
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            Integer code1 = xd.task_step_submit(shop_id, id, null, 0,comment).getInteger("code");
            Preconditions.checkArgument(code1 == 1000, "[APP]个人中心待办事项中定检巡店不合格项的复核结果，巡检员复核结果为不合格，提交失败，失败code="+code);
            xd.login("8888@qq.com", "cf79ae6addba60ad018347359bd144d2");//自动化专用店长账号
            JSONArray pic_list1= xds.getPicPath2();
            Integer code2 = xd.task_step_submit(shop_id, id, pic_list1, null,comment).getInteger("code");
            Preconditions.checkArgument(code2 == 1000, "[APP]个人中心待办事项中定检巡店不合格项复核不合格后，再次进行处理后提交失败，失败code="+code);
            xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
            Integer code3 = xd.task_step_submit(shop_id, id, null, 0,comment).getInteger("code");
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
            xd.login("8888@qq.com", "cf79ae6addba60ad018347359bd144d2");//自动化专用店长账号
            JSONArray list = (JSONArray) xds.getTab_total(page, size, 0, null).get("list");
            Long id =  xds.getId_ShopId(list,"SCHEDULE_TASK").get("id");
            JSONArray pic_list = xd.task_detail(id).getJSONArray("pic_list");
            String show_url = "";
            for(int i=0;i<pic_list.size();i++){
                show_url = pic_list.getJSONObject(i).getString("show_url");
            }
            Preconditions.checkArgument(!show_url.isEmpty(), "将【待办事项】中[未完成]中的定检任务中没有生成定检图片"
            );
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("将【待办事项】中[未完成]中的定检任务中是否有图片");
        }
    }
}
