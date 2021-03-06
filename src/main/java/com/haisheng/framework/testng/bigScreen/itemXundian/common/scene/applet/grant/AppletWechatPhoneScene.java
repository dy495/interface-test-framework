package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.applet.grant;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.4. 授权微信绑定手机号（池辉）（2021-01-21）car_platform_path: /jiaochen/applet/grant/wechat-phone
 *
 * @author wangmin
 * @date 2021-03-30 15:23:58
 */
@Builder
public class AppletWechatPhoneScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 授权的加密数据
     * 是否必填 true
     * 版本 v1.0
     */
    private final String encryptedData;

    /**
     * 描述 加密向量
     * 是否必填 true
     * 版本 v1.0
     */
    private final String iv;

    /**
     * 描述 信息id businessType 为 04,06 时传入
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long informationId;

    /**
     * 描述 小程序活动分享的分享者id
     * 是否必填 false
     * 版本 v2.0
     */
    private final String shareCustomerId;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("encrypted_data", encryptedData);
        object.put("iv", iv);
        object.put("information_id", informationId);
        object.put("share_customer_id", shareCustomerId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/grant/wechat-phone";
    }
}