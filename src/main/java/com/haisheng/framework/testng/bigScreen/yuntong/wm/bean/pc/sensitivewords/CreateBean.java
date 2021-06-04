package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.sensitivewords;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 6.6. 创建敏感词（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class CreateBean implements Serializable {
    /**
     * 描述 id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

}