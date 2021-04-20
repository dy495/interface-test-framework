package com.haisheng.framework.testng.bigScreen.fengkongdaily.util;

import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;

public class PublicParam {
    DateTimeUtil dt =new DateTimeUtil();

    public int page = 1;
    public int size = 50;
    public String userName="yuexiu@test.com";
    public String password="f5b3e737510f31b88eb2d4b5d0cd2fb4";
    public String ownerPhone="13373166806";
    public long shop_id_01 = 43072l;
    //黑名单风控名字
    public String blackName="黑名单风控规则"+(int) (Math.random() * 10000);
    //黑名单风控名字异常21个字
    public String blackNameException="12345678901234567901";
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
    //收银风控告警名字
    public String AlarmNameCashier="收银风控告警规则"+(int) (Math.random() * 10000);
    //风控告警名字
    public String AlarmNameBlack="黑名单风控风控告警规则"+(int) (Math.random() * 10000);
    //重点观察人员告警名字
    public String AlarmNameObserve="风控告警规则"+(int) (Math.random() * 10000);
    //编辑过的风控告警名字
    public String AlarmEditName="已编辑风控告警规则"+(int) (Math.random() * 10000);
    //处理风控事件的备注
    public String remarks="风控事件无异常";
    //新增角色的名称
    public String roleName="自动化角色"+(int) (Math.random() * 10000);
    //编辑角色的名称
    public String roleEditName="已编辑自动化角色"+(int) (Math.random() * 10000);
    //编辑角色的名称21个字异常
    public String rolNameException="123456789012345678901";
    //角色中的描述
    public String descriptionRole = "新增角色的description";
    //编辑角色中的描述
    public String descriptionEditRole = "编辑角色的description";
    //角色中的描述51个字
    public String descriptionRoleException = "1234567890123456789012345678901234567890哈哈hh@#*自动化呀呀";
    //账号名称
    public String staffName="自动化账号"+(int) (Math.random() * 10000);
    //账号名称51个字
    public String staffNameException="1234567890123456789012345678901234567890哈哈hh@#*自动化呀呀";
    //编辑账号名称
    public String staffEditName="已编辑自动化账号"+(int) (Math.random() * 10000);
    //交易ID
    public String openId = "deal" + CommonUtil.getRandom(8);
    //商品ID
    public String commodityId = "commodity" + CommonUtil.getRandom(8);
    //客户ID
    public String userId = "tester" + CommonUtil.getRandom(6);
    //交易ID
    String time = dt.getHistoryDate(0);
    String time1 = dt.getHHmm(0);
    public String transId = "QATest_" + CommonUtil.getRandom(3) + time + time1;
    public String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/crm/xmf/facebaes64.txt";
    public String filePath2 = "src/main/java/com/haisheng/framework/testng/bigScreen/crm/xmf/xia.png";
//    public String filePath2 = "src/main/java/com/haisheng/framework/testng/bigScreen/crm/xmf/lv.jpg";
//    public String filePath2 = "src/main/java/com/haisheng/framework/testng/bigScreen/crm/xmf/guoliya.jpg";
}
