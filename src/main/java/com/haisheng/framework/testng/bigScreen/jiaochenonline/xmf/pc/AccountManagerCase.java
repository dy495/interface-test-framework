package com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf.pc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.role.RolePageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.staff.StaffPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.LoginApp;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.LoginPc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.role.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StaffAddScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StaffDeleteScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StaffEditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StaffPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StatusChangeScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.DataAbnormal;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf.JcFunctionOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Date;

import static com.google.common.base.Preconditions.checkArgument;

public class AccountManagerCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCT = EnumTestProduct.JC_ONLINE_ZH;
    private static final EnumAccount ACCOUNT = EnumAccount.JC_ONLINE_YS;
    private static final EnumAccount account = EnumAccount.JC_ONLINE_LXQ;
    private final VisitorProxy visitor = new VisitorProxy(PRODUCT);
    private final SceneUtil util = new SceneUtil(visitor);
    private final JcFunctionOnline pf = new JcFunctionOnline();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.XMF.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCT.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(ACCOUNT.getShopId()).setReferer(PRODUCT.getReferer()).setRoleId(PRODUCT.getRoleId()).setProduct(PRODUCT.getAbbreviation());
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
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        util.loginPc(ACCOUNT);
        logger.logCaseStart(caseResult.getCaseName());
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
            Long total = scene.visitor(visitor).execute().getLong("total");
            int code = RoleAddScene.builder().name(name).description(description).authList(authList).parentRoleId(parentId).build().visitor(visitor).getResponse().getCode();
            checkArgument(code == 1000, mess);
            Long newTotal = scene.visitor(visitor).execute().getLong("total");
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
            String phone = util.getRandomPhone();
            int total = StaffPageScene.builder().build().visitor(visitor).execute().getInteger("total");
            util.getRandomRoleMap().forEach((roleId, roleName) -> {
                JSONArray shopList = util.getShopIdArray();
                String picPath = util.uploadFile();
                StaffAddScene.builder().phone(phone).name("克拉拉").shopList(shopList).roleId(roleId).roleName(roleName).picturePath(picPath).build().visitor(visitor).execute();
                int newTotal = StaffPageScene.builder().build().visitor(visitor).execute().getInteger("total");
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
                int deleteTotal = StaffPageScene.builder().build().visitor(visitor).execute().getInteger("total");
                util.deleteStaff(phone);
                int newDeleteTotal = com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.staff.StaffPageScene.builder().build().visitor(visitor).execute().getInteger("total");
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
            String phone = util.getRandomPhone();
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
            String name = "创建角色xia";
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
            String phone = util.getRandomPhone();
            //用EMAIL新增一个账号
            int code = StaffAddScene.builder().roleList(roleList).name(name).phone(phone).build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(code == 1001, "新增账户，角色超过5个");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("新增1个账号电话号码重复异常验证");
        }
    }

    @Test(description = "创建账号角色5个")
    public void staff_manager_6() {
        try {
            String name = "创建角色xia";
            String[][] roleId = new String[5][2];
            JSONArray roleList = new JSONArray();
            IScene rolePageScene = RolePageScene.builder().build();
            JSONArray list = rolePageScene.visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < 5; i++) {
                String id = list.getJSONObject(i).getString("id");
                name = list.getJSONObject(i).getString("name");
                roleId[i][0] = id;
                roleId[i][1] = name;
            }
            for (String[] strings : roleId) {
                JSONObject roleList1 = pf.getRoleList1(strings[0], strings[1]);
                roleList.add(roleList1);
            }
            String phone = util.getRandomPhone();
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
            JSONObject response = staffPageScene.visitor(visitor).execute();
            int total = response.getInteger("total");
            JSONObject obj = response.getJSONArray("list").getJSONObject(0);
            JSONArray roleList = obj.getJSONArray("role_list");
            String id = obj.getString("id");
            String phone = obj.getString("phone");
            String createTime = obj.getString("create_time");
            String name = obj.getString("name");
            StaffEditScene.builder().roleList(roleList).id(id).phone(phone).name(name + "-改").build().visitor(visitor).execute();
            JSONObject newResponse = staffPageScene.visitor(visitor).execute();
            int newTotal = newResponse.getInteger("total");
            JSONObject newObj = newResponse.getJSONArray("list").getJSONObject(0);
            String newCreateTime = newObj.getString("create_time");
            Preconditions.checkArgument(createTime.equals(newCreateTime), "编辑前创建时间：" + createTime + " 编辑后创建时间：" + newCreateTime);
            Preconditions.checkArgument(total == newTotal, "编辑一个账号，账号列表的数量由:" + total + "变成了" + newTotal);
            StaffEditScene.builder().roleList(roleList).id(id).phone(phone).name(name).build().visitor(visitor).execute();
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
            JSONObject response = StaffPageScene.builder().phone(account.getPhone()).build().visitor(visitor).execute();
            id = response.getJSONArray("list").getJSONObject(0).getString("id");
            //关闭账号
            com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StatusChangeScene.builder().id(id).status("DISABLE").build().visitor(visitor).execute();
            int pcCode = LoginPc.builder().type(1).phone(account.getPhone()).verificationCode(account.getPassword()).build().visitor(visitor).getResponse().getCode();
            int appCode = LoginApp.builder().phone(account.getPhone()).verificationCode(account.getPassword()).build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(appCode == 1001, "账户禁用，app登陆预期为：1001" + "实际为：" + appCode);
            Preconditions.checkArgument(pcCode == 1001, "账户禁用，pc登陆预期为：1001" + "实际为：" + pcCode);
            //开启账号
            com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StatusChangeScene.builder().id(id).status("ENABLE").build().visitor(visitor).execute();
            int enablePcCode = LoginPc.builder().type(1).phone(account.getPhone()).verificationCode(account.getPassword()).build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(enablePcCode == 1000, "账户启用，pc登陆预期为：1000" + "实际为：" + enablePcCode);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            //开启账号
            StatusChangeScene.builder().id(id).status("ENABLE").build().visitor(visitor).execute();
            saveData("禁用账户登录失败，开启登录成功");
        }
    }
}
