package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.role;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 33.8. 可用角色列表 （杨航）
 *
 * @author wangmin
 * @date 2021-06-01 18:39:23
 */
@Data
public class ListlistBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 角色Id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private Integer id;

    /**
     * 描述 角色名称
     * 版本 v1.0
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 描述 账号使用数量
     * 版本 v1.0
     */
    @JSONField(name = "num")
    private Integer num;

    /**
     * 描述 创建时间
     * 版本 v1.0
     */
    @JSONField(name = "create_time")
    private String createTime;

    /**
     * 描述 角色状态
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
     * 描述 权限集合
     * 版本 v1.0
     */
    @JSONField(name = "auth_list")
    private JSONArray authList;

    /**
     * 描述 角色描述
     * 版本 v2.0
     */
    @JSONField(name = "description")
    private String description;

}