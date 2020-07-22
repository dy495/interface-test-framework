package com.haisheng.framework.model.experiment.serializer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * HTTP请求参数的序列化
 *
 * @author wangmin
 * @date 2020/7/21 13:21
 */
public class HttpSerializer implements IParamSerializer {
    @Override
    public String serialize(Map<String, String> params) {
        StringBuilder resultBuilder = new StringBuilder();
        if (params != null) {
            int i = 0;
            for (String key : params.keySet()) {
                resultBuilder.append(key);
                resultBuilder.append("=");
                resultBuilder.append(params.get(key));
                if (++i < params.size()) {
                    resultBuilder.append("&");
                }
            }
            if (isEncode) {
                try {
                    return URLEncoder.encode(resultBuilder.toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                return resultBuilder.toString();
            }
        }
        return null;
    }

    private boolean isEncode = false;

    @Override
    public void setUrlEncode(boolean encode) {
        this.isEncode = encode;
    }

    @Override
    public boolean getUrlEncode() {
        return this.isEncode;
    }
}
