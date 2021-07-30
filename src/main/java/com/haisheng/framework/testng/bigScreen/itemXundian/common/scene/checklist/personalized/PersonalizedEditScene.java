package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.checklist.personalized;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 19.2. 个性化配置清单-编辑
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class PersonalizedEditScene extends BaseScene {
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
     * 描述 清单id列表
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray checkListIdList;

    /**
     * 描述 门店id
     * 是否必填 false
     * 版本 -
     */
    private final Long shopId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("check_list_id_list", checkListIdList);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/check-list/personalized/edit";
    }
}