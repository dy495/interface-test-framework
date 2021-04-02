package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.staff;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 22.8. 员工详情 （杨）（2021-03-23） v3.0
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class DetailBean implements Serializable {
    /**
     * 描述 账号Id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private String id;

    /**
     * 描述 账号名称
     * 版本 v1.0
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 描述 手机号
     * 版本 v1.0
     */
    @JSONField(name = "phone")
    private String phone;

    /**
     * 描述 性别 MALE：男，FEMALE：女
     * 版本 v1.0
     */
    @JSONField(name = "gender")
    private String gender;

    /**
     * 描述 创建时间
     * 版本 v1.0
     */
    @JSONField(name = "create_time")
    private String createTime;

    /**
     * 描述 状态
     * 版本 v1.0
     */
    @JSONField(name = "status")
    private String status;

    /**
     * 描述 状态名称
     * 版本 v1.0
     */
    @JSONField(name = "status_name")
    private String statusName;

    /**
     * 描述 角色列表
     * 版本 v1.0
     */
    @JSONField(name = "role_list")
    private JSONArray roleList;

    /**
     * 描述 角色id
     * 版本 v1.0
     */
    @JSONField(name = "role_id")
    private Integer roleId;

    /**
     * 描述 角色名称
     * 版本 v1.0
     */
    @JSONField(name = "role_name")
    private String roleName;

    /**
     * 描述 门店列表
     * 版本 v2.0
     */
    @JSONField(name = "shop_list")
    private JSONArray shopList;

    /**
     * 描述 门店id
     * 版本 v1.0
     */
    @JSONField(name = "shop_id")
    private Long shopId;

    /**
     * 描述 门店名称
     * 版本 v1.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 核销码
     * 版本 v2.0
     */
    @JSONField(name = "verify_code")
    private String verifyCode;

    /**
     * 描述 员工图片
     * 版本 v2.0
     */
    @JSONField(name = "picture_url")
    private String pictureUrl;

}