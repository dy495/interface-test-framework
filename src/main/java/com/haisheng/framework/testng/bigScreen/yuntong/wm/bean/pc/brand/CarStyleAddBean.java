package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.brand;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 30.12. 新建品牌车系（谢）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
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