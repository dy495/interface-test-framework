package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.Import;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 文件上传
 *
 * @author wangmin
 * @date 2021/1/29 10:28
 */
@Builder
public class WorkOrderScene extends BaseScene {
    private final String filePath;

    @Override
    public JSONObject getRequest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("filePath", filePath);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/import/work_order";
    }
}
