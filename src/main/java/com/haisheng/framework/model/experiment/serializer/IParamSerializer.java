package com.haisheng.framework.model.experiment.serializer;

import java.util.Map;

/**
 * 参数序列化器
 *
 * @author wangmin
 * @date 2020/7/21 13:20
 */
public interface IParamSerializer {
    /**
     * 将参数列表中的参数进行序列化
     *
     * @param params 参数列表
     * @return String 序列化后的参数串
     */
    String serialize(Map<String, String> params);

    /**
     * 设置是否进行urlencode
     *
     * @param encode
     */
    public void setUrlEncode(boolean encode);

    /**
     * 获取url encode
     *
     * @return boolean
     */
    public boolean getUrlEncode();
}
