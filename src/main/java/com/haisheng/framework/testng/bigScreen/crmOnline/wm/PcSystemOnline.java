package com.haisheng.framework.testng.bigScreen.crmOnline.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppointmentType;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCustomerLevel;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.crmOnline.CrmScenarioUtilOnline;
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
import java.util.Date;
import java.util.List;

public class PcSystemOnline extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtilOnline crm = CrmScenarioUtilOnline.getInstance();
    private static final EnumAccount zjl = EnumAccount.ZJL_ONLINE;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_ONLINE.getName());
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.PORSCHE_SHOP_ONLINE.getShopId();
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

//    ---------------------------------------------------2.0------------------------------------------------------------

    @Test(enabled = false)
    public void salesCustomerManagement_3() {
        logger.logCaseStart(caseResult.getCaseName());
        JSONObject response = crm.customerList("", "", "", "", "", 1, 10);
        JSONObject list = response.getJSONArray("list").getJSONObject(0);
        CommonUtil.valueView(list);
        List<String> array = CommonUtil.getMoreParam(list, "customer_id", "customer_name", "customer_phone", "belongs_sale_id");
        System.out.println(array);
    }

    /**
     * 销售客户管理-所有顾客
     */
    @Test(description = "公海-筛选")
    public void myCustomer_function_1() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), -180);
        try {
            int total = crm.publicCustomerList(date, date1, 1, 100).getInteger("total");
            Preconditions.checkArgument(total == 0, "开始时间>=结束时间，筛选不出公海客户");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("开始时间<=结束时间,筛选出日期内划入公海得客户列表");
        }
    }

    @Test(description = "战败-筛选-正常")
    public void myCustomer_function_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total = crm.failureCustomerList("", "", 1, 10).getInteger("total");
            Preconditions.checkArgument(total >= 0, "开始时间>=结束时间，筛选不出战败客户");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc端战败客户按照日期筛选-开始时间<=结束时间");
        }
    }

    @Test(description = "战败-筛选-异常")
    public void myCustomer_function_3() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            crm.failureCustomerList(date1, date, 1, 10);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc端战败客户按照日期筛选-开始时间>结束时间,接口不限制，前端限制时间");
        }
    }

    @Test(description = "小程序-筛选-正常")
    public void myCustomer_function_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.wechatCustomerList("", "", 1, 10).getJSONArray("list");
            Preconditions.checkArgument(list.size() > 0, "开始时间>=结束时间，筛选不出小程序客户");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc端小程序客户按照日期筛选-开始时间<=结束时间");
        }
    }

    @Test(description = "交车后，客户不回到公海")
    public void myCustomer_function_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int remainDays = 1;
            int customerId = 0;
            boolean flag = false;
            int total = crm.customerPage(10, 1, "", "", "").getInteger("total");
            for (int k = 1; k < CommonUtil.getTurningPage(total, 100); k++) {
                JSONArray list = crm.customerPage(100, k, "", "", "").getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getString("customer_level_name").equals(EnumCustomerLevel.D.getName())) {
                        Integer day = list.getJSONObject(i).getInteger("remain_days");
                        remainDays = day == null ? 0 : 1;
                        customerId = list.getJSONObject(i).getInteger("customer_id");
                        break;
                    }
                }
            }
            JSONArray list1 = crm.publicCustomerList("", "", 2 << 10, 1).getJSONArray("list");
            for (int i = 0; i < list1.size(); i++) {
                flag = list1.getJSONObject(i).getInteger("customer_id") == customerId;
            }
            CommonUtil.valueView(remainDays, flag);
            Preconditions.checkArgument(!flag, "交车后，客户进入了公海");
            Preconditions.checkArgument(remainDays == 0, "交车后，剩余天数不等于0");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("交车后，客户不回到公海");
        }
    }

    /**
     * @description: 商品管理-添加商品
     */
    @Test(description = "商品管理-添加商品")
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
            appendFailreason(e.toString());
        } finally {
            saveData("商品管理中，各必须参数不填写创建车型&&全部填写创建车型");
        }
    }

//    ---------------------------------------------------3.0------------------------------------------------------------

    /**
     * 后台运营-站内消息
     */
    @Test(description = "投放人群:售前、售后、售前/售后")
    public void stationMessage_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            boolean flag = false;
            int total = crm.messagePage(1, 10).getInteger("total");
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
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("站内消息可投放人群为售前、售后、售前/售后");
        }
    }

    @Test(description = "生效时间-生效日期：格式yyyy-MM-dd hh：mm")
    public void stationMessage_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.messagePage(1, 10);
            String date = CommonUtil.getStrField(response, 0, "send_time");
            boolean result = CommonUtil.isLegalDate(date, "yyyy-MM-dd HH:mm");
            CommonUtil.valueView(date, result);
            Preconditions.checkArgument(result, "站内消息生效日期格式非yyyy-MM-dd hh:mm");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("站内消息生效日期格式：yyyy-MM-dd hh：mm");
        }
    }

    @Test(description = "状态：排期中、发送成功,都可以查看")
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
            appendFailreason(e.toString());
        } finally {
            saveData("排期中的消息可以查看操作，接口不做限制，前端限制");
        }
    }

    @Test(description = "倒叙排列-最新的在最上方")
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
            appendFailreason(e.toString());
        } finally {
            saveData("站内消息倒叙排列-最新的在最上方&&排期中的站内消息可以删除");
        }
    }

    @Test(description = "任何状态均可删除")
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
            crm.messageDelete(messageId);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("发送成功的站内消息可以删除");
        }
    }

    @Test(description = "站内消息配置人群为销售，小程序销售可见消息，售后不可见消息", enabled = false)
    public void stationMessage_6() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "销售可见消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        try {
            boolean result1 = false;
            boolean result2 = false;
            JSONObject response = crm.messageAdd("", "", "", sendDate, title, content, "", "", "PRE_SALES");
            int id = response.getInteger("id");
            sleep(80);
            //登陆小程序-售前可见消息
            UserUtil.loginApplet(EnumAppletCode.WM);
            JSONArray list = crm.wechatMessageList("", 20).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("title").equals(title)
                        && list.getJSONObject(i).getString("date").equals(sendDate)) {
                    result1 = true;
                    break;
                }
            }
            //登陆小程序-售后不可见消息
            UserUtil.loginApplet(EnumAppletCode.XMF);
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
            UserUtil.login(zjl);
            crm.messageDelete(id);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("站内消息配置人群为销售，小程序销售可见消息，售后不可见消息");
        }
    }

    @Test(description = "站内消息配置人群为售后，小程序销售不可见消息，售后可见消息", enabled = false)
    public void stationMessage_7() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "售后可见消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        try {
            boolean result1 = false;
            boolean result2 = false;
            JSONObject response = crm.messageAdd("", "", "", sendDate, title, content, "", "", "AFTER_SALES");
            int id = response.getInteger("id");
            sleep(80);
            //登陆小程序-售前不可见消息
            UserUtil.loginApplet(EnumAppletCode.WM);
            JSONArray list = crm.wechatMessageList("", 20).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("title").equals(title)
                        && list.getJSONObject(i).getString("date").equals(sendDate)) {
                    result1 = true;
                    break;
                }
            }
            //登陆小程序-售后可见消息
            UserUtil.loginApplet(EnumAppletCode.XMF);
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
            UserUtil.login(zjl);
            crm.messageDelete(id);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("站内消息配置人群为销售，小程序销售不可见消息，售后可见消息");
        }
    }

    @Test(description = "站内消息配置人群为销售/售后，小程序销售可见消息，售后可见消息", enabled = false)
    public void stationMessage_8() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "销售/售后均可见消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        try {
            boolean result1 = false;
            boolean result2 = false;
            JSONObject response = crm.messageAdd("", "", "", sendDate, title, content, "", "", "PRE_SALES", "AFTER_SALES");
            int id = response.getInteger("id");
            sleep(80);
            //登陆小程序-售前可见消息
            UserUtil.loginApplet(EnumAppletCode.WM);
            JSONArray list = crm.wechatMessageList("", 20).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("title").equals(title)
                        && list.getJSONObject(i).getString("date").equals(sendDate)) {
                    result1 = true;
                    break;
                }
            }
            //登陆小程序-售后可见消息
            UserUtil.loginApplet(EnumAppletCode.XMF);
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
            UserUtil.login(zjl);
            crm.messageDelete(id);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("站内消息配置人群为销售/售后，小程序销售可见消息，售后可见消息");
        }
    }

    @Test(description = "站内消息内容可包括中英文，符号，数字，空格")
    public void stationMessage_9() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "Chinese&&English is No.1 in use!";
        String content = "aba aba 123456!@#$%^&*,待删除";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 100), "yyyy-MM-dd HH:mm");
        try {
            JSONObject response = crm.messageAdd("", "", "", sendDate, title, content, "", "", "PRE_SALES", "AFTER_SALES");
            int id = response.getInteger("id");
            Preconditions.checkArgument(id != 0, "创建站内消息失败");
            crm.messageDelete(id);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("站内消息内容可包括中英文，符号，数字，空格");
        }
    }

    @Test(description = "pc创建预约试驾的站内消息，小程序显示按钮,小程序可跳转填写试驾信息页", enabled = false)
    public void stationMessage_10() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "自动化站内消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        String appointmentType = EnumAppointmentType.TEST_DRIVE.getType();
        try {
            JSONObject response = crm.messageAdd("", "", "", sendDate, title, content, appointmentType, "", "PRE_SALES", "AFTER_SALES");
            int messageId = response.getInteger("id");
            sleep(80);
            UserUtil.loginApplet(EnumAppletCode.WM);
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
            UserUtil.login(zjl);
            crm.messageDelete(messageId);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc创建预约试驾的站内消息，小程序显示按钮,小程序可跳转填写试驾信息页");
        }
    }

    @Test(description = "pc创建预约维修的站内消息，小程序显示按钮,小程序可跳转填写维修信息页", enabled = false)
    public void stationMessage_11() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "自动化站内消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        String appointmentType = EnumAppointmentType.REPAIR.getType();
        try {
            JSONObject response = crm.messageAdd("", "", "", sendDate, title, content, appointmentType, "", "PRE_SALES", "AFTER_SALES");
            int messageId = response.getInteger("id");
            sleep(80);
            UserUtil.loginApplet(EnumAppletCode.WM);
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
            UserUtil.login(zjl);
            crm.messageDelete(messageId);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc创建预约维修的站内消息，小程序显示按钮,小程序可跳转填写维修信息页");
        }
    }

    @Test(description = "pc创建预约保养的站内消息，小程序显示按钮,小程序可跳转填写保养信息页", enabled = false)
    public void stationMessage_12() {
        logger.logCaseStart(caseResult.getCaseName());
        String title = "自动化站内消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 70), "yyyy-MM-dd HH:mm");
        String appointmentType = EnumAppointmentType.MAINTAIN.getType();
        try {
            JSONObject response = crm.messageAdd("", "", "", sendDate, title, content, appointmentType, "", "PRE_SALES", "AFTER_SALES");
            int messageId = response.getInteger("id");
            sleep(80);
            UserUtil.loginApplet(EnumAppletCode.WM);
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
            UserUtil.login(zjl);
            crm.messageDelete(messageId);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc创建预约保养的站内消息，小程序显示按钮,小程序可跳转填写保养信息页");
        }
    }
}
