package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 14.5. 套餐取消 v2.3（张小龙） 2021-03-17的接口
 *
 * @author wangmin
 * @date 2021-03-24 14:32:27
 */
@Builder
public class PackageManageScene extends BaseScene {
    /**
     * 描述 唯一id
     * 是否必填 false
     * 版本 -
     */
    private final Long id;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/package-manage/";
    }
}