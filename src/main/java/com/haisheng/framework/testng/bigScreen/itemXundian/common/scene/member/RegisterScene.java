package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.member;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 12.2. 注册会员
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class RegisterScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 id
     * 是否必填 false
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 头像oss path
     * 是否必填 false
     * 版本 -
     */
    private final String picPath;

    /**
     * 描述 会员id
     * 是否必填 false
     * 版本 -
     */
    private final String memberId;

    /**
     * 描述 会员名称
     * 是否必填 false
     * 版本 -
     */
    private final String memberName;

    /**
     * 描述 联系电话
     * 是否必填 false
     * 版本 -
     */
    private final String phone;

    /**
     * 描述 生日
     * 是否必填 false
     * 版本 -
     */
    private final String birthday;

    /**
     * 描述 任务id
     * 是否必填 false
     * 版本 -
     */
    private final String userId;

    /**
     * 描述 会员身份
     * 是否必填 false
     * 版本 -
     */
    private final Long identity;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("pic_path", picPath);
        object.put("member_id", memberId);
        object.put("member_name", memberName);
        object.put("phone", phone);
        object.put("birthday", birthday);
        object.put("user_id", userId);
        object.put("identity", identity);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/member/register";
    }
}