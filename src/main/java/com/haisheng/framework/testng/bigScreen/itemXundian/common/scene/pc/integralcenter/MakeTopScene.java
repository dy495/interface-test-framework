package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * /shop/pc/integral-center/make-top的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class MakeTopScene extends BaseScene {
    /**
     * 描述 唯一id
     * 是否必填 true
     * 版本 v2.0
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
        return "/shop/pc/integral-center/make-top";
    }
}