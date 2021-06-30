package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.banner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 32.3. 返回banner类型列表 （池）（2021-03-11）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class TypeListScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray list;

    /**
     * 描述 banner类型 HOME_PAGE 首页 ONLINE_EXPER在线专家 RENEW_CONSULT 续保咨询 USED_CAR 二手车 USED_CAR_ASSESS 二手车评估
     * 是否必填 true
     * 版本 -
     */
    private final String bannerType;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("list", list);
        object.put("banner_type", bannerType);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/banner/type-list";
    }
}