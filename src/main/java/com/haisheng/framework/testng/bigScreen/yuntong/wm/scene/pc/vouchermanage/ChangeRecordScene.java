package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.7. 变更记录 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class ChangeRecordScene extends BaseScene {
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
     * 描述 卡券id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long voucherId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("voucher_id", voucherId);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/voucher-manage/change-record";
    }
}