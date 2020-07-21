package com.haisheng.framework.model.experiment.commend;

import com.haisheng.framework.model.experiment.core.Api;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * POST链接
 * 建立请求
 *
 * @author wangmin
 * @date 2020/7/21 9:51
 */
public class PostCommend extends BaseCommend {

    @Override
    public void buildRequest(Request.Builder builder, Api api) {
        MediaType mediaType = MediaType.parse(api.getMediaType());
        RequestBody requestBody = RequestBody.create(mediaType, api.getRequestBody());
        builder.post(requestBody);
    }
}
