package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginlog;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 28.1. 员工登录日志-分页 （华成裕）
 *
 * @author wangmin
 * @date 2021-03-31 12:29:35
 */
@Builder
public class StaffScene extends BaseScene {
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
     * 描述 名称
     * 是否必填 false
     * 版本 v5.3
     */
    private final String name;

    /**
     * 描述 联系电话(账号)
     * 是否必填 false
     * 版本 v5.3
     */
    private final String phone;

    /**
     * 描述 开始日期
     * 是否必填 false
     * 版本 v5.3
     */
    private final String startDate;

    /**
     * 描述 结束日期
     * 是否必填 false
     * 版本 v5.3
     */
    private final String endDate;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("name", name);
        object.put("phone", phone);
        object.put("start_date", startDate);
        object.put("end_date", endDate);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/login-log/staff";
    }
}