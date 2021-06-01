package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 21.8. 销售接待员工列表（谢）v3.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class ReceptorListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 员工id
     * 版本 v2.0
     */
    @JSONField(name = "uid")
    private String uid;

    /**
     * 描述 员工姓名
     * 版本 v2.0
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 描述 员工电话
     * 版本 v2.0
     */
    @JSONField(name = "phone")
    private String phone;

}