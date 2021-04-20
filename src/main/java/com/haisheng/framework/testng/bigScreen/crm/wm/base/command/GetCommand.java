package com.haisheng.framework.testng.bigScreen.crm.wm.base.command;

import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import org.jetbrains.annotations.NotNull;

public class GetCommand extends BaseCommand {
    @Override
    public String buildRequest(@NotNull HttpConfig config, @NotNull Api api) {
        config.map(api.getUrlParams());
        try {
            return HttpClientUtil.get(config);
        } catch (HttpProcessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
