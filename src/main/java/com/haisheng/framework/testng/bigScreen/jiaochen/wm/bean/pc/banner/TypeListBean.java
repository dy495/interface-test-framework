package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.banner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 29.3. 返回banner类型列表 （池）（2021-03-11）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class TypeListBean implements Serializable {
    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "list")
    private JSONArray list;

}