package com.haisheng.framework.testng.bigScreen.itemYuntong.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.util.BasicUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.DeleteRecordPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.homepagev4.AppTodayDataBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.presalesreception.AppPreSalesReceptionPageBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.homepagev4.AppTodayDataScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppPreSalesReceptionCreateScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.afterreception.AppAfterSalesReceptionCreate;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppCustomerEditV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppPreSalesReceptionDetailV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppPreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppPreSalesReceptionCreateV7;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.task.AppReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerBuyCarPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanagev4.PreSaleCustomerInfoBuyCarRecordScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.record.ExportPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.record.ImportPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.record.LoginRecordPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.personaldata.AppPersonalOverviewBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.voicerecord.AppDepartmentPageBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.voicerecord.AppDetailBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.voicerecord.AppPersonalPageBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.carmodel.AppCarModelTreeScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.personaldata.AppPersonalOverviewScene;
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
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.bean.AfterReceptionBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.bean.PreReceptionBean;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
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
        EnumTestProduct oldProduce = visitor.getProduct();
        EnumTestProduct newProduce = visitor.isDaily() ? EnumTestProduct.YT_DAILY_ZH : EnumTestProduct.YT_ONLINE_ZH;
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
            JSONObject response = AppDepartmentPageScene.builder().orderColumn(0).isReverse(false).dataCycleType(dataCycleType).startDate(startDate).endDate(endDate).lastValue(lastValue).size(10).build().visitor(visitor).execute();
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
            JSONObject response = AppPersonalPageScene.builder().orderColumn(100).isReverse(false).dataCycleType(dataCycleType).salesId(salesId).startDate(startDate).endDate(endDate).lastValue(lastValue).size(10).build().visitor(visitor).execute();
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
        String lastValue = null;
        JSONArray array;
        do {
            JSONObject response = AppTodayDataScene.builder().type("all").lastValue(lastValue).size(20).build().visitor(visitor).execute();
            lastValue = response.getString("last_value") == null ? null : response.getString("last_value");
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
        return scene.visitor(visitor).execute();
    }

    /**
     * 获取app接待列表
     *
     * @return 接待列表
     */
    public List<AppPreSalesReceptionPageBean> getAppPreSalesReceptionPageList() {
        List<AppPreSalesReceptionPageBean> list = new ArrayList<>();
        Integer lastValue = null;
        JSONArray array;
        do {
            IScene scene = AppPreSalesReceptionPageScene.builder().size(10).lastValue(lastValue).build();
            JSONObject response = scene.visitor(visitor).execute();
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
    public void appCustomerEditV4(Long receptionId, Long customerId) {
        String shopId = getReceptionShopId();
        int carModelId = getCarModelId(shopId);
        JSONObject response = AppPreSalesReceptionDetailV4Scene.builder().id(receptionId).customerId(customerId).shopId(Long.parseLong(shopId)).build().visitor(visitor).execute();
        String customerName = response.getString("customer_name");
        int gender = response.getInteger("customer_gender");
        String customerPhone = response.getString("customer_phone");
        String estimateBuyCarDate = "2100-01-01";
        AppCustomerEditV4Scene.builder().shopId(Long.parseLong(shopId)).id(receptionId).customerId(customerId)
                .customerName(customerName).customerPhone(customerPhone).intentionCarModelId((long)carModelId)
                .estimateBuyCarDate(estimateBuyCarDate).sexId(gender).build().visitor(visitor).execute();
    }

    /**
     * 获取车型id
     *
     * @param shopId 店铺id
     * @return 车型id
     */
    public Integer getCarModelId(String shopId) {
        IScene carModelTreeScene = AppCarModelTreeScene.builder().shopId(shopId).build();
        return carModelTreeScene.visitor(visitor).execute().getJSONArray("children").getJSONObject(0).getJSONArray("children")
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
        scene.visitor(visitor).execute().getJSONArray("children").stream().map(e -> (JSONObject) e)
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
        return ShopListScene.builder().build().visitor(visitor).execute().getJSONArray("list");
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
        StaffDeleteScene.builder().id(id).build().visitor(visitor).execute();
    }

    public void addRole(int parentRoleId) {
        List<Integer> authList = new ArrayList<>(getAuthRoleMap(parentRoleId).keySet());
        RoleAddScene.builder().name("自动化创建全部权限").description("这是一个最大权限的角色").parentRoleId(parentRoleId).authList(authList).build().visitor(visitor).execute();
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
        RoleDeleteScene.builder().id(id).build().visitor(visitor).execute();
    }

    /**
     * 编辑一个账号
     *
     * @param id 账号id
     */
    public void editStaff(String id) {
        JSONObject response = StaffDetailScene.builder().id(id).build().visitor(visitor).execute();
        JSONArray roleList = response.getJSONArray("role_list");
        String name = response.getString("name");
        String phone = response.getString("phone");
        StaffEditScene.builder().roleList(roleList).name(name).phone(phone).id(id).build().visitor(visitor).execute();
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

    /**
     * 获取不重复的电话号
     *
     * @return 电话号
     */
    public String getNotExistPhone() {
        String phone = "153" + CommonUtil.getRandom(8);
        int total = PreSaleCustomerPageScene.builder().customerPhone(phone).build().visitor(visitor).execute().getInteger("total");
        return total == 0 ? phone : getNotExistPhone();
    }

    /**
     * 获取存在的电话号
     *
     * @return 电话号
     */
    public String getExistPhone() {
        return PreSaleCustomerPageScene.builder().build().visitor(visitor).execute().getJSONArray("list").getJSONObject(9).getString("customer_phone");
    }

    /**
     * 获取存在的底盘号
     *
     * @return 地盘号
     */
    public String getExistVin() {
        IScene scene = PreSaleCustomerBuyCarPageScene.builder().build();
        JSONArray list = scene.visitor(visitor).execute().getJSONArray("list");
        JSONObject jsonObject = list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("vehicle_chassis_code") != null).findFirst().orElse(null);
        Preconditions.checkNotNull(jsonObject, "未找到底盘号");
        return jsonObject.getString("vehicle_chassis_code");
    }

    /**
     * 获取不存在的底盘号
     *
     * @return 底盘号
     */
    public String getNotExistVin() {
        String vin = "AAASSDFD" + CommonUtil.getRandom(9);
        IScene scene = PreSaleCustomerPageScene.builder().build();
        List<JSONObject> list = toJavaObjectList(scene, JSONObject.class, "customer_type_name", "成交客户");
        List<String> vehicleChassisCodeList = new ArrayList<>();
        list.stream().map(e -> e.getLong("customer_id")).map(e -> PreSaleCustomerInfoBuyCarRecordScene.builder().customerId(e).build())
                .map(e -> toJavaObjectList(e, JSONObject.class)).forEach(e -> e.stream().map(a -> a.getString("vehicle_chassis_code")).forEach(vehicleChassisCodeList::add));
        return !vehicleChassisCodeList.contains(vin) ? vin : getNotExistVin();
    }

    public String getReceptionShopId() {
        return visitor.isDaily() ? EnumAccount.YT_DAILY_WM.getReceptionShopId() : EnumAccount.YT_ONLINE_LXQ.getReceptionShopId();
    }

    public String getSaleId() {
        return visitor.isDaily() ? "uid_23562d04" : "uid_a024b8b5";
    }

    public String getCarModelId() {
        return visitor.isDaily() ? "676" : "20882";
    }

    public String getCarStyleId() {
        return visitor.isDaily() ? "1398" : "2513";
    }

    public String getRoleId() {
        return visitor.isDaily() ? "9872" : "";
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
            thisPage.put("brand_id", visitor.isDaily() ? "1526" : "1342");
        }
        if ("/car-platform/pc/brand/car-style/car-model/export".equals(path)) {
            thisPage.put("style_id", visitor.isDaily() ? "1584" : "2527");
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
        return ExportPageScene.builder().build().visitor(visitor).execute();
    }

    /**
     * @description : 导入记录页面查询校验
     **/
    public JSONObject checkImport() {
        return ImportPageScene.builder().build().visitor(visitor).execute();
    }

    /**
     * @description : 删除记录页面查询校验
     **/
    public JSONObject checkDelete() {
        return DeleteRecordPageScene.builder().page(1).size(10).build().visitor(visitor).execute();
    }

    /**
     * @description : 登陆记录页面查询校验
     **/
    public JSONObject checkLogin() {
        return LoginRecordPageScene.builder().build().visitor(visitor).execute();
    }

    /**
     * @return :{"id":接待id, "shopId": shopId, "customerId":customerId}
     * @description :创建一个接待，这没用了
     **/
    public Map<String, String> createCustomerCommon(String name, String sex, String phone, String carId, String buyTime) {
        Map<String, String> customer = new HashMap<>();
        AppPreSalesReceptionCreateScene.builder().customerName(name).customerPhone(phone).sexId(sex).intentionCarModelId(carId).estimateBuyCarTime(buyTime).build().visitor(visitor).execute();//创建销售接待
        JSONObject pageInfo = PreSalesReceptionPageScene.builder().build().visitor(visitor).execute();
        List<JSONObject> newCustomer = pageInfo.getJSONArray("list").stream().map(ele -> (JSONObject) ele).filter(obj -> Objects.equals(phone, obj.getString("customer_phone"))).collect(Collectors.toList());
        String id = newCustomer.get(0).getString("id");
        String shopId = pageInfo.getJSONArray("list").getJSONObject(0).getString("shop_id");
        String customerId = newCustomer.get(0).getString("customer_id");
        customer.put("id", id);
        customer.put("shopId", shopId);
        customer.put("customerId", customerId);
        return customer;
    }

    public String mcCarId() {
        return visitor.isDaily() ? "775" : "20895";
    }
    /**
     * @description: 将虚拟卡片资料维护后返回一个有客户id的接待
     * @param card: 虚拟卡片AppReceptionBean
     * @return : 接待的AppReceptionBean
     **/
    public PreReceptionBean cardToReception(PreReceptionBean card){
        String message = AppCustomerEditV4Scene.builder().id(card.getId()).customerId(card.getCustomerId())
                .shopId(card.getShopId()).customerName("自动创建"+new DateTimeUtil().getHistoryDate(0)).customerPhone("13"+CommonUtil.getRandom(9)).customerSource("NATURE_VISIT")
                .sexId(1).intentionCarModelId(Long.parseLong(mcCarId())).estimateBuyCarDate("2035-12-20").build().visitor(visitor).getResponse().getMessage();
        Preconditions.checkArgument(Objects.equals("success", message),"修改资料失败:"+message);
        return AppPreSalesReceptionPageScene.builder().size(10).lastValue(null).build().visitor(visitor).execute().getJSONArray("list").stream().map(e -> (JSONObject) e)
                .filter(e ->Objects.equals(e.getString("id"), card.getId().toString())).findFirst().get().toJavaObject(PreReceptionBean.class);
    }
    /**
     * @description: 创建一个接待卡片
     * @param : null
     * @return : 接待卡片AppReceptionBean
     **/
    public PreReceptionBean createPreCard(){
        AppPreSalesReceptionCreateV7.builder().build().visitor(visitor).getResponse();
        return AppPreSalesReceptionPageScene.builder().size(10).lastValue(null).build()
                .visitor(visitor).execute().getJSONArray("list").getJSONObject(0).toJavaObject(PreReceptionBean.class);
    }
    /**
     * @description: 获取一个接待卡片
     * @param : null
     * @return : 接待卡片AppReceptionBean
     **/
    public PreReceptionBean getReceptionCard(){
        JSONObject receptionCard = AppPreSalesReceptionPageScene.builder().size(10).lastValue(null).build().visitor(visitor).execute().getJSONArray("list").stream().map(e -> (JSONObject) e)
                .filter(e -> !e.containsKey("customer_id")).findFirst().orElse(null);
        if (receptionCard != null){
            return receptionCard.toJavaObject(PreReceptionBean.class);
        } else {
            return createPreCard();
        }
    }

    /**
     * @description: 获取一个接待，没有则创建
     * @param : null
     * @return : AppReceptionBean
     **/
    public PreReceptionBean getReception(){
        JSONObject findReception = AppPreSalesReceptionPageScene.builder().size(10).lastValue(null).build().visitor(visitor).execute().getJSONArray("list").stream().map(e -> (JSONObject) e).findAny().orElse(null);
        if (findReception == null){
            return cardToReception(createPreCard());
        }else if (findReception.containsKey("customer_id")){
            return findReception.toJavaObject(PreReceptionBean.class);
        }else {
            return cardToReception(findReception.toJavaObject(PreReceptionBean.class));
        }
    }
    /**
     * @Description: 创建一个虚拟卡片
     * @return:
     **/
    public AfterReceptionBean createAfterCard(){
        AppAfterSalesReceptionCreate.builder().build().visitor(visitor).getResponse().getMessage();
        return AppReceptionPageScene.builder().size(10).build().visitor(visitor).execute().getJSONArray("list")
                .getJSONObject(0).toJavaObject(AfterReceptionBean.class);
    }

    /**
     * @Description: 获取到一个售后的接待，没有则创建
     * @return: AfterReceptionBean
     **/
    public AfterReceptionBean getAfterReception(){
        AfterReceptionBean getReception = AppReceptionPageScene.builder().size(10).lastValue(null).build().visitor(visitor).execute().getJSONArray("list").stream().map(e -> (JSONObject) e)
                .map(e -> e.toJavaObject(AfterReceptionBean.class)).findAny().orElse(null);
        if (getReception == null){
            return createAfterCard();
        }else {
            return getReception;
        }
    }



}
