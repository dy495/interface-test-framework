package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * vip类型
 * @author wangmin
 * @date 2020/11/12 20:27
 */
public enum EquityAwardCountUnitEnum {

    /**
     * 单位
     */
    TIME("次"),
    /**
     * 积分
     */
    SCORE("积分");


    private String value;

    EquityAwardCountUnitEnum(String value ){
        this.value = value ;
    }

    public String getValue(){
        return value;
    }

    public static List<Map<String,Object>> findList(){
        return Arrays.stream(values()).map( v ->{
            Map<String,Object> map = new HashMap<>(4);
            map.put("key",v.name());
            map.put("value",v.getValue());
            return map;
        }).collect(Collectors.toList());
    }

    public static EquityAwardCountUnitEnum findByName(String name){
        Preconditions.checkArgument(StringUtils.isNotEmpty(name),"会员类型不存在");
        Optional<EquityAwardCountUnitEnum> any = Arrays.asList(values()).stream().filter(t -> t.name().equals(name)).findAny();
        return any.orElse(null);
    }

}
