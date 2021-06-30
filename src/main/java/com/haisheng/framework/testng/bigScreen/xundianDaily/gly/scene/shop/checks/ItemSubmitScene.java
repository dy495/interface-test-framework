package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.shop.checks;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 36.13. 提交巡检项目结果
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class ItemSubmitScene extends BaseScene {
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
     * 描述 巡检项目id
     * 是否必填 true
     * 版本 -
     */
    private final Long itemId;

    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 -
     */
    private final Long shopId;

    /**
     * 描述 巡检记录id
     * 是否必填 true
     * 版本 -
     */
    private final Long patrolId;

    /**
     * 描述 巡检清单id
     * 是否必填 true
     * 版本 -
     */
    private final Long listId;

    /**
     * 描述 巡检项目结果 1合格；2不合格；3不适用
     * 是否必填 false
     * 版本 -
     */
    private final Integer checkResult;

    /**
     * 描述 审核意见
     * 是否必填 false
     * 版本 -
     */
    private final String auditComment;

    /**
     * 描述 留痕照片list
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray picList;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("item_id", itemId);
        object.put("shop_id", shopId);
        object.put("patrol_id", patrolId);
        object.put("list_id", listId);
        object.put("check_result", checkResult);
        object.put("audit_comment", auditComment);
        object.put("pic_list", picList);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/shop/checks/item/submit";
    }
}