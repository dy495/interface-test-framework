package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.reception.pre;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 接待状态
 *
 * @author liujiachun
 * @date  2020/5/21  13:04
 */
public enum PreReceptionStatusEnum {

    /**
     * 接待中
     */
    IN_SERVICE(0, "接待中", "#A4F0B8", true),
    /**
     * 订单客户
     */
    ORDER_CUSTOMER(1, "订单客户", "#E2E3F1", true),
    /**
     * 等待中
     */
    WAITING(2, "等待中", "#FFBDBD", true),
    /**
     * 离店
     */
    LEAVE(3, "完成接待", "#D2ECFF", false),
    /**
     * 他人接待 不要存不要存不要存
     */
    OTHER_IN_SERVICE(4, "他人接待", "#DDE1E5", false);

    private Integer id;

    private String name;

    private String color;

    private boolean unReceptionStatus;


    PreReceptionStatusEnum(Integer id, String name, String color, boolean unReceptionStatus) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.unReceptionStatus = unReceptionStatus;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public static PreReceptionStatusEnum findById(Integer id) {
        Optional<PreReceptionStatusEnum> any = Arrays.stream(values())
                .filter(e -> e.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "接待状态不存在");
        return any.get();
    }

    public static List<Integer> unReceptionStatus() {
        return Arrays.stream(values()).filter(s -> s.unReceptionStatus)
                .map(PreReceptionStatusEnum::ordinal).collect(Collectors.toList());
    }

}
