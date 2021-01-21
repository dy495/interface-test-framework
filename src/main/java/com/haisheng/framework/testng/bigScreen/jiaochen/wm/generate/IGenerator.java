package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;

/**
 * @author wangmin
 * @date 2021/1/20 14:36
 * @desc 卡券状态生成接口
 */
public interface IGenerator {

    Long getVoucherId();

    void execute(Visitor visitor);
}
