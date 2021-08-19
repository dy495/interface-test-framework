package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.saleschedule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 14.8. 销售顾问加入组 (刘) v5.0组id+销售id
 *
 * @author wangmin
 * @date 2021-08-06 16:38:24
 */
@Builder
public class AppSaleScheduleJoinGroupScene extends BaseScene {
    /**
     * 描述 PRE 销售 AFTER 售后
     * 是否必填 true
     * 版本 5.0
     */
    private final String type;

    /**
     * 描述 ADD_GROUP ADD_SALE DEL_GROUP DEL_SALE
     * 是否必填 true
     * 版本 5.0
     */
    private final String openType;

    /**
     * 描述 组id
     * 是否必填 false
     * 版本 v5.0
     */
    private final Long groupId;

    /**
     * 描述 组内成员
     * 是否必填 false
     * 版本 v5.0
     */
    private final JSONArray salesInfoList;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("open_type", openType);
        object.put("group_id", groupId);
        object.put("sales_info_list", salesInfoList);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/sale-schedule/join-group";
    }
}