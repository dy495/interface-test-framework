package com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf.pc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.commonDs.JsonPathUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.Constant;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.manage.AppointmentMaintainConfigDetailBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.role.RolePageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.shopstylemodel.ManageModelPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.staff.StaffPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment.AppointmentTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletAppointmentTimeListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletEvaluateSubmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.LoginApp;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.LoginPc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage.AppointmentTimeRangeDetailScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage.EvaluateConfigSubmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptionScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.role.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shopstylemodel.ManageModelEditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shopstylemodel.ManageModelPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StaffAddScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StaffDeleteScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StaffEditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StaffPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StatusChangeScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.DataAbnormal;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.SelectReception;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.PcAppointmentConfig;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf.JcFunctionOnline;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf.PublicParamOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.jooq.tools.StringUtils;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class JcPcOnline extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCT = EnumTestProduct.JC_ONLINE_ZH;
    private static final EnumAccount ACCOUNT = EnumAccount.JC_ALL_ONLINE;
    private static final EnumAccount account = EnumAccount.JC_ALL_ONLINE_LXQ;
    private static final EnumAppletToken APPLET_USER = EnumAppletToken.JC_LXQ_ONLINE;
    private final VisitorProxy visitor = new VisitorProxy(PRODUCT);
    private final SceneUtil util = new SceneUtil(visitor);
    CommonConfig commonConfig = new CommonConfig();
    ScenarioUtil jc = ScenarioUtil.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParamOnline pp = new PublicParamOnline();
    JcFunctionOnline pf = new JcFunctionOnline();
    public String name = "创建角色xia";
    public String email = "";
    public String phone = "";


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        jc.changeIpPort(PRODUCT.getIp());
        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "夏明凤";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCT.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(ACCOUNT.getShopId()).setReferer(PRODUCT.getReferer()).setRoleId(PRODUCT.getRoleId()).setProduct(PRODUCT.getAbbreviation());
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);
    }

    //app登录
    public void appLogin(String username, String password, String roleId) {
        String path = "/account-platform/login-m-app";
        JSONObject object = new JSONObject();
        object.put("phone", username);
        object.put("verification_code", password);
        commonConfig.setRoleId(roleId);
        httpPost(EnumTestProduct.JC_ONLINE_ZH.getIp(), path, object);
    }

    //pc登录
    public void pcLogin(String phone, String verificationCode, String roleId) {
        String path = "/account-platform/login-pc";
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        object.put("type", 1);
        commonConfig.setRoleId(roleId);
        httpPost(EnumTestProduct.JC_ONLINE_ZH.getIp(), path, object);
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
        util.loginPc(ACCOUNT);
    }

    @AfterMethod
    public void restoreProduct() {
        visitor.setProduct(EnumTestProduct.JC_ONLINE_ZH);
    }

    /**
     * ====================新增角色(名称校验)======================
     */
    @Test(description = "角色的CURD,数据校验")
    public void role_manager_1() {
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
    public void role_manager_2(String name, String mess) {
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
    public void role_manager_3(String name, String message, String err, Long parentRoleId, JSONArray authList, String description) {
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
    public void staff_manager_1() {
        try {
            String phone = "15555555555";
            int total = StaffPageScene.builder().build().execute(visitor).getInteger("total");
            util.getRandomRoleMap().forEach((roleId, roleName) -> {
                JSONArray shopList = util.getShopIdArray();
                String picPath = util.uploadFile();
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

    @Test(description = "新增姓名异常")
    public void staff_manager_2() {
        try {
            String phone = "15555555555";
            String name = "123456789012345678901咿呀咿呀";
            IScene scene = StaffPageScene.builder().build();
            int total = scene.visitor(visitor).execute().getInteger("total");
            util.getRandomRoleMap().forEach((roleId, roleName) -> {
                JSONArray shopList = util.getShopIdArray();
                String picPath = util.uploadFile();
                Integer code = StaffAddScene.builder().phone(phone).name(name).shopList(shopList).roleId(roleId).roleName(roleName).picturePath(picPath).build().visitor(visitor).getResponse().getCode();
                Preconditions.checkArgument(code == 1001, "新增账户名称为：" + name + " 预期code为1000 实际为：" + code);
            });
            int newTotal = scene.visitor(visitor).execute().getInteger("total");
            Preconditions.checkArgument(newTotal == total, "新增账户前列表数为：" + total + " 新增账户后列表数为：" + newTotal);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("新增姓名异常");
        }
    }

    @Test(description = "新增姓名电话异常", dataProvider = "ERR_PHONE", dataProviderClass = DataAbnormal.class)
    public void staff_manager_3(String phone) {
        try {
            String name = "创建角色xia";
            util.getRandomRoleMap().forEach((roleId, roleName) -> {
                JSONArray shopList = util.getShopIdArray();
                String picPath = util.uploadFile();
                Integer code = StaffAddScene.builder().phone(phone).name(name).shopList(shopList).roleId(roleId).roleName(roleName).picturePath(picPath).build().visitor(visitor).getResponse().getCode();
                Preconditions.checkArgument(code == 1001, "新增账户电话号为：" + phone + " 预期code为1000 实际为：" + code);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("新增姓名电话异常");
        }
    }

    @Test(description = "重复电话异常")
    public void staff_manager_4() {
        try {
            String name = "创建角色xia";
            IScene scene = StaffPageScene.builder().build();
            String phone = util.toFirstJavaObject(scene, StaffPageBean.class).getPhone();
            util.getRandomRoleMap().forEach((roleId, roleName) -> {
                JSONArray shopList = util.getShopIdArray();
                String picPath = util.uploadFile();
                Integer code = StaffAddScene.builder().phone(phone).name(name).shopList(shopList).roleId(roleId).roleName(roleName).picturePath(picPath).build().visitor(visitor).getResponse().getCode();
                Preconditions.checkArgument(code == 1001, "新增账户电话号为：" + phone + " 预期code为1000 实际为：" + code);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("重复电话异常");
        }
    }

    @Test(description = "角色6个异常")
    public void staff_manager_5() {
        try {
            JSONArray roleList = new JSONArray();
            String[][] roleId = {
                    {"2874", "角色1（东东测试）"},
                    {"2875", "角色2（东东测试）"},
                    {"2847", "角色A"},
                    {"2889", "测试角色"},
                    {"2873", "个人-售前接待"},
                    {"2889", "测试角色A"},
            };
            for (String[] strings : roleId) {
                JSONObject roleList1 = pf.getRoleList1(strings[0], strings[1]);
                roleList.add(roleList1);
            }
            String phone = pf.genPhoneNum();
            //用EMAIL新增一个账号
            int code = StaffAddScene.builder().roleList(roleList).name(name).phone(phone).build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(code == 1001, "新增账户，角色超过5个");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("新增1个账号电话号码重复异常验证");
        }
    }

    @Test(description = "创建账号角色5个")    //ok
    public void staff_manager_6() {
        try {
            String[][] roleId = new String[5][2];
            JSONArray roleList = new JSONArray();
            IScene rolePageScene = RolePageScene.builder().build();
            JSONArray list = rolePageScene.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < 5; i++) {
                String id = list.getJSONObject(i).getString("id");
                String name = list.getJSONObject(i).getString("name");
                roleId[i][0] = id;
                roleId[i][1] = name;
            }
            for (String[] strings : roleId) {
                JSONObject roleList1 = pf.getRoleList1(strings[0], strings[1]);
                roleList.add(roleList1);
            }
            String phone = pf.genPhoneNum();
            //新增一个账号
            StaffAddScene.builder().roleList(roleList).name(name).phone(phone).build().visitor(visitor).execute();
            IScene staffPageScene = StaffPageScene.builder().name(name).build();
            String id = util.toFirstJavaObject(staffPageScene, StaffPageBean.class).getId();
            StaffDeleteScene.builder().id(id).build().visitor(visitor).execute();
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("新增1个账号角色5个");
        }
    }

    /**
     * ====================账户管理中的一致性========================
     */
    @Test(description = "编辑账号信息以后，创建者和创建时间是否发生改变")
    public void staff_manager_7() {
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

    @Test(description = "禁用账户登录失败，开启登录成功")
    public void staff_manager_8() {
        String id = null;
        try {
            JSONObject response = StaffPageScene.builder().phone(account.getPhone()).build().execute(visitor);
            id = response.getJSONArray("list").getJSONObject(0).getString("id");
            //关闭账号
            com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StatusChangeScene.builder().id(id).status("DISABLE").build().execute(visitor);
            int pcCode = LoginPc.builder().type(1).phone(account.getPhone()).verificationCode(account.getPassword()).build().visitor(visitor).getResponse().getCode();
            int appCode = LoginApp.builder().phone(account.getPhone()).verificationCode(account.getPassword()).build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(appCode == 1001, "账户禁用，app登陆预期为：1001" + "实际为：" + appCode);
            Preconditions.checkArgument(pcCode == 1001, "账户禁用，pc登陆预期为：1001" + "实际为：" + pcCode);
            //开启账号
            com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StatusChangeScene.builder().id(id).status("ENABLE").build().execute(visitor);
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

    //查询案例

    /**
     * @description :接待管理查询   ---for  liya
     * @date :2020/8/3 12:48
     **/
//    @Test(dataProvider = "SELECT_PARM", dataProviderClass = ScenarioUtil.class)
    public void receptionSelect(String parm, String output) {
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
            collectMessage(e);
        } finally {
            saveData("接待管理查询，结果校验");
        }
    }

    //    @Test()
    public void selectAppointmentRecodeFilter() {
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
            collectMessage(e);
        } finally {
            saveData("接待管理列表查询全填，结果校验");
        }
    }

    //    @Test(dataProvider = "SELECT_PreSleCustomerManageFilter",dataProviderClass = Constant.class)
    public void preSleCustomerManageOneFilter(String pram, String output) {
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
            collectMessage(e);
        } finally {
            saveData("销售客户查询单项查询，结果校验");
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

    //pc-取消接待
//    @Test()
    public void pcCancleReception() {
        try {
            appLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            int appTask = pf.appReceptionPage();
            pcLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            //取消接待
            JSONObject dd = jc.receptionManage("", "1", "10", "", "").getJSONArray("list").getJSONObject(0);
            long receptionID = dd.getLong("id");
            long shopId = dd.getLong("shop_id");
            String reception_status_name = dd.getString("reception_status_name");
            if (!reception_status_name.equals("接待中")) {
                logger.warn("暂无接待中客户");
                return;
            }
            jc.pcCancelReception(receptionID, shopId);
            appLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            int appTaskA = pf.appReceptionPage();
            Preconditions.checkArgument(appTask - appTaskA == 1, "pc取消接待，app接待任务没-1");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            pcLogin(pp.gwphone, pp.gwpassword, pp.roleId);
            saveData("pc取消接待，app任务-1");
        }
    }

    //***************pc保养配置相关********************

    @DataProvider(name = "TYPE")
    public static Object[] type() {
        return new String[]{
                "MAINTAIN",
                "REPAIR"
        };
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

    @Test(dataProvider = "appointment_type", description = "pc修改车预约价格，价格格式异常判断", enabled = false)
    public void pcMaintainPriceEditAb(String type) {
        try {
            commonConfig.setShopId(account.getReceptionShopId()).setRoleId(ACCOUNT.getRoleId());
            visitor.setProduct(EnumTestProduct.JC_ONLINE_JD);
            Double[] price = {null, 100000000.011};
            IScene scene = ManageModelPageScene.builder().type(type).build();
            Long id = util.toFirstJavaObject(scene, ManageModelPageBean.class).getId();
            //修改预约价格
            for (Double s : price) {
                int code = ManageModelEditScene.builder().id(id).price(s).type(type).status("ENABLE").build().visitor(visitor).getResponse().getCode();
                Preconditions.checkArgument(code == 1001, "修改异常价格预期code为1001 实际为：" + code);
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            commonConfig.setShopId(PRODUCT.getShopId()).setRoleId(PRODUCT.getRoleId());
            saveData("pc修改预约配置价格格式验证");
        }
    }

    @Test()  //pc修改车预约价格，列表价格变更，其他不变更
    public void pcmaintainPriceEdit2() {
        String type = "MAINTAIN";
        try {
            pcLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            String price = "300.0";
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 0);
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                price = "200.0";
            }
            jc.pcCarModelPriceEdit(pp.modelIdAppointment, price, null, true, type);
            IScene modelpage = ManageModelPageScene.builder().page(1).type(type).size(10).carModel(pp.carModel).build();

            String priceAfter = jc.invokeApi(modelpage).getJSONArray("list").getJSONObject(0).getString("price");
            Preconditions.checkArgument(priceAfter.equals(price), "编辑后价格与入参不一致");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc修改预约配置价格，列表验证");
        }
    }

    @Test(dataProvider = "appointment_type", enabled = false)  //pc开关【保养配置】-预约按钮，小程序对应变更
    public void pcappointmentButton(String type) {
        try {
            IScene modelPage = ManageModelPageScene.builder().page(1).size(10).type(type).carModel(pp.carModel).build();
            JSONObject data = jc.invokeApi(modelPage).getJSONArray("list").getJSONObject(0);
            String status = data.getString("status");
            if (!status.equals("ENABLE")) {
                throw new Exception("车型,预约配置被关闭");
            }
            //  public JSONObject pcCarModelPriceEdit(String id, String price, String status, Boolean checkcode,String  type) {
            jc.pcCarModelPriceEdit(pp.modelIdAppointment, null, "DISABLE", true, type);

            jc.appletLoginToken(pp.appletToken);
            JSONObject isAble = jc.appletmaintainTimeList(Long.parseLong(pp.shopIdZ), pp.car_idA, dt.getHistoryDate(1), type, false);
            int code = isAble.getInteger("code");
            String message = isAble.getString("message");

            jc.pcLogin(pp.gwphone, pp.gwpassword);
            jc.pcCarModelPriceEdit(pp.modelIdAppointment, null, "ENABLE", true, type);


            Preconditions.checkArgument(code != 1000, "预约配置关闭小程序预约保养页返回" + message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            saveData("pc修改预约配置验证");
        }
    }

    @Test(dataProvider = "appointment_type", enabled = false) //pc开关【保养配置】-预约按钮，保养配置列表数量无变化
    public void pcappointmentButton2(String type) {
        try {
            IScene modelPage = ManageModelPageScene.builder().page(1).size(10).type(type).carModel(pp.carModel).build();
            JSONObject data = jc.invokeApi(modelPage).getJSONArray("list").getJSONObject(0);
            String status = data.getString("status");
            if (!status.equals("ENABLE")) {
                throw new Exception("车型-赢识-xia,预约配置被关闭");
            }
            IScene modelPage2 = ManageModelPageScene.builder().page(1).size(10).type(type).build();
            int total = jc.invokeApi(modelPage2).getInteger("total");

            jc.pcCarModelPriceEdit(pp.modelIdAppointment, null, "DISABLE", true, type);
            IScene modelPage3 = ManageModelPageScene.builder().page(1).size(10).type(type).build();
            int totalAfter = jc.invokeApi(modelPage3).getInteger("total");

            jc.pcCarModelPriceEdit(pp.modelIdAppointment, null, "ENABLE", true, type);
            IScene modelPage4 = ManageModelPageScene.builder().page(1).size(10).type(type).build();
            int totalAfter2 = jc.invokeApi(modelPage4).getInteger("total");

            Preconditions.checkArgument(total == totalAfter, "关闭后列表数量变化");
            Preconditions.checkArgument(totalAfter == totalAfter2, "开启预约配置后列表数量变化");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            saveData("pc开关预约配置按钮，预约配置列表数量有无变化");
        }
    }
    //***********pc预约配置************************

    @Test(dataProvider = "appointment_type")  //pc修改工位，工作日和休息日不同步变更
    public void pcmaintainTableEdit(String type) {
        try {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);

            pcLogin(pp.jdgw, pp.gwpassword, pp.roleidJdgw);
            //修改配置前，工作日的配置
            JSONObject timeDetailWeekday = jc.timeRangeDetail(type, "WEEKDAY");
            JSONObject afternoonBefore = timeDetailWeekday.getJSONObject("afternoon");
            JSONObject morningBefore = timeDetailWeekday.getJSONObject("morning");

            //修改休息日的配置
            JSONObject timeDetailWeekend = jc.timeRangeDetail(type, "WEEKEND");
            JSONObject morning1 = timeDetailWeekend.getJSONObject("morning");
            JSONObject afternoon1 = timeDetailWeekend.getJSONObject("afternoon");

            JSONObject morning = JSONObject.parseObject(morning1.toJSONString());

            if (day % 2 == 1) {
                morning.put("reply_start", "09:00");  //奇数天 设置9：00
            } else {
                morning.put("reply_start", "08:00");
            }
            afternoon1.put("title", "下午时段");

            timeDetailWeekend.put("morning", morning);
            timeDetailWeekend.put("afternoon", afternoon1);
            timeDetailWeekend.put("date_type", "WEEKEND");
            timeDetailWeekend.put("type", type);

            //修改休息日工位配置
            jc.appointmentTimeEdit(timeDetailWeekend);

            //修改之后的配置,工作日
            JSONObject timeDetailWeekdayAfter = jc.timeRangeDetail(type, "WEEKDAY");
            JSONObject afternoonAfter = timeDetailWeekdayAfter.getJSONObject("afternoon");
            JSONObject morningAfter = timeDetailWeekdayAfter.getJSONObject("morning");
            //休息日
            JSONObject timeDetailWeekendAfter = jc.timeRangeDetail(type, "WEEKEND");
            JSONObject afternoonAfterend = timeDetailWeekendAfter.getJSONObject("afternoon");
            JSONObject morningAfterend = timeDetailWeekendAfter.getJSONObject("morning");

            //工作日
            System.out.println(afternoonAfter.equals(afternoonBefore));  //true
            System.out.println(morningAfter.equals(morningBefore));  //true
            //休息日
            System.out.println(afternoonAfterend.equals(afternoon1));  //ture
            System.out.println(morningAfterend.equals(morning1));   //false

//            //工作日
            Preconditions.checkArgument(afternoonAfter.equals(afternoonBefore), "修改休息日上午工作配置，工作日下午看板变更了");
            Preconditions.checkArgument(morningAfter.equals(morningBefore), "修改休息日上午工作配置，工作日上午看板变更了");
//            //休息日
//            Preconditions.checkArgument(afternoonAfterend.equals(afternoon1),"修改休息日上午工作配置，休息日下午看板变更了");
//            Preconditions.checkArgument(morningAfterend.equals(morning1),"修改休息日上午工作配置，休息日上午看板没有变更了");   //false


        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            saveData("pc修改工位，工作日和休息日不同步变更");
        }
    }

    @Test(dataProvider = "appointment_type")  //pc修改预约配置，正常
    public void pcmaintainTableEdit2(String type) {
        try {
            pcLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            JSONArray voucher = new JSONArray();
            voucher.add("77");
            PcAppointmentConfig er = new PcAppointmentConfig();
            er.type = type;
            er.remind_time = "720";
            er.replay_time_limit = "720";
            er.appointment_interval = "12";  //TODO:需设置成12 ，目前有bug
//            er.on_time_reward=true;
//            er.is_send_voucher=true;
//            er.vouchers=voucher;
//            er.voucher_start=dt.getHistoryDate(0);
//            er.voucher_end=dt.getHistoryDate(10);
            jc.pcappointmentConfig(er);

            er.remind_time = "20";
            er.replay_time_limit = "30";
            er.appointment_interval = "1";
            jc.pcappointmentConfig(er);
            JSONObject config = jc.AppointmentMaintainConfigDetailScene(type);

            AppointmentMaintainConfigDetailBean as = JSONObject.toJavaObject(config, AppointmentMaintainConfigDetailBean.class);

            Preconditions.checkArgument(as.getAppointmentInterval() == 1, "间隔时间配置失败");
            Preconditions.checkArgument(as.getReplayTimeLimit() == 30, "超时时间配置失败");
            Preconditions.checkArgument(as.getRemindTime() == 20, "提醒时间配置失败");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            saveData("pc修改预约配置，正常");
        }
    }

    //    @Test  //pc修改预约配置，正常  限制使用天数超过2000  TODO:目前未作限制，bug修改后打开,2.0该功能取消
    public void pcmaintainTableEdit3() {
        try {
            jc.pcLogin(pp.jdgw, pp.jdgwpassword);
            JSONArray voucher = new JSONArray();
            voucher.add("77");
            PcAppointmentConfig er = new PcAppointmentConfig();
            er.checkcode = false;
            er.type = "MAINTAIN";
            er.remind_time = "30";
            er.replay_time_limit = "20";
            er.appointment_interval = "1";
            er.on_time_reward = true;
            er.is_send_voucher = true;
            er.vouchers = voucher;
            er.voucher_effective_days = "20000";
            int code = jc.pcappointmentConfig(er).getInteger("code");
            Preconditions.checkArgument(code == 1001, "修改预约配置，卡券使用天数限制超过2000仍成功");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            saveData("pc修改预约配置卡券限制使用时间2000天验证");
        }
    }

    //    @Test  //pc修改预约配置,异常    TODO:接口未作限制，前端限制
    public void pcmaintainTableEditAb() {
        try {
            jc.pcLogin(pp.jdgw, pp.jdgwpassword);
            JSONArray voucher = new JSONArray();
            voucher.add("77");
            PcAppointmentConfig er = new PcAppointmentConfig();
            er.checkcode = false;
            er.type = "MAINTAIN";
            er.remind_time = "721";
            er.replay_time_limit = "20";
            er.appointment_interval = "1";
//            er.on_time_reward=true;
//            er.is_send_voucher=true;
//            er.vouchers=voucher;
//            er.voucher_start=dt.getHistoryDate(0);
//            er.voucher_end=dt.getHistoryDate(10);
            int code = jc.pcappointmentConfig(er).getInteger("code");
            Preconditions.checkArgument(code == 1001, "修改预约配置【提醒时间】异常：" + er.remind_time);
            er.remind_time = "20";
            er.replay_time_limit = "721";
            er.appointment_interval = "1";
            int code2 = jc.pcappointmentConfig(er).getInteger("code");
            Preconditions.checkArgument(code2 == 1001, "修改预约配置【超时应答时间】异常：" + er.replay_time_limit);

            er.replay_time_limit = "20";
            er.appointment_interval = "13";
            int code3 = jc.pcappointmentConfig(er).getInteger("code");
            Preconditions.checkArgument(code3 == 1001, "修改预约配置【正常应答时间】异常：" + er.appointment_interval);
            er.appointment_interval = "1";
            int code4 = jc.pcappointmentConfig(er).getInteger("code");
            Preconditions.checkArgument(code4 == 1000, "修改预约配置异常");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            saveData("pc修改预约配置，异常");
        }
    }

    @DataProvider(name = "APPOINTMENT_TYPE")
    public static Object[] appointment_type() {
        return new String[]{
                "MAINTAIN",
                "REPAIR",
        };
    }

    //***********pc门店按钮开关************
    @Test(dataProvider = "APPOINTMENT_TYPE") //pc门店预约关闭，小程序对应变更
    public void pcShopAppointmentButton(String type) {
        try {
            JSONObject data = jc.shopListFilterManage("", "1", "10", "name", pp.shopName).getJSONArray("list").getJSONObject(0);
            String status = data.getString("appointment_status");
            if (!status.equals("ENABLE")) {
                throw new Exception("中关村店,预约开关被关闭了");
            }
            //配置前预约门店配置列表
            jc.appletLoginToken(pp.appletToken);
            JSONArray isAble = jc.appletmaintainShopList(pp.car_idA.toString(), pp.coordinate, type).getJSONArray("list");
            int total = isAble.size();
            //关闭门店预约配置
            pcLogin(pp.gwphone, pp.gwpassword, pp.roleId);
            jc.shopStatusChange(pp.shopIdZ, "APPOINTMENT", "DISABLE");
            //小程序预约门店列表
            jc.appletLoginToken(pp.appletToken);
            JSONArray isAbleAfter = jc.appletmaintainShopList(pp.car_idA.toString(), pp.coordinate, type).getJSONArray("list");
            int totalAfter = isAbleAfter.size();

            pcLogin(pp.gwphone, pp.gwpassword, pp.roleId);
            jc.shopStatusChange(pp.shopIdZ, "APPOINTMENT", "ENABLE");
            jc.appletLoginToken(pp.appletToken);

            int totalAfter2 = jc.appletmaintainShopList(pp.car_idA.toString(), pp.coordinate, "MAINTAIN").getJSONArray("list").size();
            Preconditions.checkArgument(total - totalAfter == 1, "关闭预约配置，小程序预约门店-1");
            Preconditions.checkArgument(totalAfter2 - totalAfter == 1, "开启预约配置，小程序预约门店+1");

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
//            System.out.println("ip=="+IpPort);
            String statusAll = "DISABLE";
            String open = "ENABLE";
            JSONObject data = jc.shopListFilterManage("", "1", "10", "name", pp.shopName).getJSONArray("list").getJSONObject(0);
            String status1 = data.getString("status");
            if (!status1.equals("ENABLE")) {
                statusAll = "ENABLE";
                open = "DISABLE";
            }
            //关闭门店预约配置
            pcLogin(pp.gwphone, pp.gwpassword, pp.roleId);
            jc.shopStatusChange(pp.shopIdZ, "SHOP", statusAll);
            JSONObject dataAfter = jc.shopListFilterManage("", "1", "10", "name", "中关村店").getJSONArray("list").getJSONObject(0);
            String status = dataAfter.getString("status");
            String appointment_status = dataAfter.getString("appointment_status");
            String washing_status = dataAfter.getString("washing_status");
            Preconditions.checkArgument(appointment_status.equals(statusAll) && washing_status.equals(statusAll) && status.equals(statusAll), "门店开关未同步");

            jc.shopStatusChange(pp.shopIdZ, "SHOP", open);
            JSONObject dataAfter2 = jc.shopListFilterManage("", "1", "10", "name", "中关村店").getJSONArray("list").getJSONObject(0);
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

    @DataProvider(name = "EVALUATETYPE")
    public static Object[][] evaluatetype() {
        return new String[][]{
                {"1", "保养评价消息"},
                {"2", "维修评价消息"}
        };
    }

    @Test(dataProvider = "EVALUATETYPE")
    public void evalute(String type1, String messageName) {
        try {
            pcLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            System.out.println(type1 + ":" + messageName);
            Integer type = Integer.parseInt(type1);

            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            Integer points;
            if (day % 2 == 1) {
                points = 3;
            } else {
                points = 2;
            }
            //修改配置，评价，积分数，卡券数
            IScene evaluateConfig = EvaluateConfigSubmitScene.builder().evaluateReward(true)
                    .defaultFavourableCycle(3)
                    .isSendPoints(true).isSendVoucher(true).points(points)
                    .type(type).vouchersId(pp.voucherIdevluate).build();
            jc.invokeApi(evaluateConfig);

            jc.appletLoginToken(pp.appletToken);
            String id[] = pf.getMessageId(messageName);
            if (StringUtils.isEmpty(id[0])) {
                throw new Exception("没有待评价的消息，检查接待case 是否失败");
            }
            System.out.println("messageId" + id[0]);
            //评价前的积分和卡券数
            Integer voucherTotal = pf.getVoucherTotal();
            Integer appletScore = jc.appletUserInfoDetail().getInteger("score");


            JSONObject evaluateConfigDescribe = jc.AppletEvaluateConfigScene(type, Long.parseLong(pp.shopIdZ));
            String describe = evaluateConfigDescribe.getJSONArray("list").getJSONObject(2).getString("describe");
            List label = evaluateConfigDescribe.getJSONArray("list").getJSONObject(2).getJSONArray("labels");

            //评价
            IScene evaluatesubmit = AppletEvaluateSubmitScene.builder()
                    .describe(describe).labels(label).id(Long.valueOf(id[1]))
                    .isAnonymous(true)
                    .score(2)
                    .shopId(Long.parseLong(pp.shopIdZ)).suggestion("自动评价").type(type)
                    .build();
            jc.invokeApi(evaluatesubmit);

            //评价前的积分和卡券数
            Integer voucherTotalAfter = pf.getVoucherTotal();
            Integer appletScoreAfter = jc.appletUserInfoDetail().getInteger("score");

//            Preconditions.checkArgument(voucherTotalAfter-voucherTotal==1,"评价完成后，卡券没+1"+"评价前："+voucherTotal+",评价后："+voucherTotalAfter);
            Preconditions.checkArgument(appletScoreAfter - appletScore == points, "评价完成后，积分奖励没发");


        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            pcLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            saveData(type1 + "评价配置修改验证");
        }
    }


}
