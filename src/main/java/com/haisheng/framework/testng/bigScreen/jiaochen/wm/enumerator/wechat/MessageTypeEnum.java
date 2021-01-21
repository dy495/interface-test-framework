package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.wechat;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.evaluate.EvaluateTypeEnum;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date  2020/8/7  15:43
 */
public enum MessageTypeEnum {

    /**
     * 出门条
     */
    EXIT_NOTE("出门条", null, false),
    MAINTAIN_TIP("该保养了", null, false),
    VACATION_MAINTAIN_TIP("假期保养", null, false),
    EVALUATE_TIP("评价消息", EvaluateTypeEnum.BUY_NEW_CAR, true),
    CUSTOM("自定义消息", null, false),
    SYSTEM("系统消息", null, false);

    private String typeName;

    private EvaluateTypeEnum serviceType;

    private boolean isEvaluateType;

    MessageTypeEnum(String typeName, EvaluateTypeEnum serviceType, boolean isEvaluateType) {
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

    public static MessageTypeEnum findByType(String type) {
        Optional<MessageTypeEnum> any = Arrays.stream(values()).filter(t -> t.name().equals(type))
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "消息类型不存在");
        return any.get();
    }
}
