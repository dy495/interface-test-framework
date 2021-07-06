package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.packagemanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 17.12. 套餐购买记录取消 （张小龙）（2020-12-25）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class CancelSoldPackageScene extends BaseScene {
    /**
     * 描述 套餐购买记录id
     * 是否必填 true
     * 版本 v2.0
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
        return "/yt/pc/package-manage/cancel-sold-package";
    }
}