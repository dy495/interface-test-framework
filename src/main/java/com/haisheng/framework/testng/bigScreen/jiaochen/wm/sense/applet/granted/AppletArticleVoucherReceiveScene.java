package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/applet/granted/article/voucher/receive的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:53:04
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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("article_id", articleId);
        object.put("voucher_id", voucherId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/article/voucher/receive";
    }
}