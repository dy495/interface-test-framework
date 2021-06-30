package com.haisheng.framework.testng.bigScreen.itemXundian.scene.shop.checks;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 36.12. 开始或继续巡店
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class StartScene extends BaseScene {
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
     * 描述 门店id
     * 是否必填 true
     * 版本 -
     */
    private final Long shopId;

    /**
     * 描述 巡店方式（SCHEDULED：定检巡店；SPOT：自主巡店；REMOTE：远程巡店）
     * 是否必填 true
     * 版本 -
     */
    private final String checkType;

    /**
     * 描述 是否重置检查清单 0 否 1是
     * 是否必填 false
     * 版本 -
     */
    private final Integer reset;

    /**
     * 描述 任务id，定检巡店巡店时上传定检任务id
     * 是否必填 false
     * 版本 -
     */
    private final Long taskId;

    /**
     * 描述 是否 刚配置完成个性化配置清单（0 否 1 是）
     * 是否必填 false
     * 版本 -
     */
    private final Integer isPersonalizedCheckList;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("shop_id", shopId);
        object.put("check_type", checkType);
        object.put("reset", reset);
        object.put("task_id", taskId);
        object.put("is_personalized_check_list", isPersonalizedCheckList);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/shop/checks/start";
    }
}