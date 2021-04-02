package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.operation;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 24.3. 内容运营 : 创建文章 （谢）（2020-12-22）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class ArticleAddBean implements Serializable {
    /**
     * 描述 id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

}