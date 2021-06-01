package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.district;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 11.3. 获取区划树
 *
 * @author wangmin
 * @date 2021-06-01 18:39:24
 */
@Data
public class AppTreetreeBean implements Serializable {
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