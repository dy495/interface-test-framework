package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.gly;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.BusinessUtil;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.Constant;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.YunTongUtil;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.lxq.SystemCase;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerBuyCarPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerEditScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerInfoScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanagev4.PreSaleCustomerInfoBuyCarRecordScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

public class SalesManagementCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct product = EnumTestProduct.YT_DAILY_JD;
    private static final EnumAccount account = EnumAccount.YT_DAILY_YS;
    YunTongUtil yt = new YunTongUtil(product);
    private final VisitorProxy visitor = new VisitorProxy(product);
    private final SceneUtil util = new SceneUtil(visitor);
    BusinessUtil businessUtil = new BusinessUtil(visitor);
    SystemCase systemCase = new SystemCase();


    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.GLY.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.setShopId(product.getShopId()).setRoleId(product.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);
        util.loginPc(account);
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
        logger.logCaseStart(caseResult.getCaseName());
    }

    /**
     * @date :2021-6-4
     * @deprecated 展厅客户列表-筛选栏单项搜索
     */
    @Test(dataProvider = "SELECT_preSalesReceptionPageRecordFilter", dataProviderClass = Constant.class, description = "展厅客户列表-筛选栏单项搜索")
    public void preSaleCustomerPage1(String pram, String output) {
        try {
            IScene scene = PreSaleCustomerPageScene.builder().build();
            JSONObject rsp = scene.visitor(visitor).execute();
            if (rsp.getJSONArray("list").size() > 0) {
                String outPutResult = rsp.getJSONArray("list").getJSONObject(0).getString(output);
                scene = scene.modify(pram, outPutResult);
                List<JSONObject> searchResultList = util.toJavaObjectList(scene, JSONObject.class);
                searchResultList.forEach(e -> {
                    String searchResult = e.getString(output);
                    Preconditions.checkArgument(searchResult.contains(outPutResult), "销售客户接待列表按" + outPutResult + "查询，结果中包含错误结果" + searchResult);
                });
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("销售客户接待列表-筛选栏单项搜索 ");
        }
    }

    /**
     * @date :2021-06-08
     * @deprecated 展厅客户列表-筛选栏时间搜索
     */
    @Test(description = "展厅客户列表-筛选栏时间搜索")
    public void preSaleCustomerPage2() {
        try {
            String startTime = DateTimeUtil.addDayFormat(new Date(), -30);
            String endTime = DateTimeUtil.addDayFormat(new Date(), 30);
            IScene scene = PreSaleCustomerPageScene.builder().startTime(startTime).endTime(endTime).build();
            List<JSONObject> list = util.toJavaObjectList(scene, JSONObject.class);
            list.forEach(e -> {
                String createDate = e.getString("create_date");
                Preconditions.checkArgument(startTime.compareTo(createDate) < 0 && endTime.compareTo(createDate) > 0, "开始时间：" + startTime + " 结束时间：" + endTime + "列表中创建时间时间:" + createDate);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData(" 销售接待列表-筛选栏时间搜索 ");
        }
    }

    /**
     * 销售客户列表项校验
     */
    @Test(description = "销售客户列表项校验")
    public void preSaleCustomerPage3() {
        try {
            IScene scene = PreSaleCustomerPageScene.builder().build();
            int pages = scene.visitor(visitor).execute().getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                scene.setPage(page);
                JSONArray list = scene.visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String shopName = list.getJSONObject(i).getString("shop_name");
                    String brandName = list.getJSONObject(i).getString("brand_name");
                    String subjectType = list.getJSONObject(i).getString("subject_type");
                    String customerTypeName = list.getJSONObject(i).getString("customer_type_name");
                    String registrationStatus = list.getJSONObject(i).getString("registration_status");
                    String customerName = list.getJSONObject(i).getString("customer_name");
                    String customerPhone = list.getJSONObject(i).getString("customer_phone");
                    String intentionCarModelName = list.getJSONObject(i).getString("intention_car_model_name");
                    String sex = list.getJSONObject(i).getString("sex");
                    String intentionCarStyleName = list.getJSONObject(i).getString("intention_car_style_name");
                    String createDate = list.getJSONObject(i).getString("create_date");
                    String saleName = list.getJSONObject(i).getString("sale_name");
                    String salePhone = list.getJSONObject(i).getString("sale_phone");
                    Preconditions.checkArgument(shopName != null && brandName != null && subjectType != null && customerTypeName != null && registrationStatus != null && customerName != null && customerPhone != null && intentionCarModelName != null && sex != null && intentionCarStyleName != null && createDate != null && saleName != null && salePhone != null, "第" + page + "页，第" + (i + 1) + "行存列表项为空的数据");
                }
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("销售客户列表项校验 ");
        }

    }

    /**
     * 销售客户查看详情
     */
    @Test(description = "销售客户查看详情")
    public void preSaleCustomerPage4() {
        try {
            JSONObject response = yt.preSalesCustomerPage("1", "10", "", "");
            int pages = response.getInteger("pages") > 20 ? 20 : response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = yt.preSalesCustomerPage("1", "10", "", "").getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String sex = list.getJSONObject(i).getString("sex");
                    String saleName = list.getJSONObject(i).getString("sale_name");
                    String customerPhone = list.getJSONObject(i).getString("customer_phone");
                    String intentionCarStyleName = list.getJSONObject(i).getString("intention_car_style_name");
                    String createDate = list.getJSONObject(i).getString("create_date");
                    Long shopId = list.getJSONObject(i).getLong("shop_id");
                    Long customerId = list.getJSONObject(i).getLong("customer_id");
                    //进入销售客户详情
                    IScene scene = PreSaleCustomerInfoScene.builder().customerId(customerId).shopId(shopId).build();
                    scene.visitor(visitor).execute();
                    JSONObject response1 = scene.visitor(visitor).execute();
                    String sexDetails = response1.getString("sex");
                    String saleNameDetails = response1.getString("sale_name");
                    String customerPhoneDetails = response1.getString("customer_phone");
                    String intentionCarStyleNameDetails = response1.getString("intention_car_style_name");
                    String createDateDetails = response1.getString("create_date");

                    Preconditions.checkArgument(sex.equals(sexDetails), "第" + pages + "页，第" + i + "行列表项为：" + sex + "  客户详情中的内容为：" + sexDetails);
                    Preconditions.checkArgument(saleName.equals(saleNameDetails), "第" + pages + "页，第" + i + "行列表项为：" + saleName + "  客户详情中的内容为：" + saleNameDetails);
                    Preconditions.checkArgument(customerPhone.equals(customerPhoneDetails), "第" + pages + "页，第" + i + "行列表项为：" + customerPhone + "  客户详情中的内容为：" + customerPhoneDetails);
                    Preconditions.checkArgument(intentionCarStyleName.equals(intentionCarStyleNameDetails), "第" + pages + "页，第" + i + "行列表项为：" + intentionCarStyleName + "  客户详情中的内容为：" + intentionCarStyleNameDetails);
                    Preconditions.checkArgument(createDate.equals(createDateDetails), "第" + pages + "页，第" + i + "行列表项为：" + createDate + "  客户详情中的内容为：" + createDateDetails);
                }
            }

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("销售客户查看详情 ");
        }
    }


    /**
     * 成交客户筛选栏
     */
    @Test(dataProvider = "SELECT_ preSaleBuyCarPageFilter", dataProviderClass = Constant.class, description = "成交客户筛选栏")
    public void preSaleCustomerPage5(String pram, String output) {
        try {
            JSONObject response = yt.preSalesBuyCarPage("1", "10", "", "");
            if (response.getJSONArray("list").size() > 0) {
                if (pram.equals("shop_id")) {
                    String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                    Long shopId = businessUtil.getStoreId(result);
                    System.err.println(result);
                    JSONObject response1 = yt.preSalesBuyCarPage("1", "10", pram, String.valueOf(shopId));
                    int pages = response1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = yt.preSalesBuyCarPage(String.valueOf(page), "10", pram, String.valueOf(shopId)).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            System.out.println("销售客户接待列表按" + result + "查询，结果错误" + Flag);
                            Preconditions.checkArgument(Flag.contains(result), "销售客户接待列表按" + result + "查询，结果错误" + Flag);
                        }
                    }
                } else if (pram.equals("car_style_id")) {
                    String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                    Long styleId = businessUtil.getStyleId(result);
                    System.err.println(result);
                    JSONObject response1 = yt.preSalesBuyCarPage("1", "10", pram, String.valueOf(styleId));
                    int pages = response1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = yt.preSalesBuyCarPage(String.valueOf(page), "10", pram, String.valueOf(styleId)).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            System.out.println("销售客户接待列表按" + result + "查询，结果错误" + Flag);
                            Preconditions.checkArgument(Flag.contains(result), "销售客户接待列表按" + result + "查询，结果错误" + Flag);
                        }
                    }
                } else {
                    String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                    System.err.println(result);
                    JSONObject response1 = yt.preSalesBuyCarPage("1", "10", pram, result);
                    int pages = response1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = yt.preSalesBuyCarPage(String.valueOf(page), "10", pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            System.out.println("销售客户接待列表按" + result + "查询，结果错误" + Flag);
                            Preconditions.checkArgument(Flag.contains(result), "销售客户接待列表按" + result + "查询，结果错误" + Flag);
                        }
                    }
                }
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("销售客户接待列表-筛选栏单项搜索 ");
        }
    }


    /**
     * @date :2021-06-08
     * @deprecated 成交记录-筛选栏时间搜索
     */
    @Test(description = "成交记录-筛选栏时间搜索")
    public void preSaleCustomerPage6() {
        try {
            String startTime = dt.getHistoryDate(-30);
            String endTime = dt.getHistoryDate(30);
            JSONObject response = yt.preSalesBuyCarPageTime("1", "10", startTime, endTime);
            int pages = response.getInteger("pages") > 10 ? 10 : response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = yt.preSalesBuyCarPageTime(String.valueOf(page), "10", startTime, endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String buyCarDate = list.getJSONObject(i).containsKey("buy_car_date") ? list.getJSONObject(i).getString("buy_car_date").substring(0, 10) : startTime;
                    Preconditions.checkArgument(buyCarDate.compareTo(startTime) >= 0 && buyCarDate.compareTo(endTime) <= 0, "开始时间：" + startTime + " 结束时间：" + endTime + "列表中创建时间时间:" + buyCarDate);
                }
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData(" 成交记录-筛选栏时间搜索 ");
        }
    }

    /**
     * 成交记录列表项校验
     */
    @Test(description = "成交记录列表项校验")
    public void preSaleCustomerPage7() {
        try {
            JSONObject response = yt.preSalesBuyCarPage("1", "10", "", "");
            int pages = response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = yt.preSalesBuyCarPage("1", "10", "", "").getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String shopName = list.getJSONObject(i).getString("shop_name");
                    String brandName = list.getJSONObject(i).getString("brand_name");
                    String ownerTypeName = list.getJSONObject(i).getString("owner_type_name");
                    String customerName = list.getJSONObject(i).getString("customer_name");
                    String customerPhone = list.getJSONObject(i).getString("customer_phone");
                    String sex = list.getJSONObject(i).getString("sex");
                    String intentionCarStyleName = list.getJSONObject(i).getString("intention_car_style_name");
                    String intentionCarModelName = list.getJSONObject(i).getString("intention_car_model_name");
                    String buyCarDate = list.getJSONObject(i).getString("buy_car_date");
                    String vehicleChassisCode = list.getJSONObject(i).getString("vehicle_chassis_code");
                    String preSaleName = list.getJSONObject(i).getString("pre_sale_name");
                    String preSaleAccount = list.getJSONObject(i).getString("pre_sale_account");
                    Preconditions.checkArgument(shopName != null && brandName != null && ownerTypeName != null && buyCarDate != null && vehicleChassisCode != null && customerName != null && customerPhone != null && intentionCarModelName != null && sex != null && intentionCarStyleName != null && preSaleName != null && preSaleAccount != null, "第" + pages + "页，第" + i + "行存列表项为空的数据");
                }
            }

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("成交记录列表项校验 ");
        }
    }


    /**
     * 展厅客户编辑客户信息
     */
    @Test(description = "展厅客户编辑客户信息")
    public void preSaleCustomerPage8() {
        try {
            JSONObject response = yt.preSalesCustomerPage("1", "10", "", "");
            JSONArray list = response.getJSONArray("list");
            int sexId = list.getJSONObject(0).getInteger("sex_id");
            String subjectType = list.getJSONObject(0).getString("subject_type");
            String customerName = list.getJSONObject(0).getString("customer_name");
            String customerPhone = list.getJSONObject(0).getString("customer_phone");
            Long intentionCarStyleId = list.getJSONObject(0).getLong("intention_car_style_id");
            Long intentionCarModelId = list.getJSONObject(0).getLong("intention_car_model_id");
            Long shopId = list.getJSONObject(0).getLong("shop_id");
            Long customerId = list.getJSONObject(0).getLong("customer_id");
            //编辑客户信息，修改的内容为列表种内容
            IScene scene = PreSaleCustomerEditScene.builder()
                    .customerId(customerId)
                    .shopId(shopId)
                    .subjectType(subjectType)
                    .customerName(customerName + businessUtil.randomNumber())
                    .customerPhone(customerPhone)
                    .sex(sexId)
                    .intentionCarModelId(intentionCarModelId)
                    .carStyleId(intentionCarStyleId)
                    .build();
            String message = scene.visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(message.equals("success"), "编辑客户失败");
            //编辑后的列表信息
            JSONObject response1 = yt.preSalesCustomerPage("1", "10", "", "");
            JSONArray list1 = response.getJSONArray("list");
            int sexId1 = list1.getJSONObject(0).getInteger("sex_id");
            String subjectType1 = list1.getJSONObject(0).getString("subject_type");
            String customerName1 = list1.getJSONObject(0).getString("customer_name");
            String customerPhone1 = list1.getJSONObject(0).getString("customer_phone");
            Long intentionCarStyleId1 = list1.getJSONObject(0).getLong("intention_car_style_id");
            Long intentionCarModelId1 = list1.getJSONObject(0).getLong("intention_car_model_id");

            Preconditions.checkArgument(sexId1 == sexId, "编辑后的性别为：" + sexId1 + "    编辑的性别为：" + sexId);
            Preconditions.checkArgument(subjectType1.equals(subjectType), "编辑后的车主类型为：" + subjectType1 + "    编辑的车主类型为：" + subjectType);
            Preconditions.checkArgument(customerName1.equals(customerName), "编辑后的姓名为：" + customerName1 + "    编辑的姓名为：" + customerName1);
            Preconditions.checkArgument(customerPhone1.equals(customerPhone), "编辑后的电话为：" + customerPhone1 + "    编辑的电话为：" + customerPhone);
            Preconditions.checkArgument(intentionCarStyleId1.equals(intentionCarStyleId), "编辑后的意向车系为：" + intentionCarStyleId1 + "    编辑的意向车系为：" + intentionCarStyleId);
            Preconditions.checkArgument(intentionCarModelId1.equals(intentionCarModelId), "编辑后的意向车型为：" + intentionCarModelId1 + "    编辑的意向车型为：" + intentionCarModelId);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("销售客户查看详情 ");
        }
    }

    /**
     * @date :2021-06-08
     * @deprecated 销售客户接待列表-筛选栏单项搜索
     */
    @Test(dataProvider = "SELECT_preSalesReceptionPageRecordFilter", dataProviderClass = Constant.class, description = "销售客户接待列表-筛选栏单项搜索")
    public void preSaleCustomerPage9(String pram, String output) {
        try {
            JSONObject response = yt.salesReceptionPage("1", "10", "", "");
            if (response.getJSONArray("list").size() > 0) {
                System.out.println(pram + "   " + output);
                String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                System.err.println(result);
                JSONObject response1 = yt.salesReceptionPage("1", "10", pram, result);
                int pages = response1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = yt.salesReceptionPage(String.valueOf(page), "10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        System.out.println("销售客户接待列表按" + result + "查询，结果错误" + Flag);
                        Preconditions.checkArgument(Flag.contains(result), "V3.1销售客户接待列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("销售客户接待列表-筛选栏单项搜索 ");
        }
    }

    /**
     * @date :2021-06-08
     * @deprecated 销售接待列表-筛选栏时间搜索
     */
    @Test(description = "销售接待列表-筛选栏时间搜索")
    public void preSaleCustomerPage10() {
        try {
            String startTime = dt.getHistoryDate(-30);
            String endTime = dt.getHistoryDate(30);
            JSONObject response = yt.salesReceptionPageTime("1", "10", startTime, endTime);
            System.out.println("---------" + response);
            int pages = response.getInteger("pages") > 10 ? 10 : response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = yt.salesReceptionPageTime(String.valueOf(page), "10", startTime, endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String receptionStartTime = list.getJSONObject(i).containsKey("reception_start_time") ? list.getJSONObject(i).getString("reception_start_time").substring(0, 10) : startTime;
                    String receptionEndTime = list.getJSONObject(i).containsKey("reception_end_time") ? list.getJSONObject(i).getString("reception_end_time").substring(0, 10) : endTime;
                    System.out.println("开始时间：" + startTime + " 结束时间：" + endTime + "列表中接待开始时间和结束时间:" + receptionStartTime + "    " + receptionEndTime);
                    Preconditions.checkArgument(receptionStartTime.compareTo(startTime) >= 0 && receptionEndTime.compareTo(endTime) <= 0, "开始时间：" + startTime + " 结束时间：" + endTime + "列表中接待开始时间和结束时间:" + receptionStartTime + "    " + receptionEndTime);
                }
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData(" 销售接待列表-筛选栏时间搜索 ");
        }
    }


    /**
     * --------------------------------------------------------------------数据一致性----------------------------------------------------------------
     */

    /**
     * 新建成交记录,成交客户的手机号在销售客户的列表中存在，成交记录+1，销售客户+0
     */
    @Test(description = "新建成交记录,成交客户的手机号在销售客户的列表中存在，成交记录+1   ")
    public void preSaleCustomerPageCase1() {
        try {
            //成交记录列表
            IScene scene = PreSaleCustomerBuyCarPageScene.builder().page(1).size(10).build();
            JSONObject response = scene.visitor(visitor).execute();
            //新建成交记录前的列表条数
            int totalBefore = response.getInteger("total");
            //展厅客户的列表
            IScene scene1 = PreSaleCustomerPageScene.builder().page(1).size(10).build();
            JSONObject response2 = scene1.visitor(visitor).execute();
            //新建成交记录前的销售客户列表条数
            int totalBefore1 = response2.getInteger("total");

            //新建成交记录
            systemCase.newCstmRecord("Max", "13373166806", "CORPORATION", "0", "", "true");
            //新建成交记录后的列表条数
            JSONObject response1 = PreSaleCustomerBuyCarPageScene.builder().page(1).size(10).build().visitor(visitor).execute();
            int totalAfter = response1.getInteger("total");
            //新建成交记录后的销售客户列表条数
            int totalAfter1 = PreSaleCustomerPageScene.builder().page(1).size(10).build().visitor(visitor).execute().getInteger("total");
            //获取新建的成家记录的信息
            JSONObject object = response1.getJSONArray("list").getJSONObject(0);
            String customerName = object.getString("customer_name");
            String customerPhone = object.getString("customer_phone");
            String ownerTypeId = object.getString("owner_type_name").equals("公司") ? "CORPORATION" : "PERSON";
            String sex = object.getString("sex").equals("女") ? "0" : "1";
            Preconditions.checkArgument(totalAfter == totalBefore + 1, "新建成交记录前的成交记录条数为：" + totalBefore + "   新建成交记录以后的成交记录条数为：" + totalAfter);
            Preconditions.checkArgument(totalAfter1 == totalBefore1 + 1, "新建成交记录前的销售客户条数为：" + totalBefore1 + "   新建成交记录以后的销售客户条数为：" + totalAfter1);
            Preconditions.checkArgument(customerName.equals("Max") && customerPhone.equals("13373166806") && ownerTypeId.equals("CORPORATION") && sex.equals("0"), "新建成交记录的信息与创建时填写的不一致");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("新建成交记录,成交客户的手机号在销售客户的列表中存在，成交记录+1 ，销售客户+0");
        }
    }

    /**
     * 新建成交记录,成交客户的手机号在销售客户的列表中存在，展厅客户中的购车记录+1
     */
    @Test(description = "新建成交记录,成交客户的手机号在销售客户的列表中存在，展厅客户中的购车记录+1")
    public void preSaleCustomerPageCase2() {
        try {
            IScene scene = PreSaleCustomerPageScene.builder().page(1).size(10).customerPhone("13373166806").build();
            JSONObject response = scene.visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
            Long shopId = response.getLong("shop_id");
            Long customerId = response.getLong("customer_id");
            JSONObject respond = PreSaleCustomerInfoBuyCarRecordScene.builder().customerId(customerId).shopId(shopId).build().visitor(visitor).execute();
            //新建成交记录前客户详情中的购车记录条数
            int totalBefore = respond.getInteger("total");
            //新建成交记录
            systemCase.newCstmRecord("Max", "13373166806", "CORPORATION", "0", "", "true");
            //新建成交记录后的列表条数
            JSONObject respond1 = PreSaleCustomerInfoBuyCarRecordScene.builder().customerId(customerId).shopId(shopId).build().visitor(visitor).execute();
            int totalAfter = respond1.getInteger("total");
            JSONObject object = respond1.getJSONArray("list").getJSONObject(0);
            String customerName = object.getString("customer_name");
            String customerPhone = object.getString("customer_phone");
            String buyCarDate = object.getString("buy_car_date");

            Preconditions.checkArgument(totalAfter == totalBefore + 1, "新建成交记录前的购车记录条数为：" + totalBefore + "   新建成交记录以后的购车记录条数为：" + totalAfter);
            Preconditions.checkArgument(customerName.equals("Max") && customerPhone.equals("13373166806") && buyCarDate.contains(businessUtil.getDateNow()), "购车记录信息与新建购车记录时填写的信息不一致");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("新建成交记录,成交客户的手机号在销售客户的列表中存在，展厅客户中的购车记录+1");
        }
    }

    /**
     * 新建潜客记录,潜客的手机号在列表中不存在，销售客户+1
     */
    @Test(description = "新建潜客记录,潜客的手机号在列表中不存在，销售客户+1")
    public void preSaleCustomerPageCase3() {
        try {
            //展厅客户的列表
            IScene scene = PreSaleCustomerPageScene.builder().page(1).size(10).build();
            JSONObject response = scene.visitor(visitor).execute();
            //新建潜客前的销售客户列表条数
            int totalBefore = response.getInteger("total");
            //新建潜客
            systemCase.newPotentialCustomer("自动化潜客" + businessUtil.randomNumber(), "1337316" + businessUtil.randomNumber(), "CORPORATION", "0", "", "true");
            //新建潜客后的列表条数
            JSONObject response1 = PreSaleCustomerPageScene.builder().page(1).size(10).build().visitor(visitor).execute();
            int totalAfter = response1.getInteger("total");
            //获取新建的潜客的信息
            JSONObject object = response1.getJSONArray("list").getJSONObject(0);
            String customerName = object.getString("customer_name");
            String customerPhone = object.getString("customer_phone");
            String ownerTypeId = object.getString("owner_type_name").equals("公司") ? "CORPORATION" : "PERSON";
            String sex = object.getString("sex").equals("女") ? "0" : "1";
            Preconditions.checkArgument(totalAfter == totalBefore + 1, "新建潜客前的条数为：" + totalBefore + "   新建潜客以后的条数为：" + totalAfter);
            Preconditions.checkArgument(customerName.equals("Max") && customerPhone.equals("13373166806") && ownerTypeId.equals("CORPORATION") && sex.equals("0"), "新建潜客的信息与创建时填写的不一致");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("新建潜客记录,潜客的手机号在列表中不存在，销售客户+1");
        }
    }


}