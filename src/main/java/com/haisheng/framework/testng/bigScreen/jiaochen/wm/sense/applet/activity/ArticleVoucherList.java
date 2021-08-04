package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-活动管理列表-更多--gly
 */
@Builder
public class ArticleVoucherList extends BaseScene {
    private final Long id;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/applet/granted/article/voucher/list";
    }


}