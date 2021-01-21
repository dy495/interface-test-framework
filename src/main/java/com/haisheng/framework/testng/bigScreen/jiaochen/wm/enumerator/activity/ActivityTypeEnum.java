package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.flow.InformationFlowTypeEnum;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date  2020/12/23  16:26
 */
public enum ActivityTypeEnum {

    /**
     * 裂变优惠券
     */
    FISSION_VOUCHER(1, "裂变优惠券", TargetTypeEnum.ATTRACT_NEW, InformationFlowTypeEnum.ACTIVITY),
    /**
     * 活动招募
     */
    RECRUIT(2, "活动招募", TargetTypeEnum.ATTRACT_NEW, InformationFlowTypeEnum.ACTIVITY);

    private Integer id;

    private String name;

    private TargetTypeEnum targetType;

    private InformationFlowTypeEnum informationFlowTypeEnum;

    ActivityTypeEnum(Integer id, String name, TargetTypeEnum targetType, InformationFlowTypeEnum informationFlowTypeEnum) {
        this.id = id;
        this.name = name;
        this.targetType = targetType;
        this.informationFlowTypeEnum = informationFlowTypeEnum;
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

    public static ActivityTypeEnum findById(Integer id) {
        Optional<ActivityTypeEnum> any = Arrays.stream(values()).filter(t -> t.id.equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "活动类型不正确");
        return any.get();
    }
}
