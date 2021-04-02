package com.haisheng.framework.testng.bigScreen.fengkongdaily.util;

public class PublicParam {
    public int page = 1;
    public int size = 50;
    public String userName="";
    public String password="";
    public long shop_id_01 = 43072l;
    //黑名单风控名字
    public String blackName="黑名单风控规则"+(int) (Math.random() * 10000);
    //收银风控名字
    public String cashierName="收银风控规则"+(int) (Math.random() * 10000);
    //重点观察人员风控名字  观察
    public String observeName="收银风控规则"+(int) (Math.random() * 10000);
    //黑名单风控名字
    public String blackAlarmName="黑名单告警风控规则"+(int) (Math.random() * 10000);
    //收银风控名字
    public String cashierAlarmName="收银风控告警规则"+(int) (Math.random() * 10000);
    //重点观察人员风控名字  观察
    public String observeAlarmName="收银风控告警规则"+(int) (Math.random() * 10000);
    //风控告警名字
    public String AlarmName="风控告警规则"+(int) (Math.random() * 10000);
    //编辑过的风控告警名字
    public String AlarmEditName="已编辑风控告警规则"+(int) (Math.random() * 10000);
    //处理风控事件的备注
    public String remarks="风控事件无异常";
}
