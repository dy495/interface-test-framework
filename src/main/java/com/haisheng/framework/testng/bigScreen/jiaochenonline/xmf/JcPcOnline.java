package com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.commonDs.JsonPathUtil;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.file.FileUploadScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment.AppointmentTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.LoginApp;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.LoginPc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptionScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.role.RoleAddScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.role.RoleDeleteScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.role.RolePageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

import static com.google.common.base.Preconditions.checkArgument;


public class JcPcOnline extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCT = EnumTestProduct.JC_ONLINE_ZH;
    private static final EnumAccount ACCOUNT = EnumAccount.JC_ALL_ONLINE;
    private static final EnumAccount account = EnumAccount.JC_ALL_ONLINE_LXQ;
    private final VisitorProxy visitor = new VisitorProxy(PRODUCT);
    private final SceneUtil util = new SceneUtil(visitor);

    ScenarioUtilOnline jc = ScenarioUtilOnline.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParamOnline pp = new PublicParamOnline();
    JcFunctionOnline pf = new JcFunctionOnline();

    public int page = 1;
    public int size = 50;
    public String name = "创建角色xia";
    public String email = "";
    public String phone = "";
    CommonConfig commonConfig = new CommonConfig();

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.XMF.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCT.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(PRODUCT.getShopId()).setReferer(PRODUCT.getReferer()).setRoleId(PRODUCT.getRoleId()).setProduct(PRODUCT.getAbbreviation());
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);
        util.loginPc(ACCOUNT);
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


    //创建复合权限角色
//    @Test(dataProvider = "LIMITID", dataProviderClass = ScenarioUtil.class)
    public void createRole(int[] a) {
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
            collectMessage(e);
        } finally {
            saveData("新增角色");
        }


    }

    /**
     * ====================新增角色======================
     */
    @Test(description = "角色的CURD,数据校验")
    public void role_add() {
        try {
            String description = "自动化测试给店长用的角色";
            JSONArray moduleId = pp.roleList;
            //新增一个角色
            JSONObject res = jc.organizationRoleAdd(name, description, moduleId, true);
            int total = jc.roleListFilterManage("", "1", "10", "", "").getInteger("total");
            int[] page = pf.getPage(total);
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

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("新增删改查角色");
        }
    }

    /**
     * ====================新增角色(名称校验)======================
     */
    @DataProvider(name = "ROLE_NAME_SUCCESS")
    public static Object[][] roleNameSuccess() {
        return new String[][]{
                {"这是一个二十字的角色名称是的是的是的", "角色名称为20个字，创建失败"},
                {"这是一个二十字的角色名称AABB1111", "角色名称为中文+字母+数字，创建失败"},
                {"这是一个二十字的角色名称AABB11.。", "角色名称为中文+字母+数字+字符，创建失败"},
        };
    }

    //ok
    @Test(dataProvider = "roleNameSuccess")
    public void role_add_work2(String name, String mess) {
        Integer id = null;
        try {
            visitor.setProduct(EnumTestProduct.JC_ONLINE_JD);
            JSONArray moduleId = pp.roleList;
            String description = "自动化测试给店长自动化用的角色";
            IScene scene = RolePageScene.builder().build();
            Long total = scene.execute(visitor).getLong("total");
            int code = RoleAddScene.builder().name(name).description(description).authList(moduleId).build().visitor(visitor).getResponse().getCode();
            checkArgument(code == 1000, mess);
            Long newTotal = scene.execute(visitor).getLong("total");
            IScene rolePageScene = RolePageScene.builder().name(name).build();
            id = util.toJavaObject(rolePageScene, JSONObject.class, "name", name).getInteger("id");
            Preconditions.checkArgument(newTotal - total == 1, "新增角色前列表数：" + total + " 新增角色后列表数：" + newTotal);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            RoleDeleteScene.builder().id(id).build().execute(visitor);
            saveData("新增角色(名称校验)-正常");
        }
    }

    @DataProvider(name = "ROLE_NAME_FAIL")
    public static Object[][] roleNameFail() {
        return new String[][]{
                {"这是一个二十一字的角色名称是的是的是的是的", "角色名称需要在1-20个字内", "角色名称需要在1-20个字内"},
                {"别删-仅卡劵申请tab", "新增角色失败当前角色名称已存在！请勿重复添加", "重复的角色名称，创建成功"},
        };
    }

    //ok
    @Test(dataProvider = "ROLE_NAME_FAIL")
    public void role_add_workAb(String name, String res, String mess) {
        try {
            visitor.setProduct(EnumTestProduct.JC_ONLINE_JD);
            JSONArray moduleId = pp.roleList;
            String description = "自动化测试给店长自动化用的角色";
            String message = RoleAddScene.builder().authList(moduleId).description(description).name(name).build().visitor(visitor).getResponse().getMessage();
            checkArgument(message.equals(res), mess);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("新增角色(名称异常校验)");
        }
    }

    /**
     * ====================账户管理中的一致性========================
     */
    //ok
    @Test(description = "新增1个账号，列表+1；删除1个账号，列表-1；修改账号名称后与列表是否一致")
    public void accountInfoData() {
        try {
            String phone = "15555555555";
            int total = StaffPageScene.builder().build().execute(visitor).getInteger("total");
            util.getRandomRoleMap().forEach((roleId, roleName) -> {
                JSONArray shopList = util.getShopIdArray();
                String pic = new ImageUtil().getImageBinary("src/main/java/com/haisheng/framework/testng/bigScreen/itemYuntong/common/resources/picture/touxiang.jpg");
                String picPath = FileUploadScene.builder().pic("data:image/jpeg;base64," + pic).permanentPicType(0).ratio(1.0).ratioStr("1:1").build().execute(visitor).getString("pic_path");
                StaffAddScene.builder().phone(phone).name("克拉拉").shopList(shopList).roleId(roleId).roleName(roleName).picturePath(picPath).build().execute(visitor);
                int newTotal = StaffPageScene.builder().build().execute(visitor).getInteger("total");
                Preconditions.checkArgument(newTotal == total + 1, "新增一个账号前列表数：" + total + " 新增一个账号后列表数：" + newTotal);
                IScene scene = StaffPageScene.builder().phone(phone).build();
                JSONObject object = util.toFirstJavaObject(scene, JSONObject.class);
                String name = object.getString("name");
                String staffPhone = object.getString("phone");
                String createTime = object.getString("create_time");
                JSONObject role = object.getJSONArray("role_list").getJSONObject(0);
                String role_Name = role.getString("role_name");
                int role_id = role.getInteger("role_id");
                JSONArray shop_list = role.getJSONArray("shop_list");
                Preconditions.checkArgument(createTime.equals(DateTimeUtil.getFormat(new Date())));
                Preconditions.checkArgument(role_Name.equals(roleName));
                Preconditions.checkArgument(role_id == roleId);
                Preconditions.checkArgument(name.equals("克拉拉"));
                Preconditions.checkArgument(staffPhone.equals(phone));
                Preconditions.checkArgument(shop_list.equals(shopList));
                int deleteTotal = StaffPageScene.builder().build().execute(visitor).getInteger("total");
                util.deleteStaff("15555555555");
                int newDeleteTotal = com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.staff.StaffPageScene.builder().build().execute(visitor).getInteger("total");
                Preconditions.checkArgument(newDeleteTotal == deleteTotal - 1, "删除一个账号前列表数：" + deleteTotal + " 删除一个账号后列表数：" + newDeleteTotal);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("新增1个账号，列表+1；删除1个账号，列表-1；修改账号信息以后与列表是否一致");
        }

    }

    /**
     * ====================账户管理中的一致性========================
     */
    @Test  //编辑账号信息以后，创建者和创建时间是否发生改变
    public void accountInfoData_1() {
        try {
            visitor.setProduct(EnumTestProduct.JC_ONLINE_ZH);
            IScene staffPageScene = StaffPageScene.builder().phone(ACCOUNT.getPhone()).build();
            JSONObject response = staffPageScene.execute(visitor);
            int total = response.getInteger("total");
            JSONObject obj = response.getJSONArray("list").getJSONObject(0);
            JSONArray roleList = obj.getJSONArray("role_list");
            String id = obj.getString("id");
            String phone = obj.getString("phone");
            String createTime = obj.getString("create_time");
            String name = obj.getString("name");
            StaffEditScene.builder().roleList(roleList).id(id).phone(phone).name(name + "-改").build().execute(visitor);
            JSONObject newResponse = staffPageScene.execute(visitor);
            int newTotal = newResponse.getInteger("total");
            JSONObject newObj = newResponse.getJSONArray("list").getJSONObject(0);
            String newCreateTime = newObj.getString("create_time");
            Preconditions.checkArgument(createTime.equals(newCreateTime), "编辑前创建时间：" + createTime + " 编辑后创建时间：" + newCreateTime);
            Preconditions.checkArgument(total == newTotal, "编辑一个账号，账号列表的数量由:" + total + "变成了" + newTotal);
            StaffEditScene.builder().roleList(roleList).id(id).phone(phone).name(name).build().execute(visitor);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("编辑账号信息以后，创建者和创建时间是否发生改变");
        }
    }

    //禁用账户登录失败，开启登录成功
    @Test
    public void accountStart() {
        String id = null;
        try {
            JSONObject response = StaffPageScene.builder().phone(account.getPhone()).build().execute(visitor);
            id = response.getJSONArray("list").getJSONObject(0).getString("id");
            //关闭账号
            StatusChangeScene.builder().id(id).status("DISABLE").build().execute(visitor);
            int pcCode = LoginPc.builder().type(1).phone(account.getPhone()).verificationCode(account.getPassword()).build().visitor(visitor).getResponse().getCode();
            int appCode = LoginApp.builder().phone(account.getPhone()).verificationCode(account.getPassword()).build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(appCode == 1001, "账户禁用，app登陆预期为：1001" + "实际为：" + appCode);
            Preconditions.checkArgument(pcCode == 1001, "账户禁用，pc登陆预期为：1001" + "实际为：" + pcCode);
            //开启账号
            StatusChangeScene.builder().id(id).status("ENABLE").build().execute(visitor);
            int enablePcCode = LoginPc.builder().type(1).phone(account.getPhone()).verificationCode(account.getPassword()).build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(enablePcCode == 1000, "账户启用，pc登陆预期为：1000" + "实际为：" + enablePcCode);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            //开启账号
            StatusChangeScene.builder().id(id).status("ENABLE").build().execute(visitor);
            saveData("禁用账户登录失败，开启登录成功");
        }
    }

    /**
     * @description :开始接待接口车牌号异常验证
     * @date :2020/12/15 17:47
     **/
    @Test(description = "pc接待车牌号验证", dataProvider = "PLATE", dataProviderClass = ScenarioUtil.class)
    public void pcReceiptAb(String plate) {
        try {
            visitor.setProduct(EnumTestProduct.JC_ONLINE_JD);
            int code = ReceptionScene.builder().plateNumber(plate).build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(code == 1001, "接待车牌为：" + code + "时，预期code为1001 实际code为：" + code);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc接待车牌号验证");
        }
    }

    @Test(description = "pc接待搜索老车牌号展示项验证")
    public void pcReceipt() {
        try {
            visitor.setProduct(EnumTestProduct.JC_ONLINE_JD);
            util.loginPc(account);
            JSONObject response = ReceptionScene.builder().plateNumber(util.getPlatNumber(EnumAppletToken.JC_WM_DAILY.getPhone())).build().execute(visitor);
            String jsonpath = "$.arrive_times&&$.customers[*].voucher_list[*]&&$.er_code_url&&$.last_reception_sale_name&&$.last_arrive_time&&$.plate_number";
            JsonPathUtil.spiltString(response.toJSONString(), jsonpath);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc接待搜索老车牌号展示项验证");
        }
    }

    //pc-取消接待
    @Test()
    public void pcCancelReception() {
        try {
            util.loginApp(account);
            int appTask = util.getReceptionPageList().size();
            jc.appLogin(pp.jdgw, pp.jdgwpassword);
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
            collectMessage(e);
        } finally {
            saveData("pc取消接待，app任务-1");
        }
    }


    @Test(enabled = false)  //pc修改车预约价格，小程序对应变更
    public void pcmaintainPriceEdit() {
        try {
            commonConfig.setShopId("20032");
            commonConfig.setRoleId("424");
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
            jc.pcCarModelPriceEdit(pp.modelIdAppointment, price, "ENABLE", "MAINTAIN");

            //工位配置里的折扣
            JSONObject timeRangeDetail = jc.timeRangeDetail("MAINTAIN", dataType);

            JSONArray morning = timeRangeDetail.getJSONObject("morning").getJSONArray("list");
            JSONArray afternoon = timeRangeDetail.getJSONObject("afternoon").getJSONArray("list");
            Double[] discount = new Double[morning.size() + afternoon.size()];

            for (int i = 0; i < morning.size(); i++) {
                discount[i] = morning.getJSONObject(i).getDouble("discount");
            }
            for (int j = 0; j < afternoon.size(); j++) {
                discount[morning.size() + j] = afternoon.getJSONObject(j).getDouble("discount");
            }

            //小程序这个车预约的价格
            jc.appletLoginToken(pp.appletToken);
            JSONArray appletTime = jc.appletmaintainTimeList(Long.parseLong(pp.shopIdZ), pp.car_idA, dt.getHistoryDate(num), AppointmentTypeEnum.MAINTAIN.name()).getJSONArray("list");
            Preconditions.checkArgument(discount.length == appletTime.size(), "pc配置的预约时间段与小程序展示不一致");
            for (int z = 0; z < appletTime.size(); z++) {
                String priceApplet = appletTime.getJSONObject(z).getString("price");
                Double result = price * discount[z];
                String pp = priceApplet.substring(1);
                Double pa = Double.valueOf(pp);
                Preconditions.checkArgument(result.equals(pa), "预约价格异常" + result + ":" + pa);
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            commonConfig.setShopId("-1");
            saveData("pc修改预约配置验证");
        }
    }

    @Test  //pc修改工位，工作日和休息日不同步变更
    public void pcmaintainTableEdit() {
        try {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);

            jc.pcLogin(pp.jdgw, pp.gwpassword);
            //修改配置前，工作日的配置
            JSONObject timeDetailWeekday = jc.timeRangeDetail("MAINTAIN", "WEEKDAY");
            JSONObject afternoonBefore = timeDetailWeekday.getJSONObject("afternoon");
            JSONObject morningBefore = timeDetailWeekday.getJSONObject("morning");

            //修改休息日的配置
            JSONObject timeDetailWeekend = jc.timeRangeDetail("MAINTAIN", "WEEKEND");
            JSONObject morning1 = timeDetailWeekend.getJSONObject("morning");
            JSONObject afternoon1 = timeDetailWeekend.getJSONObject("afternoon");

            if (day % 2 == 1) {
                morning1.put("reply_start", "09:00");  //奇数天 设置9：00
            } else {
                morning1.put("reply_start", "08:00");
            }

            timeDetailWeekend.put("morning", morning1);
            timeDetailWeekend.put("date_type", "WEEKDAY");
            timeDetailWeekend.put("type", "MAINTAIN");
            //修改工作日工位配置
            jc.appointmentTimeEdit(timeDetailWeekend);

            //修改之后的配置,工作日
            JSONObject timeDetailWeekdayAfter = jc.timeRangeDetail("MAINTAIN", "WEEKDAY");
            JSONObject afternoonAfter = timeDetailWeekdayAfter.getJSONObject("afternoon");
            JSONObject morningAfter = timeDetailWeekdayAfter.getJSONObject("morning");
            //休息日
            JSONObject timeDetailWeekendAfter = jc.timeRangeDetail("MAINTAIN", "WEEKEND");
            JSONObject afternoonAfterend = timeDetailWeekendAfter.getJSONObject("afternoon");
            JSONObject morningAfterend = timeDetailWeekendAfter.getJSONObject("morning");

            //工作日
            System.out.println(afternoonAfter.equals(afternoonBefore));
            System.out.println(morningAfter.equals(morningBefore));
            //休息日
            System.out.println(afternoonAfterend.equals(afternoon1));
            System.out.println(morningAfterend.equals(morning1));


        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            saveData("pc修改工位，工作日和休息日不同步变更");
        }
    }


    @Test(enabled = false) //pc开关预约配置按钮，小程序对应变更
    public void pcappointmentButton() {
        try {
            JSONObject data = jc.maintainFilterManage("", "1", "10", "car_model", pp.carModel).getJSONArray("list").getJSONObject(0);
            String status = data.getString("status");
            if (!status.equals("ENABLE")) {
                throw new Exception(pp.carModel + "车型,预约配置被关闭");
            }
            jc.pcCarModelPriceEdit(pp.modelIdAppointment, null, "DISABLE", "MAINTAIN");
            jc.appletLoginToken(pp.appletToken);
            JSONObject isAble = jc.appletmaintainTimeList(Long.parseLong(pp.shopIdZ), pp.car_idA, dt.getHistoryDate(1), AppointmentTypeEnum.MAINTAIN.name(), false);
            int code = isAble.getInteger("code");
            String message = isAble.getString("message");

            jc.pcLogin(pp.gwphone, pp.gwpassword);
            jc.pcCarModelPriceEdit(pp.modelIdAppointment, null, "ENABLE", "MAINTAIN");


            Preconditions.checkArgument(code == 1001, "预约配置关闭小程序预约保养页返回" + message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            saveData("pc修改预约配置验证");
        }
    }

    @Test(enabled = false)  //pc门店预约关闭，小程序对应变更
    public void pcShopAppointmentButton() {
        try {
            JSONObject data = jc.shopListFilterManage("", "1", "10", "name", pp.shopName).getJSONArray("list").getJSONObject(0);
            String status = data.getString("appointment_status");
            if (!status.equals("ENABLE")) {
                throw new Exception(pp.shopName + "门店,预约开关被关闭了");
            }
            //配置前预约门店配置列表
            jc.appletLoginToken(pp.appletToken);
            JSONArray isAble = jc.appletmaintainShopList(pp.car_idA.toString(), pp.coordinate).getJSONArray("list");
            int total = isAble.size();
            //关闭门店预约配置
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            jc.shopStatusChange(pp.shopIdZ, "APPOINTMENT", "DISABLE");
            //小程序预约门店列表
            jc.appletLoginToken(pp.appletToken);
            JSONArray isAbleAfter = jc.appletmaintainShopList(pp.car_idA.toString(), pp.coordinate).getJSONArray("list");
            int totalAfter = isAbleAfter.size();

            jc.pcLogin(pp.gwphone, pp.gwpassword);
            jc.shopStatusChange(pp.shopIdZ, "APPOINTMENT", "ENABLE");
            Preconditions.checkArgument(total - totalAfter == 1, "关闭预约配置，小程序预约门店-1");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            saveData("pc修改预约配置验证");
        }
    }

    @Test  //pc门店开关关闭，预约和洗车开关自动关闭
    public void pcShopButton() {
        try {
            String statusAll = "DISABLE";
            String open = "ENABLE";
            JSONObject data = jc.shopListFilterManage("", "1", "10", "name", pp.shopName).getJSONArray("list").getJSONObject(0);
            String status1 = data.getString("status");
            if (!status1.equals("ENABLE")) {
                statusAll = "ENABLE";
                open = "DISABLE";
            }
            //关闭门店预约配置
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            jc.shopStatusChange(pp.shopIdZ, "SHOP", statusAll);
            JSONObject dataAfter = jc.shopListFilterManage("", "1", "10", "name", pp.shopName).getJSONArray("list").getJSONObject(0);
            String status = dataAfter.getString("status");
            String appointment_status = dataAfter.getString("appointment_status");
            String washing_status = dataAfter.getString("washing_status");
            Preconditions.checkArgument(appointment_status.equals(statusAll) && washing_status.equals(statusAll) && status.equals(statusAll), "门店开关未同步");

            jc.shopStatusChange(pp.shopIdZ, "SHOP", open);
            JSONObject dataAfter2 = jc.shopListFilterManage("", "1", "10", "name", pp.shopName).getJSONArray("list").getJSONObject(0);
            String status2 = dataAfter2.getString("status");
            String appointment_status2 = dataAfter2.getString("appointment_status");
            String washing_status2 = dataAfter2.getString("washing_status");
            Preconditions.checkArgument(appointment_status2.equals(statusAll) && washing_status2.equals(statusAll) && status2.equals(open), "门店开关未同步2");
            jc.shopStatusChange(pp.shopIdZ, "WASHING", open);
            jc.shopStatusChange(pp.shopIdZ, "APPOINTMENT", open);

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc门店按钮修改关联验证");
        }
    }


}
