package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.district;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 32.3. 区划树
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class TreeBean implements Serializable {
    /**
     * 描述 叶子节点值（key）
     * 版本 v1.0
     */
    @JSONField(name = "value")
    private JSONObject value;

    /**
     * 描述 叶子节点展示标签名
     * 版本 v1.0
     */
    @JSONField(name = "label")
    private String label;

    /**
     * 描述 叶子节点详细描述
     * 版本 v1.0
     */
    @JSONField(name = "desc")
    private String desc;

    /**
     * 描述 叶子节点
     * 版本 -
     */
    @JSONField(name = "children")
    private JSONArray children;

}