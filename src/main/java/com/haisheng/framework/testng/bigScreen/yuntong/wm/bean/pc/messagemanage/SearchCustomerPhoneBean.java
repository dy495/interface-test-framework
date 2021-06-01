package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.messagemanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 14.5. 消息管理客户查询（张小龙）（2021-03-09）v3.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
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