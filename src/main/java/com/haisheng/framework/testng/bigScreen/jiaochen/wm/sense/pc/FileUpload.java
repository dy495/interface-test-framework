package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

@Builder
public class FileUpload extends BaseScene {
    private final String pic;
    private final Boolean isPermanent;
    private final Double ratio;
    private final String ratioStr;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("pic", pic);
        object.put("isPermanent", isPermanent);
        object.put("ratio", ratio);
        object.put("ratio_str", ratioStr);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/file/upload";
    }
}
