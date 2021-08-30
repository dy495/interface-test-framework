package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.prealertwarn;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 19.2. 备岗预警设置 （刘）
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class PreAlertWarnConfigScene extends BaseScene {
    /**
     * 描述 空闲销售阈值数量
     * 是否必填 true
     * 版本 v5.0
     */
    private final Integer freeSaleNum;

    /**
     * 描述 告警手机号
     * 是否必填 true
     * 版本 v5.0
     */
    private final String warnPhone;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("free_sale_num", freeSaleNum);
        object.put("warn_phone", warnPhone);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/pre-alert-warn/config";
    }
}