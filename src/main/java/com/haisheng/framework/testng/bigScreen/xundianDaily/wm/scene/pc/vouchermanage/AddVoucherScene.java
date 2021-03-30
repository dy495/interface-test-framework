package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.22. 卡券增发
 *
 * @author wangmin
 * @date 2021-03-30 14:00:03
 */
@Builder
public class AddVoucherScene extends BaseScene {
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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
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