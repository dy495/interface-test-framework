package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wangmin
 * @date 2021/1/21 10:44
 * @desc 卡券申请信息
 */
@Data
public class ApplyPage implements Serializable {

    /**
     * 优惠券申请类型
     */
    @JSONField(name = "apply_type_name")
    private String applyTypeName;

    /**
     * 审核状态
     */
    @JSONField(name = "status_name")
    private String statusName;

    /**
     * 项目名称
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 列表id自增
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 发出数量
     */
    @JSONField(name = "num")
    private Integer num;
}
