package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 37.27. 客户积分变更记录分页
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class ChangeRecordPageScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 当前页
     * 是否必填 true
     * 版本 -
     */
    private final Integer page;

    /**
     * 描述 当前页的数量
     * 是否必填 true
     * 版本 -
     */
    private final Integer size;

    /**
     * 描述 变更时间范围查询开始日期
     * 是否必填 false
     * 版本 v2.2
     */
    private final String changeStart;

    /**
     * 描述 变更时间范围查询结束日期
     * 是否必填 false
     * 版本 v2.2
     */
    private final String changeEnd;

    /**
     * 描述 客户联系方式
     * 是否必填 false
     * 版本 v2.2
     */
    private final String customerPhone;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("change_start", changeStart);
        object.put("change_end", changeEnd);
        object.put("customer_phone", customerPhone);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-center/customer-integral/change-record/page";
    }
}