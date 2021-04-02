package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.messagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 12.4. 自定义客户导入 （张小龙） （2020-03-10）v3.0 modify
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class CustomersImportBean implements Serializable {
    /**
     * 描述 客户id列表 推送目标个人客户时时必填
     * 版本 v3.0
     */
    @JSONField(name = "customer_id_list")
    private JSONArray customerIdList;

    /**
     * 描述 发送数量
     * 版本 -
     */
    @JSONField(name = "total")
    private Long total;

}