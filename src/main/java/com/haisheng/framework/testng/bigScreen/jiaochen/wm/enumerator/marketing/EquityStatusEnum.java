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
public enum EquityStatusEnum {

    /**
     * 开启
     */
    STARTED(1,"开启"),
    /**
     * 普通vip
     */
    COLSED(0,"关闭");


    private Integer code;

    private String desc;

    EquityStatusEnum(Integer code,String desc ){
        this.code = code ;
        this.desc = desc;
    }

    public Integer getCode(){
        return code;
    }
    public String getdesc(){
        return desc;
    }

    public static List<Map<String,Object>> findList(){
        return Arrays.stream(values()).map( v ->{
            Map<String,Object> map = new HashMap<>(4);
            map.put("key",v.name());
            map.put("value",v.getdesc());
            return map;
        }).collect(Collectors.toList());
    }

    public static EquityStatusEnum findByName(String name){
        Preconditions.checkArgument(StringUtils.isNotEmpty(name),"状态不存在");
        Optional<EquityStatusEnum> any = Arrays.asList(values()).stream().filter(t -> t.name().equals(name)).findAny();
        return any.orElse(null);
    }

    public static EquityStatusEnum findByCode(Integer code){
        Preconditions.checkArgument(code != null ,"状态不存在");
        Optional<EquityStatusEnum> any = Arrays.asList(values()).stream().filter(t -> t.getCode().equals(code)).findAny();
        return any.orElse(null);
    }

}
