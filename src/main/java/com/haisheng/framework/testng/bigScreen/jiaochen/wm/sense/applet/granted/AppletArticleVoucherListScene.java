package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 16.6. 小程序-文章详情-文章活动卡券列表 （谢）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletArticleVoucherListScene extends BaseScene {
    /**
     * 描述 活动ID
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/article/voucher/list";
    }
}