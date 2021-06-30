package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.staff;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 25.8. 员工详情 （杨）（2021-03-23） v3.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class DetailScene extends BaseScene {
    /**
     * 描述 账号id
     * 是否必填 true
     * 版本 v2.0
     */
    private final String id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/staff/detail";
    }
}