package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.7. 变更销售接待（谢）v3.0 （2021-03-16）的接口
 *
 * @author wangmin
 * @date 2021-03-24 14:44:47
 */
@Builder
public class AppReceptorChangeScene extends BaseScene {
    /**
     * 描述 接待id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long shopId;

    /**
     * 描述 售后接待员工id
     * 是否必填 true
     * 版本 v2.0
     */
    private final String receptorId;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shop_id", shopId);
        object.put("receptor_id", receptorId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/pre-sales-reception/receptor/change";
    }
}