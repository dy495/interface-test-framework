package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.file;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.5. 图片上传
 *
 * @author wangmin
 * @date 2021-03-31 13:03:23
 */
@Builder
public class AppUploadScene extends BaseScene {
    /**
     * 描述 图片base64
     * 是否必填 true
     * 版本 v1.0
     */
    private final String pic;

    /**
     * 描述 图片应用类型 取值 字典表《永久图片使用路径类型》
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer permanentPicType;

    /**
     * 描述 校验长宽比 不传则不校验
     * 是否必填 false
     * 版本 v1.0
     */
    private final Double ratio;

    /**
     * 描述 长宽比说明
     * 是否必填 false
     * 版本 v1.0
     */
    private final String ratioStr;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("pic", pic);
        object.put("permanent_pic_type", permanentPicType);
        object.put("ratio", ratio);
        object.put("ratio_str", ratioStr);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/file/upload";
    }
}