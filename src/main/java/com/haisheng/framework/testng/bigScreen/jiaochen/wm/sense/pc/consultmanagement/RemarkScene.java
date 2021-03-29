package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.consultmanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.8. 备注（通用）（池）(2021-03-12)的接口
 *
 * @author wangmin
 * @date 2021-03-24 14:32:26
 */
@Builder
public class RemarkScene extends BaseScene {
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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("remark-content", remarkContent);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/consult-management/remark";
    }
}