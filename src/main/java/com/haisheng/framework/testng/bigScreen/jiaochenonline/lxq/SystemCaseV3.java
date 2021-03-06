package com.haisheng.framework.testng.bigScreen.jiaochenonline.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.Response;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.model.AppletModeListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.followup.AppPageV3Scene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.followup.AppRemarkV3Scene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.followup.AppReplyV3Scene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.appointmentmanage.RecordExportScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.consultmanagement.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage.PreSaleCustomerStyleListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.ShopListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage.EvaluateExportScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.record.ExportPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shop.AddScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.List;

/**
 * ????????????
 *
 * @author lxq
 * @date 2021/1/29 11:17
 */
public class SystemCaseV3 extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCE = EnumTestProduct.JC_ONLINE_JD;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.JC_ONLINE_YS;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_LXQ_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public SceneUtil util = new SceneUtil(visitor);
    jiaoChenInfoOnline info = new jiaoChenInfoOnline();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //??????checklist???????????????
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = "?????????";
        //??????jenkins-job???????????????
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //??????????????????
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //??????shopId
        commonConfig.setShopId(PRODUCE.getShopId()).setReferer(PRODUCE.getReferer()).setRoleId(ALL_AUTHORITY.getRoleId()).setProduct(PRODUCE.getAbbreviation());
        beforeClassInit(commonConfig);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        util.loginApplet(APPLET_USER_ONE);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);

    }


    /**
     * -----------------------------??????????????????------------------------------------
     */

    @Test(dataProvider = "ONLINEEXPERTINFO")
    public void onlineExpertApplet1(String customerName, String customerPhone, String content, String mess, String status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginApplet(APPLET_USER_ONE);
            Long brandId = info.BrandIDOnline;
            Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(info.CarStyleIDOnline).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("shop_id");

            Response obj = AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .brandId(brandId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code = obj.getCode();
            if (status.equals("false")) {
                String message = obj.getMessage();
                Preconditions.checkArgument(code == 1001, mess + ", ????????????:" + code + ", ????????????:" + message);
            }
            if (status.equals("true")) {
                Preconditions.checkArgument(code == 1000, mess + ", ????????????:" + code);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("?????????????????????????????????????????????????????????");
        }
    }

    @Test
    public void onlineExpertApplet2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginApplet(APPLET_USER_ONE);
            Long brandId = info.BrandIDOnline;
            Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(info.CarStyleIDOnline).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("shop_id");

            String customerName = "??????";
            String customerPhone = "13811110000";
            String content = "12345678901234567890";

            Response obj1 = AppletConsultOnlineExpertsSubmitScene.builder().customerPhone(customerPhone).content(content)
                    .brandId(brandId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code1 = obj1.getCode();
            String message = obj1.getMessage();
            Preconditions.checkArgument(code1 == 1001, "??????????????????????????????:" + code1 + ", ????????????:" + message);

            Response obj2 = AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).content(content)
                    .brandId(brandId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code2 = obj2.getCode();
            String message2 = obj2.getMessage();
            Preconditions.checkArgument(code2 == 1001, "?????????????????????????????????:" + code2 + ", ????????????:" + message2);

            Response obj3 = AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).customerPhone(customerPhone)
                    .brandId(brandId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code3 = obj3.getCode();
            String message3 = obj3.getMessage();
            Preconditions.checkArgument(code3 == 1001, "????????????????????????????????????:" + code3 + ", ????????????:" + message3);

//            Response obj4 = AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
//                    .modelId(modelId).shopId(shopId).build().invoke(visitor, false);
//            int code4 = obj4.getCode();
//            String message4 = obj4.getMessage();
//            Preconditions.checkArgument(code4==1001,"??????????????????????????????:"+code4+", ????????????:"+message4);

            Response obj5 = AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .brandId(brandId).shopId(shopId).build().visitor(visitor).getResponse();
            int code5 = obj5.getCode();
            String message5 = obj5.getMessage();
            Preconditions.checkArgument(code5 == 1001, "??????????????????????????????:" + code5 + ", ????????????:" + message5);

            Response obj6 = AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .brandId(brandId).modelId(modelId).build().visitor(visitor).getResponse();
            int code6 = obj6.getCode();
            String message6 = obj6.getMessage();
            Preconditions.checkArgument(code6 == 1001, "????????????????????????????????????:" + code6 + ", ????????????:" + message6);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????????????????????????????????????????");
        }
    }

    @Test
    public void onlineExpertApplet3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //?????????????????????????????????
            int befApplet = info.getAppletmessNum();
            //??????app??????????????????
            util.loginApp(ALL_AUTHORITY);
            int befApp = AppPageV3Scene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            //PC????????????????????????
            util.loginPc(ALL_AUTHORITY);
            int befPC = OnlineExpertsPageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");

            //???????????????????????????
            JSONObject submitobj = info.submitonlineExpert();
            String name = submitobj.getString("customerName");
            String phone = submitobj.getString("customerPhone");
            String brandId = info.brandIdForName(submitobj.getLong("brandId"));
//            String modelId = submitobj.getString("modelId");
            String shopId = info.shopIdForName(submitobj.getLong("shopId"));

            //?????????????????????????????????
            int afterApplet = info.getAppletmessNum();
            //??????app??????????????????
            util.loginApp(ALL_AUTHORITY);
            int afterApp = AppPageV3Scene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            //PC????????????????????????
            util.loginPc(ALL_AUTHORITY);
            JSONObject obj = OnlineExpertsPageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
            int afterPC = OnlineExpertsPageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");


            int applet = afterApplet - befApplet;
            int app = afterApp - befApp;
            int pc = afterPC - befPC;
            Preconditions.checkArgument(applet == 1, "????????????????????????+1");
            Preconditions.checkArgument(app == 1, "app???????????????+1");
            Preconditions.checkArgument(pc == 1, "PC???????????????????????????+1");
            if (pc == 1) {
                String customer_name = obj.getString("customer_name");
                String customer_phone = obj.getString("customer_phone");
                String shop_name = obj.getString("shop_name");
                String brand_name = obj.getString("brand_name");
                String model_name = obj.getString("model_name");
                Preconditions.checkArgument(customer_name.equals(name), "???????????????" + name + ", PC??????" + customer_name);
                Preconditions.checkArgument(customer_phone.equals(phone), "??????????????????" + phone + ", PC??????" + customer_phone);
                Preconditions.checkArgument(shop_name.equals(shopId), "????????????????????????" + shopId + ", PC??????" + shop_name);
                Preconditions.checkArgument(brand_name.equals(brandId), "????????????????????????" + brandId + ", PC??????" + brand_name);
//                Preconditions.checkArgument(model_name.equals(modelId),"????????????????????????"+modelId+", PC??????"+model_name);

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("???????????????????????????????????????????????????app????????????&?????????????????????????????????&PC??????????????????????????????");
        }
    }

    @Test
    public void onlineExpertPC1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            JSONArray alllist = OnlineExpertsPageListScene.builder().page(1).size(50).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist.size() > 0) {
                for (int i = 0; i < alllist.size(); i++) {
                    JSONObject obj = alllist.getJSONObject(i);
                    Preconditions.checkArgument(obj.containsKey("shop_name"), obj.getString("id") + "?????????????????????");
                    Preconditions.checkArgument(obj.containsKey("consult_date"), obj.getString("id") + "?????????????????????");
                    Preconditions.checkArgument(obj.containsKey("customer_name"), obj.getString("id") + "??????????????????");
                    Preconditions.checkArgument(obj.containsKey("customer_phone"), obj.getString("id") + "?????????????????????");
                    Preconditions.checkArgument(obj.containsKey("brand_name"), obj.getString("id") + "???????????????");
                    Preconditions.checkArgument(obj.containsKey("style_name"), obj.getString("id") + "???????????????");
                    Preconditions.checkArgument(obj.containsKey("model_name"), obj.getString("id") + "???????????????");
                    Preconditions.checkArgument(obj.containsKey("is_over_time"), obj.getString("id") + "?????????????????????");
                    Preconditions.checkArgument(obj.containsKey("consult_content"), obj.getString("id") + "?????????????????????");
                }
            }

            JSONArray followLIst = OnlineExpertsPageListScene.builder().page(1).size(50).followDateStart("2021-04-14").followDateEnd("2021-06-01").build().visitor(visitor).execute().getJSONArray("list");
            if (followLIst.size() > 0) {
                for (int j = 0; j < followLIst.size(); j++) {
                    JSONObject obj = followLIst.getJSONObject(j);
                    Preconditions.checkArgument(obj.containsKey("follow_date"), obj.getString("id") + "?????????????????????");
                    Preconditions.checkArgument(obj.containsKey("follow_sales_name"), obj.getString("id") + "?????????????????????");
                    Preconditions.checkArgument(obj.containsKey("follow_login_name"), obj.getString("id") + "?????????????????????");

                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC???????????????????????????????????????");
        }
    }

    @Test
    public void onlineExpertPC2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            // todo

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC??????????????????????????????????????????????????????");
        }
    }

    @Test
    public void onlineExpertPC3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            // todo

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC???????????????????????????????????????????????????????????????");
        }
    }

    @Test
    public void onlineExpertPCsearch1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            //????????????????????????
            JSONObject shopobj = ShopListScene.builder().build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
            Long shopId = shopobj.getLong("shop_id");
            String shopName = shopobj.getString("shop_name");
            JSONArray alllist = OnlineExpertsPageListScene.builder().page(1).size(20).shopId(shopId).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist.size() > 0) {
                for (int i = 0; i < alllist.size(); i++) {
                    JSONObject obj = alllist.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("shop_name").equals(shopName), "??????????????????=" + shopName + "??????????????????" + obj.getString("shop_name"));
                }
            }

            //??????????????????=?????????
            JSONArray alllist2 = OnlineExpertsPageListScene.builder().page(1).size(20).isOverTime(true).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist2.size() > 0) {
                for (int i = 0; i < alllist2.size(); i++) {
                    JSONObject obj = alllist2.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("is_over_time").equals("???"), "??????????????????=?????????????????????" + obj.getString("is_over_time"));
                }
            }

            //??????????????????=?????????
            JSONArray alllist3 = OnlineExpertsPageListScene.builder().page(1).size(20).isOverTime(false).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist3.size() > 0) {
                for (int i = 0; i < alllist3.size(); i++) {
                    JSONObject obj = alllist3.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("is_over_time").equals("???"), "??????????????????=?????????????????????" + obj.getString("is_over_time"));
                }
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC????????????????????????????????????");
        }
    }

    @Test(dataProvider = "ONLINEEXPERTSEARCH")
    public void onlineExpertPCsearch2(String conditions) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);

            //????????????????????????
            JSONArray alllist = OnlineExpertsPageListScene.builder().page(1).size(20).followLoginName(conditions).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist.size() > 0) {
                for (int i = 0; i < alllist.size(); i++) {
                    JSONObject obj = alllist.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("follow_login_name").contains(conditions), "??????????????????=" + conditions + "??????????????????" + obj.getString("follow_login_name"));
                }
            }

            //????????????????????????
            JSONArray alllist2 = OnlineExpertsPageListScene.builder().page(1).size(20).followSalesName(conditions).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist2.size() > 0) {
                for (int i = 0; i < alllist2.size(); i++) {
                    JSONObject obj = alllist2.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("follow_sales_name").contains(conditions), "??????????????????=" + conditions + "??????????????????" + obj.getString("follow_sales_name"));
                }
            }

            //?????????????????????
            JSONArray alllist3 = OnlineExpertsPageListScene.builder().page(1).size(20).customerName(conditions).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist3.size() > 0) {
                for (int i = 0; i < alllist3.size(); i++) {
                    JSONObject obj = alllist3.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("customer_name").contains(conditions), "???????????????=" + conditions + "??????????????????" + obj.getString("customer_name"));
                }
            }

            //????????????????????????
            JSONArray alllist4 = OnlineExpertsPageListScene.builder().page(1).size(20).customerPhone(conditions).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist4.size() > 0) {
                for (int i = 0; i < alllist4.size(); i++) {
                    JSONObject obj = alllist4.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("customer_phone").contains(conditions), "??????????????????=" + conditions + "??????????????????" + obj.getString("customer_phone"));
                }
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC????????????????????????????????????");
        }
    }

    @Test
    public void onlineExpertPCsearch3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            // ????????????????????????
            String followdate = "";
            JSONArray alllist = OnlineExpertsPageListScene.builder().page(1).size(50).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist.size() > 0) {
                for (int i = 0; i < alllist.size(); i++) {
                    JSONObject obj = alllist.getJSONObject(i);
                    if (obj.containsKey("follow_date")) {
                        followdate = obj.getString("follow_date").substring(0, 10);
                        break;
                    }
                }
                JSONArray searchlist1 = OnlineExpertsPageListScene.builder().page(1).size(50).followDateStart(followdate).followDateEnd(followdate).build().visitor(visitor).execute().getJSONArray("list");
                Preconditions.checkArgument(searchlist1.size() >= 1, "?????????????????????" + followdate + ", ???????????????");
                for (int j = 0; j < searchlist1.size(); j++) {
                    JSONObject obj1 = searchlist1.getJSONObject(j);
                    Preconditions.checkArgument(obj1.getString("follow_date").substring(0, 10).equals(followdate), "??????" + followdate + " , ????????????" + obj1.getString("follow_date").substring(0, 9));
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC????????????????????????????????????");
        }
    }

    @Test
    public void onlineExpertPCsearch4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            // ????????????????????????
            String consult_date = "";
            JSONArray alllist = OnlineExpertsPageListScene.builder().page(1).size(50).build().visitor(visitor).execute().getJSONArray("list");

            if (alllist.size() > 0) {
                for (int i = 0; i < alllist.size(); i++) {
                    JSONObject obj = alllist.getJSONObject(i);
                    if (obj.containsKey("consult_date")) {
                        consult_date = obj.getString("consult_date").substring(0, 10);
                        break;
                    }
                }
                JSONArray searchlist1 = OnlineExpertsPageListScene.builder().page(1).size(50).consultDateStart(consult_date).consultDateEnd(consult_date).build().visitor(visitor).execute().getJSONArray("list");
                Preconditions.checkArgument(searchlist1.size() >= 1, "?????????????????????" + consult_date + ", ???????????????");
                for (int j = 0; j < searchlist1.size(); j++) {
                    JSONObject obj1 = searchlist1.getJSONObject(j);
                    Preconditions.checkArgument(obj1.getString("consult_date").substring(0, 10).equals(consult_date), "??????" + consult_date + " , ????????????" + obj1.getString("consult_date").substring(0, 9));
                }
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC????????????????????????????????????");
        }
    }


    @Test(dataProvider = "ONLINEEXPERTEXPLAIN")
    public void onlineExpertPCExplain1(String content, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);

            Response obj = OnlineExpertsExplainEditScene.builder().content(content).build().visitor(visitor).getResponse();
            int code = obj.getCode();
            if (!mess.contains("20001")) {
                Preconditions.checkArgument(code == 1000, mess + "??????" + obj.getMessage());
            }
            if (mess.contains("20001")) {
                Preconditions.checkArgument(code == 1001, mess + "??????" + obj.getMessage());
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC????????????????????????,????????????");
        }
    }

    @Test
    public void onlineExpertPCExplain2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);

            String content1 = "<p style=\"text-align:center;\" size=\"5\" _root=\"[object Object]\" __ownerID=\"undefined\" __hash=\"" +
                    "undefined\" __altered=\"false\">??????</p><p><span style=\"font-size:32px\">??????</span></p><p><span style=\"font-si" +
                    "ze:12px\">??????</span></p><p><strong>??????</strong></p><p><em>??????</em></p><p><u>?????????</u></p><p><span style=\"color:#f3" +
                    "2784\">??????</span></p><p></p><p></p><div class=\"media-wrap image-wrap\"><img src=\"" + info.getLogoUrl() + "\"/></div><p></p><p></p>";

            Response obj = OnlineExpertsExplainEditScene.builder().content(content1).build().visitor(visitor).getResponse();
            int code = obj.getCode();

            Preconditions.checkArgument(code == 1000, "??????" + obj.getMessage());


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC????????????????????????,?????????");
        }
    }

    @Test(dataProvider = "ONLINEEXPERTRULE")
    public void onlineExpertPCRule1(String type, String remind, String over, String work1, String work2, String work3, String work4, String week1, String week2, String week3, String week4, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            String workStr = "{\n" +
                    "    \"forenoon_date_start\": \"" + work1 + "\",\n" +
                    "    \"forenoon_date_end\": \"" + work2 + "\",\n" +
                    "    \"afternoon_date_start\": \"" + work3 + "\",\n" +
                    "    \"afternoon_date_end\": \"" + work4 + "\"\n" +
                    "  }";
            String weekStr = "{\n" +
                    "    \"forenoon_date_start\": \"" + week1 + "\",\n" +
                    "    \"forenoon_date_end\": \"" + week2 + "\",\n" +
                    "    \"afternoon_date_start\": \"" + week3 + "\",\n" +
                    "    \"afternoon_date_end\": \"" + week4 + "\"\n" +
                    "  }";
            JSONObject work_day = JSONObject.parseObject(workStr);
            JSONObject week_day = JSONObject.parseObject(weekStr);
            Response obj = ResponseRuleEditScene.builder().businessType(type).remindTime(Integer.parseInt(remind)).overTime(Integer.parseInt(over))
                    .workDay(work_day).weekDay(week_day).build().visitor(visitor).getResponse();
            int code = obj.getCode();
            if (mess.contains("??????")) {
                Preconditions.checkArgument(code == 1000, mess + "?????????" + obj.getMessage());
            }
            if (mess.contains("??????")) {
                Preconditions.checkArgument(code == 1001, mess + "?????????" + code);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC????????????/??????????????????????????????");
        }
    }

    @Test
    public void onlineExpertPCBanner() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            // todo ???????????????????????????banner

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC????????????????????????");
        }
    }

    @Test(dataProvider = "ONLINEEXPERTREPLY")
    public void onlineExpertPCReply(String content, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //???????????????????????????
            int befApplet = info.getAppletmessNum();

            //???????????????????????????
            info.submitonlineExpert();
            //PC??????????????????
            util.loginPc(ALL_AUTHORITY);
            Long id1 = OnlineExpertsPageListScene.builder().page(1).size(5).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj1 = ReplyScene.builder().id(id1).content(content).build().visitor(visitor).getResponse();
            int code1 = obj1.getCode();

            //?????????????????????????????????
            info.submitPreService();
            //PC??????
            util.loginPc(ALL_AUTHORITY);
            Long id2 = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj2 = ReplyScene.builder().id(id2).content(content).build().visitor(visitor).getResponse();
            int code2 = obj2.getCode();

            //?????????????????????????????????
            info.submitAfterService();
            //PC??????
            util.loginPc(ALL_AUTHORITY);
            Long id3 = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj3 = ReplyScene.builder().id(id3).content(content).build().visitor(visitor).getResponse();
            int code3 = obj3.getCode();

            //???????????????????????????
            int afterApplet = info.getAppletmessNum();
            int sum = afterApplet - befApplet;

            if (mess.contains("??????")) {
                Preconditions.checkArgument(code1 == 1000, mess + "??????" + obj1.getMessage());
                Preconditions.checkArgument(code2 == 1000, mess + "??????" + obj2.getMessage());
                Preconditions.checkArgument(code3 == 1000, mess + "??????" + obj3.getMessage());
                Preconditions.checkArgument(sum == 6, "???????????????&PC??????????????????????????????????????????");
            }
            if (mess.contains("??????")) {
                Preconditions.checkArgument(code1 == 1001, mess + "?????????" + code1);
                Preconditions.checkArgument(code2 == 1001, mess + "?????????" + code2);
                Preconditions.checkArgument(code3 == 1001, mess + "?????????" + code3);
                Preconditions.checkArgument(sum == 3, "??????????????????????????????????????????");
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC????????????/???????????? ??????");
        }
    }

    @Test(dataProvider = "ONLINEEXPERTREMARK")
    public void onlineExpertPCRemark(String content, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //???????????????????????????
            int befApplet = info.getAppletmessNum();

            //???????????????????????????
            info.submitonlineExpert();
            //PC??????????????????
            util.loginPc(ALL_AUTHORITY);
            Long id1 = OnlineExpertsPageListScene.builder().page(1).size(5).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj1 = RemarkScene.builder().id(id1).remarkContent(content).build().visitor(visitor).getResponse();
            int code1 = obj1.getCode();

            //?????????????????????????????????
            info.submitPreService();
            //PC??????
            util.loginPc(ALL_AUTHORITY);
            Long id2 = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj2 = RemarkScene.builder().id(id2).remarkContent(content).build().visitor(visitor).getResponse();
            int code2 = obj2.getCode();

            //?????????????????????????????????
            info.submitAfterService();
            //PC??????
            util.loginPc(ALL_AUTHORITY);
            Long id3 = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj3 = RemarkScene.builder().id(id3).remarkContent(content).build().visitor(visitor).getResponse();
            int code3 = obj3.getCode();

            //???????????????????????????
            int afterApplet = info.getAppletmessNum();
            int sum = afterApplet - befApplet;

            if (mess.contains("??????")) {
                Preconditions.checkArgument(code1 == 1000, mess + "??????" + obj1.getMessage());
                Preconditions.checkArgument(code2 == 1000, mess + "??????" + obj2.getMessage());
                Preconditions.checkArgument(code3 == 1000, mess + "??????" + obj3.getMessage());
                Preconditions.checkArgument(sum == 3, "???????????????&PC??????????????????????????????????????????");
            }
            if (mess.contains("??????")) {
                Preconditions.checkArgument(code1 == 1001, mess + "?????????" + code1);
                Preconditions.checkArgument(code2 == 1001, mess + "?????????" + code2);
                Preconditions.checkArgument(code3 == 1001, mess + "?????????" + code3);
                Preconditions.checkArgument(sum == 3, "??????????????????????????????????????????");
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC????????????/???????????? ??????");
        }
    }

    @Test(dataProvider = "ONLINEEXPERTREPLY")
    public void onlineExpertAPPReply(String content, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //???????????????????????????
            int befApplet = info.getAppletmessNum();

            //???????????????????????????
            info.submitonlineExpert();
            //PC??????????????????
            util.loginApp(ALL_AUTHORITY);
            Long id1 = AppPageV3Scene.builder().type(info.ONLINE_EXPERTS).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj1 = AppReplyV3Scene.builder().followId(id1).content(content).build().visitor(visitor).getResponse();
            int code1 = obj1.getCode();

            //?????????????????????????????????
            info.submitPreService();
            //PC??????
            util.loginApp(ALL_AUTHORITY);
            Long id2 = AppPageV3Scene.builder().type(info.SALES).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj2 = AppReplyV3Scene.builder().followId(id2).content(content).build().visitor(visitor).getResponse();
            int code2 = obj2.getCode();

            //?????????????????????????????????
            info.submitAfterService();
            //PC??????
            util.loginApp(ALL_AUTHORITY);
            Long id3 = AppPageV3Scene.builder().type(info.AFTER_SALES).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj3 = AppReplyV3Scene.builder().followId(id3).content(content).build().visitor(visitor).getResponse();
            int code3 = obj3.getCode();

            //???????????????????????????
            int afterApplet = info.getAppletmessNum();
            int sum = afterApplet - befApplet;

            if (mess.contains("??????")) {
                Preconditions.checkArgument(code1 == 1000, mess + "??????" + obj1.getMessage());
                Preconditions.checkArgument(code2 == 1000, mess + "??????" + obj2.getMessage());
                Preconditions.checkArgument(code3 == 1000, mess + "??????" + obj3.getMessage());
                Preconditions.checkArgument(sum == 6, "???????????????&app??????????????????????????????????????????");
            }
            if (mess.contains("??????")) {
                Preconditions.checkArgument(code1 == 1001, mess + "?????????" + code1);
                Preconditions.checkArgument(code2 == 1001, mess + "?????????" + code2);
                Preconditions.checkArgument(code3 == 1001, mess + "?????????" + code3);
                Preconditions.checkArgument(sum == 3, "??????????????????????????????????????????");
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app????????????/???????????? ??????");
        }
    }

    @Test(dataProvider = "ONLINEEXPERTREMARK")
    public void onlineExpertAPPRemark(String content, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //???????????????????????????
            int befApplet = info.getAppletmessNum();

            //???????????????????????????
            info.submitonlineExpert();
            //PC??????????????????
            util.loginApp(ALL_AUTHORITY);
            Long id1 = AppPageV3Scene.builder().type(info.ONLINE_EXPERTS).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj1 = AppRemarkV3Scene.builder().followId(id1).remark(content).build().visitor(visitor).getResponse();
            int code1 = obj1.getCode();

            //?????????????????????????????????
            info.submitPreService();
            //app??????
            util.loginApp(ALL_AUTHORITY);
            Long id2 = AppPageV3Scene.builder().type(info.SALES).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj2 = AppRemarkV3Scene.builder().followId(id2).remark(content).build().visitor(visitor).getResponse();
            int code2 = obj2.getCode();

            //?????????????????????????????????
            info.submitAfterService();
            //app??????
            util.loginApp(ALL_AUTHORITY);
            Long id3 = AppPageV3Scene.builder().type(info.AFTER_SALES).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj3 = AppRemarkV3Scene.builder().followId(id3).remark(content).build().visitor(visitor).getResponse();
            int code3 = obj3.getCode();

            //???????????????????????????
            Thread.sleep(500);
            int afterApplet = info.getAppletmessNum();
            int sum = afterApplet - befApplet;

            if (mess.contains("??????")) {
                Preconditions.checkArgument(code1 == 1000, mess + "??????" + obj1.getMessage());
                Preconditions.checkArgument(code2 == 1000, mess + "??????" + obj2.getMessage());
                Preconditions.checkArgument(code3 == 1000, mess + "??????" + obj3.getMessage());
                Preconditions.checkArgument(sum == 3, "???????????????&app??????????????????????????????????????????");
            }
            if (mess.contains("??????")) {
                Preconditions.checkArgument(code1 == 1001, mess + "?????????" + code1);
                Preconditions.checkArgument(code2 == 1001, mess + "?????????" + code2);
                Preconditions.checkArgument(code3 == 1001, mess + "?????????" + code3);
                Preconditions.checkArgument(sum == 3, "??????????????????????????????????????????");
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app????????????/???????????? ??????");
        }
    }


    /**
     * -----------------------------??????????????????------------------------------------
     */

    @Test(dataProvider = "ONLINEEXPERTINFO")
    public void preServiceSubmit1(String customerName, String customerPhone, String content, String mess, String status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Long brandId = info.BrandIDOnline;
            Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(info.CarStyleIDOnline).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("shop_id");
            String salesId = AppletConsultDedicatedServiceSalesListScene.builder().build().visitor(visitor).execute().getJSONArray("sales_list").getJSONObject(0).getString("sales_id");//??????
            String aftersalesId = AppletConsultDedicatedServiceSalesListScene.builder().build().visitor(visitor).execute().getJSONArray("after_sales_list").getJSONObject(0).getString("sales_id");//??????

            Response obj = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code = obj.getCode();
            if (status.equals("false")) {
                String message = obj.getMessage();
                Preconditions.checkArgument(code == 1001, mess + ", ????????????:" + code + ", ????????????:" + message);
            }
            if (status.equals("true")) {
                Preconditions.checkArgument(code == 1000, mess + ", ????????????:" + code);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("???????????????????????????????????????????????????????????????");
        }
    }

    @Test(dataProvider = "ONLINEEXPERTINFO")
    public void preServiceSubmit11(String customerName, String customerPhone, String content, String mess, String status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Long brandId = info.BrandIDOnline;
            Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(info.CarStyleIDOnline).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("shop_id");
            String aftersalesId = AppletConsultDedicatedServiceSalesListScene.builder().build().visitor(visitor).execute().getJSONArray("after_sales_list").getJSONObject(0).getString("sales_id");//??????

            Response obj = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(aftersalesId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code = obj.getCode();
            if (status.equals("false")) {
                String message = obj.getMessage();
                Preconditions.checkArgument(code == 1001, mess + ", ????????????:" + code + ", ????????????:" + message);
            }
            if (status.equals("true")) {
                Preconditions.checkArgument(code == 1000, mess + ", ????????????:" + code);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("???????????????????????????????????????????????????????????????");
        }
    }

    @Test
    public void preServiceSubmit2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long brandId = info.BrandIDOnline;
            Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(info.CarStyleIDOnline).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("shop_id");
            String salesId = AppletConsultDedicatedServiceSalesListScene.builder().build().visitor(visitor).execute().getJSONArray("after_sales_list").getJSONObject(0).getString("sales_id");//??????

            String customerName = "??????";
            String customerPhone = "13811110000";
            String content = "12345678901234567890";

            Response obj1 = AppletConsultPreServiceSubmitScene.builder().customerPhone(customerPhone).content(content)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code1 = obj1.getCode();
            String message = obj1.getMessage();
            Preconditions.checkArgument(code1 == 1001, "??????????????????????????????:" + code1 + ", ????????????:" + message);

            Response obj2 = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).content(content)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code2 = obj2.getCode();
            String message2 = obj2.getMessage();
            Preconditions.checkArgument(code2 == 1001, "?????????????????????????????????:" + code2 + ", ????????????:" + message2);

            Response obj3 = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code3 = obj3.getCode();
            String message3 = obj3.getMessage();
            Preconditions.checkArgument(code3 == 1001, "??????????????????????????????:" + code3 + ", ????????????:" + message3);

            Response obj4 = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code4 = obj4.getCode();
            String message4 = obj4.getMessage();
            Preconditions.checkArgument(code4 == 1001, "??????????????????????????????:" + code4 + ", ????????????:" + message4);

            Response obj5 = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(salesId).shopId(shopId).build().visitor(visitor).getResponse();
            int code5 = obj5.getCode();
            String message5 = obj5.getMessage();
            Preconditions.checkArgument(code5 == 1001, "??????????????????????????????:" + code5 + ", ????????????:" + message5);

            Response obj6 = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(salesId).modelId(modelId).build().visitor(visitor).getResponse();
            int code6 = obj6.getCode();
            String message6 = obj6.getMessage();
            Preconditions.checkArgument(code6 == 1001, "????????????????????????????????????:" + code6 + ", ????????????:" + message6);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("????????????????????????????????????????????????????????????");
        }
    }

    @Test
    public void preServiceSubmit3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //?????????????????????????????????
            int befApplet = info.getAppletmessNum();
            //??????app??????????????????
            util.loginApp(ALL_AUTHORITY);
            int befApp = AppPageV3Scene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            //PC????????????????????????
            util.loginPc(ALL_AUTHORITY);
            int befPC = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");

            //???????????????????????????
            JSONObject submitobj = info.submitPreService();
            String name = submitobj.getString("customerName");
            String phone = submitobj.getString("customerPhone");
            String brandId = submitobj.getString("brandId");
//            String modelId = submitobj.getString("modelId");
            String shopId = submitobj.getString("shopName");

            //?????????????????????????????????
            int afterApplet = info.getAppletmessNum();
            //??????app??????????????????
            util.loginApp(ALL_AUTHORITY);
            int afterApp = AppPageV3Scene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            //PC????????????????????????
            util.loginPc(ALL_AUTHORITY);
            JSONObject obj = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
            int afterPC = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");


            int applet = afterApplet - befApplet;
            int app = afterApp - befApp;
            int pc = afterPC - befPC;
            Preconditions.checkArgument(applet == 1, "????????????????????????+1");
            Preconditions.checkArgument(app == 1, "app???????????????+1");
            Preconditions.checkArgument(pc == 1, "PC???????????????????????????+1");
            if (pc == 1) {
                String customer_name = obj.getString("customer_name");
                String customer_phone = obj.getString("customer_phone");
                String shop_name = obj.getString("shop_name");
                String brand_name = obj.getString("brand_name");
//                String model_name = obj.getString("model_name");
                Preconditions.checkArgument(customer_name.equals(name), "???????????????" + name + ", PC??????" + customer_name);
                Preconditions.checkArgument(customer_phone.equals(phone), "??????????????????" + phone + ", PC??????" + customer_phone);
                Preconditions.checkArgument(shop_name.equals(shopId), "????????????????????????" + shopId + ", PC??????" + shop_name);
                Preconditions.checkArgument(brand_name.equals(brandId), "????????????????????????" + brandId + ", PC??????" + brand_name);
//                Preconditions.checkArgument(model_name.equals(modelId),"????????????????????????"+modelId+", PC??????"+model_name);
                Preconditions.checkArgument(obj.getString("sale_type").contains("??????"), "???????????????, PC??????" + obj.getString("sale_type"));

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("???????????????????????????????????????????????????app????????????&?????????????????????????????????&PC??????????????????????????????");
        }
    }


    @Test(dataProvider = "ONLINEEXPERTINFO")
    public void afterServiceSubmit1(String customerName, String customerPhone, String content, String mess, String status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Long brandId = info.BrandIDOnline;
            Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(info.CarStyleIDOnline).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("shop_id");

            String salesId = AppletConsultDedicatedServiceSalesListScene.builder().build().visitor(visitor).execute().getJSONArray("after_sales_list").getJSONObject(0).getString("sales_id");

            Response obj = AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code = obj.getCode();
            if (status.equals("false")) {
                String message = obj.getMessage();
                Preconditions.checkArgument(code == 1001, mess + ", ????????????:" + code + ", ????????????:" + message);
            }
            if (status.equals("true")) {
                Preconditions.checkArgument(code == 1000, mess + ", ????????????:" + code);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("???????????????????????????????????????????????????????????????");
        }
    }

    @Test
    public void afterServiceSubmit2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long brandId = info.BrandIDOnline;
            Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(info.CarStyleIDOnline).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("shop_id");

            String salesId = AppletConsultDedicatedServiceSalesListScene.builder().build().visitor(visitor).execute().getJSONArray("after_sales_list").getJSONObject(0).getString("sales_id");

            String customerName = "??????";
            String customerPhone = "13811110000";
            String content = "12345678901234567890";

            Response obj1 = AppletConsultAfterServiceSubmitScene.builder().customerPhone(customerPhone).content(content)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code1 = obj1.getCode();
            String message = obj1.getMessage();
            Preconditions.checkArgument(code1 == 1001, "??????????????????????????????:" + code1 + ", ????????????:" + message);

            Response obj2 = AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).content(content)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code2 = obj2.getCode();
            String message2 = obj2.getMessage();
            Preconditions.checkArgument(code2 == 1001, "?????????????????????????????????:" + code2 + ", ????????????:" + message2);

            Response obj3 = AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code3 = obj3.getCode();
            String message3 = obj3.getMessage();
            Preconditions.checkArgument(code3 == 1001, "??????????????????????????????:" + code3 + ", ????????????:" + message3);

            Response obj4 = AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code4 = obj4.getCode();
            String message4 = obj4.getMessage();
            Preconditions.checkArgument(code4 == 1001, "??????????????????????????????:" + code4 + ", ????????????:" + message4);

            Response obj5 = AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(salesId).shopId(shopId).build().visitor(visitor).getResponse();
            int code5 = obj5.getCode();
            String message5 = obj5.getMessage();
            Preconditions.checkArgument(code5 == 1001, "??????????????????????????????:" + code5 + ", ????????????:" + message5);

            Response obj6 = AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(salesId).modelId(modelId).build().visitor(visitor).getResponse();
            int code6 = obj6.getCode();
            String message6 = obj6.getMessage();
            Preconditions.checkArgument(code6 == 1001, "????????????????????????????????????:" + code6 + ", ????????????:" + message6);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("????????????????????????????????????????????????????????????");
        }
    }

    @Test
    public void afterServiceSubmit3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //?????????????????????????????????
            int befApplet = info.getAppletmessNum();
            //??????app??????????????????
            util.loginApp(ALL_AUTHORITY);
            int befApp = AppPageV3Scene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            //PC????????????????????????
            util.loginPc(ALL_AUTHORITY);
            int befPC = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");

            //???????????????????????????
            JSONObject submitobj = info.submitAfterService();
            String name = submitobj.getString("customerName");
            String phone = submitobj.getString("customerPhone");
            String brandId = submitobj.getString("brandId");
//            String modelId = submitobj.getString("modelId");
            String shopId = submitobj.getString("shopName");


            Thread.sleep(1000);
            //??????app??????????????????
            util.loginApp(ALL_AUTHORITY);
            int afterApp = AppPageV3Scene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            //?????????????????????????????????
            int afterApplet = info.getAppletmessNum();
            //PC????????????????????????
            util.loginPc(ALL_AUTHORITY);

            JSONObject obj = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
            int afterPC = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");

            int applet = afterApplet - befApplet;
            int app = afterApp - befApp;
            int pc = afterPC - befPC;
            Preconditions.checkArgument(applet == 1, "????????????????????????+1");
            Preconditions.checkArgument(app == 1, "app???????????????+1");
            Preconditions.checkArgument(pc == 1, "PC???????????????????????????+1");
            if (pc == 1) {
                String customer_name = obj.getString("customer_name");
                String customer_phone = obj.getString("customer_phone");
                String shop_name = obj.getString("shop_name");
                String brand_name = obj.getString("brand_name");
//                String model_name = obj.getString("model_name");
                Preconditions.checkArgument(customer_name.equals(name), "???????????????" + name + ", PC??????" + customer_name);
                Preconditions.checkArgument(customer_phone.equals(phone), "??????????????????" + phone + ", PC??????" + customer_phone);
                Preconditions.checkArgument(shop_name.equals(shopId), "????????????????????????" + shopId + ", PC??????" + shop_name);
                Preconditions.checkArgument(brand_name.equals(brandId), "????????????????????????" + brandId + ", PC??????" + brand_name);
//                Preconditions.checkArgument(model_name.equals(modelId),"????????????????????????"+modelId+", PC??????"+model_name);
                Preconditions.checkArgument(obj.getString("sale_type").contains("??????"), "???????????????, PC??????" + obj.getString("sale_type"));

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("???????????????????????????????????????????????????app????????????&?????????????????????????????????&PC??????????????????????????????");
        }
    }

    @Test(dataProvider = "ONLINEEXPERTEXPLAIN")
    public void dedicatedServicePCExplain1(String content, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);

            Response obj2 = DedicatedServiceExplainEditScene.builder().content(content).build().visitor(visitor).getResponse();
            int code2 = obj2.getCode();
            if (!mess.contains("20001")) {
                Preconditions.checkArgument(code2 == 1000, mess + "??????" + obj2.getMessage());
            }
            if (mess.contains("20001")) {
                Preconditions.checkArgument(code2 == 1001, mess + "??????" + obj2.getMessage());
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC????????????????????????,????????????");
        }
    }

    @Test
    public void dedicatedServicePCExplain2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);

            String content1 = "<p style=\"text-align:center;\" size=\"5\" _root=\"[object Object]\" __ownerID=\"undefined\" __hash=\"" +
                    "undefined\" __altered=\"false\">??????????????????</p><p><span style=\"font-size:32px\">??????????????????</span></p><p><span style=\"font-si" +
                    "ze:12px\">??????</span></p><p><strong>??????????????????</strong></p><p><em>??????????????????</em></p><p><u>?????????????????????</u></p><p><span style=\"color:#f3" +
                    "2784\">??????????????????</span></p><p></p><p></p><div class=\"media-wrap image-wrap\"><img src=\"" + info.getLogoUrl() + "\"/></div><p></p><p></p>";

            Response obj = DedicatedServiceExplainEditScene.builder().content(content1).build().visitor(visitor).getResponse();
            int code = obj.getCode();

            Preconditions.checkArgument(code == 1000, "??????" + obj.getMessage());


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC????????????????????????,?????????");
        }
    }


    @Test
    public void dedicatedServicePCsearch1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            //????????????????????????
            JSONObject shopobj = ShopListScene.builder().build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
            Long shopId = shopobj.getLong("shop_id");
            String shopName = shopobj.getString("shop_name");
            JSONArray alllist = DedicatedServicePageListScene.builder().page(1).size(20).shopId(shopId).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist.size() > 0) {
                for (int i = 0; i < alllist.size(); i++) {
                    JSONObject obj = alllist.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("shop_name").equals(shopName), "??????????????????=" + shopName + "??????????????????" + obj.getString("shop_name"));
                }
            }

            //??????????????????
            JSONObject styleobj = PreSaleCustomerStyleListScene.builder().shopId(-1L).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
            Long styleId = styleobj.getLong("style_id");
            String styleName = styleobj.getString("style_name");
            JSONArray alllist3 = DedicatedServicePageListScene.builder().page(1).size(20).carStyleId(styleId).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist3.size() > 0) {
                for (int i = 0; i < alllist3.size(); i++) {
                    JSONObject obj = alllist3.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("style_name").equals(styleName), "????????????=" + styleName + "??????????????????" + obj.getString("style_name"));
                }
            }

            //??????????????????=?????????
            JSONArray alllist2 = DedicatedServicePageListScene.builder().page(1).size(20).isOverTime(true).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist2.size() > 0) {
                for (int i = 0; i < alllist2.size(); i++) {
                    JSONObject obj = alllist2.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("is_over_time").equals("???"), "??????????????????=?????????????????????" + obj.getString("is_over_time"));
                }
            }

            //??????????????????=?????????
            JSONArray alllist4 = DedicatedServicePageListScene.builder().page(1).size(20).isOverTime(false).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist4.size() > 0) {
                for (int i = 0; i < alllist4.size(); i++) {
                    JSONObject obj = alllist4.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("is_over_time").equals("???"), "??????????????????=?????????????????????" + obj.getString("is_over_time"));
                }
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC??????????????????????????????");
        }
    }

    @Test(dataProvider = "ONLINEEXPERTSEARCH")
    public void dedicatedServicePCsearch2(String conditions) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);

            //????????????????????????
            JSONArray alllist = DedicatedServicePageListScene.builder().page(1).size(20).followLoginName(conditions).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist.size() > 0) {
                for (int i = 0; i < alllist.size(); i++) {
                    JSONObject obj = alllist.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("follow_login_name").contains(conditions), "??????????????????=" + conditions + "??????????????????" + obj.getString("follow_login_name"));
                }
            }

            //????????????????????????
            JSONArray alllist2 = DedicatedServicePageListScene.builder().page(1).size(20).followSalesName(conditions).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist2.size() > 0) {
                for (int i = 0; i < alllist2.size(); i++) {
                    JSONObject obj = alllist2.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("follow_sales_name").contains(conditions), "??????????????????=" + conditions + "??????????????????" + obj.getString("follow_sales_name"));
                }
            }

            //?????????????????????
            JSONArray alllist3 = DedicatedServicePageListScene.builder().page(1).size(20).customerName(conditions).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist3.size() > 0) {
                for (int i = 0; i < alllist3.size(); i++) {
                    JSONObject obj = alllist3.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("customer_name").contains(conditions), "???????????????=" + conditions + "??????????????????" + obj.getString("customer_name"));
                }
            }

            //????????????????????????
            JSONArray alllist4 = DedicatedServicePageListScene.builder().page(1).size(20).customerPhone(conditions).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist4.size() > 0) {
                for (int i = 0; i < alllist4.size(); i++) {
                    JSONObject obj = alllist4.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("customer_phone").contains(conditions), "??????????????????=" + conditions + "??????????????????" + obj.getString("customer_phone"));
                }
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC??????????????????????????????");
        }
    }

    @Test
    public void dedicatedServicePCsearch3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            // ????????????????????????
            String followdate = "";
            JSONArray alllist = DedicatedServicePageListScene.builder().page(1).size(50).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist.size() > 0) {
                for (int i = 0; i < alllist.size(); i++) {
                    JSONObject obj = alllist.getJSONObject(i);
                    if (obj.containsKey("follow_date")) {
                        followdate = obj.getString("follow_date").substring(0, 10);
                        break;
                    }
                }
                JSONArray searchlist1 = DedicatedServicePageListScene.builder().page(1).size(50).followDateStart(followdate).followDateEnd(followdate).build().visitor(visitor).execute().getJSONArray("list");
                Preconditions.checkArgument(searchlist1.size() >= 1, "?????????????????????" + followdate + ", ???????????????");
                for (int j = 0; j < searchlist1.size(); j++) {
                    JSONObject obj1 = searchlist1.getJSONObject(j);
                    Preconditions.checkArgument(obj1.getString("follow_date").substring(0, 10).equals(followdate), "??????" + followdate + " , ????????????" + obj1.getString("follow_date").substring(0, 9));
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC??????????????????????????????");
        }
    }

    @Test
    public void dedicatedServicePCsearch4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            // ????????????????????????
            String consult_date = "";
            JSONArray alllist = DedicatedServicePageListScene.builder().page(1).size(50).build().visitor(visitor).execute().getJSONArray("list");

            if (alllist.size() > 0) {
                for (int i = 0; i < alllist.size(); i++) {
                    JSONObject obj = alllist.getJSONObject(i);
                    if (obj.containsKey("consult_date")) {
                        consult_date = obj.getString("consult_date").substring(0, 10);
                        break;
                    }
                }
                JSONArray searchlist1 = DedicatedServicePageListScene.builder().page(1).size(50).consultDateStart(consult_date).consultDateEnd(consult_date).build().visitor(visitor).execute().getJSONArray("list");
                Preconditions.checkArgument(searchlist1.size() >= 1, "?????????????????????" + consult_date + ", ???????????????");
                for (int j = 0; j < searchlist1.size(); j++) {
                    JSONObject obj1 = searchlist1.getJSONObject(j);
                    Preconditions.checkArgument(obj1.getString("consult_date").substring(0, 10).equals(consult_date), "??????" + consult_date + " , ????????????" + obj1.getString("consult_date").substring(0, 9));
                }
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC??????????????????????????????");
        }
    }


    /* -----------------------------------V3.1??????---------------------------------------------------*/

    //@Test(dataProvider = "EXPORT1")
    public void Export1(String type, String mess) {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            int code = RecordExportScene.builder().type(type).page(1).size(10).exportType("CURRENT_PAGE").build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(code == 1000, mess + "??????????????????" + code);
            Thread.sleep(800);
            String status = ExportPageScene.builder().page(1).size(1).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getString("status_name");
            Preconditions.checkArgument(status.equals("????????????"), mess + " " + status);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????");
        }
    }

    //@Test(dataProvider = "EXPORT2")
    public void Export2(String type, String mess) {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            int code = EvaluateExportScene.builder().evaluateType(Integer.parseInt(type)).page(1).size(10).exportType("CURRENT_PAGE").build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(code == 1000, mess + "??????????????????" + code);
            Thread.sleep(800);
            String status = ExportPageScene.builder().page(1).size(1).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getString("status_name");
            Preconditions.checkArgument(status.equals("????????????"), mess + " " + status);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????");
        }
    }


    /* -----------------------------------V3.1??????---------------------------------------------------*/


//    @Test(dataProvider = "SHOP")
//    public void addshop(String simple_name, String name, String district_code, String adddress, String sale_tel, String service_tel,
//                        String longitude, String latitude, String appointment_status,String washing_status) {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            util.loginPc(ALL_AUTHORITY);
//            JSONArray arr = new JSONArray();
//            arr.add(info.BrandID);
//
//            int code = AddScene.builder().name(name).simpleName(simple_name).districtCode(district_code).address(adddress).brandList(arr)
//                    .saleTel(sale_tel).serviceTel(service_tel).longitude(Double.valueOf(longitude)).latitude(Double.valueOf(latitude)).avatarPath(info.getLogo()).customerServiceTel(sale_tel).rescueTel(sale_tel)
//                    .build().invoke(visitor,false).getCode();
//            Preconditions.checkArgument(code==1000,"???????????????1000?????????"+ code);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC?????????????????????????????????");
//        }
//    }


    @Test
    public void addshoperr1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            util.loginPc(ALL_AUTHORITY);
            String sale_tel = info.sitphone1;
            String service_tel = info.phone;

            JSONArray arr = new JSONArray();
            arr.add(System.currentTimeMillis());

            int code = AddScene.builder().name(info.stringsix).simpleName(info.stringsix).districtCode(info.district_code).address(info.stringsix).brandList(arr)
                    .saleTel(sale_tel).serviceTel(service_tel).longitude(129.8439).latitude(42.96805).avatarPath(info.getLogo()).customerServiceTel(sale_tel).rescueTel(sale_tel)
                    .build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(code == 1001, "???????????????1001?????????" + code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC???????????????????????????????????????????????????");
        }
    }


    /**
     *
     */
    @Test
    public void a1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            AppletConsultOnlineExpertsSubmitScene.builder().brandId(12L).build().visitor(visitor).getResponse().getMessage();
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("");
        }

    }

    //PC ??????????????????
    @Test
    public void evalute_Buycar1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            //PC??????????????????????????????
            int type = 3;
            String messageName = "??????????????????";
            int points = info.setevaluate(type, messageName).getInteger("points");


            //??????????????????????????????
            int voucherTotal = info.getVoucherTotal("13436941018");
            util.loginApplet(APPLET_USER_ONE);
            Integer appletScore = AppletUserInfoDetailScene.builder().build().visitor(visitor).execute().getInteger("score");

            //PC??????
            util.loginPc(ALL_AUTHORITY);
            info.newBuyCarRec();
            util.loginApplet(APPLET_USER_ONE);
            JSONObject evaluateConfigDescribe = AppletEvaluateConfigScene.builder().type(type).shopId(info.oneshopid).build().visitor(visitor).execute();

            String describe = evaluateConfigDescribe.getJSONArray("list").getJSONObject(2).getString("describe");
            List label = evaluateConfigDescribe.getJSONArray("list").getJSONObject(2).getJSONArray("labels");


            //??????

            AppletEvaluateSubmitScene.builder()
                    .describe(describe).labels(label).id(info.getMessDetailId())
                    .isAnonymous(true)
                    .score(2)
                    .shopId(info.oneshopid).suggestion("????????????").type(type)
                    .build().visitor(visitor).execute();


            //??????????????????????????????

            Integer appletScoreAfter = AppletUserInfoDetailScene.builder().build().visitor(visitor).execute().getInteger("score");
            util.loginPc(ALL_AUTHORITY);
            int voucherTotalAfter = info.getVoucherTotal("13436941018");

            Preconditions.checkArgument(voucherTotalAfter - voucherTotal == 1, "???????????????????????????+1");
            Preconditions.checkArgument(appletScoreAfter - appletScore == points, "????????????????????????????????????");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC??????????????????->?????????????????????????????????????????????");
        }
    }


    @Test
    public void remark() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

//            util.loginPc(ALL_AUTHORITY);
//
//
//            CustomerRemarkScene.builder().remark(info.string200).id(161L).shopId(46522L).build().invoke(visitor);
//
//

            //???????????????????????????
            util.loginApplet(APPLET_USER_ONE);
            JSONArray arr = AppletCarListScene.builder().build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if (!obj.containsKey("plate_number")) {
                    AppletCarDeleteScene.builder().id(obj.getLong("id")).build().visitor(visitor).execute();

                }
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC????????????");
        }
    }


    /**
     * -----------------------------------dataProvider??????????????????---------------------------------------------------
     */
    @DataProvider(name = "ONLINEEXPERTINFO")
    public Object[] onlineExpertrInfo() {
        return new String[][]{ // ?????? ????????? ????????????(10-200)  ????????? ??????/??????

                {"???", "1382172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), "??????10geZI???@", "??????1??????&????????????10??????(????????????)", "true"},
                {info.stringfifty, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), info.string200, "??????50??????&????????????200??????(????????????)", "true"},
                {info.stringsix, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 100)), info.stringfifty, "?????????10???(????????????)", "false"},
                {info.stringsix, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), info.stringfifty, "?????????12???(????????????)", "false"},
                {info.stringfifty + "1", "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), info.stringfifty, "??????51???(????????????)", "false"},
                {info.stringsix, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), "??????9geZI???@", "????????????9??????(????????????)", "false"},
                {info.stringsix, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), info.string200 + "1", "????????????201??????(????????????)", "false"},

        };
    }

    @DataProvider(name = "ONLINEEXPERTSEARCH")
    public Object[] onlineExpertrSearch() {
        return new String[]{
                "1",
                "a",
                "??????",
                "??????",
        };
    }

    @DataProvider(name = "ONLINEEXPERTEXPLAIN")
    public Object[] onlineExpertrExplain() {
        return new String[][]{

                {"<p>???????????????<span style=\\\"font-size:120px\\\">51234  </span></p>", "??????????????????"},
                {info.string200 + info.string200 + info.string200 + info.string200 + info.string200 + info.string200 + info.string200 + info.string200 + info.string200 + info.string200, "2000???"},//2000???
                {info.getString(20000), "20000???"},
                {info.getString(20001), "20001???"},

        };
    }

    @DataProvider(name = "ONLINEEXPERTRULE")
    public Object[] onlineExpertrRULE() {
        return new String[][]{ //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????

                //????????????
                {"ONLINE_EXPERTS", "1", "1", "11:00", "12:00", "13:00", "15:00", "08:00", "09:00", "22:00", "23:00", "??????????????????"},
                {"ONLINE_EXPERTS", "720", "720", "08:00", "09:00", "13:00", "15:00", "01:00", "02:00", "13:00", "15:00", "??????????????????"},
                {"ONLINE_EXPERTS", "720", "720", "00:00", "12:00", "12:00", "23:59", "00:00", "12:00", "12:00", "23:59", "??????????????????"},
                {"ONLINE_EXPERTS", "721", "720", "08:00", "09:00", "13:00", "15:00", "01:00", "02:00", "13:00", "15:00", "?????????????????????????????????721??????"},
                {"ONLINE_EXPERTS", "60", "721", "08:00", "09:00", "13:00", "15:00", "01:00", "02:00", "13:00", "15:00", "?????????????????????????????????721??????"},
                {"ONLINE_EXPERTS", "60", "60", "08:00", "07:00", "13:00", "15:00", "01:00", "02:00", "13:00", "15:00", "????????????????????????????????????????????????>????????????"},
                {"ONLINE_EXPERTS", "60", "60", "08:00", "09:00", "15:00", "13:00", "01:00", "02:00", "13:00", "15:00", "????????????????????????????????????????????????>????????????"},
                {"ONLINE_EXPERTS", "60", "60", "08:00", "09:00", "13:00", "15:00", "08:00", "07:00", "13:00", "15:00", "????????????????????????????????????????????????>????????????"},
                {"ONLINE_EXPERTS", "60", "60", "08:00", "09:00", "13:00", "15:00", "01:00", "02:00", "15:00", "13:00", "????????????????????????????????????????????????>????????????"},

                //????????????
                {"SALES", "1", "1", "11:00", "12:00", "13:00", "15:00", "08:00", "09:00", "22:00", "23:00", "??????????????????"},
                {"SALES", "720", "720", "08:00", "09:00", "13:00", "15:00", "01:00", "02:00", "13:00", "15:00", "??????????????????"},
                {"SALES", "60", "60", "00:00", "12:00", "12:00", "23:59", "00:00", "12:00", "12:00", "23:59", "??????????????????"},
                {"SALES", "721", "720", "08:00", "09:00", "13:00", "15:00", "01:00", "02:00", "13:00", "15:00", "?????????????????????????????????721??????"},
                {"SALES", "60", "721", "08:00", "09:00", "13:00", "15:00", "01:00", "02:00", "13:00", "15:00", "?????????????????????????????????721??????"},
                {"SALES", "60", "60", "08:00", "07:00", "13:00", "15:00", "01:00", "02:00", "13:00", "15:00", "????????????????????????????????????????????????>????????????"},
                {"SALES", "60", "60", "08:00", "09:00", "15:00", "13:00", "01:00", "02:00", "13:00", "15:00", "????????????????????????????????????????????????>????????????"},
                {"SALES", "60", "60", "08:00", "09:00", "13:00", "15:00", "08:00", "07:00", "13:00", "15:00", "????????????????????????????????????????????????>????????????"},
                {"SALES", "60", "60", "08:00", "09:00", "13:00", "15:00", "01:00", "02:00", "15:00", "13:00", "????????????????????????????????????????????????>????????????"},


        };
    }

    @DataProvider(name = "ONLINEEXPERTREPLY")
    public Object[] onlineExpertrReply() {
        return new String[][]{

//                {"1???A???5", "????????????5??????"},
//                {info.getString(1000), "????????????1000??????"},
//                {info.string200, "????????????200??????"},
                {"????????????", "????????????4??????"},
                {info.getString(1001), "????????????1001??????"},

        };
    }

    @DataProvider(name = "ONLINEEXPERTREMARK")
    public Object[] onlineExpertrRemark() {
        return new String[][]{

                {"???????????????", "????????????5??????"},
//                {info.string20, "????????????20??????"},
//                {info.string200, "????????????200??????"},
//                {info.getString(1000), "????????????1000??????"},
                {info.getString(1001), "????????????1001??????"},
                {info.getString(4), "????????????4??????"},

        };
    }

    @DataProvider(name = "EXPORT1")
    public Object[] export1() {
        return new String[][]{
                {"REPAIR", "??????????????????"},
                {"MAINTAIN", "??????????????????"},
                {"TEST_DRIVE", "??????????????????"},

        };
    }

    @DataProvider(name = "EXPORT2")
    public Object[] export2() {
        return new String[][]{
                {"3", "??????????????????"},
                {"1", "??????????????????"},
                {"2", "??????????????????"},
                {"4", "??????????????????"},

        };
    }

    @DataProvider(name = "SHOP")
    public Object[] shop() {

        return new String[][]{
//                {info.stringone, info.stringone,info.district_code,info.stringone, info.sitphone1,info.sitphone2,"129.8439","42.96805","ENABLE","ENABLE"}, //????????????????????? ????????? ??????????????????
//                {info.stringone, info.stringten,info.district_code,info.stringfifty, info.phone,info.phone,"129.8439","42.96805","ENABLE","DISABLE"},
//                {info.stringten, info.stringone,info.district_code,info.stringten, info.phone,info.phone,"129.8439","42.96805","DISABLE","ENABLE"},
                {info.stringten, info.stringfifty, info.district_code, info.stringone, info.sitphone1, info.phone, "129.8439", "42.96805", "DISABLE", "DISABLE"},
//                {info.stringone, info.stringfifty,info.district_code,info.stringten, info.phone,info.sitphone2,"129.8439","42.96805","DISABLE","DISABLE"},

        };
    }
}
