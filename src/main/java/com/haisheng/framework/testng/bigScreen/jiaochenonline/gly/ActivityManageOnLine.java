package com.haisheng.framework.testng.bigScreen.jiaochenonline.gly;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.util.PublicParameter;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletVoucherInfo;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.VoucherSendRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityApprovalStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.activity.AppletArticleListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.activity.ArticleVoucherReceiveScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.FileUpload;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.ChangeProvideStatusScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.InvalidVoucherScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.gly.util.BusinessUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityManageOnLine extends TestCaseCommon implements TestCaseStd {
    ScenarioUtil jc = new ScenarioUtil();
    private static final EnumTestProduce product = EnumTestProduce.JC_ONLINE;
    private static final EnumAccount ADMINISTRATOR=EnumAccount.ALL_JC_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(product);
//    BusinessUtil businessUtil = new BusinessUtil(visitor);
    BusinessUtilOnline businessUtil=new BusinessUtilOnline(visitor);
    SupporterUtil supporterUtil = new SupporterUtil(visitor);
    PublicParameter pp = new PublicParameter();
    UserUtil user = new UserUtil(visitor);
    CommonConfig commonConfig = new CommonConfig();

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        jc.changeIpPort(EnumTestProduce.JC_ONLINE.getAddress());
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.GLY.getName();
        commonConfig.product = product.getAbbreviation();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.roleId = ADMINISTRATOR.getRoleId();
        commonConfig.referer = product.getReferer();
        commonConfig.shopId = product.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @deprecated :get a fresh case ds to save case result, such as result/response
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        user.loginPc(ADMINISTRATOR);
//        jc.pcLogin(pp.phone,pp.password);
    }


    /**
     * ----------------------活动主流程2个case--------------------------------
     */

    /**
     * 创建裂变活动-撤回裂变活动-再次创建裂变活动-审批通过裂变活动-小程序查看裂变活动 ok
     */
    @Test
    public void fissionVoucherMainProcess() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Boolean flag = false;
            JSONObject lastValue=null;
            JSONArray list=null;
            //获取一个卡券
            Long voucherId = businessUtil.getVoucherId();
            //创建裂变活动
            Long activityId = businessUtil.createFissionActivity(voucherId);
            //获取活动的状态
            int status = businessUtil.getActivityStatus(activityId);
            //撤销裂变活动
            businessUtil.getRevokeActivity(activityId);
            //获取活动的状态
            int statusRevoke = businessUtil.getActivityStatus(activityId);
            //获取一个卡券
            Long voucherId2 = businessUtil.getVoucherId(); //获取一个卡券
            //再次创建裂变活动
            Long activityId2 = businessUtil.createFissionActivity(voucherId2);
            //审批通过裂变活动
            String message=businessUtil.getApprovalPassed(activityId2);
            System.err.println("message:"+message);
            //获取活动的状态
            int statusPassed = businessUtil.getActivityStatus(activityId2);
            //获取此活动的名称
            String title=businessUtil.getRecruitActivityDetailDate1(activityId2).getString("title");
            System.err.println("----------title:"+title);
            //登录小程序
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            //获取小程序推荐列表  判断裂变活动是否创建成功
            do {
                IScene scene = AppletArticleListScene.builder().lastValue(lastValue).size(10).build();
                JSONObject response = visitor.invokeApi(scene);
                lastValue = response.getJSONObject("last_value");
                list = response.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String title1 = list.getJSONObject(i).getString("title");
                    System.out.println("----------title1:"+title1);
                    if (title.equals(title1)) {
                        flag=true;
                    }
                }
            } while (list.size() == 10);
            Preconditions.checkArgument(status == ActivityStatusEnum.PENDING.getId(), "待审核的活动状态为：" + status);
            Preconditions.checkArgument(statusRevoke == ActivityStatusEnum.REVOKE.getId(), "已撤销的活动状态为：" + statusRevoke);
            Preconditions.checkArgument(statusPassed == ActivityStatusEnum.PASSED.getId(), "审批通过的活动状态为：" + statusPassed);
            Preconditions.checkArgument(flag.equals(true), "小程序中不展示此审批通过的活动");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建裂变活动-撤回裂变活动-再次创建裂变活动-审批通过裂变活动-小程序查看裂变活动");
        }
    }

    /**
     * 创建招募活动-活动审批通过-报名活动-点击审批提醒进入报名审批页面-审批通过活动报名-取消活动  ok
     */
    @Test
    public void recruitMainProcess() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Boolean flag = false;
            Long id = 0L;
            //获取一个卡券
            Long voucherId = businessUtil.getVoucherId();
            //创建招募活动
            Long activityId = businessUtil.createRecruitActivity(voucherId, true, 0, true);
            System.err.println("--------------"+activityId);
            //获取活动的状态
            int status = businessUtil.getActivityStatus(activityId);
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            //获取活动的状态
            int statusPassed = businessUtil.getActivityStatus(activityId);
            System.err.println(activityId+"--------"+statusPassed);
            //获取此活动的名称
            String title=businessUtil.getRecruitActivityDetailDate1(activityId).getString("title");
            System.err.println("----------title:"+title);
            //登录小程序
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            //小程序报名此活动
            businessUtil.activityRegisterApplet(activityId, "13373166806", "郭丽雅", 2, "1513814362@qq.com", "22", "女","其他");
            //登录PC
            user.loginPc(ADMINISTRATOR);
            //审批通过小程序活动报名
            List<Long> ids = businessUtil.RegisterAppletIds(activityId);
            businessUtil.getRegisterApprovalPassed(activityId, ids.get(0));
            //获取卡券码
            List<VoucherSendRecord> vList = supporterUtil.getVoucherSendRecordList(voucherId);
            String voucherCode = vList.get(0).getVoucherCode();
            System.err.println("-----获取卡券码-----" + voucherCode);
            //登录小程序
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            System.err.println("AD:"+activityId+"      title:"+title);
            //查询是否获得此卡券(通过卡券码查询，看看能否有此卡券的返回值)
            AppletVoucherInfo voucher = supporterUtil.getAppletVoucherInfo(voucherCode);
            //小程序我的报名列表
            JSONArray list1 = jc.appletMyActually(null, "20").getJSONArray("list");
            for (int i = 0; i < list1.size(); i++) {
                String title1 = list1.getJSONObject(i).getString("title");
                if (title1.equals(title)) {
                    id = list1.getJSONObject(i).getLong("id");
                    break;
                }
            }
            //小程序取消报名
            businessUtil.activityCancelScene(id);
            //报名取消后获得此报名的状态
            String activityStatus = businessUtil.appointmentActivityStatus(id);
            System.err.println(title+"-----------"+activityStatus);
//            //获取【我的卡券】列表条数
            int numAfter = jc.appletVoucherList(null, "GENERAL", 100).getJSONArray("list").size();
            Preconditions.checkArgument(status == ActivityStatusEnum.PENDING.getId(), "待审核的活动状态为：" + status);
            Preconditions.checkArgument(statusPassed == ActivityStatusEnum.PASSED.getId(), "审核通过的活动状态为：" + statusPassed);
            Preconditions.checkArgument(voucher != null, "小程序报名通过没有获得此卡券");
            Preconditions.checkArgument(activityStatus.equals(ActivityStatusEnum.CANCELED.getStatusName()), "小程序报名通过取消以后此活动的状态为：" + activityStatus);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("创建招募活动-活动审批通过-报名活动-点击审批提醒进入报名审批页面-审批通过活动报名-取消活动");
        }
    }



    @Test(enabled = false)
    public void justTry(){
        try {
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
          //小程序报名活动(报名信息不填写)
            businessUtil.activityRegisterApplet(2122L,"","",1,"","","","");
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("测试呀呀呀呀");
        }
    }



    /**
     * 创建1个招募活动和个裂变活动
     */
    public void preCreateSomeActivity() {
        //获取一个卡券
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        //创建招募活动
        for (int i = 0; i < 3; i++) {
            Long activityId = businessUtil.createRecruitActivity(voucherId, true, 0, true);
            businessUtil.activityRegisterApplet(activityId, "13373166806", "郭丽雅", 2, "1513814362@qq.com", "22", "女","其他");
            //创建裂变活动
            Long activityId1 = businessUtil.createFissionActivity(voucherId);
        }
    }

/**
 * ===================================活动管理的数据一致性==================================
 */
    /**
     * 创建招募活动-数据一致性校验{列表+1&状态=待审核,【活动审批】列表+1&状态=待审核}    ok
     */
    @Test(description = "创建招募活动-数据一致性校验{列表+1&状态=待审核}")
    public void createActivityDate1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建活动之前的列表数量
            IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
            int totalBefore = visitor.invokeApi(scene).getInteger("total");
            //获取一个卡券
            Long voucherId = businessUtil.getVoucherId();
            //创建招募活动
            Long activityId = businessUtil.createRecruitActivity(voucherId, true, 0, true);
            //创建招募活动
//            Long id=businessUtil.createRecruitActivityApproval();
            //创建招募活动之后的列表数量
            int totalAfter = visitor.invokeApi(scene).getInteger("total");
            //创建活动后-状态=待审核
            int status = businessUtil.getActivityStatus(activityId);
            Preconditions.checkArgument(totalAfter == (totalBefore + 1) && status == ActivityStatusEnum.PENDING.getId(), "创建活动之前的列表数为：" + totalBefore + "创建活动之后的列表数为：" + totalAfter + "活动的状态为：" + status);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建招募活动-数据一致性校验{列表+1&状态=待审核,【活动审批】列表+1&状态=待审核}");
        }
    }

    /**
     * 创建招募活动-数据一致性校验{【活动详情】发放数量=创建时的填写数量,【活动详情】面值=优惠券的面值（不是每一个优惠券都有面值）,【活动详情】剩余库存=创建时填写数量}   ok
     */
    @Test(description = "创建招募活动-{【活动详情】发放数量=创建时的填写数量,【活动详情】剩余库存=创建时填写数量}")
    public void createActivityDate2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取一个卡券
            Long voucherId = businessUtil.getVoucherId();
            //创建招募活动
            Long activityId = businessUtil.createRecruitActivity(voucherId, true, 0, true);
            //优惠券的面值
//            String parValue = businessUtil.getPrice(voucherId);
            JSONArray list = businessUtil.getRecruitActivityDetail(activityId);
            //活动详情发放数量
            String num = list.getJSONObject(0).getString("num");
            //活动详情剩余库存
            String leftNum = list.getJSONObject(0).getString("left_num");
            //活动详情中-优惠券面值
            String price = list.getJSONObject(0).getString("price");
            //填写库存数量
            String number = String.valueOf(businessUtil.getVoucherAllowUseInventoryNum(voucherId));
            System.out.println("发放数量：" + num + "--剩余库存" + leftNum + "--填写的库存数量" + number);
            Preconditions.checkArgument(num.equals(number) && leftNum.equals(number), "活动详情中的数值与创建时的数字不一致");
//            Preconditions.checkArgument(price.equals(parValue),"优惠券的面值和活动中优惠券的面值不一致");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建招募活动-【活动详情】发放数量=创建时的填写数量,【活动详情】面值=创建时填写的数量,【活动详情】剩余库存=创建时填写数量");
        }
    }

    /**
     * 创建招募活动-数据一致性校验{【调整记录】+1&调整类型=新建活动}     ok
     */
    @Test(description = "创建招募活动-数据一致性校验{【调整记录】+1&调整类型=新建活动}")
    public void createActivityDate3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建招募活动-活动ID
            Long activityId = businessUtil.createRecruitActivityApproval();
            //线程等待2秒   创建完立马进入变更记录，查询内容为空
            sleep(2);
            //活动管理-变更记录
            IScene scene = ManageChangeRecordScene.builder().id(activityId).page(1).size(10).build();
            JSONObject response = visitor.invokeApi(scene);
            JSONArray list = response.getJSONArray("list");
            String content = list.getJSONObject(0).getString("content");
            Preconditions.checkArgument(content.equals("创建活动") && list.size() == 1, "调整记录中新增的记录的状态为：" + content);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建招募活动-数据一致性校验{【调整记录】+1&调整类型=新建活动}");
        }
    }

    /**
     * 活动管理-编辑招募活动的规则和标题，①内容更新，②【调整记录】+1&调整类型=修改活动  √
     */
    @Test
    public void editWorkingActivityDate4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中的活动ID
            List<Long> ids = businessUtil.getRecruitActivityWorking();
            //编辑前-变更记录条数
            JSONArray list = businessUtil.changeRecordPage(ids.get(0)).getJSONArray("list");
            int numBefore = list.size();
            System.err.println(numBefore);
            //编辑活动名称、活动名额
            String message = businessUtil.activityEditScene(ids.get(0));
            //获取活动详情中编辑后的标题和活动规则
            String title = businessUtil.getRecruitActivityDetailDate1(ids.get(0)).getString("title");
            String rule = businessUtil.getRecruitActivityDetailDate(ids.get(0)).getString("rule");
            //编辑后 变更记录+1&调整类型=修改活动
            int numAfter =businessUtil.changeRecordPage(ids.get(0)).getJSONArray("list").size();
            System.err.println(numAfter);
            System.err.println(title+"-------"+rule);
            String content = businessUtil.changeRecordPage(ids.get(0)).getJSONArray("list").getJSONObject(0).getString("content");
            Preconditions.checkArgument(title.contains("编辑过后的招募活动") && rule.equals(pp.EditRule), "编辑后的活动名字为：" + title + "  编辑后的活动规则为：" + rule);
            Preconditions.checkArgument(numAfter == numBefore + 1 && content.equals("编辑活动"), "编辑前变更记录的条数为：" + numBefore + "编辑后的变更记录的条数为：" + numAfter + "  编辑后变更记录新增的内容为：" + content);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData(" 活动管理-编辑招募活动的规则和标题，①内容更新，②【调整记录】+1&调整类型=修改活动");
        }
    }

    /**
     * 活动管理-取消【进行中】的招募活动，①【调整记录】+1&调整类型=取消  ②活动状态=已取消      ok
     */
    @Test(description = "活动管理-取消【进行中】的活动，①【调整记录】+1&调整类型=取消  ②活动状态=已取消")
    public void cancelWorkingActivityDate5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getActivityWorking();
            //编辑前-变更记录条数
            JSONArray list = businessUtil.changeRecordPage(ids.get(0)).getJSONArray("list");
            int numBefore = list.size();
            //取消进行中的活动
            businessUtil.getCancelActivity(ids.get(0));
            //获取此活动的状态
            int status = businessUtil.getActivityStatus(ids.get(0));
            System.err.println("---------" + status);
            //编辑后 变更记录+1&调整类型=取消活动
            JSONArray list1 = businessUtil.changeRecordPage(ids.get(0)).getJSONArray("list");
            int numAfter = list1.size();
            String content = list1.getJSONObject(0).getString("content");
            Preconditions.checkArgument(status == ActivityStatusEnum.CANCELED.getId(), "取消【进行中】的活动,现活动的状态为" + ActivityStatusEnum.CANCELED.getStatusName());
            Preconditions.checkArgument(numAfter == numBefore + 1 && content.equals("取消活动"), "取消前变更记录的条数为：" + numBefore + "取消后的变更记录的条数为：" + numAfter + "  取消后变更记录新增的内容为：" + content);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-取消【进行中】的活动，①【调整记录】+1&调整类型=取消  ②活动状态=已取消");
        }
    }

    /**
     * 活动管理-招募活动详情-①成本合计=发放数量*成本   ②总数量=剩余库存+发放数量&总数量>=剩余库存    ok
     */
    @Test(description = "活动管理-活动详情-①成本合计=发放数量*成本   ②总数量=剩余库存+发放数量&总数量>=剩余库存")
    public void activityDetailDate6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中的活动招募ID
            List<Long> ids = businessUtil.getRecruitActivityWorking();
            //进入此活动的活动详情页
            JSONArray list = businessUtil.getRecruitActivityDetail(ids.get(0));
            //获取优惠券ID
            Long voucherId = list.getJSONObject(0).getLong("id");
            //总成本
            String totalCost = list.getJSONObject(0).getString("total_cost");
            //成本
            String cost = businessUtil.getCost(voucherId);
            //发放数量
            int sendNum = list.getJSONObject(0).getInteger("send_num");
            //剩余数量
            int leftNum = list.getJSONObject(0).getInteger("left_num");
            //奖励项总数量
            int num = list.getJSONObject(0).getInteger("num");
            Preconditions.checkArgument(Double.parseDouble(totalCost) == Double.parseDouble(cost) * num && num == leftNum + sendNum && num >= leftNum, "总成本为：" + totalCost + "  应等于面值*数量为" + Double.parseDouble(cost) * num + "  奖励项总数为：" + num + "  发放数量为：" + sendNum + "  剩余数量为：" + leftNum);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-招募活动详情-①成本合计=发放数量*成本   ②总数量=剩余库存+发放数量&总数量>=剩余库存");
        }

    }

    /**
     * 活动报名列---①活动名额>=报名成功人数和   ②报名成功数=【全部列表】状态为审核通过人数和 ③已报名数>=报名成功数       ok
     */
    @Test
    public void activityRegisterDate7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取【进行中】的招募活动ID
            List<Long> ids = businessUtil.getRecruitActivityWorking();
            for (int j = 0; j < ids.size(); j++) {
                System.err.println("------" + ids.get(j));
                int registerPassedNum = 0;
                //报名列表的返回值
                JSONObject pageRes = businessUtil.getRegisterPage(ids.get(j));
                int pages = pageRes.getInteger("pages")>20?20:pageRes.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    IScene scene = ManageRegisterPageScene.builder().page(page).size(10).activityId(ids.get(j)).build();
                    JSONArray list = visitor.invokeApi(scene).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String statusName = list.getJSONObject(i).getString("status_name");
                        if (statusName.equals("已通过")) {
                            int num = list.getJSONObject(i).getInteger("register_num");
                            //报名审核通过的人数
                            registerPassedNum += num;
                        }
                    }
                }
                //报名数据的返回值
                JSONObject dataRes = businessUtil.getRegisterData(ids.get(j));
                //活动名额
                int quota = dataRes.containsKey("quota")?dataRes.getInteger("quota"):50;
                //报名总人数（已报名）
                int total = dataRes.getInteger("total");
                //待审批人数
                int wait = dataRes.getInteger("wait");
                //报名成功的人数
                int passed = dataRes.getInteger("passed");
                //报名失败的人数
                int failed = dataRes.getInteger("failed");
                Preconditions.checkArgument(quota >= total, "活动名额为：" + quota + "  报名总人数为：" + total);
                Preconditions.checkArgument(passed == registerPassedNum, "报名成功人数为：" + passed + "  【全部列表】状态为审核通过人数和：" + registerPassedNum);
                Preconditions.checkArgument(total >= passed, "已报名的人数为：" + total + "  报名成功的为：" + passed);

            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动报名列---①活动名额>=报名成功人数和 ②报名成功数=【全部列表】状态为审核通过人数和  ③已报名数>=报名成功数");
        }
    }

    /**
     * 报名列表-已报名数=【全部】报名人数和=【待审核】【报名成功】【报名失败】人数和     ok
     */
    @Test()
    public void activityRegisterDate8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取【进行中】的招募活动ID
            List<Long> ids = businessUtil.getRecruitActivityWorking();
            for (int j = 0; j < ids.size(); j++) {
                int registerNum = 0;
                //报名列表的返回值
                JSONObject pageRes = businessUtil.getRegisterPage(ids.get(j));
                int pages = pageRes.getInteger("pages")>20?20:pageRes.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    IScene scene = ManageRegisterPageScene.builder().page(page).size(10).activityId(ids.get(j)).build();
                    JSONArray list = visitor.invokeApi(scene).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String statusName = list.getJSONObject(i).getString("status_name");
                        if (statusName.equals("已通过") || statusName.equals("待审批") || statusName.equals("已拒绝")) {
                            int num = list.getJSONObject(i).getInteger("register_num");
                            registerNum += num;
                        }
                    }
                }
                //报名数据的返回值
                JSONObject dataRes = businessUtil.getRegisterData(ids.get(j));
                //活动名额
                int quota = dataRes.containsKey("quota")?dataRes.getInteger("quota"):50;
                //报名总人数
                int total = dataRes.getInteger("total");
                //待审批人数
                int wait = dataRes.getInteger("wait");
                //报名成功的人数
                int passed = dataRes.getInteger("passed");
                //报名失败的人数
                int failed = dataRes.getInteger("failed");

                Preconditions.checkArgument(total == registerNum && total == (wait + passed + failed), "报名总人数为：" + total + "  （待审批+报名成功+报名失败）人数为：" + (wait + passed + failed) + "  列表中的报名人数和为：" + registerNum);

            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("报名列表-已报名数=【全部】报名人数和=【待审核】【报名列表】【报名失败】人数和");
        }
    }

    /**
     * 报名列表-审批通过1条，报名成功&报名成功列表     ok
     */
    @Test
    public void activityRegisterDate9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int registerNum = 0;
            //获取进行中的活动存在待审批数量的ID
            List<Long> ids = businessUtil.getRecruitActivityWorkingApproval();
            System.err.println(ids);
            //审批通过之前报名成功的数量
            int passedBefore = businessUtil.getRegisterData(ids.get(0)).getInteger("passed");
            //报名待审批的ID合集
            List<Long> idArray = businessUtil.registerApproval(ids.get(0));
            //审批通过其中一条报名
            businessUtil.getRegisterApprovalPassed(ids.get(0), idArray.get(0));
            //审批通过的活动人数
            int pages = businessUtil.getRegisterPage(ids.get(0)).getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                IScene scene = ManageRegisterPageScene.builder().page(page).size(10).activityId(ids.get(0)).build();
                JSONArray List = visitor.invokeApi(scene).getJSONArray("list");
                for (int i = 0; i < List.size(); i++) {
                    Long idOne = List.getJSONObject(i).getLong("id");
                    String statusName = List.getJSONObject(i).getString("status_name");
                    if (idArray.get(0).equals(idOne) && statusName.equals("已通过")) {
                        int num = List.getJSONObject(i).getInteger("register_num");
                        registerNum += num;
                        System.err.println(registerNum);
                    }
                }
            }
            //审批通过之后报名成功的数量
            int passedAfter = businessUtil.getRegisterData(ids.get(0)).getInteger("passed");
            Preconditions.checkArgument(passedAfter > 0 && passedAfter == (passedBefore + registerNum), "审批的通过的人数为:" + registerNum);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("报名列表-审批通过1条，报名成功&报名成功列表");
        }
    }


    /**
     * 报名列表-审批通过1条，报名成功&报名成功列表--报名人数不填写
     */
    @Test()
    public void activityRegisterDate91() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int registerNum = 0;
//          //获取一个卡券
            Long voucherId = businessUtil.getVoucherId();
            //创建招募活动
            IScene scene1 = businessUtil.createRecruitActivityScene(voucherId, true, 0, true,false);
            Long activityId = visitor.invokeApi(scene1).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            //登录小程序
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            //小程序报名此活动
            businessUtil.activityRegisterApplet(activityId);
            //登录PC
            user.loginPc(ADMINISTRATOR);
            //审批通过之前报名成功的数量
            int passedBefore = businessUtil.getRegisterData(activityId).getInteger("passed");
            //审批通过小程序活动报名
            List<Long> ids = businessUtil.RegisterAppletIds(activityId);
            businessUtil.getRegisterApprovalPassed(activityId, ids.get(0));
            //审批通过的活动人数
            int pages = businessUtil.getRegisterPage(activityId).getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                IScene scene = ManageRegisterPageScene.builder().page(page).size(10).activityId(activityId).build();
                JSONArray List = visitor.invokeApi(scene).getJSONArray("list");
                for (int i = 0; i < List.size(); i++) {
                    Long idOne = List.getJSONObject(i).getLong("id");
                    String statusName = List.getJSONObject(i).getString("status_name");
                    if (ids.get(0).equals(idOne) && statusName.equals("已通过")) {
                        registerNum += 1;
                    }
                }
            }
            //审批通过之后报名成功的数量
            int passedAfter = businessUtil.getRegisterData(activityId).getInteger("passed");
            Preconditions.checkArgument(passedAfter > 0 && passedAfter == (passedBefore + 1), "审批的通过的人数为:" + registerNum);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("报名列表-审批通过1条，报名成功&报名成功列表");
        }
    }

    @Test()
    public void justTry1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            System.err.println("-------:"+businessUtil.getRecruitActivityDetailDate(971L));
    } catch (AssertionError | Exception e) {
            collectMessage(e) ;
        } finally {
            saveData("ceshiya ");
        }
    }

    /**
     * 报名列表-审批不通过1条，报名失败
     */
    @Test
    public void activityRegisterDate10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int registerNum = 0;
            //获取进行中的活动存在待审批数量的ID
            List<Long> ids = businessUtil.getRecruitActivityWorkingApproval();
            if(ids.size()>0){
                System.err.println("ids:"+ids);
                sleep(3);
                //审批通过之前报名成功的数量
                int failedBefore = businessUtil.getRegisterData(ids.get(0)).getInteger("failed");
                System.err.println("failedBefore:"+failedBefore);
                //报名待审批的ID合集
                List<Long> idArray = businessUtil.registerApproval(ids.get(0));
                System.err.println("idArray:"+idArray);
                //审批不通过其中一条报名
                String message=businessUtil.getRegisterApprovalReject(ids.get(0), idArray.get(0));
                System.err.println("----审批不通过其中一条报名message-------:"+message);
                //审批通过的活动人数
                int pages = businessUtil.getRegisterPage(ids.get(0)).getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    IScene scene = ManageRegisterPageScene.builder().page(page).size(10).activityId(ids.get(0)).build();
                    JSONArray List = visitor.invokeApi(scene).getJSONArray("list");
                    for (int i = 0; i < List.size(); i++) {
                        Long idOne = List.getJSONObject(i).getLong("id");
                        String statusName = List.getJSONObject(i).getString("status_name");
                        if (idArray.get(0).equals(idOne) && statusName.equals("已拒绝")) {
                            int num = List.getJSONObject(i).getInteger("register_num");
                            registerNum += num;
                            System.err.println("----registerNum-------:"+registerNum);
                        }
                    }
                }
                //审批通过之后报名成功的数量
                int failedAfter = businessUtil.getRegisterData(ids.get(0)).getInteger("failed");
                System.err.println("----failedAfter-------:"+failedAfter);
                Preconditions.checkArgument(failedAfter > 0 && failedAfter == (failedBefore + registerNum), "审批不通过的人数为:" + registerNum);

            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("报名列表-审批不通过1条，报名失败");
        }
    }

    /**
     * 活动报名列---【活动名额】=创建活动时填写数量--活动名额有限     ok
     */
    @Test
    public void activityRegisterDate11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取【进行中】的招募活动ID
            List<Long> ids = businessUtil.getRecruitActivityWorking();
            //活动详情中【活动名额】
            IScene scene = ManageDetailScene.builder().id(ids.get(0)).build();
            JSONObject response = visitor.invokeApi(scene).getJSONObject("recruit_activity_info");
            Boolean isLimitQuota = response.getBoolean("is_limit_quota");
            if (isLimitQuota.equals(true)) {
                int num = response.getInteger("quota");
                //报名数据的返回值
                JSONObject dataRes = businessUtil.getRegisterData(ids.get(0));
                //活动名额
                int quota = dataRes.getInteger("quota");
                Preconditions.checkArgument(quota >= num, "活动名额为：" + quota + "  创建活动时填写数量：" + num);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动报名列---【活动名额】=创建活动时填写数量");
        }
    }

    /**
     * 活动审批--①全部审批=【全部】列表数  ②全部审批=【待审核】【审核通过】【审核未通过】列表数加和      ok
     */
    @Test
    public void activityApproval12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取活动审批的数据
            JSONObject response = businessUtil.getActivityApprovalDate();
            //全部活动
            int total = response.getInteger("total");
            //待审批活动
            int wait = response.getInteger("wait");
            //通过的活动
            int passed = response.getInteger("passed");
            //未通过活动
            int failed = response.getInteger("failed");
            //获取【全部tab】的列表数
            IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
            JSONObject response1 = visitor.invokeApi(scene);
            int totalNum = response1.getInteger("total");
            //获取【待审核tab】的列表数
            int waitNum = businessUtil.getActivityManagePage(ActivityApprovalStatusEnum.PENDING.getId()).getInteger("total");
            //获取【审核通过tab】的列表数
            int passedNum = businessUtil.getActivityManagePage(ActivityApprovalStatusEnum.PASSED.getId()).getInteger("total");
            //获取【审核未通过tab】的列表数
            int failedNum = businessUtil.getActivityManagePage(ActivityApprovalStatusEnum.REJECT.getId()).getInteger("total");
            Preconditions.checkArgument(total <= totalNum, "活动审批数据-全部活动为：" + total + "审批列表中全部活动的列表数为：" + totalNum);
            Preconditions.checkArgument(total == (waitNum + passedNum + failedNum), "活动审批数据-全部活动为：" + total + "【待审核】【审核通过】【审核未通过】列表数加和为：" + (waitNum + passedNum + failedNum));
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动审批--①全部审批=【全部】列表数  ②全部审批=【待审核】【审核通过】【审核未通过】列表数加和");
        }
    }

    /**
     * 活动审批--①待审批=活动状态为待审批的列表数  ②审批通过=活动状态为审批通过的列表数 ③审批未通过=活动状态为审批未通过的列表数   ok
     */
    @Test
    public void activityApproval13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取活动审批的数据
            JSONObject response = businessUtil.getActivityApprovalDate();
            //全部活动
            int total = response.getInteger("total");
            //待审批活动
            int wait = response.getInteger("wait");
            //通过的活动
            int passed = response.getInteger("passed");
            //未通过活动
            int failed = response.getInteger("failed");

            //获取【全部tab】的列表数
            IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
            JSONObject response1 = visitor.invokeApi(scene);
            int totalNum = response1.getInteger("total");
            //获取【待审核tab】的列表数
            int waitNum = businessUtil.getActivityManagePage(ActivityApprovalStatusEnum.PENDING.getId()).getInteger("total");
            //获取【审核通过tab】的列表数
            int passedNum = businessUtil.getActivityManagePage(ActivityApprovalStatusEnum.PASSED.getId()).getInteger("total");
            //获取【审核未通过tab】的列表数
            int failedNum = businessUtil.getActivityManagePage(ActivityApprovalStatusEnum.REJECT.getId()).getInteger("total");
            Preconditions.checkArgument(wait == waitNum, "待审批的数量为：" + wait + "活动审核列表中待审核的数量为：" + waitNum);
            Preconditions.checkArgument(passed == passedNum, "审批通过的数量为：" + passed + "活动审核列表中审批通过的数量为：" + passedNum);
            Preconditions.checkArgument(failed == failedNum, "审批未通过的数量为：" + failed + "活动审核列表中审批未通过的数量为：" + failedNum);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动审批--①待审批=活动状态为待审批的列表数  ②审批通过=活动状态为审批通过的列表数 ③审批未通过=活动状态为审批未通过的列表数");
        }
    }

    /**
     * 活动审批--审批通过，待审批-1&审批通过+1      ok
     */
    @Test
    public void activityApproval14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建待审批的活动
            Long id= businessUtil.createRecruitActivityApproval();
            System.err.println(id);
            if (id > 0) {
                //获取活动审批的数据
                JSONObject response = businessUtil.getActivityApprovalDate();
                //待审批活动
                int waitBefore = response.getInteger("wait");
                //通过的活动
                int passedBefore = response.getInteger("passed");
                //审批通过其中一条
                String message=businessUtil.getApprovalPassed(id);
                System.err.println("--------------"+message);
                //获取审批后活动审批的数据
                JSONObject response1 = businessUtil.getActivityApprovalDate();
                //待审批活动
                int waitAfter = response1.getInteger("wait");
                //通过的活动
                int passedAfter = response1.getInteger("passed");
                //获取活动审批后的状态
                int status = businessUtil.getActivityStatus(id);
                Preconditions.checkArgument(waitAfter == (waitBefore - 1), "活动审批数后的待审批数量：" + waitAfter + "活动审批数前的待审批数量：" + waitBefore);
                Preconditions.checkArgument(passedAfter == (passedBefore + 1), "活动审批数后的审批通过数量：" + passedAfter + "活动审批数前的审批通过数量：" + passedBefore);
                Preconditions.checkArgument(status == ActivityStatusEnum.WAITING_START.getId()||status == ActivityStatusEnum.PASSED.getId(), "活动审批数后活动的状态应为【进行中】或者【未开始】，此时为为：" + status);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动审批--审批通过，待审批-1&审批通过+1");
        }
    }

    /**
     * 活动审批--审批驳回，待审批-1&审批未通过+1          ok
     */
    @Test
    public void activityApproval15() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取活动审批的数据
            JSONObject response = businessUtil.getActivityApprovalDate();
            //待审批活动
            int waitBefore = response.getInteger("wait");
            //未通过活动
            int failedBefore = response.getInteger("failed");
            //获取进行中的活动存在待审批ID合集
            List<Long> ids = businessUtil.getActivityWait();
            if (ids.size() > 0) {
                //审批通过其中一条
                businessUtil.getApprovalReject(ids.get(0));
                //获取审批后活动审批的数据
                JSONObject response1 = businessUtil.getActivityApprovalDate();
                //待审批活动
                int waitAfter = response1.getInteger("wait");
                //未通过活动
                int failedAfter = response1.getInteger("failed");
                System.out.println("11111111" + response);
                System.out.println("2222222222：" + response1);
                //获取活动审批后的状态
                int status = businessUtil.getActivityStatus(ids.get(0));
                if (waitBefore == 0) {
                    Preconditions.checkArgument(waitAfter == (waitBefore), "活动审批数后的待审批数量：" + waitAfter + "活动审批数前的待审批数量：" + waitBefore);
                    Preconditions.checkArgument(failedAfter == (failedBefore + 1), "活动审批数后的审批未通过数量：" + failedAfter + "活动审批数前的审批未通过数量：" + failedBefore);
                    Preconditions.checkArgument(status == ActivityStatusEnum.REJECT.getId(), "活动审批数后活动的状态应为【审核未通过】，此时为为：" + status);
                } else {
                    Preconditions.checkArgument(waitAfter == (waitBefore - 1), "活动审批数后的待审批数量：" + waitAfter + "活动审批数前的待审批数量：" + waitBefore);
                    Preconditions.checkArgument(failedAfter == (failedBefore + 1), "活动审批数后的审批未通过数量：" + failedAfter + "活动审批数前的审批未通过数量：" + failedBefore);
                    Preconditions.checkArgument(status == ActivityStatusEnum.REJECT.getId(), "活动审批数后活动的状态应为【审核未通过】，此时为为：" + status);
                }
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("活动审批--审批驳回，待审批-1&审批未通过+1");
        }
    }

    /**
     * 小策程序报名活动    ok
     */
    @Test(enabled = false)
    public void appletActivityRegister() {
        try {
            //创建活动
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            Long activityId = businessUtil.createRecruitActivity(voucherId, true, 0, true);
            //审批活动
            businessUtil.getApprovalPassed(activityId);
            //小程序报名活动
            businessUtil.activityRegisterApplet(activityId, "13373166806", "郭丽雅", 2, "1513814362@qq.com", "22", "女","其他");

            System.out.println("----------------" + response);
        } catch (AssertionError | Exception e) {
            e.printStackTrace();
        } finally {
            saveData("小程序报名活动");
        }
    }


    /**
     * ---------------------------------------------------------招募活动的各个状态---------------------------------------------------
     */


    /**
     * 招募活动-【待审核】的活动-查看
     * 2021-3-17
     */
    @Test(description = "招募活动-【待审核】的活动-查看")
    public void revokeActivityCheck() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待审核活动的ID
            List<Long> ids = businessUtil.getRecruitActivityWaitingApproval();
            //获取待审核的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【待审核】的活动-查看");
        }
    }
    /**
     * 招募活动-撤回【待审批】的活动   ok
     */
    @Test(description = "招募活动-【待审批】的活动撤回")
    public void revokeApprovalsActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待审批活动的ID
            List<Long> ids = businessUtil.getRecruitActivityWaitingApproval();
            //撤回待审批的活动
            String message = businessUtil.getRevokeActivity(ids.get(0));
            //获取刚才通过的活动的状态
            int status = businessUtil.getActivityStatus(ids.get(0));
            Preconditions.checkArgument(status == ActivityStatusEnum.REVOKE.getId(), "撤回的待审批的活动,现活动的状态为：" + ActivityStatusEnum.REVOKE.getStatusName());

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-活动审批未通过");
        }
    }

    /**
     * 招募活动-【待审核】的活动-置顶
     * 2021-3-17
     */
    @Test(description = "招募活动-【待审核】的活动-置顶")
    public void pendingActivityTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待审核活动的ID
            List<Long> ids = businessUtil.getRecruitActivityWaitingApproval();
            //获取待审核的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //置顶【待审批的活动】
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            System.out.println(title+"-------"+message);
            Preconditions.checkArgument(message.equals("活动未审核通过！暂不能置顶"), "现在活动的名称为：" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【待审核】的活动-置顶");
        }
    }

    /**
     * 招募活动-【已撤销】的活动-查看
     * 2021-3-17
     */
    @Test(description = "招募活动-【已撤销】的活动-查看")
    public void pendingActivityCheck() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已撤销活动的ID
            List<Long> ids = businessUtil.getRecruitActivityRevoke();
            //获取已撤销的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            System.out.println(title+"-------"+title1);
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【已撤销】的活动-查看");
        }
    }

    /**
     * 招募活动-【已撤销】的活动-置顶
     * 2021-3-17
     */
    @Test(description = "招募活动-【已撤销】的活动-置顶")
    public void revokeActivityTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已撤销活动的ID
            List<Long> ids = businessUtil.getRecruitActivityRevoke();
            //获取已撤销的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //置顶【已撤销的活动】
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            System.out.println(title+"-------"+message);
            Preconditions.checkArgument(message.equals("活动未审核通过！暂不能置顶"), "置顶已撤销的活动的相关提示:" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【已撤销】的活动-置顶");
        }
    }

    /**
     * 招募活动-【已撤销】的活动-删除
     * 2021-3-17
     */
    @Test(description = "招募活动-【已撤销】的活动-删除")
    public void revokeActivityDele() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已撤销活动的ID
            List<Long> ids = businessUtil.getRecruitActivityRevoke();
            //删除已撤销的活动
            String message = businessUtil.getDelActivity(ids.get(0));
            Preconditions.checkArgument(message.equals("success"), "已撤销的活动删除失败");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【已撤销】的活动-删除");
        }
    }

    /**
     * 招募活动-【已撤销】的活动-编辑：名称，活动规则----招募活动
     * 2021-3-17
     */
    @Test(description = "招募活动-【已撤销】的活动-编辑")
    public void revokeActivityEdit(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已撤销活动的ID
            List<Long> ids =  businessUtil.getRecruitActivityRevoke();
            System.err.println("----ids:"+ids.get(0));
            //编辑已撤销的活动
            //获取一个卡券
            Long voucherId = businessUtil.getVoucherId();
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(businessUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            JSONArray registerInformationList = businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, Math.min(businessUtil.getVoucherAllowUseInventory(voucherId),3));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, "", "", 10);
            String subject= supporterUtil.getSubjectType();
            IScene scene = ManageRecruitEditScene.builder()
                    .type(2)
                    .id(ids.get(0))
                    .title(pp.editTitle)
                    .participationLimitType(0)
                    .rule(pp.EditRule)
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .subjectType(subject)
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("RED_PAPER")
                    .picList(picList)
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(2)
                    .address(pp.address)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(0)
                    .isNeedApproval(true)
                    .rewardVouchers(registerObject)
                    .voucherValid(voucherValid)
                    .address("呀呀呀呀呀呀呀呀呀呀地址")
                    .build();
            String message = visitor.invokeApi(scene,false).getString("message");
            System.out.println("-----"+message);
            //获取活动详情
            IScene scene1=ManageDetailScene.builder().id(ids.get(0)).build();
            JSONObject object=visitor.invokeApi(scene1);
            String title=object.getString("title");
            String rule=object.getJSONObject("recruit_activity_info").getString("rule");
            String participationLimitType=object.getString("participation_limit_type");
            String startDate=object.getString("start_date");
            String endDate=object.getString("end_date");
            String applyStart=object.getJSONObject("recruit_activity_info").getString("apply_start");
            String applyEnd=object.getJSONObject("recruit_activity_info").getString("apply_end");
            String quota=object.getJSONObject("recruit_activity_info").getString("quota");
            String subjectType=object.getString("subject_type");
            String label=object.getString("label");
            String address=object.getJSONObject("recruit_activity_info").getString("address");
            Long id=object.getJSONObject("recruit_activity_info").getJSONArray("reward_vouchers").getJSONObject(0).getLong("id");
            String approval=object.getJSONObject("recruit_activity_info").getString("is_need_approval");
            String picName=object.getJSONArray("pic_list").getJSONObject(0).getString("pic_path");
            String content = businessUtil.changeRecordPage(ids.get(0)).getJSONArray("list").getJSONObject(0).getString("content");
            System.out.println("---------"+picName);
            System.out.println(title+"--------"+rule+"--------"+participationLimitType+"--------"+startDate+"--------"+endDate+"--------"+applyStart+"--------"+applyEnd+"--------"+quota+"--------"+subjectType+"--------"+label+"--------"+address+"--------"+approval+"--------"+id+"--------"+voucherId);
            Preconditions.checkArgument(message.equals("success")&&title.equals(pp.editTitle)&&rule.equals(pp.EditRule), "已撤销的活动编辑失败1");
            Preconditions.checkArgument(participationLimitType.equals("0")&&quota.equals("2"), "已撤销的活动编辑失败2");
            Preconditions.checkArgument(startDate.equals(businessUtil.getStartDate())&&endDate.equals(businessUtil.getEndDate()), "已撤销的活动编辑失败3");
            Preconditions.checkArgument(applyStart.equals(businessUtil.getStartDate())&&applyEnd.equals(businessUtil.getEndDate()), "已撤销的活动编辑失败4");
            Preconditions.checkArgument(subjectType.equals(subject)&&label.equals("RED_PAPER"), "已撤销的活动编辑失败5");
//            Preconditions.checkArgument(picName.contains("活动.jpeg"), "已撤销的活动编辑失败");
            Preconditions.checkArgument(address.equals("呀呀呀呀呀呀呀呀呀呀地址")&&id.equals(voucherId)&&approval.equals("true"), "已撤销的活动编辑失败6");
            Preconditions.checkArgument(content.equals("编辑活动"),"变更记录中的变更事项没有更新");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【已撤销】的活动-编辑");
        }
    }

    /**
     * 招募活动-【审核未通过】的活动-查看
     * 2021-3-17
     */
    @Test(description = "招募活动-【审核未通过】的活动-查看")
    public void rejectActivityCheck() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取审核未通过活动的ID
            List<Long> ids = businessUtil.getRecruitActivityReject();
            //获取审核未通过的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            System.out.println(title+"-------"+title1);
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【审核未通过】的活动-查看");
        }
    }
    /**
     * 招募活动-【审核未通过】的活动-置顶
     * 2021-3-17
     */
    @Test(description = "招募活动-【审核未通过】的活动-置顶")
    public void rejectActivityTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取审核未通过活动的ID
            List<Long> ids = businessUtil.getRecruitActivityReject();
            //获取审核未通过的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //置顶【审核未通过的活动】
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            System.out.println(title+"-------"+message);
            Preconditions.checkArgument(message.equals("活动未审核通过！暂不能置顶"), "置顶审核不通过的活动的相关提示:" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【审核未通过】的活动-置顶");
        }
    }
    /**
     * 招募活动-删除【审核未通过】的活动       ok
     */
    @Test(description = "招募活动-删除【审核未通过】的活动")
    public void delWorkingActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取审核未通过活动的ID
            List<Long> ids = businessUtil.getRecruitActivityReject();
            //删除进行中的活动
            String message = businessUtil.getDelActivity(ids.get(0));
            Preconditions.checkArgument(message.equals("success"), "删除【审核未通过】的活动的message为：" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-删除【审核未通过】的活动");
        }
    }

    /**
     * 招募活动-【审核未通过】的活动-编辑：名称，活动规则----招募活动
     * 2021-3-17
     */
    @Test(description = "招募活动-【审核未通过】的活动-编辑")
    public void rejectActivityEdit(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取审核未通过活动的ID
            List<Long> ids =  businessUtil.getRecruitActivityReject();
            System.err.println("----ids:"+ids.get(0));
            //编辑审核未通过的活动
            //获取一个卡券
            Long voucherId = businessUtil.getVoucherId();
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(businessUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            JSONArray registerInformationList = businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, Math.min(businessUtil.getVoucherAllowUseInventory(voucherId),3));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, "", "", 10);
            String subject= supporterUtil.getSubjectType();
            IScene scene = ManageRecruitEditScene.builder()
                    .type(2)
                    .id(ids.get(0))
                    .title(pp.editTitle)
                    .participationLimitType(0)
                    .rule(pp.EditRule)
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .subjectType(subject)
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("RED_PAPER")
                    .picList(picList)
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(2)
                    .address(pp.address)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(0)
                    .isNeedApproval(true)
                    .rewardVouchers(registerObject)
                    .voucherValid(voucherValid)
                    .address("呀呀呀呀呀呀呀呀呀呀地址")
                    .build();
            String message = visitor.invokeApi(scene,false).getString("message");
            System.out.println("-----"+message);
            //获取活动详情
            IScene scene1=ManageDetailScene.builder().id(ids.get(0)).build();
            JSONObject object=visitor.invokeApi(scene1);
            String title=object.getString("title");
            String rule=object.getJSONObject("recruit_activity_info").getString("rule");
            String participationLimitType=object.getString("participation_limit_type");
            String startDate=object.getString("start_date");
            String endDate=object.getString("end_date");
            String applyStart=object.getJSONObject("recruit_activity_info").getString("apply_start");
            String applyEnd=object.getJSONObject("recruit_activity_info").getString("apply_end");
            String quota=object.getJSONObject("recruit_activity_info").getString("quota");
            String subjectType=object.getString("subject_type");
            String label=object.getString("label");
            String address=object.getJSONObject("recruit_activity_info").getString("address");
            Long id=object.getJSONObject("recruit_activity_info").getJSONArray("reward_vouchers").getJSONObject(0).getLong("id");
            String approval=object.getJSONObject("recruit_activity_info").getString("is_need_approval");
            String picName=object.getJSONArray("pic_list").getJSONObject(0).getString("pic_path");
            String content = businessUtil.changeRecordPage(ids.get(0)).getJSONArray("list").getJSONObject(0).getString("content");

            System.out.println("---------"+picName);
            System.out.println(title+"--------"+rule+"--------"+participationLimitType+"--------"+startDate+"--------"+endDate+"--------"+applyStart+"--------"+applyEnd+"--------"+quota+"--------"+subjectType+"--------"+label+"--------"+address+"--------"+approval+"--------"+id+"--------"+voucherId);
            Preconditions.checkArgument(message.equals("success")&&title.equals(pp.editTitle)&&rule.equals(pp.EditRule), "审核未通过的活动编辑失败1");
            Preconditions.checkArgument(participationLimitType.equals("0")&&quota.equals("2"), "审核未通过的活动编辑失败2");
            Preconditions.checkArgument(startDate.equals(businessUtil.getStartDate())&&endDate.equals(businessUtil.getEndDate()), "审核未通过的活动编辑失败3");
            Preconditions.checkArgument(applyStart.equals(businessUtil.getStartDate())&&applyEnd.equals(businessUtil.getEndDate()), "审核未通过的活动编辑失败4");
            Preconditions.checkArgument(subjectType.equals(subject)&&label.equals("RED_PAPER"), "审核未通过的活动编辑失败5");
//            Preconditions.checkArgument(picName.contains("活动.jpeg"), "审核未通过的活动编辑失败");
            Preconditions.checkArgument(address.equals("呀呀呀呀呀呀呀呀呀呀地址")&&id.equals(voucherId)&&approval.equals("true"), "审核未通过的活动编辑失败6");
            Preconditions.checkArgument(content.equals("编辑活动"),"变更记录中的变更事项没有更新");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【审核未通过】的活动-编辑");
        }
    }
    /**
     * 招募活动-【已取消】的活动-查看
     * 2021-3-17
     */
    @Test(description = "招募活动-【已取消】的活动-查看")
    public void canceledActivityCheck() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已取消活动的ID
            List<Long> ids = businessUtil.getRecruitActivityCancel();
            //获取已取消的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            System.out.println(title+"-------"+title1);
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【已取消】的活动-查看");
        }
    }

    /**
     * 招募活动-【已取消】的活动-置顶
     * 2021-3-17
     */
    @Test(description = "招募活动-【已取消】的活动-置顶")
    public void canceledActivityTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已取消活动的ID
            List<Long> ids = businessUtil.getRecruitActivityCancel();
            //获取已取消的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //置顶【已取消的活动】
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            System.out.println(title+"-------"+message);
            Preconditions.checkArgument(message.equals("当前状态【 已取消】！不能置顶"), "置顶已取消的活动的相关提示:" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【已取消】的活动-置顶");
        }
    }

    /**
     * 招募活动-【进行中】的活动-查看
     * 2021-3-17
     */
    @Test(description = "招募活动-【进行中】的活动-查看")
    public void workingActivityCheck(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getRecruitActivityWorking();
            //获取进行中的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            System.out.println(title+"-------"+title1);
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【进行中】的活动-查看");
        }
    }

    /**
     * 招募活动-【进行中】的活动-置顶
     * 2021-3-17
     */
    @Test(description = "招募活动-【进行中】的活动-置顶")
    public void workingActivityTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getRecruitActivityWorking();
            //获取进行中的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //置顶【进行中的活动】
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            System.out.println(title+"-------"+message);
            //小程序中第一个为此活动
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            JSONObject response=businessUtil.appointmentActivityTitleNew();
            String title1=response.getJSONArray("list").getJSONObject(0).getString("title");
            Preconditions.checkArgument(title1.equals(title), "PC进行中活动的标题为：" + title+"小程序中的更多中的活动标题为："+title1);
            Preconditions.checkArgument(message.equals("success"), "置顶进行中的活动的相关提示:" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【进行中】的活动-置顶");
        }
    }

    /**
     * 招募活动-推广【进行中】的活动
     */
    @Test(description = "招募活动-推广【进行中】的活动")
    public void promotionWorkingActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getRecruitActivityWorking();
            //推广进行中的活动
            String appletCodeUrl = businessUtil.getPromotionActivity(ids.get(0));
            Preconditions.checkArgument(!appletCodeUrl.equals(""), "推广【进行中】的活动的小程序二维码的返回值为空");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-推广【进行中】的活动");
        }
    }

    /**
     * 招募活动-取消【进行中】的活动     ok
     */
    @Test(description = "招募活动-推广【进行中】的活动")
    public void cancelWorkingActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getRecruitActivityWorking();
            //取消进行中的活动
            businessUtil.getCancelActivity(ids.get(0));
            //获取活动的状态
            int status = businessUtil.getActivityStatus(ids.get(0));
            Preconditions.checkArgument(status == ActivityStatusEnum.CANCELED.getId(), "现在活动的状态为：" + status);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-取消【进行中】的活动");
        }
    }

    /**
     * 招募活动-【进行中】的活动-编辑：名称，活动规则----招募活动
     * 2021-3-17
     */
    @Test(description = "招募活动-【进行中】的活动-编辑")
    public void workingActivityEdit(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            List<Long> ids =  businessUtil.getRecruitActivityWorking();
            System.err.println("----ids:"+ids.get(0));
            //编辑活动名称、活动名额
            String message = businessUtil.activityEditScene(ids.get(0));
            System.err.println("--------" + message);
            //获取活动详情中编辑后的标题和活动规则
            String title = businessUtil.getRecruitActivityDetailDate1(ids.get(0)).getString("title");
            String rule = businessUtil.getRecruitActivityDetailDate(ids.get(0)).getString("rule");
            //获取活动状态
            int status=businessUtil.getActivityStatus(ids.get(0));
            String content = businessUtil.changeRecordPage(ids.get(0)).getJSONArray("list").getJSONObject(0).getString("content");
            System.out.println(title+"--------"+rule);
            Preconditions.checkArgument(message.equals("success")&&title.contains("编辑过后的招募活动")&&rule.equals(pp.EditRule)&&content.equals("编辑活动"), "进行中的活动编辑失败");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【进行中】的活动-编辑");
        }
    }

    /**
     * 招募活动-【未开始】的活动-查看
     * 2021-3-17
     */
    @Test(description = "招募活动-【未开始】的活动-查看")
    public void waitingStarActivityCheck(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取未开始活动的ID
            List<Long> ids = businessUtil.getRecruitActivityWaitingStar();
            //获取未开始的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            System.out.println(title+"-------"+title1);
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【未开始】的活动-查看");
        }
    }

    /**
     * 招募活动-【未开始】的活动-置顶
     * 2021-3-17
     */
    @Test(description = "招募活动-【未开始】的活动-置顶")
    public void waitingStarActivityTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            ///创建未开始的活动
            Long id=businessUtil.createRecruitActivity();
            //审批活动
            businessUtil.getApprovalPassed(id);
            //获取未开始的活动名称
            String title=businessUtil.getActivityTitle(id);
            //置顶【未开始的活动】
            IScene scene=ActivityManageTopScene.builder().id(id).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            System.out.println(title+"-------"+message);
            //小程序中第一个为此活动
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            JSONObject response=businessUtil.appointmentActivityTitleNew();
            String title1=response.getJSONArray("list").getJSONObject(0).getString("title");
            Preconditions.checkArgument(title1.equals(title), "PC未开始活动的ID为：" + title+"小程序中的更多中的活动ID为："+title);
            Preconditions.checkArgument(message.equals("success"), "置顶未开始的活动的相关提示:" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【未开始】的活动-置顶");
        }
    }
    /**
     * 招募活动-取消【未开始】的活动
     */
    @Test(description = "招募活动-推广【未开始】的活动")
    public void cancelWaitingStarActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取未开始活动的ID
            List<Long> ids = businessUtil.getRecruitActivityWaitingStar();
            //取消未开始的活动
            businessUtil.getCancelActivity(ids.get(0));
            //获取活动的状态
            int status = businessUtil.getActivityStatus(ids.get(0));
            System.err.println("------"+status);
            Preconditions.checkArgument(status == ActivityStatusEnum.CANCELED.getId(), "现在活动的状态为：" + status);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-取消【未开始】的活动");
        }
    }

    /**
     * 招募活动-推广【未开始】的活动  推广
     */
    @Test(description = "招募活动-推广【未开始】的活动")
    public void promotionWaitingStarActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取未开始活动的ID
            List<Long> ids = businessUtil.getRecruitActivityWaitingStar();
            //推广未开始的活动
            String appletCodeUrl = businessUtil.getPromotionActivity(ids.get(0));
            System.err.println(appletCodeUrl+"--------"+ids);
            Preconditions.checkArgument(!appletCodeUrl.equals(""), "推广【未开始】的活动的小程序二维码的返回值为空");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-推广【未开始】的活动");
        }
    }

    /**
     * 招募活动-【未开始】的活动-编辑：名称，活动规则----招募活动
     * 2021-3-17
     */
    @Test(description = "招募活动-【进行中】的活动-编辑")
    public void promotionRecruitActivityEdit(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取未开始活动的ID
            List<Long> ids =  businessUtil.getRecruitActivityWaitingStar();
            System.err.println("----ids:"+ids.get(0));
            //编辑活动名称、活动名额
            String message = businessUtil.activityEditScene(ids.get(0));
            System.err.println("--------" + message);
            //获取活动详情中编辑后的标题和活动规则
            String title = businessUtil.getRecruitActivityDetailDate1(ids.get(0)).getString("title");
            String rule = businessUtil.getRecruitActivityDetailDate(ids.get(0)).getString("rule");
            //获取活动状态
            int status=businessUtil.getActivityStatus(ids.get(0));
            String content = businessUtil.changeRecordPage(ids.get(0)).getJSONArray("list").getJSONObject(0).getString("content");

            System.out.println(title+"--------"+rule);
            Preconditions.checkArgument(message.equals("success")&&title.contains("编辑过后的招募活动")&&rule.equals(pp.EditRule)&&content.equals("编辑活动"), "进行中的活动编辑失败");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【未开始】的活动-编辑");
        }
    }


    /**
     * 招募活动-【已过期】的活动-查看
     * 2021-3-17
     */
    @Test(description = "招募活动-【已过期】的活动-查看")
    public void FinishActivityCheck(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已过期活动的ID
            List<Long> ids = businessUtil.getRecruitActivityFinish();
           if(ids.size()>0){
               //获取已过期的活动名称
               String title=businessUtil.getActivityTitle(ids.get(0));
               //获取活动详情中的此活动的名称
               IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
               String title1=visitor.invokeApi(scene).getString("title");
               System.out.println(title+"-------"+title1);
               Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
           }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【已过期】的活动-查看");
        }
    }

    /**
     * 招募活动-【已过期】的活动-置顶
     * 2021-3-17
     */
    @Test(description = "招募活动-【已过期】的活动-置顶")
    public void FinishActivityTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已过期活动的ID
            List<Long> ids = businessUtil.getRecruitActivityFinish();
           if(ids.size()>0){
               //置顶【已过期的活动】
               IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
               String message=visitor.invokeApi(scene,false).getString("message");
               Preconditions.checkArgument(message.equals("当前状态【 已结束】！不能置顶"), "置顶已过期的活动的相关提示:" + message);
           }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【已过期】的活动-置顶");
        }
    }


    /**
     * ---------------------------------裂变活动的各个状态-----------------------------------------------------------------
     */


    /**
     * 裂变活动-【待审核】的活动-查看
     * 2021-3-17
     */
    @Test(description = "裂变活动-【待审核】的活动-查看")
    public void revokeFissionActivityCheck() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待审核活动的ID
            List<Long> ids = businessUtil.getFissionActivityWaitingApproval();
            //获取待审核的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【待审核】的活动-查看");
        }
    }
    /**
     * 裂变活动-撤回【待审批】的活动   ok
     */
    @Test(description = "裂变活动-【待审批】的活动撤回")
    public void revokeApprovalsFissionActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待审批活动的ID
            List<Long> ids = businessUtil.getFissionActivityWaitingApproval();
            //撤回待审批的活动
            String message = businessUtil.getRevokeActivity(ids.get(0));
            //获取刚才通过的活动的状态
            int status = businessUtil.getActivityStatus(ids.get(0));
            Preconditions.checkArgument(status == ActivityStatusEnum.REVOKE.getId(), "撤回的待审批的活动,现活动的状态为：" + ActivityStatusEnum.REVOKE.getStatusName());

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-活动审批未通过");
        }
    }

    /**
     * 裂变活动-【待审核】的活动-置顶
     * 2021-3-17
     */
    @Test(description = "裂变活动-【待审核】的活动-置顶")
    public void pendingFissionActivityTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待审核活动的ID
            List<Long> ids = businessUtil.getFissionActivityWaitingApproval();
            //获取待审核的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //置顶【待审批的活动】
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            System.out.println(title+"-------"+message);
            Preconditions.checkArgument(message.equals("活动未审核通过！暂不能置顶"), "现在活动的名称为：" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【待审核】的活动-置顶");
        }
    }

    /**
     * 裂变活动-【已撤销】的活动-查看
     * 2021-3-17
     */
    @Test(description = "裂变活动-【已撤销】的活动-查看")
    public void pendingFissionActivityCheck() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已撤销活动的ID
            List<Long> ids = businessUtil.getFissionActivityRevoke();
            //获取已撤销的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            System.out.println(title+"-------"+title1);
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【已撤销】的活动-查看");
        }
    }

    /**
     * 裂变活动-【已撤销】的活动-置顶
     * 2021-3-17
     */
    @Test(description = "裂变活动-【已撤销】的活动-置顶")
    public void revokeFissionActivityTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已撤销活动的ID
            List<Long> ids = businessUtil.getFissionActivityRevoke();
            //获取已撤销的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //置顶【已撤销的活动】
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            System.out.println(title+"-------"+message);
            Preconditions.checkArgument(message.equals("活动未审核通过！暂不能置顶"), "置顶已撤销的活动的相关提示:" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【已撤销】的活动-置顶");
        }
    }

    /**
     * 裂变活动-【已撤销】的活动-删除
     * 2021-3-17
     */
    @Test(description = "裂变活动-【已撤销】的活动-删除")
    public void revokeFissionActivityDele() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已撤销活动的ID
            List<Long> ids = businessUtil.getFissionActivityRevoke();
            //删除已撤销的活动
            String message = businessUtil.getDelActivity(ids.get(0));
            Preconditions.checkArgument(message.equals("success"), "已撤销的活动删除失败");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【已撤销】的活动-删除");
        }
    }

    /**
     * 裂变活动-【已撤销】的活动-编辑：名称，活动规则----裂变活动
     * 2021-3-17
     */
    @Test(enabled = true,description = "裂变活动-【已撤销】的活动-编辑")
    public void revokeFissionActivityEdit(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已撤销活动的ID
            List<Long> ids =  businessUtil.getFissionActivityRevoke();
            System.err.println("----ids:"+ids.get(0));
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            List<String> picList = new ArrayList<>();
            picList.add(businessUtil.getPicPath());
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher = businessUtil.getInvitedVoucher(voucherId, 1, String.valueOf(businessUtil.getVoucherAllowUseInventory(voucherId)), 2, "", "", 1);
            JSONObject shareVoucher = businessUtil.getShareVoucher(voucherId, 1, String.valueOf(businessUtil.getVoucherAllowUseInventory(voucherId)), 2, "", "", 1);
            //所属主体
            String subject=supporterUtil.getSubjectType();
            //开始时间和结束时间
            String startTime=businessUtil.getStartDate();
            String endTime=businessUtil.getEndDate();
            //编辑裂变活动
            IScene scene= FissionVoucherEditScene.builder()
                    .id(ids.get(0))
                    .type(1)
                    .participationLimitType(0)
                    .receiveLimitType(0)
                    .title(pp.fissionVoucherNameEdit)
                    .rule(pp.EditFissionRule)
                    .startDate(startTime)
                    .endDate(endTime)
                    .subjectType(subject)
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("RED_PAPER")
                    .picList(picList)
                    .shareNum("2")
                    .shareVoucher(shareVoucher)
                    .invitedVoucher(invitedVoucher)
                    .build();

            //编辑已撤销的活动
            String message=visitor.invokeApi(scene,false).getString("message");
            System.out.println("---------"+message);
            //获取活动详情中编辑后的标题和活动规则
            JSONObject response=businessUtil.getFissionActivityDetailDate1(ids.get(0));
            JSONObject response1=businessUtil.getFissionActivityDetailData(ids.get(0));
            String title = response.getString("title");
            String rule =response1.getString("rule");
            String participationLimitType=response.getString("participation_limit_type");
            String receiveLimitType=response1.getString("receive_limit_type");
            String startDate=response.getString("start_date");
            String endDate=response.getString("end_date");
            String shareNum=response1.getString("share_num");
            String subjectType=response.getString("subject_type");
            String label=response.getString("label");
            String picName=response.getJSONArray("pic_list").getJSONObject(0).getString("pic_path");
            Long invitedVoucherId=response1.getJSONObject("invited_voucher").getLong("id");
            Long shareVoucherId=response1.getJSONObject("share_voucher").getLong("id");
            String content = businessUtil.changeRecordPage(ids.get(0)).getJSONArray("list").getJSONObject(0).getString("content");
            System.out.println(invitedVoucherId+"----------"+voucherId+"----------"+title+"----------"+rule+"--------"+participationLimitType+"----------"+receiveLimitType+"--------"+startTime+"----------"+endTime+"--------"+shareNum+"----------"+subjectType+"--------"+label+"----------"+picName+"--------"+startDate+"----------"+endDate);
            Preconditions.checkArgument(message.equals("success")&&title.contains("编辑过的裂变活动")&&rule.equals(pp.EditFissionRule), "已撤销的活动编辑失败1");
            Preconditions.checkArgument(participationLimitType.equals("0")&&receiveLimitType.equals("0"), "已撤销的活动编辑失败2");
            Preconditions.checkArgument(startDate.equals(startTime)&&endDate.equals(endTime), "已撤销的活动编辑失败3"+startDate+"   "+startTime+"   "+endDate+"   "+endTime);
            Preconditions.checkArgument(shareNum.equals("2")&&subjectType.equals(subject)&&label.equals("RED_PAPER"), "已撤销的活动编辑失败4");
//            Preconditions.checkArgument(picName.contains("活动.jpeg"), "已撤销的活动编辑失败");
            Preconditions.checkArgument(voucherId.equals(invitedVoucherId) && shareVoucherId.equals(voucherId), "已撤销的活动编辑失败5  "+voucherId+"   "+voucherId+"    "+shareVoucherId);
            Preconditions.checkArgument(content.equals("编辑活动"),"变更记录中的变更事项没有更新");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("裂变活动-【已撤销】的活动-编辑");
        }
    }

    /**
     * 裂变活动-【审核未通过】的活动-查看
     * 2021-3-17
     */
    @Test(description = "裂变活动-【审核未通过】的活动-查看")
    public void rejectFissionActivityCheck() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取审核未通过活动的ID
            List<Long> ids = businessUtil.getFissionActivityReject();
            //获取审核未通过的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            System.out.println(title+"-------"+title1);
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【审核未通过】的活动-查看");
        }
    }
    /**
     * 裂变活动-【审核未通过】的活动-置顶
     * 2021-3-17
     */
    @Test(description = "裂变活动-【审核未通过】的活动-置顶")
    public void rejectFissionActivityTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取审核未通过活动的ID
            List<Long> ids = businessUtil.getFissionActivityReject();
            //获取审核未通过的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //置顶【审核未通过的活动】
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            System.out.println(title+"-------"+message);
            Preconditions.checkArgument(message.equals("活动未审核通过！暂不能置顶"), "置顶审核不通过的活动的相关提示:" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【审核未通过】的活动-置顶");
        }
    }
    /**
     * 裂变活动-删除【审核未通过】的活动       ok
     */
    @Test(description = "裂变活动-删除【审核未通过】的活动")
    public void delWorkingFissionActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取审核未通过活动的ID
            List<Long> ids = businessUtil.getFissionActivityReject();
            //删除进行中的活动
            String message = businessUtil.getDelActivity(ids.get(0));
            Preconditions.checkArgument(message.equals("success"), "删除【审核未通过】的活动的message为：" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-删除【审核未通过】的活动");
        }
    }

    /**
     * 裂变活动-【审核未通过】的活动-编辑：名称，活动规则----裂变活动
     * 2021-3-17
     */
    @Test(enabled = true,description = "裂变活动-【审核未通过】的活动-编辑")
    public void rejectFissionActivityEdit(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取审核未通过活动的ID
            List<Long> ids = businessUtil.getFissionActivityReject();
            System.err.println("----ids:"+ids.get(0));
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            List<String> picList = new ArrayList<>();
            picList.add(businessUtil.getPicturePath());
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher = businessUtil.getInvitedVoucher(voucherId, 1, "1", 2, "", "", 1);
            JSONObject shareVoucher = businessUtil.getShareVoucher(voucherId, 1, "1", 2, "", "", 1);
            //所属主体
            String subject=supporterUtil.getSubjectType();
            //开始时间和结束时间
            String startTime=businessUtil.getStartDate();
            String endTime=businessUtil.getEndDate();
            //编辑裂变活动
            IScene scene= FissionVoucherEditScene.builder()
                    .id(ids.get(0))
                    .type(1)
                    .participationLimitType(0)
                    .receiveLimitType(0)
                    .title(pp.fissionVoucherNameEdit)
                    .rule(pp.EditFissionRule)
                    .startDate(startTime)
                    .endDate(endTime)
                    .subjectType(subject)
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("RED_PAPER")
                    .picList(picList)
                    .shareNum("2")
                    .shareVoucher(shareVoucher)
                    .invitedVoucher(invitedVoucher)
                    .build();

            //编辑已撤销的活动
            String message=visitor.invokeApi(scene,false).getString("message");
            System.out.println("---------"+message);
            //获取活动详情中编辑后的标题和活动规则
            JSONObject response=businessUtil.getFissionActivityDetailDate1(ids.get(0));
            JSONObject response1=businessUtil.getFissionActivityDetailData(ids.get(0));
            String title = response.getString("title");
            String rule =response1.getString("rule");
            String participationLimitType=response.getString("participation_limit_type");
            String receiveLimitType=response1.getString("receive_limit_type");
            String startDate=response.getString("start_date");
            String endDate=response.getString("end_date");
            String shareNum=response1.getString("share_num");
            String subjectType=response.getString("subject_type");
            String label=response.getString("label");
            String picName=response.getJSONArray("pic_list").getJSONObject(0).getString("pic_path");
            Long invitedVoucherId=response1.getJSONObject("invited_voucher").getLong("id");
            Long shareVoucherId=response1.getJSONObject("share_voucher").getLong("id");
            String content = businessUtil.changeRecordPage(ids.get(0)).getJSONArray("list").getJSONObject(0).getString("content");
            System.out.println(invitedVoucherId+"----------"+voucherId+"----------"+title+"----------"+rule+"--------"+participationLimitType+"----------"+receiveLimitType+"--------"+startTime+"----------"+endTime+"--------"+shareNum+"----------"+subjectType+"--------"+label+"----------"+picName+"--------"+startDate+"----------"+endDate);
            Preconditions.checkArgument(message.equals("success")&&title.contains("编辑过的裂变活动")&&rule.equals(pp.EditFissionRule), "已撤销的活动编辑失败1");
            Preconditions.checkArgument(participationLimitType.equals("0")&&receiveLimitType.equals("0"), "已撤销的活动编辑失败2");
            Preconditions.checkArgument(startDate.equals(startTime)&&endDate.equals(endTime), "已撤销的活动编辑失败3"+startDate+"   "+startTime+"   "+endDate+"   "+endTime);
            Preconditions.checkArgument(shareNum.equals("2")&&subjectType.equals(subject)&&label.equals("RED_PAPER"), "已撤销的活动编辑失败4");
//            Preconditions.checkArgument(picName.contains("活动.jpeg"), "已撤销的活动编辑失败");
            Preconditions.checkArgument(voucherId.equals(invitedVoucherId) && shareVoucherId.equals(voucherId), "已撤销的活动编辑失败5  "+voucherId+"   "+voucherId+"    "+shareVoucherId);
            Preconditions.checkArgument(content.equals("编辑活动"),"变更记录中的变更事项没有更新");
        }catch(Exception|AssertionError e){
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【审核未通过】的活动-编辑");
        }
    }
    /**
     * 裂变活动-【已取消】的活动-查看
     * 2021-3-17
     */
    @Test(description = "裂变活动-【已取消】的活动-查看")
    public void canceledFissionActivityCheck() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已取消活动的ID
            List<Long> ids = businessUtil.getFissionActivityCancel();
            //获取已取消的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            System.out.println(title+"-------"+title1);
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【已取消】的活动-查看");
        }
    }

    /**
     * 裂变活动-【已取消】的活动-置顶
     * 2021-3-17
     */
    @Test(description = "裂变活动-【已取消】的活动-置顶")
    public void canceledFissionActivityTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已取消活动的ID
            List<Long> ids = businessUtil.getFissionActivityCancel();
            //获取已取消的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //置顶【已取消的活动】
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            System.out.println(title+"-------"+message);
            Preconditions.checkArgument(message.equals("当前状态【 已取消】！不能置顶"), "置顶已取消的活动的相关提示:" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-【已取消】的活动-置顶");
        }
    }

    /**
     * 裂变活动-【进行中】的活动-查看
     * 2021-3-17
     */
    @Test(description = "裂变活动-【进行中】的活动-查看")
    public void workingFissionActivityCheck(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getFissionActivityWorking();
            //获取进行中的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            System.out.println(title+"-------"+title1);
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【进行中】的活动-查看");
        }
    }

    /**
     * 裂变活动-【进行中】的活动-置顶
     * 2021-3-17
     */
    @Test(description = "裂变活动-【进行中】的活动-置顶")
    public void workingFissionActivityTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getFissionActivityWorking();
            //获取进行中的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //置顶【进行中的活动】
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            System.out.println(title+"-------"+message);
            //小程序中第一个为此活动
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            JSONObject response=businessUtil.appointmentActivityTitleNew();
            String title1=response.getJSONArray("list").getJSONObject(0).getString("title");
            Preconditions.checkArgument(title1.equals(title), "PC进行中活动的名称为：" + title+"小程序中的更多中的活动名称为："+title1);
            Preconditions.checkArgument(message.equals("success"), "置顶进行中的活动的相关提示:" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【进行中】的活动-置顶");
        }
    }

    /**
     * 裂变活动-推广【进行中】的活动
     */
    @Test(description = "裂变活动-推广【进行中】的活动")
    public void promotionFissionWorkingActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getFissionActivityWorking();
            //推广进行中的活动
            String appletCodeUrl = businessUtil.getPromotionActivity(ids.get(0));
            Preconditions.checkArgument(!appletCodeUrl.equals(""), "推广【进行中】的活动的小程序二维码的返回值为空");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-推广【进行中】的活动");
        }
    }

    /**
     * 裂变活动-取消【进行中】的活动     ok
     */
    @Test(description = "裂变活动-推广【进行中】的活动")
    public void cancelWorkingFissionActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getFissionActivityWorking();
            //取消进行中的活动
            businessUtil.getCancelActivity(ids.get(0));
            //获取活动的状态
            int status = businessUtil.getActivityStatus(ids.get(0));
            Preconditions.checkArgument(status == ActivityStatusEnum.CANCELED.getId(), "现在活动的状态为：" + status);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-取消【进行中】的活动");
        }
    }

    /**
     * 裂变活动-【进行中】的活动-编辑：名称，活动规则----裂变活动
     * 2021-3-17
     */
    @Test(enabled = true,description = "裂变活动-【进行中】的活动-编辑")
    public void workingFissionActivityEdit(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getFissionActivityWorking();
            System.err.println("----ids:"+ids.get(0));
            //编辑进行中的活动
            IScene scene =businessUtil.fissionActivityEditScene(ids.get(0));
            String message=visitor.invokeApi(scene,false).getString("message");
            System.out.println("---------"+message);
            //获取活动详情中编辑后的标题和活动规则
            String title = businessUtil.getFissionActivityDetailDate1(ids.get(0)).getString("title");
            String rule = businessUtil.getFissionActivityDetailData(ids.get(0)).getString("rule");
            System.out.println(title+"----------"+rule);
            //变更记录中的内容
//            String content = businessUtil.changeRecordPage(ids.get(0)).getJSONArray("list").getJSONObject(0).getString("content");   &&content.equals("编辑活动")
            Preconditions.checkArgument(message.equals("success")&&title.contains("编辑过的裂变活动")&&rule.equals(pp.EditFissionRule), "进行中的活动编辑失败");   } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【进行中】的活动-编辑");
        }
    }

    /**
     * 裂变活动-【未开始】的活动-查看
     * 2021-3-17
     */
    @Test(description = "裂变活动-【未开始】的活动-查看")
    public void waitingStarFissionActivityCheck(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取未开始活动的ID
            List<Long> ids = businessUtil.getFissionActivityWaitingStar();
            //获取未开始的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            System.out.println(title+"-------"+title1);
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【未开始】的活动-查看");
        }
    }

    /**
     * 裂变活动-【未开始】的活动-置顶
     * 2021-3-17
     */
    @Test(description = "裂变活动-【未开始】的活动-置顶")
    public void waitingStarFissionActivityTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取未开始活动的ID
            List<Long> ids = businessUtil.getFissionActivityWaitingStar();
            //获取未开始的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //置顶【未开始的活动】
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            System.out.println(title+"-------"+message);
            //小程序中第一个为此活动
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            JSONObject response=businessUtil.appointmentActivityTitleNew();
            String title1=response.getJSONArray("list").getJSONObject(0).getString("title");
            Preconditions.checkArgument(title1.equals(title), "PC未开始活动的ID为：" + title+"小程序中的更多中的活动标题为："+title1);
            Preconditions.checkArgument(message.equals("success"), "置顶未开始的活动的相关提示:" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【未开始】的活动-置顶");
        }
    }
    /**
     * 裂变活动-取消【未开始】的活动
     */
    @Test(description = "裂变活动-推广【未开始】的活动")
    public void cancelWaitingStarFissionActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建活动
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            Long id = businessUtil.createFissionActivity(voucherId);
            //审批活动
            businessUtil.getApprovalPassed(id);
            //取消未开始的活动
            businessUtil.getCancelActivity(id);
            //获取活动的状态
            int status = businessUtil.getActivityStatus(id);
            System.err.println("------"+status);
            Preconditions.checkArgument(status == ActivityStatusEnum.CANCELED.getId(), "现在活动的状态为：" + status);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-取消【未开始】的活动");
        }
    }

    /**
     * 裂变活动-推广【未开始】的活动  推广
     */
    @Test(description = "裂变活动-推广【未开始】的活动")
    public void promotionWaitingStarFissionActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取未开始活动的ID
            List<Long> ids = businessUtil.getFissionActivityWaitingStar();
            //推广未开始的活动
            String appletCodeUrl = businessUtil.getPromotionActivity(ids.get(0));
            System.err.println(appletCodeUrl+"--------"+ids);
            Preconditions.checkArgument(!appletCodeUrl.equals(""), "推广【未开始】的活动的小程序二维码的返回值为空");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-推广【未开始】的活动");
        }
    }

    /**
     * 裂变活动-【未开始】的活动-编辑：名称，活动规则----裂变活动
     * 2021-3-17
     */
    @Test(enabled = true,description = "裂变活动-【进行中】的活动-编辑")
    public void promotionFissionActivityEdit(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建活动
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            Long id = businessUtil.createFissionActivity(voucherId);
            //审批活动
            businessUtil.getApprovalPassed(id);
            //编辑未开始的活动
            IScene scene =businessUtil.fissionActivityEditScene(id);
            String message=visitor.invokeApi(scene,false).getString("message");
            //获取活动详情中编辑后的标题和活动规则
            String title = businessUtil.getFissionActivityDetailDate1(id).getString("title");
            String rule = businessUtil.getFissionActivityDetailData(id).getString("rule");
            System.out.println(title+"----------"+rule);
//            String content = businessUtil.changeRecordPage(ids.get(0)).getJSONArray("list").getJSONObject(0).getString("content");     &&content.equals("编辑活动")
            Preconditions.checkArgument(message.equals("success")&&title.contains("编辑过的裂变活动")&&rule.equals(pp.EditFissionRule), "未开始的活动编辑失败");} catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【未开始】的活动-编辑");
        }
    }

    /**
     * 裂变活动-【已过期】的活动-查看
     * 2021-3-17
     */
    @Test(description = "裂变活动-【已过期】的活动-查看")
    public void FinishFissionActivityCheck(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已过期活动的ID
            List<Long> ids = businessUtil.getFissionActivityFinish();
           if(ids.size()>0){
               //获取已过期的活动名称
               String title=businessUtil.getActivityTitle(ids.get(0));
               //获取活动详情中的此活动的名称
               IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
               String title1=visitor.invokeApi(scene).getString("title");
               System.out.println(title+"-------"+title1);
               Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
           }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【已过期】的活动-查看");
        }
    }

    /**
     * 裂变活动-【已过期】的活动-置顶
     * 2021-3-17
     */
    @Test(description = "裂变活动-【已过期】的活动-置顶")
    public void FinishFissionActivityTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已过期活动的ID
            List<Long> ids = businessUtil.getFissionActivityFinish();
            if(ids.size()>0){
                //置顶【已过期的活动】
                IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
                String message=visitor.invokeApi(scene,false).getString("message");
                Preconditions.checkArgument(message.equals("当前状态【 已结束】！不能置顶"), "置顶已过期的活动的相关提示:" + message);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【已过期】的活动-置顶");
        }
    }










    /**
     * ==========================创建活动的异常情况===========================
     */

    /**
     * 创建活动-领取次数的异常情况{长度101}     ok
     */
    @Test(description = "创建活动-领取次数的异常情况{长度101}")
    public void fissionVoucherReceiveLimitException1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //添加活动图片
            List<String> picList = new ArrayList<>();
            picList.add(supporterUtil.getPicPath());
            //获取优惠券ID
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher = businessUtil.getInvitedVoucher(voucherId, 1, "1", 2, "", "", 3);
            JSONObject shareVoucher = businessUtil.getShareVoucher(pp.packageId, 2, "1", 2, "", "", 3);
            String[] receiveLimit = pp.receiveLimitException;
            for (int num = 1; num <= 2; num++) {
                for (int i = 0; i < receiveLimit.length; i++) {
                    IScene scene = FissionVoucherAddScene.builder()
                            .type(1)
                            .participationLimitType(0)
                            .receiveLimitType(num)
                            .receiveLimitTimes(receiveLimit[i])
                            .title(pp.fissionVoucherName)
                            .rule(pp.rule)
                            .startDate(businessUtil.getStartDate())
                            .endDate(businessUtil.getEndDate())
                            .subjectType(supporterUtil.getSubjectType())
                            .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                            .label("RED_PAPER")
                            .picList(picList)
                            .shareNum("3")
                            .shareVoucher(shareVoucher)
                            .invitedVoucher(invitedVoucher)
                            .build();
                    String message = visitor.invokeApi(scene, false).getString("message");
                    Preconditions.checkArgument(message.equals("活动期间总次数范围为[1,100]") || message.equals("活动期间每日次数不能为空") || message.equals("活动期间总次数不能为空") || message.equals("活动期间每日次数范围为[1,100]"), "获取期间总次数(1)/每天领取总次数(2)错误的类型为：" + num + "错误的结果为: " + receiveLimit[i]);

                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建活动-领取次数的异常情况{长度101}");
        }
    }

    /**
     * 创建活动-  标题的异常情况{不填写、21位}   ok
     */
    @Test(description = "创建活动-  标题的异常情况{不填写、21位}")
    public void fissionVoucherTitleException2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //添加活动图片
            List<String> picList = new ArrayList<>();
            picList.add(supporterUtil.getPicPath());
            //获取优惠券ID
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher = businessUtil.getInvitedVoucher(voucherId, 1, "1", 2, "", "", 3);
            JSONObject shareVoucher = businessUtil.getShareVoucher(pp.packageId, 2, "1", 2, "", "", 3);
            String[] title = pp.titleException;
            for (int i = 0; i < title.length; i++) {
                IScene scene = FissionVoucherAddScene.builder()
                        .type(1)
                        .participationLimitType(0)
                        .receiveLimitType(0)
                        .title(title[i])
                        .rule(pp.rule)
                        .startDate(businessUtil.getStartDate())
                        .endDate(businessUtil.getEndDate())
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label("RED_PAPER")
                        .picList(picList)
                        .shareNum("3")
                        .shareVoucher(shareVoucher)
                        .invitedVoucher(invitedVoucher)
                        .build();
                String message = visitor.invokeApi(scene, false).getString("message");
                Preconditions.checkArgument(message.equals("活动名称长度为[1,20]") || message.equals("活动名称不能为空"), "创建活动填写的名字为：" + title[i]);

            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建活动-标题异常情况");
        }
    }

    /**
     * 创建活动-分享人数的异常情况{不填写、10000、中文、英文、标点符号}         ok
     */
    @Test(description = "创建活动-分享人数的异常情况{不填写、10000、中文、英文、标点符号}")
    public void fissionVoucherTitleException3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //添加活动图片
            List<String> picList = new ArrayList<>();
            picList.add(supporterUtil.getPicPath());
            //获取优惠券ID
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher = businessUtil.getInvitedVoucher(voucherId, 1, "1", 2, "", "", 3);
            JSONObject shareVoucher = businessUtil.getShareVoucher(pp.packageId, 2, "1", 2, "", "", 3);
            String[] shareNum = pp.shareNumException;
            for (int i = 0; i < shareNum.length; i++) {
                IScene scene = FissionVoucherAddScene.builder()
                        .type(1)
                        .participationLimitType(0)
                        .receiveLimitType(0)
                        .title(pp.fissionVoucherName)
                        .rule(pp.rule)
                        .startDate(businessUtil.getStartDate())
                        .endDate(businessUtil.getEndDate())
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label("RED_PAPER")
                        .picList(picList)
                        .shareNum(shareNum[i])
                        .shareVoucher(shareVoucher)
                        .invitedVoucher(invitedVoucher)
                        .build();
                String message = visitor.invokeApi(scene, false).getString("message");
                Preconditions.checkArgument(message.equals("分享人数不能为空") || message.equals("分享人数范围为[1,50]"), "创建活动分享人数为：" + shareNum[i]);

            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建活动-分享人数的异常情况{不填写、10000、中文、英文、标点符号}");
        }
    }

    /**
     * 创建活动-活动规则异常{不填写，2001位}-----此功能取消
     */
    @Test(description = "创建活动-活动规则异常{2001位}",enabled = false)
    public void fissionVoucherTitleException4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //添加活动图片
            List<String> picList = new ArrayList<>();
            picList.add(supporterUtil.getPicPath());
            //获取优惠券ID
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher = businessUtil.getInvitedVoucher(voucherId, 1, "1", 2, "", "", 3);
            JSONObject shareVoucher = businessUtil.getShareVoucher(pp.packageId, 2, "1", 2, "", "", 3);
            String[] rule = pp.ruleException;
            for (int i = 0; i < rule.length; i++) {
                IScene scene = FissionVoucherAddScene.builder()
                        .type(1)
                        .participationLimitType(0)
                        .receiveLimitType(0)
                        .title(pp.fissionVoucherName)
                        .rule(rule[i])
                        .startDate(businessUtil.getStartDate())
                        .endDate(businessUtil.getEndDate())
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label("RED_PAPER")
                        .picList(picList)
                        .shareNum("3")
                        .shareVoucher(shareVoucher)
                        .invitedVoucher(invitedVoucher)
                        .build();
                String message = visitor.invokeApi(scene, false).getString("message");
                Preconditions.checkArgument(message.equals("活动规则字数为[1,2000]"), "创建活动规则为：" + rule[i]);

            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建活动-活动规则异常{2001位}");
        }
    }

    /**
     * 创建活动-活动图片异常{不填写}      ok
     */
    @Test(description = "创建活动-活动图片异常{不填写}")
    public void fissionVoucherTitleException5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //添加活动图片
            List<String> picList = new ArrayList<>();
            //获取优惠券ID
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher = businessUtil.getInvitedVoucher(voucherId, 1, "1", 2, "", "", 3);
            JSONObject shareVoucher = businessUtil.getShareVoucher(voucherId, 1, "1", 2, "", "", 3);
            IScene scene = FissionVoucherAddScene.builder()
                    .type(1)
                    .participationLimitType(0)
                    .receiveLimitType(0)
                    .title(pp.fissionVoucherName)
                    .rule(pp.rule)
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("RED_PAPER")
                    .picList(picList)
                    .shareNum("3")
                    .shareVoucher(shareVoucher)
                    .invitedVoucher(invitedVoucher)
                    .build();
            String message = visitor.invokeApi(scene, false).getString("message");
            Preconditions.checkArgument(message.equals("图片不能为空"), "创建活动图片为空");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建活动-活动图片异常{不填写}");
        }
    }

    /**
     * 创建活动-活动图片异常{>500K,.gif}}   ok
     */
    @Test
    public void fissionVoucherTitleException6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] paths = {"src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/大于500k.jpg",
                    "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/动图.gif"};
            Arrays.stream(paths).forEach(path -> {
                String picture = new ImageUtil().getImageBinary(path);
                IScene scene = FileUpload.builder().isPermanent(false).permanentPicType(0).pic(picture).ratio(1.5).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                Preconditions.checkArgument(message.equals("success") || message.equals("图片宽高比不符合null的要求"), "活动图片的异常情况为：" + path);
            });
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建活动-活动图片异常{>500K,.gif}");
        }
    }

    /**
     * 创建裂变活动-分享者优惠券配置{0，不填写，>库存}   ok
     */
    @Test(description = "创建裂变活动-分享者优惠券配置{0，不填写，>库存}")
    public void fissionVoucherTitleException7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //添加活动图片
            List<String> picList = new ArrayList<>();
            picList.add(businessUtil.getPicPath());
            //获取优惠券ID
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            //获取优惠券库存
            String surplus = businessUtil.getSurplusInventory(voucherId);
            int surpass = Integer.parseInt(surplus) + 1;
            String[] num = {"", String.valueOf(surpass), "0"};
            for (int i = 0; i < num.length; i++) {
                // 创建被邀请者和分享者的信息字段
                JSONObject invitedVoucher = businessUtil.getInvitedVoucher(voucherId, 1, num[i], 2, "", "", 3);
                JSONObject shareVoucher = businessUtil.getShareVoucher(voucherId, 1, "1", 2, "", "", 3);
                IScene scene = FissionVoucherAddScene.builder()
                        .type(1)
                        .participationLimitType(0)
                        .receiveLimitType(0)
                        .title(pp.fissionVoucherName)
                        .rule(pp.rule)
                        .startDate(businessUtil.getStartDate())
                        .endDate(businessUtil.getEndDate())
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label("RED_PAPER")
                        .picList(picList)
                        .shareNum("3")
                        .shareVoucher(shareVoucher)
                        .invitedVoucher(invitedVoucher)
                        .build();
                String message = visitor.invokeApi(scene, false).getString("message");
                Preconditions.checkArgument(message.equals("卡券数量不能为空") || message.equals("奖励数量不能为空") || message.contains("可用库存不足") || message.equals("奖励数量至少一张"), "分享者优惠券配置异常情况为：" + num[i]);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建裂变活动-分享者优惠券配置{0，不填写，>库存}");
        }
    }

    /**
     * 创建裂变活动-被邀请者优惠券配置{0，不填写，>库存}    ok
     */

    @Test(description = "创建裂变活动-被邀请者优惠券配置{0，不填写，>库存}")
    public void fissionVoucherTitleException8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //添加活动图片
            List<String> picList = new ArrayList<>();
            picList.add(businessUtil.getPicPath());
            //获取优惠券ID
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            //获取优惠券库存
            String surplus = businessUtil.getSurplusInventory(voucherId);
            String[] num = {"", surplus + 1, "0"};
            // 创建被邀请者和分享者的信息字段
            for (int i = 0; i < num.length; i++) {
                JSONObject invitedVoucher = businessUtil.getInvitedVoucher(voucherId, 1, "1", 2, "", "", 3);
                JSONObject shareVoucher = businessUtil.getShareVoucher(voucherId, 1, num[i], 2, "", "", 3);
                IScene scene = FissionVoucherAddScene.builder()
                        .type(1)
                        .participationLimitType(0)
                        .receiveLimitType(0)
                        .title(pp.fissionVoucherName)
                        .rule(pp.rule)
                        .startDate(businessUtil.getStartDate())
                        .endDate(businessUtil.getEndDate())
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label("RED_PAPER")
                        .picList(picList)
                        .shareNum("3")
                        .shareVoucher(shareVoucher)
                        .invitedVoucher(invitedVoucher)
                        .build();
                String message = visitor.invokeApi(scene, false).getString("message");
                Preconditions.checkArgument(message.equals("卡券数量不能为空") || message.equals("奖励数量不能为空") || message.contains("可用库存不足") || message.equals("奖励数量至少一张"), "被邀请者优惠券配置异常情况为：" + num[i]);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建裂变活动-被邀请者优惠券配置{0，不填写，>库存}");
        }
    }

    /**
     * 创建招募活动-校验已作废的优惠券
     */
    @Test
    public void activityInvalidVoucher() {
        try {
            //获取作废的优惠券
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor)
                    .status(VoucherStatusEnum.INVALIDED).buildVoucher().getVoucherId();
            //创建招募活动
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, 1);
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .title("招募活动-部分客户" + (int) (Math.random() * 10000))
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(0)
                    .isNeedApproval(true);
            if (true) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            String message = visitor.invokeApi(scene, false).getString("message");
            System.out.println(message);
            Preconditions.checkArgument(message.contains("可用库存不足") || message.contains("已作废"), "作废优惠券的校验，message返回结果为：" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建招募活动-校验已作废的优惠券");
        }
    }
    /**
     * V3，0创建裂变活动-优惠券领取后使用天数为3651
     */

    @Test(description = "创建裂变活动-优惠券领取后使用天数为3651")
    public void fissionVoucherTitleException9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //添加活动图片
            List<String> picList = new ArrayList<>();
            picList.add(businessUtil.getPicPath());
            //获取优惠券ID
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            //获取优惠券库存
            String surplus = businessUtil.getSurplusInventory(voucherId);
            JSONObject invitedVoucher = businessUtil.getInvitedVoucher(voucherId, 1, "1", 2, "", "", 3);
            JSONObject shareVoucher = businessUtil.getShareVoucher(voucherId, 1, "1", 2, "", "", 3651);
            IScene scene = FissionVoucherAddScene.builder()
                    .type(1)
                    .participationLimitType(0)
                    .receiveLimitType(0)
                    .title(pp.fissionVoucherName)
                    .rule(pp.rule)
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("RED_PAPER")
                    .picList(picList)
                    .shareNum("3")
                    .shareVoucher(shareVoucher)
                    .invitedVoucher(invitedVoucher)
                    .build();
            String message = visitor.invokeApi(scene, false).getString("message");
            Preconditions.checkArgument(message.equals("卡券有效天数范围为[1,3650]"), "优惠券领取后使用天数为3651，创建成功");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建裂变活动-优惠券领取后使用天数为3651");
        }
    }


    /**
     * 2021/3/11
     * -----------------------------------------------创建招募活动异常校验-------------------------------------------
     */

    /**
     *创建招募活动--标题的异常情况{不填写、21位}
     */
    @Test(description = "创建招募活动--标题的异常情况{不填写、21位}")
    public void RecruitActivityTitleException9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            List<Integer> labels = new ArrayList<>();
            labels.add(1000);
            labels.add(1);
            labels.add(100);
            labels.add(2000);
            labels.add(3000);
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherAllowUseInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            String[] title = pp.titleException;
            for (int i = 0; i < title.length; i++) {
                ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                        .type(2)
                        .participationLimitType(0)
                        .title(title[i])
                        .startDate(businessUtil.getStartDate())
                        .endDate(businessUtil.getEndDate())
                        .applyStart(businessUtil.getStartDate())
                        .applyEnd(businessUtil.getEndDate())
                        .isLimitQuota(true)
                        .quota(10)
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label("BARGAIN")
                        .picList(picList)
                        .rule(pp.rule)
                        .registerInformationList(registerInformationList)
                        .successReward(true)
                        .rewardReceiveType(0)
                        .isNeedApproval(true);
                if (true) {
                    builder.rewardVouchers(registerObject)
                            .voucherValid(voucherValid);
                }
                IScene scene = builder.build();
                String message = visitor.invokeApi(scene, false).getString("message");
                Preconditions.checkArgument(message.equals("活动名称长度为[1,20]") || message.equals("活动名称不能为空"), "创建招募活动--标题的异常情况{不填写、21位},标题异常情况创建成功");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建招募活动--标题的异常情况{不填写、21位}");
        }
    }

    /**
     *创建招募活动--活动时间的异常情况{今天之前，一年以后}      ----没有此功能
     */
    @Test(description = "创建招募活动--活动时间的异常情况{今天之前，一年以后}",enabled = false)
    public void RecruitActivityTitleException10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            List<Integer> labels = new ArrayList<>();
            labels.add(1000);
            labels.add(1);
            labels.add(100);
            labels.add(2000);
            labels.add(3000);
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherAllowUseInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            String[] activityTime = {businessUtil.getDateTime(-8),businessUtil.getDateTime(+370)};
            for(int i=0;i<activityTime.length;i++){
                ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                        .type(2)
                        .participationLimitType(0)
                        .title("招募-活动时间异常情况" + (int) (Math.random() * 10000))
                        .startDate(activityTime[i])
                        .endDate(activityTime[i])
                        .applyStart(activityTime[i])
                        .applyEnd(activityTime[i])
                        .isLimitQuota(true)
                        .quota(10)
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label("BARGAIN")
                        .picList(picList)
                        .rule(pp.rule)
                        .registerInformationList(registerInformationList)
                        .successReward(true)
                        .rewardReceiveType(0)
                        .isNeedApproval(true);
                if (true) {
                    builder.rewardVouchers(registerObject)
                            .voucherValid(voucherValid);
                }
                IScene scene = builder.build();
                String message = visitor.invokeApi(scene, false).getString("message");
                Preconditions.checkArgument(message.equals("活动结束日期不早于今日") || message.equals("活动名称不能为空"), "创建招募活动--活动时间的异常情况{今天之前，一年以后},时间异常情况创建成功");

            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建招募活动--活动时间的异常情况{今天之前，一年以后}");
        }
    }

    /**
     *创建招募活动--活动名额的异常情况{"",10000}        ----不能为空，前端做的限制，后端没有限制
     */
    @Test(description = "创建招募活动--活动名额的异常情况{空,10000}")
    public void RecruitActivityTitleException11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            List<Integer> labels = new ArrayList<>();
            labels.add(1000);
            labels.add(1);
            labels.add(100);
            labels.add(2000);
            labels.add(3000);
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherAllowUseInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .title("招募-活动时间异常情况" + (int) (Math.random() * 10000))
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10000)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(0)
                    .isNeedApproval(true);
            if (true) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            String message = visitor.invokeApi(scene, false).getString("message");
            Preconditions.checkArgument(message.equals("限制名额数取值范围为[1,9999]") , "创建招募活动--活动名额的异常情况{空,51},招募人数异常情况创建成功");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建招募活动--活动名额的异常情况{空,51}");
        }
    }

    /**
     *创建招募活动--活动规则的异常情况{"",2001}----此功能与已取消
     */
    @Test(description = "创建招募活动--活动规则的异常情况{空,2001} ",enabled = false)
    public void RecruitActivityTitleException12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Integer> labels = new ArrayList<>();
            labels.add(1000);
            labels.add(1);
            labels.add(100);
            labels.add(2000);
            labels.add(3000);
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            String[] rule = pp.ruleException;
            for (int i = 0; i < rule.length; i++) {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherAllowUseInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
                ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                        .type(2)
                        .participationLimitType(0)
                        .title("招募-活动时间异常情况" + (int) (Math.random() * 10000))
                        .startDate(businessUtil.getStartDate())
                        .endDate(businessUtil.getEndDate())
                        .applyStart(businessUtil.getStartDate())
                        .applyEnd(businessUtil.getEndDate())
                        .isLimitQuota(true)
                        .quota(5)
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label("BARGAIN")
                        .picList(picList)
                        .rule(rule[i])
                        .registerInformationList(registerInformationList)
                        .successReward(true)
                        .rewardReceiveType(0)
                        .isNeedApproval(true);
                if (true) {
                    builder.rewardVouchers(registerObject)
                            .voucherValid(voucherValid);
                }
                IScene scene = builder.build();
                String message = visitor.invokeApi(scene, false).getString("message");
                Preconditions.checkArgument(message.equals("活动规则字数为[1,2000]") , "创建招募活动--活动规则的异常情况{空,2001},活动规则异常创建成功");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建招募活动--活动规则的异常情况{空,2001} ");
        }
    }

    /**
     *创建招募活动--优惠券发行张数的异常情况{"",大于库存，0}
     */
    @Test(description = "创建招募活动--优惠券发行张数的异常情况{null,大于库存，0} ")
    public void RecruitActivityTitleException13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            List<Integer> labels = new ArrayList<>();
            labels.add(1000);
            labels.add(1);
            labels.add(100);
            labels.add(2000);
            labels.add(3000);
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            String surplus = businessUtil.getSurplusInventory(voucherId);
            String[] num = {"0",surplus + 1,""};
            // 创建被邀请者和分享者的信息字段
            for (int i = 0; i < num.length; i++) {
                JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
                //报名成功奖励
                JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, num[i]);
                ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                        .type(2)
                        .participationLimitType(0)
                        .title("招募-活动时间异常情况" + (int) (Math.random() * 10000))
                        .startDate(businessUtil.getStartDate())
                        .endDate(businessUtil.getEndDate())
                        .applyStart(businessUtil.getStartDate())
                        .applyEnd(businessUtil.getEndDate())
                        .isLimitQuota(true)
                        .quota(5)
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label("BARGAIN")
                        .picList(picList)
                        .rule(pp.rule)
                        .registerInformationList(registerInformationList)
                        .successReward(true)
                        .rewardReceiveType(0)
                        .isNeedApproval(true);
                if (true) {
                    builder.rewardVouchers(registerObject)
                            .voucherValid(voucherValid);
                }
                IScene scene = builder.build();
                String message = visitor.invokeApi(scene, false).getString("message");
                Preconditions.checkArgument(message.contains("可用库存不足") || message.equals("奖励数量不能为空") || message.equals("奖励数量至少一张"), "被邀请者优惠券配置异常情况为：" + num[i]);
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建招募活动--优惠券发行张数的异常情况{null,大于库存，0} ");
        }
    }

    /**
     *创建招募活动--优惠券有效期为3651天
     */
    @Test(description = "创建招募活动--优惠券有效期为3651天 ")
    public void RecruitActivityTitleException14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            List<Integer> labels = new ArrayList<>();
            labels.add(1000);
            labels.add(1);
            labels.add(100);
            labels.add(2000);
            labels.add(3000);
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 3651);
            //创建招募活动-共有的--基础信息
            String surplus = businessUtil.getSurplusInventory(voucherId);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1,businessUtil.getVoucherAllowUseInventory(voucherId));
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .title("招募-活动时间异常情况" + (int) (Math.random() * 10000))
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(5)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(0)
                    .isNeedApproval(true);
            if (true) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            String message = visitor.invokeApi(scene, false).getString("message");
            Preconditions.checkArgument(message.equals("卡券有效天数范围为[1,3650]"), "优惠券有效期为3651天，创建成功" );

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建招募活动--优惠券有效期为3651天");
        }
    }




    /**
     * 创建招募活动-校验已售罄的优惠券  --------------------优惠券校验-------------------------------
     */
    @Test
    public void activitySellOutVoucher() {
        try {
            //获取已售罄的优惠券
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor)
                    .status(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
            //创建招募活动
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, 1);
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .title("招募活动-部分客户" + (int) (Math.random() * 10000))
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(0)
                    .isNeedApproval(true);
            if (true) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            String message = visitor.invokeApi(scene, false).getString("message");
            System.out.println(message);
            Preconditions.checkArgument(message.contains("可用库存不足") || message.contains("已售罄"), "作废优惠券的校验，message返回结果为：" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建招募活动-校验已售罄的优惠券");
        }
    }

    /**
     * 创建招募活动-校验暂停发放的优惠券
     */
    @Test
    public void activityStopVoucher() {
        try {
            //获取暂停发放的优惠券
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor)
                    .status(VoucherStatusEnum.STOP).buildVoucher().getVoucherId();
            //创建招募活动
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, 1);
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .title("招募活动-部分客户" + (int) (Math.random() * 10000))
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(0)
                    .isNeedApproval(true);
            if (true) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            String message = visitor.invokeApi(scene, false).getString("message");
            System.out.println(message);
            Preconditions.checkArgument(message.contains("可用库存不足") || message.contains("暂停发放"), "作废优惠券的校验，message返回结果为：" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建招募活动-校验暂停发放的优惠券");
        }
    }


    /**
     * 创建裂变活动-参与客户限制为【部分】，部分中的标签选择全部的
     */
    @Test(enabled = false)
    public void activityChooseLabelsSome() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            List<Integer> labels = new ArrayList<>();
            labels.add(1000);
            labels.add(1);
            labels.add(100);
            labels.add(2000);
            labels.add(3000);
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            List<String> picList = new ArrayList<>();
            picList.add(supporterUtil.getPicPath());
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher = businessUtil.getInvitedVoucher(voucherId, 1, String.valueOf(businessUtil.getVoucherAllowUseInventory(voucherId)), 2, "", "", 3);
            JSONObject shareVoucher = businessUtil.getShareVoucher(voucherId, 1, String.valueOf(businessUtil.getVoucherAllowUseInventory(voucherId)), 2, "", "", 3);
            IScene scene = FissionVoucherAddScene.builder()
                    .type(1)
                    .participationLimitType(1)
                    .chooseLabels(labels)
                    .receiveLimitType(0)
                    .receiveLimitTimes("1")
                    .title("裂变活动-部分客户" + (int) (Math.random() * 10000))
                    .rule(pp.rule)
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("RED_PAPER")
                    .picList(picList)
                    .shareNum("3")
                    .shareVoucher(shareVoucher)
                    .invitedVoucher(invitedVoucher)
                    .build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            Preconditions.checkArgument(activityId > 0, "创建裂变活动-参与客户限制为【部分】，部分中的标签选择全部的活动ID为：" + activityId);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建裂变活动-参与客户限制为【部分】，部分中的标签选择全部的");
        }
    }

    /**
     * 创建招募活动-参与客户限制为【部分】，部分中的标签选择全部的
     */
    @Test(enabled = false)
    public void activityChooseLabels() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            List<Integer> labels = new ArrayList<>();
            labels.add(1000);
            labels.add(1);
            labels.add(100);
            labels.add(2000);
            labels.add(3000);
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            isShow.add(true);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            isRequired.add(true);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherAllowUseInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .title("招募活动-部分客户" + (int) (Math.random() * 10000))
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(0)
                    .isNeedApproval(true);
            if (true) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            Preconditions.checkArgument(activityId > 0, "创建招募活动，报名信息全为非必填项活动-参与客户限制为【部分】，部分中的标签选择全部的活动ID为：" + activityId);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建招募活动，报名信息全为非必填项活动-参与客户限制为【部分】，部分中的标签选择全部的");
        }
    }

    /**
     * 创建裂变活动-客户标签
     */
    @Test(enabled = false)
    public void activityLabels() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            List<Integer> chooseLabels = new ArrayList<>();
            chooseLabels.add(1000);
            chooseLabels.add(1);
            chooseLabels.add(100);
            chooseLabels.add(2000);
            chooseLabels.add(3000);
            String[][] label = {{"PREFERENTIAL", "优惠"}, {"BARGAIN", "特价"}, {"WELFARE", "福利"}, {"RED_PAPER", "红包"}, {"GIFT", "礼品"}, {"SELL_WELL", "热销"}, {"RECOMMEND", "推荐"}};
            for (int i = 0; i < label.length; i++) {
                System.err.println(label.length + "-------" + 1);
                SupporterUtil supporterUtil = new SupporterUtil(visitor);
                PublicParameter pp = new PublicParameter();
                List<String> picList = new ArrayList<>();
                picList.add(supporterUtil.getPicPath());
                // 创建被邀请者和分享者的信息字段
                JSONObject invitedVoucher = businessUtil.getInvitedVoucher(voucherId, 1, String.valueOf(businessUtil.getVoucherAllowUseInventory(voucherId)), 2, "", "", 3);
                JSONObject shareVoucher = businessUtil.getShareVoucher(voucherId, 1, String.valueOf(businessUtil.getVoucherAllowUseInventory(voucherId)), 2, "", "", 3);
                IScene scene = FissionVoucherAddScene.builder()
                        .type(1)
                        .participationLimitType(1)
                        .chooseLabels(chooseLabels)
                        .receiveLimitType(0)
                        .title("裂变活动标签为-" + label[i][1] + "-" + (int) (Math.random() * 10000))
                        .rule(pp.rule)
                        .startDate(businessUtil.getStartDate())
                        .endDate(businessUtil.getEndDate())
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label(label[i][0])
                        .picList(picList)
                        .shareNum("3")
                        .shareVoucher(shareVoucher)
                        .invitedVoucher(invitedVoucher)
                        .build();
                Long activityId = visitor.invokeApi(scene).getLong("id");
                //审批通过招募活动
                businessUtil.getApprovalPassed(activityId);
                Preconditions.checkArgument(activityId > 0, "创建裂变活动-参与客户限制为【部分】，部分中的标签选择全部的活动ID为：" + activityId);
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建裂变活动-参与客户限制为【部分】，部分中的标签选择全部的");
        }
    }

    /**
     * 招募活动，报名信息全为非必填项
     */
    @Test(enabled = false)
    public void activityRegistrationIssue() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            List<Integer> labels = new ArrayList<>();
            labels.add(1000);
            labels.add(1);
            labels.add(100);
            labels.add(2000);
            labels.add(3000);
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherAllowUseInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .title("招募-报名信息非必填" + (int) (Math.random() * 10000))
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(0)
                    .isNeedApproval(true);
            if (true) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            //小程序报名活动(报名信息不填写)
            businessUtil.activityRegisterApplet(activityId,"","",1,"","","","");

            Preconditions.checkArgument(activityId > 0, "招募活动，报名信息全为非必填项");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动，报名信息全为非必填项");
        }
    }

    /**
     * 招募活动，报名信息为空
     */
    @Test(enabled = false)
    public void activityRegistrationNUll() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            List<Integer> labels = new ArrayList<>();
            labels.add(1000);
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);

            JSONArray registerInformationList = this.businessUtil.getRegisterInformationNullList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherAllowUseInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .title("招募-报名信息为空" + (int) (Math.random() * 10000))
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(0)
                    .isNeedApproval(true);
            if (true) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            //小程序报名
            businessUtil.activityRegisterApplet(activityId);

            Preconditions.checkArgument(activityId > 0, "招募活动，报名信息为空");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动，报名信息为空");
        }
    }

    /**
     *-----------------------------------小程序活动相关代码校验------------------------------------
     */


    /**
     * 招募活动，报名成功自动发放，小程序中小喇叭中优惠券的状态为已领取
     */
    @Test(enabled = true)
    public void activityVoucherStatus1() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            List<Integer> labels = new ArrayList<>();
            labels.add(1000);
            labels.add(1);
            labels.add(100);
            labels.add(2000);
            labels.add(3000);
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherAllowUseInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .title("招募-报名信息非必填" + (int) (Math.random() * 10000))
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(0)
                    .isNeedApproval(true);
            if (true) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            //小程序报名活动(报名信息不填写)
            businessUtil.activityRegisterApplet(activityId,"","",1,"","","","");
            jc.pcLogin(pp.phone,pp.password);
            //获取报名管理中的信息
            IScene scene3= ManageRegisterPageScene.builder().page(1).size(10).activityId(activityId).build();
            Long registerId=visitor.invokeApi(scene3).getJSONArray("list").getJSONObject(0).getLong("id");
            System.out.println("---------registerId:"+registerId);
            //报名审批通过
            businessUtil.getRegisterApprovalPassed(activityId,registerId);
//            //获取卡券码
            List<VoucherSendRecord> vList = supporterUtil.getVoucherSendRecordList(voucherId);
            String voucherCode = vList.get(0).getVoucherCode();
            //登录小程序
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            //查看小程序中此活动对应的小喇叭中的卡券的状态
            String isReceived=businessUtil.articleVoucher(activityId);
            System.err.println("----------------"+isReceived);
//            //查询是否获得此卡券(通过卡券码查询，看看能否有此卡券的返回值)
            AppletVoucherInfo voucher = supporterUtil.getAppletVoucherInfo(voucherCode);
            String code=voucher.getVoucherCode();
            System.out.println(code+"---------------voucher:---"+voucher);
            Preconditions.checkArgument(isReceived.equals("true")&&code.equals(voucherCode),"小程序活动中小喇叭的状态："+isReceived+"    在小程序中查到的卡券码为："+code);

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("招募活动，报名成功自动发放，小程序中小喇叭中优惠券的状态为已领取");
        }
    }



    /**
     * 小程序报名审批并通过，卡券为手动领取，卡券状态为未领取，再次领取卡券，卡券的状态为，已领取
     */
    @Test(enabled = true)
    public void activityVoucherStatus2() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            List<Integer> labels = new ArrayList<>();
            labels.add(1000);
            labels.add(1);
            labels.add(100);
            labels.add(2000);
            labels.add(3000);
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherAllowUseInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .title("招募-报名信息非必填" + (int) (Math.random() * 10000))
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(1)
                    .isNeedApproval(true);
            if (true) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            //小程序报名活动(报名信息不填写)
            businessUtil.activityRegisterApplet(activityId,"","",1,"","","","");
            jc.pcLogin(pp.phone,pp.password);
            //获取报名管理中的信息
            IScene scene3=ManageRegisterPageScene.builder().page(1).size(10).activityId(activityId).build();
            Long registerId=visitor.invokeApi(scene3).getJSONArray("list").getJSONObject(0).getLong("id");
            //报名审批通过
            businessUtil.getRegisterApprovalPassed(activityId,registerId);
            //登录小程序
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            //查看小程序中此活动对应的小喇叭中的卡券的状态
            String isReceivedBefore=businessUtil.articleVoucher(activityId);
            //小程序中手动领取优惠券
            Long id=businessUtil.appointmentActivityId(activityId);
            //获取小程序中的卡券ID
            long vId=businessUtil.articleVoucherData(activityId).getJSONArray("list").getJSONObject(0).getLong("id");
            IScene scene4= ArticleVoucherReceiveScene.builder().articleId(id).voucherId(vId).build();
            String message=visitor.invokeApi(scene4,false).getString("message");
            String isReceivedAfter=businessUtil.articleVoucher(activityId);
            //登录PC
            jc.pcLogin(pp.phone,pp.password);
            //获取卡券码
            List<VoucherSendRecord> vList = supporterUtil.getVoucherSendRecordList(voucherId);
            String voucherCode = vList.get(0).getVoucherCode();
            //登录小程序
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            //查询是否获得此卡券(通过卡券码查询，看看能否有此卡券的返回值)
            AppletVoucherInfo voucher = supporterUtil.getAppletVoucherInfo(voucherCode);
            String code=voucher.getVoucherCode();
            System.out.println(code+"---------------voucher:---"+voucher);

            Preconditions.checkArgument(isReceivedBefore.equals("false")&&isReceivedAfter.equals("true")&&code.equals(voucherCode),"招募活动中卡券领取失败||小程序没有到账");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("小程序报名审批并通过，卡券为手动领取，卡券状态为未领取，再次领取卡券，卡券的状态为，已领取");
        }
    }

    /**
     * 小程序报名审批并通过，卡券为手动领取，此时作废卡券，卡券领取提示
     */
    @Test(enabled = true)
    public void activityVoucherStatus3() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            List<Integer> labels = new ArrayList<>();
            labels.add(1000);
            labels.add(1);
            labels.add(100);
            labels.add(2000);
            labels.add(3000);
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherAllowUseInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .title("招募-报名信息非必填" + (int) (Math.random() * 10000))
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(1)
                    .isNeedApproval(true);
            if (true) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            //小程序报名活动(报名信息不填写)
            businessUtil.activityRegisterApplet(activityId,"","",1,"","","","");
            jc.pcLogin(pp.phone,pp.password);
            //获取报名管理中的信息
            IScene scene3=ManageRegisterPageScene.builder().page(1).size(10).activityId(activityId).build();
            Long registerId=visitor.invokeApi(scene3).getJSONArray("list").getJSONObject(0).getLong("id");
            //报名审批通过
            businessUtil.getRegisterApprovalPassed(activityId,registerId);
            //作废卡券
            visitor.invokeApi(InvalidVoucherScene.builder().id(voucherId).build());
            //登录小程序
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            //查看小程序中此活动对应的小喇叭中的卡券的状态
            String isReceivedBefore=businessUtil.articleVoucher(activityId);
            //小程序中手动领取优惠券
            Long id=businessUtil.appointmentActivityId(activityId);
            //获取小程序中的卡券ID
            long vId=businessUtil.articleVoucherData(activityId).getJSONArray("list").getJSONObject(0).getLong("id");
            IScene scene4= ArticleVoucherReceiveScene.builder().articleId(id).voucherId(vId).build();
            String message=visitor.invokeApi(scene4,false).getString("message");
            String isReceivedAfter=businessUtil.articleVoucher(activityId);
            System.out.println(isReceivedAfter+"--------"+message);

            Preconditions.checkArgument(isReceivedBefore.equals("false")&&isReceivedAfter.equals("false")&&message.equals("很遗憾，优惠券已经被抢光了～更多活动敬请期待"),"招募活动中卡券领取失败||小程序没有到账");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("小程序报名审批并通过，卡券为手动领取，此时作废卡券，卡券领取提示");
        }
    }


    /**
     * 小程序报名审批并通过，卡券为手动领取，此时暂停发放卡券，卡券领取提示
     */
    @Test(enabled = true)
    public void activityVoucherStatus4(){
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            List<Integer> labels = new ArrayList<>();
            labels.add(1000);
            labels.add(1);
            labels.add(100);
            labels.add(2000);
            labels.add(3000);
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1,Math.min(businessUtil.getVoucherAllowUseInventory(voucherId),3));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                    .type(2)
                    .participationLimitType(0)
                    .title("招募-报名信息非必填" + (int) (Math.random() * 10000))
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .applyStart(businessUtil.getStartDate())
                    .applyEnd(businessUtil.getEndDate())
                    .isLimitQuota(true)
                    .quota(10)
                    .subjectType(supporterUtil.getSubjectType())
                    .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                    .label("BARGAIN")
                    .picList(picList)
                    .rule(pp.rule)
                    .registerInformationList(registerInformationList)
                    .successReward(true)
                    .rewardReceiveType(1)
                    .isNeedApproval(true);
            if (true) {
                builder.rewardVouchers(registerObject)
                        .voucherValid(voucherValid);
            }
            IScene scene = builder.build();
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            //小程序报名活动(报名信息不填写)
            businessUtil.activityRegisterApplet(activityId,"","",1,"","","","");
            jc.pcLogin(pp.phone,pp.password);
            //获取报名管理中的信息
            IScene scene3=ManageRegisterPageScene.builder().page(1).size(10).activityId(activityId).build();
            Long registerId=visitor.invokeApi(scene3).getJSONArray("list").getJSONObject(0).getLong("id");
            //报名审批通过
            businessUtil.getRegisterApprovalPassed(activityId,registerId);
            //暂停发放
            IScene changeProvideStatusScene = ChangeProvideStatusScene.builder().id(voucherId).isStart(false).build();
            visitor.invokeApi(changeProvideStatusScene);
            //登录小程序
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            //查看小程序中此活动对应的小喇叭中的卡券的状态
            String isReceivedBefore=businessUtil.articleVoucher(activityId);
            //小程序中手动领取优惠券
            Long id=businessUtil.appointmentActivityId(activityId);
            //获取小程序中的卡券ID
            long vId=businessUtil.articleVoucherData(activityId).getJSONArray("list").getJSONObject(0).getLong("id");
            IScene scene4= ArticleVoucherReceiveScene.builder().articleId(id).voucherId(vId).build();
            String message=visitor.invokeApi(scene4,false).getString("message");
            String isReceivedAfter=businessUtil.articleVoucher(activityId);
            System.out.println(isReceivedAfter+"--------"+message);
            Preconditions.checkArgument(isReceivedBefore.equals("false")&&isReceivedAfter.equals("false")&&message.equals("很遗憾，优惠券已经被抢光了～更多活动敬请期待"),"招募活动中卡券领取失败，message提示为："+message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("小程序报名审批并通过，卡券为手动领取，此时暂停卡券，卡券领取提示");
        }
    }


    /**
     * ----------------------------------内容营销活动的各个状态+矫3.1新增case---------------------------------
     */

    /**
     * 内容营销-待审核的活动-查看
     */
    @Test()
    public void ContentMarketingPendingCheck(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待审核活动的ID
            List<Long> ids = businessUtil.getContentMarketingWaitingApproval();
            //获取待审核的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-内容营销-【待审核】的活动-查看");
        }
    }

    /**
     * 活动管理-内容营销-【待审批】的活动撤回   ok
     */
    @Test(description = "活动管理-内容营销-【待审批】的活动撤回")
    public void ContentMarketingPendingRecoke() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待审批活动的ID
            List<Long> ids = businessUtil.getContentMarketingWaitingApproval();
            //撤回待审批的活动
            String message = businessUtil.getRevokeActivity(ids.get(0));
            //获取刚才通过的活动的状态
            int status = businessUtil.getActivityStatus(ids.get(0));
            Preconditions.checkArgument(status == ActivityStatusEnum.REVOKE.getId()&&message.equals("success"), "撤回的待审批的活动,现活动的状态为：" + status);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-内容营销-【待审批】的活动撤回");
        }
    }

    /**
     *活动管理-内容营销-【待审核】的活动-置顶
     * 2021-3-17
     */
    @Test(description = "活动管理-内容营销-【待审核】的活动-置顶")
    public void ContentMarketingPendingTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待审核活动的ID
            List<Long> ids = businessUtil.getContentMarketingWaitingApproval();
            //获取待审核的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //置顶【待审批的活动】
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            Preconditions.checkArgument(message.equals("活动未审核通过！暂不能置顶"), "现在活动的名称为：" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-内容营销-【待审核】的活动-置顶");
        }
    }

    /**
     * 活动管理-内容营销-内容营销-【已撤销】的活动-查看
     * 2021-3-17
     */
    @Test(description = "活动管理-内容营销-内容营销-【已撤销】的活动-查看")
    public void revokeContentMarketingCheck() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已撤销活动的ID
            List<Long> ids = businessUtil.getContentMarketingRevoke();
            //获取已撤销的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            System.out.println(title+"-------"+title1);
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-内容营销-内容营销-【已撤销】的活动-查看");
        }
    }

    /**
     * 活动管理-内容营销-【已撤销】的活动-置顶
     * 2021-3-17
     */
    @Test(description = "活动管理-内容营销-【已撤销】的活动-置顶")
    public void revokeContentMarketingTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已撤销活动的ID
            List<Long> ids = businessUtil.getContentMarketingRevoke();
            //获取已撤销的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //置顶【已撤销的活动】
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            System.out.println(title+"-------"+message);
            Preconditions.checkArgument(message.equals("活动未审核通过！暂不能置顶"), "置顶已撤销的活动的相关提示:" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-内容营销-【已撤销】的活动-置顶");
        }
    }

    /**
     * 活动管理-内容营销-【已撤销】的活动-删除
     * 2021-3-17
     */
    @Test(description = "活动管理-内容营销-【已撤销】的活动-删除")
    public void revokeContentMarketingDel() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已撤销活动的ID
            List<Long> ids = businessUtil.getContentMarketingRevoke();
            //删除已撤销的活动
            String message = businessUtil.getDelActivity(ids.get(0));
            Preconditions.checkArgument(message.equals("success"), "已撤销的活动删除失败");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-内容营销-【已撤销】的活动-删除");
        }
    }

    /**
     * 活动管理-内容营销-【已撤销】的活动-编辑：名称，活动规则----招募活动
     * 2021-3-17
     */
    @Test(description = "活动管理-内容营销-【已撤销】的活动-编辑")
    public void revokeContentMarketingEdit(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已撤销活动的ID
            List<Long> ids =  businessUtil.getContentMarketingRevoke();
            System.err.println("----ids:"+ids.get(0));
            //编辑已撤销的活动
            List<String> picList = new ArrayList<>();
            picList.add(0, businessUtil.getPicPath());
            String[][] label = {{"PREFERENTIAL", "优惠"}, {"BARGAIN", "特价"}, {"WELFARE", "福利"}, {"RED_PAPER", "红包"}, {"GIFT", "礼品"}, {"SELL_WELL", "热销"}, {"RECOMMEND", "推荐"}};
            IScene scene =ManageContentMarketingEditScene.builder()
                    .id(ids.get(0))
                    .type(3)
                    .participationLimitType(0)
                    .title(pp.contentMarketingNameEdit)
                    .rule(pp.rule)
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .subjectType(supporterUtil.getSubjectType())
                    .label(label[1][0])
                    .picList(picList)
                    .actionPoint(2)
                    .build();
            String message = visitor.invokeApi(scene,false).getString("message");
            Preconditions.checkArgument(message.equals("success"),"编辑活动失败");

            //获取活动详情
            IScene scene1=ManageDetailScene.builder().id(ids.get(0)).build();
            JSONObject object=visitor.invokeApi(scene1);
            String title=object.getString("title");
            String participationLimitType=object.getString("participation_limit_type");
            String startDate=object.getString("start_date");
            String endDate=object.getString("end_date");
            String subjectType=object.getString("subject_type");
            String label1=object.getString("label");
            String actionPoint=object.getJSONObject("content_marketing_info").getString("action_point");
//            String picPath=object.getJSONArray("pic_list").getJSONObject(0).getString("pic_path");
            Preconditions.checkArgument(title.equals(pp.contentMarketingNameEdit),"活动详情中的活动为："+title);
            Preconditions.checkArgument(participationLimitType.equals("0"),"活动详情中的活动目标为："+participationLimitType);
            Preconditions.checkArgument(startDate.equals(businessUtil.getStartDate()),"活动详情中的开始时间为："+startDate);
            Preconditions.checkArgument(endDate.equals(businessUtil.getEndDate()),"活动详情中的结束时间为："+endDate);
            Preconditions.checkArgument(subjectType.equals(supporterUtil.getSubjectType()),"活动详情中的活动类型为："+subjectType);
            Preconditions.checkArgument(label1.equals(label[1][0]),"活动详情中的活动标签为："+label1);
            Preconditions.checkArgument(actionPoint.equals("2"),"活动详情中的客户行动点为："+actionPoint);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-内容营销-【已撤销】的活动-编辑");
        }
    }

    /**
     * 活动管理-内容营销-【审核未通过】的活动-查看
     * 2021-3-17
     */
    @Test(description = "活动管理-内容营销-【审核未通过】的活动-查看")
    public void rejectContentMarketingCheck() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取审核未通过活动的ID
            List<Long> ids = businessUtil.getContentMarketingReject();
            //获取审核未通过的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            System.out.println(title+"-------"+title1);
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-内容营销-【审核未通过】的活动-查看");
        }
    }
    /**
     * 内容营销-【审核未通过】的活动-置顶
     * 2021-3-17
     */
    @Test(description = "内容营销-【审核未通过】的活动-置顶",enabled = true)
    public void rejectContentMarketingTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取审核未通过活动的ID
            List<Long> ids = businessUtil.getContentMarketingReject();
            //获取审核未通过的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //置顶【审核未通过的活动】
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            System.out.println(title+"-------"+message);
            Preconditions.checkArgument(message.equals("活动未审核通过！暂不能置顶"), "置顶审核不通过的活动的相关提示:" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容营销【审核未通过】的活动-置顶");
        }
    }
    /**
     * 内容营销-删除【审核未通过】的活动       ok
     */
    @Test(description = "内容营销-删除【审核未通过】的活动")
    public void rejectContentMarketingDel() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取审核未通过活动的ID
            List<Long> ids = businessUtil.getContentMarketingReject();
            //删除审核未通过的活动
            String message = businessUtil.getDelActivity(ids.get(0));
            Preconditions.checkArgument(message.equals("success"), "删除【审核未通过】的活动的message为：" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-删除【审核未通过】的活动");
        }
    }

    /**
     * 内容营销-【审核未通过】的活动-编辑：名称，活动规则
     * 2021-3-17
     */
    @Test(description = "活动管理-【审核未通过】的活动-编辑")
    public void rejectContentMarketingEdit(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取审核未通过活动的ID
            List<Long> ids =  businessUtil.getContentMarketingReject();
            System.err.println("----ids:"+ids.get(0));
            //编辑审核未通过的活动
            List<String> picList = new ArrayList<>();
            picList.add(0, businessUtil.getPicPath());
            String[][] label = {{"PREFERENTIAL", "优惠"}, {"BARGAIN", "特价"}, {"WELFARE", "福利"}, {"RED_PAPER", "红包"}, {"GIFT", "礼品"}, {"SELL_WELL", "热销"}, {"RECOMMEND", "推荐"}};
            IScene scene =ManageContentMarketingEditScene.builder()
                    .id(ids.get(0))
                    .type(3)
                    .participationLimitType(0)
                    .title(pp.contentMarketingNameEdit)
                    .rule(pp.rule)
                    .startDate(businessUtil.getStartDate())
                    .endDate(businessUtil.getEndDate())
                    .subjectType(supporterUtil.getSubjectType())
                    .label(label[1][0])
                    .picList(picList)
                    .actionPoint(2)
                    .build();
            String message = visitor.invokeApi(scene,false).getString("message");
            Preconditions.checkArgument(message.equals("success"),"编辑活动失败");

            //获取活动详情
            IScene scene1=ManageDetailScene.builder().id(ids.get(0)).build();
            JSONObject object=visitor.invokeApi(scene1);
            String title=object.getString("title");
            String participationLimitType=object.getString("participation_limit_type");
            String startDate=object.getString("start_date");
            String endDate=object.getString("end_date");
            String subjectType=object.getString("subject_type");
            String label1=object.getString("label");
            String actionPoint=object.getJSONObject("content_marketing_info").getString("action_point");
//            String picPath=object.getJSONArray("pic_list").getJSONObject(0).getString("pic_path");
            Preconditions.checkArgument(title.equals(pp.contentMarketingNameEdit),"活动详情中的活动为："+title);
            Preconditions.checkArgument(participationLimitType.equals("0"),"活动详情中的活动目标为："+participationLimitType);
            Preconditions.checkArgument(startDate.equals(businessUtil.getStartDate()),"活动详情中的开始时间为："+startDate);
            Preconditions.checkArgument(endDate.equals(businessUtil.getEndDate()),"活动详情中的结束时间为："+endDate);
            Preconditions.checkArgument(subjectType.equals(supporterUtil.getSubjectType()),"活动详情中的活动类型为："+subjectType);
            Preconditions.checkArgument(label1.equals(label[1][0]),"活动详情中的活动标签为："+label1);
            Preconditions.checkArgument(actionPoint.equals("2"),"活动详情中的客户行动点为："+actionPoint);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容营销-【审核未通过】的活动-编辑");
        }
    }

    /**
     * 内容营销-【已取消】的活动-查看
     * 2021-3-17
     */
    @Test(description = "内容营销-【已取消】的活动-查看")
    public void cancelContentMarketingCheck() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已取消活动的ID
            List<Long> ids = businessUtil.getContentMarketingCancel();
            //获取已取消的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            System.out.println(title+"-------"+title1);
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容营销-【已取消】的活动-查看");
        }
    }

    /**
     * 内容营销-【已取消】的活动-恢复
     * 2021-3-17
     */
    @Test(description = "内容营销-【已取消】的活动-恢复")
    public void cancelContentMarketingRecover() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已取消活动的ID
            List<Long> ids = businessUtil.getContentMarketingCancel();
            //获取活动的状态
            int statusCancel=businessUtil.getActivityStatus(ids.get(0));
            //恢复活动并获取状态
            String message=businessUtil.getContentMarketingRecover(ids.get(0));
            int statusRecover=businessUtil.getActivityStatus(ids.get(0));
            Preconditions.checkArgument(message.equals("success"), "活动恢复失败" );
            Preconditions.checkArgument(statusCancel==ActivityStatusEnum.CANCELED.getId()&&statusRecover==ActivityStatusEnum.PENDING.getId(), "恢复以后的状态为：：" + statusRecover);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容营销-【已取消】的活动-恢复");
        }
    }

    /**
     * 内容营销-【进行中】的活动-查看
     * 2021-3-17
     */
    @Test(description = "内容营销-【进行中】的活动-查看")
    public void workingContentMarketingCheck(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getContentMarketingWorking();
            //获取进行中的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            System.out.println(title+"-------"+title1);
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容营销-【进行中】的活动-查看");
        }
    }

    /**
     * 内容营销-【进行中】的活动-置顶
     * 2021-3-17
     */
    @Test(description = "内容营销-【进行中】的活动-置顶")
    public void workingContentMarketingTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getContentMarketingWorking();
            //获取进行中的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //置顶【进行中的活动】
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            System.out.println(title+"-------"+message);
            //小程序中第一个为此活动
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            JSONObject response=businessUtil.appointmentActivityTitleNew();
            String title1=response.getJSONArray("list").getJSONObject(0).getString("title");
            Preconditions.checkArgument(title1.equals(title), "PC进行中活动的名称为：" + title+"小程序中的更多中的活动名称为："+title1);
            Preconditions.checkArgument(message.equals("success"), "置顶进行中的活动的相关提示:" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容营销-【进行中】的活动-置顶");
        }
    }

    /**
     * 内容营销-推广【进行中】的活动
     */
    @Test(description = "内容营销-推广【进行中】的活动")
    public void promotionContentMarketing() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getContentMarketingWorking();
            //推广进行中的活动
            String appletCodeUrl = businessUtil.getPromotionActivity(ids.get(0));
            Preconditions.checkArgument(!appletCodeUrl.equals(""), "推广【进行中】的活动的小程序二维码的返回值为空");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容营销-推广【进行中】的活动");
        }
    }

    /**
     * 内容营销-取消【进行中】的活动     ok
     */
    @Test(description = "内容营销-推广【进行中】的活动")
    public void cancelWorkingContentMarketing() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getContentMarketingWorking();
            //取消进行中的活动
            businessUtil.getCancelActivity(ids.get(0));
            //获取活动的状态
            int status = businessUtil.getActivityStatus(ids.get(0));
            Preconditions.checkArgument(status == ActivityStatusEnum.CANCELED.getId(), "现在活动的状态为：" + status);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容营销-取消【进行中】的活动");
        }
    }

    /**
     * 内容营销-【进行中】的活动-编辑：名称，活动规则----内容营销
     * 2021-3-17
     */
    @Test(enabled = true,description = "内容营销-【进行中】的活动-编辑")
    public void workingContentMarketingEdit(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getContentMarketingWorking();
            System.err.println("----ids:"+ids.get(0));
            //编辑进行中的活动
            String message=businessUtil.getContentMarketingEdit(ids.get(0),pp.contentMarketingNameEdit,pp.EditRule);
            System.out.println("---------"+message);
            //获取活动详情中编辑后的标题和活动规则
            String title = businessUtil.getFissionActivityDetailDate1(ids.get(0)).getString("title");
            String rule = businessUtil.getFissionActivityDetailDate1(ids.get(0)).getString("rule");
            System.out.println(title+"----------"+rule);
            System.out.println(rule);
            //变更记录
            Preconditions.checkArgument(message.equals("success")&&title.equals(pp.contentMarketingNameEdit)&&rule.equals(pp.EditRule), "进行中的活动编辑失败");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容营销-【进行中】的活动-编辑");
        }
    }

    /**
     * 内容营销-【进行中】的活动-下架,再上架
     * 2021-3-17
     */
    @Test(enabled = true,description = "内容营销-【进行中】的活动-下架")
    public void workingContentMarketingOffLine(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Boolean flag=false;
            JSONObject lastValue=null;
            JSONArray list=null;
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getContentMarketingWorking();
            System.err.println("----ids:"+ids.get(0));
            //获取进行中的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //进行中的活动下架
            String message=businessUtil.getContentMarketingOffLine(ids.get(0));
            //获取活动的状态
            int statusOffLine=businessUtil.getActivityStatus(ids.get(0));
            //登录小程序
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            //获取小程序推荐列表
            do {
                IScene scene = AppletArticleListScene.builder().lastValue(lastValue).size(10).build();
                JSONObject response = visitor.invokeApi(scene);
                lastValue = response.getJSONObject("last_value");
                list = response.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String title1 = list.getJSONObject(i).getString("title");
                    if (title.equals(title1)) {
                        flag=true;
                        System.err.println("----------title1:"+title1);
                    }
                }
            } while (list.size() == 10);
            System.err.println(flag);
            System.out.println("----message:-------"+message+"---statusOffLine----"+statusOffLine);
            Preconditions.checkArgument(message.equals("success")&&statusOffLine==ActivityStatusEnum.OFFLINE.getId(), "下架失败");
            Preconditions.checkArgument(!flag,"小程序中还是能够查看到此活动");
            jc.pcLogin(pp.phone,pp.password);
            //活动上架
            String message1=businessUtil.getContentMarketingOnline(ids.get(0));
            //获取活动的状态
            int statusOnLine=businessUtil.getActivityStatus(ids.get(0));
            //置顶此活动
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            visitor.invokeApi(scene,false).getString("message");
            //小程序中第一个为此活动
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            JSONObject response=businessUtil.appointmentActivityTitleNew();
            String title2=response.getJSONArray("list").getJSONObject(0).getString("title");
            Preconditions.checkArgument(statusOnLine==ActivityStatusEnum.PASSED.getId()&&message1.equals("success"),"上架以后活动的状态为："+statusOnLine);
            Preconditions.checkArgument(title2.equals(title), "PC进行中活动的名称为：" + title+"小程序中的更多中的活动名称为："+title2);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容营销-【进行中】的活动-下架");
        }
    }

    /**
     * 内容营销-【未开始】的活动-查看
     * 2021-3-17
     */
    @Test(description = "内容营销-【未开始】的活动-查看")
    public void waitingContentMarketingCheck(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取未开始活动的ID
            List<Long> ids = businessUtil.getContentMarketingWaitingStar();
            //获取未开始的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            System.out.println(title+"-------"+title1);
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容营销-【未开始】的活动-查看");
        }
    }

    /**
     * 内容营销-【未开始】的活动-置顶
     * 2021-3-17
     */
    @Test(description = "内容营销-【未开始】的活动-置顶")
    public void waitingContentMarketingTop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取未开始活动的ID
            List<Long> ids = businessUtil.getContentMarketingWaitingStar();
            //获取未开始的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //置顶【未开始的活动】
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            String message=visitor.invokeApi(scene,false).getString("message");
            System.out.println(title+"-------"+message);
            //小程序中第一个为此活动
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            JSONObject response=businessUtil.appointmentActivityTitleNew();
            String title1=response.getJSONArray("list").getJSONObject(0).getString("title");
            Preconditions.checkArgument(title1.equals(title), "PC未开始活动的ID为：" + title+"小程序中的更多中的活动ID为："+title);
            Preconditions.checkArgument(message.equals("success"), "置顶未开始的活动的相关提示:" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容营销-【未开始】的活动-置顶");
        }
    }
    /**
     * 内容营销-取消【未开始】的活动
     */
    @Test(description = "内容营销-推广【未开始】的活动")
    public void cancelWaitingContentMarketing() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取未开始活动的ID
            List<Long> ids = businessUtil.getContentMarketingWaitingStar();
            //取消未开始的活动
            businessUtil.getCancelActivity(ids.get(0));
            //获取活动的状态
            int status = businessUtil.getActivityStatus(ids.get(0));
            System.err.println("------"+status);
            Preconditions.checkArgument(status == ActivityStatusEnum.CANCELED.getId(), "现在活动的状态为：" + status);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容营销-取消【未开始】的活动");
        }
    }

    /**
     * 内容营销-推广【未开始】的活动  推广
     */
    @Test(description = "内容营销-推广【未开始】的活动")
    public void promotionWaitingContentMarketing() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取未开始活动的ID
            List<Long> ids = businessUtil.getContentMarketingWaitingStar();
            //推广未开始的活动
            String appletCodeUrl = businessUtil.getPromotionActivity(ids.get(0));
            System.err.println(appletCodeUrl+"--------"+ids);
            Preconditions.checkArgument(!appletCodeUrl.equals(""), "推广【未开始】的活动的小程序二维码的返回值为空");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-推广【未开始】的活动");
        }
    }

    /**
     * 内容营销-【未开始】的活动-编辑：名称，活动规则----内容营销
     * 2021-3-17
     */
    @Test(enabled = true,description = "内容营销-【未开始】的活动-编辑")
    public void waitingStarContentMarketingEdit(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取未开始活动的ID
            List<Long> ids = businessUtil.getContentMarketingWaitingStar();
            System.err.println("----ids:"+ids.get(0));
            //编辑未开始中的活动
            String message=businessUtil.getContentMarketingEdit(ids.get(0),pp.contentMarketingNameEdit,pp.EditRule);
            System.out.println("---------"+message);
            //获取活动详情中编辑后的标题和活动规则
            String title = businessUtil.getFissionActivityDetailDate1(ids.get(0)).getString("title");
            String rule = businessUtil.getFissionActivityDetailDate1(ids.get(0)).getString("rule");
            System.out.println(title+"----------"+rule);
            System.out.println(rule);
            //变更记录
            Preconditions.checkArgument(message.equals("success")&&title.equals(pp.contentMarketingNameEdit)&&rule.equals(pp.EditRule), "进行中的活动编辑失败");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容营销-【未开始】的活动-编辑");
        }
    }

    /**
     * 内容营销-【未开始中】的活动-下架
     * 2021-3-17
     */
    @Test(enabled = true,description = "内容营销-【未开始中】的活动-下架")
    public void waitingContentMarketingOffLine(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Boolean flag=false;
            JSONObject lastValue=null;
            JSONArray list=null;
            //获取未开始活动的ID
            List<Long> ids = businessUtil.getContentMarketingWaitingStar();
            System.err.println("----ids:"+ids.get(0));
            //获取进行中的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //进行中的活动下架
            String message=businessUtil.getContentMarketingOffLine(ids.get(0));
            //获取活动的状态
            int statusOffLine=businessUtil.getActivityStatus(ids.get(0));
            //登录小程序
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            //获取小程序推荐列表
            do {
                IScene scene = AppletArticleListScene.builder().lastValue(lastValue).size(10).build();
                JSONObject response = visitor.invokeApi(scene);
                lastValue = response.getJSONObject("last_value");
                list = response.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String title1 = list.getJSONObject(i).getString("title");
                    if (title.equals(title1)) {
                        flag=true;
                    }
                }
            } while (list.size() == 10);
            Preconditions.checkArgument(message.equals("success")&&statusOffLine==ActivityStatusEnum.OFFLINE.getId(), "下架失败");
            Preconditions.checkArgument(!flag,"小程序中还是能够查看到此活动");
            jc.pcLogin(pp.phone,pp.password);
            //活动上架
            String message1=businessUtil.getContentMarketingOnline(ids.get(0));
            //获取活动的状态
            int statusOnLine=businessUtil.getActivityStatus(ids.get(0));
            //置顶此活动
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            visitor.invokeApi(scene,false).getString("message");
            //小程序中第一个为此活动
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            JSONObject response=businessUtil.appointmentActivityTitleNew();
            String title2=response.getJSONArray("list").getJSONObject(0).getString("title");
            Preconditions.checkArgument(statusOnLine==ActivityStatusEnum.WAITING_START.getId()&&message1.equals("success"),"上架以后活动的状态为："+statusOnLine);
            Preconditions.checkArgument(title2.equals(title), "PC进行中活动的名称为：" + title+"小程序中的更多中的活动名称为："+title2);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容营销-【未开始中】的活动-下架");
        }
    }

    /**
     * 内容营销-【已过期】的活动-查看
     * 2021-3-17
     */
    @Test(description = "内容营销-【已过期】的活动-查看")
    public void FinishContentMarketingCheck(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已过期活动的ID
            List<Long> ids = businessUtil.geContentMarketingFinish();
            if(ids.size()>0){
                //获取已过期的活动名称
                String title=businessUtil.getActivityTitle(ids.get(0));
                //获取活动详情中的此活动的名称
                IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
                String title1=visitor.invokeApi(scene).getString("title");
                System.out.println(title+"-------"+title1);
                Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容营销-【已过期】的活动-查看");
        }
    }

    /**
     * 内容营销-【未开始】的活动-下架和上架
     * 2021-3-17
     */
    @Test(enabled = true,description = "内容营销-【未开始中】的活动-下架")
    public void finishContentMarketingOffLine(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Boolean flag=false;
            JSONObject lastValue=null;
            JSONArray list=null;
            //获取未开始活动的ID
            List<Long> ids = businessUtil.geContentMarketingFinish();
            if(ids.size()>0){
                System.err.println("----ids:"+ids.get(0));
                //获取进行中的活动名称
                String title=businessUtil.getActivityTitle(ids.get(0));
                //进行中的活动下架
                String message=businessUtil.getContentMarketingOffLine(ids.get(0));
                //获取活动的状态
                int statusOffLine=businessUtil.getActivityStatus(ids.get(0));
                //登录小程序
                user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
                //获取小程序推荐列表
                do {
                    IScene scene = AppletArticleListScene.builder().lastValue(lastValue).size(10).build();
                    JSONObject response = visitor.invokeApi(scene);
                    lastValue = response.getJSONObject("last_value");
                    list = response.getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String title1 = list.getJSONObject(i).getString("title");
                        if (title.equals(title1)) {
                            flag=true;
                        }
                    }
                } while (list.size() == 10);
                Preconditions.checkArgument(message.equals("success")&&statusOffLine==ActivityStatusEnum.OFFLINE.getId(), "下架失败");
                Preconditions.checkArgument(!flag,"小程序中还是能够查看到此活动");
                jc.pcLogin(pp.phone,pp.password);
                //活动上架
                String message1=businessUtil.getContentMarketingOnline(ids.get(0));
                //获取活动的状态
                int statusOnLine=businessUtil.getActivityStatus(ids.get(0));
                //置顶此活动
                IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
                visitor.invokeApi(scene,false).getString("message");
                //小程序中第一个为此活动
                user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
                JSONObject response=businessUtil.appointmentActivityTitleNew();
                String title2=response.getJSONArray("list").getJSONObject(0).getString("title");
                Preconditions.checkArgument(statusOnLine==ActivityStatusEnum.WAITING_START.getId()&&message1.equals("success"),"上架以后活动的状态为："+statusOnLine);
                Preconditions.checkArgument(title2.equals(title), "PC进行中活动的名称为：" + title+"小程序中的更多中的活动名称为："+title2);

            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容营销-【未开始中】的活动-下架");
        }
    }

    /**
     * 内容营销-【已下架】的活动-查看
     * 2021-4/29
     */
    @Test(description = "内容营销-【已下架】的活动-查看")
    public void offlineContentMarketingCheck(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已下架活动的ID
            List<Long> ids = businessUtil.getContentMarketingOffLine();
            //获取已下架的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            System.out.println(title+"-------"+title1);
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容营销-【已下架】的活动-查看");
        }
    }

    /**
     * 内容营销-【已下架】的活动-删除
     * 2021-3-17
     */
    @Test(description = "内容营销-【已下架】的活动-删除")
    public void offlineContentMarketingDel(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已下架活动的ID
            List<Long> ids = businessUtil.getContentMarketingOffLine();
            //删除已下架的活动
            String message = businessUtil.getDelActivity(ids.get(0));
            Preconditions.checkArgument(message.equals("success"), "已下架的活动删除失败");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容营销-【已下架】的活动-删除");
        }
    }

    /**
     * 裂变活动-【进行中】的活动-下架,再上架
     * 2021-3-17
     */
    @Test(enabled = true,description = "裂变活动-【进行中】的活动-下架上架")
    public void workingFissionActivityOffLine(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Boolean flag=false;
            JSONObject lastValue=null;
            JSONArray list=null;
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getFissionActivityWorking();
            System.err.println("----ids:"+ids.get(0));
            //获取进行中的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //进行中的活动下架
            String message=businessUtil.getContentMarketingOffLine(ids.get(0));
            //获取活动的状态
            int statusOffLine=businessUtil.getActivityStatus(ids.get(0));
            //登录小程序
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            //获取小程序推荐列表
            do {
                IScene scene = AppletArticleListScene.builder().lastValue(lastValue).size(10).build();
                JSONObject response = visitor.invokeApi(scene);
                lastValue = response.getJSONObject("last_value");
                list = response.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String title1 = list.getJSONObject(i).getString("title");
                    if (title.equals(title1)) {
                        flag=true;
                        System.err.println("----------title1:"+title1);
                    }
                }
            } while (list.size() == 10);
            System.err.println(flag);
            System.out.println("----message:-------"+message+"---statusOffLine----"+statusOffLine);
            Preconditions.checkArgument(message.equals("success")&&statusOffLine==ActivityStatusEnum.OFFLINE.getId(), "下架失败");
            Preconditions.checkArgument(!flag,"小程序中还是能够查看到此活动");
            jc.pcLogin(pp.phone,pp.password);
            //活动上架
            String message1=businessUtil.getContentMarketingOnline(ids.get(0));
            //获取活动的状态
            int statusOnLine=businessUtil.getActivityStatus(ids.get(0));
            //置顶此活动
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            visitor.invokeApi(scene,false).getString("message");
            //小程序中第一个为此活动
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            JSONObject response=businessUtil.appointmentActivityTitleNew();
            String title2=response.getJSONArray("list").getJSONObject(0).getString("title");
            Preconditions.checkArgument(statusOnLine==ActivityStatusEnum.PASSED.getId()&&message1.equals("success"),"上架以后活动的状态为："+statusOnLine);
            Preconditions.checkArgument(title2.equals(title), "PC进行中活动的名称为：" + title+"小程序中的更多中的活动名称为："+title2);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【进行中】的活动-下架上架");
        }
    }

    /**
     * 裂变活动-【未开始中】的活动-下架和上架
     * 2021-3-17
     */
    @Test(enabled = true,description = "裂变活动-【未开始中】的活动-下架")
    public void finishFissionActivityOffLine(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Boolean flag=false;
            JSONObject lastValue=null;
            JSONArray list=null;
            //获取未开始活动的ID
            List<Long> ids = businessUtil.getFissionActivityWaitingStar();
            if(ids.size()>0){
                System.err.println("----ids:"+ids.get(0));
                //获取进行中的活动名称
                String title=businessUtil.getActivityTitle(ids.get(0));
                //进行中的活动下架
                String message=businessUtil.getContentMarketingOffLine(ids.get(0));
                //获取活动的状态
                int statusOffLine=businessUtil.getActivityStatus(ids.get(0));
                //登录小程序
                user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
                //获取小程序推荐列表
                do {
                    IScene scene = AppletArticleListScene.builder().lastValue(lastValue).size(10).build();
                    JSONObject response = visitor.invokeApi(scene);
                    lastValue = response.getJSONObject("last_value");
                    list = response.getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String title1 = list.getJSONObject(i).getString("title");
                        if (title.equals(title1)) {
                            flag=true;
                        }
                    }
                } while (list.size() == 10);
                Preconditions.checkArgument(message.equals("success")&&statusOffLine==ActivityStatusEnum.OFFLINE.getId(), "下架失败");
                Preconditions.checkArgument(!flag,"小程序中还是能够查看到此活动");
                jc.pcLogin(pp.phone,pp.password);
                //活动上架
                String message1=businessUtil.getContentMarketingOnline(ids.get(0));
                //获取活动的状态
                int statusOnLine=businessUtil.getActivityStatus(ids.get(0));
                //置顶此活动
                IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
                visitor.invokeApi(scene,false).getString("message");
                //小程序中第一个为此活动
                user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
                JSONObject response=businessUtil.appointmentActivityTitleNew();
                String title2=response.getJSONArray("list").getJSONObject(0).getString("title");
                Preconditions.checkArgument(statusOnLine==ActivityStatusEnum.WAITING_START.getId()&&message1.equals("success"),"上架以后活动的状态为："+statusOnLine);
                Preconditions.checkArgument(title2.equals(title), "PC进行中活动的名称为：" + title+"小程序中的更多中的活动名称为："+title2);

            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【未开始】的活动-下架");
        }
    }

    /**
     * 裂变活动-【已下架】的活动-查看
     * 2021-4/29
     */
    @Test(description = "裂变活动-【已下架】的活动-查看")
    public void offlineFissionActivityCheck(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已下架活动的ID
            List<Long> ids = businessUtil.getFissionActivityOffLine();
            //获取已下架的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            System.out.println(title+"-------"+title1);
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【已下架】的活动-查看");
        }
    }

    /**
     * 裂变活动-【已下架】的活动-删除
     * 2021-3-17
     */
    @Test(description = "裂变活动-【已下架】的活动-删除")
    public void offlineFissionActivityDel(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已下架活动的ID
            List<Long> ids = businessUtil.getFissionActivityOffLine();
            //删除已下架的活动
            String message = businessUtil.getDelActivity(ids.get(0));
            Preconditions.checkArgument(message.equals("success"), "已下架的活动删除失败");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【已下架】的活动-删除");
        }
    }

    /**
     * 裂变活动-【已取消】的活动-恢复
     * 2021-3-17
     */
    @Test(description = "裂变活动-【已取消】的活动-恢复")
    public void cancelFissionActivityRecover() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已取消活动的ID
            List<Long> ids = businessUtil.getFissionActivityCancel();
            //获取活动的状态
            int statusCancel=businessUtil.getActivityStatus(ids.get(0));
            //恢复活动并获取状态
            String message=businessUtil.getContentMarketingRecover(ids.get(0));
            int statusRecover=businessUtil.getActivityStatus(ids.get(0));
            Preconditions.checkArgument(message.equals("success"), "活动恢复失败" );
            Preconditions.checkArgument(statusCancel==ActivityStatusEnum.CANCELED.getId()&&statusRecover==ActivityStatusEnum.PENDING.getId(), "恢复以后的状态为：：" + statusRecover);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("裂变活动-【已取消】的活动-恢复");
        }
    }

    /**
     * 招募活动-【进行中】的活动-下架,再上架
     * 2021-3-17
     */
    @Test(enabled = true,description = "招募活动-【进行中】的活动-下架,再上架")
    public void workingRecruitActivityOffLine(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Boolean flag=false;
            JSONObject lastValue=null;
            JSONArray list=null;
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getRecruitActivityWorking();
            System.err.println("----ids:"+ids.get(0));
            //获取进行中的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //进行中的活动下架
            String message=businessUtil.getContentMarketingOffLine(ids.get(0));
            //获取活动的状态
            int statusOffLine=businessUtil.getActivityStatus(ids.get(0));
            //登录小程序
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            //获取小程序推荐列表
            do {
                IScene scene = AppletArticleListScene.builder().lastValue(lastValue).size(10).build();
                JSONObject response = visitor.invokeApi(scene);
                lastValue = response.getJSONObject("last_value");
                list = response.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String title1 = list.getJSONObject(i).getString("title");
                    if (title.equals(title1)) {
                        flag=true;
                        System.err.println("----------title1:"+title1);
                    }
                }
            } while (list.size() == 10);
            System.err.println(flag);
            System.out.println("----message:-------"+message+"---statusOffLine----"+statusOffLine);
            Preconditions.checkArgument(message.equals("success")&&statusOffLine==ActivityStatusEnum.OFFLINE.getId(), "下架失败");
            Preconditions.checkArgument(!flag,"小程序中还是能够查看到此活动");
            jc.pcLogin(pp.phone,pp.password);
            //活动上架
            String message1=businessUtil.getContentMarketingOnline(ids.get(0));
            //获取活动的状态
            int statusOnLine=businessUtil.getActivityStatus(ids.get(0));
            //置顶此活动
            IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
            visitor.invokeApi(scene,false).getString("message");
            //小程序中第一个为此活动
            user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
            JSONObject response=businessUtil.appointmentActivityTitleNew();
            String title2=response.getJSONArray("list").getJSONObject(0).getString("title");
            Preconditions.checkArgument(statusOnLine==ActivityStatusEnum.PASSED.getId()&&message1.equals("success"),"上架以后活动的状态为："+statusOnLine);
            Preconditions.checkArgument(title2.equals(title), "PC进行中活动的名称为：" + title+"小程序中的更多中的活动名称为："+title2);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【进行中】的活动-下架,再上架");
        }
    }

    /**
     * 招募活动-【已下架】的活动-查看
     * 2021-4/29
     */
    @Test(description = "招募活动-【已下架】的活动-查看")
    public void offlineRecruitActivityCheck(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已下架活动的ID
            List<Long> ids = businessUtil.getRecruitActivityOffLine();
            //获取已下架的活动名称
            String title=businessUtil.getActivityTitle(ids.get(0));
            //获取活动详情中的此活动的名称
            IScene scene=ManageDetailScene.builder().id(ids.get(0)).build();
            String title1=visitor.invokeApi(scene).getString("title");
            System.out.println(title+"-------"+title1);
            Preconditions.checkArgument(title.equals(title1), "现在活动的名称为：" + title);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【已下架】的活动-查看");
        }
    }

    /**
     * 招募活动-【已下架】的活动-删除
     * 2021-3-17
     */
    @Test(description = "招募活动-【已下架】的活动-删除")
    public void offlineRecruitActivityDel(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已下架活动的ID
            List<Long> ids = businessUtil.getRecruitActivityOffLine();
            //删除已下架的活动
            String message = businessUtil.getDelActivity(ids.get(0));
            Preconditions.checkArgument(message.equals("success"), "已下架的活动删除失败");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【已下架】的活动-删除");
        }
    }

    /**
     * 招募活动-【未开始】的活动-下架和上架
     * 2021-3-17
     */
    @Test(enabled = true,description = "招募活动-【未开始中】的活动-下架")
    public void finishRecruitActivityOffLine(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Boolean flag=false;
            JSONObject lastValue=null;
            JSONArray list=null;
            //获取未开始活动的ID
            List<Long> ids = businessUtil.geContentMarketingFinish();
            if(ids.size()>0){
                System.err.println("----ids:"+ids.get(0));
                //获取进行中的活动名称
                String title=businessUtil.getActivityTitle(ids.get(0));
                //进行中的活动下架
                String message=businessUtil.getContentMarketingOffLine(ids.get(0));
                //获取活动的状态
                int statusOffLine=businessUtil.getActivityStatus(ids.get(0));
                //登录小程序
                user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
                //获取小程序推荐列表
                do {
                    IScene scene = AppletArticleListScene.builder().lastValue(lastValue).size(10).build();
                    JSONObject response = visitor.invokeApi(scene);
                    lastValue = response.getJSONObject("last_value");
                    list = response.getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String title1 = list.getJSONObject(i).getString("title");
                        if (title.equals(title1)) {
                            flag=true;
                        }
                    }
                } while (list.size() == 10);
                Preconditions.checkArgument(message.equals("success")&&statusOffLine==ActivityStatusEnum.OFFLINE.getId(), "下架失败");
                Preconditions.checkArgument(!flag,"小程序中还是能够查看到此活动");
                jc.pcLogin(pp.phone,pp.password);
                //活动上架
                String message1=businessUtil.getContentMarketingOnline(ids.get(0));
                //获取活动的状态
                int statusOnLine=businessUtil.getActivityStatus(ids.get(0));
                //置顶此活动
                IScene scene=ActivityManageTopScene.builder().id(ids.get(0)).build();
                visitor.invokeApi(scene,false).getString("message");
                //小程序中第一个为此活动
                user.loginApplet(EnumAppletToken.JC_GLY_ONLINE);
                JSONObject response=businessUtil.appointmentActivityTitleNew();
                String title2=response.getJSONArray("list").getJSONObject(0).getString("title");
                Preconditions.checkArgument(statusOnLine==ActivityStatusEnum.WAITING_START.getId()&&message1.equals("success"),"上架以后活动的状态为："+statusOnLine);
                Preconditions.checkArgument(title2.equals(title), "PC进行中活动的名称为：" + title+"小程序中的更多中的活动名称为："+title2);

            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【未开始中】的活动-下架");
        }
    }

    /**
     * 招募活动-【已取消】的活动-恢复
     */
    @Test(description = "招募活动-【已取消】的活动-恢复")
    public void cancelActivityRecover() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已取消活动的ID
            List<Long> ids = businessUtil.getRecruitActivityCancel();
            //获取活动的状态
            int statusCancel=businessUtil.getActivityStatus(ids.get(0));
            //恢复活动并获取状态
            String message=businessUtil.getContentMarketingRecover(ids.get(0));
            int statusRecover=businessUtil.getActivityStatus(ids.get(0));
            Preconditions.checkArgument(message.equals("success"), "活动恢复失败" );
            Preconditions.checkArgument(statusCancel==ActivityStatusEnum.CANCELED.getId()&&statusRecover==ActivityStatusEnum.PENDING.getId(), "恢复以后的状态为：：" + statusRecover);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-【已取消】的活动-恢复");
        }
    }


    /**
     * 创建招募活动-优惠券的可用库存校验-创建卡券,创建活动,优惠券数量不变,审批通过,可用库存数量减少
     */
    @Test(description = "创建招募活动-优惠券的可用库存校验-创建卡券,创建活动,优惠券数量不变,审批通过,可用库存数量减少")
    public void VoucherAllowUseInventoryCheck() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建卡券
            Long voucherId=supporterUtil.createVoucherId(1000, VoucherTypeEnum.COUPON);
            //获取卡券的名字
            String voucherName = supporterUtil.getVoucherName(voucherId);
            //审批通过
            supporterUtil.applyVoucher(voucherName, "1");
            //获取卡券的可用库存
            Long AllowUseInventory=businessUtil.getVoucherAllowUseInventoryNum(voucherId);
            //创建活动
            Long activityId =businessUtil.createRecruitActivity(voucherId,true,0,true);
            //获取卡券的可用库存
            Long AllowUseInventoryBefore=businessUtil.getVoucherAllowUseInventoryNum(voucherId);
            //审批活动
            businessUtil.getApprovalPassed(activityId);
            //获取卡券的可用库存
            Long AllowUseInventoryAfter=businessUtil.getVoucherAllowUseInventoryNum(voucherId);
            System.out.println("可用库存的数量不为:"+AllowUseInventory+"+++++++++"+AllowUseInventoryAfter+"    "+activityId);
            Preconditions.checkArgument(AllowUseInventoryBefore==1000&&AllowUseInventoryAfter==0L,"可用库存的数量不为0");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData(" 创建招募活动-优惠券的可用库存校验-创建卡券,创建活动,优惠券数量不变,审批通过,可用库存数量减少");
        }
    }





}