package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.*;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2021/1/20 11:53
 * @desc 卡券状态
 */
public enum VoucherStatusEnum {
    /**
     * 待审核
     */
    WAITING(0, "审核中", new WaitingVoucherGenerator.Builder()),

    /**
     * 已撤回
     */
    RECALL(1, "已撤回", new RecallGenerator.Builder()),

    /**
     * 审核未通过
     */
    REJECT(2, "已拒绝", new RejectGenerator.Builder()),
    /**
     * 进行中
     */
    WORKING(3, "进行中", new WorkingGenerator.Builder()),
//    /**
//     * 暂停发放
//     */
//    STOP(4, "暂停发放"),
//    /**
//     * 已作废
//     */
//    INVALIDED(5, "已作废"),
//    /**
//     * 已售罄
//     */
//    SELL_OUT(6, "已售罄"),
//    /**
//     * 已过期
//     */
//    EXPIRED(7, "已过期"),

    CREATE(-1, "初始状态", new VoucherGenerator.Builder());;

    @Getter
    private final Integer id;
    @Getter
    private final String name;
    @Getter
    private final BaseGenerator.BaseBuilder generateBuilder;

    VoucherStatusEnum(Integer id, String name, BaseGenerator.BaseBuilder generateBuilder) {
        this.id = id;
        this.name = name;
        this.generateBuilder = generateBuilder;
    }

    public static VoucherStatusEnum findById(Integer id) {
        Preconditions.checkArgument(null != id, "卡券状态不存在");
        Optional<VoucherStatusEnum> any = Arrays.stream(values()).filter(v -> id.equals(v.getId())).findAny();
        Preconditions.checkArgument(any.isPresent(), "卡券状态不存在");
        return any.get();
    }

    public static String getNameById(Integer id) {
        return Arrays.stream(VoucherStatusEnum.values()).filter(e -> e.getId().equals(id)).map(VoucherStatusEnum::getName).findFirst().orElse(null);
    }

}