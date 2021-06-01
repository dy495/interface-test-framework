package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 8.2. 标准话术（池）v4.0 （2021-05-08 更新）
 *
 * @author wangmin
 * @date 2021-06-01 18:39:24
 */
@Data
public class AppStandardVerbalTrickstandardVerbalTrickBean implements Serializable {
    /**
     * 描述 类型
     * 版本 v4.0
     */
    @JSONField(name = "type")
    private String type;

    /**
     * 描述 内容
     * 版本 v4.0
     */
    @JSONField(name = "content")
    private String content;

}