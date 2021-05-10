package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.sensitivewords;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.7. 编辑敏感词（谢）
 *
 * @author wangmin
 * @date 2021-05-08 20:23:16
 */
@Builder
public class EditScene extends BaseScene {
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

    /**
     * 描述 敏感词id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("words", words);
        object.put("sensitive_words_type", sensitiveWordsType);
        object.put("brand_ids", brandIds);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/sensitive-words/edit";
    }
}