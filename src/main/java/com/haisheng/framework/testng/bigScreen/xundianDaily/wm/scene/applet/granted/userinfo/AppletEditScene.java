package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.granted.userinfo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.2. 编辑个人信息详情（谢）car_platform_path: /jiaochen/applet/granted/user-info/edit
 *
 * @author wangmin
 * @date 2021-03-30 15:23:58
 */
@Builder
public class AppletEditScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

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
        object.put("referer", referer);
        object.put("name", name);
        object.put("contact", contact);
        object.put("gender", gender);
        object.put("birthday", birthday);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/user-info/edit";
    }
}