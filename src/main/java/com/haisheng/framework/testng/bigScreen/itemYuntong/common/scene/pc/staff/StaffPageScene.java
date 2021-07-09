package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.staff;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 25.1. 员工分页 （杨）（2021-03-23）v3.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class StaffPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    @Builder.Default
    private  Integer page=1;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    @Builder.Default
    private final Integer size=10;

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
    protected JSONObject getRequestBody() {
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
        return "/account-platform/auth/staff/page";
    }


    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public void setSize(Integer size) {
        super.setSize(size);
    }
}