package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.shop;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 25.4. 添加门店 （杨）（2021-03-23）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class AddBean implements Serializable {
    /**
     * 描述 id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

}