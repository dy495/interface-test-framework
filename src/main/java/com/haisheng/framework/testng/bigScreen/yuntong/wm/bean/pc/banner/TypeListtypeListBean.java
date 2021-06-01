package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.banner;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 31.3. 返回banner类型列表 （池）（2021-03-11）
 *
 * @author wangmin
 * @date 2021-06-01 18:39:23
 */
@Data
public class TypeListtypeListBean implements Serializable {
    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "list")
    private JSONArray list;

}