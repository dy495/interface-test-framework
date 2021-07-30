package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.district;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.3. 区划树
 *
 * @author wangmin
 * @date 2021-07-28 16:58:57
 */
@Builder
public class DistrictTreeScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/district/tree";
    }
}