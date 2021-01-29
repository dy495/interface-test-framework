package com.haisheng.framework.testng.bigScreen.jiaochen.gly;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.datastore.A;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.util.BusinessUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.util.PublicParameter;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityApprovalStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.FileUpload;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherDetailScene;
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
 * ===================================活动管理的数据一致性==================================
 */
    /**
     *创建招募活动-数据一致性校验{列表+1&状态=待审核,【活动审批】列表+1&状态=待审核}
     */
    @Test(description = "创建招募活动-数据一致性校验{列表+1&状态=待审核}")
    public void createActivityDate1(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //创建活动之前的列表数量
            IScene scene= ActivityManageListScene.builder().page(1).size(10).build();
            int totalBefore=visitor.invokeApi(scene).getInteger("totoal");
            //创建招募活动
            Long id=businessUtil.createRecruitActivityApproval();
            //创建招募活动之后的列表数量
            int totalAfter=visitor.invokeApi(scene).getInteger("total");
            //创建活动后-状态=待审核
            int status=businessUtil.getActivityStatus(id);
            Preconditions.checkArgument(totalAfter==(totalBefore+1)&&status==ActivityStatusEnum.PENDING.getId(),"创建活动之前的列表数为："+totalBefore+"创建活动之后的列表数为："+totalAfter+"活动的状态为："+status);

        }catch(AssertionError| Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("创建招募活动-数据一致性校验{列表+1&状态=待审核,【活动审批】列表+1&状态=待审核}");
        }
    }

    /**
     *创建招募活动-数据一致性校验{【活动详情】发放数量=创建时的填写数量,【活动详情】面值=创建时填写的数量,【活动详情】剩余库存=创建时填写数量}
     */
    @Test(description = "创建招募活动-{【活动详情】发放数量=创建时的填写数量,【活动详情】面值=创建时填写的数量,【活动详情】剩余库存=创建时填写数量}")
    public void createActivityDate2(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //创建招募活动-活动ID
            Long activityId=businessUtil.createRecruitActivityApproval();
            IScene scene= ActivityManageListScene.builder().page(1).size(10).build();
            JSONArray list=visitor.invokeApi(scene).getJSONArray("list");
            //卡券ID
            Long voucherId=list.getJSONObject(0).getJSONObject("reward_vouchers").getLong("id");
            //优惠券的面值
            String parValue=businessUtil.getPrice(voucherId);
            JSONObject response=businessUtil.getRecruitActivityDetail(activityId);
            //活动详情发放数量
            String num=response.getString("num");
            //活动详情剩余库存
            String leftNum=response.getString("left_num");
            //活动详情中-优惠券面值
            String price=response.getString("price");

            Preconditions.checkArgument(num.equals("10")&&leftNum.equals("10")&&price.equals(parValue),"活动详情中的数值与创建时的数字不一致");

        }catch(AssertionError| Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("创建招募活动-【活动详情】发放数量=创建时的填写数量,【活动详情】面值=创建时填写的数量,【活动详情】剩余库存=创建时填写数量");
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
            Long activityId=businessUtil.createRecruitActivityApproval();

           //活动管理-变更记录
            IScene scene=ManageDetailScene.builder().id(activityId).build();
            JSONArray list=visitor.invokeApi(scene).getJSONArray("list");
            String content=list.getJSONObject(0).getString("content");

            Preconditions.checkArgument(content.equals("新建活动")&&list.size()==1,"调整记录中新增的记录的状态为："+content);

        }catch(AssertionError| Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("创建招募活动-数据一致性校验{【调整记录】+1&调整类型=新建活动}");
        }
    }

    /**
     * 活动管理-编辑招募活动的规则和标题，①内容更新，②【调整记录】+1&调整类型=修改活动
     */
    public void editWorkingActivityDate4(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取进行中的活动ID
            Long id=businessUtil.getActivityWorking();
            //编辑前-变更记录条数
            JSONArray list=businessUtil.changeRecordPage(id).getJSONArray("list");
            int numBefore=list.size();
            //编辑活动名称、活动名额
            IScene scene = ManageRecruitEditScene.builder().title(pp.editTitle).rule(pp.EditRule).build();
            visitor.invokeApi(scene);
            //获取编辑后的标题和活动规则
            JSONObject object=businessUtil.getActivityRespond(id);
            String title=object.getString("title");
            String rule=object.getString("rule");
            Preconditions.checkArgument(title.equals(pp.editTitle)&&rule.equals(pp.EditRule),"编辑后的活动名字为："+title+"  编辑后的活动规则为："+rule);
            //编辑后 变更记录+1&调整类型=修改活动
            int numAfter=list.size();
            String content=list.getJSONObject(0).getString("content");
            Preconditions.checkArgument(numAfter==numBefore+1&&content.equals("修改活动"),"编辑前变更记录的条数为："+numBefore+"编辑后的变更记录的条数为："+numAfter+"  编辑后变更记录新增的内容为："+content);
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData(" 活动管理-编辑招募活动的规则和标题，①内容更新，②【调整记录】+1&调整类型=修改活动");
        }
    }

    /**
     * 活动管理-取消【进行中】的招募活动，①【调整记录】+1&调整类型=取消  ②活动状态=已取消
     */
    @Test(description = "活动管理-取消【进行中】的活动，①【调整记录】+1&调整类型=取消  ②活动状态=已取消")
    public void cancelWorkingActivityDate5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            Long id = businessUtil.getActivityWorking();
            //编辑前-变更记录条数
            JSONArray list=businessUtil.changeRecordPage(id).getJSONArray("list");
            int numBefore=list.size();
            //取消进行中的活动
            String message = businessUtil.getCancelActivity(id);
            //获取此活动的状态
            int status=businessUtil.getActivityStatus(id);
            Preconditions.checkArgument(status==ActivityStatusEnum.CANCELED.getId(),"取消【进行中】的活动,现活动的状态为" + ActivityStatusEnum.CANCELED.getStatusName() );
            //编辑后 变更记录+1&调整类型=取消活动
            int numAfter=list.size();
            String content=list.getJSONObject(0).getString("content");
            Preconditions.checkArgument(numAfter==numBefore+1&&content.equals("取消活动"),"取消前变更记录的条数为："+numBefore+"取消后的变更记录的条数为："+numAfter+"  取消后变更记录新增的内容为："+content);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-取消【进行中】的活动，①【调整记录】+1&调整类型=取消  ②活动状态=已取消");
        }
    }

    /**
     * 活动管理-招募活动详情-①成本合计=发放数量*面值   ②总数量=剩余库存+发放数量&总数量>=剩余库存
     */
    @Test(description = "活动管理-活动详情-①成本合计=发放数量*面值   ②总数量=剩余库存+发放数量&总数量>=剩余库存")
    public void activityDetailDate6(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取进行中的活动招募ID
            Long id=businessUtil.getRecruitActivityWorking();
            //进入此活动的活动详情页
            JSONObject response=businessUtil.getRecruitActivityDetail(id);
            //总成本
            int totalCost=response.getInteger("total_cost");
            //面值
            String price=response.getString("price");
            //发放数量
            int sendNum=response.getInteger("send_num");
            //剩余数量
            int leftNum=response.getInteger("left_num");
            //奖励项总数量
            int num=response.getInteger("num");
            Preconditions.checkArgument(totalCost==Double.parseDouble(price)*num&&num==leftNum+sendNum&&num>=leftNum,"总成本为："+totalCost+"  应等于面值*数量为"+Double.parseDouble(price)*num+"  奖励项总数为："+num+"  发放数量为："+sendNum+"  剩余数量为："+leftNum);

        }catch(AssertionError |Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("活动管理-活动详情-①成本合计=发放数量*面值 + ②总数量=剩余库存+发放数量&总数量>=剩余库存");
        }

    }

    /**
     * 活动报名列---①活动名额>=报名成功人数和   ②报名成功数=【全部列表】状态为审核通过人数和 ③已报名数>=报名成功数
     */
    public void activityRegisterDate7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取【进行中】的招募活动ID
            Long activityId = businessUtil.getRecruitActivityWorking();
            int registerPassedNum=0;
            //报名列表的返回值
            JSONObject pageRes = businessUtil.getRegisterPage(activityId);
            int pages=pageRes.getInteger("pages");
            for (int page=1;page<=pages;page++){
                IScene scene=ManageRegisterPageScene.builder().page(page).size(10).activityId(activityId).build();
                JSONArray list=visitor.invokeApi(scene).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    int status=list.getJSONObject(i).getInteger("status");
                    if(status== ActivityApprovalStatusEnum.PASSED.getId()){
                        registerPassedNum=list.getJSONObject(i).getInteger("register_num");
                        //报名审核通过的人数
                        registerPassedNum+=registerPassedNum;
                    }
                }
            }

            //报名数据的返回值
            JSONObject dataRes = businessUtil.getRegisterData(activityId);
            //活动名额
            int quota=dataRes.getInteger("quota");
            //报名总人数（已报名）
            int total=dataRes.getInteger("total");
            //待审批人数
            int wait=dataRes.getInteger("wait");
            //报名成功的人数
            int passed=dataRes.getInteger("passed");
            //报名失败的人数
            int failed=dataRes.getInteger("failed");

            Preconditions.checkArgument(quota>=total,"活动名额为："+quota+"  报名总人数为："+total);
            Preconditions.checkArgument(passed==registerPassedNum,"报名成功人数为："+passed+"  【全部列表】状态为审核通过人数和："+registerPassedNum);
            Preconditions.checkArgument(total>=passed,"已报名的人数为："+total+"  报名成功的为："+passed);
        } catch (AssertionError|Exception e) {
            appendFailReason(e.toString());
        }finally {
            saveData("活动报名列---①活动名额>=报名成功人数和 ②报名成功数=【全部列表】状态为审核通过人数和  ③已报名数>=报名成功数");
        }
    }

    /**
     * 报名列表-已报名数=【全部】报名人数和=【待审核】【报名列表】【报名失败】人数和
     */
    @Test
    public void activityRegisterDate8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取【进行中】的招募活动ID
            Long activityId = businessUtil.getRecruitActivityWorking();
            int registerNum=0;
            //报名列表的返回值
            JSONObject pageRes = businessUtil.getRegisterPage(activityId);
            int pages=pageRes.getInteger("pages");
            for (int page=1;page<=pages;page++){
                IScene scene=ManageRegisterPageScene.builder().page(page).size(10).activityId(activityId).build();
                JSONArray list=visitor.invokeApi(scene).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    int status=list.getJSONObject(i).getInteger("status");
                    String customerPhone=list.getJSONObject(i).getString("customer_phone");
                    String modify_time=list.getJSONObject(i).getString("modify_time");
                    List<String> phoneArray=businessUtil.phoneSameArrayCheck(activityId);
                    //todo 报名人数（去重）
                    for(int j=0;j<phoneArray.size();j++){
                        if(customerPhone.equals(phoneArray.get(j))){
                            //todo 怎么处理重复数据

                        }else{
                            registerNum=list.getJSONObject(i).getInteger("register_num");
                            registerNum+=registerNum;
                        }
                    }

                }
            }

            //报名数据的返回值
            JSONObject dataRes = businessUtil.getRegisterData(activityId);
            //活动名额
            int quota=dataRes.getInteger("quota");
            //报名总人数
            int total=dataRes.getInteger("total");
            //待审批人数
            int wait=dataRes.getInteger("wait");
            //报名成功的人数
            int passed=dataRes.getInteger("passed");
            //报名失败的人数
            int failed=dataRes.getInteger("failed");

            Preconditions.checkArgument(total==registerNum&&total==(wait+passed+failed),"报名总人数为："+total+"  （待审批+报名成功+报名失败）人数为："+(wait+passed+failed)+"  列表中的报名人数和为："+registerNum);
        } catch (AssertionError|Exception e) {
            appendFailReason(e.toString());
        }finally {
            saveData("报名列表-已报名数=【全部】报名人数和=【待审核】【报名列表】【报名失败】人数和");
        }
    }

    /**
     * 报名列表-审批通过1条，报名成功&报名成功列表
     */
    @Test
    public void activityRegisterDate9(){
        logger.logCaseStart(caseResult.getCaseName());
       try{
           int registerNum=0;
           //获取进行中的活动存在待审批数量的ID
           Long activityId=businessUtil.getRecruitActivityWorkingApproval();
           //审批通过之前报名成功的数量
           int passedBefore=businessUtil.getRegisterData(activityId).getInteger("passed");
           //待审批活动的ID合集
           List<Long> idArray=businessUtil.registerApproval(activityId);
           //审批通过其中一条
           businessUtil.getApprovalPassed(idArray.get(0));
           //审批通过的活动人数
            int pages=businessUtil.getRegisterPage(activityId).getInteger("pages");
            for(int page=1;page<pages;page++){
                IScene scene = ManageRegisterPageScene.builder().page(page).size(10).activityId(activityId).build();
                JSONArray List = visitor.invokeApi(scene).getJSONArray("list");
                for(int i=0;i<List.size();i++){
                    Long idOne=List.getJSONObject(i).getLong("id");
                    int status=List.getJSONObject(i).getInteger("status");
                    if(idArray.get(0).equals(idOne)&&status==ActivityApprovalStatusEnum.PASSED.getId()){
                            registerNum= List.getJSONObject(i).getInteger("register_num");
                            registerNum+=registerNum;
                    }
                }
            }
           //审批通过之后报名成功的数量
           int passedAfter=businessUtil.getRegisterPage(activityId).getInteger("passed");
           Preconditions.checkArgument(idArray.size()>0&&passedAfter==(passedBefore+registerNum),"审批的通过的人数为:"+registerNum);

       }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
       }finally{
            saveData("报名列表-审批通过1条，报名成功&报名成功列表");
       }
    }

    /**
     * 报名列表-审批不通过1条，报名失败列表
     */
    @Test
    public void activityRegisterDate10(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            int registerNum=0;
            //获取进行中的活动存在待审批数量的ID
            Long activityId=businessUtil.getRecruitActivityWorkingApproval();
            //审批通过之前报名成功的数量
            int failedBefore=businessUtil.getRegisterData(activityId).getInteger("failed");
            //待审批活动的ID合集
            List<Long> idArray=businessUtil.registerApproval(activityId);
            //审批通过其中一条
            businessUtil.getApprovalPassed(idArray.get(0));
            //审批通过的活动人数
            int pages=businessUtil.getRegisterPage(activityId).getInteger("pages");
            for(int page=1;page<pages;page++){
                IScene scene = ManageRegisterPageScene.builder().page(page).size(10).activityId(activityId).build();
                JSONArray List = visitor.invokeApi(scene).getJSONArray("list");
                for(int i=0;i<List.size();i++){
                    //报名ID
                    Long idOne=List.getJSONObject(i).getLong("id");
                    int status=List.getJSONObject(i).getInteger("status");
                    if(idArray.get(0).equals(idOne)&&status==ActivityApprovalStatusEnum.REJECT.getId()){
                        registerNum= List.getJSONObject(i).getInteger("register_num");
                        registerNum+=registerNum;
                    }
                }
            }
            //审批通过之后报名成功的数量
            int failedAfter=businessUtil.getRegisterPage(activityId).getInteger("failed");
            Preconditions.checkArgument(idArray.size()>0&&failedAfter==(failedBefore+registerNum),"审批不通过的人数为:"+registerNum);

        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("报名列表-审批不通过1条，报名失败列表");
        }
    }
    /**
     * 活动报名列---【活动名额】=创建活动时填写数量--活动名额有限
     */
    public void activityRegisterDate11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取【进行中】的招募活动ID
            Long activityId = businessUtil.getRecruitActivityWorking();
            //活动详情中【活动名额】
            IScene scene=ManageDetailScene.builder().id(activityId).build();
            JSONObject response=visitor.invokeApi(scene).getJSONObject("recruit_activity_info");
            Boolean isLimitQuota=response.getBoolean("is_limit_quota");
            if (isLimitQuota.equals(true)){
                int num=response.getInteger("quota");
                //报名数据的返回值
                JSONObject dataRes = businessUtil.getRegisterData(activityId);
                //活动名额
                int quota=dataRes.getInteger("quota");
                Preconditions.checkArgument(quota>=num,"活动名额为："+quota+"  创建活动时填写数量："+num);
            }
        } catch (AssertionError|Exception e) {
            appendFailReason(e.toString());
        }finally {
            saveData("活动报名列---【活动名额】=创建活动时填写数量");
        }
    }

    /**
     * 活动审批--①全部审批=【全部】列表数  ②全部审批=【待审核】【审核通过】【审核未通过】列表数加和
     */
    public void activityApproval12(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取活动审批的数据
            JSONObject response=businessUtil.getActivityApprovalDate();
            //全部活动
            int total= response.getInteger("total");
            //待审批活动
            int wait=response.getInteger("wait");
            //通过的活动
            int passed=response.getInteger("passed");
            //未通过活动
            int failed=response.getInteger("failed");

            //获取【全部tab】的列表数
            IScene scene = ActivityManageListScene.builder().page(1).size(10).build();
            JSONObject response1 = visitor.invokeApi(scene);
            int totalNum=response1.getInteger("total");
            //获取【待审核tab】的列表数
            int waitNum=businessUtil.getActivityManagePage(ActivityApprovalStatusEnum.PENDING.getId()).getInteger("total");
            //获取【审核通过tab】的列表数
            int passedNum=businessUtil.getActivityManagePage(ActivityApprovalStatusEnum.PASSED.getId()).getInteger("total");
            //获取【审核未通过tab】的列表数
            int failedNum=businessUtil.getActivityManagePage(ActivityApprovalStatusEnum.REJECT.getId()).getInteger("total");
            Preconditions.checkArgument(total==totalNum,"活动审批数据-全部活动为："+total+"审批列表中全部活动的列表数为："+totalNum);
            Preconditions.checkArgument(total==(waitNum+passedNum+failedNum),"活动审批数据-全部活动为："+total+"【待审核】【审核通过】【审核未通过】列表数加和为："+(waitNum+passedNum+failedNum));
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("活动审批--①全部审批=【全部】列表数  ②全部审批=【待审核】【审核通过】【审核未通过】列表数加和");
        }
    }
    /**
     * 活动审批--审批通过，待审批-1&审批通过+1
     */
    public void activityApproval13(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取活动审批的数据
            JSONObject response=businessUtil.getActivityApprovalDate();
            //待审批活动
            int waitBefore=response.getInteger("wait");
            //通过的活动
            int passedBefore=response.getInteger("passed");
            //获取进行中的活动存在待审批数量的ID
            Long activityId=businessUtil.getRecruitActivityWorkingApproval();
            //待审批活动的ID合集
            List<Long> idArray=businessUtil.registerApproval(activityId);
            //审批通过其中一条
            businessUtil.getApprovalPassed(idArray.get(0));
            //获取审批后活动审批的数据
            JSONObject response1=businessUtil.getActivityApprovalDate();
            //待审批活动
            int waitAfter=response1.getInteger("wait");
            //通过的活动
            int passedAfter=response1.getInteger("passed");

            Preconditions.checkArgument(waitAfter==(waitBefore-1),"活动审批数后的待审批数量："+waitAfter+"活动审批数前的待审批数量："+waitBefore );
            Preconditions.checkArgument(passedAfter==(passedBefore+1),"活动审批数后的审批通过数量："+passedAfter+"活动审批数前的审批通过数量："+passedBefore );
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("活动审批--①全部审批=【全部】列表数  ②全部审批=【待审核】【审核通过】【审核未通过】列表数加和");
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
            Long id=businessUtil.getActivityWaitingApproval();
            List<Long> idArray=new ArrayList<>();
            idArray.add(id);
            //通过待审批的活动
            IScene scene2= ManageApprovalScene.builder().status(101).ids(idArray).build();
            visitor.invokeApi(scene2);
            //获取刚才通过的活动的状态
            int status=businessUtil.getActivityStatus(id);
            Preconditions.checkArgument(status== ActivityStatusEnum.PASSED.getId(),"审批通过后活动的状态为："+status);

        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("活动管理-活动审批通过");
        }
    }

    /**
     * 活动管理-撤回【待审批】的活动
     */
    @Test(description = "活动管理-【待审批】的活动撤回")
    public void revokeApprovalsActivity(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取待审批活动的ID
            Long id=businessUtil.getActivityReject();
            //撤回待审批的活动
            String message=businessUtil.getRevokeActivity(id);
            //获取刚才通过的活动的状态
            int status=businessUtil.getActivityStatus(id);
            Preconditions.checkArgument(status==ActivityStatusEnum.REVOKE.getId(),"撤回的待审批的活动,现活动的状态为："+ActivityStatusEnum.REVOKE.getStatusName());
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("活动管理-撤回【待审批】的活动");
        }
    }

    /**
     * 活动管理-审核不通过【待审批】的活动
     */
    @Test(description = "活动管理-【待审批】的活动撤回")
    public void approvalsActivityFail(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取待审批活动的ID
            Long id=businessUtil.getActivityWait();
            //活动审批不通过
            businessUtil.getApprovalReject(id);
            //获取此活动的状态
            int status=businessUtil.getActivityStatus(id);
            Preconditions.checkArgument(status==ActivityStatusEnum.REJECT.getId(),"撤回的待审批的活动,现活动的状态为："+ActivityStatusEnum.REJECT.getStatusName());
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("活动管理-审核不通过【待审批】的活动");
        }
    }



    /**
     * 活动管理-删除【进行中】的活动
     */
    @Test(description = "活动管理-删除【进行中】的活动")
    public void delWorkingActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            Long id = businessUtil.getActivityWorking();
            //删除进行中的活动
            String message = businessUtil.getDelActivity(id);
            Preconditions.checkArgument(message.equals("成功"), "删除【进行中】的活动的message为：" + message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-删除【进行中】的活动");
        }
    }




    /**
     * 活动管理-推广【进行中】的活动
     */
    @Test(description = "活动管理-推广【进行中】的活动")
    public void promotionWokingActivity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取进行中活动的ID
            Long id = businessUtil.getActivityWorking();
            //推广进行中的活动
            String appletCodeUrl = businessUtil.getPromotionActivity(id);
            Preconditions.checkArgument(!appletCodeUrl.equals(""), "推广【进行中】的活动的小程序二维码的返回值为空");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("活动管理-推广【进行中】的活动");
        }
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
            JSONObject invitedVoucher=businessUtil.getInvitedVoucher(voucherId,1,"1",2,"","",3);
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
            JSONObject invitedVoucher=businessUtil.getInvitedVoucher(voucherId,1,"1",2,"","",3);
            JSONObject shareVoucher=businessUtil.getShareVoucher(pp.packageId,2,"1",2,"","",3);
            String[] title=pp.titleException;
            for(int i=0;i<=title.length;i++){
                IScene scene= FissionVoucherAddScene.builder()
                        .type(0)
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
            JSONObject invitedVoucher=businessUtil.getInvitedVoucher(voucherId,1,"1",2,"","",3);
            JSONObject shareVoucher=businessUtil.getShareVoucher(pp.packageId,2,"1",2,"","",3);
            String[] shareNum=pp.shareNumException;
            for(int i=0;i<=shareNum.length;i++){
                IScene scene= FissionVoucherAddScene.builder()
                        .type(0)
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
            JSONObject invitedVoucher=businessUtil.getInvitedVoucher(voucherId,1,"1",2,"","",3);
            JSONObject shareVoucher=businessUtil.getShareVoucher(pp.packageId,2,"1",2,"","",3);
            String[] rule=pp.ruleException;
            for(int i=0;i<=rule.length;i++){
                IScene scene= FissionVoucherAddScene.builder()
                        .type(0)
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
            JSONObject invitedVoucher=businessUtil.getInvitedVoucher(voucherId,1,"1",2,"","",3);
            JSONObject shareVoucher=businessUtil.getShareVoucher(pp.packageId,2,"1",2,"","",3);
            IScene scene= FissionVoucherAddScene.builder()
                    .type(0)
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
                JSONObject invitedVoucher=businessUtil.getInvitedVoucher(voucherId,1,num[i],2,"","",3);
                JSONObject shareVoucher=businessUtil.getShareVoucher(pp.packageId,2,"1",2,"","",3);
                IScene scene= FissionVoucherAddScene.builder()
                        .type(0)
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
                JSONObject invitedVoucher=businessUtil.getInvitedVoucher(voucherId,1,"1",2,"","",3);
                JSONObject shareVoucher=businessUtil.getShareVoucher(voucherId,1,num[i],2,"","",3);
                IScene scene= FissionVoucherAddScene.builder()
                        .type(0)
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
                Preconditions.checkArgument(message.equals(""),"被邀请者优惠券配置异常情况为："+num[i]);
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("创建裂变活动-被邀请者优惠券配置{0，不填写，>库存}");
        }
    }

















}