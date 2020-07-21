package com.haisheng.framework.model.experiment.urlbuilder;

import java.util.Map;

/**
 * 普通的url拼接器的定义，将module.api形式的apiName，拼接成baseUrl/module/api的url，
 * 如果只包含api,则拼接成baseUrl/api
 *
 * @author wangmin
 * @date 2020/7/21 13:16
 */
public class SimpleUrlBuilder implements IUrlBuilder {
    @Override
    public String build(String address, String apiName) {
        StringBuilder sb = new StringBuilder(address);
        if (apiName != null) {
            if (!address.endsWith("/")) {
                sb.append("/");
            }
            if (apiName.contains(".")) {
                sb.append(apiName.replace('.', '/'));
            } else {
                sb.append(apiName);
            }
        }
        return sb.toString();
    }

    @Override
    public String build(String address, String apiName, String[] others) {
        return build(address, apiName);
    }

    @Override
    public String build(String address, String apiName, Map<String, String> others) {
        return build(address, apiName);
    }

    private SimpleUrlBuilder() {
    }

    private static class Holder {
        private final static IUrlBuilder INSTANCE = new SimpleUrlBuilder();
    }

    public static IUrlBuilder getInstance() {
        return Holder.INSTANCE;
    }
}
