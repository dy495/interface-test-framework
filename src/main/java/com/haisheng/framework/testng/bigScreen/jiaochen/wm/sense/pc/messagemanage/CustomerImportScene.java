package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 消息管理，导入客户
 */
@Builder
public class CustomerImportScene extends BaseScene {
    private final String filePath;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("filePath", filePath);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/message-manage/customers/import";
    }
}
