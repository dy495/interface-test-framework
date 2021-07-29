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
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduce;
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
import java.util.Arrays;

public class A extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce product = EnumTestProduce.CMS_DAILY;
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
        IContainer container = new ExcelContainer.Builder().path("src/main/java/com/haisheng/framework/testng/bigScreen/itemcms/common/multimedia/file/楼层.xlsx").build();
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
            //创建平面
            int floorId = EnumDataLayout.finEnumByName(tableName).getFloorId();
            DataLayoutScene.builder().name(tableName).description(tableName).subjectId(subjectId).floorId(floorId).build().invoke(visitor);
            long layoutId = getLayoutId(subjectId, floorId);
            IRow[] rows = table.getRows();
            Arrays.stream(rows).forEach(row -> {
                //创建设备并将设备关联进楼层
                String deviceId = createDeviceAndGetDeviceId(subjectId, row);
                if (deviceId != null) {
                    DataLayoutDeviceScene.builder().deviceId(deviceId).layoutId(layoutId).build().invoke(visitor);
                }
            });
//        } else if (tableName.equals("店铺")) {
//            IRow[] rows = table.getRows();
//            Arrays.stream(rows).forEach(row -> {
//                String shopName = row.getField("店铺名").getValue();
//                String floorName = row.getField("楼层").getValue().replace("层", "");
//                IScene scene = LayoutListScene.builder().subjectId(String.valueOf(subjectId)).build();
//                Long layoutId = StringUtils.isEmpty(floorName) ? 0L : util.toJavaObject(scene, JSONObject.class, "floor_id", EnumDataLayout.finEnumByName(floorName).getFloorId()).getLong("layout_id");
//                DataRegionScene.builder().subjectId(String.valueOf(subjectId)).regionName(shopName).layoutId(layoutId).regionType(EnumRegionType.GENERAL.name()).build().invoke(visitor);
//            });
        }
    }

    /**
     * 获取楼层id
     *
     * @param subjectId 主体id
     * @param floorId   楼层id
     * @return 创建好楼层后的id
     */
    public Long getLayoutId(long subjectId, int floorId) {
        IScene scene = LayoutListScene.builder().subjectId(String.valueOf(subjectId)).build();
        return util.toJavaObject(scene, JSONObject.class, "floor_id", floorId).getLong("layout_id");
    }

    /**
     * 创建设备并获取设备id
     *
     * @param subjectId 主体id
     * @param row       行数据
     */
    public String createDeviceAndGetDeviceId(Long subjectId, IRow row) {
        String name;
        IField shopName = row.getField("店铺名");
        if (shopName != null) {
            String floor = row.getField("楼层").getValue();
            name = floor + shopName.getValue();
        } else {
            name = row.getField("点位名称").getValue();
        }
        return createDeviceAndGetDeviceId(subjectId, row, name);
    }

    /**
     * 创建设备并获取设备id
     *
     * @param subjectId 主体id
     * @param row       行数据
     * @param name      设备名称
     */
    public String createDeviceAndGetDeviceId(Long subjectId, IRow row, String name) {
        String pointOne = row.getField("点位1").getValue();
        String pointTwo = row.getField("点位2").getValue();
        if (!StringUtils.isEmpty(pointOne)) {
            name = name + "-点位1";
            createDevice(subjectId, name, pointOne);
            return getDeviceId(name);
        }
        if (!StringUtils.isEmpty(pointTwo)) {
            name = name + "-点位2";
            createDevice(subjectId, name, pointTwo);
            return getDeviceId(name);
        }
        return null;
    }

    /**
     * 获取设备id
     *
     * @param deviceName 设备名称
     * @return 设备id
     */
    public String getDeviceId(String deviceName) {
        IScene scene = DeviceListScene.builder().name(deviceName).build();
        return util.toJavaObject(scene, JSONObject.class, "name", deviceName).getString("device_id");
    }

    /**
     * 创建设备
     *
     * @param subjectId 主体id
     * @param name      设备名称
     * @param url       视频流地址
     */
    public void createDevice(Long subjectId, String name, String url) {
        DataDeviceScene.builder().name(name).deviceType(EnumDeviceType.WEB_CAMERA.name()).url(url)
                .subjectId(String.valueOf(subjectId)).manufacturer(EnumManufacturer.HIKVISION.getName())
                .deploymentGroupId(util.getDeploymentGroupId())
                .deploymentId(util.getDeploymentId())
                .cloudSceneType(util.getCloudSceneType()).build().invoke(visitor);
    }

    @Test
    public void testA() {
//        IContainer container = new ExcelContainer.Builder().path("src/main/java/com/haisheng/framework/testng/bigScreen/itemcms/出入口.xlsx").build();
//        container.init();
//        ITable[] tables = container.getTables();
//        Arrays.stream(tables).forEach(table -> {
//            table.load();
//            logger.info("表名：{}", table.getKey());
//            IRow[] rows = table.getRows();
//            Arrays.stream(rows).forEach(row -> createDevice(57814L, row));
//        });
        String path = DataLayoutDeviceScene.builder().deviceId("8709281125434368").layoutId(7450L).build().getPath();
        System.err.println(path);

    }
}
