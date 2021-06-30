package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.packagemanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 17.1. 套餐表单 v1.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class PackageFormPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 套餐状态
     * 是否必填 false
     * 版本 v1.0
     */
    private final Boolean packageStatus;

    /**
     * 描述 套餐名称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String packageName;

    /**
     * 描述 创建者
     * 是否必填 false
     * 版本 v1.0
     */
    private final String creator;

    /**
     * 描述 开始时间
     * 是否必填 false
     * 版本 v1.0
     */
    private final String startTime;

    /**
     * 描述 结束时间
     * 是否必填 false
     * 版本 v1.0
     */
    private final String endTime;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("package_status", packageStatus);
        object.put("package_name", packageName);
        object.put("creator", creator);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/package-manage/package-form/page";
    }
}