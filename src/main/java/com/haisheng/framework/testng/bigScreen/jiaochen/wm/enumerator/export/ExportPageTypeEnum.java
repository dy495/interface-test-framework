package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.export;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date  2020/12/22  18:01
 */
public enum ExportPageTypeEnum {

    /**
     * 接待管理
     */
    RECEPTION("接待管理"),
    /**
     * 销售客户
     */
    SALE_CUSTOMER("销售客户"),
    /**
     * 售后客户
     */
    AFTER_CUSTOMER("售后客户"),

    /**
     * 维修记录
     */
    REPAIR_RECORD("维修记录"),

    /**
     * 小程序客户
     */
    WECHAT_CUSTOMER("小程序客户"),

    /**
     * 预约记录
     */
    APPOINTMENT_RECORD("预约记录"),

    /**
     * 保养配置
     */
    MAINTAIN("保养配置"),

    /**
     * 评价列表
     */
    EVALUATE_LIST("评价列表"),

    /**
     * 卡券列表
     */
    VOUCHER_LIST("卡券列表"),

    /**
     * 变更记录
     */
    VOUCHER_CHANGE_LIST("变更记录"),

    /**
     * 作废记录页
     */
    VOUCHER_INVALID_LIST("作废记录"),

    /**
     * 增发记录
     */
    VOUCHER_ADDITIONAL_LIST("增发记录"),

    /**
     * 卡券领取记录
     */
    VOUCHER_RECEIVE_LIST("卡券领取记录"),

    /**
     * 核销记录
     */
    VERIFICATION_RECORD("核销记录"),

    /**
     * 核销人员
     */
    VERIFICATION_PEOPLE("核销人员"),

    /**
     * 套餐购买记录
     */
    BUY_PACKAGE_RECORD("套餐购买记录"),

    /**
     * 内容管理-文章导出
     */
    ARTICLE_LIST("文章列表"),

    /**
     * 活动列表
     */
    ACTIVITY_LIST("活动列表"),

    /**
     * 活动报名列表
     */
    REGISTER_LIST("活动报名记录"),

    /**
     * 卡券申请
     */
    VOUCHER_APPLY("卡券申请"),

    /**
     * 门店管理
     */
    SHOP_LIST("门店管理"),

    /**
     * 品牌管理
     */
    BRAND_LIST("品牌管理"),

    /**
     * 车系列表
     */
    CAR_STYLE_LIST("车系列表"),

    /**
     * 车型列表
     */
    CAR_MODEL_LIST("车型列表"),

    /**
     * 角色管理
     */
    ROLE_LIST("角色管理"),

    /**
     * 员工管理
     */
    STAFF_LIST("员工管理"),

    /**
     * 导入记录
     */
    INPUT_RECORD("导入记录"),

    /**
     * 导出记录
     */
    EXPORT_RECORD("导出记录"),

    /**
     * 消息记录
     */
    MESSAGE_RECORD("消息记录"),

    /**
     * 在线救援
     */
    RESCUE_RECORD("在线救援"),

    /**
     * 洗车管理
     */
    WASH_CAR_LIST("洗车管理"),

    /**
     * 调整次数
     */
    ADJUST_NUMBER_RECORD("调整次数"),

    /**
     * 积分兑换商品
     */
    INTEGRAL_EXCHANGE_GOODS("积分兑换商品"),

    /**
     * 积分兑换明细
     */
    INTEGRAL_EXCHANGE_DETAIL("积分兑换明细"),

    /**
     * 积分兑换订单
     */
    INTEGRAL_EXCHANGE_ORDER("积分兑换订单"),

    /**
     * 商品管理
     */
    GOODS_LIST("商品管理"),

    /**
     * 积分变更记录
     */
    SIGN_CHANGE_RECORD("积分变更记录"),

    ;

    private String desc;

    ExportPageTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static ExportPageTypeEnum findByType(String type) {
        Optional<ExportPageTypeEnum> any = Arrays.stream(values()).filter(t -> t.name().equals(type)).findAny();
        Preconditions.checkArgument(any.isPresent(), "导出页面类型不存在");
        return any.get();
    }

}
