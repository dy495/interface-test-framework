package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.points;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date  2020/12/7  16:41
 */

public enum PointsSourceEnum {

    /**
     * 预约准时到店奖励
     */
    APPOINTMENT_ON_TIME_REWARD(1, "预约准时到店奖励", InOutTypeEnum.INCOME, "预约准时到店积分奖励奖励");

    private int id;

    private String sourceName;


    private InOutTypeEnum type;


    private String remark;

    PointsSourceEnum(int id, String sourceName, InOutTypeEnum type, String remark) {
        this.id = id;
        this.sourceName = sourceName;
        this.type = type;
        this.remark = remark;
    }


    public int getId() {
        return id;
    }

    public String getSourceName() {
        return sourceName;
    }

    public InOutTypeEnum getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }

    public static PointsSourceEnum findById(Integer id) {
        Preconditions.checkArgument(id != null, "积分渠道不存在");
        Optional<PointsSourceEnum> any = Arrays.stream(values()).filter(s -> s.id == id).findAny();
        Preconditions.checkArgument(any.isPresent(), "积分渠道不存在");
        return any.get();
    }
}
