package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.packagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 21.6. 套餐详情 v1.0
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class PackageManagePackageDetailScene extends BaseScene {
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
        return "/car-platform/pc/package-manage/package-detail";
    }
}