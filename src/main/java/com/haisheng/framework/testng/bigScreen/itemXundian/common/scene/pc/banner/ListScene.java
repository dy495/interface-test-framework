package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.banner;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.2. banner列表（id=广告位id）
 *
 * @author wangmin
 * @date 2021-03-30 14:00:02
 */
@Builder
public class ListScene extends BaseScene {
    /**
     * 描述 id
     * 是否必填 true
     * 版本 -
     */
    private final Long id;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/pc/banner/list";
    }
}