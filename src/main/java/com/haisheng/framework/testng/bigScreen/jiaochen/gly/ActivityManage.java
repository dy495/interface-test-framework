package com.haisheng.framework.testng.bigScreen.jiaochen.gly;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.util.BusinessUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.util.PublicParameter;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityApprovalStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.FileUpload;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityManage extends TestCaseCommon implements TestCaseStd {
    ScenarioUtil jc = new ScenarioUtil();
    private static final EnumTestProduce product = EnumTestProduce.JIAOCHEN_DAILY;
    private static final EnumAppletToken APPLET_USER = EnumAppletToken.JC_GLY_DAILY;
    public Visitor visitor = new Visitor(product);
    BusinessUtil businessUtil = new BusinessUtil(visitor);
    SupporterUtil supporterUtil = new SupporterUtil(visitor);
    PublicParameter pp = new PublicParameter();
    UserUtil user = new UserUtil(visitor);

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_JIAOCHEN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "郭丽雅";
        commonConfig.product = product.getAbbreviation();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "jc-daily-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.referer = "https://servicewechat.com/wxbd41de85739a00c7/0/page-frame.html";
        commonConfig.shopId = product.getShopId();
        commonConfig.roleId = "603";
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
        jc.pcLogin("13114785236", pp.password);
    }


    /**
     * 创建5个招募活动和个裂变活动
     */
    @Test
    public void preCreateSomeActivity() {
        //获取一个卡券
        Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
        //创建招募活动
        Long activityId = businessUtil.createRecruitActivity(voucherId, true, 0, true);
        
        //创建裂变活动
        Long activityId1 = businessUtil.createFissionActivity(voucherId);


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
     * 创建招募活动-数据一致性校验{【活动详情】发放数量=创建时的填写数量,【活动详情】面值=创建时填写的数量,【活动详情】剩余库存=创建时填写数量}   ok
     */
    @Test(description = "创建招募活动-{【活动详情】发放数量=创建时的填写数量,【活动详情】面值=创建时填写的数量,【活动详情】剩余库存=创建时填写数量}")
    public void createActivityDate2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取一个卡券
            Long voucherId = businessUtil.getVoucherId();
            //创建招募活动
            Long activityId = businessUtil.createRecruitActivity(voucherId, true, 0, true);
            //优惠券的面值
            String parValue = businessUtil.getPrice(voucherId);
            JSONArray list = businessUtil.getRecruitActivityDetail(activityId);
            //活动详情发放数量
            String num = list.getJSONObject(0).getString("num");
            //活动详情剩余库存
            String leftNum = list.getJSONObject(0).getString("left_num");
            //活动详情中-优惠券面值
            String price = list.getJSONObject(0).getString("price");
            //填写库存数量
            String number = String.valueOf(businessUtil.getVoucherSurplusInventory(voucherId));
            Preconditions.checkArgument(num.equals(number) && leftNum.equals(number) && price.equals(parValue), "活动详情中的数值与创建时的数字不一致");
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
     * 活动管理-编辑招募活动的规则和标题，①内容更新，②【调整记录】+1&调整类型=修改活动   编辑未调通--接口问题：编辑活动未进入变更记录中
     */
    @Test
    public void editWorkingActivityDate4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中的活动ID
            List<Long> ids = businessUtil.getActivityWorking();
            //编辑前-变更记录条数
            JSONArray list = businessUtil.changeRecordPage(ids.get(0)).getJSONArray("list");
            int numBefore = list.size();
            //编辑活动名称、活动名额
            String message = businessUtil.activityEditScene(ids.get(0));
            System.err.println("--------" + message);
            //获取活动详情中编辑后的标题和活动规则
            JSONObject object = businessUtil.getRecruitActivityDetailDate(ids.get(0));
            String title = object.getString("title");
            String rule = object.getString("rule");
            Preconditions.checkArgument(title.equals(pp.editTitle) && rule.equals(pp.EditRule), "编辑后的活动名字为：" + title + "  编辑后的活动规则为：" + rule);
            //编辑后 变更记录+1&调整类型=修改活动
            int numAfter = list.size();
            String content = list.getJSONObject(0).getString("content");
            Preconditions.checkArgument(numAfter == numBefore + 1 && content.equals("修改活动"), "编辑前变更记录的条数为：" + numBefore + "编辑后的变更记录的条数为：" + numAfter + "  编辑后变更记录新增的内容为：" + content);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
//            saveData(" 活动管理-编辑招募活动的规则和标题，①内容更新，②【调整记录】+1&调整类型=修改活动");
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
            String message = businessUtil.getCancelActivity(ids.get(0));
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
     * 活动管理-招募活动详情-①成本合计=发放数量*面值   ②总数量=剩余库存+发放数量&总数量>=剩余库存     ok
     */
    @Test(description = "活动管理-活动详情-①成本合计=发放数量*面值   ②总数量=剩余库存+发放数量&总数量>=剩余库存")
    public void activityDetailDate6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中的活动招募ID
            List<Long> ids = businessUtil.getRecruitActivityWorking();
            //进入此活动的活动详情页
            JSONArray list = businessUtil.getRecruitActivityDetail(ids.get(0));
            //总成本
            String totalCost = list.getJSONObject(0).getString("total_cost");
            //面值
            String price = list.getJSONObject(0).getString("price");
            //发放数量
            int sendNum = list.getJSONObject(0).getInteger("send_num");
            //剩余数量
            int leftNum = list.getJSONObject(0).getInteger("left_num");
            //奖励项总数量
            int num = list.getJSONObject(0).getInteger("num");
//            System.out.println("-------"+"总成本为："+totalCost+"  应等于面值*数量为"+Double.parseDouble(price)*num+"  奖励项总数为："+num+"  发放数量为："+sendNum+"  剩余数量为："+leftNum);
            Preconditions.checkArgument(Double.parseDouble(totalCost) == Double.parseDouble(price) * num && num == leftNum + sendNum && num >= leftNum, "总成本为：" + totalCost + "  应等于面值*数量为" + Double.parseDouble(price) * num + "  奖励项总数为：" + num + "  发放数量为：" + sendNum + "  剩余数量为：" + leftNum);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-活动详情-①成本合计=发放数量*面值 + ②总数量=剩余库存+发放数量&总数量>=剩余库存");
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
            int registerPassedNum = 0;
            //报名列表的返回值
            JSONObject pageRes = businessUtil.getRegisterPage(ids.get(0));
            int pages = pageRes.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                IScene scene = ManageRegisterPageScene.builder().page(page).size(10).activityId(ids.get(0)).build();
                JSONArray list = visitor.invokeApi(scene).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    int status = list.getJSONObject(i).getInteger("status");
                    if (status == ActivityApprovalStatusEnum.PASSED.getId()) {
                        registerPassedNum = list.getJSONObject(i).getInteger("register_num");
                        //报名审核通过的人数
                        registerPassedNum += registerPassedNum;
                    }
                }
            }

            //报名数据的返回值
            JSONObject dataRes = businessUtil.getRegisterData(ids.get(0));
            //活动名额
            int quota = dataRes.getInteger("quota");
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
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动报名列---①活动名额>=报名成功人数和 ②报名成功数=【全部列表】状态为审核通过人数和  ③已报名数>=报名成功数");
        }
    }

    /**
     * 报名列表-已报名数=【全部】报名人数和=【待审核】【报名列表】【报名失败】人数和     未完成
     */
    @Test(enabled = false)
    public void activityRegisterDate8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取【进行中】的招募活动ID
            List<Long> ids = businessUtil.getRecruitActivityWorking();
            int registerNum = 0;
            //报名列表的返回值
            JSONObject pageRes = businessUtil.getRegisterPage(ids.get(0));
            int pages = pageRes.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                IScene scene = ManageRegisterPageScene.builder().page(page).size(10).activityId(ids.get(0)).build();
                JSONArray list = visitor.invokeApi(scene).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    int status = list.getJSONObject(i).getInteger("status");
                    String customerPhone = list.getJSONObject(i).getString("customer_phone");
                    String modify_time = list.getJSONObject(i).getString("modify_time");
                    List<String> phoneArray = businessUtil.phoneSameArrayCheck(ids.get(0));
                    //todo 报名人数（去重）
                    for (int j = 0; j < phoneArray.size(); j++) {
                        if (customerPhone.equals(phoneArray.get(j))) {
                            //todo 怎么处理重复数据

                        } else {
                            registerNum = list.getJSONObject(i).getInteger("register_num");
                            registerNum += registerNum;
                        }
                    }

                }
            }

            //报名数据的返回值
            JSONObject dataRes = businessUtil.getRegisterData(ids.get(0));
            //活动名额
            int quota = dataRes.getInteger("quota");
            //报名总人数
            int total = dataRes.getInteger("total");
            //待审批人数
            int wait = dataRes.getInteger("wait");
            //报名成功的人数
            int passed = dataRes.getInteger("passed");
            //报名失败的人数
            int failed = dataRes.getInteger("failed");

            Preconditions.checkArgument(total == registerNum && total == (wait + passed + failed), "报名总人数为：" + total + "  （待审批+报名成功+报名失败）人数为：" + (wait + passed + failed) + "  列表中的报名人数和为：" + registerNum);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("报名列表-已报名数=【全部】报名人数和=【待审核】【报名列表】【报名失败】人数和");
        }
    }

    /**
     * 报名列表-审批通过1条，报名成功&报名成功列表     小程序无法报名
     */
    @Test
    public void activityRegisterDate9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int registerNum = 0;
            //获取进行中的活动存在待审批数量的ID
            List<Long> ids = businessUtil.getRecruitActivityWorkingApproval();
            //审批通过之前报名成功的数量
            int passedBefore = businessUtil.getRegisterData(ids.get(0)).getInteger("passed");
            //待审批活动的ID合集
            List<Long> idArray = businessUtil.registerApproval(ids.get(0));
            //审批通过其中一条
            businessUtil.getRegisterApprovalPassed(idArray.get(0));
            //审批通过的活动人数
            int pages = businessUtil.getRegisterPage(ids.get(0)).getInteger("pages");
            for (int page = 1; page < pages; page++) {
                IScene scene = ManageRegisterPageScene.builder().page(page).size(10).activityId(ids.get(0)).build();
                JSONArray List = visitor.invokeApi(scene).getJSONArray("list");
                for (int i = 0; i < List.size(); i++) {
                    Long idOne = List.getJSONObject(i).getLong("id");
                    int status = List.getJSONObject(i).getInteger("status");
                    if (idArray.get(0).equals(idOne) && status == ActivityApprovalStatusEnum.PASSED.getId()) {
                        registerNum = List.getJSONObject(i).getInteger("register_num");
                        registerNum += registerNum;
                    }
                }
            }
            //审批通过之后报名成功的数量
            int passedAfter = businessUtil.getRegisterPage(ids.get(0)).getInteger("passed");
            Preconditions.checkArgument(idArray.size() > 0 && passedAfter == (passedBefore + registerNum), "审批的通过的人数为:" + registerNum);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("报名列表-审批通过1条，报名成功&报名成功列表");
        }
    }

    /**
     * 报名列表-审批不通过1条，报名失败列表      小程序无法报名
     */
    @Test
    public void activityRegisterDate10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int registerNum = 0;
            //获取进行中的活动存在待审批数量的ID
            List<Long> ids = businessUtil.getRecruitActivityWorkingApproval();
            //审批通过之前报名成功的数量
            int failedBefore = businessUtil.getRegisterData(ids.get(0)).getInteger("failed");
            //待审批活动的ID合集
            List<Long> idArray = businessUtil.registerApproval(ids.get(0));
            //审批通过其中一条
            businessUtil.getRegisterApprovalPassed(idArray.get(0));
            //审批通过的活动人数
            int pages = businessUtil.getRegisterPage(ids.get(0)).getInteger("pages");
            for (int page = 1; page < pages; page++) {
                IScene scene = ManageRegisterPageScene.builder().page(page).size(10).activityId(ids.get(0)).build();
                JSONArray List = visitor.invokeApi(scene).getJSONArray("list");
                for (int i = 0; i < List.size(); i++) {
                    //报名ID
                    Long idOne = List.getJSONObject(i).getLong("id");
                    int status = List.getJSONObject(i).getInteger("status");
                    if (idArray.get(0).equals(idOne) && status == ActivityApprovalStatusEnum.REJECT.getId()) {
                        registerNum = List.getJSONObject(i).getInteger("register_num");
                        registerNum += registerNum;
                    }
                }
            }
            //审批通过之后报名成功的数量
            int failedAfter = businessUtil.getRegisterPage(ids.get(0)).getInteger("failed");
            Preconditions.checkArgument(idArray.size() > 0 && failedAfter == (failedBefore + registerNum), "审批不通过的人数为:" + registerNum);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("报名列表-审批不通过1条，报名失败列表");
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
     * 活动审批--①全部审批=【全部】列表数  ②全部审批=【待审核】【审核通过】【审核未通过】列表数加和      数据不准确：活动审批数据-全部活动为：85审批列表中全部活动的列表数为：90
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
            System.err.println("活动审批数据-全部活动为：" + total + "审批列表中全部活动的列表数为：" + totalNum);
            Preconditions.checkArgument(total == totalNum, "活动审批数据-全部活动为：" + total + "审批列表中全部活动的列表数为：" + totalNum);
            Preconditions.checkArgument(total == (waitNum + passedNum + failedNum), "活动审批数据-全部活动为：" + total + "【待审核】【审核通过】【审核未通过】列表数加和为：" + (waitNum + passedNum + failedNum));
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动审批--①全部审批=【全部】列表数  ②全部审批=【待审核】【审核通过】【审核未通过】列表数加和");
        }
    }

    /**
     * 活动审批--①待审批=活动状态为待审批的列表数  ②审批通过=活动状态为审批通过的列表数 ③审批未通过=活动状态为审批未通过的列表数          数据存在问题，bug7102
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
            //获取活动审批的数据
            JSONObject response = businessUtil.getActivityApprovalDate();
            //待审批活动
            int waitBefore = response.getInteger("wait");
            //通过的活动
            int passedBefore = response.getInteger("passed");
            //获取进行中的活动存在待审批ID合集
            List<Long> ids = businessUtil.getActivityWait();
            if (ids.size() > 0) {
                //审批通过其中一条
                businessUtil.getApprovalPassed(ids.get(0));
                //获取审批后活动审批的数据
                JSONObject response1 = businessUtil.getActivityApprovalDate();
                //待审批活动
                int waitAfter = response1.getInteger("wait");
                //通过的活动
                int passedAfter = response1.getInteger("passed");
                //获取活动审批后的状态
                int status = businessUtil.getActivityStatus(ids.get(0));
                Preconditions.checkArgument(waitAfter == (waitBefore - 1), "活动审批数后的待审批数量：" + waitAfter + "活动审批数前的待审批数量：" + waitBefore);
                Preconditions.checkArgument(passedAfter == (passedBefore + 1), "活动审批数后的审批通过数量：" + passedAfter + "活动审批数前的审批通过数量：" + passedBefore);
                Preconditions.checkArgument(status == ActivityStatusEnum.PASSED.getId(), "活动审批数后活动的状态应为【进行中】，此时为为：" + status);

            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动审批--审批通过，待审批-1&审批通过+1");
        }
    }

    /**
     * 活动审批--审批驳回，待审批-1&审批未通过+1          审批不通过，审批未通过的数据没有变化【bug7103】
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
                businessUtil.getApprovalPassed(ids.get(0));
                //获取审批后活动审批的数据
                JSONObject response1 = businessUtil.getActivityApprovalDate();
                //待审批活动
                int waitAfter = response1.getInteger("wait");
                //通过的活动
                int failedAfter = response1.getInteger("failed");
                //获取活动审批后的状态
                int status = businessUtil.getActivityStatus(ids.get(0));
                Preconditions.checkArgument(waitAfter == (waitBefore - 1), "活动审批数后的待审批数量：" + waitAfter + "活动审批数前的待审批数量：" + waitBefore);
                Preconditions.checkArgument(failedAfter == (failedBefore + 1), "活动审批数后的审批未通过数量：" + failedAfter + "活动审批数前的审批未通过数量：" + failedBefore);
                Preconditions.checkArgument(status == ActivityStatusEnum.REJECT.getId(), "活动审批数后活动的状态应为【审核未通过】，此时为为：" + status);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动审批--审批驳回，待审批-1&审批未通过+1");
        }
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
            //再次创建裂变活动
            Long activityId2 = businessUtil.createFissionActivity(voucherId);
            //审批通过裂变活动
            businessUtil.getApprovalPassed(activityId2);
            //获取活动的状态
            int statusPassed = businessUtil.getActivityStatus(activityId2);
            //登录小程序
//            authorization.equals(EnumAppletToken.JC_GLY_DAILY);
            //小程序列表中可看见此活动的
//            int pages=businessUtil.getAppletArticleList().getInteger("pages");
//            for(int page=1;page<=pages;page++){
//                IScene scene= AppletArticleList.builder().lastValue(page).size(10).build();
//                JSONArray list=visitor.invokeApi(scene).getJSONArray("list");
//                for(int i=0;i<list.size();i++){
//                    Long id=list.getJSONObject(i).getLong("id");
//                    if(id==activityId){
//                        flag=true;
//                    }
//                }
//            }
            Preconditions.checkArgument(status == ActivityStatusEnum.PENDING.getId(), "待审核的活动状态为：" + status);
            Preconditions.checkArgument(statusRevoke == ActivityStatusEnum.REVOKE.getId(), "已撤销的活动状态为：" + statusRevoke);
            Preconditions.checkArgument(statusPassed == ActivityStatusEnum.PASSED.getId(), "审批通过的活动状态为：" + statusPassed);
//            Preconditions.checkArgument(flag.equals(true),"小程序中不展示此审批通过的活动");
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
            //获取一个卡券
            Long voucherId = businessUtil.getVoucherId();
            //创建招募活动
            Long activityId = businessUtil.createRecruitActivity(voucherId, true, 0, true);
            //获取活动的状态
            int status = businessUtil.getActivityStatus(activityId);
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            //获取活动的状态
            int statusPassed = businessUtil.getActivityStatus(activityId);
//            //登录小程序
//            authorization.equals(EnumAppletToken.JC_GLY_DAILY);
//            //小程序报名此活动
//            businessUtil.activityRegisterApplet(activityId,"13373166806","max",3,"1513184362@qq.com");
//             //登录PC
//            jc.pcLogin(pp.phone, pp.password);
//            //审批通过小程序活动报名
//            List<Long> ids=businessUtil.RegisterAppletIds(activityId);
//            IScene scene = ManageRegisterApprovalScene.builder().ids(ids).status(ActivityApprovalStatusEnum.PASSED.getId()).build();
//            visitor.invokeApi(scene,false).getString("message");
//            //获取卡券码
//            List<VoucherSendRecord> vList=supporterUtil.getVoucherSendRecordList(voucherId);
//            String voucherCode= vList.get(0).getVoucherCode();
//            //登录小程序
//            authorization.equals(EnumAppletToken.JC_GLY_DAILY);
//            //查询是否获得此卡券(通过卡券码查询，看看能否有此卡券的返回值)
//            AppletVoucherInfo voucher=supporterUtil.getAppletVoucherInfo(voucherCode);
//            //小程序取消报名
//            businessUtil.activityCancelScene(activityId);
//            //报名取消后获得此报名的状态
//            String activityStatus=businessUtil.appointmentActivityStatus(activityId);
//            //取消报名后卡券的状态为【已失效】
//            String voucherStatus=supporterUtil.getAppletVoucherInfo(voucherCode).getStatus();

            Preconditions.checkArgument(status == ActivityStatusEnum.PENDING.getId(), "待审核的活动状态为：" + status);
            Preconditions.checkArgument(statusPassed == ActivityStatusEnum.PASSED.getId(), "审核通过的活动状态为：" + statusPassed);
//            Preconditions.checkArgument(voucher!=null,"小程序报名通过没有获得此卡券");
//            Preconditions.checkArgument(activityStatus.equals(ActivityStatusEnum.CANCELED.getStatusName()),"小程序报名通过取消以后此卡券的状态为："+activityStatus);
//            Preconditions.checkArgument(voucherStatus.equals("已失效"),"取消活动以后此卡券的状态为："+voucherStatus);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建招募活动-活动审批通过-报名活动-点击审批提醒进入报名审批页面-审批通过活动报名-取消活动");
        }
    }


    /**
     * 测试的case
     */
    @Test
    public void test() {
        try {

//            //获取进行中的活动ID
            List<Long> ids = businessUtil.getRecruitActivityWorking();
            System.out.println("------" + ids);
            //编辑活动
            String message = businessUtil.activityEditScene(124L);
            System.err.println("----------" + message);
            sleep(3);
            JSONObject object = businessUtil.getRecruitActivityDetailDate1(124L);
            String title = object.getString("title");
            String rule = object.getJSONObject("recruit_activity_info").getString("rule");
            System.err.println(title + "-------" + rule);
//            //编辑活动名称、活动名额
//            IScene scene = ManageRecruitEditScene.builder().id(ids.get(0)).isLimitQuota(true).isNeedApproval(true).title(pp.editTitle).rule(pp.EditRule).build();
//            visitor.invokeApi(scene);
//            //获取编辑后的标题和活动规则
//            JSONObject object=businessUtil.getActivityRespond(ids.get(0));
//            String title=object.getString("title");
//            String rule=object.getString("rule");
//            System.out.println("--------标题："+title+"--------"+rule);

//            //获取一个卡券
//            Long voucherId=businessUtil.getVoucherId();
//            System.out.println("-----"+voucherId);
//            //创建招募活动
//            Long activityId=businessUtil.createRecruitActivity(voucherId,true,0,true);
//            //创建裂变活动
//            Long activityId1=businessUtil.createFissionActivity(voucherId);
//            int status=businessUtil.getActivityStatus(activityId);
//            System.err.println("----创建活动的ID为："+activityId+"状态为："+status);
            //活动列表
//            IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
//            JSONObject respon=visitor.invokeApi(scene);
//            System.err.println("---------"+respon);
            //登录小程序
//            user.loginApplet(APPLET_USER);
//            JSONObject res=businessUtil.getAppletArticleList();
//            System.err.println(res);
//            JSONObject object= businessUtil.appointmentActivityList();
//            System.err.println("----"+object);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("测试case");
        }


    }


/**
 * ==============================活动列表的各个状态=======================
 */


    /**
     * 活动管理-活动审批通过  ok
     */
    @Test(description = "活动管理-活动审批通过")
    public void activityApproval() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取列表中状态为【待审批】的活动ID
            List<Long> ids = businessUtil.getActivityWaitingApproval();
            //通过待审批的活动
            String message = businessUtil.getApprovalPassed(ids.get(0));
            //获取刚才通过的活动的状态
            int status = businessUtil.getActivityStatus(ids.get(0));
            Preconditions.checkArgument(status == ActivityStatusEnum.PASSED.getId(), "审批通过后活动的状态为：" + status);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-活动审批通过");
        }
    }

    /**
     * 活动管理-活动审批未通过 ok
     */
    @Test(description = "活动管理-活动审批未通过")
    public void activityNotApproval() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取列表中状态为【待审批】的活动ID
            List<Long> ids = businessUtil.getActivityWaitingApproval();
            //审批不通过待审批的活动
            businessUtil.getApprovalReject(ids.get(0));
            //获取刚才不通过的活动的状态
            int status = businessUtil.getActivityStatus(ids.get(0));
            Preconditions.checkArgument(status == ActivityStatusEnum.REJECT.getId(), "审批未通过后活动的状态为：" + status);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-活动审批未通过");
        }
    }

    /**
     * 活动管理-撤回【待审批】的活动   ok
     */
    @Test(description = "活动管理-【待审批】的活动撤回")
    public void revokeApprovalsActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待审批活动的ID
            List<Long> ids = businessUtil.getActivityWait();
            //撤回待审批的活动
            String message = businessUtil.getRevokeActivity(ids.get(0));
            //获取刚才通过的活动的状态
            int status = businessUtil.getActivityStatus(ids.get(0));
            Preconditions.checkArgument(status == ActivityStatusEnum.REVOKE.getId(), "撤回的待审批的活动,现活动的状态为：" + ActivityStatusEnum.REVOKE.getStatusName());

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-活动审批未通过");
        }
    }

    /**
     * 活动管理-审核不通过【待审批】的活动  ok
     */
    @Test(description = "活动管理-【待审批】的活动撤回")
    public void approvalsActivityFail() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待审批活动的ID
            List<Long> ids = businessUtil.getActivityWait();
            //活动审批不通过
            businessUtil.getApprovalReject(ids.get(0));
            //获取此活动的状态
            int status = businessUtil.getActivityStatus(ids.get(0));
            Preconditions.checkArgument(status == ActivityStatusEnum.REJECT.getId(), "审批不通过,现活动的状态为：" + ActivityStatusEnum.REJECT.getStatusName());
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-审核不通过【待审批】的活动");
        }
    }


    /**
     * 活动管理-删除【审核未通过】的活动       ok
     */
    @Test(description = "活动管理-删除【审核未通过】的活动")
    public void delWorkingActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取审核未通过活动的ID
            List<Long> ids = businessUtil.getActivityReject();
            //删除进行中的活动
            String message = businessUtil.getDelActivity(ids.get(0));
            Preconditions.checkArgument(message.equals("success"), "删除【审核未通过】的活动的message为：" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-删除【审核未通过】的活动");
        }
    }


    /**
     * 活动管理-推广【进行中】的活动    接口不通
     */
    @Test(description = "活动管理-推广【进行中】的活动")
    public void promotionWorkingActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getActivityWorking();
            //推广进行中的活动
            String appletCodeUrl = businessUtil.getPromotionActivity(ids.get(0));
            Preconditions.checkArgument(!appletCodeUrl.equals(""), "推广【进行中】的活动的小程序二维码的返回值为空");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-推广【进行中】的活动");
        }
    }

    /**
     * 活动管理-取消【进行中】的活动     ok
     */
    @Test(description = "活动管理-推广【进行中】的活动")
    public void cancelWorkingActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            List<Long> ids = businessUtil.getActivityWorking();
            //取消进行中的活动
            businessUtil.getCancelActivity(ids.get(0));
            //获取活动的状态
            int status = businessUtil.getActivityStatus(ids.get(0));
            Preconditions.checkArgument(status == ActivityStatusEnum.CANCELED.getId(), "现在活动的状态为：" + status);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-取消【进行中】的活动");
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
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
                Preconditions.checkArgument(message.equals("分享人数不能为空") || message.equals("分享人数范围为[1,9999]"), "创建活动分享人数为：" + shareNum[i]);

            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建活动-分享人数的异常情况{不填写、10000、中文、英文、标点符号}");
        }
    }

    /**
     * 创建活动-活动规则异常{不填写，2001位}
     */
    @Test(description = "创建活动-活动规则异常{2001位}")
    public void fissionVoucherTitleException4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //添加活动图片
            List<String> picList = new ArrayList<>();
            picList.add(supporterUtil.getPicPath());
            //获取优惠券ID
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
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
                IScene scene = FileUpload.builder().isPermanent(false).pic(picture).ratio(1.5).build();
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
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
                Preconditions.checkArgument(message.equals("卡券数量不能为空") || message.equals("奖励数量不能为空") || message.equals("卡券【测试卡券】库存不足，请重新选择！") || message.equals("奖励数量至少一张"), "分享者优惠券配置异常情况为：" + num[i]);
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
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
                Preconditions.checkArgument(message.equals("卡券数量不能为空") || message.equals("奖励数量不能为空") || message.equals("卡券【测试卡券】库存不足，请重新选择！") || message.equals("奖励数量至少一张"), "被邀请者优惠券配置异常情况为：" + num[i]);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建裂变活动-被邀请者优惠券配置{0，不填写，>库存}");
        }
    }


}