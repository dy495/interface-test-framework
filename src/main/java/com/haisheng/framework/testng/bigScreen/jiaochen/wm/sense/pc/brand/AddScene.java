package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.brand;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/brand/add的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class AddScene extends BaseScene {
    /**
     * 描述 品牌名称
     * 是否必填 true
     * 版本 v1.0
     */
    private final String name;

    /**
     * 描述 品牌logo oss路径
     * 是否必填 true
     * 版本 v1.0
     */
    private final String logoPath;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("logoPath", logoPath);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/brand/add";
    }
}