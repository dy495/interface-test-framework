package com.haisheng.framework.testng.bigScreen.crm.wm.base.command;

import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.exception.HttpProcessException;

public class UploadCommand extends BaseCommand{
    @Override
    public String buildRequest(HttpConfig config, Api api) {
        String[] files = {api.getRequestBody().getString("filePath")};
        config.files(files);
        try {
            return HttpClientUtil.post(config);
        } catch (HttpProcessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
