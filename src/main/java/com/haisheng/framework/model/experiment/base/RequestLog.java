package com.haisheng.framework.model.experiment.base;

import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.haisheng.framework.model.experiment.core.Api;
import lombok.Data;
import lombok.experimental.Accessors;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

/**
 * 用于描述一次请求的log
 *
 * @author xiaolong.xxl
 * @date 2015年10月29日 下午4:30:39
 */
@Data
@Accessors(fluent = true, chain = true)
public class RequestLog<T> {
    public String getResult() {
        if (StringUtils.isEmpty(strResult)) {
            try {
                if (originResult instanceof String) {
                    this.strResult = (String) originResult;
                } else if (originResult instanceof Response) {
                    this.strResult = Objects.requireNonNull(((Response) originResult).body()).string();
                } else {
                    this.strResult = originResult.toString();
                }
            } catch (IOException e) {
                this.strResult = "Response body can not support String!";
            }
        }
        return this.strResult;
    }

    public byte[] getBytesResult() {
        if (byteResult == null) {
            try {
                if (originResult instanceof Response) {
                    this.byteResult = Objects.requireNonNull(((Response) originResult).body()).bytes();
                    return byteResult;
                }
            } catch (IOException e) {
                this.strResult = "Response body can not support bytes!";
            }
        }
        this.byteResult = new byte[0];
        return this.byteResult;
    }

    public long getResponseTime() {
        return endTime - startTime;
    }

    @Override
    public String toString() {
        return "Api Request Log:" + Constants.SYSTEM_LINE_SEPARATOR
                + "    -url : " + this.url + Constants.SYSTEM_LINE_SEPARATOR
                + "    -apiName : " + this.api.getApiName() + Constants.SYSTEM_LINE_SEPARATOR
                + "    -params : " + null + Constants.SYSTEM_LINE_SEPARATOR
                + "    -body : " + this.api.getRequestBody() + Constants.SYSTEM_LINE_SEPARATOR
                + "    -response code: " + this.responseCode() + Constants.SYSTEM_LINE_SEPARATOR
                + "    -result : " + getResult() + Constants.SYSTEM_LINE_SEPARATOR
                + "    -start time : " + startTime() + Constants.SYSTEM_LINE_SEPARATOR
                + "    -end time : " + endTime() + Constants.SYSTEM_LINE_SEPARATOR
                + "    -response time : " + getResponseTime() + "ms";
    }

    private Api api;
    private String responseCode = "-1";
    private T originResult;
    private String strResult;
    private String url;
    private byte[] byteResult;
    private long startTime;
    private long endTime;
}
