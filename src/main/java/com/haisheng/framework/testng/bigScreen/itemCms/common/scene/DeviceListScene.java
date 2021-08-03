package com.haisheng.framework.testng.bigScreen.itemCms.common.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 设备管理列表
 *
 * @author wangmin
 * @date 2021/08/03
 */
@Builder
public class DeviceListScene extends BaseScene {
    private final String name;
    @Builder.Default
    private final Integer level = 2;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("level", level);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/admin/data/device/list";
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }
}
