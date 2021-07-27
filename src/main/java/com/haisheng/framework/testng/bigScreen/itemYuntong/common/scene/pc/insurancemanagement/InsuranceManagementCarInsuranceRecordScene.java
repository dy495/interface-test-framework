package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.insurancemanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 24.4. 车辆投保记录 （池）（2021-03-05）
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class InsuranceManagementCarInsuranceRecordScene extends BaseScene {
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
     * 描述 列表id
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/insurance-management/car-insurance-record";
    }
}