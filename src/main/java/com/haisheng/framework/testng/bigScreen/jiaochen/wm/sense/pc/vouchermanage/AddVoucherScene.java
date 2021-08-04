package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 卡券增发接口
 *
 * @author wangmin
 * @date 2020-12-29
 */
@Builder
public class AddVoucherScene extends BaseScene {
    private final Long id;
    private final Integer addNumber;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("add_number", addNumber);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/voucher-manage/add-voucher";
    }
}
