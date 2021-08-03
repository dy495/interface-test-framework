package com.haisheng.framework.testng.bigScreen.itemcms.casedaily;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.container.ExcelContainer;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.container.IContainer;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.field.IField;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.row.IRow;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.table.ITable;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemcms.common.enumerator.*;
import com.haisheng.framework.testng.bigScreen.itemcms.common.scene.*;
import com.haisheng.framework.testng.bigScreen.itemcms.common.util.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;

public class A extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct product = EnumTestProduct.CMS_DAILY;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.CMS_DAILY;
    public VisitorProxy visitor = new VisitorProxy(product);
    public ScenarioUtil util = new ScenarioUtil(visitor);

    @BeforeClass
    @Override
    public void initial() {
        CommonConfig commonConfig = new CommonConfig();
        logger.debug("before class initial");
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_XUNDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.GLY.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.product = product.getAbbreviation();
        commonConfig.shopId = product.getShopId();
        commonConfig.referer = product.getReferer();
        beforeClassInit(commonConfig);
        util.login(ALL_AUTHORITY);
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
    }

//    public void createFloat(ITable table, IRow iRow) {
//        IRow[] rows = table.getRows();
//        Arrays.stream(rows).forEach(row -> {
//            String subjectId = getSubjectId(row);
//            String floorName = iRow.getField("楼层").getValue().replace("层", "");
//            int floorId = EnumDataLayout.finEnumByName(floorName).getFloodId();
//            DataLayoutScene.builder().floorId(floorId).description(floorName).name(floorName).subjectId(Long.parseLong(subjectId)).build().invoke(visitor);
//        });
//    }
//
//    public String getSubjectId(@NotNull IRow row) {
//        String appId = row.getField("app_id").getValue();
//        String subjectName = row.getField("subject_name").getValue();
//        String manager = row.getField("manager").getValue();
//        String telephone = row.getField("telephone").getValue();
//        String local = row.getField("local").getValue();
//        String region = row.getField("region").getValue();
//        JSONObject object = SubjectListScene.builder().subjectName(subjectName).build().invoke(visitor);
//        int total = object.getInteger("total");
//        if (total == 0) {
//            Long brandId = BrandArrayScene.builder().appId(appId).build().invoke(visitor).getLong("brand_id");
//            DataSubjectScene.builder().appId(appId).brandId(brandId).subjectType(EnumSubjectType.findEnumByName(subjectName)
//                    .getId()).subjectName(subjectName).region(null).local(local).manager(manager).telephone(telephone).build().invoke(visitor);
//            return getSubjectId(row);
//        } else {
//            return object.getJSONArray("list").getJSONObject(0).getString("subject_id");
//        }
//    }

    @Test
    public void test() {
        IContainer container = new ExcelContainer.Builder().path("src/main/java/com/haisheng/framework/testng/bigScreen/itemcms/common/multimedia/file/model.xlsx").build();
        container.init();
        ITable[] tables = container.getTables();
        Arrays.stream(tables).forEach(table -> createLayoutAndAddDevice(57814L, table));
    }

    /**
     * 创建布局并且添加设备
     *
     * @param subjectId 主体id
     * @param table     表数据
     */
    public void createLayoutAndAddDevice(long subjectId, ITable table) {
        String tableName = table.getKey();
        table.load();
        if (Arrays.stream(EnumDataLayout.values()).anyMatch(e -> e.name().equals(tableName))) {
            long layoutId = createLayoutAndGetLayoutId(subjectId, tableName);
            IScene scene = RegionListScene.builder().regionName(tableName).subjectId(String.valueOf(subjectId)).build();
            long regionId = util.toFirstJavaObject(scene, JSONObject.class).getLong("region_id");
            Arrays.stream(table.getRows()).map(row -> createDeviceAndGetDeviceMap(subjectId, row))
                    .forEach(map -> map.entrySet().stream().filter(e -> e.getValue() != null).forEach(e -> {
                        DataLayoutDeviceScene.builder().deviceId(e.getValue()).layoutId(layoutId).build().invoke(visitor);
                        createEntranceAccess(regionId, e.getKey(), "REGION");
                    }));
        } else if (tableName.equals("店铺")) {
            Arrays.stream(table.getRows()).forEach(row -> {
                String shopName = row.getField("店铺名").getValue();
                String floorName = row.getField("楼层").getValue().replace("层", "");
                int floorId = StringUtils.isEmpty(floorName) ? EnumDataLayout.L1.getFloorId() : EnumDataLayout.finEnumByName(floorName).getFloorId();
                IScene scene = LayoutListScene.builder().subjectId(String.valueOf(subjectId)).build();
                Long layoutId = util.toJavaObject(scene, JSONObject.class, "floor_id", floorId).getLong("layout_id");
                DataRegionScene.builder().subjectId(String.valueOf(subjectId)).regionName(shopName).layoutId(layoutId).regionType(EnumRegionType.GENERAL.name()).build().invoke(visitor);
                IScene regionListScene = RegionListScene.builder().regionName(tableName).subjectId(String.valueOf(subjectId)).build();
                long regionId = util.toJavaObject(regionListScene, JSONObject.class, "region_name", shopName).getLong("region_id");
                createDeviceAndGetDeviceMap(subjectId, row).forEach((key, value) -> {
                    DataRegionDeviceScene.builder().regionId(regionId).deviceId(value).build();
                    createEntranceAccess(regionId, key, "REGION");
                });
            });
        }
    }

    /**
     * 创建平面并获取平面id获取楼层id
     *
     * @param subjectId 主体id
     * @return 创建好的平面id
     */
    public Long createLayoutAndGetLayoutId(long subjectId, String tableName) {
        int floorId = EnumDataLayout.finEnumByName(tableName).getFloorId();
        DataLayoutScene.builder().name(tableName).description(tableName).subjectId(subjectId).floorId(floorId).build().invoke(visitor);
        IScene scene = LayoutListScene.builder().subjectId(String.valueOf(subjectId)).build();
        return util.toJavaObject(scene, JSONObject.class, "floor_id", floorId).getLong("layout_id");
    }

    /**
     * 创建设备并获取每个点的设备id集合
     *
     * @param subjectId 主体id
     * @param row       行数据
     * @return 每个点的设备id集合
     */
    public Map<String, String> createDeviceAndGetDeviceMap(Long subjectId, IRow row) {
        IField shopName = row.getField("店铺名");
        IField floor = row.getField("楼层");
        IField pointName = row.getField("点位名称");
        String name = shopName != null ? floor.getValue() + shopName.getValue() : pointName.getValue();
        return createDeviceAndGetDeviceMap(subjectId, row, name);
    }

    /**
     * 创建设备并获取每个点的设备id集合
     *
     * @param subjectId 主体id
     * @param row       行数据
     * @param name      设备名称
     * @return 每个点的设备id集合
     */
    public Map<String, String> createDeviceAndGetDeviceMap(Long subjectId, IRow row, String name) {
        Map<String, String> map = new LinkedHashMap<>();
        String pointOne = row.getField("点位1").getValue();
        String pointTwo = row.getField("点位2").getValue();
        if (!StringUtils.isEmpty(pointOne)) {
            name = name + "-点位1";
            map.put(name, createDeviceAndGetDeviceId(subjectId, name, pointOne));
        }
        if (!StringUtils.isEmpty(pointTwo)) {
            name = name + "-点位2";
            map.put(name, createDeviceAndGetDeviceId(subjectId, name, pointTwo));
        }
        return map;
    }

    /**
     * 创建设备并且获取设备id
     *
     * @param subjectId 主体id
     * @param name      设备名称
     * @param url       视频流地址
     * @return 设备id
     */
    public String createDeviceAndGetDeviceId(Long subjectId, String name, String url) {
        DataDeviceScene.builder().name(name).deviceType(EnumDeviceType.WEB_CAMERA.name()).url(url)
                .subjectId(String.valueOf(subjectId)).manufacturer(EnumManufacturer.HIKVISION.getName())
                .deploymentGroupId(util.getDeploymentGroupId()).deploymentId(util.getDeploymentId())
                .cloudSceneType(util.getCloudSceneType()).build().invoke(visitor);
        return getDeviceIdByDeviceName(name);
    }

    /**
     * 通过设备名称获取设备id
     *
     * @param deviceName 设备名称
     * @return 设备id
     */
    public String getDeviceIdByDeviceName(String deviceName) {
        IScene scene = DeviceListScene.builder().name(deviceName).build();
        return util.toJavaObject(scene, JSONObject.class, "name", deviceName).getString("device_id");
    }

    /**
     * 创建出入口
     *
     * @param regionId     平面id
     * @param name         出入口名称
     * @param entranceType 出入口类型
     */
    public void createEntranceAccess(Long regionId, String name, String entranceType) {
        DataEntranceScene.builder().regionId(String.valueOf(regionId)).entranceName(name).entranceType(entranceType).build().invoke(visitor);
    }
}
