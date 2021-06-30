package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 24.15. 开始/结束发放 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class ChangeProvideStatusScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

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
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("is_start", isStart);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/voucher-manage/change-provide-status";
    }
}