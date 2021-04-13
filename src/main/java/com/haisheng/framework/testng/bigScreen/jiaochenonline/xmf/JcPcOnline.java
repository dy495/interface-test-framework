package com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.JsonPathUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Calendar;

import static com.google.common.base.Preconditions.checkArgument;


public class JcPcOnline extends TestCaseCommon implements TestCaseStd {

    ScenarioUtilOnline jc = ScenarioUtilOnline.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParmOnline pp = new PublicParmOnline();
    JcFunctionOnline pf = new JcFunctionOnline();
    JsonPathUtil jpu = new JsonPathUtil();

    public int page = 1;
    public int size = 50;
    public String name = "创建角色xia";
    public String email = "";
    public String phone = "";


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();



        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "xmf";
        commonConfig.referer = EnumTestProduce.JC_ONLINE.getReferer();
        commonConfig.product = EnumTestProduce.JC_ONLINE.getAbbreviation();


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JC_ONLINE.getDesc() + commonConfig.checklistQaOwner);


        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};
//        commonConfig.referer="http://dev.dealer-jc.winsenseos.cn/authpage/login";
        //set shop id
        commonConfig.shopId = "-1";
        beforeClassInit(commonConfig);

        logger.debug("jc: " + jc);
        jc.pcLogin(pp.gwname, pp.gwpassword);


    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }


    //创建复合权限角色
//    @Test(dataProvider = "LIMITID", dataProviderClass = ScenarioUtil.class)
    public void Jc_createRole(int a[]) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = "" + a[0];
            JSONArray moduleId = new JSONArray();
            for (int i = 1; i < a.length; i++) {
                moduleId.add(a[i]);
            }
            String description = "自动创建" + a[0];
            //新增一个角色
            jc.organizationRoleAdd(name, description, moduleId, true);

            int size = jc.organizationRolePage(1, 10).getInteger("total");
            if (size < 100) {
                JSONArray list = jc.organizationRolePage(name, 1, 100).getJSONArray("list");
                Long role_id = list.getJSONObject(list.size() - 1).getLong("id");
//                jc.organizationRoleDelete(role_id, true);
            } else {
                logger.warn("警告：角色数量超过100个，不在删除新增角色，将造成数据冗余");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-新增角色");
        }


    }

    /**
     * ====================新增角色======================
     */
    @Test(description = "角色的CURD,数据校验")
    public void Jc_role_add() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            String description = "自动化测试给店长用的角色";
            JSONArray moduleId = pp.roleList;
            //新增一个角色
            JSONObject res = jc.organizationRoleAdd(name, description, moduleId, true);
            int total = jc.roleListFilterManage("", "1", "10", "", "").getInteger("total");
            int page[] = pf.getPage(total);
            String id = jc.roleListFilterManage("", "" + page[0], "10", "", "").getJSONArray("list").getJSONObject(page[1]).getString("id");
            //编辑角色
            String name1 = "AUTOtest在编辑";
            Integer code1 = jc.organizationRoleEdit(Long.parseLong(id), name1, description, moduleId).getInteger("code");

            checkArgument(code1 == 1000, "编辑角色的信息失败了");

            //列表中编辑过的角色是否已更新
            JSONArray list1 = jc.organizationRolePage(name1, 1, 10).getJSONArray("list");
            String role_name = list1.getJSONObject(0).getString("name");

            checkArgument(name1.equals(role_name), "编辑过的角色没有更新在列表");

            jc.organizationidRoleDelete(id);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("轿辰-新增删改查角色");
        }

    }

    /**
     * ====================新增角色(名称校验)======================
     */
    @DataProvider(name = "ROLENAME")
    public static Object[][] rolename() {
        return new String[][]{
                {"这是一个二十字的角色名称是的是的是的", "角色名称为20个字，创建失败"},
                {"这是一个二十字的角色名称AABB1111", "角色名称为中文+字母+数字，创建失败"},
                {"这是一个二十字的角色名称AABB11.。", "角色名称为中文+字母+数字+字符，创建失败"},
        };
    }

    @Test(dataProvider = "ROLENAME")  //ok
    public void Jc_role_add_work2(String name, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray moduleId = pp.roleList;
            int totalB = jc.roleListFilterManage("", "1", "10", "", "").getInteger("total");

            String description = "自动化测试给店长自动化用的角色";
            JSONObject res = jc.organizationRoleAdd(name, description, moduleId);
            checkArgument(res.getInteger("code") == 1000, mess);
            int total = jc.roleListFilterManage("", "1", "10", "", "").getInteger("total");
            int page[] = pf.getPage(total);
            String id = jc.roleListFilterManage("", "" + page[0], "10", "", "").getJSONArray("list").getJSONObject(page[1]).getString("id");
            jc.organizationidRoleDelete(id);
            Preconditions.checkArgument(total - totalB == 1, "新增角色列表没+1");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-新增角色(名称校验)-正常");
        }
    }

    @DataProvider(name = "ROLENAMEAB")
    public static Object[][] rolenameab() {
        return new String[][]{
                {"这是一个二十一字的角色名称是的是的是的是的", "角色名称需要在1-20个字内", "角色名称需要在1-20个字内"},
                {"别删-仅卡劵申请tab", "新增角色失败当前角色名称已存在！请勿重复添加", "重复的角色名称，创建成功"},
        };
    }

    @Test(dataProvider = "ROLENAMEAB")  //ok
    public void Jc_role_add_workAb(String name, String res, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray moduleId = pp.roleList;
            String description = "自动化测试给店长自动化用的角色";

            JSONObject res3 = jc.organizationRoleAdd(name, description, moduleId);
            checkArgument(res3.getString("message").equals(res), mess);


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-新增角色(名称异常校验)");
        }

    }


    /**
     * ====================账户管理中的一致性========================
     */
    @Test(description = "新增1个账号，列表+1；删除1个账号，列表-1；修改账号名称后与列表是否一致")    //ok
    public void Jc_accountInfoData() {
        try {
            jc.pcLogin(pp.gwphone,pp.gwpassword);
            Integer total = jc.pcStaffPage("", page, size).getInteger("total");

            JSONArray r_dList = new JSONArray();
            r_dList.add(423);

            JSONArray shop_list = new JSONArray();
            shop_list.add(20032);
            String phone = pf.genPhoneNum();
            //用EMAIL新增一个账号
            JSONObject res = jc.organizationAccountAdd(name, phone, r_dList, shop_list);

            JSONArray accountList = jc.pcStaffPage(name, 1, 10).getJSONArray("list");
            String account = accountList.getJSONObject(0).getString("id");

            //新增账号以后，再查询列表
            Integer total1 = jc.pcStaffPage("", page, size).getInteger("total");
            int result = total1 - total;
            Preconditions.checkArgument(result == 1, "新增1个账号，账号列表的数量却加了：" + result);


            //编辑账号的名称，是否与列表该账号的一致
            String reName = "自动化测编辑";
            jc.organizationAccountEdit(account, reName,  phone, r_dList,  shop_list);
            JSONArray accountsList = jc.pcStaffPage(reName, page, size).getJSONArray("list");
            String name_1 = accountsList.getJSONObject(0).getString("name");
            Preconditions.checkArgument(name_1.equals(reName), "修改账号：" + account + "的名称为：" + reName + "修改后，该账号的名称为：" + name_1);


            //删除账号以后，再查询列表
            Integer code1 = jc.organizationAccountDelete(account).getInteger("code");
            Preconditions.checkArgument(code1 == 1000, "删除emial的账号:" + email + "失败了");
            Integer total2 = jc.pcStaffPage("", page, size).getInteger("total");
            int result1 = total1 - total2;
            Preconditions.checkArgument(result1 == 1, "删除1个账号，账号列表的数量却减了：" + result);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("轿辰-新增1个账号，列表+1；删除1个账号，列表-1；修改账号信息以后与列表是否一致");
        }

    }

    /**
     * ====================账户管理中的一致性========================
     */
    @Test  //编辑账号信息以后，创建者和创建时间是否发生改变
    public void Jc_accountInfoData_1() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            Integer total = jc.pcStaffPage("", page, size).getInteger("total");

            JSONArray list = jc.pcStaffPage("", page, size).getJSONArray("list");
            String today = dt.getHistoryDate(0);
            String id = "";
            String old_phone = "";
            String name = "";
            String create_time = "";

            JSONArray r_dList=new JSONArray();
            JSONArray shop_list = new JSONArray();

            for (int i = 1; i < list.size(); i++) {
                create_time = list.getJSONObject(i).getString("create_time");
                if (!create_time.equals(today)) {
                    id = list.getJSONObject(i).getString("id");
                    name = list.getJSONObject(i).getString("name");
                    old_phone = list.getJSONObject(i).getString("phone");
                    r_dList=list.getJSONObject(i).getJSONArray("role_list");
                    shop_list=list.getJSONObject(i).getJSONArray("shop_list");
                    break;
                }
            }
            JSONArray r_dList2=new JSONArray();
            JSONArray shop_list2 = new JSONArray();

            for(int i=0;i<r_dList.size();i++){
                String rid=r_dList.getJSONObject(i).getString("role_id");
                r_dList2.add(rid);
            }
            for(int i=0;i<shop_list.size();i++){
                String rid=shop_list.getJSONObject(i).getString("shop_id");
                shop_list2.add(rid);
            }
            if (old_phone != "" && old_phone != null) {
                //编辑账号的名称，权限
                jc.organizationAccountEdit(id, name, old_phone, r_dList2, shop_list2);
                //获取列表该账号
                JSONArray accountList = jc.pcStaffPage(name, page, size).getJSONArray("list");
                String create_time_1 = "";
                String phone_1 = accountList.getJSONObject(0).getString("phone");//获取通过手机号搜索到的账号的手机号
                if (phone_1.equals(old_phone)) {
                    create_time_1 = accountList.getJSONObject(0).getString("create_time");

                }
                Preconditions.checkArgument(create_time_1.equals(create_time), "编辑昨天" + create_time + "的创建的账号" + old_phone + "列表该账号的创建时间变成了最新编辑的时间" + create_time_1);
                //编辑完以后获取列表的数量，是否有增多或者减少
                Integer total1 = jc.pcStaffPage(null, page, size).getInteger("total");
                Preconditions.checkArgument(total == total1, "编辑一个账号，账号列表的数量由:" + total + "变成了" + total1);

            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("轿辰-编辑账号信息以后，创建者和创建时间是否发生改变");
        }

    }

    //禁用账户登录失败，开启登录成功
    @Test
    public void Jc_accountStart() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray data = jc.staffListFilterManage(null, "1", "100", "name", "接待顾问2").getJSONArray("list");
            String id = "";
            for (int i = 0; i < data.size(); i++) {
                String status = data.getJSONObject(i).getString("status");
                if (status.equals("ENABLE")) {
                    id = data.getJSONObject(i).getString("id");
                    break;
                }else{
                    throw new Exception("账户:接待顾问2 被关闭了");
                }
            }

            //禁用开启按钮
            jc.organizationAccountButtom(id, "DISABLE");
            int codeApp=jc.appLogin2(pp.jdgw2,pp.jdgwpassword,false).getInteger("code");
            int codePc=jc.pcTryLogin(pp.jdgw2,pp.jdgwpassword,false).getInteger("code");

            jc.organizationAccountButtom(id, "ENABLE");
            int codePcAfter=jc.pcTryLogin(pp.jdgw2,pp.jdgwpassword,false).getInteger("code");

            Preconditions.checkArgument(codeApp==1001,"账户禁用，app仍登录成功");
            Preconditions.checkArgument(codePc==1001,"账户禁用，pc仍登录成功");
            Preconditions.checkArgument(codePcAfter==1000,"账户启用，pc登录失败");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-禁用账户登录失败，开启登录成功");
        }
    }


    /**
     * @description :开始接待接口车牌号异常验证
     * @date :2020/12/15 17:47
     **/
    @Test(description = "pc接待车牌号验证", dataProvider = "PLATE", dataProviderClass = ScenarioUtil.class)
    public void Jc_pcReceiptAb(String plate) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int code = jc.pcManageReception(plate, false).getInteger("code");
            Preconditions.checkArgument(code == 1001, "异常车牌号依然成功");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc接待车牌号验证");
        }
    }

    @Test(description = "pc接待搜索老车牌号展示项验证")
    public void Jc_pcReceipt() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.pcManageReception(pp.carplate, true);
            String jsonpath = "$.arrive_times&&$.customers[*].voucher_list[*]&&$.er_code_url&&$.last_reception_sale_name&&$.last_arrive_time&&$.plate_number";
            jpu.spiltString(data.toJSONString(), jsonpath);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc接待搜索老车牌号展示项验证");
        }
    }

    //pc-取消接待
    @Test()
    public void Jc_pcCancleReception() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appLogin(pp.jdgw, pp.jdgwpassword);
            int appTask = pf.appReceptionPage();
            jc.pcLogin(pp.jdgw, pp.jdgwpassword);
            //取消接待
            JSONObject dd = jc.receptionManage("", "1", "10", "", "").getJSONArray("list").getJSONObject(0);
            long receptionID = dd.getLong("reception_id");
            long shopId = dd.getLong("shop_id");
            String reception_status_name = dd.getString("reception_status_name");
            if (!reception_status_name.equals("接待中")) {
                logger.warn("暂无接待中客户");
                return;
            }
            jc.pcCancelReception(receptionID, shopId);
            jc.appLogin(pp.jdgw, pp.jdgwpassword);
            int appTaskA = pf.appReceptionPage();
            Preconditions.checkArgument(appTask - appTaskA == 1, "pc取消接待，app接待任务没-1");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc取消接待，app任务-1");
        }
    }


    @Test  //pc修改车预约价格，小程序对应变更
    public void Jc_pcmaintainPriceEdit() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int num = 0;
            String dataType = "WEEKDAY";
            jc.pcLogin(pp.jdgw, pp.gwpassword);
            //判断今天是否是周末，周末就取周末的折扣配置
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, num);
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                dataType = "WEEKEND";
            }
            Double price = 300.00;
            //修改预约价格
            jc.pcCarModelPriceEdit(pp.modolIdAppointment, price, null);

            //工位配置里的折扣
            JSONObject timeRangeDetail = jc.timeRangeDetail("MAINTAIN", dataType);

            JSONArray morning = timeRangeDetail.getJSONObject("morning").getJSONArray("list");
            JSONArray afternoon = timeRangeDetail.getJSONObject("afternoon").getJSONArray("list");
            Double discount[] = new Double[morning.size() + afternoon.size()];

            for (int i = 0; i < morning.size(); i++) {
                discount[i] = morning.getJSONObject(i).getDouble("discount");
            }
            for (int j = 0; j < afternoon.size(); j++) {
                discount[morning.size() + j] = afternoon.getJSONObject(j).getDouble("discount");
            }

            //小程序这个车预约的价格
            jc.appletLoginToken(pp.appletTocken);
            JSONArray appletTime = jc.appletmaintainTimeList(Long.parseLong(pp.shopIdZ), pp.car_idA, dt.getHistoryDate(num)).getJSONArray("list");
            Preconditions.checkArgument(discount.length == appletTime.size(), "pc配置的预约时间段与小程序展示不一致");
            for (int z = 0; z < appletTime.size(); z++) {
                String priceApplet = appletTime.getJSONObject(z).getString("price");
                Double result = price * discount[z];
                String pp = priceApplet.substring(1);
                Double pa = Double.valueOf(pp);
                Preconditions.checkArgument(result.equals(pa), "预约价格异常" + result + ":" + pa);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("JC_pc修改预约配置验证");
        }
    }

    @Test  //pc修改工位，工作日和休息日不同步变更
    public void Jc_pcmaintainTableEdit() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Calendar calendar=Calendar.getInstance();
            int day=calendar.get(Calendar.DAY_OF_WEEK);

            jc.pcLogin(pp.jdgw,pp.gwpassword);
            //修改配置前，工作日的配置
            JSONObject timeDetailWeekday=jc.timeRangeDetail("MAINTAIN","WEEKDAY");
            JSONObject afternoonBefore=timeDetailWeekday.getJSONObject("afternoon");
            JSONObject morningBefore=timeDetailWeekday.getJSONObject("morning");

            //修改休息日的配置
            JSONObject timeDetailWeekend=jc.timeRangeDetail("MAINTAIN","WEEKEND");
            JSONObject morning1=timeDetailWeekend.getJSONObject("morning");
            JSONObject afternoon1=timeDetailWeekend.getJSONObject("afternoon");

            JSONObject morning=morning1;
            if(day%2==1){
                morning.put("reply_start","09:00");  //奇数天 设置9：00
            }else{
                morning.put("reply_start","08:00");
            }

            timeDetailWeekend.put("morning",morning);
            timeDetailWeekend.put("date_type","WEEKDAY");
            timeDetailWeekend.put("type","MAINTAIN");
            //修改工作日工位配置
            jc.appointmentTimeEdit(timeDetailWeekend);

            //修改之后的配置,工作日
            JSONObject timeDetailWeekdayAfter=jc.timeRangeDetail("MAINTAIN","WEEKDAY");
            JSONObject afternoonAfter=timeDetailWeekdayAfter.getJSONObject("afternoon");
            JSONObject morningAfter=timeDetailWeekdayAfter.getJSONObject("morning");
            //休息日
            JSONObject timeDetailWeekendAfter=jc.timeRangeDetail("MAINTAIN","WEEKEND");
            JSONObject afternoonAfterend=timeDetailWeekendAfter.getJSONObject("afternoon");
            JSONObject morningAfterend=timeDetailWeekendAfter.getJSONObject("morning");

            //工作日
            System.out.println(afternoonAfter.equals(afternoonBefore));
            System.out.println(morningAfter.equals(morningBefore));
            //休息日
            System.out.println(afternoonAfterend.equals(afternoon1));
            System.out.println(morningAfterend.equals(morning1));



        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            jc.pcLogin(pp.gwphone,pp.gwpassword);
            saveData("JC_pc修改工位，工作日和休息日不同步变更");
        }
    }


    @Test  //pc开关预约配置按钮，小程序对应变更
    public void Jc_pcappointmentButton() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data=jc.maintainFilterManage("","1","10","car_model",pp.carModel).getJSONArray("list").getJSONObject(0);
            String status=data.getString("status");
            if(!status.equals("ENABLE")){
                throw new Exception(pp.carModel+"车型,预约配置被关闭");
            }
            jc.pcCarModelPriceEdit(pp.modolIdAppointment,null,"DISABLE");

            jc.appletLoginToken(pp.appletTocken);
            JSONObject isAble=jc.appletmaintainTimeList(Long.parseLong(pp.shopIdZ),pp.car_idA,dt.getHistoryDate(1),false);
            int code= isAble.getInteger("code");
            String message= isAble.getString("message");

            jc.pcLogin(pp.gwphone,pp.gwpassword);
            jc.pcCarModelPriceEdit(pp.modolIdAppointment,null,"ENABLE");


            Preconditions.checkArgument(code==1001,"预约配置关闭小程序预约保养页返回"+message);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            jc.pcLogin(pp.gwphone,pp.gwpassword);
            saveData("JC_pc修改预约配置验证");
        }
    }

    @Test  //pc门店预约关闭，小程序对应变更
    public void Jc_pcShopAppointmentButton() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data=jc.shopListFilterManage("","1","10","name",pp.shopname).getJSONArray("list").getJSONObject(0);
            String status=data.getString("appointment_status");
            if(!status.equals("ENABLE")){
                throw new Exception(pp.shopname+"门店,预约开关被关闭了");
            }
            //配置前预约门店配置列表
            jc.appletLoginToken(pp.appletTocken);
            JSONArray isAble=jc.appletmaintainShopList(pp.car_idA.toString(),pp.coordinate).getJSONArray("list");
            int total=isAble.size();
            //关闭门店预约配置
            jc.pcLogin(pp.gwphone,pp.gwpassword);
            jc.shopStatusChange(pp.shopIdZ,"APPOINTMENT","DISABLE");
            //小程序预约门店列表
            jc.appletLoginToken(pp.appletTocken);
            JSONArray isAbleAfter=jc.appletmaintainShopList(pp.car_idA.toString(),pp.coordinate).getJSONArray("list");
            int totalAfter=isAbleAfter.size();

            jc.pcLogin(pp.gwphone,pp.gwpassword);
            jc.shopStatusChange(pp.shopIdZ,"APPOINTMENT","ENABLE");
            Preconditions.checkArgument(total-totalAfter==1,"关闭预约配置，小程序预约门店-1");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            jc.pcLogin(pp.gwphone,pp.gwpassword);
            saveData("JC_pc修改预约配置验证");
        }
    }

    @Test  //pc门店开关关闭，预约和洗车开关自动关闭
    public void Jc_pcShopButton() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String statusAll="DISABLE";
            String open="ENABLE";
            JSONObject data=jc.shopListFilterManage("","1","10","name",pp.shopname).getJSONArray("list").getJSONObject(0);
            String status1=data.getString("status");
            if(!status1.equals("ENABLE")){
                statusAll="ENABLE";
                open="DISABLE";
            }
            //关闭门店预约配置
            jc.pcLogin(pp.gwphone,pp.gwpassword);
            jc.shopStatusChange(pp.shopIdZ,"SHOP",statusAll);
            JSONObject dataAfter=jc.shopListFilterManage("","1","10","name",pp.shopname).getJSONArray("list").getJSONObject(0);
            String status=dataAfter.getString("status");
            String appointment_status=dataAfter.getString("appointment_status");
            String washing_status=dataAfter.getString("washing_status");
            Preconditions.checkArgument(appointment_status.equals(statusAll)&&washing_status.equals(statusAll)&&status.equals(statusAll),"门店开关未同步");

            jc.shopStatusChange(pp.shopIdZ,"SHOP",open);
            JSONObject dataAfter2=jc.shopListFilterManage("","1","10","name",pp.shopname).getJSONArray("list").getJSONObject(0);
            String status2=dataAfter2.getString("status");
            String appointment_status2=dataAfter2.getString("appointment_status");
            String washing_status2=dataAfter2.getString("washing_status");
            Preconditions.checkArgument(appointment_status2.equals(statusAll)&&washing_status2.equals(statusAll)&&status2.equals(open),"门店开关未同步2");
            jc.shopStatusChange(pp.shopIdZ,"WASHING",open);
            jc.shopStatusChange(pp.shopIdZ,"APPOINTMENT",open);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("JC_pc门店按钮修改关联验证");
        }
    }



}
