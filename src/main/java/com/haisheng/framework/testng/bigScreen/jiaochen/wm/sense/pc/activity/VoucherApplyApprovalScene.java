package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

    /**
     * 卡券管理 -> 卡券表单
     */
    @Builder
    public class VoucherApplyApprovalScene extends BaseScene {
        private final Long id;
        private final Integer status;


        @Override
        public JSONObject getRequest() {
            JSONObject object = new JSONObject();
            object.put("id", id);
            object.put("status", status);
            return object;
        }

        @Override
        public String getPath() {
            return "/jiaochen/pc/voucher/apply/approval";
        }
        @Override
        public String getIpPort() {
            return null;
        }
    }
