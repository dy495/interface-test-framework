package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 56.25. 积分客户管理
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class CustomerPageScene extends BaseScene {
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
     * 描述 客户联系方式
     * 是否必填 false
     * 版本 v2.2
     */
    private final String customerPhone;

    /**
     * 描述 注册日期
     * 是否必填 false
     * 版本 -
     */
    private final String registerDate;

    /**
     * 描述 会员名称
     * 是否必填 false
     * 版本 -
     */
    private final String memberName;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("customer_phone", customerPhone);
        object.put("register_date", registerDate);
        object.put("member_name", memberName);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-center/customer/page";
    }
}