package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.banner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 25.1. 修改banner详情
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class EditScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray list;

    /**
     * 描述 内容,富文本
     * 是否必填 false
     * 版本 v3.0
     */
    private final String content;

    /**
     * 描述 banner类型 HOME_PAGE 首页 ONLINE_EXPERTS 在线专家 RENEW_CONSULT 续保咨询 USED_CAR 二手车 USED_CAR_ASSESS 二手车评估
     * 是否必填 true
     * 版本 -
     */
    private final String bannerType;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("list", list);
        object.put("content", content);
        object.put("banner_type", bannerType);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/banner/edit";
    }
}