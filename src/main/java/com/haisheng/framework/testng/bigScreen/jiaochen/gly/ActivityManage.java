package com.haisheng.framework.testng.bigScreen.jiaochen.gly;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.util.BusinessUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.util.PublicParameter;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.FissionVoucherAddScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.voucher.VoucherGenerator;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ActivityManage extends TestCaseCommon implements TestCaseStd {
    ScenarioUtil jc = new ScenarioUtil();
    private static final EnumTestProduce product = EnumTestProduce.JIAOCHEN_DAILY;
    public Visitor visitor = new Visitor(product);
    BusinessUtil businessUtil=new BusinessUtil(visitor);
    SupporterUtil supporterUtil=new SupporterUtil(visitor);
    PublicParameter pp=new PublicParameter();

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
        commonConfig.referer=product.getReferer();
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
        jc.pcLogin(pp.phone, pp.password);
    }

    /**
     * ==========================创建活动的异常情况===========================
     */

    /**
     *创建活动-领取次数的异常情况{中文、英文、标点符号、长度101}
     */
    @Test(description = "创建活动-领取次数的异常情况{中文、英文、标点符号、长度101}")
    public void fissionVoucherReceiveLimitException1(){
        try{
            //添加活动图片
            List<String> picList=new ArrayList<>();
            picList.add(supporterUtil.getPicPath());
            //获取优惠券ID
            Long voucherId= new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher=businessUtil.getinvitedVoucher(voucherId,1,1,2,"","",3);
            JSONObject shareVoucher=businessUtil.getShareVoucher(pp.packageId,2,1,2,"","",3);
            String[] receiveLimit=pp.receiveLimitException;
            for(int num=1;num<=2;num++){
                for(int i=0;i<=receiveLimit.length;i++){
                    IScene scene= FissionVoucherAddScene.builder()
                            .type(0)
                            .participationLimitType(0)
                            .receiveLimitType(num)
                            .receiveLimitTimes(receiveLimit[i])
                            .title(pp.fissionVoucherName)
                            .rule(pp.rule)
                            .startDate(businessUtil.nowTimeFormat())
                            .endDate(businessUtil.futureTimeFormat())
                            .subjectType(supporterUtil.getSubjectType())
                            .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                            .label("RED_PAPER")
                            .picList(picList)
                            .shareNum("3")
                            .shareVoucher(shareVoucher)
                            .invitedVoucher(invitedVoucher)
                            .build();
                    String message = visitor.invokeApi(scene, false).getString("message");
                    Preconditions.checkArgument(message.equals(""),"获取期间总次数(1)/每天领取总次数(2)错误的类型为："+num+"错误的结果为: "+receiveLimit[i]);

                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("创建活动-领取次数的异常情况{中文、英文、标点符号、长度101}");
        }
    }
    /**
     *创建活动-  标题的异常情况{不填写、21位}
     */
    @Test(description = "创建活动-  标题的异常情况{不填写、21位}")
    public void fissionVoucherTitleException2(){
        try{
            //添加活动图片
            List<String> picList=new ArrayList<>();
            picList.add(supporterUtil.getPicPath());
            //获取优惠券ID
            Long voucherId= new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher=businessUtil.getinvitedVoucher(voucherId,1,1,2,"","",3);
            JSONObject shareVoucher=businessUtil.getShareVoucher(pp.packageId,2,1,2,"","",3);
            String[] title=pp.titleException;
            for(int i=0;i<=title.length;i++){
                IScene scene= FissionVoucherAddScene.builder()
                        .type(0)
                        .participationLimitType(0)
                        .receiveLimitType(0)
                        .title(title[i])
                        .rule(pp.rule)
                        .startDate(businessUtil.nowTimeFormat())
                        .endDate(businessUtil.futureTimeFormat())
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label("RED_PAPER")
                        .picList(picList)
                        .shareNum("3")
                        .shareVoucher(shareVoucher)
                        .invitedVoucher(invitedVoucher)
                        .build();
                String message = visitor.invokeApi(scene, false).getString("message");
                Preconditions.checkArgument(message.equals(""),"创建活动填写的名字为："+title[i]);

            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("创建活动-标题异常情况");
        }
    }

    /**
     *创建活动-分享人数的异常情况{不填写、10000、中文、英文、标点符号}
     */
    @Test(description = "创建活动-分享人数的异常情况{不填写、10000、中文、英文、标点符号}")
    public void fissionVoucherTitleException3(){
        try{
            //添加活动图片
            List<String> picList=new ArrayList<>();
            picList.add(supporterUtil.getPicPath());
            //获取优惠券ID
            Long voucherId= new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher=businessUtil.getinvitedVoucher(voucherId,1,1,2,"","",3);
            JSONObject shareVoucher=businessUtil.getShareVoucher(pp.packageId,2,1,2,"","",3);
            String[] shareNum=pp.shareNumException;
            for(int i=0;i<=shareNum.length;i++){
                IScene scene= FissionVoucherAddScene.builder()
                        .type(0)
                        .participationLimitType(0)
                        .receiveLimitType(0)
                        .title(pp.fissionVoucherName)
                        .rule(pp.rule)
                        .startDate(businessUtil.nowTimeFormat())
                        .endDate(businessUtil.futureTimeFormat())
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label("RED_PAPER")
                        .picList(picList)
                        .shareNum(shareNum[i])
                        .shareVoucher(shareVoucher)
                        .invitedVoucher(invitedVoucher)
                        .build();
                String message = visitor.invokeApi(scene, false).getString("message");
                Preconditions.checkArgument(message.equals(""),"创建活动分享人数为："+shareNum[i]);

            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("创建活动-分享人数的异常情况{不填写、10000、中文、英文、标点符号}");
        }
    }

    /**
     *创建活动-活动规则异常{不填写，2001位}
     */
    @Test(description = "创建活动-活动规则异常{不填写，2001位}")
    public void fissionVoucherTitleException4(){
        try{
            //添加活动图片
            List<String> picList=new ArrayList<>();
            picList.add(supporterUtil.getPicPath());
            //获取优惠券ID
            Long voucherId= new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher=businessUtil.getinvitedVoucher(voucherId,1,1,2,"","",3);
            JSONObject shareVoucher=businessUtil.getShareVoucher(pp.packageId,2,1,2,"","",3);
            String[] rule=pp.ruleException;
            for(int i=0;i<=rule.length;i++){
                IScene scene= FissionVoucherAddScene.builder()
                        .type(0)
                        .participationLimitType(0)
                        .receiveLimitType(0)
                        .title(pp.fissionVoucherName)
                        .rule(rule[i])
                        .startDate(businessUtil.nowTimeFormat())
                        .endDate(businessUtil.futureTimeFormat())
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label("RED_PAPER")
                        .picList(picList)
                        .shareNum("3")
                        .shareVoucher(shareVoucher)
                        .invitedVoucher(invitedVoucher)
                        .build();
                String message = visitor.invokeApi(scene, false).getString("message");
                Preconditions.checkArgument(message.equals(""),"创建活动规则为："+rule[i]);

            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("创建活动-活动规则异常{不填写，2001位}");
        }
    }
    /**
     *创建活动-活动图片异常{不填写,<500K,.gif,5：3，1：1}
     */
    @Test(description = "创建活动-活动规则异常{不填写，2001位}")
    public void fissionVoucherTitleException5(){
        try{
            //添加活动图片
            List<String> picList=new ArrayList<>();
            //获取优惠券ID
            Long voucherId= new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher=businessUtil.getinvitedVoucher(voucherId,1,1,2,"","",3);
            JSONObject shareVoucher=businessUtil.getShareVoucher(pp.packageId,2,1,2,"","",3);
                IScene scene= FissionVoucherAddScene.builder()
                        .type(0)
                        .participationLimitType(0)
                        .receiveLimitType(0)
                        .title(pp.fissionVoucherName)
                        .rule(pp.rule)
                        .startDate(businessUtil.nowTimeFormat())
                        .endDate(businessUtil.futureTimeFormat())
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label("RED_PAPER")
                        .picList(picList)
                        .shareNum("3")
                        .shareVoucher(shareVoucher)
                        .invitedVoucher(invitedVoucher)
                        .build();
                String message = visitor.invokeApi(scene, false).getString("message");
                Preconditions.checkArgument(message.equals(""),"创建活动图片为：");
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("创建活动-活动图片异常{不填写,<500K,.gif,5：3，1：1}");
        }
    }







}
