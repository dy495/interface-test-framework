package com.haisheng.framework.testng.bigScreen.jiaochen.wm.util;

import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.Create;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherFormPage;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.ImageUtil;

import java.util.ArrayList;

/**
 * 业务场景工具
 */
public class BusinessUtil {
    ScenarioUtil jc = ScenarioUtil.getInstance();
    private static final int size = 100;

    public void createVoucher() {
        String path = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/头像.jpg";
        String picture = new ImageUtil().getImageBinary(path);
        String voucherDescription = "商家大促销";
        String voucherName = getVoucherName();
        IScene scene1 = Create.builder().voucherPic(picture).voucherName(voucherName)
                .voucherDescription(voucherDescription).stock(10000).cost(10000)
                .shopType(1).shopIds(new ArrayList<>())
                .selfVerification(true).subjectType("").subjectId(1L).build();
        jc.invokeApi(scene1);
    }

    public String getVoucherName() {
        int money = CommonUtil.getRandom(1, 100000);
        String voucherName = "满" + money + "减" + money + "代金券";
        VoucherFormPage.VoucherFormPageBuilder scene = VoucherFormPage.builder();
        int total = jc.invokeApi(scene.build()).getInteger("total");
        int s = CommonUtil.getTurningPage(total, size);
        for (int i = 1; i < s; i++) {
            scene.page(i).size(size);
            JSONArray array = jc.invokeApi(scene.build()).getJSONArray("list");
            for (int j = 0; j < array.size(); j++) {
                if (!array.getJSONObject(j).getString("voucher_name").equals(voucherName)) {
                    return voucherName;
                }
            }
        }
        return getVoucherName();
    }
}
