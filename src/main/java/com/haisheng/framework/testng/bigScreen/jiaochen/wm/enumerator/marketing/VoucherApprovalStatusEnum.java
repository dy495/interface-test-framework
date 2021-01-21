package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangmin
 * @date 2020/12/23 10:57 AM
 * @desc
 */
public enum VoucherApprovalStatusEnum {
    /**
     * 审核中
     */
    AUDITING(0, "审核中"),
    /**
     * 同意
     */
    AGREE(1, "已通过"),
    /**
     * 拒绝
     */
    REFUSAL(2, "审核未通过"),
    /**
     * 取消
     */
    RECALL(3, "已撤回");

    private Integer id;
    private String name;

    VoucherApprovalStatusEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static VoucherApprovalStatusEnum findById(Integer id) {
        Preconditions.checkArgument(id != null, "审核状态不存在");
        Optional<VoucherApprovalStatusEnum> any = Arrays.stream(values()).filter(e -> e.getId().equals(id))
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "审核状态不存在");
        return any.get();
    }

    public static VoucherApprovalStatusEnum findByStatus(String status) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(status), "审核状态不存在");
        Optional<VoucherApprovalStatusEnum> any = Arrays.stream(values()).filter(e -> e.name().equals(status))
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "审核状态不存在");
        return any.get();
    }

    public static List<Map<String, Object>> findList() {
        return Arrays.stream(values()).map(r -> {
            Map<String, Object> map = new HashMap<>(8);
            map.put("audit_status", r.getId());
            map.put("audit_status_name", r.getName());
            return map;
        }).collect(Collectors.toList());
    }

    public static List<Map<String, Object>> findNameList() {
        return Arrays.stream(values()).map(r -> {
            Map<String, Object> map = new HashMap<>(8);
            map.put("value", r.getName());
            map.put("key", r.name());
            return map;
        }).collect(Collectors.toList());
    }
}
