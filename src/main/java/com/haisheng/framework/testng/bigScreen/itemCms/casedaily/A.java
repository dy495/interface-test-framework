package com.haisheng.framework.testng.bigScreen.itemCms.casedaily;

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
import com.haisheng.framework.testng.bigScreen.itemCms.common.enumerator.EnumDataLayout;
import com.haisheng.framework.testng.bigScreen.itemCms.common.enumerator.EnumDeviceType;
import com.haisheng.framework.testng.bigScreen.itemCms.common.enumerator.EnumManufacturer;
import com.haisheng.framework.testng.bigScreen.itemCms.common.enumerator.EnumRegionType;
import com.haisheng.framework.testng.bigScreen.itemCms.common.scene.*;
import com.haisheng.framework.testng.bigScreen.itemCms.common.util.CmsConstants;
import com.haisheng.framework.testng.bigScreen.itemCms.common.util.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

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

    @Test
    public void test() {
        IContainer container = new ExcelContainer.Builder().path("src/main/java/com/haisheng/framework/testng/bigScreen/itemcms/common/multimedia/file/店铺.xlsx").build();
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
        table.load();
        String tableName = table.getKey();
        if (Arrays.stream(EnumDataLayout.values()).anyMatch(e -> e.name().equals(tableName))) {
            floorGenerator(subjectId, table);
        } else if (tableName.equals(CmsConstants.SHOP_TABLE_NAME)) {
            shopGenerator(subjectId, table);
        } else if (tableName.equals(CmsConstants.ENTRANCE_TABLE_NAME)) {
            entranceGenerator(subjectId, table);
        }
    }

    /**
     * 生成楼层
     *
     * @param subjectId 主体id
     * @param table     表数据
     */
    private void floorGenerator(long subjectId, ITable table) {
        String tableName = table.getKey();
        EnumDataLayout enumDataLayout = EnumDataLayout.finEnumByName(tableName);
        Long layoutId = getLayoutIdByLayoutName(subjectId, enumDataLayout);
        Long regionId = getRegionIdByRegionName(subjectId, tableName);
        Arrays.stream(table.getRows()).map(row -> createDeviceAndGetDeviceMap(subjectId, row))
                .forEach(map -> map.entrySet().stream().filter(e -> e.getValue() != null).forEach(e -> {
                    DataLayoutDeviceScene.builder().deviceId(e.getValue()).layoutId(layoutId).build().invoke(visitor);
                    DataEntranceScene.builder().regionId(regionId).entranceName(e.getKey()).entranceType("REGION").build().invoke(visitor);
                }));
    }

    /**
     * 生成店铺区域
     *
     * @param subjectId 主体id
     * @param table     表数据
     */
    private void shopGenerator(long subjectId, ITable table) {
        Arrays.stream(table.getRows()).forEach(row -> {
            String shopName = row.getField(CmsConstants.TABLE_FIELD_SHOP_NAME).getValue();
            String floorName = row.getField(CmsConstants.TABLE_FIELD_FLOOR_NAME).getValue();
            EnumDataLayout enumDataLayout = StringUtils.isEmpty(floorName) ? EnumDataLayout.L1 : EnumDataLayout.finEnumByName(floorName.replace("层", ""));
            Long layoutId = getLayoutIdByLayoutName(subjectId, enumDataLayout);
            DataRegionScene.builder().subjectId(subjectId).regionName(shopName).layoutId(layoutId).regionType(EnumRegionType.GENERAL.name()).build().invoke(visitor);
            Long regionId = getRegionIdByRegionName(subjectId, shopName);
            createDeviceAndGetDeviceMap(subjectId, row).entrySet().stream().filter(map -> map.getValue() != null).forEach(e -> {
                DataLayoutDeviceScene.builder().deviceId(e.getValue()).layoutId(layoutId).build().invoke(visitor);
                DataRegionDeviceScene.builder().regionId(regionId).deviceId(e.getValue()).build().invoke(visitor);
                DataEntranceScene.builder().regionId(regionId).entranceName(e.getKey()).entranceType("REGION").isWhole(false).build().invoke(visitor);
            });
        });
    }

    /**
     * 生成出入口
     *
     * @param subjectId 主体id
     * @param table     表数据
     */
    private void entranceGenerator(long subjectId, ITable table) {
        Arrays.stream(table.getRows()).forEach(row -> {
            String floorName = row.getField(CmsConstants.TABLE_FIELD_FLOOR_NAME).getValue().replace("层", "");
            EnumDataLayout enumDataLayout = EnumDataLayout.finEnumByName(floorName);
            Long layoutId = getLayoutIdByLayoutName(subjectId, enumDataLayout);
            Long regionId = getRegionIdByRegionName(subjectId, floorName);
            createDeviceAndGetDeviceMap(subjectId, row).entrySet().stream().filter(map -> map.getValue() != null).forEach(e -> {
                DataLayoutDeviceScene.builder().deviceId(e.getValue()).layoutId(layoutId).build().invoke(visitor);
                DataEntranceScene.builder().regionId(regionId).entranceName(e.getKey()).entranceType("REGION").build().invoke(visitor);
            });
        });
    }

    /**
     * 通过平面名称获取平面id
     *
     * @param subjectId      主体id
     * @param enumDataLayout 平面枚举
     * @return 平面id
     */
    private Long getLayoutIdByLayoutName(Long subjectId, EnumDataLayout enumDataLayout) {
        Integer floorId = enumDataLayout.getFloorId();
        String layoutName = enumDataLayout.name();
        IScene scene = LayoutListScene.builder().subjectId(subjectId).build();
        JSONObject rsp = util.toJavaObject(scene, JSONObject.class, "floor_id", floorId);
        return rsp == null ? createLayoutAndGetLayoutId(subjectId, layoutName) : rsp.getLong("layout_id");
    }

    /**
     * 通过区域名称获取区域id
     *
     * @param subjectId  主体id
     * @param regionName 区域名称
     * @return 区域id
     */
    private Long getRegionIdByRegionName(Long subjectId, String regionName) {
        IScene scene = RegionListScene.builder().regionName(regionName).subjectId(subjectId).build();
        return util.toFirstJavaObject(scene, JSONObject.class).getLong("region_id");
    }

    /**
     * 创建平面并获取平面名称获取楼层id
     *
     * @param subjectId 主体id
     * @return 创建好的平面id
     */
    public Long createLayoutAndGetLayoutId(long subjectId, String layoutName) {
        int floorId = EnumDataLayout.finEnumByName(layoutName).getFloorId();
        DataLayoutScene.builder().name(layoutName).description(layoutName).subjectId(subjectId).floorId(floorId).build().invoke(visitor);
        IScene scene = LayoutListScene.builder().subjectId(subjectId).build();
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
        IField shopName = row.getField(CmsConstants.TABLE_FIELD_SHOP_NAME);
        IField floor = row.getField(CmsConstants.TABLE_FIELD_FLOOR_NAME);
        IField pointName = row.getField(CmsConstants.TABLE_FIELD_POINT_NAME);
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
        String pointOne = row.getField(CmsConstants.TABLE_FIELD_POINT_ONE).getValue();
        String pointTwo = row.getField(CmsConstants.TABLE_FIELD_POINT_TWO).getValue();
        if (!StringUtils.isEmpty(pointOne)) {
            String deviceName = name + CmsConstants.TABLE_FIELD_POINT_ONE;
            map.put(deviceName, createDeviceAndGetDeviceId(subjectId, deviceName, pointOne));
        }
        if (!StringUtils.isEmpty(pointTwo)) {
            String deviceName = name + CmsConstants.TABLE_FIELD_POINT_TWO;
            map.put(deviceName, createDeviceAndGetDeviceId(subjectId, deviceName, pointTwo));
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
}
