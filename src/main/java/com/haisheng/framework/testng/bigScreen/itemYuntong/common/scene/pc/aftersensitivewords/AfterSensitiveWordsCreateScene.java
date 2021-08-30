package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.aftersensitivewords;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.6. 创建敏感词（华成裕）
 *
 * @author wangmin
 * @date 2021-08-30 14:39:23
 */
@Builder
public class AfterSensitiveWordsCreateScene extends BaseScene {
    /**
     * 描述 敏感词
     * 是否必填 true
     * 版本 v1.0
     */
    private final String words;

    /**
     * 描述 敏感词类别 使用"获取指定枚举值列表"接口获取 enum_type为 SENSITIVE_WORDS_TYPES
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer sensitiveWordsType;

    /**
     * 描述 适用品牌id列表 通过获取品牌列表接口获取
     * 是否必填 true
     * 版本 v1.0
     */
    private final JSONArray brandIds;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("words", words);
        object.put("sensitive_words_type", sensitiveWordsType);
        object.put("brand_ids", brandIds);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/after-sensitive-words/create";
    }
}