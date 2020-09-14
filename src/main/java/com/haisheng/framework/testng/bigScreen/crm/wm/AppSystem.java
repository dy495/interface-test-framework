package com.haisheng.framework.testng.bigScreen.crm.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumCustomerType;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.springframework.util.StringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * CRM-App-System 自动化用例
 *
 * @author wangmin
 */
public class AppSystem extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();

    @BeforeClass
    @Override
    public void initial() {
        CommonUtil.addConfigDaily();
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        CommonUtil.login(EnumAccount.XSGW);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

//    ---------------------------------------------------2.0------------------------------------------------------------

    @Test(enabled = false)
    public void registeredCustomer() {
        logger.logCaseStart(caseResult.getCaseName());
        int activityTaskId = 0;
        int activityId = 0;
        try {
            CommonUtil.login(EnumAccount.XSGW);
            JSONArray list = crm.activityTaskPage(1, 10).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getBoolean("is_edit")) {
                    activityTaskId = list.getJSONObject(i).getInteger("activity_task_id");
                    activityId = list.getJSONObject(i).getInteger("activity_id");
                    break;
                }
            }
            CommonUtil.login(EnumAccount.ZJL);
            int activityCustomer = crm.customerTaskPage(10, 1, (long) activityId).getJSONArray("list").size();
            CommonUtil.login(EnumAccount.XSGW);
            //添加报名信息
            crm.registeredCustomer((long) activityTaskId, "张三", "13454678912");
            //pc任务客户数量+1
            CommonUtil.login(EnumAccount.ZJL);
            int activityCustomer1 = crm.customerTaskPage(10, 1, (long) activityId).getJSONArray("list").size();
            Preconditions.checkArgument(activityCustomer1 == activityCustomer + 1, "添加报名人信息后，pc端任务活动未+1");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            CommonUtil.login(EnumAccount.XSGW);
            int customerId = 0;
            JSONArray list = crm.customerTaskPage(10, 1, (long) activityId).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("customer_phone_number").equals("13454678912")) {
                    customerId = list.getJSONObject(i).getInteger("id");
                }
            }
            //删除报名人
            crm.deleteCustomer(String.valueOf(activityTaskId), customerId);
            saveData("添加报名人信息");
        }
    }

    @Test(description = "创建接待", enabled = false)
    public void createReception() {
        logger.logCaseStart(caseResult.getCaseName());
        //分配销售
        try {
            //登录前台账号
            CommonUtil.login(EnumAccount.QT);
            //创建接待
            JSONObject response = crm.saleReceptionCreatReception();
            if (response.getString("message").equals("当前没有空闲销售~")) {
                //登录销售账号
                CommonUtil.login(EnumAccount.XSGW);
                long customerId = crm.userInfService().getLong("customer_id");
                //完成接待
                crm.finishReception(customerId, 7, "测试顾客1", "", "H级客户-taskListChkNum-修改时间为昨天");
                //再次分配
                crm.saleReceptionCreatReception();
            }
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建接待");
        }
    }

    /**
     * @description: 售后-工作管理-我的回访
     */
    @Test(description = "开始时间<=结束时间,成功")
    public void afterSaleMyReturnVisit_function_1() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            crm.afterSale_VisitRecordList(1, 1, "", date, date1);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，开始时间<=结束时间");
        }
    }

    @Test(description = "仅输入开始时间")
    public void afterSaleMyReturnVisit_function_2() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        try {
            crm.afterSale_VisitRecordList(1, 10, "", date, "");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，进输入开始时间");
        }
    }

    @Test(description = "仅输入结束时间")
    public void afterSaleMyReturnVisit_function_3() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        try {
            crm.afterSale_VisitRecordList(1, 10, "", "", date);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，进输入开始时间");
        }
    }

    @Test(description = "结束时间>开始时间")
    public void afterSaleMyReturnVisit_function_4() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            boolean flag = false;
            JSONArray list = crm.afterSale_VisitRecordList(1, 10, "", date1, date).getJSONArray("list");
            if (list.size() == 0) {
                flag = true;
            }
            Preconditions.checkArgument(flag, "结束时间>开始时间,查询成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，结束时间>开始时间");
        }
    }

//    ---------------------------------------------------2.1------------------------------------------------------------

    @Test(description = "创建线索,客户姓名为汉字-长度1-10")
    public void myCustomer_function_1() {
        logger.logCaseStart(caseResult.getCaseName());
        String phone = "13333333333";
        String remark = "七月七日长生殿，夜半无人私语时。在天愿作比翼鸟，在地愿为连理枝。天长地久有时尽，此恨绵绵无绝期。";
        String str = "!@#$%^&*()12345678历史记录计算机asdfghj";
        try {
            deleteCustomer(phone);
            //汉字，10字之内
            JSONObject response = crm.createLine("【自动化】王", 6, phone, 2, remark);
            Preconditions.checkArgument(response.getString("message").equals("成功"), "客户姓名为汉字，长度1-10个字内创建线索失败");
            deleteCustomer(phone);
            //汉字，1字
            JSONObject response1 = crm.createLine("王", 6, phone, 2, remark);
            Preconditions.checkArgument(response1.getString("message").equals("成功"), "客户姓名为汉字，长度1个字创建线索失败");
            deleteCustomer(phone);
            //汉字，10个字
            JSONObject response2 = crm.createLine("语言是一种通用的面向", 6, phone, 2, remark);
            Preconditions.checkArgument(response2.getString("message").equals("成功"), "客户姓名为汉字，长度1个字创建线索失败");
            deleteCustomer(phone);
            //备注包含中英文、汉字、符号、数字
            JSONObject response3 = crm.createLine("望京", 6, phone, 2, str);
            Preconditions.checkArgument(response3.getString("message").equals("成功"), "客户姓名为汉字，长度1个字创建线索失败");
            deleteCustomer(phone);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            deleteCustomer(phone);
            saveData("创建线索,客户姓名为汉字，长度1-10，备注20-200字之内");
        }
    }

    @Test(description = "创建线索,意向车型与商品管理中车型一致")
    public void myCustomer_function_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.afterSaleEnumInfo();
            JSONArray carTypes = response.getJSONArray("car_types");
            for (int i = 0; i < carTypes.size(); i++) {
                String carTypeName = carTypes.getJSONObject(i).getString("car_type_name");
                int carType = carTypes.getJSONObject(i).getInteger("car_type");
                JSONArray carList = crm.carList().getJSONArray("list");
                for (int j = 0; j < carList.size(); j++) {
                    if (carList.getJSONObject(j).getInteger("id") == carType) {
                        String carTypeName1 = carList.getJSONObject(j).getString("car_type_name");
                        Preconditions.checkArgument(carTypeName.equals(carTypeName1), "创建线索中意向车型与商品管理中车辆不一致");
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建线索,意向车型与商品管理中车型一致");
        }
    }

    @Test(description = "客户级别,下拉，HABCFG")
    public void myCustomer_function_3() {
        logger.logCaseStart(caseResult.getCaseName());
        String strings = "G,F,C,B,A,H";
        try {
            JSONArray list = crm.appCustomerLevelList().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String level = list.getJSONObject(i).getString("level");
                CommonUtil.valueView(level);
                Preconditions.checkArgument(strings.contains(level), "客户级别不包含" + strings + "之一");
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建线索，客户级别,下拉，包含HABCFG");
        }
    }

    @Test(description = "创建线索，不填写必填项")
    public void myCustomer_function_4() {
        logger.logCaseStart(caseResult.getCaseName());
        String phone = "13333333333";
        String remark = "七月七日长生殿，夜半无人私语时。在天愿作比翼鸟，在地愿为连理枝。天长地久有时尽，此恨绵绵无绝期。";
        try {
            deleteCustomer(phone);
            //不填客户名称
            JSONObject response = crm.createLine("", 6, phone, 2, remark);
            Preconditions.checkArgument(response.getString("message").equals("顾客姓名不能为空"), "顾客姓名为空也可创建成功");
            //不填意向车型
            JSONObject response1 = crm.createLine("望京", 0, phone, 2, remark);
            Preconditions.checkArgument(response1.getString("message").equals("意向车型不能为空"), "意向车型不存在也可创建成功");
            //不填电话
            JSONObject response2 = crm.createLine("望京", 1, "", 2, remark);
            Preconditions.checkArgument(response2.getString("message").equals("顾客手机号不能为空"), "顾客手机号为空也可创建成功");
            //不填客户级别
            JSONObject response3 = crm.createLine("望京", 1, phone, 0, remark);
            Preconditions.checkArgument(response3.getString("message").equals("顾客等级不能为空"), "顾客等级为空也可创建成功");
            //不填备注
            JSONObject response4 = crm.createLine("望京", 1, phone, 1, "");
            Preconditions.checkArgument(response4.getString("message").equals("备注信息20-200字之间"), "备注信息为空也可创建成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            deleteCustomer(phone);
            saveData("创建线索，不填写必填项");
        }
    }

    @Test(description = "创建线索，填写全部必填项,备注长度201，联系方式：系统存在")
    public void myCustomer_function_5() {
        logger.logCaseStart(caseResult.getCaseName());
        String phone = "15321527989";
        String remark = "七月七日长生殿，夜半无人私语时。在天愿作比翼鸟，在地愿为连理枝。天长地久有时尽，此恨绵绵无绝期。";
        String remarks = "汉皇重色思倾国，御宇多年求不得。\n" +
                "杨家有女初长成，养在深闺人未识。\n" +
                "天生丽质难自弃，一朝选在君王侧。\n" +
                "回眸一笑百媚生，六宫粉黛无颜色。\n" +
                "春寒赐浴华清池，温泉水滑洗凝脂。\n" +
                "侍儿扶起娇无力，始是新承恩泽时。\n" +
                "云鬓花颜金步摇，芙蓉帐暖度春宵。\n" +
                "春宵苦短日高起，从此君王不早朝。\n" +
                "承欢侍宴无闲暇，春从春游夜专夜。\n" +
                "后宫佳丽三千人，三千宠爱在一身。\n" +
                "金屋妆成娇侍夜，玉楼宴罢醉和春。\n" +
                "姊妹弟兄皆列土，可怜光彩生门户。\n" +
                "遂令天下父母心，不重生男重生女。\n" +
                "骊宫高处入青云，仙乐风飘处处闻。\n" +
                "缓歌慢舞凝丝竹，尽日君王看不足。\n" +
                "渔阳鼙鼓动地来，惊破霓裳羽衣曲。\n" +
                "九重城阙烟尘生，千乘万骑西南行。\n" +
                "翠华摇摇行复止，西出都门百余里。\n" +
                "六军不发无奈何，宛转蛾眉马前死。\n" +
                "花钿委地无人收，翠翘金雀玉搔头。\n" +
                "君王掩面救不得，回看血泪相和流。\n" +
                "黄埃散漫风萧索，云栈萦纡登剑阁。\n" +
                "峨嵋山下少人行，旌旗无光日色薄。\n" +
                "蜀江水碧蜀山青，圣主朝朝暮暮情。\n" +
                "行宫见月伤心色，夜雨闻铃肠断声。\n" +
                "天旋地转回龙驭，到此踌躇不能去。\n" +
                "马嵬坡下泥土中，不见玉颜空死处。\n" +
                "君臣相顾尽沾衣，东望都门信马归。\n";
        try {
            //存在的电话号
            JSONObject response = crm.createLine("王", 6, phone, 2, remark);
            Preconditions.checkArgument(response.getString("message").equals("手机号码重复"), "手机号码重复也可创建成功");
            //备注长度201
            JSONObject response4 = crm.createLine("望京", 1, phone, 1, remarks);
            Preconditions.checkArgument(response4.getString("message").equals("备注信息20-200字之间"), "备注信息超过200字也可创建成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("创建线索，不填写必填项");
        }
    }

    /**
     * 工作管理-我的回访
     */
    @Test(description = "开始时间<=结束时间,成功")
    public void myReturnVisit_function_1() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            int listSize = crm.returnVisitTaskPage(1, 10, date, date1).getJSONArray("list").size();
            Preconditions.checkArgument(listSize != 0, "回访查询失败");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，开始时间<=结束时间");
        }
    }

    @Test(description = "仅输入开始时间")
    public void myReturnVisit_function_2() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        try {
            int listSize = crm.returnVisitTaskPage(1, 10, date, "").getJSONArray("list").size();
            Preconditions.checkArgument(listSize != 0, "回访查询失败");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，进输入开始时间");
        }
    }

    @Test(description = "仅输入结束时间")
    public void myReturnVisit_function_3() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        try {
            int listSize = crm.returnVisitTaskPage(1, 10, "", date).getJSONArray("list").size();
            Preconditions.checkArgument(listSize != 0, "回访查询失败");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，进输入开始时间");
        }
    }

    @Test(description = "结束时间>开始时间")
    public void myReturnVisit_function_4() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            boolean flag = false;
            String list = crm.returnVisitTaskPage(1, 10, date1, date).getString("list");
            if (StringUtils.isEmpty(list)) {
                flag = true;
            }
            Preconditions.checkArgument(flag, "结束时间>开始时间,查询成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访按照时间筛选，结束时间>开始时间");
        }
    }

    @Test(description = "排序,一级：未联系在上，已联系在下", enabled = false)
    public void myReturnVisit_function_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CommonUtil.login(EnumAccount.XSGW);
            int s = 0;
            int total = crm.returnVisitTaskPage(1, 1, "", "").getInteger("total");
            int page1 = CommonUtil.pageTurning(total, 100);
            for (int i = 1; i < page1; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100, "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("task_status_name").equals("未完成")) {
                        s++;
                    }
                }
            }
            CommonUtil.valueView(s);
            int page2 = CommonUtil.pageTurning(s, 100);
            for (int i = 1; i < page2; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100, "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    Preconditions.checkArgument(list.getJSONObject(j).getString("task_status_name").equals("未完成"));

                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        }
    }

    @Test(description = "排序,二级：相同状态时间倒序排列", enabled = false)
    public void myReturnVisit_function_6() {

    }

    @Test(description = "手机号为11位手机号")
    public void myReturnVisit_function_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int s = CommonUtil.pageTurning(response.getInteger("total"), 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100, "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    String customerPhone = list.getJSONObject(j).getString("customer_phone");
                    Preconditions.checkArgument(customerPhone.length() == 11, "我的回访存在非11位电话号，电话号为：" + customerPhone);
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访，手机号为11位手机号码");
        }
    }

    @Test(description = "作为所属销售的客户")
    public void myReturnVisit_function_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CommonUtil.login(EnumAccount.XSGW);
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int s = CommonUtil.pageTurning(response.getInteger("total"), 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100, "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    String belongsSaleName = list.getJSONObject(i).getString("belongs_sale_name");
                    Preconditions.checkArgument(belongsSaleName.equals("销售顾问temp"), "我的回访列表，存在非当前登录账号的回访任务。所属销售为：" + belongsSaleName);
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访任务里 我看不到别人的客户 那些客户的所属销售 都是当前登陆账号");
        }
    }

    @Test(description = "列表项包括:所属销售、客户等级、客户名称、联系电话、意向车型、回访类型、是否完成")
    public void myReturnVisit_function_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.returnVisitTaskPage(1, 10, "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("belongs_sale_name"), "接口返回参数中不包含字段：belongs_sale_name");
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("customer_level_name"), "接口返回参数中不包含字段：customer_level_name");
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("customer_name"), "接口返回参数中不包含字段：customer_name");
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("customer_phone"), "接口返回参数中不包含字段：customer_phone");
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("like_car_name"), "接口返回参数中不包含字段：like_car_name");
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("customer_type_name"), "接口返回参数中不包含字段：customer_type_name");
                Preconditions.checkArgument(list.getJSONObject(i).containsKey("task_status_name"), "接口返回参数中不包含字段：task_status_name");
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访列表包括:所属销售、客户等级、客户名称、联系电话、意向车型、回访类型、是否完成");
        }
    }

    @Test(description = "回访类型:潜客，创建接待时“订车”标记为否的客户")
    public void myReturnVisit_function_10() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String date1 = DateTimeUtil.addDayFormat(new Date(), 1);
        try {
            CommonUtil.login(EnumAccount.ZJL);
            JSONArray list = crm.returnVisitTaskPage(1, 100, date, date1).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("customer_type_name").equals("潜客")) {
                    String customerPhone;
                    customerPhone = list.getJSONObject(i).getString("customer_phone");
                    if (StringUtils.isEmpty(customerPhone)) {
                        customerPhone = list.getJSONObject(i - 2).getString("customer_phone");
                        CommonUtil.valueView(customerPhone);
                    }
                    CommonUtil.valueView("电话号是" + customerPhone);
                    JSONObject result = crm.customerList("", customerPhone, "", "", "", 1, 10);
                    String ifByCarName = result.getJSONArray("list").getJSONObject(0).getString("buy_car_name");
                    CommonUtil.valueView(ifByCarName);
                    Preconditions.checkArgument(ifByCarName.equals("否"), "回访类型:潜客，创建接待时不是“订车”标记为否的客户");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访类型:潜客，创建接待时“订车”标记为否的客户");
        }
    }

    @Test(description = "回访类型:成交，创建接待时“订车”标记为是的客户")
    public void myReturnVisit_function_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CommonUtil.login(EnumAccount.ZJL);
            JSONArray list = crm.returnVisitTaskPage(1, 100, "", "").getJSONArray("list");
            for (int i = 0; i < 100; i++) {
                if (list.getJSONObject(i).getString("customer_type_name").equals(EnumCustomerType.CHANGE_HANDS.getName())) {
                    String customerPhone = list.getJSONObject(i).getString("customer_phone");
                    CommonUtil.valueView(customerPhone);
                    JSONObject result = crm.customerList("", customerPhone, "", "", "", 1, 10);
                    String ifByCarName = result.getJSONArray("list").getJSONObject(0).getString("buy_car_name");
                    CommonUtil.valueView(i, ifByCarName);
                    CommonUtil.log("--------------------分割线--------------------");
                    Preconditions.checkArgument(ifByCarName.equals("是"), "回访类型:成交，创建接待时不是“订车”标记为是的客户");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访类型:成交，创建接待时“订车”标记为是的客户");
        }
    }

    @Test(description = "回访类型:预约，预约记录中存在")
    public void myReturnVisit_function_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.returnVisitTaskPage(1, 100, "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("customer_type_name").equals(EnumCustomerType.APPOINTMENT.getName())) {
                    String customerPhone;
                    customerPhone = list.getJSONObject(i).getString("customer_phone");
                    if (StringUtils.isEmpty(customerPhone)) {
                        customerPhone = list.getJSONObject(i - 2).getString("customer_phone");
                        CommonUtil.valueView(customerPhone);
                    }
                    CommonUtil.valueView("电话号是:" + customerPhone);
                    JSONObject result = crm.appointmentTestDriverList(customerPhone, "", "", 1, 10);
                    JSONArray list1 = result.getJSONArray("list");
                    CommonUtil.valueView(list1);
                    Preconditions.checkArgument(list1.size() >= 1, "回访类型:预约，预约记录中不存在此电话号");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访类型:预约，工作管理-我的预约中存该手机号");
        }
    }

    @Test(description = "查看,有截图,show_pic字段为true")
    public void myReturnVisit_function_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total = crm.returnVisitTaskPage(1, 10, "", "").getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100, "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("task_status_name").equals("已完成")) {
                        boolean showPic = list.getJSONObject(j).getBoolean("show_pic");
                        int taskId = list.getJSONObject(j).getInteger("task_id");
                        CommonUtil.valueView(showPic, taskId);
                        JSONArray picList = crm.showPicResult(taskId).getJSONArray("data").getJSONObject(0).getJSONArray("pic_list");
                        Preconditions.checkArgument(showPic, "已完成的回访任务无法查看");
                        Preconditions.checkArgument(picList.size() > 0, "已完成的回访任务无截图");
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访,查看已完成任务,有图片,show_pic字段为true");
        }
    }

    @Test(description = "查看,无截图置灰,show_pic字段为false")
    public void myReturnVisit_function_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total = crm.returnVisitTaskPage(1, 10, "", "").getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            for (int i = 1; i < s; i++) {
                JSONArray list = crm.returnVisitTaskPage(i, 100, "", "").getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (!list.getJSONObject(j).getBoolean("show_pic")) {
                        String taskStatusName = list.getJSONObject(j).getString("task_status_name");
                        Preconditions.checkArgument(taskStatusName.equals("未完成"));
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访,未完成任务无图片,按钮置灰,show_pic字段为false");
        }
    }

    @Test(description = "回访客户,接听,是否完成=已完成")
    public void myReturnVisit_function_15() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String common = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            //获取回访列表
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 1, "task_id");
            //回访
            crm.returnVisitTaskExecute(taskId, common, "", date, "ANSWER", false, picture);
            CommonUtil.valueView(taskId);
            String taskStatusName = null;
            int total = crm.returnVisitTaskPage(1, 10, "", "").getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            for (int j = 1; j < s; j++) {
                JSONArray list = crm.returnVisitTaskPage(j, 100, "", "").getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getInteger("task_id") == taskId) {
                        taskStatusName = list.getJSONObject(i).getString("task_status_name");
                    }
                }
            }
            assert taskStatusName != null;
            CommonUtil.valueView(taskStatusName);
            Preconditions.checkArgument(taskStatusName.equals("已完成"), "完成回访后,是否完成状态未变为已完成");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访客户");
        }
    }

    @Test(description = "回访客户,其他,是否完成=已完成")
    public void myReturnVisit_function_16() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String common = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            //获取回访列表
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 1, "task_id");
            //回访
            crm.returnVisitTaskExecute(taskId, common, "", date, "OTHER", false, picture);
            CommonUtil.valueView(taskId);
            String taskStatusName = null;
            int total = crm.returnVisitTaskPage(1, 10, "", "").getInteger("total");
            int s = CommonUtil.pageTurning(total, 100);
            for (int j = 1; j < s; j++) {
                JSONArray list = crm.returnVisitTaskPage(j, 100, "", "").getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    if (list.getJSONObject(i).getInteger("task_id") == taskId) {
                        taskStatusName = list.getJSONObject(i).getString("task_status_name");
                    }
                }
            }
            assert taskStatusName != null;
            CommonUtil.valueView(taskStatusName);
            Preconditions.checkArgument(taskStatusName.equals("已完成"), "完成回访后,是否完成状态未变为已完成");
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访客户");
        }
    }

    @Test(description = "回访客户,无人接听,是否完成=未完成")
    public void myReturnVisit_function_17() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String common = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        int taskId = 0;
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(taskId, common, "", date, "NO_ONE_ANSWER", false, picture);
            Preconditions.checkArgument(result.getString("message").equals("成功"));
            //验证是否完成为未完成
            JSONArray list = crm.returnVisitTaskPage(1, 10, "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getInteger("task_id") == taskId) {
                    String taskStatusName = list.getJSONObject(i).getString("task_status_name");
                    CommonUtil.valueView(taskStatusName);
                    Preconditions.checkArgument(taskStatusName.equals("未完成"), "第一次回访为无人接听时,回访任务状态不为未完成");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            crm.returnVisitTaskExecute(taskId, common, "", date, "ANSWER", false, picPath);
            saveData("回访选择第一次无人接听时,是否完成=未完成");
        }
    }

    @Test(description = "回访客户,挂断,是否完成=未完成")
    public void myReturnVisit_function_18() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String common = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(taskId, common, "", date, "HANG_UP", false, picture);
            Preconditions.checkArgument(result.getString("message").equals("成功"));
            //验证是否完成为未完成
            JSONArray list = crm.returnVisitTaskPage(1, 10, "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getInteger("task_id") == taskId) {
                    String taskStatusName = list.getJSONObject(i).getString("task_status_name");
                    CommonUtil.valueView(taskStatusName);
                    Preconditions.checkArgument(taskStatusName.equals("未完成"), "第一次回访为挂断时,回访任务状态不为未完成");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访选择第一次挂断时,是否完成=未完成");
        }
    }

    @Test(description = "回访客户,稍后联系,是否完成=未完成")
    public void myReturnVisit_function_19() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), 1);
        String common = "一言均赋，四韵俱成。请洒潘江，各倾陆海云尔";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(taskId, common, "", date, "CONTACT_LATER", false, picture);
            Preconditions.checkArgument(result.getString("message").equals("成功"));
            //验证是否完成为未完成
            JSONArray list = crm.returnVisitTaskPage(1, 10, "", "").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getInteger("task_id") == taskId) {
                    String taskStatusName = list.getJSONObject(i).getString("task_status_name");
                    CommonUtil.valueView(taskStatusName);
                    Preconditions.checkArgument(taskStatusName.equals("未完成"), "第一次回访为稍后联系时,回访任务状态不为未完成");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访选择第一次稍后联系时,是否完成=未完成");
        }
    }

    @Test(description = "回访客户,错号,是否完成=已完成,所有等级产生的回访任务取消", enabled = false)
    public void myReturnVisit_function_20() {
        logger.logCaseStart(caseResult.getCaseName());

    }

    @Test(description = "回访记录,内容,汉字英文数字符号,10-200字")
    public void myReturnVisit_function_21() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String common = "(壬戌之秋)7w既望%苏子与客泛舟游于赤壁之下@清风徐来水波不兴。举酒属客，诵明月之诗，歌窈窕之章。少焉，月出于东山之上，徘徊于斗牛之间。白露横江，水光接天。纵一苇之所如，凌万顷之茫然。浩浩乎如冯虚御风，而不知其所止；飘飘乎如遗世独立，羽化而登仙。\n" +
                "于是饮酒乐甚，扣舷而歌之。歌曰：“桂棹兮兰桨，击空明兮溯流光。渺渺兮予怀，望美人兮天一方。”客有吹洞箫者，倚歌而和之。其声呜呜然，如怨如慕，如泣如诉";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(taskId, common, "", date, "ANSWER", false, picture);
            Preconditions.checkArgument(result.getString("message").equals("成功"), "回访记录200字,回访失败");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访记录,内容,汉字英文数字符号,10-200字");
        }
    }

    @Test(description = "回访截图,2张-3张")
    public void myReturnVisit_function_22() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String common = "举酒属客，诵明月之诗，歌窈窕之章";
        String picPath = "src/main/java/com/haisheng/framework/model/experiment/multimedia/goodsmanager/";
        String picture1 = new ImageUtil().getImageBinary(picPath + "车辆照片.jpg");
        String picture2 = new ImageUtil().getImageBinary(picPath + "外观照片.jpg");
        String picture3 = new ImageUtil().getImageBinary(picPath + "大图照片.jpg");
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访2张图片
            JSONObject result = crm.returnVisitTaskExecute(taskId, common, "", date, "CONTACT_LATER", false, picture1, picture2);
            Preconditions.checkArgument(result.getString("message").equals("成功"), "回访截图2张,回访失败");
            //回访3张图片
            JSONObject result1 = crm.returnVisitTaskExecute(taskId, common, "", date, "CONTACT_LATER", false, picture1, picture2, picture3);
            Preconditions.checkArgument(result1.getString("message").equals("成功"), "回访截图3张,回访失败");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访截图,2张-3张");
        }
    }

    @Test(description = "回访记录,200字以上")
    public void myReturnVisit_function_23() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String common = "壬戌之秋，七月既望，苏子与客泛舟游于赤壁之下。清风徐来，水波不兴。举酒属客，诵明月之诗，歌窈窕之章。少焉，月出于东山之上，徘徊于斗牛之间。白露横江，水光接天。纵一苇之所如，凌万顷之茫然。浩浩乎如冯虚御风，而不知其所止；飘飘乎如遗世独立，羽化而登仙。\n" +
                "于是饮酒乐甚，扣舷而歌之。歌曰：“桂棹兮兰桨，击空明兮溯流光。渺渺兮予怀，望美人兮天一方。”客有吹洞箫者，倚歌而和之。其声呜呜然，如怨如慕，如泣如诉，余音袅袅，不绝如缕。舞幽壑之潜蛟，泣孤舟之嫠妇。\n" +
                "苏子愀然，正襟危坐而问客曰：“何为其然也？”客曰：“月明星稀，乌鹊南飞，此非曹孟德之诗乎？西望夏口，东望武昌，山川相缪，郁乎苍苍，此非孟德之困于周郎者乎？方其破荆州，下江陵，顺流而东也，舳舻千里，旌旗蔽空，酾酒临江，横槊赋诗，固一世之雄也，而今安在哉？况吾与子渔樵于江渚之上，侣鱼虾而友麋鹿，驾一叶之扁舟，举匏樽以相属。寄蜉蝣于天地，渺沧海之一粟。哀吾生之须臾，羡长江之无穷。挟飞仙以遨游，抱明月而长终。知不可乎骤得，托遗响于悲风。”";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(taskId, common, "", date, "ANSWER", false, picture);
            Preconditions.checkArgument(result.getString("message").equals("下次回访内容长度必须在10和200之间"), "下次回访内容长度必须在10和200之外也可以回访成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("回访记录,内容,汉字英文数字符号,200字以上");
        }
    }

    @Test(description = "下次回访日期,昨天及之前")
    public void myReturnVisit_function_24() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        String common = "壬戌之秋，七月既望，苏子与客泛舟游于赤壁之下。";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(taskId, common, "", date, "ANSWER", false, picture);
            Preconditions.checkArgument(result.getString("message").equals("回访日期不允许在今日之前"), "下次回访日期在今日之前也可以回访成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("昨天及之前不可做为下次回访日期");
        }
    }

    @Test(description = "我的回访,不可以修改为订单客户")
    public void myReturnVisit_function_25() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String common = "壬戌之秋，七月既望，苏子与客泛舟游于赤壁之下。";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(taskId, common, "4", date, "ANSWER", false, picture);
            Preconditions.checkArgument(result.getString("message").equals("不可以修改为订单客户"), "修改为订单客户也可以回访成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访,不可以修改为订单客户");
        }
    }

    @Test(description = "我的回访,不可以修改为订单客户")
    public void myReturnVisit_function_26() {
        logger.logCaseStart(caseResult.getCaseName());
        String date = DateTimeUtil.getFormat(new Date());
        String common = "壬戌之秋，七月既望，苏子与客泛舟游于赤壁之下。";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String picture = new ImageUtil().getImageBinary(picPath);
        try {
            JSONObject response = crm.returnVisitTaskPage(1, 10, "", "");
            int taskId = CommonUtil.getIntField(response, 0, "task_id");
            //回访
            JSONObject result = crm.returnVisitTaskExecute(taskId, common, "4", date, "ANSWER", false, picture);
            Preconditions.checkArgument(result.getString("message").equals("不可以修改为订单客户"), "修改为订单客户也可以回访成功");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("我的回访,不可以修改为订单客户");
        }
    }

    /**
     * @description: 前台销售排班
     */
    @Test(description = "销售排班")
    public void saleOrder_function_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取销售排班
            JSONObject response = crm.saleOrderList();
            String saleId = CommonUtil.getStrField(response, 0, "sale_id");
            //销售排班
            crm.saleOrder(saleId, 2);
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("销售排班");
        }
    }

    @Test(enabled = false)
    public void afterSaleCustomer() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.publicFaceList();
            String analysisCustomerId = CommonUtil.getStrField(response, 0, "analysis_customer_id");
            crm.afterSalelCustomer(analysisCustomerId);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("售后客户标记");
        }
    }

//    ---------------------------------------------------私有方法区-------------------------------------------------------

    /**
     * 删除客户
     *
     * @param phone 客户电话号
     */
    private void deleteCustomer(String phone) {
        CommonUtil.login(EnumAccount.ZJL);
        JSONObject response = crm.customerList("", phone, "", "", "", 1, 10);
        if (!response.getJSONArray("list").isEmpty()) {
            int customerId = CommonUtil.getIntField(response, 0, "customer_id");
            crm.customerDelete(customerId);
        } else {
            System.out.println(response.getString("message"));
        }
    }
}
