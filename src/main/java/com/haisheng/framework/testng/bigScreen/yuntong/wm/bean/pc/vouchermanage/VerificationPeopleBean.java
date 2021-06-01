package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 7.27. 
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class VerificationPeopleBean implements Serializable {
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
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "verification_person")
    private String verificationPerson;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "create_time")
    private String createTime;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "verification_identity")
    private String verificationIdentity;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "verification_phone")
    private String verificationPhone;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "verification_code")
    private String verificationCode;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "verification_number")
    private Integer verificationNumber;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "verification_status")
    private Boolean verificationStatus;

}