package com.haisheng.framework.testng.bigScreen.jiaochen.gly;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.util.BusinessUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.util.PublicParameter;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ActivityManageListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.FissionVoucherAddScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ManageApprovalScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ManageDetailScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.FileUpload;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherDetailScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.voucher.VoucherGenerator;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.lang.reflect.Method;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.Arrays;
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
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //添加活动图片
            List<String> picList=new ArrayList<>();
            picList.add(supporterUtil.getPicPath());
            //获取优惠券ID
            Long voucherId= new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher=businessUtil.getinvitedVoucher(voucherId,1,"1",2,"","",3);
            JSONObject shareVoucher=businessUtil.getShareVoucher(pp.packageId,2,"1",2,"","",3);
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
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //添加活动图片
            List<String> picList=new ArrayList<>();
            picList.add(supporterUtil.getPicPath());
            //获取优惠券ID
            Long voucherId= new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher=businessUtil.getinvitedVoucher(voucherId,1,"1",2,"","",3);
            JSONObject shareVoucher=businessUtil.getShareVoucher(pp.packageId,2,"1",2,"","",3);
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
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //添加活动图片
            List<String> picList=new ArrayList<>();
            picList.add(supporterUtil.getPicPath());
            //获取优惠券ID
            Long voucherId= new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher=businessUtil.getinvitedVoucher(voucherId,1,"1",2,"","",3);
            JSONObject shareVoucher=businessUtil.getShareVoucher(pp.packageId,2,"1",2,"","",3);
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
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //添加活动图片
            List<String> picList=new ArrayList<>();
            picList.add(supporterUtil.getPicPath());
            //获取优惠券ID
            Long voucherId= new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher=businessUtil.getinvitedVoucher(voucherId,1,"1",2,"","",3);
            JSONObject shareVoucher=businessUtil.getShareVoucher(pp.packageId,2,"1",2,"","",3);
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
     *创建活动-活动图片异常{不填写}
     */
    @Test(description = "创建活动-活动图片异常{不填写}")
    public void fissionVoucherTitleException5(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //添加活动图片
            List<String> picList=new ArrayList<>();
            picList.add("");
            //获取优惠券ID
            Long voucherId= new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            // 创建被邀请者和分享者的信息字段
            JSONObject invitedVoucher=businessUtil.getinvitedVoucher(voucherId,1,"1",2,"","",3);
            JSONObject shareVoucher=businessUtil.getShareVoucher(pp.packageId,2,"1",2,"","",3);
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
            saveData("创建活动-活动图片异常{不填写}");
        }
    }

    /**
     * 创建活动-活动图片异常{>500K,.gif}}
     */
    @Test
    public void fissionVoucherTitleException6(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] paths={"src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/大于500k.jpg",
            "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/动图.gif"};
            Arrays.stream(paths).forEach(path->{
                String picture = new ImageUtil().getImageBinary(path);
                IScene scene = FileUpload.builder().isPermanent(false).pic(picture).ratio(1.5).build();
                String message=   visitor.invokeApi(scene,false).getString("message");
                Preconditions.checkArgument(message.equals(""),"活动图片的异常情况为："+path);
                String err="";
                CommonUtil.checkResult("图片异常",err,message);
            });
        }catch (Exception|AssertionError e){
           appendFailReason(e.toString());
        }finally{
            saveData("创建活动-活动图片异常{>500K,.gif}");
        }
    }

    /**
     *创建裂变活动-分享者优惠券配置{0，不填写，>库存}
     */
    @Test(description = "创建裂变活动-分享者优惠券配置{0，不填写，>库存}")
    public void fissionVoucherTitleException7(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //添加活动图片
            List<String> picList=new ArrayList<>();
            picList.add(businessUtil.getPicPath());
            //获取优惠券ID
            Long voucherId= new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            //获取优惠券库存
            String surplus= businessUtil.getSurplusInventory(voucherId);
            int surpass= Integer.parseInt(surplus)+1;
            String[] num={"0","", String.valueOf(surpass)};
            for(int i=0;i<num.length;i++){
                // 创建被邀请者和分享者的信息字段
                JSONObject invitedVoucher=businessUtil.getinvitedVoucher(voucherId,1,num[i],2,"","",3);
                JSONObject shareVoucher=businessUtil.getShareVoucher(pp.packageId,2,"1",2,"","",3);
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
                    Preconditions.checkArgument(message.equals(""),"分享者优惠券配置异常情况为："+num[i]);
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("创建裂变活动-分享者优惠券配置{0，不填写，>库存}");
        }
    }

    /**
     *创建裂变活动-被邀请者优惠券配置{0，不填写，>库存}
     */
    @Test(description = "创建裂变活动-被邀请者优惠券配置{0，不填写，>库存}")
    public void fissionVoucherTitleException8(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //添加活动图片
            List<String> picList=new ArrayList<>();
            picList.add(businessUtil.getPicPath());
            //获取优惠券ID
            Long voucherId= new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            //获取优惠券库存
            String surplus= businessUtil.getSurplusInventory(voucherId);
            String[] num={"0","",surplus};
            // 创建被邀请者和分享者的信息字段
            for(int i=0;i<num.length;i++){
                JSONObject invitedVoucher=businessUtil.getinvitedVoucher(voucherId,1,"1",2,"","",3);
                JSONObject shareVoucher=businessUtil.getShareVoucher(voucherId,1,num[i],2,"","",3);
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
                Preconditions.checkArgument(message.equals(""),"被邀请者优惠券配置异常情况为："+num[i]);
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("创建裂变活动-被邀请者优惠券配置{0，不填写，>库存}");
        }
    }

/**
 * ===================================活动管理的主流程==================================
 */
    /**
     *创建招募活动-数据一致性校验{列表+1&状态=待审核,【活动审批】列表+1&状态=待审核}
     */
    @Test(description = "创建招募活动-数据一致性校验{列表+1&状态=待审核}")
    public void createActivityDate1(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取优惠券ID


            //创建活动之前的列表数量
            IScene scene= ActivityManageListScene.builder().page(1).size(10).build();
            String totalBefore=visitor.invokeApi(scene).getString("totoal");
            //创建招募活动
            businessUtil.createRecruitActivity();
            //创建招募活动之后的列表数量
            String totalAfter=visitor.invokeApi(scene).getString("total");

            Preconditions.checkArgument(totalAfter==totalBefore+1,"创建活动之前的列表数为："+totalBefore+"创建活动之后的列表数为："+totalAfter);

        }catch(AssertionError| Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("创建招募活动的数据一致性校验");
        }
    }

    /**
     *创建招募活动-数据一致性校验{【活动详情】发放数量=创建时的填写数量,【活动详情】面值=创建时填写的数量,【活动详情】剩余库存=创建时填写数量}
     */
    @Test(description = "创建招募活动-数据一致性校验{【活动详情】发放数量=创建时的填写数量,【活动详情】面值=创建时填写的数量,【活动详情】剩余库存=创建时填写数量}")
    public void createActivityDate2(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //创建招募活动-活动ID
            Long activityId=businessUtil.createRecruitActivity();
            IScene scene= ActivityManageListScene.builder().page(1).size(10).build();
            JSONArray list=visitor.invokeApi(scene).getJSONArray("list");
            //卡券ID
            Long voucherId=list.getJSONObject(0).getJSONObject("reward_vouchers").getLong("id");
            //优惠券的面值
            String parValue=businessUtil.getPrice(voucherId);
            IScene scene1= ManageDetailScene.builder().id(activityId).build();
            //活动详情发放数量
            String num=visitor.invokeApi(scene1).getJSONObject("reward_vouchers").getString("num");
            //活动详情剩余库存
            String leftNum=visitor.invokeApi(scene1).getJSONObject("reward_vouchers").getString("left_num");
            //活动详情中-优惠券面值
            String price=visitor.invokeApi(scene1).getJSONObject("reward_vouchers").getString("price");

            Preconditions.checkArgument(num.equals("10")&&leftNum.equals("10")&&price.equals(parValue),"活动详情中的数值与创建时的数字不一致");

        }catch(AssertionError| Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("创建招募活动的数据一致性校验");
        }
    }

    /**
     *创建招募活动-数据一致性校验{【调整记录】+1&调整类型=新建活动}
     */
    @Test(description = "创建招募活动-数据一致性校验{【调整记录】+1&调整类型=新建活动}")
    public void createActivityDate3(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //创建招募活动-活动ID
            Long activityId=businessUtil.createRecruitActivity();
            IScene scene= ActivityManageListScene.builder().page(1).size(10).build();
            JSONArray list=visitor.invokeApi(scene).getJSONArray("list");

           //活动管理-变更记录
            IScene scene1= VoucherDetailScene.builder().id(activityId).build();
            JSONArray list1=visitor.invokeApi(scene1).getJSONArray("list");
            String content=list1.getJSONObject(0).getString("content");

            Preconditions.checkArgument(content.equals("新建活动")&&list1.size()==1,"调整记录中新增的记录的状态为："+content);

        }catch(AssertionError| Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("创建招募活动-数据一致性校验{【调整记录】+1&调整类型=新建活动}");
        }
    }

    /**
     *活动管理-活动审批通过
     */
    @Test(description = "活动管理-活动审批通过")
    public void activityApproval(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取列表中状态为【待审批】的活动ID
            Long id=businessUtil.getActivityApproval();
            List<Long> idArray=new ArrayList<>();
            idArray.add(id);
            //通过待审批的活动
            IScene scene2= ManageApprovalScene.builder().status(101).ids(idArray).build();
            visitor.invokeApi(scene2);
            //获取刚才通过的活动的状态
            int status=businessUtil.getActivityStatus(id);
            Preconditions.checkArgument(status==101,"审批通过后活动的状态为："+status);

        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("活动管理-活动审批通过");
        }
    }

    /**
     * 活动管理-【待审批】的活动删除
     */
    @Test()
    public void delActivity1(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取待审批活动的ID
            Long id=businessUtil.getActivityApproval();
            //删除待审批的活动
            String message=businessUtil.getDelActivity(id);
            Preconditions.checkArgument(message.equals("成功"),"删除的待审批的活动的message为："+message);
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("活动管理-【待审批】的活动删除");
        }

    }

    /**
     * 活动管理-删除【进行中】的活动
     */
    @Test(description = "活动管理-删除【进行中】的活动")
    public void delActivity2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待审批活动的ID
            Long id = businessUtil.getActivityApproval();
            //删除待审批的活动
            String message = businessUtil.getDelActivity(id);
            Preconditions.checkArgument(message.equals("成功"), "删除的待审批的活动的message为：" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-删除【进行中】的活动");
        }
    }














}