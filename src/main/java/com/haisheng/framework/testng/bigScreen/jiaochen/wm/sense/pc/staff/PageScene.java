package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/staff/page的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class PageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 账号名称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String name;

    /**
     * 描述 联系电话
     * 是否必填 false
     * 版本 v2.0
     */
    private final String phone;

    /**
     * 描述 归属门店id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long shopId;

    /**
     * 描述 角色id （通过角色查询账号分页时使用）
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer roleId;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("name", name);
        object.put("phone", phone);
        object.put("shop_id", shopId);
        object.put("role_id", roleId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/staff/page";
    }
}