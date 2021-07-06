package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 29.26. 活动变更记录分页 （谢）（2020-12-23）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class ManageChangeRecordPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 活动id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/activity/manage/change/record/page";
    }
}