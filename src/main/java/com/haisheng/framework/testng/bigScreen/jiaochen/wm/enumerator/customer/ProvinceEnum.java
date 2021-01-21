package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.customer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangmin
 * @date 2020/7/2 12:02 PM
 * @desc
 */
public enum ProvinceEnum {
    /**
     * 身份名称
     */
    京, 沪, 津, 渝, 鲁, 冀, 晋, 蒙, 辽, 吉, 黑, 苏, 浙, 皖, 闽, 赣, 豫, 湘, 鄂, 粤, 桂, 琼, 川, 贵, 云, 藏, 陕, 甘, 青, 宁, 新;

    public static List<Map<String, Object>> findList(){
        return Arrays.stream(values()).map(r -> {
            Map<String, Object> map = new HashMap<>(7);
            map.put("province", r.ordinal());
            map.put("province_name", r.name());
            return map;
        }).collect(Collectors.toList());
    }
}
