package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

public enum EnumDesc {

    PACKAGE_DESC("2020马上就要过去，赶快来保养一下爱车吧，开开心心过年！！！"),

    VOUCHER_DESC("使用此卡券可在适用门店享受立减优惠哦~~~"),

    ARTICLE_DESC("先帝创业未半而中道崩殂，今天下三分，益州疲弊，此诚危急存亡之秋也。然侍卫之臣不懈于内，忠志之士忘身于外者，盖追先帝之殊遇，欲报之于陛下也。" +
            "诚宜开张圣听，以光先帝遗德，恢弘志士之气，不宜妄自菲薄，引喻失义，以塞忠谏之路也。宫中府中，俱为一体，陟罚臧否，不宜异同。若有作奸犯科及为忠善者，" +
            "宜付有司论其刑赏，以昭陛下平明之理，不宜偏私，使内外异法也。侍中、侍郎郭攸之、费祎、董允等，此皆良实，志虑忠纯，是以先帝简拔以遗陛下。" +
            "愚以为宫中之事，事无大小，悉以咨之，然后施行，必能裨补阙漏，有所广益。"),

    MESSAGE_DESC("I hope the new covid-19 epidemic will be over."),

    MESSAGE_TITLE("来自土星的消息"),

    INVALID_REASON("不是价值客户，作废卡券"),

    ACTIVITY_DESC("I hope the new covid-19 epidemic will be over."),

    FAULT_DESCRIPTION("网线传输能力不足或网络不稳定"),
    ;

    EnumDesc(String desc) {
        this.desc = desc;
    }

    @Getter
    private final String desc;
}
