package com.haisheng.framework.util;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * @author xin.huang
 * @date 2019/9/9 11:23 上午
 */
public class OssClientUtil {

    private static String ACCESS_ID = "LTAIlYpjA39n18Yr";
    private static final String ACCESS_KEY = "fUPPfBIWeTKJp8oeVincGRjV5mt3Cg";
    private static OSSClient OSS_CLIENT;
    private static String BUCKET_NAME = "retail-huabei2";

    private static final long ExpirationTime = 3 * 86400 * 1000L;


    public OssClientUtil() {
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(500);
        conf.setSocketTimeout(3000);
        conf.setMaxErrorRetry(3);
        OSS_CLIENT = new OSSClient("http://oss-cn-beijing.aliyuncs.com", ACCESS_ID, ACCESS_KEY, conf);
    }

    public String genUrl(String ossKey) {
        return OSS_CLIENT.generatePresignedUrl(BUCKET_NAME, ossKey, new Date(System.currentTimeMillis() + ExpirationTime)).toString();
    }
}
