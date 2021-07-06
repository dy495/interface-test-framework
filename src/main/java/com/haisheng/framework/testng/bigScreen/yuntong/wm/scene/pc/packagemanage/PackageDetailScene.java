package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.packagemanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 17.6. 套餐详情 v1.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class PackageDetailScene extends BaseScene {
    /**
     * 描述 唯一id
     * 是否必填 false
     * 版本 -
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/package-manage/package-detail";
    }
}