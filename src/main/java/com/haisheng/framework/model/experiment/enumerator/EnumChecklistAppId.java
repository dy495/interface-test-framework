package com.haisheng.framework.model.experiment.enumerator;

import lombok.Getter;

/**
 * Checklist平台id
 *
 * @author wangmin
 * @date 2020/7/24 14:37
 */
public enum EnumChecklistAppId {

    DB_APP_ID_EDGE_SERVICE(1),
    /**
     * 云端服务
     */
    DB_APP_ID_CLOUD_SERVICE(2),
    /**
     * 管理平台
     */
    DB_APP_ID_MANAGE_PORTAL_SERVICE(3),
    /**
     * 开放平台
     */
    DB_APP_ID_OPEN_PLATFORM_SERVICE(4),
    /**
     * 大屏展示
     */
    DB_APP_ID_SCREEN_SERVICE(5),
    /**
     * 货架商品
     */
    DB_APP_ID_SHELF_SERVICE(6);

    EnumChecklistAppId(int id) {
        this.id = id;
    }

    @Getter
    private final int id;
}
