package com.haisheng.framework.testng.bigScreen.itemXundian.scene.pc.file;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class FileUpload extends BaseScene {
    private final String pic;
    private final Boolean isPermanent;
    private final Double ratio;
    private final String ratioStr;
    private final Integer permanentPicType;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("permanent_pic_type", permanentPicType);
        object.put("pic", pic);
        object.put("isPermanent", isPermanent);
        object.put("ratio", ratio);
        object.put("ratio_str", ratioStr);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/file/upload";
    }
}
