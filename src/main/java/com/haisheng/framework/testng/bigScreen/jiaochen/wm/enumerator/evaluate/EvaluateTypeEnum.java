package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.evaluate;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.common.AuthCodeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.common.ServiceTypeEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author : wangmin
 * @date :  2020/8/7  12:12
 */
public enum EvaluateTypeEnum {
    /**
     * 保养
     */
    MAINTAIN(1, ServiceTypeEnum.AFTER_SALE, AuthCodeEnum.AFTER_EVALUATE, "保养接待评价", "保养接待评价消息", "保养接待评价"),
    REPAIR(2, ServiceTypeEnum.AFTER_SALE, AuthCodeEnum.AFTER_EVALUATE, "维修接待评价", "维修接待评价消息", "维修接待评价"),
    BUY_NEW_CAR(3, ServiceTypeEnum.PRE_SALES, AuthCodeEnum.PRE_EVALUATE, "销售购车评价", "销售购车评价消息", "销售购车评价"),
    PRE_SALES_RECEPTION(4, ServiceTypeEnum.PRE_SALES, AuthCodeEnum.PRE_EVALUATE, "销售接待评价", "销售接待评价消息", "销售接待评价");


    private Integer id;

    private ServiceTypeEnum serviceType;

    private AuthCodeEnum authCode;

    private String typeName;

    private String msgTitle;

    private String desc;

    public String getDesc() {
        return desc;
    }

    EvaluateTypeEnum(Integer id, ServiceTypeEnum serviceType, AuthCodeEnum authCode, String typeName, String msgTitle, String desc) {
        this.id = id;
        this.serviceType = serviceType;
        this.authCode = authCode;
        this.typeName = typeName;
        this.msgTitle = msgTitle;
        this.desc = desc;
    }

    public ServiceTypeEnum getServiceType() {
        return serviceType;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public Integer getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

    public static EvaluateTypeEnum findById(Integer id) {
        Optional<EvaluateTypeEnum> any = Arrays.stream(values()).filter(t -> t.id.equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "评价类型不存在");
        return any.get();
    }

    public static List<Integer> findIdsByAuth(AuthCodeEnum authCode) {
        return Arrays.stream(values()).filter(t -> t.authCode != null).filter(t -> t.authCode == authCode)
                .map(EvaluateTypeEnum::getId).collect(Collectors.toList());
    }
}
