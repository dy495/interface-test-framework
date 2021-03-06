package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 分享管理
 *
 * @author wangmin
 * @date 2021/2/1 17:02
 */
@Builder
public class ShareManagerPageScene extends BaseScene {

    @Builder.Default
    private final Integer size = 10;
    @Builder.Default
    private final Integer page = 1;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("page", page);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/vip-marketing/share-manager/page";
    }
}
