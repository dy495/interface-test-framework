package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.generator.IGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.VoucherPage;

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

    /**
     * 获取卡券页
     *
     * @return 卡券页
     */
    VoucherPage getVoucherPage();
}
