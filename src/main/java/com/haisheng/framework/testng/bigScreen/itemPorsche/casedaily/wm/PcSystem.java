package com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.commonDs.PublicMethod;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.bean.SaleInfo;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.enumerator.customer.EnumAppointmentType;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.enumerator.customer.EnumCarModel;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.scene.app.ArticleAddScene;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.scene.pc.CustomerListScene;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.scene.pc.DccEditScene;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.scene.pc.MessageAddScene;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * pc系统测试用例
 *
 * @author wangmin
 */
public class PcSystem extends TestCaseCommon implements TestCaseStd {
    EnumTestProduct product = EnumTestProduct.PORSCHE_DAILY;
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    PublicMethod method = new PublicMethod();
    private static final EnumAccount zjl = EnumAccount.ZJL_DAILY;
    private static final EnumAppletToken sale = EnumAppletToken.BSJ_WM_DAILY;
    private static final EnumAppletToken afterSale = EnumAppletToken.BSJ_GLY_DAILY;
    private static final EnumCarModel car = EnumCarModel.PANAMERA_TURBO_S_E_HYBRID_SPORT_TURISMO;
    private static final int SIZE = 100;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.setShopId(product.getShopId()).setReferer(product.getReferer()).setRoleId(product.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);
        logger.debug("crm: " + crm);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        UserUtil.login(zjl);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    /**
     * 销售客户管理-所有顾客
     */
    @Test(description = "销售客户管理--公海--开始时间<=结束时间,筛选出公海日期在此区间内的客户")
    public void myCustomer_function_1() {
        logger.logCaseStart(caseResult.getCaseName());
        String startDate = DateTimeUtil.addDayFormat(new Date(), -30);
        String endDate = DateTimeUtil.getFormat(new Date());
        String unixStart = DateTimeUtil.dateToStamp(startDate, "yyyy-MM-dd");
        String unixEnd = DateTimeUtil.dateToStamp(DateTimeUtil.addDayFormat(new Date(), 1), "yyyy-MM-dd");
        try {
            int total = crm.publicCustomerList(startDate, endDate, 10, 1).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.publicCustomerList(startDate, endDate, 100, i).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    String distributeTime = list.getJSONObject(j).getString("distribute_time");
                    CommonUtil.valueView(unixStart, distributeTime, unixEnd);
                    Preconditions.checkArgument(distributeTime.compareTo(unixEnd) <= 0, "开始时间>=结束时间，结果包含不在此时间区间内的客户");
                    Preconditions.checkArgument(distributeTime.compareTo(unixStart) >= 0, "开始时间>=结束时间，结果包含不在此时间区间内的客户");
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售客户管理--公海--开始时间<=结束时间,筛选出公海日期在此区间内的客户");
        }
    }

    @Test(description = "销售客户管理--战败--开始时间<=结束时间,筛选出战败日期在此区间内的客户")
    public void myCustomer_function_2() {
        logger.logCaseStart(caseResult.getCaseName());
        String startDate = DateTimeUtil.addDayFormat(new Date(), -120);
        String endDate = DateTimeUtil.getFormat(new Date());
        String unixStart = DateTimeUtil.dateToStamp(startDate, "yyyy-MM-dd");
        String unixEnd = DateTimeUtil.dateToStamp(DateTimeUtil.addDayFormat(new Date(), 1), "yyyy-MM-dd");
        try {
            int total = crm.failureCustomerList(startDate, endDate, 1, 10).getInteger("total");
            int s = CommonUtil.getTurningPage(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.failureCustomerList(startDate, endDate, i, 100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    String distributeTime = list.getJSONObject(j).getString("distribute_time");
                    CommonUtil.valueView(unixStart, distributeTime, unixEnd);
                    Preconditions.checkArgument(distributeTime.compareTo(unixEnd) <= 0, "开始时间>=结束时间，结果包含不在此时间区间内的客户");
                    Preconditions.checkArgument(distributeTime.compareTo(unixStart) >= 0, "开始时间>=结束时间，结果包含不在此时间区间内的客户");
                    CommonUtil.log("分割线");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售客户管理--战败--开始时间<=结束时间,筛选出战败日期在此区间内的客户");
        }
    }

    @Test(description = "销售客户管理--战败--异常筛选")
    public void myCustomer_function_3() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            crm.failureCustomerList(date1, date, 1, 10);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售客户管理--战败--异常筛选");
        }
    }

    @Test(description = "销售客户管理--小程序--开始时间<=结束时间,筛选出小程序人员创建日期在此区间内的客户")
    public void myCustomer_function_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.wechatCustomerList("", "", 1, 10).getJSONArray("list");
            Preconditions.checkArgument(list.size() > 0, "开始时间>=结束时间，筛选不出小程序客户");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售客户管理--小程序--开始时间<=结束时间,筛选出小程序人员创建日期在此区间内的客户");
        }
    }

    @Test(description = "商品管理--各必须参数不填写创建车型&&全部填写创建车型")
    public void goodsManager_function_1() {
        logger.logCaseStart(caseResult.getCaseName());
        String path = "src/main/java/com/haisheng/framework/testng/bigScreen/crm/wm/multimedia/goodsmanager/";
        String bigPic = new ImageUtil().getImageBinary(path + "大图照片.jpg");
        String interiorPic = new ImageUtil().getImageBinary(path + "内饰照片.jpg");
        String spacePic = new ImageUtil().getImageBinary(path + "空间照片.jpg");
        String appearancePic = new ImageUtil().getImageBinary(path + "外观照片.jpg");
        String carPic = new ImageUtil().getImageBinary(path + "车辆照片.jpg");
        String carTypeName = "凯迪拉克CT6";
        String carIntroduce = "无介绍";
        try {
            String message = crm.goodsManagerAddCar(appearancePic, bigPic, "无优惠", carIntroduce, "", carTypeName, 10, interiorPic, 20, spacePic).getString("message");
            String message1 = crm.goodsManagerAddCar("", bigPic, "无优惠", carIntroduce, carPic, carTypeName, 10, interiorPic, 20, spacePic).getString("message");
            String message2 = crm.goodsManagerAddCar(appearancePic, bigPic, "无优惠", carIntroduce, carPic, carTypeName, 10, "", 20, spacePic).getString("message");
            String message3 = crm.goodsManagerAddCar(appearancePic, bigPic, "无优惠", carIntroduce, carPic, carTypeName, 10, interiorPic, 20, "").getString("message");
            String message4 = crm.goodsManagerAddCar(appearancePic, "", "无优惠", carIntroduce, carPic, carTypeName, 10, interiorPic, 20, "").getString("message");
            String message5 = crm.goodsManagerAddCar(appearancePic, bigPic, "无优惠", carIntroduce, carPic, carTypeName, -1, interiorPic, 20, spacePic).getString("message");
            String message6 = crm.goodsManagerAddCar(appearancePic, bigPic, "无优惠", carIntroduce, carPic, carTypeName, 10, interiorPic, -1, spacePic).getString("message");
            String message7 = crm.goodsManagerAddCar(appearancePic, bigPic, "无优惠", "", carPic, carTypeName, 20, interiorPic, 10, spacePic).getString("message");
            String message8 = crm.goodsManagerAddCar(appearancePic, bigPic, "无优惠", carIntroduce, carPic, "", 20, interiorPic, 10, spacePic).getString("message");
            String message9 = crm.goodsManagerAddCar(appearancePic, bigPic, "无优惠", carIntroduce, carPic, carTypeName, 10, interiorPic, 200, spacePic).getString("message");
            Preconditions.checkArgument(message.equals("车辆图片不能为空"), "pc商品管理，车辆图片为空也可创建成功");
            Preconditions.checkArgument(message1.equals("车辆外观图片不能为空"), "pc商品管理，外观照片为空也可创建成功");
            Preconditions.checkArgument(message2.equals("车辆内饰图片不能为空"), "pc商品管理，内饰照片为空也可创建成功");
            Preconditions.checkArgument(message3.equals("车辆空间图片不能为空"), "pc商品管理，空间照片为空也可创建成功");
            Preconditions.checkArgument(message4.equals("车辆大图不能为空"), "pc商品管理，大图照片为空也可创建成功");
            Preconditions.checkArgument(message5.equals("车辆最高价格不能为空"), "pc商品管理，最高价格为空也可创建成功");
            Preconditions.checkArgument(message6.equals("车辆最低价格不能为空"), "pc商品管理，最低价格为空也可创建成功");
            Preconditions.checkArgument(message7.equals("车辆介绍不能为空"), "pc商品管理，车辆介绍为空也可创建成功");
            Preconditions.checkArgument(message8.equals("车辆类型名称不能为空"), "pc商品管理，车辆最低价格>最高价格也可创建成功");
            Preconditions.checkArgument(message9.equals("车辆最低价格不能高于车辆最高价格"), "所有必填项全正确填写，车型创建失败");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("商品管理--各必须参数不填写创建车型&&全部填写创建车型");
        }
    }

    @Test(description = "销售客户管理--DCC客户编辑--填写所有项，保存")
    public void myCustomer_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            JSONArray array = new JSONArray();
            JSONObject object = new JSONObject();
            object.put("comment", "清风徐来，水波不兴");
            object.put("next_return_visit_date", date);
            array.add(object);
            String[] records = {"壬戌之秋，七月既望，苏子与客泛舟游于赤壁之下。清风徐来，水波不兴", "纵一苇之所如，凌万顷之茫然。浩浩乎如冯虚御风，而不知其所止；飘飘乎如遗世独立，羽化而登仙"};
            String expectedBuyDay = DateTimeUtil.addDayFormat(new Date(), 10);
            JSONArray list = crm.dccList("", "", "", "", 1, 10).getJSONArray("list");
            String platNumber = method.getDistinctPlat("辽A", 6);
            String customerName = list.getJSONObject(0).getString("customer_name");
            String customerLevel = list.getJSONObject(0).getString("customer_level");
            String belongsSaleId = list.getJSONObject(0).getString("belongs_sale_id");
            String recordDate = list.getJSONObject(0).getString("record_date");
            String customerPhone = list.getJSONObject(0).getString("customer_phone");
            int customerId = list.getJSONObject(0).getInteger("customer_id");
            DccEditScene.DccEditSceneBuilder builder = DccEditScene.builder();
            builder.customerName(customerName).customerLevel(customerLevel).recordDate(recordDate)
                    .belongsSaleId(belongsSaleId).createDate("").carStyle(Integer.parseInt(car.getStyleId()))
                    .carModel(Integer.parseInt(car.getModelId())).sourceChannel(1).expectedBuyDay(expectedBuyDay)
                    .plateNumber1(platNumber).plateNumber2(platNumber).region("110101").records(records).visits(array)
                    .customerPhone1(customerPhone).customerPhone2(customerPhone).customerId(customerId).build();
            String message = dccEdit(builder);
            Preconditions.checkArgument(message.equals("成功"), message);
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("销售客户管理--DCC客户编辑--填写所有项，保存");
        }
    }

    @Test(description = "销售客户管理--DCC客户编辑--不填写联系方式")
    public void myCustomer_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            JSONArray array = new JSONArray();
            JSONObject object = new JSONObject();
            object.put("comment", "清风徐来，水波不兴");
            object.put("next_return_visit_date", date);
            array.add(object);
            String[] records = {"壬戌之秋，七月既望，苏子与客泛舟游于赤壁之下。清风徐来，水波不兴", "纵一苇之所如，凌万顷之茫然。浩浩乎如冯虚御风，而不知其所止；飘飘乎如遗世独立，羽化而登仙"};
            String expectedBuyDay = DateTimeUtil.addDayFormat(new Date(), 10);
            JSONArray list = crm.dccList("", "", "", "", 1, 10).getJSONArray("list");
            String platNumber = method.getDistinctPlat("辽A", 6);
            String customerName = list.getJSONObject(0).getString("customer_name");
            String customerLevel = list.getJSONObject(0).getString("customer_level");
            String belongsSaleId = list.getJSONObject(0).getString("belongs_sale_id");
            String recordDate = list.getJSONObject(0).getString("record_date");
            int customerId = list.getJSONObject(0).getInteger("customer_id");
            DccEditScene.DccEditSceneBuilder builder = DccEditScene.builder();
            builder.customerName(customerName).customerLevel(customerLevel).recordDate(recordDate)
                    .belongsSaleId(belongsSaleId).createDate("").carStyle(Integer.parseInt(car.getStyleId()))
                    .carModel(Integer.parseInt(car.getModelId())).sourceChannel(1).expectedBuyDay(expectedBuyDay)
                    .plateNumber1(platNumber).plateNumber2(platNumber).region("110101").records(records).visits(array)
                    .customerId(customerId).build();
            String message = dccEdit(builder);
            Preconditions.checkArgument(message.equals("DCC客户手机号不能为空"), message);
        } catch (Exception | AssertionError e) {
            e.printStackTrace();
            appendFailReason(e.toString());
        } finally {
            saveData("销售客户管理--DCC客户编辑--不填写联系方式");
        }
    }

    private String dccEdit(DccEditScene.DccEditSceneBuilder builder) {
        String message = crm.invokeApi(builder.build(), false).getString("message");
        if (!message.equals("车牌号已存在") && !message.equals("车牌号已被试驾车占用")) {
            return message;
        }
        String platNumber = method.getDistinctPlat("川A", 6);
        builder.plateNumber1(platNumber).plateNumber2(platNumber).build();
        return dccEdit(builder);
    }

    /**
     * 后台运营-站内消息
     */
    @Test(description = "站内消息--投放人群:售前、售后、售前/售后")
    public void stationMessage_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            boolean flag = false;
            int total = crm.messagePage(1, 10).getInteger("total");
            if (total != 0) {
                for (int i = 1; i < CommonUtil.getTurningPage(total, 100); i++) {
                    JSONArray list = crm.messagePage(1, 100).getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        flag = list.getJSONObject(j).getString("customer_types").contains("销售/售后")
                                || list.getJSONObject(j).getString("customer_types").contains("销售")
                                || list.getJSONObject(j).getString("customer_types").contains("售后");
                    }
                }
                CommonUtil.valueView(flag);
                Preconditions.checkArgument(flag, "站内消息投放人群不正确,人群不含销售、售后、销售/售后");
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("站内消息--站内消息可投放人群为售前、售后、售前/售后");
        }
    }

    @Test(description = "站内消息--生效时间-生效日期：格式yyyy-MM-dd hh：mm")
    public void stationMessage_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.messagePage(1, 10);
            int total = response.getInteger("total");
            if (total != 0) {
                String date = CommonUtil.getStrField(response, 0, "send_time");
                boolean result = CommonUtil.isLegalDate(date, "yyyy-MM-dd HH:mm");
                CommonUtil.valueView(date, result);
                Preconditions.checkArgument(result, "站内消息生效日期格式非yyyy-MM-dd hh:mm");
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("站内消息--站内消息生效日期格式：yyyy-MM-dd hh：mm");
        }
    }

    @Test(description = "站内消息--状态：排期中、发送成功,都可以查看")
    public void stationMessage_3() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "销售可见消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        try {
            JSONObject response = crm.messageAdd("", "", "", sendDate, title, content, "", "", "PRE_SALES");
            int id = response.getInteger("id");
            JSONObject result = crm.messageDetail(id);
            String title1 = result.getString("title");
            CommonUtil.valueView(title, title1);
            Preconditions.checkArgument(title.equals(title1), "排期中的站内消息可以查看操作");
            crm.messageDelete(id);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("站内消息--排期中的消息可以查看操作，接口不做限制，前端限制");
        }
    }

    @Test(description = "站内消息--倒叙排列-最新的在最上方")
    public void stationMessage_4() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "自动化站内消息-待删";
        String content = "自动化";
        Date date = DateTimeUtil.addDay(new Date(), 1);
        String sendDate = DateTimeUtil.getFormat(date, "yyyy-MM-dd HH:mm");
        try {
            crm.messageAdd("", "", "", sendDate, title, content, "", "", "PRE_SALES", "AFTER_SALES");
            int messageId = crm.messagePage(1, 10).getJSONArray("list").getJSONObject(0).getInteger("id");
            String content1 = crm.messageDetail(messageId).getString("content");
            String title1 = crm.messageDetail(messageId).getString("title");
            CommonUtil.valueView(title, title1, content, content1);
            Preconditions.checkArgument(content.equals(content1), "新创建的文章未排列在第一位");
            Preconditions.checkArgument(title.equals(title1), "新创建的文章未排列在第一位");
            crm.messageDelete(messageId);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("站内消息--站内消息倒叙排列-最新的在最上方&&排期中的站内消息可以删除");
        }
    }

    @Test(description = "站内消息--任何状态均可删除")
    public void stationMessage_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.messagePage(1, 100).getJSONArray("list");
            int messageId = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("status_name").equals("发送成功")) {
                    messageId = list.getJSONObject(i).getInteger("id");
                }
            }
            if (messageId != 0) {
                crm.messageDelete(messageId);
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("站内消息--发送成功的站内消息可以删除");
        }
    }

    @Test(description = "站内消息--配置人群为销售，小程序销售可见消息，售后不可见消息")
    public void stationMessage_6() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "销售可见消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        try {
            deleteMessage();
            boolean result1 = false;
            boolean result2 = false;
            JSONObject response = crm.messageAdd("", "", "", sendDate, title, content, "", "", "PRE_SALES");
            int id = response.getInteger("id");
            sleep(80);
            //登陆小程序-售前可见消息
            UserUtil.loginApplet(sale);
            JSONArray list = crm.wechatMessageList("", 20).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("title").equals(title)
                        && list.getJSONObject(i).getString("date").equals(sendDate)) {
                    result1 = true;
                    break;
                }
            }
            //登陆小程序-售后不可见消息
            UserUtil.loginApplet(afterSale);
            JSONArray list1 = crm.wechatMessageList("", 20).getJSONArray("list");
            for (int i = 0; i < list1.size(); i++) {
                if (list1.getJSONObject(i).getString("title").equals(title)
                        && list1.getJSONObject(i).getString("date").equals(sendDate)) {
                    result2 = true;
                    break;
                }
            }
            CommonUtil.valueView(result1, result2);
            Preconditions.checkArgument(result1, "小程序销售客户看不见消息");
            Preconditions.checkArgument(!result2, "小程序售后客户能看见消息");
            deleteMessage(id);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("站内消息--配置人群为销售，小程序销售可见消息，售后不可见消息");
        }
    }

    @Test(description = "站内消息--配置人群为销售，小程序销售可见消息，售后不可见消息消息配置人群为售后，小程序销售不可见消息，售后可见消息")
    public void stationMessage_7() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "售后可见消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        try {
            deleteMessage();
            boolean result1 = false;
            boolean result2 = false;
            JSONObject response = crm.messageAdd("", "", "", sendDate, title, content, "", "", "AFTER_SALES");
            int id = response.getInteger("id");
            sleep(80);
            //登陆小程序-售前不可见消息
            UserUtil.loginApplet(sale);
            JSONArray list = crm.wechatMessageList("", 20).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("title").equals(title)
                        && list.getJSONObject(i).getString("date").equals(sendDate)) {
                    result1 = true;
                    break;
                }
            }
            //登陆小程序-售后可见消息
            UserUtil.loginApplet(afterSale);
            JSONArray list1 = crm.wechatMessageList("", 20).getJSONArray("list");
            for (int i = 0; i < list1.size(); i++) {
                if (list1.getJSONObject(i).getString("title").equals(title)
                        && list1.getJSONObject(i).getString("date").equals(sendDate)) {
                    result2 = true;
                    break;
                }
            }
            CommonUtil.valueView(result1, result2);
            Preconditions.checkArgument(!result1, "小程序销售客户能看见消息");
            Preconditions.checkArgument(result2, "小程序售后客户不能看见消息");
            deleteMessage(id);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("站内消息--配置人群为销售，小程序销售可见消息，售后不可见消息消息配置人群为售后，小程序销售不可见消息，售后可见消息");
        }
    }

    @Test(description = "站内消息--配置人群为销售/售后，小程序销售可见消息，售后可见消息")
    public void stationMessage_8() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "销售/售后均可见消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        try {
            deleteMessage();
            boolean result1 = false;
            boolean result2 = false;
            JSONObject response = crm.messageAdd("", "", "", sendDate, title, content, "", "", "PRE_SALES", "AFTER_SALES");
            int id = response.getInteger("id");
            sleep(80);
            //登陆小程序-售前可见消息
            UserUtil.loginApplet(sale);
            JSONArray list = crm.wechatMessageList("", 20).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("title").equals(title)
                        && list.getJSONObject(i).getString("date").equals(sendDate)) {
                    result1 = true;
                    break;
                }
            }
            //登陆小程序-售后可见消息
            UserUtil.loginApplet(afterSale);
            JSONArray list1 = crm.wechatMessageList("", 20).getJSONArray("list");
            for (int i = 0; i < list1.size(); i++) {
                if (list1.getJSONObject(i).getString("title").equals(title)
                        && list1.getJSONObject(i).getString("date").equals(sendDate)) {
                    result2 = true;
                    break;
                }
            }
            CommonUtil.valueView(result1, result2);
            Preconditions.checkArgument(result1, "小程序销售客户能看见消息");
            Preconditions.checkArgument(result2, "小程序售后客户不能看见消息");
            deleteMessage(id);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("站内消息--配置人群为销售/售后，小程序销售可见消息，售后可见消息");
        }
    }

    @Test(description = "站内消息--内容可包括中英文，符号，数字，空格")
    public void stationMessage_9() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "Chinese&&English is No.1 in use!";
        String content = "aba aba 123456!@#$%^&*,待删除";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 100), "yyyy-MM-dd HH:mm");
        try {
            deleteMessage();
            JSONObject response = crm.messageAdd("", "", "", sendDate, title, content, "", "", "PRE_SALES", "AFTER_SALES");
            int id = response.getInteger("id");
            Preconditions.checkArgument(id != 0, "创建站内消息失败");
            crm.messageDelete(id);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("站内消息--内容可包括中英文，符号，数字，空格");
        }
    }

    @Test(description = "站内消息--创建预约试驾的站内消息，小程序显示按钮,小程序可跳转填写试驾信息页")
    public void stationMessage_10() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "自动化站内消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        String appointmentType = EnumAppointmentType.TEST_DRIVE.getType();
        try {
            deleteMessage();
            JSONObject response = crm.messageAdd("", "", "", sendDate, title, content, appointmentType, "", "PRE_SALES", "AFTER_SALES");
            int messageId = response.getInteger("id");
            sleep(80);
            UserUtil.loginApplet(sale);
            JSONArray list = crm.wechatMessageList("", 20).getJSONArray("list");
            int id = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("date").equals(sendDate)) {
                    id = list.getJSONObject(i).getInteger("id");
                }
            }
            String appletAppointmentType = crm.messageDetail((long) id).getString("appointment_type");
            CommonUtil.valueView(appletAppointmentType);
            Preconditions.checkArgument(appointmentType.equals(appletAppointmentType), "pc端发送的站内消息，小程序接收到以后没有预约试驾按钮");
            deleteMessage(messageId);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("站内消息--创建预约试驾的站内消息，小程序显示按钮,小程序可跳转填写试驾信息页");
        }
    }

    @Test(description = "站内消息--创建预约维修的站内消息，小程序显示按钮,小程序可跳转填写维修信息页")
    public void stationMessage_11() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "自动化站内消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        String appointmentType = EnumAppointmentType.REPAIR.getType();
        try {
            deleteMessage();
            JSONObject response = crm.messageAdd("", "", "", sendDate, title, content, appointmentType, "", "PRE_SALES", "AFTER_SALES");
            int messageId = response.getInteger("id");
            sleep(80);
            UserUtil.loginApplet(sale);
            JSONArray list = crm.wechatMessageList("", 20).getJSONArray("list");
            int id = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("date").equals(sendDate)) {
                    id = list.getJSONObject(i).getInteger("id");
                }
            }
            String appletAppointmentType = crm.messageDetail((long) id).getString("appointment_type");
            CommonUtil.valueView(appletAppointmentType);
            Preconditions.checkArgument(appointmentType.equals(appletAppointmentType), "pc端发送的站内消息，小程序接收到以后没有预约维修按钮");
            deleteMessage(messageId);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("站内消息--创建预约维修的站内消息，小程序显示按钮,小程序可跳转填写维修信息页");
        }
    }

    @Test(description = "站内消息--创建预约保养的站内消息，小程序显示按钮,小程序可跳转填写保养信息页")
    public void stationMessage_12() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "自动化站内消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        String appointmentType = EnumAppointmentType.MAINTAIN.getType();
        try {
            deleteMessage();
            JSONObject response = crm.messageAdd("", "", "", sendDate, title, content, appointmentType, "", "PRE_SALES", "AFTER_SALES");
            int messageId = response.getInteger("id");
            sleep(80);
            UserUtil.loginApplet(sale);
            JSONArray list = crm.wechatMessageList("", 20).getJSONArray("list");
            int id = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("date").equals(sendDate)) {
                    id = list.getJSONObject(i).getInteger("id");
                }
            }
            String appletAppointmentType = crm.messageDetail((long) id).getString("appointment_type");
            CommonUtil.valueView(appletAppointmentType);
            Preconditions.checkArgument(appointmentType.equals(appletAppointmentType), "pc端发送的站内消息，小程序接收到以后没有预约保养按钮");
            deleteMessage(messageId);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("站内消息--创建预约保养的站内消息，小程序显示按钮,小程序可跳转填写保养信息页");
        }
    }

    @Test(description = "站内消息--创建预约保养的站内消息，小程序显示按钮,小程序可跳转填写保养信息页")
    public void stationMessage_13() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "自动化站内消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        String appointmentType = EnumAppointmentType.MAINTAIN.getType();
        try {
            deleteMessage();
            JSONObject response = crm.messageAdd("", "", "", sendDate, title, content, appointmentType, "", "PRE_SALES", "AFTER_SALES");
            int messageId = response.getInteger("id");
            sleep(80);
            UserUtil.loginApplet(sale);
            JSONArray list = crm.wechatMessageList("", 20).getJSONArray("list");
            int id = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("date").equals(sendDate)) {
                    id = list.getJSONObject(i).getInteger("id");
                }
            }
            String appletAppointmentType = crm.messageDetail((long) id).getString("appointment_type");
            CommonUtil.valueView(appletAppointmentType);
            Preconditions.checkArgument(appointmentType.equals(appletAppointmentType), "pc端发送的站内消息，小程序接收到以后没有预约保养按钮");
            deleteMessage(messageId);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("站内消息--创建预约保养的站内消息，小程序显示按钮,小程序可跳转填写保养信息页");
        }
    }

    @Test(description = "站内消息--所有项全部都填，发送成功")
    public void stationMessage_14() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "自动化站内消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        String appointmentType = EnumAppointmentType.MAINTAIN.getType();
        String[] customerTypes = {"PRE_SALES", "AFTER_SALES"};
        int[] carTypes = {6, 5, 4, 3, 2, 1};
        int[] customerLevel = {6, 3, 2, 1, 14, 7, 15, 17};
        String[] customerProperty = {"LOST", "MAINTENANCE", "LOYAL"};
        try {
            IScene scene = MessageAddScene.builder().title(title).content(content).sendTime(sendDate).appointmentType(appointmentType)
                    .carTypes(carTypes).customerTypes(customerTypes).customerLevel(customerLevel).customerProperty(customerProperty)
                    .build();
            String message = crm.invokeApi(scene, false).getString("message");
            Preconditions.checkArgument(message.equals("成功"), "创建消息时所有项全部填创建失败");
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("站内消息--所有项全部都填，发送成功");
        }
    }

    @Test(description = "内容运营--创建售前/售后活动，小程序售前、售后均可见")
    public void contentOperation_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String type = "ACTIVITY_1";
            String content = "测试";
            String title = "售前售后都可见";
            String path = "src/main/java/com/haisheng/framework/testng/bigScreen/crm/wm/multimedia/proportion/比例5-3图.jpg";
            String picture = new ImageUtil().getImageBinary(path);
            String startDate = DateTimeUtil.getFormat(new Date());
            String endDate = DateTimeUtil.addDayFormat(new Date(), 1);
            JSONArray array = new JSONArray();
            array.add("PRE_SALES");
            array.add("AFTER_SALES");
            IScene scene = ArticleAddScene.builder().position(type).customerTypes(array).validStart(startDate).validEnd(endDate)
                    .articleTitle(title).articleBgPic(picture).articleContent(content).build();
            int id = crm.invokeApi(scene).getInteger("id");
            UserUtil.loginApplet(sale);
            JSONArray list = crm.articleList().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("article_type").equals(type)) {
                    String articleContent = list.getJSONObject(i).getString("article_content");
                    String articleTitle = list.getJSONObject(i).getString("article_title");
                    Preconditions.checkArgument(articleContent.equals(content), "pc车主活动1配置内容：" + content + "售前小程序车主活动1展示内容" + articleContent);
                    Preconditions.checkArgument(articleTitle.equals(title), "pc车主活动1配置标题：" + title + "售前小程序车主活动1展示标题" + articleTitle);
                }
            }
            UserUtil.loginApplet(afterSale);
            JSONArray list1 = crm.articleList().getJSONArray("list");
            for (int i = 0; i < list1.size(); i++) {
                if (list1.getJSONObject(i).getString("article_type").equals(type)) {
                    String articleContent = list1.getJSONObject(i).getString("article_content");
                    String articleTitle = list1.getJSONObject(i).getString("article_title");
                    Preconditions.checkArgument(articleContent.equals(content), "pc车主活动1配置内容：" + content + "售后小程序车主活动1展示内容" + articleContent);
                    Preconditions.checkArgument(articleTitle.equals(title), "pc车主活动1配置标题：" + title + "售后小程序车主活动1展示标题" + articleTitle);
                }
            }
            deleteActivity(id);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容运营--创建售前/售后活动，小程序售前、售后均可见");
        }
    }

    @Test(description = "内容运营--创建售前活动，小程序售前可见，售后不可见")
    public void contentOperation_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String type = "ACTIVITY_1";
            String title = "售前可见";
            String content = "测试" + title;
            String path = "src/main/java/com/haisheng/framework/testng/bigScreen/crm/wm/multimedia/proportion/比例5-3图.jpg";
            String picture = new ImageUtil().getImageBinary(path);
            String startDate = DateTimeUtil.getFormat(new Date());
            String endDate = DateTimeUtil.addDayFormat(new Date(), 1);
            JSONArray array = new JSONArray();
            array.add("PRE_SALES");
            IScene scene = ArticleAddScene.builder().position(type).customerTypes(array).validStart(startDate).validEnd(endDate)
                    .articleTitle(title).articleBgPic(picture).articleContent(content).build();
            int id = crm.invokeApi(scene).getInteger("id");
            UserUtil.loginApplet(sale);
            JSONArray list = crm.articleList().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("article_type").equals(type)) {
                    String articleContent = list.getJSONObject(i).getString("article_content");
                    String articleTitle = list.getJSONObject(i).getString("article_title");
                    Preconditions.checkArgument(articleContent.equals(content), "pc车主活动1配置内容：" + content + "售前小程序车主活动1展示内容" + articleContent);
                    Preconditions.checkArgument(articleTitle.equals(title), "pc车主活动1配置标题：" + title + "售前小程序车主活动1展示标题" + articleTitle);
                }
            }
            UserUtil.loginApplet(afterSale);
            JSONArray list1 = crm.articleList().getJSONArray("list");
            for (int i = 0; i < list1.size(); i++) {
                if (list1.getJSONObject(i).getString("article_type").equals(type)) {
                    String articleContent = list1.getJSONObject(i).getString("article_content");
                    String articleTitle = list1.getJSONObject(i).getString("article_title");
                    Preconditions.checkArgument(!articleContent.equals(content), "pc车主活动1配置内容：" + content + "售后小程序车主活动1展示内容" + articleContent);
                    Preconditions.checkArgument(!articleTitle.equals(title), "pc车主活动1配置标题：" + title + "售后小程序车主活动1展示标题" + articleTitle);
                }
            }
            deleteActivity(id);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--创建售前活动，小程序售前可见，售后不可见");
        }
    }

    @Test(description = "内容运营--创建售后活动，小程序售后可见，售前不可见")
    public void contentOperation_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String type = "ACTIVITY_1";
            String title = "售后可见";
            String content = "测试" + title;
            String path = "src/main/java/com/haisheng/framework/testng/bigScreen/crm/wm/multimedia/proportion/比例5-3图.jpg";
            String picture = new ImageUtil().getImageBinary(path);
            String startDate = DateTimeUtil.getFormat(new Date());
            String endDate = DateTimeUtil.addDayFormat(new Date(), 1);
            JSONArray array = new JSONArray();
            array.add("AFTER_SALES");
            IScene scene = ArticleAddScene.builder().position(type).customerTypes(array).validStart(startDate).validEnd(endDate)
                    .articleTitle(title).articleBgPic(picture).articleContent(content).build();
            int id = crm.invokeApi(scene).getInteger("id");
            UserUtil.loginApplet(sale);
            JSONArray list = crm.articleList().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("article_type").equals(type)) {
                    String articleContent = list.getJSONObject(i).getString("article_content");
                    String articleTitle = list.getJSONObject(i).getString("article_title");
                    Preconditions.checkArgument(!articleContent.equals(content), "pc车主活动1配置内容：" + content + "售前小程序车主活动1展示内容" + articleContent);
                    Preconditions.checkArgument(!articleTitle.equals(title), "pc车主活动1配置标题：" + title + "售前小程序车主活动1展示标题" + articleTitle);
                }
            }
            UserUtil.loginApplet(afterSale);
            JSONArray list1 = crm.articleList().getJSONArray("list");
            for (int i = 0; i < list1.size(); i++) {
                if (list1.getJSONObject(i).getString("article_type").equals(type)) {
                    String articleContent = list1.getJSONObject(i).getString("article_content");
                    String articleTitle = list1.getJSONObject(i).getString("article_title");
                    Preconditions.checkArgument(articleContent.equals(content), "pc车主活动1配置内容：" + content + "售后小程序车主活动1展示内容" + articleContent);
                    Preconditions.checkArgument(articleTitle.equals(title), "pc车主活动1配置标题：" + title + "售后小程序车主活动1展示标题" + articleTitle);
                }
            }
            deleteActivity(id);
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("内容运营--创建售后活动，小程序售后可见，售前不可见");
        }
    }

    @Test(enabled = false)
    public void contentOperation_4() {
    }

    private void deleteActivity(int id) throws Exception {
        UserUtil.login(zjl);
        crm.articleStatusChange((long) id);
        String message = crm.articleDelete((long) id).getString("message");
        Preconditions.checkArgument(message.equals("成功"), id + "文章删除失败");
    }

    private void deleteActivity() throws Exception {
        UserUtil.login(zjl);
        JSONArray list = crm.articlePage(1, 100, "ACTIVITY_1").getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            if (list.getJSONObject(i).getString("status_name").equals("已过期")
                    || list.getJSONObject(i).getString("status_name").equals("已下架")) {
                int id = list.getJSONObject(i).getInteger("id");
                String message = crm.articleDelete((long) id).getString("message");
                Preconditions.checkArgument(message.equals("成功"), id + "文章删除失败");
            }
        }
    }

    private void deleteMessage() throws Exception {
        UserUtil.login(zjl);
        int total = crm.messagePage(1, 10).getInteger("total");
        int s = CommonUtil.getTurningPage(total, 100);
        for (int i = 1; i < s; i++) {
            JSONArray list = crm.messagePage(i, 100).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                if (list.getJSONObject(j).getString("status_name").equals("发送成功")) {
                    int id = list.getJSONObject(j).getInteger("id");
                    crm.messageDelete(id);
                }
            }
        }
    }

    private void deleteMessage(int id) {
        UserUtil.login(zjl);
        crm.messageDelete(id);
    }

    @Test(description = "销售客户管理--我的客户--按照销售顾问搜索")
    public void exhibitionCustomer_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<SaleInfo> saleInfos = method.getSaleList("销售顾问");
            saleInfos.forEach(e -> {
                if (e.getUserId() != null) {
                    List<String> saleList = new ArrayList<>();
                    CustomerListScene.CustomerListSceneBuilder builder = CustomerListScene.builder().belongsSaleId(e.getUserId());
                    int total = crm.invokeApi(builder.build()).getInteger("total");
                    int s = CommonUtil.getTurningPage(total, SIZE);
                    for (int i = 1; i < s; i++) {
                        JSONArray array = crm.invokeApi(builder.page(i).size(SIZE).build()).getJSONArray("list");
                        saleList.addAll(array.stream().map(object -> (JSONObject) object).map(object -> object.getString("belongs_sale_name")).collect(Collectors.toList()));
                    }
                    saleList.forEach(value -> Preconditions.checkArgument(value.equals(e.getUserName()), "搜索" + e.getUserName() + "列表结果：" + value));
                    CommonUtil.logger(e.getUserName());
                }
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("销售客户管理--我的客户--按照销售顾问搜索");
        }
    }
}


