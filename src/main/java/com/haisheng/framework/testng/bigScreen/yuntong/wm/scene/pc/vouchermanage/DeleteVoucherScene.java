package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.12. 删除卡券 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class DeleteVoucherScene extends BaseScene {
    /**
     * 描述 卡券id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/voucher-manage/delete-voucher";
    }
}