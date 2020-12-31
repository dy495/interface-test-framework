package com.haisheng.framework.testng.bigScreen.jiaochen.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.inject.internal.cglib.proxy.$MethodProxy;
import com.google.inject.internal.util.$SourceProvider;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.Constant;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.SelectReception;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;


public class JcPc extends TestCaseCommon implements TestCaseStd {

    ScenarioUtil jc = ScenarioUtil.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp = new PublicParm();
    JcFunction pf = new JcFunction();
    FileUtil file = new FileUtil();
    Random random = new Random();
    public int page = 1;
    public int size = 50;
    public String name = "创建角色xia";
    public String email = "";
    public String phone = "";

    Integer status = 1;
    String type = "PHONE";

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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "xmf";
        commonConfig.referer = getJcReferdaily();
        commonConfig.produce = EnumTestProduce.JIAOCHEN_DAILY.name();

        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getName() + commonConfig.checklistQaOwner);


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
                {"这是一个二十字的角色名称是的是的是的", "角色名称为20个字，创建失败" },
                {"这是一个二十字的角色名称AABB1111", "角色名称为中文+字母+数字，创建失败" },
                {"这是一个二十字的角色名称AABB11.。", "角色名称为中文+字母+数字+字符，创建失败" },
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
                {"这是一个二十一字的角色名称是的是的是的是的", "角色名称需要在1-20个字内", "角色名称需要在1-20个字内" },
                {"别删-仅卡劵申请tab", "新增角色失败当前角色名称已存在！请勿重复添加", "重复的角色名称，创建成功" },
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
            Integer total = jc.pcStaffPage("", page, size).getInteger("total");

            List<String> r_dList = new ArrayList<String>();
            r_dList.add("417");

            List<String> shop_list = new ArrayList<String>();
            shop_list.add("46012");
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
            jc.organizationAccountEdit(account, reName, email, phone, r_dList, status, shop_list, type);
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
//    @Test
    public void Jc_accountInfoData_1() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            Integer total = jc.pcStaffPage("", page, size).getInteger("total");

            List<String> r_dList = new ArrayList<String>();
            r_dList.add("4");
            r_dList.add("99");

            List<String> shop_list = new ArrayList<String>();
            shop_list.add("4116");


            JSONArray list = jc.pcStaffPage("", page, size).getJSONArray("list");
            String today = dt.getHHmm(0);
            String account = "";
            String old_phone = "";
            String create_time = "";
            for (int i = 1; i < list.size(); i++) {
                create_time = list.getJSONObject(list.size() - 1).getString("create_time");
                if (!create_time.equals(today)) {
                    account = list.getJSONObject(list.size() - 1).getString("account");
                    old_phone = list.getJSONObject(list.size() - 1).getString("phone");
                    break;
                }
            }

            if (old_phone != "" && old_phone != null) {
                //编辑账号的名称，权限
                String reName = "自动化在测编辑";
                jc.organizationAccountEdit(account, reName, "", old_phone, r_dList, status, shop_list, type);
                //获取列表该账号
                JSONArray accountList = jc.pcStaffPage("", page, size).getJSONArray("list");
                String create_time_1 = "";
                String phone_1 = accountList.getJSONObject(0).getString("phone");//获取通过手机号搜索到的账号的手机号
                if (phone_1.equals(old_phone)) {
                    create_time_1 = accountList.getJSONObject(0).getString("create_time");

                }
                Preconditions.checkArgument(create_time_1.equals(create_time), "编辑昨天" + create_time + "的创建的账号" + old_phone + "列表该账号的创建时间变成了最新编辑的时间" + create_time_1);
                //编辑完以后获取列表的数量，是否有增多或者减少
                Integer total1 = jc.pcStaffPage("", page, size).getInteger("total");
                Preconditions.checkArgument(total == total1, "编辑一个账号，账号列表的数量由:" + total + "变成了" + total1);

            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("轿辰-编辑账号信息以后，创建者和创建时间是否发生改变");
        }

    }

    //禁用账户登录失败，开启登录成功
//    @Test
    public void Jc_accountStart() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray data = jc.roleListFilterManage("", "1", "100", "", "").getJSONArray("list");
            String id = "";
            for (int i = 1; i < data.size(); i++) {
                String status = data.getJSONObject(i).getString("status");
                if (status.equals("ENABLE")) {
                    id = data.getJSONObject(i).getString("id");
                    break;
                }
            }
            JSONArray list = jc.pcRoleList().getJSONArray("list");
            int total = list.size();
            //禁用开启按钮
            jc.organizationRoleButtom(id, "DISABLE");
            int totalA = jc.pcRoleList().getJSONArray("list").size();

            jc.organizationRoleButtom(id, "ENABLE");
            int totalB = jc.pcRoleList().getJSONArray("list").size();

            Preconditions.checkArgument(total - totalA == 1, "禁用角色列表-1");
            Preconditions.checkArgument(totalB - total == 0, "启用角色列表+1");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-禁用账户登录失败，开启登录成功");
        }
    }

    //查询案例

    /**
     * @description :接待管理查询   ---for  liya
     * @date :2020/8/3 12:48
     **/
//    @Test(dataProvider = "SELECT_PARM", dataProviderClass = ScenarioUtil.class)
    public void Jc_receptionSelect(String parm, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String shopId = "-1";
            JSONObject data = jc.afterSleCustomerManage(shopId, "1", "10", "", "");

//            JSONObject data = jc.brandListFilterManage1("", "1","10","","");

            String Expetresult = data.getJSONArray("list").getJSONObject(0).getString(parm);
            JSONArray list = jc.receptionManage(shopId, "1", "10", parm, Expetresult).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String SelectResult = list.getJSONObject(i).getString(output);
                Preconditions.checkArgument((Expetresult.equals(SelectResult)), "接待管理按" + parm + "查询，结果错误" + SelectResult);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-接待管理查询，结果校验");
        }
    }

    //    @Test()
    public void Jc_selectAppointmentRecodeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] ss = Constant.receptionManageFilter_pram();
            SelectReception sr = new SelectReception();
            JSONArray res = jc.receptionManage(pp.shopId, "1", "10", "", "").getJSONArray("list");
            JSONObject data = res.getJSONObject(0);

            sr.plate_number = data.getString(ss[0][1].toString());

            sr.reception_sale_id = data.getString(ss[1][1].toString());
            sr.reception_date = data.getString(ss[2][1].toString());

            JSONObject result = jc.receptionManageC(sr);

            Preconditions.checkArgument(result.getString(ss[0][1].toString()).contains(sr.plate_number), "");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-接待管理列表查询全填，结果校验");
        }
    }

    //    @Test(dataProvider = "SELECT_PreSleCustomerManageFilter",dataProviderClass = Constant.class)
    public void preSleCustomerManageOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.preSleCustomerManage("", "1", "10", "", "");
            int pages = respon.getInteger("pages");
            String result = respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.preSleCustomerManage("", String.valueOf(page), "10", pram, result).getJSONArray("list");
                for (int i = 0; i < 10; i++) {
                    String Flag = list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "销售管理按" + result + "查询，结果错误" + Flag);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售客户查询单项查询，结果校验");
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

    //    @Test  //pc修改工位，工作日和休息日不同步变更
    public void Jc_pcmaintainTableEdit() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("JC_pc修改工位，工作日和休息日不同步变更");
        }
    }


}
