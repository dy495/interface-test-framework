package com.haisheng.framework.testng.bigScreen.itemcms.common.enumerator;

import com.haisheng.framework.testng.bigScreen.itemcms.common.scene.AiCameraAddScene;
import com.haisheng.framework.testng.bigScreen.itemcms.common.scene.DataDeviceScene;
import lombok.Getter;

/**
 * 摄像头类型枚举
 *
 * @author wangmin
 * @date 2021-07-29
 */
public enum EnumCameraType {
    AI(AiCameraAddScene.builder()),

    COMMON(DataDeviceScene.builder());

    EnumCameraType(Object scene) {
        this.scene = scene;
    }

    @Getter
    private final Object scene;
}
