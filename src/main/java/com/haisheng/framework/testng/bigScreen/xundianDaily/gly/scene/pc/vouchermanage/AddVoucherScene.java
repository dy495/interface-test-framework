package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 24.23. 卡券增发
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class AddVoucherScene extends BaseScene {
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
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray shopIds;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Integer addNumber;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("shop_ids", shopIds);
        object.put("add_number", addNumber);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/voucher-manage/add-voucher";
    }
}