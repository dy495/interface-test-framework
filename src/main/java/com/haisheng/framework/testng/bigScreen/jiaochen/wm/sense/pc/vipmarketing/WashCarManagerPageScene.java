package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 洗车管理列表
 *
 * @author wangmin
 * @date 2021/2/1 17:02
 */
@Builder
public class WashCarManagerPageScene extends BaseScene {
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    /**
     * 客户姓名
     */
    private final Integer customerName;

    /**
     * 电话
     */
    private final String phone;

    /**
     * 客户类型
     */
    private final Integer customerType;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_name", customerName);
        object.put("phone", phone);
        object.put("customer_type", customerType);
        object.put("size", size);
        object.put("page", page);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/vip-marketing/wash-car-manager/page";
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }
}
