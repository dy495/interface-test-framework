package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.generator.voucher;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.IGenerator;

/**
 * 卡券生成器
 *
 * @author wangmin
 * @date 2021/1/22 11:26
 */
public interface IVoucher extends IGenerator {

    /**
     * 获取卡券id
     *
     * @return voucherId
     */
    Long getVoucherId();
}
