package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.file;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 21.5. 通用文件上传接口
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class UploadScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("pic", pic);
        object.put("permanent_pic_type", permanentPicType);
        object.put("ratio", ratio);
        object.put("ratio_str", ratioStr);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/file/upload";
    }
}