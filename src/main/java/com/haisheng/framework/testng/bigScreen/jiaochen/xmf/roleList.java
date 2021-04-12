package com.haisheng.framework.testng.bigScreen.jiaochen.xmf;

import java.util.Arrays;
import java.util.Optional;

public enum roleList {
    DateAccess0("全部",136),
    DateAccess1("个人",142),
    DateAccess2("导出",193),
    DateAccess3("集团",138),
    DateAccess4("区域",144),
    DateAccess5("品牌",146),
    DateAccess6("门店",148),
    DateAccess7("接待管理页面",104),
    DateAccess8("售后接待",137),
    DateAccess9("变更售后接待",166),
    DateAccess10("客户管理页面",105),
    DateAccess11("销售客户TAB ",121),
    DateAccess12("售后客户TAB",122),
    DateAccess13("小程序客户TAB",123),
    DateAccess14("预约管理页面",106),
    DateAccess15("预约看板TAB",124),
    DateAccess16("预约记录TAB",125),
    DateAccess17("预约配置TAB",126),
    DateAccess18("保养配置TAB",127),
    DateAccess19("预约应答人",145),
    DateAccess20("提醒接收人",147),
    DateAccess21("预约维修配置",167),
    DateAccess22("预约保养分配",143),
    DateAccess23("售后评价差评接收人",168),
    DateAccess24("销售评价差评接收人",169),
    DateAccess25("评价管理",194),
    DateAccess26("评价列表",195),
    DateAccess27("售后评价配置",196),
    DateAccess28("销售评价配置",197),
    DateAccess29("智能提醒",170),
    DateAccess30("道路救援",171),
    DateAccess31("门店管理页面",107),
    DateAccess32("品牌管理页面",108),
    DateAccess33("角色管理页面",109),
    DateAccess34("员工管理页面",110),
    DateAccess35("品牌删除按钮",140),
    DateAccess36("删除车系",200),
    DateAccess37("删除车型",201),
    DateAccess38("核销人员页面",131),
    DateAccess39("核销权限",173),
    DateAccess40("套餐管理页面",112),
    DateAccess41("套餐表单TAB",132),
    DateAccess42("套餐购买记录TAB",133),
    DateAccess43("确认支付按钮",141),
    DateAccess44("临时套餐表单",174),
    DateAccess45("套餐通过/拒绝",199),
    DateAccess46("套餐作废",202),
    DateAccess47("消息管理页面",113),
    DateAccess48("优惠券管理",175),
    DateAccess49("优惠券作废",177),
    DateAccess50("删除优惠券",198),
    DateAccess51("优惠券审批页面",117),
    DateAccess52("活动管理",178),
    DateAccess53("活动审批",179),
    DateAccess54("会员营销",180),
    DateAccess55("内容管理页面",114),
    DateAccess56("banner管理页面",116),
    DateAccess57("导入记录页面",118),
    DateAccess58("导出记录页面",119),
    DateAccess59("消息记录页面",120),
    DateAccess60("删除记录",181),
    DateAccess61("商品管理页面",182),
    DateAccess62("商品品类页面",183),
    DateAccess63("商品品牌页面",184),
    DateAccess64("商品规格页面",185),
    DateAccess65("积分兑换页面",186),
    DateAccess66("积分明细页面",187),
    DateAccess67("积分订单页面",188),
    DateAccess68("积分规则页面",189),
    DateAccess69("商城订单页面",190),
    DateAccess70("商城套餐页面",191),
    DateAccess71("分销员管理页面",192),
    DateAccess72("积分客户管理页面",203),
    DateAccess73("增/减积分按钮",204);


    private int value;
    private String lable;
    public int getValue() {
        return value;
    }

    public String getLable() {
        return lable;
    }

    roleList(String lable,int value){
            this.lable=lable;
            this.value=value;

    }
    public static roleList findByLable(String lable){
        Optional <roleList> any= Arrays.stream(values()).filter(f->f.getLable().equals(lable)).findAny();

        return any.get();
    }


}
