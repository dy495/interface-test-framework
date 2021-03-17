package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.role;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/role/delete的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class DeleteScene extends BaseScene {
    /**
     * 描述 角色id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer id;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/role/delete";
    }
}