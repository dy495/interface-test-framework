package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-活动管理列表-更多--gly
 */
@Builder
public class ArticleVoucherReceiveScene extends BaseScene {
    private final Long voucherId;
    private final Long articleId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("voucher_id", voucherId);
        object.put("article_id", articleId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/article/voucher/receive";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}