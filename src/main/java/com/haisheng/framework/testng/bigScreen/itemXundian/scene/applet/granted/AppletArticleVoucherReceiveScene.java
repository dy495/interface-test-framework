package com.haisheng.framework.testng.bigScreen.itemXundian.scene.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 16.2. 小程序-文章详情-卡券领取 （谢）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletArticleVoucherReceiveScene extends BaseScene {
    /**
     * 描述 文章id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long articleId;

    /**
     * 描述 卡券id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long voucherId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("article_id", articleId);
        object.put("voucher_id", voucherId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/article/voucher/receive";
    }
}