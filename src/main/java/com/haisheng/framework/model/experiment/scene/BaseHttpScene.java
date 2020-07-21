package com.haisheng.framework.model.experiment.scene;

import com.haisheng.framework.model.experiment.base.RequestLog;
import com.haisheng.framework.model.experiment.core.IUser;
import okhttp3.Response;

/**
 * HTTP接口的抽象类
 *
 * @author wangmin
 * @date 2020/7/20 21:05
 */
public abstract class BaseHttpScene implements IScene<Response> {
    private final IUser user;

    protected BaseHttpScene(BaseBuilder builder) {
        this.user = builder.user;
    }

    protected abstract RequestLog<Response> invokeApi();

    @Override
    public RequestLog<Response> run() {
//        if (user == null) {
//            try {
//                throw new IllegalAccessException("user为空，请初始化");
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
        return invokeApi();
    }

    @Override
    public IUser getUser() {
        return this.user;
    }

    public abstract static class BaseBuilder<T extends BaseBuilder> {
        private IUser user;

        public T user(IUser user) {
            this.user = user;
            return (T) this;
        }

        public abstract IScene<Response> build();
    }
}
