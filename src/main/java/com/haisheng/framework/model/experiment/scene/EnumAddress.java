package com.haisheng.framework.model.experiment.scene;

import lombok.Getter;

/**
 * @author wangmin
 * @date 2020/7/21 14:30
 */
public enum EnumAddress {

    WIN_SENSE("http://dev.porsche.dealer-ydauto.winsenseos.cn");


    EnumAddress(String address) {
        this.address = address;
    }

    @Getter
    private final String address;
}
