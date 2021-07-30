package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistAppId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistConfId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.AppFlowUp.AppFlowUpPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.AppFlowUp.AppFlowUpRemarkScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.*;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesRecpEvaluateOpt;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesRecpEvaluateSubmit;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.YunTongInfo;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ReceivingSystemCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.YT_DAILY_SSO; // 管理页—-首页
    private static final EnumAccount YT_RECEPTION_DAILY = EnumAccount.YT_RECEPTION_DAILY_M; // 全部权限账号 【运通】
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);   // 产品类放到代理类中（通过代理类发请求）
    public SceneUtil util = new SceneUtil(visitor);
    public YunTongInfo info = new YunTongInfo();
    CommonConfig commonConfig = new CommonConfig();    // 配置类初始化

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = "孟辰";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.product = PRODUCE.getAbbreviation(); // 产品代号 -- YT
        commonConfig.referer = PRODUCE.getReferer();
        commonConfig.shopId = YT_RECEPTION_DAILY.getReceptionShopId();  //请求头放入shopId
        commonConfig.roleId = YT_RECEPTION_DAILY.getRoleId(); //请求头放入roleId
        beforeClassInit(commonConfig);  // 配置请求头
        util.loginPc(YT_RECEPTION_DAILY);   //登录
//        LoginPc loginScene = LoginPc.builder().phone("13402050043").verificationCode("000000").build();
//        httpPost(loginScene.getPath(),loginScene.getBody(),PRODUCE.getPort());
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
    // 随机n位数字
    private String numRandom(Integer n) {
        Random ran = new Random();
        String numStr = "";
        for (int i = 0; i < n; i++) {
            String num = ran.nextInt(9) + "";
            numStr += num;
        }
        return numStr;
    }

    @Test(dataProvider = "errorInfo")
    public void test01createCustomer_system_err(String description,String point,String content,String expect) {
        visitor.setProduct(EnumTestProduce.YT_DAILY_CAR);
        try {
            IScene scene = AppPreSalesReceptionCreateScene.builder().customerName("正常名字").customerPhone("18" + numRandom(9)).sexId("1").intentionCarModelId(util.mcCarId()).estimateBuyCarTime("2035-12-20").build().modify(point, content);
            String code = scene.invoke(visitor,false).getString("code");
            Preconditions.checkArgument(Objects.equals(expect,code),description+",预期结果code="+expect+"，实际结果code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("手动创建接待,所有异常情况");
        }

    }
    @DataProvider(name = "errorInfo")
    public Object[] info(){
        String phone = "15"+numRandom(9);
        return new Object[][]{
                //{"校验姓名：必填","customer_name",null,"1001"},  //X   "message":"系统繁忙，请稍后再试！！"
                //{"校验姓名：长度51字","customer_name",info.stringfifty1,"1001"},//X    "message":"系统繁忙，请稍后再试！！"
                //{"校验姓名：空字符","customer_name","","1001"},  //X   success
                //{"校验姓名：一个空格","customer_name"," ","1001"}, //X   success
                //{"校验联系方式：必填","customer_phone",null,"1001"},  //X   success
               // {"校验联系方式：长度10","customer_phone","15"+numRandom(8),"1001"}, //X   success
                //{"校验联系方式：长度12","customer_phone","15"+numRandom(10),"1001"}, //X   success
                //{"校验联系方式：11位非手机号","customer_phone","00"+numRandom(10),"1001"}, //X   success
                //{"校验联系方式：11位中文","customer_phone","阿坝县的风格解开了破了","1001"}, //X   success
                //{"校验联系方式：11位英文","customer_phone","AbCdEfGhiJk","1001"}, //X   success
                //{"校验联系方式：11位符号","customer_phone",")(_{[';@$% `","1001"}, //X   success
                {"成功创建,手机号固定,前置条件","customer_phone",phone,"1000"},
                {"校验联系方式：同一个手机号接待中再创建接待","customer_phone",phone,"1001"}, //"当前客户正在接待中，请勿重复接待"
                //{"校验性别：必填","sex_id",null,"1001"},  // "message":"系统繁忙，请稍后再试！！"
                {"校验性别：格式","sex_id","3","1001"}, //"性别不正确"
                //{"校验购车车型：必填","intention_car_model_id",null,"1001"},  //X   success
                {"校验购车车型：不存在的车型","intention_car_model_id","1234","1001"}, // "车型不存在"
                //{"校验购车车型：权限外","intention_car_model_id","716","1001"}, //X   success
                //{"校验购车时间：必填","estimate_buy_car_time",null,"1001"},  //X   success
                {"校验购车时间：早于今天","estimate_buy_car_time","2021-01-01","1001"}, //"预计购车时间不能小于今天"
        };
    }


    @Test
    public void test02samePhone(){
        try{
            String phone = "15"+numRandom(9);
            Map<String, String> first = util.createCustomerCommon("一次接待", "1", phone, util.mcCarId(), "2066-12-21");
            AppFinishReceptionScene.builder().id(first.get("id")).shopId(first.get("shopId")).build().invoke(visitor);
            Map<String, String> second = util.createCustomerCommon("二次接待", "1", phone, util.mcCarId(), "2066-12-21");
            AppFinishReceptionScene.builder().id(second.get("id")).shopId(second.get("shopId")).build().invoke(visitor);
            String message = AppPreSalesReceptionCreateScene.builder().customerName("三次接待").customerPhone(phone).sexId("1").intentionCarModelId(util.mcCarId()).estimateBuyCarTime("2035-12-20").build().invoke(visitor, false).getString("message");
            //Boolean isFinish = PreSalesReceptionPageScene.builder().build().invoke(visitor, true).getJSONArray("list").getJSONObject(0).getBoolean("is_finish");
            Preconditions.checkArgument(Objects.equals("该客户当天已接待2次！不能再进行接待！",message), "同一个手机号当天接待三次,message:"+message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("手机号接待次数：同一个手机号当天接待最多两次");
        }
    }

    @Test(dataProvider = "remarkContent")
    public void flowUpContent(String description, String expect, String remark){
        try{
            Map<String, String> customer = util.createCustomerCommon("自动创建差评跟进", "1", "150" + CommonUtil.getRandom(8), util.mcCarId(), "2033-12-20");
            AppFinishReceptionScene.builder().id(customer.get("id")).shopId(customer.get("shopId")).build().invoke(visitor);
            commonConfig.shopId = null;
            commonConfig.roleId = null;
            JSONArray evaluateInfoList = new JSONArray();
            PreSalesRecpEvaluateOpt.builder().reception_id(Long.parseLong(customer.get("id"))).build().invoke(visitor, true).getJSONArray("list").stream().map(j -> (JSONObject) j).map(json -> json.getInteger("id")).forEach(e-> evaluateInfoList.add(lowEvaluate(e)));
            String message = PreSalesRecpEvaluateSubmit.builder().evaluate_info_list(evaluateInfoList).reception_id(Long.parseLong(customer.get("id"))).build().invoke(visitor, false).getString("message");
            commonConfig.shopId = YT_RECEPTION_DAILY.getReceptionShopId();
            commonConfig.roleId = YT_RECEPTION_DAILY.getRoleId();
            if (Objects.equals(message,"success")){
                Integer flowUpId = AppFlowUpPageScene.builder().size(10).build().invoke(visitor, true).getJSONArray("list").getJSONObject(0).getInteger("id");
                String code = AppFlowUpRemarkScene.builder().followId(flowUpId).remark(remark).build().invoke(visitor, false).getString("code");
                Preconditions.checkArgument(Objects.equals(expect,code),description+",预期结果code="+expect+",实际结果code="+code);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售接待差评,跟进内容校验");
        }
    }
    private JSONObject lowEvaluate(int id){
        JSONObject o = new JSONObject();
        o.put("id",id);
        o.put("score",1);
        return o;
    }

    @DataProvider(name = "remarkContent")
    private Object[] remark() {
        return new Object[][]{
                {"备注长度10个字符", "1000", "@%^#*?><jh"},
                //{"备注长度9个字符", "1001", "zsdfghyjh"},
                {"备注长度0个字符", "1001", ""},
                {"符合长度限制的字符组合", "1000", "!_=-][/?ASF你        我他slj,.  l;'\nffflllai"},
        };
    }


}