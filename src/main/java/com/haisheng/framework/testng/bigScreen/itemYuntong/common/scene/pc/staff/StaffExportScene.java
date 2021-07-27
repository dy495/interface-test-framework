package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.staff;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 30.2. 员工导出 （谢） （2020-12-22）
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class StaffExportScene extends BaseScene {
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

    /**
     * 描述 导出类型 ALL：导出全部，CURRENT_PAGE：导出当前页，SPECIFIED_DATA：导出特定数据
     * 是否必填 true
     * 版本 v2.0
     */
    private final String exportType;

    /**
     * 描述 导出数据id列表，特定数据时必填
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray ids;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("name", name);
        object.put("phone", phone);
        object.put("shop_id", shopId);
        object.put("role_id", roleId);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/staff/export";
    }
}