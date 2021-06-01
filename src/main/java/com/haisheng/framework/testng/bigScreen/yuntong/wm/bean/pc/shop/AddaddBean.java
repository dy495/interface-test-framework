package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.shop;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 27.4. 添加门店 （杨）（2021-03-23）
 *
 * @author wangmin
 * @date 2021-06-01 18:39:23
 */
@Data
public class AddaddBean implements Serializable {
    /**
     * 描述 id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

}