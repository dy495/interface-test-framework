package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/applet/granted/user-info/edit的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:53:03
 */
@Builder
public class AppletUserInfoEditScene extends BaseScene {
    /**
     * 描述 姓名
     * 是否必填 false
     * 版本 v1.0
     */
    private final String name;

    /**
     * 描述 联系方式
     * 是否必填 false
     * 版本 v1.0
     */
    private final String contact;

    /**
     * 描述 性别 MALE：男，FEMALE：女
     * 是否必填 false
     * 版本 v1.0
     */
    private final String gender;

    /**
     * 描述 生日
     * 是否必填 false
     * 版本 v1.0
     */
    private final String birthday;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("contact", contact);
        object.put("gender", gender);
        object.put("birthday", birthday);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/user-info/edit";
    }
}