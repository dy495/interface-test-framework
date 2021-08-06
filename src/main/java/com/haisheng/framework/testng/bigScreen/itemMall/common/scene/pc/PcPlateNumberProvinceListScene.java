package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.4. 省份车牌首字母接口
 *
 * @author wangmin
 * @date 2021-08-06 17:47:04
 */
@Builder
public class PcPlateNumberProvinceListScene extends BaseScene {
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
        return "/shop/pc/plate-number-province-list";
    }
}