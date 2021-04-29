package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.message;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.evaluate.EvaluateTypeEnum;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author : wangmin
 * @date :  2020/8/7  15:43
 */
public enum AppletMessageTypeEnum {

    /**
     * 评价消息
     */
    RECEPTION_EVALUATE_TIP("销售接待评价消息", EvaluateTypeEnum.PRE_SALES_RECEPTION, true),
    NEW_CAR_EVALUATE_TIP("新车评价消息", EvaluateTypeEnum.BUY_NEW_CAR, true),
    MAINTAIN_EVALUATE_TIP("保养评价消息", EvaluateTypeEnum.MAINTAIN, true),
    REPAIR_EVALUATE_TIP("维修评价消息", EvaluateTypeEnum.REPAIR, true),
    SYSTEM("系统消息", null, false),
    CARD_VOLUME("卡券类", null, false),
    ACTIVITY("活动类", null, false),
    EVALUATE("评价类", null, false),
    SETMEAL("套餐类", null, false),
    SCORE("积分类", null, false),
    MAINTAIN("保养类", null, false);

    private String typeName;

    private EvaluateTypeEnum serviceType;

    private boolean isEvaluateType;

    AppletMessageTypeEnum(String typeName, EvaluateTypeEnum serviceType, boolean isEvaluateType) {
        this.typeName = typeName;
        this.serviceType = serviceType;
        this.isEvaluateType = isEvaluateType;
    }

    public EvaluateTypeEnum getServiceType() {
        return serviceType;
    }

    public String getTypeName() {
        return typeName;
    }

    public boolean isEvaluateType() {
        return isEvaluateType;
    }

    public static AppletMessageTypeEnum findByType(String type) {
        Optional<AppletMessageTypeEnum> any = Arrays.stream(values()).filter(t -> t.name().equals(type))
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "消息类型不存在");
        return any.get();
    }

    public static AppletMessageTypeEnum findByEvaluateType(EvaluateTypeEnum type) {
        Preconditions.checkArgument(type != null, "消息类型不存在");
        Optional<AppletMessageTypeEnum> any = Arrays.stream(values())
                .parallel().filter(t -> t.getServiceType() != null)
                .filter(t -> t.getServiceType() == type)
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "消息类型不存在");
        return any.get();
    }
}
