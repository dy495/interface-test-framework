package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.reception.after;

import com.google.common.base.Preconditions;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 客户所属区域
 *
 * @author liujiachun
 * @date  2020/5/21  13:04
 */
public enum MaintainTypesEnum {

    /*****/

    MAINTAIN("保养", true, "MAINTAIN"),

    DETECTION("检测", false, ""),

    ACCIDENT("事故", false, ""),

    FIRST_MAINTAIN("首保", true, ""),

    REPAIR("维修", false, "REPAIR");

    private String name;

    private boolean isMaintain;

    private String appointmentType;

    MaintainTypesEnum(String name, boolean isMaintain, String appointmentType) {
        this.name = name;
        this.isMaintain = isMaintain;
        this.appointmentType = appointmentType;
    }

    public String getName() {
        return name;
    }

    public static MaintainTypesEnum findById(Integer id) {
        Preconditions.checkArgument(id != null, "维修类型不存在");
        Optional<MaintainTypesEnum> any = Arrays.stream(values()).filter(e -> e.ordinal() == id)
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "维修类型不存在");
        return any.get();
    }

    public static MaintainTypesEnum findByType(String type) {
        Optional<MaintainTypesEnum> any = Arrays.stream(values()).filter(e -> e.name().equals(type))
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "维修类型不存在");
        return any.get();
    }

    public static MaintainTypesEnum findByAppointmentType(String appointmentType) {
        Optional<MaintainTypesEnum> any = Arrays.stream(values())
                .filter(e -> e.appointmentType.equals(appointmentType))
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "维修类型不存在");
        return any.get();
    }


    public static boolean isMaintain(String type) {
        if (null == type) {
            return false;
        }
        MaintainTypesEnum maintainType = findByType(type);
        return maintainType.isMaintain;
    }

    public static List<String> repairTypes() {
        return Arrays.stream(values()).filter(t -> !t.isMaintain)
                .map(MaintainTypesEnum::name).collect(Collectors.toList());
    }


    public static List<Map<String, Object>> findList() {
        return Arrays.stream(values()).map(r -> {
            Map<String, Object> map = new HashMap<>(7);
            map.put("maintain_type", r.ordinal());
            map.put("maintain_type_name", r.getName());
            return map;
        }).collect(Collectors.toList());
    }

}
