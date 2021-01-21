package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

/**
 * @author wangmin
 * @date  2020/7/9  11:22
 */
public enum SmsTemplateCodeEnum {
    /**
     * 客户到店通知-有手机号
     */
    CUSTOMER_NOTIFY_WITH_PHONE("SMS_191210086", "客户到店通知-有手机号"),
    CUSTOMER_NOTIFY_WITHOUT_PHONE("SMS_191195236", "客户到店通知-无手机号"),
    ACTIVITY_INVITATION("SMS_195860448", "活动邀请"),
    ACTIVITY_REGISTER_SUCCESS("SMS_195870324", "活动报名成功通知"),
    APPOINTMENT_TEST_DRIVE_SMS("SMS_195860448", "预约试驾成功通知"),
    APPOINTMENT_MAINTAIN_OR_REPAIR_SMS("SMS_195586900", "预约保养或者维修成功通知"),
    TEST_DRIVE_CAR_REMAND_DAY("SMS_201683129", "试驾车剩余时间提醒"),
    APPLET_LOGIN_VERIFY_CODE("SMS_205881431", "小程序登录验证码");

    private String code;

    private String templateName;


    public String getCode() {
        return code;
    }

    public String getTemplateName() {
        return templateName;
    }

    SmsTemplateCodeEnum(String code, String templateName) {
        this.code = code;
        this.templateName = templateName;
    }
}
