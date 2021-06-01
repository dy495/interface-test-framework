package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.record;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 6.8. 登陆日志 (杨) v3.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class LoginRecordPageBean implements Serializable {
    /**
     * 描述 当前页
     * 版本 v1.0
     */
    @JSONField(name = "page")
    private Integer page;

    /**
     * 描述 当前页的数量
     * 版本 v1.0
     */
    @JSONField(name = "size")
    private Integer size;

    /**
     * 描述 每页的数量
     * 版本 v1.0
     */
    @JSONField(name = " page_size")
    private Integer  pageSize;

    /**
     * 描述 总数
     * 版本 v1.0
     */
    @JSONField(name = "total")
    private Long total;

    /**
     * 描述 总页数
     * 版本 v1.0
     */
    @JSONField(name = "pages")
    private Integer pages;

    /**
     * 描述 详细数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 登陆名称
     * 版本 v3.0
     */
    @JSONField(name = "login_name")
    private String loginName;

    /**
     * 描述 最早登陆时间
     * 版本 v3.0
     */
    @JSONField(name = "earliest_login_time")
    private String earliestLoginTime;

    /**
     * 描述 最晚登陆时间
     * 版本 v3.0
     */
    @JSONField(name = "latest_login_time")
    private String latestLoginTime;

    /**
     * 描述 登陆账号
     * 版本 v3.0
     */
    @JSONField(name = "login_account")
    private String loginAccount;

}