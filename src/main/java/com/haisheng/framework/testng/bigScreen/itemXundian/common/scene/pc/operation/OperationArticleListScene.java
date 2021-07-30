package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.operation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.9. 内容运营 : 活动下拉, 全部信息流（2021-01-26）
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class OperationArticleListScene extends BaseScene {
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


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/operation/article-list";
    }
}