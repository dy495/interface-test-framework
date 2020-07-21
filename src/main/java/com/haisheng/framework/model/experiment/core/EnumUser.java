package com.haisheng.framework.model.experiment.core;

import lombok.Getter;

/**
 * @author wangmin
 * @date 2020/7/21 14:46
 */
public enum EnumUser {

    WANG_MIN(new User.Builder().build());


    EnumUser(IUser user) {
        this.user = user;
    }

    @Getter
    private final IUser user;

//    EnumUser(String username, String password) {
//        user = new User.Builder().build();
//    }
}
