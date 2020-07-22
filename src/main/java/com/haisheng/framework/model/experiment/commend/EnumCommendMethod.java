package com.haisheng.framework.model.experiment.commend;

import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import lombok.Getter;

/**
 * 请求类型枚举
 *
 * @author wangmin
 * @date 2020/7/21 9:44
 */
public enum EnumCommendMethod {

    GET("get", "http", new GetCommend()),

    POST("post", "http", new PostCommend());


    EnumCommendMethod(String name, String protocol, BaseCommend command) {
        this.name = name;
        this.command = command;
        this.protocol = protocol;
    }

    @Getter
    private final String name;
    @Getter
    private final String protocol;
    @Getter
    private final BaseCommend command;

    /**
     * 根据command名获取指定的command类型
     *
     * @param typeName 类型名
     * @return EnumCommand 如果类型名为null，则默认返回get
     */
    public static EnumCommendMethod parse(String typeName) {
        if (!StringUtils.isEmpty(typeName)) {
            String name = typeName.toUpperCase();
            try {
                return Enum.valueOf(EnumCommendMethod.class, name);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        // 默认返回get
        return EnumCommendMethod.POST;
    }
}
