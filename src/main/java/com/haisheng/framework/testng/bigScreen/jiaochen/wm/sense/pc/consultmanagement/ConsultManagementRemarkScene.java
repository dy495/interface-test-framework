package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.consultmanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.10. 备注（通用）（池）(2021-03-12)
 *
 * @author wangmin
 * @date 2021-08-30 14:26:54
 */
@Builder
public class ConsultManagementRemarkScene extends BaseScene {
    /**
     * 描述 唯一id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long id;

    /**
     * 描述 备注内容
     * 是否必填 true
     * 版本 v3.0
     */
    private final String remarkContent;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("remark_content", remarkContent);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/consult-management/remark";
    }
}