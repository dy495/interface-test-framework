package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.brand;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.7. 品牌列表
 *
 * @author wangmin
 * @date 2021-07-28 16:58:57
 */
@Builder
public class BrandAllScene extends BaseScene {
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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
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
        return "/shop/pc/brand/all";
    }
}