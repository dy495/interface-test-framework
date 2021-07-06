package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.store;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.16. 创建分销员 v2.0(池)
 *
 * @author wangmin
 * @date 2021-03-31 12:47:26
 */
@Builder
public class SalesCreateScene extends BaseScene {
    /**
     * 描述 分销员手机号
     * 是否必填 true
     * 版本 v2.0
     */
    private final String salesPhone;

    /**
     * 描述 姓名
     * 是否必填 false
     * 版本 v2.0
     */
    private final String salesName;

    /**
     * 描述 门店
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long shopId;

    /**
     * 描述 门店名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String shopName;

    /**
     * 描述 部门
     * 是否必填 false
     * 版本 v2.0
     */
    private final String deptName;

    /**
     * 描述 岗位
     * 是否必填 false
     * 版本 v2.0
     */
    private final String jobName;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("sales_phone", salesPhone);
        object.put("sales_name", salesName);
        object.put("shop_id", shopId);
        object.put("shop_name", shopName);
        object.put("dept_name", deptName);
        object.put("job_name", jobName);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/store/sales/create";
    }
}