package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.messagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 12.5. 消息管理客户查询（张小龙）（2021-03-09）v3.0
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class SearchCustomerPhoneBean implements Serializable {
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