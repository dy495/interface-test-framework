package com.haisheng.framework.testng.bigScreen.itemCms.common.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.datacheck.AliyunConfig;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.container.IContainer;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.field.IField;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.row.IRow;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.table.ITable;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.util.BasicUtil;
import com.haisheng.framework.testng.bigScreen.itemCms.common.enumerator.*;
import com.haisheng.framework.testng.bigScreen.itemCms.common.scene.*;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class SceneUtil extends BasicUtil {
    private final VisitorProxy visitor;

    public SceneUtil(VisitorProxy visitor) {
        super(visitor);
        this.visitor = visitor;
    }

    public void login(EnumAccount account) {
        String email = "28e3e02ba627a44c949d3ef94b217388";
        IScene scene = LoginScene.builder().email(account.getPhone()).password(email).build();
        visitor.setToken(scene);
    }


    public Long getDeploymentGroupId() {
        return visitor.isDaily() ? 72L : 0L;
    }

    public Long getDeploymentId() {
        return visitor.isDaily() ? 256L : 0L;
    }

    public String getCloudSceneType() {
        return EnumCloudSceneType.AUTO_DEFAULT.name();
    }

    public AliyunConfig getConfig() {
        AliyunConfig config = new AliyunConfig();
        config.setAccessKeyId("LTAI5t8wVqrm9pfHZswwRok1");
        config.setAccessKeySecret("O1DToNZRCeRxVdVewpuJrg4sPKojHe");
        config.setEndPoint("http://oss-cn-beijing.aliyuncs.com");
        return config;
    }

    public File downloadFile() throws AssertionError {
        String tableName = System.getProperty("TABLE_NAME");
        Preconditions.checkArgument(tableName != null, "表名不可为空");
        AliyunConfig config = getConfig();
        OSS ossClient = new OSSClientBuilder().build(config.getEndPoint(), config.getAccessKeyId(), config.getAccessKeySecret());
        logger.info("文件开始下载");
        String objectName = visitor.isDaily() ? "test/日常/" + tableName : "test/线上/" + tableName;
        GetObjectRequest objectRequest = new GetObjectRequest("retail-huabei2", objectName);
        String userDir = System.getProperty("user.dir");
        File file = new File(userDir + "/src/main/java/com/haisheng/framework/testng/bigScreen/itemCms/common/multimedia/file/" + tableName);
        logger.info(file.exists() ? "文件已存在" : "文件生成路径：{} ObjectMetadata：{}", file.getPath(), ossClient.getObject(objectRequest, file));
        return file;
    }

    public void deleteFile(File file) {
        logger.info(file == null ? "文件不存在" : file.exists() && file.isFile() ? "删除文件--" + file.delete() : "文件不存在或者不是文件");
    }

    /**
     * 获取主体Id
     *
     * @param container 容器
     * @return 主体id
     */
    public long getSubjectId(IContainer container) {
        ITable table = container.getTable(CmsConstants.SUBJECT_TABLE_NAME);
        Preconditions.checkNotNull(table, "没有主体表");
        table.load();
        IRow[] rows = table.getRows();
        Preconditions.checkArgument(rows.length != 0, "主体为空");
        String subjectId = table.getRows()[0].getField("subject_id").getValue();
        return Long.parseLong(subjectId);
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
                    DataLayoutDeviceScene.builder().deviceId(e.getValue()).layoutId(layoutId).build().visitor(visitor).execute();
                    DataEntranceScene.builder().regionId(regionId).entranceName(e.getKey()).entranceType("REGION").build().visitor(visitor).execute();
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
            DataRegionScene.builder().subjectId(subjectId).regionName(shopName).layoutId(layoutId).regionType(EnumRegionType.GENERAL.name()).build().visitor(visitor).execute();
            Long regionId = getRegionIdByRegionName(subjectId, shopName);
            createDeviceAndGetDeviceMap(subjectId, row).entrySet().stream().filter(map -> map.getValue() != null).forEach(e -> {
                DataLayoutDeviceScene.builder().deviceId(e.getValue()).layoutId(layoutId).build().visitor(visitor).execute();
                DataRegionDeviceScene.builder().regionId(regionId).deviceId(e.getValue()).build().visitor(visitor).execute();
                DataEntranceScene.builder().regionId(regionId).entranceName(e.getKey()).entranceType("REGION").isWhole(false).build().visitor(visitor).execute();
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
                DataLayoutDeviceScene.builder().deviceId(e.getValue()).layoutId(layoutId).build().visitor(visitor).execute();
                DataEntranceScene.builder().regionId(regionId).entranceName(e.getKey()).entranceType("REGION").build().visitor(visitor).execute();
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
        JSONObject rsp = toJavaObject(scene, JSONObject.class, "floor_id", floorId);
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
        return toFirstJavaObject(scene, JSONObject.class).getLong("region_id");
    }

    /**
     * 创建平面并获取平面名称获取楼层id
     *
     * @param subjectId 主体id
     * @return 创建好的平面id
     */
    public Long createLayoutAndGetLayoutId(long subjectId, String layoutName) {
        int floorId = EnumDataLayout.finEnumByName(layoutName).getFloorId();
        DataLayoutScene.builder().name(layoutName).description(layoutName).subjectId(subjectId).floorId(floorId).build().visitor(visitor).execute();
        IScene scene = LayoutListScene.builder().subjectId(subjectId).build();
        return toJavaObject(scene, JSONObject.class, "floor_id", floorId).getLong("layout_id");
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
                .deploymentGroupId(getDeploymentGroupId()).deploymentId(getDeploymentId())
                .cloudSceneType(getCloudSceneType()).build().visitor(visitor).execute();
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
        return toJavaObject(scene, JSONObject.class, "name", deviceName).getString("device_id");
    }
}
