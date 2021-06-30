package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.shop.remark;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 36.23. 图片中心列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class PicturePageScene extends BaseScene {
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
     * 描述 留痕方式
     * 是否必填 false
     * 版本 -
     */
    private final String patrolType;

    /**
     * 描述 开始时间
     * 是否必填 false
     * 版本 -
     */
    private final String startTime;

    /**
     * 描述 结束时间
     * 是否必填 false
     * 版本 -
     */
    private final String endTime;

    /**
     * 描述 门店名称
     * 是否必填 false
     * 版本 -
     */
    private final String shopName;

    /**
     * 描述 是否异常 1异常 0非异常
     * 是否必填 false
     * 版本 -
     */
    private final Integer isAbnormal;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("patrol_type", patrolType);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("shop_name", shopName);
        object.put("is_abnormal", isAbnormal);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/shop/remark/picture/page";
    }
}