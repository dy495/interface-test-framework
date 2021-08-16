package com.haisheng.framework.testng.bigScreen.itemYuntong.caseonline.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.file.FileUploadScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.role.RoleAddScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.role.RoleDeleteScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.role.RoleListScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.role.RolePageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.staff.StaffAddScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.staff.StaffPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.staff.StatusChangeScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 系统配置case
 */
public class SystemConfigCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCE = EnumTestProduct.YT_ONLINE_ZH;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.YT_ALL_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public SceneUtil util = new SceneUtil(visitor);

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        //替换checklist的相关信息
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //放入shopId
        commonConfig.setShopId(ALL_AUTHORITY.getShopId()).setRoleId(ALL_AUTHORITY.getRoleId()).setProduct(PRODUCE.getAbbreviation());
        beforeClassInit(commonConfig);
        util.loginApp(ALL_AUTHORITY);
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

    @Test(description = "新增一个角色，角色列表+1，【账户管理】页角色下拉框+1")
    public void roleManage_data_1() {
        try {
            IScene roleListScene = RoleListScene.builder().build();
            int a = roleListScene.invoke(visitor).getJSONArray("list").size();
            IScene scene = RolePageScene.builder().build();
            int total = scene.invoke(visitor).getInteger("total");
            int parentRoleId = Integer.parseInt(ALL_AUTHORITY.getRoleId());
            List<Integer> authList = new ArrayList<>(util.getAuthRoleMap(parentRoleId).keySet());
            RoleAddScene.builder().name("自动化创建全部权限").description("这是一个最大权限的角色").parentRoleId(parentRoleId).authList(authList).build().invoke(visitor);
            int newTotal = scene.invoke(visitor).getInteger("total");
            int b = roleListScene.invoke(visitor).getJSONArray("list").size();
            Preconditions.checkArgument(newTotal == total + 1, "创建权限之前权限列表总数：" + total + " 创建权限之后权限列表总数：" + newTotal);
            Preconditions.checkArgument(b == a + 1, "创建权限之前【账户管理】页角色下拉框总数：" + a + " 创建权限之后【账户管理】页角色下拉框总数：" + b);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("新增一个角色，角色列表+1，【账户管理】页角色下拉框+1");
        }
    }

    @Test(description = "删除一个角色，角色列表-1，【账户管理】页角色下拉框-1", dependsOnMethods = "roleManage_data_1")
    public void roleManage_data_2() {
        try {
            IScene roleListScene = RoleListScene.builder().build();
            int a = roleListScene.invoke(visitor).getJSONArray("list").size();
            IScene scene = RolePageScene.builder().build();
            List<JSONObject> list = util.toJavaObjectList(scene, JSONObject.class);
            int id = list.get(list.size() - 1).getInteger("id");
            RoleDeleteScene.builder().id(id).build().invoke(visitor);
            int total = scene.invoke(visitor).getInteger("total");
            int b = roleListScene.invoke(visitor).getJSONArray("list").size();
            Preconditions.checkArgument(list.size() - 1 == total, "删除角色之前权限列表总数：" + list.size() + " 删除角色之后权限列表总数：" + total);
            Preconditions.checkArgument(b == a - 1, "删除角色之前【账户管理】页角色下拉框总数：" + a + " 删除角色之后【账户管理】页角色下拉框总数：" + b);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("删除一个角色，角色列表-1，【账户管理】页角色下拉框-1");
        }
    }

    @Test(description = "使用账号数量==账号列表中的数量")
    public void roleManage_data_3() {
        try {
            Map<Integer, Integer> map = new HashMap<>();
            IScene scene = RolePageScene.builder().build();
            List<JSONObject> list = util.toJavaObjectList(scene, JSONObject.class);
            list.forEach(e -> map.put(e.getInteger("id"), e.getInteger("num")));
            map.forEach((key, value) -> {
                int total = StaffPageScene.builder().roleId(key).build().invoke(visitor).getInteger("total");
                Preconditions.checkArgument(total == value, "权限 " + key + " 的使用账号数量：" + value + " 账号列表使用此权限的账号数量：" + total);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("使用账号数量==账号列表中的数量");
        }
    }

    @Test(description = "新增一个账户，账号管理列表+1，新增账号的信息与列表该账号的信息一致")
    public void staffManage_data_1() {
        try {
            String phone = "15555555555";
            int total = StaffPageScene.builder().build().invoke(visitor).getInteger("total");
            util.getRandomRoleMap().forEach((roleId, roleName) -> {
                JSONArray shopList = util.getShopIdArray();
                String pic = new ImageUtil().getImageBinary("src/main/java/com/haisheng/framework/testng/bigScreen/itemYuntong/common/resources/picture/touxiang.jpg");
                String picPath = FileUploadScene.builder().pic("data:image/jpeg;base64," + pic).permanentPicType(0).ratio(1.0).ratioStr("1:1").build().invoke(visitor).getString("pic_path");
                StaffAddScene.builder().phone(phone).name("克拉拉").shopList(shopList).roleId(roleId).roleName(roleName).picturePath(picPath).build().invoke(visitor);
                int newTotal = StaffPageScene.builder().build().invoke(visitor).getInteger("total");
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
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("新增一个账户，账号管理列表+1，新增账号的信息与列表该账号的信息一致");
        }
    }

    @Test(description = "删除一个账户，账号管理列表-1", dependsOnMethods = "staffManage_data_1")
    public void staffManage_data_2() {
        try {
            int total = StaffPageScene.builder().build().invoke(visitor).getInteger("total");
            util.deleteStaff("15555555555");
            int newTotal = StaffPageScene.builder().build().invoke(visitor).getInteger("total");
            Preconditions.checkArgument(newTotal == total - 1, "删除一个账号前列表数：" + total + " 删除一个账号后列表数：" + newTotal);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("删除一个账户，账号管理列表-1");
        }
    }

    @Test(description = "编辑一个账号提交以后，列表数量不变")
    public void staffManage_data_3() {
        try {
            IScene staffPageScene = StaffPageScene.builder().build();
            int total = staffPageScene.invoke(visitor).getInteger("total");
            String id = util.toFirstJavaObject(staffPageScene, JSONObject.class).getString("id");
            util.editStaff(id);
            int newTotal = staffPageScene.invoke(visitor).getInteger("total");
            Preconditions.checkArgument(newTotal == total, "编辑前列表数量：" + total + " 编辑后列表数量：" + newTotal);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("禁用一个账号，列表数量不变");
        }
    }

    @Test(description = "禁用一个账号，列表数量不变")
    public void staffManage_data_4() {
        String id = null;
        try {
            IScene staffPageScene = StaffPageScene.builder().build();
            int total = staffPageScene.invoke(visitor).getInteger("total");
            id = util.toFirstJavaObject(staffPageScene, JSONObject.class).getString("id");
            StatusChangeScene.builder().id(id).status("DISABLE").build().invoke(visitor);
            int newTotal = staffPageScene.invoke(visitor).getInteger("total");
            Preconditions.checkArgument(newTotal == total, "禁用前列表数量：" + total + " 禁用后列表数量：" + newTotal);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            StatusChangeScene.builder().id(id).status("ENABLE").build().invoke(visitor);
            saveData("禁用一个账号，列表数量不变");
        }
    }
}
