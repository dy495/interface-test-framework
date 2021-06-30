package com.haisheng.framework.testng.bigScreen.itemXundian.scene.newmember.all;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.3. 门店新增客户-门店列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class ListScene extends BaseScene {
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
     * 描述 门店类型
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray shopType;

    /**
     * 描述 门店名称
     * 是否必填 false
     * 版本 -
     */
    private final String shopName;

    /**
     * 描述 门店负责人
     * 是否必填 false
     * 版本 -
     */
    private final String shopManager;

    /**
     * 描述 区域编码
     * 是否必填 false
     * 版本 -
     */
    private final String districtCode;

    /**
     * 描述 会员类型，和member_type_order共同生效。默认是降序
     * 是否必填 false
     * 版本 -
     */
    private final String memberType;

    /**
     * 描述 会员类型排序，必须和member_type同时配置才会生效。0： 降序 1：升序 。默认降序
     * 是否必填 false
     * 版本 -
     */
    private final Integer memberTypeOrder;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("shop_type", shopType);
        object.put("shop_name", shopName);
        object.put("shop_manager", shopManager);
        object.put("district_code", districtCode);
        object.put("member_type", memberType);
        object.put("member_type_order", memberTypeOrder);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/new_member/all/list";
    }
}