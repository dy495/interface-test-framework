package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.activity;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

    /**
     * 小程序-我的卡券----gly
     */
    @Builder
    public class appletVoucherDetailScene extends BaseScene {
        private final Integer lastValue;
        private  final Integer size;
        private  final Long id;
        private  final Integer customerId;
        private  final String verificationCode;
        private  final Integer showEndTime;
        private  final Integer voucherStatus;

        @Override
        public JSONObject getRequest() {
            JSONObject object = new JSONObject();
            object.put("size",size);
            object.put("last_value",lastValue);
            object.put("id",id);
            object.put("customer_id",customerId);
            object.put("verification_code",verificationCode);
            object.put("show_end_time",showEndTime);
            object.put("voucher_status",voucherStatus);
            return object;
        }
        @Override
        public String getPath() {
            return "/jiaochen/applet/granted/voucher/detail";
        }

        @Override
        public String getIpPort() {
            return null;
        }
}
