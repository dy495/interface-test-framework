package com.haisheng.framework.model.experiment.core;

import okhttp3.Response;
import org.jooq.util.derby.sys.Sys;

/**
 * @author wangmin
 * @date 2020/7/21 13:28
 */
public class User extends BaseUser {

    private User(Builder builder) {
        super(builder);
    }

    @Override
    public void setParams(Api api) {
        logger.info("api is:{}", api);
    }

    public static class Builder extends BaseBuilder {

        @Override
        protected IUser<Response> buildUser() {
            return new User(this);
        }
    }
}
