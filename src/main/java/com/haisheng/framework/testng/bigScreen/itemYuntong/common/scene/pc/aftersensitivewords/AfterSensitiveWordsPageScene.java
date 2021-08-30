package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.aftersensitivewords;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.5. 敏感词分页（华成裕）
 *
 * @author wangmin
 * @date 2021-08-30 14:39:23
 */
@Builder
public class AfterSensitiveWordsPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 敏感词类别 使用"获取指定枚举值列表"接口获取 enum_type为 SENSITIVE_WORDS_TYPES
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer sensitiveWordsType;

    /**
     * 描述 创建时间范围查询开始日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String createStart;

    /**
     * 描述 创建时间范围查询结束日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String createEnd;

    /**
     * 描述 品牌id 通过获取品牌列表接口获取
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long brandId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("sensitive_words_type", sensitiveWordsType);
        object.put("create_start", createStart);
        object.put("create_end", createEnd);
        object.put("brandId", brandId);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/after-sensitive-words/page";
    }
}