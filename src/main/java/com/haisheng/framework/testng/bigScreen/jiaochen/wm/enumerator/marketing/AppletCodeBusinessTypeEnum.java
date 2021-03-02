package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * 小程序码业务类型枚举
 *
 * @author wangmin
 * @date 2020/12/22 15:52
 */
public enum AppletCodeBusinessTypeEnum {


    /**
     * 洗车
     */
    WASH_CAR("00", "免费洗车"),

    /**
     * 生日积分
     */
    BIRTHDAY_SCORE("01", "生日积分"),

    /**
     * 分享管理
     */
    SHARE_MANAGER("02", "分享管理"),
    /**
     * 签到
     */
    SIGN_IN("03", "签到"),

    /**
     * 签到分享
     */
    SIGN_IN_SHARE("04", "签到分享"),

    /**
     * 活动分享
     */
    ACTIVITY_SHARE("05", "活动分享"),

    /**
     * 二维码分享
     */
    QR_CODE_SHARE("06", "二维码分享"),

    /**
     * 完善资料
     */
    PERFECT_INFORMATION("07", "完善资料"),

    /**
     * 预约保养
     */
    APPOINTMENT_MAINTAIN("08", "预约保养"),

    /**
     * 预约维修
     */
    APPOINTMENT_REPAIR("09", "预约维修"),

    /**
     * 售后维修
     */
    AFTER_SALES_REPAIR("0A", "售后维修"),

    /**
     * 小程序注册
     */
    WECHAT_REGISTER("0B", "小程序注册"),

    /**
     * 卡券推广业务
     */
    VOUCHER_EXTENSION("0C", "卡券推广"),

    /**
     * 专属顾问
     */
    EXCLUSIVE_COUNSELOR("0D", "专属顾问"),

    /**
     * 专属客服
     */
    EXCLUSIVE_CUSTOMER_SERVICE("0E", "专属客服"),

    /**
     * 活动报名优先
     */
    ACTIVITY_APPLY_PRIORITY("0F", "活动报名优先"),

    /**
     * 新车试驾优先
     */
    NEW_CAR_TEST_DRIVER_PRIORITY("0G", "新车试驾优先");


    private final String key;

    private final String typeName;

    AppletCodeBusinessTypeEnum(String key, String typeName) {
        this.key = key;
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getKey() {
        return key;
    }

    private final static Pattern KEY_PATTERN = Pattern.compile("^[a-zA-Z0-9]{2}");

    public static void checkType() {
        Arrays.stream(values()).peek(t -> Preconditions.checkArgument(KEY_PATTERN.matcher(t.key).matches(), "类型key{" + t.key + "}应为2位[a-zA-Z0-9]格式"))
                .collect(groupingBy(AppletCodeBusinessTypeEnum::getKey, counting()))
                .forEach((k, v) -> Preconditions.checkArgument(v == 1, "类型key{" + k + "}重复"));
    }

    public static AppletCodeBusinessTypeEnum findByKey(String key) {
        Optional<AppletCodeBusinessTypeEnum> any = Arrays.stream(values()).filter(t -> t.getKey().equals(key)).findAny();
        Preconditions.checkArgument(any.isPresent(), "小程序码业务类型不存在");
        return any.get();
    }

    public static String findKeyByTypeName(String typeName) {
        return Arrays.stream(AppletCodeBusinessTypeEnum.values()).filter(e -> e.getTypeName().equals(typeName)).map(AppletCodeBusinessTypeEnum::getKey).findFirst().orElse(null);
    }

    public static boolean isContainTypeName(String str) {
        return Arrays.stream(AppletCodeBusinessTypeEnum.values()).anyMatch(e -> str.contains(e.typeName));
    }

    public static AppletCodeBusinessTypeEnum findEnumByContainTypeName(String str) {
        return Arrays.stream(AppletCodeBusinessTypeEnum.values()).filter(e -> str.contains(e.typeName)).findFirst().orElse(null);
    }
}
