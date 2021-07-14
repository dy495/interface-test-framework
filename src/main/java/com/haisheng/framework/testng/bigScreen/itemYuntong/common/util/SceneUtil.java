package com.haisheng.framework.testng.bigScreen.itemYuntong.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.util.BasicUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.DeleteRecordPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.homepagev4.AppTodayDataBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.presalesreception.AppPreSalesReceptionPageBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.homepagev4.AppTodayDataScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppPreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanagev4.PreSaleCustomerInfoBuyCarRecordScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.record.ExportPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.record.ImportPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.record.LoginRecordPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.personaldata.AppPersonalOverviewBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.voicerecord.AppDepartmentPageBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.voicerecord.AppDetailBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.voicerecord.AppPersonalPageBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.carmodel.AppCarModelTreeScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.personaldata.AppPersonalOverviewScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppCustomerDetailV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppCustomerEditV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.voicerecord.AppDepartmentPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.voicerecord.AppDetailScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.voicerecord.AppPersonalPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.AuthTreeScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.loginuser.LoginApp;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.loginuser.LoginPc;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.loginuser.ShopListScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.VoiceDetailScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.role.RoleAddScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.role.RoleDeleteScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.role.RoleListScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.role.RolePageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.staff.StaffDetailScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.staff.StaffDeleteScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.staff.StaffEditScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.staff.StaffPageScene;
import com.haisheng.framework.util.CommonUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class SceneUtil extends BasicUtil {
    private final VisitorProxy visitor;

    public SceneUtil(VisitorProxy visitor) {
        super(visitor);
        this.visitor = visitor;
    }

    /**
     * pc账号登录
     *
     * @param enumAccount 账号
     */
    public void loginPc(@NotNull EnumAccount enumAccount) {
        IScene scene = LoginPc.builder().phone(enumAccount.getPhone()).verificationCode(enumAccount.getPassword()).build();
        login(scene);
    }

    /**
     * app账号登陆
     *
     * @param enumAccount 账号
     */
    public void loginApp(@NotNull EnumAccount enumAccount) {
        IScene scene = LoginApp.builder().phone(enumAccount.getPhone()).verificationCode(enumAccount.getPassword()).build();
        login(scene);
    }

    /**
     * 为满足来回切换域名设计
     *
     * @param scene 登录接口
     */
    public void login(IScene scene) {
        EnumTestProduce oldProduce = visitor.getProduct();
        EnumTestProduce newProduce = visitor.isDaily() ? EnumTestProduce.YT_DAILY_SSO : EnumTestProduce.YT_ONLINE_ZH;
        visitor.setProduct(newProduce);
        visitor.login(scene);
        visitor.setProduct(oldProduce);
    }

    /**
     * 获取app部门接待分页信息集合
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 接待信息集合
     */
    public List<AppDepartmentPageBean> getAppDepartmentPageList(int dataCycleType, String startDate, String endDate) {
        List<AppDepartmentPageBean> list = new ArrayList<>();
        JSONObject lastValue = null;
        JSONArray array;
        do {
            JSONObject response = AppDepartmentPageScene.builder().orderColumn(0).isReverse(false).dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).lastValue(lastValue).size(10).build().invoke(visitor);
            lastValue = response.getJSONObject("last_value");
            array = response.getJSONArray("list");
            array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppDepartmentPageBean.class)).forEach(list::add);
        } while (array.size() == 10);
        return list;
    }

    /**
     * 获取app个人接待详情-记录分页
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 接待详情集合
     */
    public List<AppPersonalPageBean> getAppPersonalPageList(int dataCycleType, String salesId, String startDate, String endDate) {
        List<AppPersonalPageBean> list = new ArrayList<>();
        JSONObject lastValue = null;
        JSONArray array;
        do {
            JSONObject response = AppPersonalPageScene.builder().orderColumn(100).isReverse(false).dataCycleType(dataCycleType).salesId(salesId).startDate(startDate).endDate(endDate).lastValue(lastValue).size(10).build().invoke(visitor);
            lastValue = response.getJSONObject("last_value");
            array = response.getJSONArray("list");
            array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppPersonalPageBean.class)).forEach(list::add);
        } while (array.size() == 10);
        return list;
    }

    /**
     * 获取今日数据信息集合
     *
     * @return 今日数据集合
     */
    public List<AppTodayDataBean> getAppTodayDataList() {
        List<AppTodayDataBean> list = new ArrayList<>();
        JSONObject lastValue = null;
        JSONArray array;
        do {
            JSONObject response = AppTodayDataScene.builder().type("all").lastValue(lastValue).size(20).build().invoke(visitor);
            lastValue = response.getJSONObject("last_value") == null ? null : response.getJSONObject("last_value");
            array = response.getJSONArray("list");
            array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppTodayDataBean.class)).forEach(list::add);
        } while (array.size() == 20);
        return list;
    }

    /**
     * 获取个人数据总览
     *
     * @param dataCycleType 时间类型
     * @param salesId       销售id
     * @param startDate     开始时间
     * @param endDate       结束时间
     * @return 数据总览
     */
    public AppPersonalOverviewBean getAppPersonalOverview(int dataCycleType, String salesId, String startDate, String endDate) {
        IScene scene = AppPersonalOverviewScene.builder().dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).salesId(salesId).build();
        return toJavaObject(scene, AppPersonalOverviewBean.class);
    }

    /**
     * 获取接待详情
     *
     * @param receptionId 接待id
     * @return 接待详情
     */
    public AppDetailBean getAppVoiceRecordDetail(long receptionId) {
        IScene scene = AppDetailScene.builder().id(receptionId).build();
        return toJavaObject(scene, AppDetailBean.class);
    }

    /**
     * 获取接待详情的接待总得分
     *
     * @param receptionId 接待id
     * @return 接待总得分
     */
    public int getAppVoiceRecordDetailScoresSum(long receptionId) {
        return getAppVoiceRecordDetail(receptionId).getScores() == null ? 0 : getAppVoiceRecordDetail(receptionId).getScores().stream().map(e -> (JSONObject) e).mapToInt(e -> e.getInteger("score")).sum();
    }

    /**
     * 获取接待详情中的平均分
     *
     * @param receptionId 接待id
     * @return 平均分
     */
    public Integer getAppAverageScoreByReceptionDetail(Long receptionId) {
        IScene scene = AppDetailScene.builder().id(receptionId).build();
        JSONArray scores = toJavaObject(scene, AppDetailBean.class).getScores();
        if (scores == null) {
            return null;
        }
        int scoreSum = scores.stream().map(e -> (JSONObject) e).mapToInt(e -> e.getInteger("score")).sum();
        return CommonUtil.getCeilIntRatio(scoreSum, 5);
    }

    /**
     * 获取总分
     *
     * @param receptionId 接待id
     * @return 总分
     */
    public Integer getAppDetailScoreSum(Long receptionId) {
        IScene scene = AppDetailScene.builder().id(receptionId).build();
        JSONArray scores = toJavaObject(scene, AppDetailBean.class).getScores();
        return scores == null ? 0 : scores.stream().map(e -> (JSONObject) e).mapToInt(e -> e.getInteger("score")).sum();
    }

    /**
     * 获取pc接待详情
     *
     * @param receptionId 接待id
     * @return 接待详情
     */
    public JSONObject getVoiceDetail(Long receptionId) {
        IScene scene = VoiceDetailScene.builder().id(receptionId).build();
        return scene.invoke(visitor);
    }

    /**
     * 获取app接待列表
     *
     * @return 接待列表
     */
    public List<AppPreSalesReceptionPageBean> getAppAppPreSalesReceptionPageList() {
        List<AppPreSalesReceptionPageBean> list = new ArrayList<>();
        Integer lastValue = null;
        JSONArray array;
        do {
            IScene scene = AppPreSalesReceptionPageScene.builder().size(10).lastValue(lastValue).build();
            JSONObject response = scene.invoke(visitor);
            lastValue = response.getInteger("last_value");
            array = response.getJSONArray("list");
            array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppPreSalesReceptionPageBean.class)).forEach(list::add);
        } while (array.size() == 10);
        return list;
    }

    /**
     * app接待时编辑个人资料
     *
     * @param receptionId 接待id
     * @param customerId  客户id
     */
    public void appCustomerEditV4(String receptionId, String customerId) {
        String shopId = getReceptionShopId();
        int carModelId = getCarModelId(shopId);
        JSONObject response = AppCustomerDetailV4Scene.builder().id(receptionId).customerId(customerId).shopId(shopId).build().invoke(visitor);
        String customerName = response.getString("customer_name");
        int gender = response.getInteger("customer_gender");
        String customerPhone = response.getString("customer_phone");
        String estimateBuyCarDate = "2100-01-01";
        AppCustomerEditV4Scene.builder().shopId(shopId).id(receptionId).customerId(customerId)
                .customerName(customerName).customerPhone(customerPhone).intentionCarModelId(String.valueOf(carModelId))
                .estimateBuyCarDate(estimateBuyCarDate).sexId(gender).build().invoke(visitor);
    }

    /**
     * 获取车型id
     *
     * @param shopId 店铺id
     * @return 车型id
     */
    public Integer getCarModelId(String shopId) {
        IScene carModelTreeScene = AppCarModelTreeScene.builder().shopId(shopId).build();
        return carModelTreeScene.invoke(visitor).getJSONArray("children").getJSONObject(0).getJSONArray("children")
                .getJSONObject(0).getJSONArray("children").getJSONObject(0).getInteger("value");
    }

    /**
     * 获取指定父权限可选择的权限
     *
     * @param parentRole 父权限
     * @return 权限map
     */
    public Map<Integer, String> getAuthRoleMap(int parentRole) {
        Map<Integer, String> map = new HashMap<>();
        IScene scene = AuthTreeScene.builder().parentRole(parentRole).build();
        scene.invoke(visitor).getJSONArray("children").stream().map(e -> (JSONObject) e)
                .forEach(e -> e.getJSONArray("children").stream().map(a -> (JSONObject) a)
                        .forEach(a -> map.put(a.getInteger("value"), a.getString("label"))));
        return map;
    }

    /**
     * 获取任意一个权限map
     *
     * @return map
     */
    public Map<Integer, String> getRandomRoleMap() {
        Map<Integer, String> map = new HashMap<>();
        IScene scene = RoleListScene.builder().build();
        List<JSONObject> list = toJavaObjectList(scene, JSONObject.class, "list");
        JSONObject response = list.stream().filter(e -> !e.getString("name").equals("超级管理员")).findFirst().orElse(null);
        Preconditions.checkArgument(response != null, "角色为空");
        map.put(response.getInteger("id"), response.getString("name"));
        return map;
    }

    /**
     * 获取门店列表map
     *
     * @return map
     */
    public JSONArray getShopIdArray() {
        return ShopListScene.builder().build().invoke(visitor).getJSONArray("list");
    }

    /**
     * 删除工作人员
     *
     * @param phone 电话号
     */
    public void deleteStaff(String phone) {
        IScene scene = StaffPageScene.builder().phone(phone).build();
        JSONObject response = toFirstJavaObject(scene, JSONObject.class);
        String id = response.getString("id");
        StaffDeleteScene.builder().id(id).build().invoke(visitor);
    }

    public void addRole(int parentRoleId) {
        List<Integer> authList = new ArrayList<>(getAuthRoleMap(parentRoleId).keySet());
        RoleAddScene.builder().name("自动化创建全部权限").description("这是一个最大权限的角色").parentRoleId(parentRoleId).authList(authList).build().invoke(visitor);
    }

    /**
     * 批量删除角色
     *
     * @param roleName 角色名称
     */
    public void deleteRole(String... roleName) {
        Arrays.stream(roleName).forEach(e -> deleteRole(e = e == null ? "自动化创建全部权限" : e));
    }

    /**
     * 删除一个角色
     *
     * @param roleName 角色名称
     */
    public void deleteRole(String roleName) {
        IScene scene = RolePageScene.builder().name(roleName).build();
        JSONObject object = toJavaObject(scene, JSONObject.class, "name", roleName);
        int id = object.getInteger("id");
        RoleDeleteScene.builder().id(id).build().invoke(visitor);
    }

    /**
     * 编辑一个账号
     *
     * @param id 账号id
     */
    public void editStaff(String id) {
        JSONObject response = StaffDetailScene.builder().id(id).build().invoke(visitor);
        JSONArray roleList = response.getJSONArray("role_list");
        String name = response.getString("name");
        String phone = response.getString("phone");
        StaffEditScene.builder().roleList(roleList).name(name).phone(phone).id(id).build().invoke(visitor);
    }

    public JSONArray getSubmitLink(boolean addItem) {
        String str = "{\"links\":[{\"type\":100,\"type_name\":\"欢迎接待\",\"items\":[{\"answer_number\":2,\"answers\":[{\"score\":5,\"content\":\"A\"},{\"score\":1,\"content\":\"B\"}],\"title\":\"欢迎接待题目1\"}]},{\"type\":200,\"type_name\":\"个性需求\",\"items\":[{\"answer_number\":3,\"answers\":[{\"score\":5,\"content\":\"A\"},{\"score\":3,\"content\":\"B\"},{\"score\":1,\"content\":\"C\"}],\"title\":\"个性需求题目1\"}]},{\"type\":300,\"type_name\":\"新车推荐\",\"items\":[{\"answer_number\":5,\"answers\":[{\"score\":5,\"content\":\"A\"},{\"score\":4,\"content\":\"B\"},{\"score\":3,\"content\":\"C\"},{\"score\":2,\"content\":\"D\"},{\"score\":1,\"content\":\"E\"}],\"title\":\"新车推荐题目1\"}]},{\"type\":400,\"type_name\":\"试乘试驾\",\"items\":[{\"answer_number\":3,\"answers\":[{\"score\":5,\"content\":\"A\"},{\"score\":3,\"content\":\"B\"},{\"score\":1,\"content\":\"C\"}],\"title\":\"试乘试驾题目1\"}]},{\"type\":500,\"type_name\":\"车辆提案\",\"items\":[{\"answer_number\":2,\"answers\":[{\"score\":5,\"content\":\"A\"},{\"score\":1,\"content\":\"B\"}],\"title\":\"车辆提案题目1\"}]}]}";
        JSONObject jsonObject = JSONObject.parseObject(str);
        JSONArray links = jsonObject.getJSONArray("links");
        String newItem = "{\"title\":\"欢迎接待题目2\",\"answer_number\":3,\"answers\":[{\"score\":5,\"content\":\"1\"},{\"score\":3,\"content\":\"2\"},{\"score\":1,\"content\":\"3\"}]}";
        if (addItem) {
            links.stream().map(e -> (JSONObject) e).filter(e -> e.getString("type_name").equals("欢迎接待")).forEach(e -> e.getJSONArray("items").add(JSONObject.parseObject(newItem)));
        }
        return links;
    }

    public String getNotReceptionPhone() {
        String phone = "153" + CommonUtil.getRandom(8);
        int total = PreSaleCustomerPageScene.builder().customerPhone(phone).build().invoke(visitor).getInteger("total");
        return total == 0 ? phone : getNotReceptionPhone();
    }

    public Object[][] getType() {
        return new Object[][]{
                {"欢迎接待", 100},
                {"个性需求", 200},
                {"新车推荐", 300},
                {"试乘试驾", 400},
                {"车辆提案", 500}
        };
    }

    /**
     * 创建底盘号
     *
     * @return 底盘号
     */
    public String createVin() {
        String vin = "AAASSDFD" + CommonUtil.getRandom(9);
        IScene scene = PreSaleCustomerPageScene.builder().build();
        List<JSONObject> list = toJavaObjectList(scene, JSONObject.class, "customer_type_name", "成交客户");
        List<String> vehicleChassisCodeList = new ArrayList<>();
        list.stream().map(e -> e.getLong("customer_id")).map(e -> PreSaleCustomerInfoBuyCarRecordScene.builder().customerId(e).build())
                .map(e -> toJavaObjectList(e, JSONObject.class)).forEach(e -> e.stream().map(a -> a.getString("vehicle_chassis_code")).forEach(vehicleChassisCodeList::add));
        return !vehicleChassisCodeList.contains(vin) ? vin : createVin();
    }

    public String getReceptionShopId() {
        return visitor.isDaily() ? "56666" : "";
    }

    public String getSaleId() {
        return visitor.isDaily() ? "uid_23562d04" : "";
    }

    public String getCarModelId() {
        return visitor.isDaily() ? "676" : "";
    }

    public String getCarStyleId() {
        return visitor.isDaily() ? "1398" : "";
    }

    /**
     * @description : 导出通用所有的页面（固定导出当前页）
     * 参数：path ：导出页面path
     * @date :2021/7/13 12:48
     **/

    public JSONObject carPageExport(String path) {
        JSONObject thisPage = new JSONObject();
        if ("/car-platform/pc/manage/evaluate/v4/export".equals(path)) { // 销售接待线下评价页面需要附加参数
            thisPage.put("evaluate_type", 5);
        }
        if ("/car-platform/pc/brand/car-style/export".equals(path)) {
            thisPage.put("brand_id", "1526");
        }
        if ("/car-platform/pc/brand/car-style/car-model/export".equals(path)) {
            thisPage.put("style_id", "1584");
        }
        thisPage.put("page", 1);
        thisPage.put("size", 10);
        thisPage.put("export_type", "CURRENT_PAGE"); // 当前页  ALL为所有
        return visitor.invokeApi(path, thisPage, false);
    }

    /**
     * @description : 导出记录页面查询校验
     **/
    public JSONObject checkExport() {
        visitor.setProduct(EnumTestProduce.YT_DAILY_CAR);
        return ExportPageScene.builder().page(1).size(10).build().invoke(visitor, true);
    }

    /**
     * @description : 导入记录页面查询校验
     **/
    public JSONObject checkImport() {
        visitor.setProduct(EnumTestProduce.YT_DAILY_CAR);
        return ImportPageScene.builder().page(1).size(10).build().invoke(visitor, true);
    }

    /**
     * @description : 删除记录页面查询校验
     **/
    public JSONObject checkDelete() {
        visitor.setProduct(EnumTestProduce.YT_DAILY_CAR);
        return DeleteRecordPageScene.builder().page(1).size(10).build().invoke(visitor, true);
    }

    /**
     * @description : 登陆记录页面查询校验
     **/
    public JSONObject checkLogin() {
        visitor.setProduct(EnumTestProduce.YT_DAILY_CAR);
        return LoginRecordPageScene.builder().page(1).size(10).build().invoke(visitor, true);
    }

}
