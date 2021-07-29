package com.haisheng.framework.testng.bigScreen.itemcms.common.enumerator;

import com.haisheng.framework.testng.bigScreen.itemcms.common.scene.DataLayoutScene;
import lombok.Getter;

/**
 * 摄像头类型枚举
 *
 * @author wangmin
 * @date 2021-07-29
 */
public enum EnumPlaneType {
    FLOOR(DataLayoutScene.builder()),
    ;


//    AI(AiCameraAddScene.builder()),

//    COMMON(DataDeviceScene.builder());

    EnumPlaneType(Object scene) {
        this.scene = scene;
    }

    @Getter
    private final Object scene;
}
