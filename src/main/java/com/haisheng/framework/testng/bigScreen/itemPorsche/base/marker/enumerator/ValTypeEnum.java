package com.haisheng.framework.testng.bigScreen.itemPorsche.base.marker.enumerator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author wangmin
 * @date 2021/3/10 14:34
 */
public enum ValTypeEnum {
    /**
     * 对象
     */
    OBJECT("object", 'O', JSONObject.class),
    /**
     * 数组
     */
    ARRAY("array", 'A', JSONArray.class),
    /**
     * 整形
     */
    INT("int", 'N', Integer.class),
    /**
     * 长整形
     */
    LONG("long", 'N', Long.class),
    /**
     * 浮点型
     */
    FLOAT("float", 'N', Float.class),
    /**
     * 长浮点型
     */
    DOUBLE("double", 'N', Double.class),
    /**
     * 字符串
     */
    STRING("string", 'C', String.class),
    /**
     * 浮点型
     */
    BOOLEAN("boolean", 'B', Boolean.class),
    /**
     * 形状类型
     */
    SHAPE("shape", 'S', null),
    /**
     * 其他
     */
    OTHERS("others", 'T', null);

    ValTypeEnum(String typeName, char shortName, Class type) {
        this.typeName = typeName;
        this.shortName = shortName;
        this.type = type;
    }

    @Getter
    private final String typeName;
    @Getter
    private final char shortName;
    @Getter
    private final Class type;

    public static ValTypeEnum findByTypeName(String typeName) {
        if (typeName.equals("int64")) {
            return ValTypeEnum.LONG;
        } else if (typeName.equals("int32")) {
            return ValTypeEnum.INT;
        }
        return Arrays.stream(ValTypeEnum.values()).filter(e -> e.getTypeName().equals(typeName)).findFirst().orElse(ValTypeEnum.STRING);
    }
}
