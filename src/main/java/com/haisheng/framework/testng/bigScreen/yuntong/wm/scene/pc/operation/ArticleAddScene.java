package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.operation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 27.3. 内容运营 : 创建文章 （谢）（2020-12-22）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class ArticleAddScene extends BaseScene {
    /**
     * 描述 标题
     * 是否必填 true
     * 版本 v1.0
     */
    private final String title;

    /**
     * 描述 图片样式类型(大图 三途 左侧图)
     * 是否必填 true
     * 版本 v1.0
     */
    private final String picType;

    /**
     * 描述 富文本内容
     * 是否必填 true
     * 版本 v1.0
     */
    private final String content;

    /**
     * 描述 文章标签枚举
     * 是否必填 true
     * 版本 v1.0
     */
    private final String label;

    /**
     * 描述 文章图片OSS地址
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray picList;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("title", title);
        object.put("pic_type", picType);
        object.put("content", content);
        object.put("label", label);
        object.put("pic_list", picList);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/operation/article/add";
    }
}