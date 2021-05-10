package com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.attribute;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.enumerator.ValTypeEnum;
import com.haisheng.framework.util.CommonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 接口场景类模板需要的属性
 *
 * @author wangmin
 * @date 2021/3/8 14:32
 */
@Getter
public class ApiAttribute {
    private final String parameter;
    private final String type;
    private final String description;
    private final String required;
    private final String since;
    private final String buildParam;

    public ApiAttribute(Builder builder) {
        this.parameter = builder.parameter;
        this.type = ValTypeEnum.findByTypeName(builder.type).getType().getSimpleName();
        this.description = builder.description;
        this.required = builder.required;
        this.since = builder.since;
        this.buildParam = parameter.contains("-") || parameter.contains("_") ? CommonUtil.lineToHump(parameter, false) : parameter;
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder {
        private String parameter;
        private String type;
        private String description;
        private String required;
        private String since;

        public ApiAttribute build() {
            return new ApiAttribute(this);
        }
    }

    @Override
    public String toString() {
        return "SceneAttribute:{"
                + "parameter :" + parameter
                + "    type :" + type
                + "    description :" + description
                + "    required :" + required
                + "    since :" + since
                + "    buildParam :" + buildParam
                + "}";
    }
}
