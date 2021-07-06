package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.packagemanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 17.5. 套餐取消 v2.3（张小龙） 2021-03-17
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class CancelPackageScene extends BaseScene {
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
        return "/yt/pc/package-manage/cancel-package";
    }
}