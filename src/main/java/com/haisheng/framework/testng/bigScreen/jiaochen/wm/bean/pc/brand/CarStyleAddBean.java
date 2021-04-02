package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.brand;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 28.12. 新建品牌车系（谢）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class CarStyleAddBean implements Serializable {
    /**
     * 描述 id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

}