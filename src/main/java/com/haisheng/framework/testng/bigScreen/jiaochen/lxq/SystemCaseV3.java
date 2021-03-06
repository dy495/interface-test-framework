package com.haisheng.framework.testng.bigScreen.jiaochen.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.Response;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.JiaoChenInfo;
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
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.PublicParam;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 内容运营
 *
 * @author lxq
 * @date 2021/1/29 11:17
 */
public class SystemCaseV3 extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCE = EnumTestProduct.JC_DAILY_JD;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.JC_DAILY_YS;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_LXQ_DAILY;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public SceneUtil util = new SceneUtil(visitor);
    JiaoChenInfo info = new JiaoChenInfo();
    ScenarioUtil jc = ScenarioUtil.getInstance();

    PublicParam pp = new PublicParam();
    CommonConfig commonConfig = new CommonConfig();


    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");

        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = "吕雪晴";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
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
     * -----------------------------在线专家咨询------------------------------------
     */

    @Test(dataProvider = "ONLINEEXPERTINFO")
    public void onlineExpertApplet1(String customerName, String customerPhone, String content, String mess, String status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginApplet(APPLET_USER_ONE);
            Long brandId = info.BrandID;
            Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(info.CarStyleID).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("shop_id");

            Response obj = AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .brandId(brandId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code = obj.getCode();
            if (status.equals("false")) {
                String message = obj.getMessage();
                Preconditions.checkArgument(code == 1001, mess + ", 状态码为:" + code + ", 提示语为:" + message);
            }
            if (status.equals("true")) {
                Preconditions.checkArgument(code == 1000, mess + ", 状态码为:" + code);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交在线专家咨询，校验必填项内容");
        }
    }

    @Test
    public void onlineExpertApplet2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginApplet(APPLET_USER_ONE);
            Long brandId = info.BrandID;
            Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(info.CarStyleID).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("shop_id");

            String customerName = "奶糖";
            String customerPhone = "13811110000";
            String content = "12345678901234567890";

            Response obj1 = AppletConsultOnlineExpertsSubmitScene.builder().customerPhone(customerPhone).content(content)
                    .brandId(brandId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code1 = obj1.getCode();
            String message = obj1.getMessage();
            Preconditions.checkArgument(code1 == 1001, "不填写姓名，状态码为:" + code1 + ", 提示语为:" + message);

            Response obj2 = AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).content(content)
                    .brandId(brandId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code2 = obj2.getCode();
            String message2 = obj2.getMessage();
            Preconditions.checkArgument(code2 == 1001, "不填写手机号，状态码为:" + code2 + ", 提示语为:" + message2);

            Response obj3 = AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).customerPhone(customerPhone)
                    .brandId(brandId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code3 = obj3.getCode();
            String message3 = obj3.getMessage();
            Preconditions.checkArgument(code3 == 1001, "不填写咨询内容，状态码为:" + code3 + ", 提示语为:" + message3);

//            JSONObject obj4 = AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
//                    .modelId(modelId).shopId(shopId).build().invoke(visitor, false);
//            int code4 = obj4.getCode();
//            String message4 = obj4.getMessage();
//            Preconditions.checkArgument(code4==1001,"不填写品牌，状态码为:"+code4+", 提示语为:"+message4);

            Response obj5 = AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .brandId(brandId).shopId(shopId).build().visitor(visitor).getResponse();
            int code5 = obj5.getCode();
            String message5 = obj5.getMessage();
            Preconditions.checkArgument(code5 == 1001, "不填写车型，状态码为:" + code5 + ", 提示语为:" + message5);

            Response obj6 = AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .brandId(brandId).modelId(modelId).build().visitor(visitor).getResponse();
            int code6 = obj6.getCode();
            String message6 = obj6.getMessage();
            Preconditions.checkArgument(code6 == 1001, "不填写咨询门店，状态码为:" + code6 + ", 提示语为:" + message6);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交在线专家咨询，不填写必填项");
        }
    }

    @Test
    public void onlineExpertApplet3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取小程序消息列表数量
            int befApplet = info.getAppletmessNum();
            //获取app跟进列表数量
            util.loginApp(ALL_AUTHORITY);
            int befApp = AppPageV3Scene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            //PC【在线专家列表】
            util.loginPc(ALL_AUTHORITY);
            int befPC = OnlineExpertsPageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");

            //小程序提交在线专家
            JSONObject submitobj = info.submitonlineExpert();
            String name = submitobj.getString("customerName");
            String phone = submitobj.getString("customerPhone");
            String brandId = info.brandIdForName(submitobj.getLong("brandId"));
//            String modelId = submitobj.getString("modelId");
            String shopId = info.shopIdForName(submitobj.getLong("shopId"));

            //获取小程序消息列表数量
            int afterApplet = info.getAppletmessNum();
            //获取app跟进列表数量
            util.loginApp(ALL_AUTHORITY);
            int afterApp = AppPageV3Scene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            //PC【在线专家列表】
            util.loginPc(ALL_AUTHORITY);
            JSONObject obj = OnlineExpertsPageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
            int afterPC = OnlineExpertsPageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");


            int applet = afterApplet - befApplet;
            int app = afterApp - befApp;
            int pc = afterPC - befPC;
            Preconditions.checkArgument(applet == 1, "小程序消息列表未+1");
            Preconditions.checkArgument(app == 1, "app跟进列表未+1");
            Preconditions.checkArgument(pc == 1, "PC【在线专家】列表未+1");
            if (pc == 1) {
                String customer_name = obj.getString("customer_name");
                String customer_phone = obj.getString("customer_phone");
                String shop_name = obj.getString("shop_name");
                String brand_name = obj.getString("brand_name");
                String model_name = obj.getString("model_name");
                Preconditions.checkArgument(customer_name.equals(name), "提交时姓名" + name + ", PC展示" + customer_name);
                Preconditions.checkArgument(customer_phone.equals(phone), "提交时手机号" + phone + ", PC展示" + customer_phone);
                Preconditions.checkArgument(shop_name.equals(shopId), "提交时选择的门店" + shopId + ", PC展示" + shop_name);
                Preconditions.checkArgument(brand_name.equals(brandId), "提交时选择的品牌" + brandId + ", PC展示" + brand_name);
//                Preconditions.checkArgument(model_name.equals(modelId),"提交时选择的品牌"+modelId+", PC展示"+model_name);

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交在线专家咨询，结果校验：app产生跟进&小程序收到自动回复消息&PC【在线专家】列表展示");
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
                    Preconditions.checkArgument(obj.containsKey("shop_name"), obj.getString("id") + "未展示归属门店");
                    Preconditions.checkArgument(obj.containsKey("consult_date"), obj.getString("id") + "未展示咨询时间");
                    Preconditions.checkArgument(obj.containsKey("customer_name"), obj.getString("id") + "未展示联系人");
                    Preconditions.checkArgument(obj.containsKey("customer_phone"), obj.getString("id") + "未展示联系电话");
                    Preconditions.checkArgument(obj.containsKey("brand_name"), obj.getString("id") + "未展示品牌");
                    Preconditions.checkArgument(obj.containsKey("style_name"), obj.getString("id") + "未展示车系");
                    Preconditions.checkArgument(obj.containsKey("model_name"), obj.getString("id") + "未展示车型");
                    Preconditions.checkArgument(obj.containsKey("is_over_time"), obj.getString("id") + "未展示是否超时");
                    Preconditions.checkArgument(obj.containsKey("consult_content"), obj.getString("id") + "未展示咨询内容");
                }
            }

            JSONArray followLIst = OnlineExpertsPageListScene.builder().page(1).size(50).followDateStart("2021-04-14").followDateEnd("2021-06-01").build().visitor(visitor).execute().getJSONArray("list");
            if (followLIst.size() > 0) {
                for (int j = 0; j < followLIst.size(); j++) {
                    JSONObject obj = followLIst.getJSONObject(j);
                    Preconditions.checkArgument(obj.containsKey("follow_date"), obj.getString("id") + "未展示跟进时间");
                    Preconditions.checkArgument(obj.containsKey("follow_sales_name"), obj.getString("id") + "未展示跟进人员");
                    Preconditions.checkArgument(obj.containsKey("follow_login_name"), obj.getString("id") + "未展示跟进账号");

                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC在线专家咨询列表展示项校验");
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
            saveData("PC在线专家咨询列表根据咨询时间倒叙展示");
        }
    }


    @Test
    public void onlineExpertPCsearch1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            //根据归属门店搜索
            JSONObject shopobj = ShopListScene.builder().build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
            Long shopId = shopobj.getLong("shop_id");
            String shopName = shopobj.getString("shop_name");
            JSONArray alllist = OnlineExpertsPageListScene.builder().page(1).size(20).shopId(shopId).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist.size() > 0) {
                for (int i = 0; i < alllist.size(); i++) {
                    JSONObject obj = alllist.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("shop_name").equals(shopName), "搜索归属门店=" + shopName + "，结果中包含" + obj.getString("shop_name"));
                }
            }

            //根据是否超时=是搜索
            JSONArray alllist2 = OnlineExpertsPageListScene.builder().page(1).size(20).isOverTime(true).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist2.size() > 0) {
                for (int i = 0; i < alllist2.size(); i++) {
                    JSONObject obj = alllist2.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("is_over_time").equals("是"), "搜索是否超时=是，结果中包含" + obj.getString("is_over_time"));
                }
            }

            //根据是否超时=否搜索
            JSONArray alllist3 = OnlineExpertsPageListScene.builder().page(1).size(20).isOverTime(false).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist3.size() > 0) {
                for (int i = 0; i < alllist3.size(); i++) {
                    JSONObject obj = alllist3.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("is_over_time").equals("否"), "搜索是否超时=否，结果中包含" + obj.getString("is_over_time"));
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC在线专家咨询列表单项搜索");
        }
    }

    @Test(dataProvider = "ONLINEEXPERTSEARCH")
    public void onlineExpertPCsearch2(String conditions) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);

            //根据跟进账号搜索
            JSONArray alllist = OnlineExpertsPageListScene.builder().page(1).size(20).followLoginName(conditions).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist.size() > 0) {
                for (int i = 0; i < alllist.size(); i++) {
                    JSONObject obj = alllist.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("follow_login_name").contains(conditions), "搜索回复账号=" + conditions + "，结果中包含" + obj.getString("follow_login_name"));
                }
            }

            //根据跟进人员搜索
            JSONArray alllist2 = OnlineExpertsPageListScene.builder().page(1).size(20).followSalesName(conditions).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist2.size() > 0) {
                for (int i = 0; i < alllist2.size(); i++) {
                    JSONObject obj = alllist2.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("follow_sales_name").contains(conditions), "搜索回复人员=" + conditions + "，结果中包含" + obj.getString("follow_sales_name"));
                }
            }

            //根据联系人搜索
            JSONArray alllist3 = OnlineExpertsPageListScene.builder().page(1).size(20).customerName(conditions).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist3.size() > 0) {
                for (int i = 0; i < alllist3.size(); i++) {
                    JSONObject obj = alllist3.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("customer_name").contains(conditions), "搜索联系人=" + conditions + "，结果中包含" + obj.getString("customer_name"));
                }
            }

            //根据联系电话搜索
            JSONArray alllist4 = OnlineExpertsPageListScene.builder().page(1).size(20).customerPhone(conditions).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist4.size() > 0) {
                for (int i = 0; i < alllist4.size(); i++) {
                    JSONObject obj = alllist4.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("customer_phone").contains(conditions), "搜索联系电话=" + conditions + "，结果中包含" + obj.getString("customer_phone"));
                }
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC在线专家咨询列表单项搜索");
        }
    }

    @Test
    public void onlineExpertPCsearch3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            // 根据跟进时间搜索
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
                Preconditions.checkArgument(searchlist1.size() >= 1, "搜索跟进时间为" + followdate + ", 结果不正确");
                for (int j = 0; j < searchlist1.size(); j++) {
                    JSONObject obj1 = searchlist1.getJSONObject(j);
                    Preconditions.checkArgument(obj1.getString("follow_date").substring(0, 10).equals(followdate), "搜索" + followdate + " , 结果包含" + obj1.getString("follow_date").substring(0, 9));
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC在线专家咨询列表单项搜索");
        }
    }

    @Test
    public void onlineExpertPCsearch4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            // 根据咨询时间搜索
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
                Preconditions.checkArgument(searchlist1.size() >= 1, "搜索咨询时间为" + consult_date + ", 结果不正确");
                for (int j = 0; j < searchlist1.size(); j++) {
                    JSONObject obj1 = searchlist1.getJSONObject(j);
                    Preconditions.checkArgument(obj1.getString("consult_date").substring(0, 10).equals(consult_date), "搜索" + consult_date + " , 结果包含" + obj1.getString("consult_date").substring(0, 9));
                }
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC在线专家咨询列表单项搜索");
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
                Preconditions.checkArgument(code == 1000, mess + "提示" + obj.getMessage());
            }
            if (mess.contains("20001")) {
                Preconditions.checkArgument(code == 1001, mess + "提示" + obj.getMessage());
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC在线专家说明配置,校验字数");
        }
    }

    @Test
    public void onlineExpertPCExplain2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);

            String content1 = "<p style=\"text-align:center;\" size=\"5\" _root=\"[object Object]\" __ownerID=\"undefined\" __hash=\"" +
                    "undefined\" __altered=\"false\">居中</p><p><span style=\"font-size:32px\">放大</span></p><p><span style=\"font-si" +
                    "ze:12px\">缩小</span></p><p><strong>加粗</strong></p><p><em>斜体</em></p><p><u>下划线</u></p><p><span style=\"color:#f3" +
                    "2784\">颜色</span></p><p></p><p></p><div class=\"media-wrap image-wrap\"><img src=\"" + info.getLogoUrl() + "\"/></div><p></p><p></p>";

            Response obj = OnlineExpertsExplainEditScene.builder().content(content1).build().visitor(visitor).getResponse();
            int code = obj.getCode();

            Preconditions.checkArgument(code == 1000, "提示" + obj.getMessage());


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC在线专家说明配置,传图片");
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
            if (mess.contains("正常")) {
                Preconditions.checkArgument(code == 1000, mess + "提示语" + obj.getMessage());
            }
            if (mess.contains("异常")) {
                Preconditions.checkArgument(code == 1001, mess + "状态码" + code);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC在线专家/专属服务设置响应规则");
        }
    }

    @Test
    public void onlineExpertPCBanner() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            // todo 我觉得可以复用首页banner

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC在线专家页面配置");
        }
    }

    @Test(dataProvider = "ONLINEEXPERTREPLY")
    public void onlineExpertPCReply(String content, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //小程序消息列表数量
            int befApplet = info.getAppletmessNum();

            //小程序在线专家咨询
            info.submitonlineExpert();
            //PC在线专家回复
            util.loginPc(ALL_AUTHORITY);
            Long id1 = OnlineExpertsPageListScene.builder().page(1).size(5).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj1 = ReplyScene.builder().id(id1).content(content).build().visitor(visitor).getResponse();
            int code1 = obj1.getCode();

            //小程序专属销售服务咨询
            info.submitPreService();
            //PC回复
            util.loginPc(ALL_AUTHORITY);
            Long id2 = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj2 = ReplyScene.builder().id(id2).content(content).build().visitor(visitor).getResponse();
            int code2 = obj2.getCode();

            //小程序专属售后服务咨询
            info.submitAfterService();
            //PC回复
            util.loginPc(ALL_AUTHORITY);
            Long id3 = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj3 = ReplyScene.builder().id(id3).content(content).build().visitor(visitor).getResponse();
            int code3 = obj3.getCode();

            //小程序消息列表数量
            int afterApplet = info.getAppletmessNum();
            int sum = afterApplet - befApplet;

            if (mess.contains("正常")) {
                Preconditions.checkArgument(code1 == 1000, mess + "提示" + obj1.getMessage());
                Preconditions.checkArgument(code2 == 1000, mess + "提示" + obj2.getMessage());
                Preconditions.checkArgument(code3 == 1000, mess + "提示" + obj3.getMessage());
                Preconditions.checkArgument(sum == 6, "小程序咨询&PC回复后，小程序消息数量不正确");
            }
            if (mess.contains("异常")) {
                Preconditions.checkArgument(code1 == 1001, mess + "状态码" + code1);
                Preconditions.checkArgument(code2 == 1001, mess + "状态码" + code2);
                Preconditions.checkArgument(code3 == 1001, mess + "状态码" + code3);
                Preconditions.checkArgument(sum == 3, "小程序咨询后，消息数量不正确");
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC在线专家/专属服务 回复");
        }
    }

    @Test(dataProvider = "ONLINEEXPERTREMARK")
    public void onlineExpertPCRemark(String content, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //小程序消息列表数量
            int befApplet = info.getAppletmessNum();

            //小程序在线专家咨询
            info.submitonlineExpert();
            //PC在线专家备注
            util.loginPc(ALL_AUTHORITY);
            Long id1 = OnlineExpertsPageListScene.builder().page(1).size(5).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj1 = RemarkScene.builder().id(id1).remarkContent(content).build().visitor(visitor).getResponse();
            int code1 = obj1.getCode();

            //小程序专属销售服务咨询
            info.submitPreService();
            //PC备注
            util.loginPc(ALL_AUTHORITY);
            Long id2 = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj2 = RemarkScene.builder().id(id2).remarkContent(content).build().visitor(visitor).getResponse();
            int code2 = obj2.getCode();

            //小程序专属售后服务咨询
            info.submitAfterService();
            //PC备注
            util.loginPc(ALL_AUTHORITY);
            Long id3 = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj3 = RemarkScene.builder().id(id3).remarkContent(content).build().visitor(visitor).getResponse();
            int code3 = obj3.getCode();

            //小程序消息列表数量
            int afterApplet = info.getAppletmessNum();
            int sum = afterApplet - befApplet;

            if (mess.contains("正常")) {
                Preconditions.checkArgument(code1 == 1000, mess + "提示" + obj1.getMessage());
                Preconditions.checkArgument(code2 == 1000, mess + "提示" + obj2.getMessage());
                Preconditions.checkArgument(code3 == 1000, mess + "提示" + obj3.getMessage());
                Preconditions.checkArgument(sum == 3, "小程序咨询&PC备注后，小程序消息数量不正确");
            }
            if (mess.contains("异常")) {
                Preconditions.checkArgument(code1 == 1001, mess + "状态码" + code1);
                Preconditions.checkArgument(code2 == 1001, mess + "状态码" + code2);
                Preconditions.checkArgument(code3 == 1001, mess + "状态码" + code3);
                Preconditions.checkArgument(sum == 3, "小程序咨询后，消息数量不正确");
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC在线专家/专属服务 备注");
        }
    }

    @Test(dataProvider = "ONLINEEXPERTREPLY")
    public void onlineExpertAPPReply(String content, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //小程序消息列表数量
            int befApplet = info.getAppletmessNum();

            //小程序在线专家咨询
            info.submitonlineExpert();
            //PC在线专家回复
            util.loginApp(ALL_AUTHORITY);
            Long id1 = AppPageV3Scene.builder().type(info.ONLINE_EXPERTS).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj1 = AppReplyV3Scene.builder().followId(id1).content(content).build().visitor(visitor).getResponse();
            int code1 = obj1.getCode();

            //小程序专属销售服务咨询
            info.submitPreService();
            //PC回复
            util.loginApp(ALL_AUTHORITY);
            Long id2 = AppPageV3Scene.builder().type(info.SALES).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj2 = AppReplyV3Scene.builder().followId(id2).content(content).build().visitor(visitor).getResponse();
            int code2 = obj2.getCode();

            //小程序专属售后服务咨询
            info.submitAfterService();
            //PC回复
            util.loginApp(ALL_AUTHORITY);
            Long id3 = AppPageV3Scene.builder().type(info.AFTER_SALES).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj3 = AppReplyV3Scene.builder().followId(id3).content(content).build().visitor(visitor).getResponse();
            int code3 = obj3.getCode();

            //小程序消息列表数量
            int afterApplet = info.getAppletmessNum();
            int sum = afterApplet - befApplet;

            if (mess.contains("正常")) {
                Preconditions.checkArgument(code1 == 1000, mess + "提示" + obj1.getMessage());
                Preconditions.checkArgument(code2 == 1000, mess + "提示" + obj2.getMessage());
                Preconditions.checkArgument(code3 == 1000, mess + "提示" + obj3.getMessage());
                Preconditions.checkArgument(sum == 6, "小程序咨询&app回复后，小程序消息数量不正确");
            }
            if (mess.contains("异常")) {
                Preconditions.checkArgument(code1 == 1001, mess + "状态码" + code1);
                Preconditions.checkArgument(code2 == 1001, mess + "状态码" + code2);
                Preconditions.checkArgument(code3 == 1001, mess + "状态码" + code3);
                Preconditions.checkArgument(sum == 3, "小程序咨询后，消息数量不正确");
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app在线专家/专属服务 回复");
        }
    }

    @Test(dataProvider = "ONLINEEXPERTREMARK")
    public void onlineExpertAPPRemark(String content, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //小程序消息列表数量
            int befApplet = info.getAppletmessNum();

            //小程序在线专家咨询
            info.submitonlineExpert();
            //PC在线专家备注
            util.loginApp(ALL_AUTHORITY);
            Long id1 = AppPageV3Scene.builder().type(info.ONLINE_EXPERTS).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj1 = AppRemarkV3Scene.builder().followId(id1).remark(content).build().visitor(visitor).getResponse();
            int code1 = obj1.getCode();

            //小程序专属销售服务咨询
            info.submitPreService();
            //PC备注
            util.loginApp(ALL_AUTHORITY);
            Long id2 = AppPageV3Scene.builder().type(info.SALES).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj2 = AppRemarkV3Scene.builder().followId(id2).remark(content).build().visitor(visitor).getResponse();
            int code2 = obj2.getCode();

            //小程序专属售后服务咨询
            info.submitAfterService();
            //PC备注
            util.loginApp(ALL_AUTHORITY);
            Long id3 = AppPageV3Scene.builder().type(info.AFTER_SALES).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Response obj3 = AppRemarkV3Scene.builder().followId(id3).remark(content).build().visitor(visitor).getResponse();
            int code3 = obj3.getCode();

            //小程序消息列表数量
            int afterApplet = info.getAppletmessNum();
            int sum = afterApplet - befApplet;

            if (mess.contains("正常")) {
                Preconditions.checkArgument(code1 == 1000, mess + "提示" + obj1.getMessage());
                Preconditions.checkArgument(code2 == 1000, mess + "提示" + obj2.getMessage());
                Preconditions.checkArgument(code3 == 1000, mess + "提示" + obj3.getMessage());
                Preconditions.checkArgument(sum == 3, "小程序咨询&app备注后，小程序消息数量不正确");
            }
            if (mess.contains("异常")) {
                Preconditions.checkArgument(code1 == 1001, mess + "状态码" + code1);
                Preconditions.checkArgument(code2 == 1001, mess + "状态码" + code2);
                Preconditions.checkArgument(code3 == 1001, mess + "状态码" + code3);
                Preconditions.checkArgument(sum == 3, "小程序咨询后，消息数量不正确");
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app在线专家/专属服务 备注");
        }
    }


    /**
     * -----------------------------专属服务咨询------------------------------------
     */

    @Test(dataProvider = "ONLINEEXPERTINFO")
    public void preServiceSubmit1(String customerName, String customerPhone, String content, String mess, String status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Long brandId = info.BrandID;
            Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(info.CarStyleID).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("shop_id");
            String salesId = AppletConsultDedicatedServiceSalesListScene.builder().build().visitor(visitor).execute().getJSONArray("sales_list").getJSONObject(0).getString("sales_id");//销售
            String aftersalesId = AppletConsultDedicatedServiceSalesListScene.builder().build().visitor(visitor).execute().getJSONArray("after_sales_list").getJSONObject(0).getString("sales_id");//售后

            Response obj = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code = obj.getCode();
            if (status.equals("false")) {
                String message = obj.getMessage();
                Preconditions.checkArgument(code == 1001, mess + ", 状态码为:" + code + ", 提示语为:" + message);
            }
            if (status.equals("true")) {
                Preconditions.checkArgument(code == 1000, mess + ", 状态码为:" + code);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交专属销售顾问咨询，校验必填项内容");
        }
    }

    @Test(dataProvider = "ONLINEEXPERTINFO")
    public void preServiceSubmit11(String customerName, String customerPhone, String content, String mess, String status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Long brandId = info.BrandID;
            Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(info.CarStyleID).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("shop_id");
            String aftersalesId = AppletConsultDedicatedServiceSalesListScene.builder().build().visitor(visitor).execute().getJSONArray("after_sales_list").getJSONObject(0).getString("sales_id");//售后

            Response obj = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(aftersalesId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code = obj.getCode();
            if (status.equals("false")) {
                String message = obj.getMessage();
                Preconditions.checkArgument(code == 1001, mess + ", 状态码为:" + code + ", 提示语为:" + message);
            }
            if (status.equals("true")) {
                Preconditions.checkArgument(code == 1000, mess + ", 状态码为:" + code);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交专属售后顾问咨询，校验必填项内容");
        }
    }

    @Test
    public void preServiceSubmit2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long brandId = info.BrandID;
            Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(info.CarStyleID).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("shop_id");
            String salesId = AppletConsultDedicatedServiceSalesListScene.builder().build().visitor(visitor).execute().getJSONArray("after_sales_list").getJSONObject(0).getString("sales_id");//售后

            String customerName = "奶糖";
            String customerPhone = "13811110000";
            String content = "12345678901234567890";

            Response obj1 = AppletConsultPreServiceSubmitScene.builder().customerPhone(customerPhone).content(content)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code1 = obj1.getCode();
            String message = obj1.getMessage();
            Preconditions.checkArgument(code1 == 1001, "不填写姓名，状态码为:" + code1 + ", 提示语为:" + message);

            Response obj2 = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).content(content)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code2 = obj2.getCode();
            String message2 = obj2.getMessage();
            Preconditions.checkArgument(code2 == 1001, "不填写手机号，状态码为:" + code2 + ", 提示语为:" + message2);

            Response obj3 = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code3 = obj3.getCode();
            String message3 = obj3.getMessage();
            Preconditions.checkArgument(code3 == 1001, "不填写留言，状态码为:" + code3 + ", 提示语为:" + message3);

            Response obj4 = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code4 = obj4.getCode();
            String message4 = obj4.getMessage();
            Preconditions.checkArgument(code4 == 1001, "不选择销售，状态码为:" + code4 + ", 提示语为:" + message4);

            Response obj5 = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(salesId).shopId(shopId).build().visitor(visitor).getResponse();
            int code5 = obj5.getCode();
            String message5 = obj5.getMessage();
            Preconditions.checkArgument(code5 == 1001, "不填写车型，状态码为:" + code5 + ", 提示语为:" + message5);

            Response obj6 = AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(salesId).modelId(modelId).build().visitor(visitor).getResponse();
            int code6 = obj6.getCode();
            String message6 = obj6.getMessage();
            Preconditions.checkArgument(code6 == 1001, "不填写咨询门店，状态码为:" + code6 + ", 提示语为:" + message6);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交专属售后顾问咨询，不填写必填项");
        }
    }

    @Test
    public void preServiceSubmit3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取小程序消息列表数量
            int befApplet = info.getAppletmessNum();
            //获取app跟进列表数量
            util.loginApp(ALL_AUTHORITY);
            int befApp = AppPageV3Scene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            //PC【专属服务列表】
            util.loginPc(ALL_AUTHORITY);
            int befPC = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");

            //小程序提交销售咨询
            JSONObject submitobj = info.submitPreService();
            String name = submitobj.getString("customerName");
            String phone = submitobj.getString("customerPhone");
            String brandId = submitobj.getString("brandId");
//            String modelId = submitobj.getString("modelId");
            String shopId = submitobj.getString("shopName");

            //获取小程序消息列表数量
            int afterApplet = info.getAppletmessNum();
            //获取app跟进列表数量
            util.loginApp(ALL_AUTHORITY);
            int afterApp = AppPageV3Scene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            //PC【专属服务列表】
            util.loginPc(ALL_AUTHORITY);
            JSONObject obj = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
            int afterPC = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");


            int applet = afterApplet - befApplet;
            int app = afterApp - befApp;
            int pc = afterPC - befPC;
            Preconditions.checkArgument(applet == 1, "小程序消息列表未+1");
            Preconditions.checkArgument(app == 1, "app跟进列表未+1");
            Preconditions.checkArgument(pc == 1, "PC【专属服务】列表未+1");
            if (pc == 1) {
                String customer_name = obj.getString("customer_name");
                String customer_phone = obj.getString("customer_phone");
                String shop_name = obj.getString("shop_name");
                String brand_name = obj.getString("brand_name");
//                String model_name = obj.getString("model_name");
                Preconditions.checkArgument(customer_name.equals(name), "提交时姓名" + name + ", PC展示" + customer_name);
                Preconditions.checkArgument(customer_phone.equals(phone), "提交时手机号" + phone + ", PC展示" + customer_phone);
                Preconditions.checkArgument(shop_name.equals(shopId), "提交时选择的门店" + shopId + ", PC展示" + shop_name);
                Preconditions.checkArgument(brand_name.equals(brandId), "提交时选择的品牌" + brandId + ", PC展示" + brand_name);
//                Preconditions.checkArgument(model_name.equals(modelId),"提交时选择的品牌"+modelId+", PC展示"+model_name);
                Preconditions.checkArgument(obj.getString("sale_type").contains("销售"), "提交时销售, PC展示" + obj.getString("sale_type"));

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交专属销售咨询，结果校验：app产生跟进&小程序收到自动回复消息&PC【专属销售】列表展示");
        }
    }


    @Test(dataProvider = "ONLINEEXPERTINFO")
    public void afterServiceSubmit1(String customerName, String customerPhone, String content, String mess, String status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Long brandId = info.BrandID;
            Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(info.CarStyleID).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("shop_id");

            String salesId = AppletConsultDedicatedServiceSalesListScene.builder().build().visitor(visitor).execute().getJSONArray("after_sales_list").getJSONObject(0).getString("sales_id");

            Response obj = AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code = obj.getCode();
            if (status.equals("false")) {
                String message = obj.getMessage();
                Preconditions.checkArgument(code == 1001, mess + ", 状态码为:" + code + ", 提示语为:" + message);
            }
            if (status.equals("true")) {
                Preconditions.checkArgument(code == 1000, mess + ", 状态码为:" + code);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交专属售后顾问咨询，校验必填项内容");
        }
    }

    @Test
    public void afterServiceSubmit2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long brandId = info.BrandID;
            Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(info.CarStyleID).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("shop_id");

            String salesId = AppletConsultDedicatedServiceSalesListScene.builder().build().visitor(visitor).execute().getJSONArray("after_sales_list").getJSONObject(0).getString("sales_id");

            String customerName = "奶糖";
            String customerPhone = "13811110000";
            String content = "12345678901234567890";

            Response obj1 = AppletConsultAfterServiceSubmitScene.builder().customerPhone(customerPhone).content(content)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code1 = obj1.getCode();
            String message = obj1.getMessage();
            Preconditions.checkArgument(code1 == 1001, "不填写姓名，状态码为:" + code1 + ", 提示语为:" + message);

            Response obj2 = AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).content(content)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code2 = obj2.getCode();
            String message2 = obj2.getMessage();
            Preconditions.checkArgument(code2 == 1001, "不填写手机号，状态码为:" + code2 + ", 提示语为:" + message2);

            Response obj3 = AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone)
                    .salesId(salesId).modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code3 = obj3.getCode();
            String message3 = obj3.getMessage();
            Preconditions.checkArgument(code3 == 1001, "不填写留言，状态码为:" + code3 + ", 提示语为:" + message3);

            Response obj4 = AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .modelId(modelId).shopId(shopId).build().visitor(visitor).getResponse();
            int code4 = obj4.getCode();
            String message4 = obj4.getMessage();
            Preconditions.checkArgument(code4 == 1001, "不选择顾问，状态码为:" + code4 + ", 提示语为:" + message4);

            Response obj5 = AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(salesId).shopId(shopId).build().visitor(visitor).getResponse();
            int code5 = obj5.getCode();
            String message5 = obj5.getMessage();
            Preconditions.checkArgument(code5 == 1001, "不填写车型，状态码为:" + code5 + ", 提示语为:" + message5);

            Response obj6 = AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                    .salesId(salesId).modelId(modelId).build().visitor(visitor).getResponse();
            int code6 = obj6.getCode();
            String message6 = obj6.getMessage();
            Preconditions.checkArgument(code6 == 1001, "不填写咨询门店，状态码为:" + code6 + ", 提示语为:" + message6);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交专属售后顾问咨询，不填写必填项");
        }
    }

    @Test
    public void afterServiceSubmit3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取小程序消息列表数量
            int befApplet = info.getAppletmessNum();
            //获取app跟进列表数量
            util.loginApp(ALL_AUTHORITY);
            int befApp = AppPageV3Scene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            //PC【专属服务列表】
            util.loginPc(ALL_AUTHORITY);
            int befPC = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");

            //小程序提交售后咨询
            JSONObject submitobj = info.submitAfterService();
            String name = submitobj.getString("customerName");
            String phone = submitobj.getString("customerPhone");
            String brandId = submitobj.getString("brandId");
//            String modelId = submitobj.getString("modelId");
            String shopId = submitobj.getString("shopName");


            Thread.sleep(1000);
            //获取app跟进列表数量
            util.loginApp(ALL_AUTHORITY);
            int afterApp = AppPageV3Scene.builder().size(10).build().visitor(visitor).execute().getInteger("total");
            //获取小程序消息列表数量
            int afterApplet = info.getAppletmessNum();
            //PC【专属服务列表】
            util.loginPc(ALL_AUTHORITY);

            JSONObject obj = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
            int afterPC = DedicatedServicePageListScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");

            int applet = afterApplet - befApplet;
            int app = afterApp - befApp;
            int pc = afterPC - befPC;
            Preconditions.checkArgument(applet == 1, "小程序消息列表未+1");
            Preconditions.checkArgument(app == 1, "app跟进列表未+1");
            Preconditions.checkArgument(pc == 1, "PC【专属服务】列表未+1");
            if (pc == 1) {
                String customer_name = obj.getString("customer_name");
                String customer_phone = obj.getString("customer_phone");
                String shop_name = obj.getString("shop_name");
                String brand_name = obj.getString("brand_name");
//                String model_name = obj.getString("model_name");
                Preconditions.checkArgument(customer_name.equals(name), "提交时姓名" + name + ", PC展示" + customer_name);
                Preconditions.checkArgument(customer_phone.equals(phone), "提交时手机号" + phone + ", PC展示" + customer_phone);
                Preconditions.checkArgument(shop_name.equals(shopId), "提交时选择的门店" + shopId + ", PC展示" + shop_name);
                Preconditions.checkArgument(brand_name.equals(brandId), "提交时选择的品牌" + brandId + ", PC展示" + brand_name);
//                Preconditions.checkArgument(model_name.equals(modelId),"提交时选择的品牌"+modelId+", PC展示"+model_name);
                Preconditions.checkArgument(obj.getString("sale_type").contains("售后"), "提交时售后, PC展示" + obj.getString("sale_type"));

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序提交专属售后咨询，结果校验：app产生跟进&小程序收到自动回复消息&PC【专属销售】列表展示");
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
                Preconditions.checkArgument(code2 == 1000, mess + "提示" + obj2.getMessage());
            }
            if (mess.contains("20001")) {
                Preconditions.checkArgument(code2 == 1001, mess + "提示" + obj2.getMessage());
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC专属服务说明配置,校验字数");
        }
    }

    @Test
    public void dedicatedServicePCExplain2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);

            String content1 = "<p style=\"text-align:center;\" size=\"5\" _root=\"[object Object]\" __ownerID=\"undefined\" __hash=\"" +
                    "undefined\" __altered=\"false\">专属服务居中</p><p><span style=\"font-size:32px\">专属服务放大</span></p><p><span style=\"font-si" +
                    "ze:12px\">缩小</span></p><p><strong>专属服务加粗</strong></p><p><em>专属服务斜体</em></p><p><u>专属服务下划线</u></p><p><span style=\"color:#f3" +
                    "2784\">专属服务颜色</span></p><p></p><p></p><div class=\"media-wrap image-wrap\"><img src=\"" + info.getLogoUrl() + "\"/></div><p></p><p></p>";

            Response obj = DedicatedServiceExplainEditScene.builder().content(content1).build().visitor(visitor).getResponse();
            int code = obj.getCode();

            Preconditions.checkArgument(code == 1000, "提示" + obj.getMessage());


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC专属服务说明配置,传图片");
        }
    }


    @Test
    public void dedicatedServicePCsearch1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            //根据归属门店搜索
            JSONObject shopobj = ShopListScene.builder().build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
            Long shopId = shopobj.getLong("shop_id");
            String shopName = shopobj.getString("shop_name");
            JSONArray alllist = DedicatedServicePageListScene.builder().page(1).size(20).shopId(shopId).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist.size() > 0) {
                for (int i = 0; i < alllist.size(); i++) {
                    JSONObject obj = alllist.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("shop_name").equals(shopName), "搜索归属门店=" + shopName + "，结果中包含" + obj.getString("shop_name"));
                }
            }

            //根据车系搜索
            JSONObject styleobj = PreSaleCustomerStyleListScene.builder().shopId(-1L).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
            Long styleId = styleobj.getLong("style_id");
            String styleName = styleobj.getString("style_name");
            JSONArray alllist3 = DedicatedServicePageListScene.builder().page(1).size(20).carStyleId(styleId).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist3.size() > 0) {
                for (int i = 0; i < alllist3.size(); i++) {
                    JSONObject obj = alllist3.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("style_name").equals(styleName), "搜索车系=" + styleName + "，结果中包含" + obj.getString("style_name"));
                }
            }

            //根据是否超时=是搜索
            JSONArray alllist2 = DedicatedServicePageListScene.builder().page(1).size(20).isOverTime(true).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist2.size() > 0) {
                for (int i = 0; i < alllist2.size(); i++) {
                    JSONObject obj = alllist2.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("is_over_time").equals("是"), "搜索是否超时=是，结果中包含" + obj.getString("is_over_time"));
                }
            }

            //根据是否超时=否搜索
            JSONArray alllist4 = DedicatedServicePageListScene.builder().page(1).size(20).isOverTime(false).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist4.size() > 0) {
                for (int i = 0; i < alllist4.size(); i++) {
                    JSONObject obj = alllist4.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("is_over_time").equals("否"), "搜索是否超时=否，结果中包含" + obj.getString("is_over_time"));
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC专属服务列表单项搜索");
        }
    }

    @Test(dataProvider = "ONLINEEXPERTSEARCH")
    public void dedicatedServicePCsearch2(String conditions) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);

            //根据跟进账号搜索
            JSONArray alllist = DedicatedServicePageListScene.builder().page(1).size(20).followLoginName(conditions).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist.size() > 0) {
                for (int i = 0; i < alllist.size(); i++) {
                    JSONObject obj = alllist.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("follow_login_name").contains(conditions), "搜索跟进账号=" + conditions + "，结果中包含" + obj.getString("follow_login_name"));
                }
            }

            //根据跟进人员搜索
            JSONArray alllist2 = DedicatedServicePageListScene.builder().page(1).size(20).followSalesName(conditions).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist2.size() > 0) {
                for (int i = 0; i < alllist2.size(); i++) {
                    JSONObject obj = alllist2.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("follow_sales_name").contains(conditions), "搜索跟进人员=" + conditions + "，结果中包含" + obj.getString("follow_sales_name"));
                }
            }

            //根据联系人搜索
            JSONArray alllist3 = DedicatedServicePageListScene.builder().page(1).size(20).customerName(conditions).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist3.size() > 0) {
                for (int i = 0; i < alllist3.size(); i++) {
                    JSONObject obj = alllist3.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("customer_name").contains(conditions), "搜索联系人=" + conditions + "，结果中包含" + obj.getString("customer_name"));
                }
            }

            //根据联系电话搜索
            JSONArray alllist4 = DedicatedServicePageListScene.builder().page(1).size(20).customerPhone(conditions).build().visitor(visitor).execute().getJSONArray("list");
            if (alllist4.size() > 0) {
                for (int i = 0; i < alllist4.size(); i++) {
                    JSONObject obj = alllist4.getJSONObject(i);
                    Preconditions.checkArgument(obj.getString("customer_phone").contains(conditions), "搜索联系电话=" + conditions + "，结果中包含" + obj.getString("customer_phone"));
                }
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC专属服务列表单项搜索");
        }
    }

    @Test
    public void dedicatedServicePCsearch3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            // 根据跟进时间搜索
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
                Preconditions.checkArgument(searchlist1.size() >= 1, "搜索跟进时间为" + followdate + ", 结果不正确");
                for (int j = 0; j < searchlist1.size(); j++) {
                    JSONObject obj1 = searchlist1.getJSONObject(j);
                    Preconditions.checkArgument(obj1.getString("follow_date").substring(0, 10).equals(followdate), "搜索" + followdate + " , 结果包含" + obj1.getString("follow_date").substring(0, 9));
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC专属服务列表单项搜索");
        }
    }

    @Test
    public void dedicatedServicePCsearch4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            // 根据咨询时间搜索
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
                Preconditions.checkArgument(searchlist1.size() >= 1, "搜索咨询时间为" + consult_date + ", 结果不正确");
                for (int j = 0; j < searchlist1.size(); j++) {
                    JSONObject obj1 = searchlist1.getJSONObject(j);
                    Preconditions.checkArgument(obj1.getString("consult_date").substring(0, 10).equals(consult_date), "搜索" + consult_date + " , 结果包含" + obj1.getString("consult_date").substring(0, 9));
                }
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC专属服务列表单项搜索");
        }
    }


    /* -----------------------------------V3.1导出---------------------------------------------------*/

    //@Test(dataProvider = "EXPORT1")
    public void Export1(String type, String mess) {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            int code = RecordExportScene.builder().type(type).page(1).size(10).exportType("CURRENT_PAGE").build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(code == 1000, mess + "导出状态码为" + code);
            Thread.sleep(800);
            String status = ExportPageScene.builder().page(1).size(1).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getString("status_name");
            Preconditions.checkArgument(status.equals("导出完成"), mess + " " + status);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出");
        }
    }

    //@Test(dataProvider = "EXPORT2")
    public void Export2(String type, String mess) {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            int code = EvaluateExportScene.builder().evaluateType(Integer.parseInt(type)).page(1).size(10).exportType("CURRENT_PAGE").build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(code == 1000, mess + "导出状态码为" + code);
            Thread.sleep(800);
            String status = ExportPageScene.builder().page(1).size(1).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getString("status_name");
            Preconditions.checkArgument(status.equals("导出完成"), mess + " " + status);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出");
        }
    }


    /* -----------------------------------V3.1门店---------------------------------------------------*/


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
//            Preconditions.checkArgument(code==1000,"期待状态码1000，实际"+ code);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("PC【门店管理】，新建门店");
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
            Preconditions.checkArgument(code == 1001, "期待状态码1001，实际" + code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【门店管理】，新建门店时品牌不存在");
        }
    }


    //PC 创建成交记录
    @Test
    public void evalute_Buycar1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginPc(ALL_AUTHORITY);
            //PC配置销售购车评价奖励
            int type = 3;
            String messageName = "新车评价消息";
            int points = info.setevaluate(type, messageName).getInteger("points");


            //评价前的积分和卡券数
            int voucherTotal = info.getVoucherTotal("13436941018");
            util.loginApplet(APPLET_USER_ONE);
            Integer appletScore = jc.appletUserInfoDetail().getInteger("score");

            //PC购车
            util.loginPc(ALL_AUTHORITY);
            info.newBuyCarRec();
            util.loginApplet(APPLET_USER_ONE);
            JSONObject evaluateConfigDescribe = jc.AppletEvaluateConfigScene(type, Long.parseLong(pp.shopIdZ));
            String describe = evaluateConfigDescribe.getJSONArray("list").getJSONObject(2).getString("describe");
            List label = evaluateConfigDescribe.getJSONArray("list").getJSONObject(2).getJSONArray("labels");


            //评价

            IScene evaluatesubmit = AppletEvaluateSubmitScene.builder()
                    .describe(describe).labels(label).id(info.getMessDetailId())
                    .isAnonymous(true)
                    .score(2)
                    .shopId(info.oneshopid).suggestion("自动评价").type(type)
                    .build();
            jc.invokeApi(evaluatesubmit);

            //评价后的积分和卡券数

            Integer appletScoreAfter = jc.appletUserInfoDetail().getInteger("score");
            util.loginPc(ALL_AUTHORITY);
            int voucherTotalAfter = info.getVoucherTotal("13436941018");

            Preconditions.checkArgument(voucherTotalAfter - voucherTotal == 1, "评价完成后，卡券没+1");
            Preconditions.checkArgument(appletScoreAfter - appletScore == points, "评价完成后，积分奖励没发");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC配置购车奖励->小程序评价购车消息获得相应奖励");
        }
    }


    //新建销售接待， 先注释掉
//    @Test
//    public void evalute_reception() {
//        logger.logCaseStart(caseResult.getCase       try {
//            util.loginPc(ALL_AUTHORITY);
//            //PC配置销售接待评价奖励
//            int type = 4;
//            String messageName = "新车评价消息"; //手动阀
//            int points = info.setevaluate(type,messageName).getInteger("points");
//
//
//            //评价前的积分和卡券数
//            int voucherTotal = info.getVoucherTotal("13436941018");
//            util.loginApplet(APPLET_USER_ONE);
//            Integer appletScore = jc.appletUserInfoDetail().getInteger("score");
//
//            //app接待
//            util.loginApp(ALL_AUTHORITY_DAILY);
////            commonConfig.shopId=info.oneshopid.toString();
//            commonConfig.shopId="53704";
//            commonConfig.roleId="2942";
//
//            Long recid = Long.valueOf(info.salereception("13436941018")[0]);
//
//            IScene appfinishReception = AppFinishReceptionScene.builder()
////                    .shopId(Long.valueOf(info.oneshopid))
//                    .shopId(53704L)
//                    .id(recid).build();
//
//            jc.invokeApi(appfinishReception);
//
//            util.loginApplet(APPLET_USER_ONE);
//            JSONObject evaluateConfigDescribe= jc.AppletEvaluateConfigScene(type,Long.parseLong(pp.shopIdZ));
//            String describe=evaluateConfigDescribe.getJSONArray("list").getJSONObject(2).getString("describe");
//            List label=evaluateConfigDescribe.getJSONArray("list").getJSONObject(2).getJSONArray("labels");
//
//
//            //评价
//
//            IScene evaluatesubmit= AppletEvaluateSubmitScene.builder()
//                    .describe(describe).labels(label).id(info.getMessDetailId())
//                    .isAnonymous(true)
//                    .score(2)
////                    .shopId(info.oneshopid).suggestion("自动评价").type(type)
//                    .shopId(53704L).suggestion("自动评价").type(type)
//                    .build();
//            jc.invokeApi(evaluatesubmit);
//
//            //评价后的积分和卡券数
//
//            Integer appletScoreAfter= jc.appletUserInfoDetail().getInteger("score");
//            util.loginPc(ALL_AUTHORITY);
//            commonConfig.shopId=PRODUCE.getShopId();
//            commonConfig.roleId=PRODUCE.getRoleId();
//            int voucherTotalAfter = info.getVoucherTotal("13436941018");
//
//            Preconditions.checkArgument(voucherTotalAfter-voucherTotal==1,"评价完成后，卡券没+1");
//            Preconditions.checkArgument(appletScoreAfter-appletScore==points,"评价完成后，积分奖励没发");
//
//
//        } catch (AssertionError | Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("app接待->小程序评价销售接待消息获得相应奖励");
//        }
//    }


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

            //小程序删除无牌爱车
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
            saveData("PC临时备注");
        }
    }


    /**
     * -----------------------------------dataProvider来这里排排站---------------------------------------------------
     */
    @DataProvider(name = "ONLINEEXPERTINFO")
    public Object[] onlineExpertrInfo() {
        return new String[][]{ // 姓名 手机号 咨询内容(10-200)  提示语 正常/异常

                {"吕", "1382172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), "这是10geZI！@", "姓名1个字&咨询内容10个字(期待成功)", "true"},
                {info.stringfifty, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), info.string200, "姓名50个字&咨询内容200个字(期待成功)", "true"},
                {info.stringsix, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 100)), info.stringfifty, "手机号10位(期待失败)", "false"},
                {info.stringsix, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), info.stringfifty, "手机号12位(期待失败)", "false"},
                {info.stringfifty + "1", "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), info.stringfifty, "姓名51位(期待失败)", "false"},
                {info.stringsix, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), "这是9geZI！@", "咨询内容9个字(期待失败)", "false"},
                {info.stringsix, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), info.string200 + "1", "咨询内容201个字(期待失败)", "false"},

        };
    }

    @DataProvider(name = "ONLINEEXPERTSEARCH")
    public Object[] onlineExpertrSearch() {
        return new String[]{
                "1",
                "a",
                "预约",
                "测试",
        };
    }

    @DataProvider(name = "ONLINEEXPERTEXPLAIN")
    public Object[] onlineExpertrExplain() {
        return new String[][]{

                {"<p>啊啊啊啊啊<span style=\\\"font-size:120px\\\">51234  </span></p>", "调整字体大小"},
                {info.string200 + info.string200 + info.string200 + info.string200 + info.string200 + info.string200 + info.string200 + info.string200 + info.string200 + info.string200, "2000字"},//2000字
                {info.getString(20000), "20000字"},
                {info.getString(20001), "20001字"},

        };
    }

    @DataProvider(name = "ONLINEEXPERTRULE")
    public Object[] onlineExpertrRULE() {
        return new String[][]{ //提醒时间；超时时间；工作日上午开始上午结束，下午开始下午结束；休息日上午开始上午结束，下午开始下午结束

                //在线专家
                {"ONLINE_EXPERTS", "1", "1", "11:00", "12:00", "13:00", "15:00", "08:00", "09:00", "22:00", "23:00", "在线专家正常"},
                {"ONLINE_EXPERTS", "720", "720", "08:00", "09:00", "13:00", "15:00", "01:00", "02:00", "13:00", "15:00", "在线专家正常"},
                {"ONLINE_EXPERTS", "720", "720", "00:00", "12:00", "12:00", "23:59", "00:00", "12:00", "12:00", "23:59", "在线专家正常"},
                {"ONLINE_EXPERTS", "721", "720", "08:00", "09:00", "13:00", "15:00", "01:00", "02:00", "13:00", "15:00", "在线专家异常：提醒时间721分钟"},
                {"ONLINE_EXPERTS", "60", "721", "08:00", "09:00", "13:00", "15:00", "01:00", "02:00", "13:00", "15:00", "在线专家异常：超时时间721分钟"},
                {"ONLINE_EXPERTS", "60", "60", "08:00", "07:00", "13:00", "15:00", "01:00", "02:00", "13:00", "15:00", "在线专家异常：工作日上午开始时间>结束时间"},
                {"ONLINE_EXPERTS", "60", "60", "08:00", "09:00", "15:00", "13:00", "01:00", "02:00", "13:00", "15:00", "在线专家异常：工作日下午开始时间>结束时间"},
                {"ONLINE_EXPERTS", "60", "60", "08:00", "09:00", "13:00", "15:00", "08:00", "07:00", "13:00", "15:00", "在线专家异常：休息日上午开始时间>结束时间"},
                {"ONLINE_EXPERTS", "60", "60", "08:00", "09:00", "13:00", "15:00", "01:00", "02:00", "15:00", "13:00", "在线专家异常：休息日下午开始时间>结束时间"},

                //专属服务
                {"SALES", "1", "1", "11:00", "12:00", "13:00", "15:00", "08:00", "09:00", "22:00", "23:00", "专属服务正常"},
                {"SALES", "720", "720", "08:00", "09:00", "13:00", "15:00", "01:00", "02:00", "13:00", "15:00", "专属服务正常"},
                {"SALES", "60", "60", "00:00", "12:00", "12:00", "23:59", "00:00", "12:00", "12:00", "23:59", "专属服务正常"},
                {"SALES", "721", "720", "08:00", "09:00", "13:00", "15:00", "01:00", "02:00", "13:00", "15:00", "专属服务异常：提醒时间721分钟"},
                {"SALES", "60", "721", "08:00", "09:00", "13:00", "15:00", "01:00", "02:00", "13:00", "15:00", "专属服务异常：超时时间721分钟"},
                {"SALES", "60", "60", "08:00", "07:00", "13:00", "15:00", "01:00", "02:00", "13:00", "15:00", "专属服务异常：工作日上午开始时间>结束时间"},
                {"SALES", "60", "60", "08:00", "09:00", "15:00", "13:00", "01:00", "02:00", "13:00", "15:00", "专属服务异常：工作日下午开始时间>结束时间"},
                {"SALES", "60", "60", "08:00", "09:00", "13:00", "15:00", "08:00", "07:00", "13:00", "15:00", "专属服务异常：休息日上午开始时间>结束时间"},
                {"SALES", "60", "60", "08:00", "09:00", "13:00", "15:00", "01:00", "02:00", "15:00", "13:00", "专属服务异常：休息日下午开始时间>结束时间"},


        };
    }

    @DataProvider(name = "ONLINEEXPERTREPLY")
    public Object[] onlineExpertrReply() {
        return new String[][]{

                {"1啊A！5", "正常回复5个字"},
                {info.getString(1000), "正常回复1000个字"},
                {info.string200, "正常回复200个字"},
                {"1234", "异常回复4个字"},
                {info.getString(1001), "异常回复1001个字"},

        };
    }

    @DataProvider(name = "ONLINEEXPERTREMARK")
    public Object[] onlineExpertrRemark() {
        return new String[][]{

                {"啊1234", "正常备注5个字"},
                {info.string20, "正常备注20个字"},
                {info.string200, "正常备注200个字"},
                {info.getString(1000), "正常备注1000个字"},
                {info.getString(1001), "异常备注1001个字"},
                {info.getString(4), "异常备注4个字"},

        };
    }

    @DataProvider(name = "EXPORT1")
    public Object[] export1() {
        return new String[][]{
                {"REPAIR", "预约维修记录"},
                {"MAINTAIN", "预约保养记录"},
                {"TEST_DRIVE", "预约试驾记录"},

        };
    }

    @DataProvider(name = "EXPORT2")
    public Object[] export2() {
        return new String[][]{
                {"3", "销售购车评价"},
                {"1", "预约保养评价"},
                {"2", "预约维修评价"},
                {"4", "销售接待评价"},

        };
    }

    @DataProvider(name = "SHOP")
    public Object[] shop() {

        return new String[][]{
//                {info.stringone, info.stringone,info.district_code,info.stringone, info.sitphone1,info.sitphone2,"129.8439","42.96805","ENABLE","ENABLE"}, //一个字符太少了 注视掉 每次需要更改
//                {info.stringone, info.stringten,info.district_code,info.stringfifty, info.phone,info.phone,"129.8439","42.96805","ENABLE","DISABLE"},
//                {info.stringten, info.stringone,info.district_code,info.stringten, info.phone,info.phone,"129.8439","42.96805","DISABLE","ENABLE"},
                {info.stringten, info.stringfifty, info.district_code, info.stringone, info.sitphone1, info.phone, "129.8439", "42.96805", "DISABLE", "DISABLE"},
//                {info.stringone, info.stringfifty,info.district_code,info.stringten, info.phone,info.sitphone2,"129.8439","42.96805","DISABLE","DISABLE"},

        };
    }

    @DataProvider(name = "EVALUATETYPE")
    public static Object[][] evaluatetype() {
        return new String[][]{
                {"1", "保养评价消息"},
                {"2", "维修评价消息"}
        };
    }
}
