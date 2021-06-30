package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.staff;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 25.5. 员工编辑 （杨）（2021-03-23） v3.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class EditScene extends BaseScene {
    /**
     * 描述 姓名
     * 是否必填 true
     * 版本 v1.0
     */
    private final String name;

    /**
     * 描述 手机号
     * 是否必填 true
     * 版本 v1.0
     */
    private final String phone;

    /**
     * 描述 性别
     * 是否必填 false
     * 版本 -
     */
    private final String gender;

    /**
     * 描述 角色
     * 是否必填 true
     * 版本 v2.0
     */
    private final JSONArray roleList;

    /**
     * 描述 员工图片oss路径
     * 是否必填 false
     * 版本 v3.0
     */
    private final String picturePath;

    /**
     * 描述 账号id
     * 是否必填 true
     * 版本 v1.0
     */
    private final String id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("phone", phone);
        object.put("gender", gender);
        object.put("role_list", roleList);
        object.put("picture_path", picturePath);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/staff/edit";
    }
}