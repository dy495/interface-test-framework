package com.haisheng.framework.testng.bigScreen.xundian.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 撤回卡券
 *
 * @author wangmin
 * @date 2021/1/20 16:46
 */
@Builder
public class RecallVoucherScene extends BaseScene {
    private final Long id;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/voucher-manage/recall-voucher";
    }

}
