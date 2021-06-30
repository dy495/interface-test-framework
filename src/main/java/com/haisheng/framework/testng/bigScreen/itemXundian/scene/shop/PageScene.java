package com.haisheng.framework.testng.bigScreen.itemXundian.scene.shop;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 36.1. 获取门店列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class PageScene extends BaseScene {
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
     * 描述 按照排序的类型 PATROL_NUMBER:巡店次数 PATROL_PASS_RATE:合格率
     * 是否必填 false
     * 版本 -
     */
    private final String sortType;

    /**
     * 描述 0:降序 1:升序 默认按照巡店次数倒序
     * 是否必填 false
     * 版本 -
     */
    private final Integer sortTypeOrder;

    /**
     * 描述 门店名称
     * 是否必填 false
     * 版本 -
     */
    private final String name;

    /**
     * 描述 code编码
     * 是否必填 false
     * 版本 -
     */
    private final String districtCode;

    /**
     * 描述 巡店者姓名
     * 是否必填 false
     * 版本 -
     */
    private final String inspectorName;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("sort_type", sortType);
        object.put("sort_type_order", sortTypeOrder);
        object.put("name", name);
        object.put("district_code", districtCode);
        object.put("inspector_name", inspectorName);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/shop/page";
    }
}