package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.brand;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/brand/all的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class AllScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final String name;

    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final String logoPath;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String status;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String firstLetter;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Boolean isDeleted;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray brandIds;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);
        object.put("logoPath", logoPath);
        object.put("status", status);
        object.put("first_letter", firstLetter);
        object.put("isDeleted", isDeleted);
        object.put("brandIds", brandIds);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/brand/all";
    }
}