package com.haisheng.framework.model.experiment.core;

import com.haisheng.framework.model.experiment.base.RequestLog;
import com.haisheng.framework.model.experiment.urlbuilder.IUrlBuilder;
import com.haisheng.framework.model.experiment.urlbuilder.SimpleUrlBuilder;
import lombok.Data;
import okhttp3.Response;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * @author wangmin
 * @date 2020/7/21 13:04
 */
@Data
public abstract class BaseUser implements IUser<Response> {
    protected static final Logger logger = LoggerFactory.getLogger(BaseUser.class);

    protected BaseUser(BaseBuilder baseBuilder) {

    }

    /**
     * 放入参数
     *
     * @param api api
     */
    public abstract void setParams(Api api);

    @Override
    public RequestLog<Response> invoke(Api api, String address) throws IllegalAccessException {
        if (api == null || StringUtils.isEmpty(address))
            throw new IllegalAccessException("方法参数不能为空");
        IUrlBuilder urlBuilder = SimpleUrlBuilder.getInstance();
        String baseUrl = urlBuilder.build(address, api.getApiName());
        logger.info("url is:{}", baseUrl);
        RequestLog<Response> log = new RequestLog<>();
        setParams(api);
        //执行接口
        try {
            Response response = api.getMethod().getCommand().execute(api, baseUrl);
            log.responseCode(String.valueOf(response.code())).api(api).originResult(response);
        } catch (IOException e) {
            logger.error("接口请求出现异常：{}", e.getMessage());
        }
        return log;
    }

    public static abstract class BaseBuilder {
        protected abstract IUser buildUser();

        public IUser build() {
            return buildUser();
        }
    }
}
