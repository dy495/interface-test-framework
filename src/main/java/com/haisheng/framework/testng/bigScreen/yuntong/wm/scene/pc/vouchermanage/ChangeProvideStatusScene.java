package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.14. 开始/结束发放 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class ChangeProvideStatusScene extends BaseScene {
    /**
     * 描述 卡券id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 是否开始发放
     * 是否必填 true
     * 版本 v2.0
     */
    private final Boolean isStart;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("is_start", isStart);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/voucher-manage/change-provide-status";
    }
}