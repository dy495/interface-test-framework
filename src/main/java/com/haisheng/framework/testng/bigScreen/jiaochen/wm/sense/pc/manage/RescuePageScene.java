package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 17.1. 道路救援记录分页 （谢）（2020-12-18）
 *
 * @author wangmin
 * @date 2021-03-31 12:29:35
 */
@Builder
public class RescuePageScene extends BaseScene {
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
     * 描述 客户名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String customerName;

    /**
     * 描述 客户vip类型 通过通用枚举接口获取，key为VIP_TYPE
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer vipType;

    /**
     * 描述 客户联系方式
     * 是否必填 false
     * 版本 v2.0
     */
    private final String customerPhone;

    /**
     * 描述 救援门店
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long shopId;

    /**
     * 描述 拨号时间范围查询开始日期
     * 是否必填 false
     * 版本 v2.0
     */
    private final String dialStart;

    /**
     * 描述 拨号时间范围查询结束日期
     * 是否必填 false
     * 版本 v2.0
     */
    private final String dialEnd;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("customer_name", customerName);
        object.put("vip_type", vipType);
        object.put("customer_phone", customerPhone);
        object.put("shop_id", shopId);
        object.put("dial_start", dialStart);
        object.put("dial_end", dialEnd);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/manage/rescue/page";
    }
}