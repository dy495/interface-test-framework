package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/staff/add的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class AddScene extends BaseScene {
    /**
     * 描述 姓名
     * 是否必填 true
     * 版本 v1.0
     */
    private final String name;

    /**
     * 描述 手机号
     * 是否必填 true
     * 版本 v1.0
     */
    private final String phone;

    /**
     * 描述 性别
     * 是否必填 false
     * 版本 -
     */
    private final String gender;

    /**
     * 描述 角色
     * 是否必填 true
     * 版本 v2.0
     */
    private final JSONArray roleList;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("phone", phone);
        object.put("gender", gender);
        object.put("role_list", roleList);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/staff/add";
    }
}