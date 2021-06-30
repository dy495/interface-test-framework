package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.loginlog;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 30.1. 员工登录日志-分页 （华成裕）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
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
    protected JSONObject getRequestBody() {
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
        return "/yt/pc/login-log/staff";
    }
}