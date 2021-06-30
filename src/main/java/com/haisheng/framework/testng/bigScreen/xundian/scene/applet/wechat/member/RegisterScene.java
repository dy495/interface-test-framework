package com.haisheng.framework.testng.bigScreen.xundian.scene.applet.wechat.member;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.1. 完善个人信息 lj
 *
 * @author wangmin
 * @date 2021-03-30 15:23:58
 */
@Builder
public class RegisterScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 姓名
     * 是否必填 false
     * 版本 -
     */
    private final String name;

    /**
     * 描述 身份证号
     * 是否必填 false
     * 版本 -
     */
    private final String cardNumber;

    /**
     * 描述 联系方式
     * 是否必填 false
     * 版本 -
     */
    private final String phone;

    /**
     * 描述 性别
     * 是否必填 false
     * 版本 -
     */
    private final String gender;

    /**
     * 描述 生日
     * 是否必填 false
     * 版本 -
     */
    private final String birthday;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("name", name);
        object.put("card_number", cardNumber);
        object.put("phone", phone);
        object.put("gender", gender);
        object.put("birthday", birthday);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/wechat/member/register";
    }
}