package com.haisheng.framework.testng.bigScreen.itemYuntong.caseonline.mc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.AppFlowUp.AppFlowUpPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.AppFlowUp.AppFlowUpRemarkScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.H5.CustomerForClientSubmitScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppCustomerDetailV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.customermanager.AppCustomerManagerPreCustomerAddPlateScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.followup.AppFollowUpPageV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppCustomerEditV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppCustomerRemarkV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppPreSalesReceptionQueryRetentionQrCodeScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanagev4.PreSaleCustomerInfoRemarkRecordScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.EvaluateDetailV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.EvaluatePageV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.*;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.bean.PreReceptionBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.tool.YtDataCenter;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppFinishReceptionScene;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Objects;

public class ReceivingSystemCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct product = EnumTestProduct.YT_ONLINE_JD; // ????????????-??????
    private static final EnumAccount YT_RECEPTION_ACCOUNT = EnumAccount.YT_ONLINE_MC; // ?????????????????? ????????????
    public VisitorProxy visitor = new VisitorProxy(product);   // ?????????????????????????????????????????????????????????
    public SceneUtil util = new SceneUtil(visitor);
    CommonConfig commonConfig = new CommonConfig();    // ??????????????????

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        //??????checklist???????????????
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = "??????";
        //??????jenkins-job???????????????
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //??????????????????
        commonConfig.dingHook = DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(YT_RECEPTION_ACCOUNT.getReceptionShopId()).setRoleId(YT_RECEPTION_ACCOUNT.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);  // ???????????????
        util.loginPc(YT_RECEPTION_ACCOUNT);   //??????
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
        logger.logCaseStart(caseResult.getCaseName());
    }



    @Test(description = "??????????????????????????????",dataProvider = "qrCodeInfo", dataProviderClass = YtDataCenter.class)
    public void QRCodeCreat(String description, String point, String content, String expect){
        try {
            commonConfig.setShopId(YT_RECEPTION_ACCOUNT.getReceptionShopId()).setRoleId(YT_RECEPTION_ACCOUNT.getRoleId());
            PreReceptionBean receptionCard = util.getReceptionCard();
            AppPreSalesReceptionQueryRetentionQrCodeScene.builder().receptionId(receptionCard.getId().toString()).build().visitor(visitor).execute();
            commonConfig.setShopId(null).setRoleId(null);
            Integer code = CustomerForClientSubmitScene.builder().receptionId(receptionCard.getId()).customerName("????????????" + dt.getHistoryDate(0)).intentionCarModelId(util.mcCarId())
                    .sexId(1).estimateBuyCarTime("2030-08-08").build().modify(point, content).visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(Objects.equals(code.toString(), expect),description+"???????????????"+expect+"?????????????????????"+code);
            commonConfig.setShopId(YT_RECEPTION_ACCOUNT.getReceptionShopId()).setRoleId(YT_RECEPTION_ACCOUNT.getRoleId());
        } catch (AssertionError | Exception e){
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????????????????");
            commonConfig.setShopId(YT_RECEPTION_ACCOUNT.getReceptionShopId()).setRoleId(YT_RECEPTION_ACCOUNT.getRoleId());
        }

    }

    @Test(dataProvider = "editErrorInfo", dataProviderClass = YtDataCenter.class)
    public void test02ChangeUserInfo(String description, String point, String content, String expect) {
        try {
            PreReceptionBean reception = util.getReception();
            Integer code = AppCustomerEditV4Scene.builder().id(reception.getId()).customerId(reception.getCustomerId()).shopId(reception.getShopId()).customerName("name")
                    .customerPhone("18"+CommonUtil.getRandom(9)).sexId(1).intentionCarModelId(Long.parseLong(util.mcCarId())).estimateBuyCarDate("2035-12-20")
                    .customerSource("NATURE_VISIT").build().modify(point,content).visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(Objects.equals(code.toString(), expect), description + "???????????????=" + expect + "????????????code=" + code);
            sleep(3);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app?????????????????????,??????????????????");
        }
    }

    @Test(dataProvider = "remark", dataProviderClass = YtDataCenter.class)
    public void test03PcRemark(String description, String remark, String expect) {
        try {
            PreReceptionBean reception = util.getReception();
            Integer code = CustomerRemarkScene.builder().id(reception.getId()).shopId(reception.getShopId()).remark(remark).build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(Objects.equals(String.valueOf(code), expect), description + ",??????:" + expect + ",????????????:" + code);
            if (Objects.equals("1000", String.valueOf(code))) {
                JSONArray remarks = AppCustomerDetailV4Scene.builder().shopId(reception.getShopId().toString()).id(reception.getId().toString()).build().visitor(visitor).execute().getJSONArray("remarks");
                String addedRemark = remarks.getJSONObject(0).getString("remark");
                Preconditions.checkArgument(Objects.equals(addedRemark, remark), "????????????????????????pc?????????????????????" + remark + ",app???????????????????????????:" + addedRemark);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("????????????PC??????");
        }
    }


    @Test(dataProvider = "remark", dataProviderClass = YtDataCenter.class)
    public void test03AppRemark(String description, String remark, String expect) {
        try {
            PreReceptionBean reception = util.getReception();
            Integer code = AppCustomerRemarkV4Scene.builder().id(reception.getId()).shopId(reception.getShopId()).remark(remark).build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(Objects.equals(String.valueOf(code), expect), description + ",??????:" + expect + ",????????????:" + code);
            if (Objects.equals("1000", String.valueOf(code))) {
                String addedRemark = PreSaleCustomerInfoRemarkRecordScene.builder().shopId(reception.getShopId()).customerId(reception.getCustomerId().toString()).build().visitor(visitor).execute()
                        .getJSONArray("list").getJSONObject(0).getString("remark_content");
                Preconditions.checkArgument(Objects.equals(addedRemark, remark), "????????????????????????app?????????????????????" + remark + ",pc???????????????????????????:" + addedRemark);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app???????????????");
        }
    }

    @Test(description = "?????????????????????,??????", dataProvider = "addPlate", dataProviderClass = YtDataCenter.class)
    public void test04AddPlateNumber(String description, String content, String expect) {
        try {
            PreReceptionBean reception = util.getReception();
            String message = AppCustomerManagerPreCustomerAddPlateScene.builder().customerId(reception.getCustomerId()).shopId(reception.getShopId()).plateNumber(content).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(Objects.equals(expect, message), "????????????????????????" + description + "??????????????????" + expect + "????????????" + message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("?????????????????????,??????");
        }
    }


    @Test
    public void test05PcComplete() {
        try {
            PreReceptionBean reception = util.getReception();
            FinishReceptionScene.builder().id(reception.getId()).shopId(reception.getShopId()).build().visitor(visitor).execute();
            Boolean isFinish = PreSalesReceptionPageScene.builder().build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getBoolean("is_finish");
            Preconditions.checkArgument(isFinish, "???????????????????????????????????????=" + reception.getCustomerPhone());
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("Pc????????????");
        }
    }

    @Test(dataProvider = "evaluateRemark", dataProviderClass = YtDataCenter.class)
    public void test06FlowUpContent(String description, String remark, String expect) {
        try {
            JSONObject follow = AppFollowUpPageV4Scene.builder().size(10).build().visitor(visitor).execute().getJSONArray("list").stream()
                    .map(e -> (JSONObject) e).filter(f -> !f.getJSONObject("pre_reception_offline_evaluate").getBoolean("is_follow")).findFirst().orElse(null);
            JSONObject firstFlow;
            if (follow != null){
                firstFlow = follow;
            }else {
                PreReceptionBean customer = util.getReception();
                AppFinishReceptionScene.builder().id(customer.getId()).shopId(customer.getShopId()).build().visitor(visitor).execute();
                commonConfig.setShopId(null);
                commonConfig.setRoleId(null);
                JSONArray evaluateInfoList = new JSONArray();
                PreSalesRecpEvaluateOpt.builder().reception_id(customer.getId()).build().visitor(visitor).execute().getJSONArray("list").stream().map(j -> (JSONObject) j).map(json -> json.getInteger("id")).forEach(e -> evaluateInfoList.add(lowEvaluate(e)));
                PreSalesRecpEvaluateSubmit.builder().evaluate_info_list(evaluateInfoList).reception_id(customer.getId()).build().visitor(visitor).getResponse();
                commonConfig.setShopId(YT_RECEPTION_ACCOUNT.getReceptionShopId());
                commonConfig.setRoleId(YT_RECEPTION_ACCOUNT.getRoleId());
                firstFlow = AppFlowUpPageScene.builder().size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
            }
            Integer flowUpId = firstFlow.getInteger("id");
            String phone = firstFlow.getJSONObject("pre_reception_offline_evaluate").getString("customer_phone");
            Integer code = AppFlowUpRemarkScene.builder().followId(flowUpId).remark(remark).build().visitor(visitor).getResponse().getCode();
            Integer id = EvaluatePageV4Scene.builder().page(1).size(10).evaluateType(4).customerPhone(phone).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getInteger("id");
            String remarkContent = EvaluateDetailV4Scene.builder().id(id).build().visitor(visitor).execute().getString("remark_content");
            Preconditions.checkArgument(Objects.equals(expect, String.valueOf(code)), description + ",????????????code=" + expect + ",????????????code=" + code);
            if (Objects.equals(expect, "1000")) {
                Preconditions.checkArgument(Objects.equals(remark, remarkContent), "????????????????????????APP??????????????????:" + remark + ";PC????????????:" + remarkContent);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????,??????????????????");
        }
    }

    private JSONObject lowEvaluate(int id) {
        JSONObject o = new JSONObject();
        o.put("id", id);
        o.put("score", 1);
        return o;
    }


}
