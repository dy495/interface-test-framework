package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/message-manage/customers/import的接口
 *
 * @author wangmin
 * @date 2021-03-15 10:12:39
 */
@Builder
public class CustomersImportScene extends BaseScene {
    /**
     * 描述 
     * 是否必填 true
     * 版本 -
     */
    private final String file;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("file", file);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/message-manage/customers/import";
    }
}