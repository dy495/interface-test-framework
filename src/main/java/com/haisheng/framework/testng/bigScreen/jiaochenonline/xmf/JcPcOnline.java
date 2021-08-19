package com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.commonDs.JsonPathUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.manage.MaintainCarModelPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.role.RolePageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.shop.ShopPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.shopstylemodel.ManageModelPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment.AppointmentTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletAppointmentTimeListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.UploadScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.LoginApp;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.LoginPc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage.AppointmentTimeRangeDetailScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage.AppointmentTimeRangeEditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage.MaintainCarModelPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptionScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.role.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shop.ShopPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shop.ShopStatusChangeScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shopstylemodel.ManageModelEditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shopstylemodel.ManageModelPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StatusChangeScene;
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
    private static final EnumAppletToken APPLET_USER = EnumAppletToken.JC_LXQ_ONLINE;
    private final VisitorProxy visitor = new VisitorProxy(PRODUCT);
    private final SceneUtil util = new SceneUtil(visitor);
    private final CommonConfig commonConfig = new CommonConfig();
    ScenarioUtilOnline jc = ScenarioUtilOnline.getInstance();
    PublicParamOnline pp = new PublicParamOnline();

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
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        util.loginPc(ACCOUNT);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        logger.logCaseStart(caseResult.getCaseName());
    }

    @AfterMethod
    public void restoreProduct() {
        visitor.setProduct(EnumTestProduct.JC_ONLINE_ZH);
    }

    /**
     * ====================新增角色======================
     */
    @Test(description = "角色的CURD,数据校验")
    public void role_add() {
        Integer id = null;
        try {
            String name = "创建角色xia";
            String description = "自动化测试给店长用的角色";
            IScene rolePageScene = RolePageScene.builder().build();
            Long total = rolePageScene.visitor(visitor).execute().getLong("total");
            //新增一个角色
            JSONObject response = RoleListScene.builder().build().visitor(visitor).execute();
            JSONObject obj = response.getJSONArray("list").getJSONObject(0);
            Long parentId = obj.getLong("id");
            JSONArray authList = obj.getJSONArray("auth_list");
            RoleAddScene.builder().name(name).description(description).authList(authList).parentRoleId(parentId).build().visitor(visitor).execute();
            response = rolePageScene.visitor(visitor).execute();
            Long addTotal = response.getLong("total");
            Preconditions.checkArgument(addTotal == total + 1, "添加角色前列表数：" + total + " 添加角色后列表数：" + addTotal);
            //编辑角色
            id = util.toJavaObject(rolePageScene, RolePageBean.class, "name", name).getId();
            Integer code = RoleEditScene.builder().parentRoleId(parentId).authList(authList).description(description).name(name + "-改").id(id).build().visitor(visitor).getResponse().getCode();
            checkArgument(code == 1000, "编辑角色的信息预期code：1000 实际code：" + code);
            String newName = util.toJavaObject(rolePageScene, RolePageBean.class, "id", id).getName();
            checkArgument(newName.equals(name + "-改"), "编辑角色后账号名称预期为：" + name + "-改" + " 实际为：" + newName);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            RoleDeleteScene.builder().id(id).build().visitor(visitor).execute();
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
    @Test(dataProvider = "ROLE_NAME_SUCCESS")
    public void role_add_work2(String name, String mess) {
        Integer id = null;
        try {
            JSONObject response = RoleListScene.builder().build().visitor(visitor).execute();
            JSONObject obj = response.getJSONArray("list").getJSONObject(0);
            Long parentId = obj.getLong("id");
            JSONArray authList = obj.getJSONArray("auth_list");
            String description = "自动化测试给店长自动化用的角色";
            IScene scene = RolePageScene.builder().build();
            Long total = scene.execute(visitor).getLong("total");
            int code = RoleAddScene.builder().name(name).description(description).authList(authList).parentRoleId(parentId).build().visitor(visitor).getResponse().getCode();
            checkArgument(code == 1000, mess);
            Long newTotal = scene.execute(visitor).getLong("total");
            IScene rolePageScene = RolePageScene.builder().name(name).build();
            id = util.toJavaObject(rolePageScene, JSONObject.class, "name", name).getInteger("id");
            Preconditions.checkArgument(newTotal - total == 1, "新增角色前列表数：" + total + " 新增角色后列表数：" + newTotal);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            RoleDeleteScene.builder().id(id).build().visitor(visitor).execute();
            saveData("新增角色(名称校验)-正常");
        }
    }

    @DataProvider(name = "ROLE_NAME_FAIL")
    public Object[][] roleNameFail() {
        JSONObject obj = RolePageScene.builder().build().visitor(visitor).execute().getJSONArray("list").getJSONObject(2);
        Long parentRoleId = obj.getLong("parent_role_id");
        JSONArray authList = obj.getJSONArray("auth_list");
        String description = obj.getString("description");
        return new Object[][]{
                {"这是一个二十一字的角色名称是的是的是的是的", "角色名称需要在1-20个字内", "角色名称需要在1-20个字内", parentRoleId, authList, description},
//                {"系统管理员", "新增角色失败当前角色名称已存在！请勿重复添加", "重复的角色名称，创建成功", parentRoleId, authList, description},
        };
    }

    //ok
    @Test(dataProvider = "ROLE_NAME_FAIL")
    public void role_add_workAb(String name, String message, String err, Long parentRoleId, JSONArray authList, String description) {
        try {
            String mess = RoleAddScene.builder().authList(authList).parentRoleId(parentRoleId).description(description).name(name).build().visitor(visitor).getResponse().getMessage();
            checkArgument(mess.equals(message), err);
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
                String picPath = UploadScene.builder().pic("data:image/jpeg;base64," + pic).permanentPicType(0).ratio(1.0).ratioStr("1:1").build().execute(visitor).getString("pic_path");
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
            IScene staffPageScene = StaffPageScene.builder().phone(account.getPhone()).build();
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
            JSONObject response = ReceptionScene.builder().plateNumber(util.getPlatNumber(EnumAppletToken.JC_WM_DAILY.getPhone())).build().execute(visitor);
            String jsonpath = "$.arrive_times&&$.customers[*].voucher_list[*]&&$.er_code_url&&$.last_reception_sale_name&&$.last_arrive_time&&$.plate_number";
            JsonPathUtil.spiltString(response.toJSONString(), jsonpath);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc接待搜索老车牌号展示项验证");
        }
    }

    @Test(description = "pc修改车预约价格，小程序对应变更")
    public void pcMaintainPriceEdit() {
        try {
            commonConfig.setShopId(account.getReceptionShopId()).setRoleId(ACCOUNT.getRoleId());
            visitor.setProduct(EnumTestProduct.JC_ONLINE_JD);
            String type = AppointmentTypeEnum.MAINTAIN.name();
            //判断今天是否是周末，周末就取周末的折扣配置
            Double price = 300.00;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 0);
            String dataType = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ? "WEEKEND" : "WEEKDAY";
            //修改预约价格
            IScene scene = ManageModelPageScene.builder().type(type).build();
            Long id = util.toFirstJavaObject(scene, ManageModelPageBean.class).getId();
            ManageModelEditScene.builder().id(id).price(price).type(type).status("ENABLE").build().visitor(visitor).execute();
            //工位配置里的折扣
            JSONObject timeRangeDetail = AppointmentTimeRangeDetailScene.builder().type(type).dateType(dataType).build().visitor(visitor).execute();
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
            util.loginApplet(APPLET_USER);
            JSONArray appletTime = AppletAppointmentTimeListScene.builder().shopId(Long.parseLong(account.getReceptionShopId()))
                    .carId(util.getCarId()).day(DateTimeUtil.getFormat(new Date())).type(type).build().visitor(visitor).execute().getJSONArray("list");
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
            commonConfig.setShopId(PRODUCT.getShopId()).setRoleId(PRODUCT.getRoleId());
            saveData("pc修改预约配置验证");
        }
    }

    @Test(enabled = false)  //pc修改工位，工作日和休息日不同步变更
    public void pcMaintainTableEdit() {
        try {
            commonConfig.setShopId(account.getReceptionShopId()).setRoleId(ACCOUNT.getRoleId());
            visitor.setProduct(EnumTestProduct.JC_ONLINE_JD);
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            //修改配置前，工作日的配置
            IScene weekdayScene = AppointmentTimeRangeDetailScene.builder().type("MAINTAIN").dateType("WEEKDAY").build();
            JSONObject timeDetailWeekday = weekdayScene.visitor(visitor).execute();
            JSONObject afternoonBefore = timeDetailWeekday.getJSONObject("afternoon");
            JSONObject morningBefore = timeDetailWeekday.getJSONObject("morning");
            //修改休息日的配置
            IScene weekendScene = AppointmentTimeRangeDetailScene.builder().type("MAINTAIN").dateType("WEEKEND").build();
            JSONObject timeDetailWeekend = weekendScene.visitor(visitor).execute();
            JSONObject morning = timeDetailWeekend.getJSONObject("morning");
            JSONObject afternoon = timeDetailWeekend.getJSONObject("afternoon");
            if (day % 2 == 1) {
                morning.put("reply_start", "09:00");  //奇数天 设置9：00
            } else {
                morning.put("reply_start", "08:00");
            }
            timeDetailWeekend.put("morning", morning);
            timeDetailWeekend.put("date_type", "WEEKDAY");
            timeDetailWeekend.put("type", "MAINTAIN");
            //修改工作日工位配置
            AppointmentTimeRangeEditScene.builder().type("MAINTAIN").dateType("WEEKDAY").morning(morning).afternoon(afternoon).build().visitor(visitor).execute();
            //修改之后的配置,工作日
            JSONObject timeDetailWeekdayAfter = weekdayScene.visitor(visitor).execute();
            JSONObject afternoonAfter = timeDetailWeekdayAfter.getJSONObject("afternoon");
            JSONObject morningAfter = timeDetailWeekdayAfter.getJSONObject("morning");
            //休息日
            JSONObject timeDetailWeekendAfter = weekendScene.visitor(visitor).execute();
            JSONObject afternoonAfterEnd = timeDetailWeekendAfter.getJSONObject("afternoon");
            JSONObject morningAfterEnd = timeDetailWeekendAfter.getJSONObject("morning");
            //工作日
            System.out.println(afternoonAfter.equals(afternoonBefore));
            System.out.println(morningAfter.equals(morningBefore));
            //休息日
            System.out.println(afternoonAfterEnd.equals(afternoon));
            System.out.println(morningAfterEnd.equals(morning));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            commonConfig.setShopId(PRODUCT.getShopId()).setRoleId(PRODUCT.getRoleId());
            saveData("pc修改工位，工作日和休息日不同步变更");
        }
    }


    @Test(enabled = false) //pc开关预约配置按钮，小程序对应变更
    public void pcAppointmentButton() {
        try {
            IScene scene = MaintainCarModelPageScene.builder().carModel(pp.carModel).build();
            String status = util.toFirstJavaObject(scene, MaintainCarModelPageBean.class).getStatus();
            if (!status.equals("ENABLE")) {
                throw new Exception(pp.carModel + "车型,预约配置被关闭");
            }
            jc.pcCarModelPriceEdit(pp.modelIdAppointment, null, "DISABLE", "MAINTAIN");
            jc.appletLoginToken(pp.appletToken);
            JSONObject isAble = jc.appletmaintainTimeList(Long.parseLong(pp.shopIdZ), pp.car_idA, DateTimeUtil.addDayFormat(new Date(), 1), AppointmentTypeEnum.MAINTAIN.name(), false);
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
            IScene scene = ShopPageScene.builder().build();
            Long shopId = Long.parseLong(account.getReceptionShopId());
            ShopPageBean shopPageBean = util.toJavaObject(scene, ShopPageBean.class, "id", shopId);
            String status = shopPageBean.getAppointmentStatus();
            if (!status.equals("ENABLE")) {
                throw new Exception(pp.shopName + "门店,预约开关被关闭了");
            }
            //配置前预约门店配置列表
            util.loginApplet(APPLET_USER);
            JSONArray isAble = jc.appletmaintainShopList(pp.car_idA.toString(), pp.coordinate).getJSONArray("list");
            int total = isAble.size();
            //关闭门店预约配置
            util.loginPc(ACCOUNT);
            jc.shopStatusChange(account.getReceptionShopId(), "APPOINTMENT", "DISABLE");
            //小程序预约门店列表
            util.loginApplet(APPLET_USER);
            JSONArray isAbleAfter = jc.appletmaintainShopList(pp.car_idA.toString(), pp.coordinate).getJSONArray("list");
            int totalAfter = isAbleAfter.size();
            util.loginPc(ACCOUNT);
            jc.shopStatusChange(account.getReceptionShopId(), "APPOINTMENT", "ENABLE");
            Preconditions.checkArgument(total - totalAfter == 1, "关闭预约配置，小程序预约门店-1");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc修改预约配置验证");
        }
    }

    @Test(description = "pc门店开关关闭，预约和洗车开关自动关闭")
    public void pcShopButton() {
        try {
            visitor.setProduct(EnumTestProduct.JC_ONLINE_JD);
            Long shopId = Long.parseLong(account.getReceptionShopId());
            String statusAll = "DISABLE";
            String open = "ENABLE";
            IScene scene = ShopPageScene.builder().build();
            ShopPageBean shopPageBean = util.toJavaObject(scene, ShopPageBean.class, "id", shopId);
            String status = shopPageBean.getStatus();
            if (!status.equals("ENABLE")) {
                statusAll = "ENABLE";
                open = "DISABLE";
            }
            //关闭门店预约配置
            ShopStatusChangeScene.builder().status(statusAll).type("SHOP").id(shopId).build().visitor(visitor).execute();
            shopPageBean = util.toJavaObject(scene, ShopPageBean.class, "id", shopId);
            status = shopPageBean.getStatus();
            String appointmentStatus = shopPageBean.getAppointmentStatus();
            String washingStatus = shopPageBean.getWashingStatus();
            Preconditions.checkArgument(appointmentStatus.equals(statusAll) && washingStatus.equals(statusAll) && status.equals(statusAll), "门店开关未同步");
            //开启门店预约配置
            ShopStatusChangeScene.builder().status(open).type("SHOP").id(shopId).build().visitor(visitor).execute();
            shopPageBean = util.toJavaObject(scene, ShopPageBean.class, "id", shopId);
            status = shopPageBean.getStatus();
            appointmentStatus = shopPageBean.getAppointmentStatus();
            washingStatus = shopPageBean.getWashingStatus();
            Preconditions.checkArgument(appointmentStatus.equals(statusAll) && washingStatus.equals(statusAll) && status.equals(open), "门店开关未同步2");
            ShopStatusChangeScene.builder().status(open).type("WASHING").id(shopId).build().visitor(visitor).execute();
            ShopStatusChangeScene.builder().status(open).type("APPOINTMENT").id(shopId).build().visitor(visitor).execute();
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc门店按钮修改关联验证");
        }
    }
}
