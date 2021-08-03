package com.haisheng.framework.testng.bigScreen.itemCms.common.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 创建出入口
 *
 * @author wangmin
 * @date 2021/08/03
 */
@Builder
public class DataEntranceScene extends BaseScene {
    private final String entranceName;
    private final String entranceType;
    @Builder.Default
    private final Boolean useLine = true;
    @Builder.Default
    private final Boolean bothDir = true;
    @Builder.Default
    private final Boolean isStair = true;
    @Builder.Default
    private final Boolean isWhole = true;
    @Builder.Default
    private final Boolean isTriggerByFoot = true;
    private final Long regionId;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("entrance_name", entranceName);
        object.put("entrance_type", entranceType);
        object.put("use_line", useLine);
        object.put("both_dir", bothDir);
        object.put("is_stair", isStair);
        object.put("is_whole", isWhole);
        object.put("is_trigger_by_foot", isTriggerByFoot);
        object.put("region_id", String.valueOf(regionId));
        return object;
    }

    @Override
    public String getPath() {
        return "/admin/data/entrance/";
    }
}
