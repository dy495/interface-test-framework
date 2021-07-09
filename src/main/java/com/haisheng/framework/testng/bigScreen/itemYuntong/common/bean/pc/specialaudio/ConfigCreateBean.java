package com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.pc.specialaudio;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 5.5. 创建条件配置（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class ConfigCreateBean implements Serializable {
    /**
     * 描述 id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

}