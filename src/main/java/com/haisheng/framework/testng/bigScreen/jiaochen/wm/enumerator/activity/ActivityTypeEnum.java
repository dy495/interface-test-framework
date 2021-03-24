package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.flow.InformationFlowTypeEnum;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author : wangmin
 * @date :  2020/12/23  16:26
 */
public enum ActivityTypeEnum {

    /**
     * 裂变优惠券
     */
    FISSION_VOUCHER(1, "裂变优惠券", "pages/moduleActivity/activityFissionDetail/activityFissionDetail", TargetTypeEnum.ATTRACT_NEW, InformationFlowTypeEnum.ACTIVITY, false, true),
    /**
     * 活动招募
     */
    RECRUIT(2, "活动招募", "pages/moduleActivity/activityDetail/activityDetail", TargetTypeEnum.ATTRACT_NEW, InformationFlowTypeEnum.ACTIVITY, true, true),
    /**
     * 活动招募
     */
    CONTENT_MARKETING(3, "内容营销", "pages/moduleActivity/activityDetail/activityDetail", TargetTypeEnum.ATTRACT_NEW, InformationFlowTypeEnum.ACTIVITY, true, true);

    private Integer id;

    private String name;

    private String appletPagePath;

    private TargetTypeEnum targetType;

    private InformationFlowTypeEnum informationFlowTypeEnum;

    private boolean isCanRegister;

    private boolean isNeedAuth;

    ActivityTypeEnum(Integer id, String name, String appletPagePath, TargetTypeEnum targetType, InformationFlowTypeEnum informationFlowTypeEnum, boolean isCanRegister, boolean isNeedAuth) {
        this.id = id;
        this.name = name;
        this.appletPagePath = appletPagePath;
        this.targetType = targetType;
        this.informationFlowTypeEnum = informationFlowTypeEnum;
        this.isCanRegister = isCanRegister;
        this.isNeedAuth = isNeedAuth;
    }

    public boolean isCanRegister() {
        return isCanRegister;
    }

    public TargetTypeEnum getTargetType() {
        return targetType;
    }

    public InformationFlowTypeEnum getInformationFlowTypeEnum() {
        return informationFlowTypeEnum;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAppletPagePath() {
        return appletPagePath;
    }

    public boolean isNeedAuth() {
        return isNeedAuth;
    }

    public static ActivityTypeEnum findById(Integer id) {
        Optional<ActivityTypeEnum> any = Arrays.stream(values()).filter(t -> t.id.equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "活动类型不正确");
        return any.get();
    }
}
