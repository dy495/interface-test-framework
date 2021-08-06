package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.testdrive;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 15.8. 上传协议图（刘）v5.0
 *
 * @author wangmin
 * @date 2021-08-06 16:38:24
 */
@Builder
public class AppTestDriveUploadProtocolPngScene extends BaseScene {
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

    /**
     * 描述 文件大小 KB 当type为0生效
     * 是否必填 false
     * 版本 v3.0
     */
    private final Integer maxSize;

    /**
     * 描述 接待id
     * 是否必填 true
     * 版本 v5.0
     */
    private final Long receptionId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("pic", pic);
        object.put("permanent_pic_type", permanentPicType);
        object.put("ratio", ratio);
        object.put("ratio_str", ratioStr);
        object.put("max_size", maxSize);
        object.put("reception_id", receptionId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/test-drive/upload-protocol-png";
    }
}