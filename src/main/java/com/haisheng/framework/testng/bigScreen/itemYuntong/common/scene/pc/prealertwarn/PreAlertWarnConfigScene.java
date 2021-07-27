package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.prealertwarn;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 22.2. 备岗预警设置 （刘）
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class PreAlertWarnConfigScene extends BaseScene {
    /**
     * 描述 空闲销售阈值数量
     * 是否必填 true
     * 版本 v5.0
     */
    private final String freeSaleNum;

    /**
     * 描述 告警手机号
     * 是否必填 true
     * 版本 v5.0
     */
    private final String warnPhone;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("freeSaleNum", freeSaleNum);
        object.put("warnPhone", warnPhone);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/pre-alert-warn/config";
    }
}