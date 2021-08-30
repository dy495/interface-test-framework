package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.afterspecialaudio;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.1. 特殊音频记录分页（华成裕）
 *
 * @author wangmin
 * @date 2021-08-30 14:39:23
 */
@Builder
public class AfterSpecialAudioPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 接待顾问姓名
     * 是否必填 false
     * 版本 v1.0
     */
    private final String receptorName;

    /**
     * 描述 审核状态 使用"获取指定枚举值列表"接口获取 enum_type为 APPROVAL_STATUSES
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer approvalStatus;

    /**
     * 描述 接待时间范围查询开始日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String receptionStart;

    /**
     * 描述 接待时间范围查询结束日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String receptionEnd;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String saleId;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Boolean isSpecial;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("receptor_name", receptorName);
        object.put("approval_status", approvalStatus);
        object.put("reception_start", receptionStart);
        object.put("reception_end", receptionEnd);
        object.put("saleId", saleId);
        object.put("isSpecial", isSpecial);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/after-special-audio/page";
    }
}